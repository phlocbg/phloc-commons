/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phloc.jms.pool;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.jms.JMSUtils;

/**
 * Holds a real JMS connection along with the session pools associated with it.
 * <p/>
 * Instances of this class are shared amongst one or more PooledConnection
 * object and must track the session objects that are loaned out for cleanup on
 * close as well as ensuring that the temporary destinations of the managed
 * Connection are purged when all references to this ConnectionPool are
 * released.
 */
public class ConnectionPool
{
  private Connection m_aConnection;
  private int m_nReferenceCount;
  private long m_nLastUsed = System.currentTimeMillis ();
  private final long m_nFirstUsed = m_nLastUsed;
  private boolean m_bHasFailed;
  private boolean m_bHasExpired;
  private int m_nIdleTimeout = 30 * 1000;
  private long m_nExpiryTimeout = 0l;

  private final AtomicBoolean m_aStarted = new AtomicBoolean (false);
  private final GenericKeyedObjectPool <SessionKey, PooledSession> m_aSessionPool;
  private final List <PooledSession> m_aLoanedSessions = new CopyOnWriteArrayList <PooledSession> ();

  public ConnectionPool (final Connection connection)
  {
    m_aConnection = connection;

    // [ActiveMQ]
    // // Add a transport Listener so that we can notice if this connection
    // // should be expired due to a connection failure.
    // connection.addTransportListener (new TransportListener ()
    // {
    // public void onCommand (final Object command)
    // {}
    //
    // public void onException (final IOException error)
    // {
    // synchronized (ConnectionPool.this)
    // {
    // hasFailed = true;
    // }
    // }
    //
    // public void transportInterupted ()
    // {}
    //
    // public void transportResumed ()
    // {}
    // });
    //
    // // make sure that we set the hasFailed flag, in case the transport
    // already
    // // failed
    // // prior to the addition of our new TransportListener
    // if (connection.isTransportFailed ())
    // {
    // hasFailed = true;
    // }

    // Create our internal Pool of session instances.
    m_aSessionPool = new GenericKeyedObjectPool <SessionKey, PooledSession> (new KeyedPoolableObjectFactory <SessionKey, PooledSession> ()
    {
      @Override
      public void activateObject (final SessionKey key, final PooledSession session) throws Exception
      {
        ConnectionPool.this.m_aLoanedSessions.add (session);
      }

      @Override
      public void destroyObject (final SessionKey key, final PooledSession session) throws Exception
      {
        ConnectionPool.this.m_aLoanedSessions.remove (session);
        session.getInternalSession ().close ();
      }

      @Override
      public PooledSession makeObject (final SessionKey key) throws Exception
      {
        final Session session = ConnectionPool.this.m_aConnection.createSession (key.isTransacted (), key.getAckMode ());
        return new PooledSession (key, session, m_aSessionPool);
      }

      @Override
      public void passivateObject (final SessionKey key, final PooledSession session) throws Exception
      {
        ConnectionPool.this.m_aLoanedSessions.remove (session);
      }

      @Override
      public boolean validateObject (final SessionKey key, final PooledSession session)
      {
        return true;
      }
    });
  }

  public void start () throws JMSException
  {
    if (m_aStarted.compareAndSet (false, true))
    {
      try
      {
        m_aConnection.start ();
      }
      catch (final JMSException e)
      {
        m_aStarted.set (false);
        throw (e);
      }
    }
  }

  @Nonnull
  public synchronized Connection getConnection ()
  {
    return m_aConnection;
  }

  public Session createSession (final boolean transacted, final int ackMode) throws JMSException
  {
    final SessionKey key = new SessionKey (transacted, ackMode);
    PooledSession session;
    try
    {
      session = m_aSessionPool.borrowObject (key);
    }
    catch (final Exception e)
    {
      throw JMSUtils.createException ("Failed to borrow session", e);
    }
    return session;
  }

  public synchronized void close ()
  {
    if (m_aConnection != null)
    {
      try
      {
        m_aSessionPool.close ();
      }
      catch (final Exception e)
      {}
      finally
      {
        JMSUtils.close (m_aConnection);
        m_aConnection = null;
      }
    }
  }

  public synchronized void incrementReferenceCount ()
  {
    m_nReferenceCount++;
    m_nLastUsed = System.currentTimeMillis ();
  }

  @OverrideOnDemand
  protected void onDecrementReferenceCount ()
  {}

  public synchronized void decrementReferenceCount ()
  {
    m_nReferenceCount--;
    m_nLastUsed = System.currentTimeMillis ();
    if (m_nReferenceCount == 0)
    {
      expiredCheck ();

      // Loaned sessions are those that are active in the sessionPool and
      // have not been closed by the client before closing the connection.
      // These need to be closed so that all session's reflect the fact
      // that the parent Connection is closed.
      for (final PooledSession session : m_aLoanedSessions)
      {
        try
        {
          session.close ();
        }
        catch (final Exception e)
        {}
      }
      m_aLoanedSessions.clear ();

      // [ActiveMQ]
      // We only clean up temporary destinations when all users of this
      // connection have called close.
      // if (getConnection () != null)
      // {
      // getConnection ().cleanUpTempDestinations ();
      // }
      onDecrementReferenceCount ();
    }
  }

  /**
   * Determines if this Connection has expired.
   * <p/>
   * A ConnectionPool is considered expired when all references to it are
   * released AND either the configured idleTimeout has elapsed OR the
   * configured expiryTimeout has elapsed. Once a ConnectionPool is determined
   * to have expired its underlying Connection is closed.
   * 
   * @return true if this connection has expired.
   */
  public synchronized boolean expiredCheck ()
  {
    if (m_aConnection == null)
    {
      return true;
    }

    if (m_bHasExpired)
    {
      if (m_nReferenceCount == 0)
      {
        close ();
      }
      return true;
    }

    if (m_bHasFailed ||
        (m_nIdleTimeout > 0 && System.currentTimeMillis () > m_nLastUsed + m_nIdleTimeout) ||
        m_nExpiryTimeout > 0 &&
        System.currentTimeMillis () > m_nFirstUsed + m_nExpiryTimeout)
    {

      m_bHasExpired = true;
      if (m_nReferenceCount == 0)
      {
        close ();
      }
      return true;
    }
    return false;
  }

  public int getIdleTimeout ()
  {
    return m_nIdleTimeout;
  }

  public void setIdleTimeout (final int idleTimeout)
  {
    m_nIdleTimeout = idleTimeout;
  }

  public void setExpiryTimeout (final long expiryTimeout)
  {
    m_nExpiryTimeout = expiryTimeout;
  }

  public long getExpiryTimeout ()
  {
    return m_nExpiryTimeout;
  }

  public int getMaximumActiveSessionPerConnection ()
  {
    return m_aSessionPool.getMaxActive ();
  }

  public void setMaximumActiveSessionPerConnection (final int maximumActiveSessionPerConnection)
  {
    m_aSessionPool.setMaxActive (maximumActiveSessionPerConnection);
  }

  /**
   * @return the total number of Pooled session including idle sessions that are
   *         not currently loaned out to any client.
   */
  public int getNumSessions ()
  {
    return m_aSessionPool.getNumIdle () + m_aSessionPool.getNumActive ();
  }

  /**
   * @return the total number of Sessions that are in the Session pool but not
   *         loaned out.
   */
  public int getNumIdleSessions ()
  {
    return m_aSessionPool.getNumIdle ();
  }

  /**
   * @return the total number of Session's that have been loaned to
   *         PooledConnection instances.
   */
  public int getNumActiveSessions ()
  {
    return m_aSessionPool.getNumActive ();
  }

  /**
   * Configure whether the createSession method should block when there are no
   * more idle sessions and the pool already contains the maximum number of
   * active sessions. If false the create method will fail and throw an
   * exception.
   * 
   * @param block
   *        Indicates whether blocking should be used to wait for more space to
   *        create a session.
   */
  public void setBlockIfSessionPoolIsFull (final boolean block)
  {
    m_aSessionPool.setWhenExhaustedAction ((block ? GenericObjectPool.WHEN_EXHAUSTED_BLOCK
                                                 : GenericObjectPool.WHEN_EXHAUSTED_FAIL));
  }

  public boolean isBlockIfSessionPoolIsFull ()
  {
    return m_aSessionPool.getWhenExhaustedAction () == GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
  }

  @Override
  public String toString ()
  {
    return "ConnectionPool[" + m_aConnection + "]";
  }
}
