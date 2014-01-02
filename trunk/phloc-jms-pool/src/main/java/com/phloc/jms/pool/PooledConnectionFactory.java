/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.jms.pool;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.jms.JMSUtils;

/**
 * A JMS provider which pools Connection, Session and MessageProducer instances
 * so it can be used with tools like <a
 * href="http://camel.apache.org/activemq.html">Camel</a> and Spring's <a
 * href="http://activemq.apache.org/spring-support.html">JmsTemplate and
 * MessagListenerContainer</a>. Connections, sessions and producers are returned
 * to a pool after use so that they can be reused later without having to
 * undergo the cost of creating them again. b>NOTE:</b> while this
 * implementation does allow the creation of a collection of active consumers,
 * it does not 'pool' consumers. Pooling makes sense for connections, sessions
 * and producers, which are expensive to create and can remain idle a minimal
 * cost. Consumers, on the other hand, are usually just created at startup and
 * left active, handling incoming messages as they come. When a consumer is
 * complete, it is best to close it rather than return it to a pool for later
 * reuse: this is because, even if a consumer is idle, ActiveMQ will keep
 * delivering messages to the consumer's prefetch buffer, where they'll get held
 * until the consumer is active again. If you are creating a collection of
 * consumers (for example, for multi-threaded message consumption), you might
 * want to consider using a lower prefetch value for each consumer (e.g. 10 or
 * 20), to ensure that all messages don't end up going to just one of the
 * consumers. See this FAQ entry for more detail:
 * http://activemq.apache.org/i-do
 * -not-receive-messages-in-my-second-consumer.html Optionally, one may
 * configure the pool to examine and possibly evict objects as they sit idle in
 * the pool. This is performed by an "idle object eviction" thread, which runs
 * asynchronously. Caution should be used when configuring this optional
 * feature. Eviction runs contend with client threads for access to objects in
 * the pool, so if they run too frequently performance issues may result. The
 * idle object eviction thread may be configured using the
 * {@link #setTimeBetweenExpirationCheckMillis()} method. By default the value
 * is -1 which means no eviction thread will be run. Set to a non-negative value
 * to configure the idle eviction thread to run.
 * 
 * @org.apache.xbean.XBean element="pooledConnectionFactory"
 */
public class PooledConnectionFactory implements ConnectionFactory
{
  private static final transient Logger s_aLogger = LoggerFactory.getLogger (PooledConnectionFactory.class);

  private final AtomicBoolean m_aStopped = new AtomicBoolean (false);
  private final GenericKeyedObjectPool <ConnectionKey, ConnectionPool> m_aConnectionsPool;

  private ConnectionFactory m_aConnectionFactory;

  private int m_nMaximumActiveSessionPerConnection = 500;
  private int m_nIdleTimeout = 30 * 1000;
  private boolean m_bBlockIfSessionPoolIsFull = true;
  private long m_nExpiryTimeout = 0l;
  private boolean m_bCreateConnectionOnStartup = true;

  /**
   * Creates a new PooledConnectionFactory that will use the given
   * ConnectionFactory to create new Connection instances that will be pooled.
   * 
   * @param connectionFactory
   *        The ConnectionFactory to create new Connections for this pool.
   */
  public PooledConnectionFactory (@Nonnull final ConnectionFactory connectionFactory)
  {
    m_aConnectionFactory = connectionFactory;

    m_aConnectionsPool = new GenericKeyedObjectPool <ConnectionKey, ConnectionPool> (new KeyedPoolableObjectFactory <ConnectionKey, ConnectionPool> ()
    {
      public void activateObject (final ConnectionKey key, final ConnectionPool connection) throws Exception
      {}

      public void destroyObject (final ConnectionKey key, final ConnectionPool connection) throws Exception
      {
        try
        {
          if (s_aLogger.isTraceEnabled ())
            s_aLogger.trace ("Destroying connection " + connection);
          connection.close ();
        }
        catch (final Exception e)
        {
          s_aLogger.warn ("Close connection failed for connection: " + connection + ". This exception will be ignored.",
                          e);
        }
      }

      public ConnectionPool makeObject (final ConnectionKey key) throws Exception
      {
        final Connection delegate = createConnection (key);

        final ConnectionPool connection = createConnectionPool (delegate);
        connection.setIdleTimeout (getIdleTimeout ());
        connection.setExpiryTimeout (getExpiryTimeout ());
        connection.setMaximumActiveSessionPerConnection (getMaximumActiveSessionPerConnection ());
        connection.setBlockIfSessionPoolIsFull (isBlockIfSessionPoolIsFull ());

        if (s_aLogger.isTraceEnabled ())
          s_aLogger.trace ("Created new connection " + connection);

        return connection;
      }

      public void passivateObject (final ConnectionKey key, final ConnectionPool connection) throws Exception
      {}

      public boolean validateObject (final ConnectionKey key, final ConnectionPool connection)
      {
        if (connection != null && connection.expiredCheck ())
        {
          if (s_aLogger.isTraceEnabled ())
            s_aLogger.trace ("Connection has expired (" + connection + ") and will be destroyed");
          return false;
        }

        return true;
      }
    });

    // Set max idle (not max active) since our connections always idle in the
    // pool.
    m_aConnectionsPool.setMaxIdle (1);

    // We always want our validate method to control when idle objects are
    // evicted.
    m_aConnectionsPool.setTestOnBorrow (true);
    m_aConnectionsPool.setTestWhileIdle (true);
  }

  /**
   * @return the currently configured ConnectionFactory used to create the
   *         pooled Connections.
   */
  @Nonnull
  public ConnectionFactory getConnectionFactory ()
  {
    return m_aConnectionFactory;
  }

  /**
   * Sets the ConnectionFactory used to create new pooled Connections.
   * <p/>
   * Updates to this value do not affect Connections that were previously
   * created and placed into the pool. In order to allocate new Connections
   * based off this new ConnectionFactory it is first necessary to
   * {@link #clear()} the pooled Connections.
   * 
   * @param connectionFactory
   *        The factory to use to create pooled Connections.
   */
  public void setConnectionFactory (@Nonnull final ConnectionFactory connectionFactory)
  {
    m_aConnectionFactory = connectionFactory;
  }

  public Connection createConnection () throws JMSException
  {
    return createConnection (null, null);
  }

  public synchronized Connection createConnection (final String userName, final String password) throws JMSException
  {
    if (m_aStopped.get ())
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("PooledConnectionFactory is stopped, skip create new connection.");
      return null;
    }

    ConnectionPool connection = null;
    final ConnectionKey key = new ConnectionKey (userName, password);

    // This will either return an existing non-expired ConnectionPool or it
    // will create a new one to meet the demand.
    if (m_aConnectionsPool.getNumIdle (key) < getMaxConnections ())
    {
      try
      {
        // we want borrowObject to return the one we added.
        m_aConnectionsPool.setLifo (true);
        m_aConnectionsPool.addObject (key);
      }
      catch (final Exception e)
      {
        throw JMSUtils.createException ("Error while attempting to add new Connection to the pool", e);
      }
    }
    else
    {
      // now we want the oldest one in the pool.
      m_aConnectionsPool.setLifo (false);
    }

    try
    {
      connection = m_aConnectionsPool.borrowObject (key);
    }
    catch (final Exception e)
    {
      throw JMSUtils.createException ("Error while attempting to retrieve a connection from the pool", e);
    }

    try
    {
      m_aConnectionsPool.returnObject (key, connection);
    }
    catch (final Exception e)
    {
      throw JMSUtils.createException ("Error when returning connection to the pool", e);
    }

    return new PooledConnection (connection);
  }

  protected Connection createConnection (final ConnectionKey key) throws JMSException
  {
    if (key.getUserName () == null && key.getPassword () == null)
      return m_aConnectionFactory.createConnection ();
    return m_aConnectionFactory.createConnection (key.getUserName (), key.getPassword ());
  }

  public void start ()
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Staring the PooledConnectionFactory: create on start = " + isCreateConnectionOnStartup ());

    m_aStopped.set (false);
    if (isCreateConnectionOnStartup ())
    {
      try
      {
        // warm the pool by creating a connection during startup
        createConnection ();
      }
      catch (final JMSException e)
      {
        s_aLogger.warn ("Create pooled connection during start failed. This exception will be ignored.", e);
      }
    }
  }

  public void stop ()
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Stopping the PooledConnectionFactory, number of connections in cache: " +
                       m_aConnectionsPool.getNumActive ());

    if (m_aStopped.compareAndSet (false, true))
    {
      try
      {
        m_aConnectionsPool.close ();
      }
      catch (final Exception e)
      {}
    }
  }

  /**
   * Clears all connections from the pool. Each connection that is currently in
   * the pool is closed and removed from the pool. A new connection will be
   * created on the next call to {@link #createConnection()}. Care should be
   * taken when using this method as Connections that are in use be client's
   * will be closed.
   */
  public void clear ()
  {
    if (m_aStopped.get ())
      return;

    m_aConnectionsPool.clear ();
  }

  /**
   * Returns the currently configured maximum number of sessions a pooled
   * Connection will create before it either blocks or throws an exception when
   * a new session is requested, depending on configuration.
   * 
   * @return the number of session instances that can be taken from a pooled
   *         connection.
   */
  public int getMaximumActiveSessionPerConnection ()
  {
    return m_nMaximumActiveSessionPerConnection;
  }

  /**
   * Sets the maximum number of active sessions per connection
   * 
   * @param maximumActiveSessionPerConnection
   *        The maximum number of active session per connection in the pool.
   */
  public void setMaximumActiveSessionPerConnection (final int maximumActiveSessionPerConnection)
  {
    m_nMaximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
  }

  /**
   * Controls the behavior of the internal session pool. By default the call to
   * Connection.getSession() will block if the session pool is full. If the
   * argument false is given, it will change the default behavior and instead
   * the call to getSession() will throw a JMSException. The size of the session
   * pool is controlled by the @see #maximumActive property.
   * 
   * @param block
   *        - if true, the call to getSession() blocks if the pool is full until
   *        a session object is available. defaults to true.
   */
  public void setBlockIfSessionPoolIsFull (final boolean block)
  {
    m_bBlockIfSessionPoolIsFull = block;
  }

  /**
   * Returns whether a pooled Connection will enter a blocked state or will
   * throw an Exception once the maximum number of sessions has been borrowed
   * from the the Session Pool.
   * 
   * @return true if the pooled Connection createSession method will block when
   *         the limit is hit.
   * @see #setBlockIfSessionPoolIsFull(boolean)
   */
  public boolean isBlockIfSessionPoolIsFull ()
  {
    return m_bBlockIfSessionPoolIsFull;
  }

  /**
   * Returns the maximum number to pooled Connections that this factory will
   * allow before it begins to return connections from the pool on calls to (
   * {@link #createConnection()}.
   * 
   * @return the maxConnections that will be created for this pool.
   */
  public int getMaxConnections ()
  {
    return m_aConnectionsPool.getMaxIdle ();
  }

  /**
   * Sets the maximum number of pooled Connections (defaults to one). Each call
   * to {@link #createConnection()} will result in a new Connection being create
   * up to the max connections value.
   * 
   * @param maxConnections
   *        the maxConnections to set
   */
  public void setMaxConnections (final int maxConnections)
  {
    m_aConnectionsPool.setMaxIdle (maxConnections);
  }

  /**
   * Gets the Idle timeout value applied to new Connection's that are created by
   * this pool.
   * <p/>
   * The idle timeout is used determine if a Connection instance has sat to long
   * in the pool unused and if so is closed and removed from the pool. The
   * default value is 30 seconds.
   * 
   * @return idle timeout
   */
  public int getIdleTimeout ()
  {
    return m_nIdleTimeout;
  }

  /**
   * Sets the idle timeout value for Connection's that are created by this pool,
   * defaults to 30 seconds.
   * <p/>
   * For a Connection that is in the pool but has no current users the idle
   * timeout determines how long the Connection can live before it is eligible
   * for removal from the pool. Normally the connections are tested when an
   * attempt to check one out occurs so a Connection instance can sit in the
   * pool much longer than its idle timeout if connections are used
   * infrequently.
   * 
   * @param idleTimeout
   *        The maximum time a pooled Connection can sit unused before it is
   *        eligible for removal.
   */
  public void setIdleTimeout (final int idleTimeout)
  {
    m_nIdleTimeout = idleTimeout;
  }

  /**
   * allow connections to expire, irrespective of load or idle time. This is
   * useful with failover to force a reconnect from the pool, to reestablish
   * load balancing or use of the master post recovery
   * 
   * @param expiryTimeout
   *        non zero in milliseconds
   */
  public void setExpiryTimeout (final long expiryTimeout)
  {
    m_nExpiryTimeout = expiryTimeout;
  }

  /**
   * @return the configured expiration timeout for connections in the pool.
   */
  public long getExpiryTimeout ()
  {
    return m_nExpiryTimeout;
  }

  /**
   * @return true if a Connection is created immediately on a call to
   *         {@link #start()}.
   */
  public boolean isCreateConnectionOnStartup ()
  {
    return m_bCreateConnectionOnStartup;
  }

  /**
   * Whether to create a connection on starting this
   * {@link PooledConnectionFactory}.
   * <p/>
   * This can be used to warm-up the pool on startup. Notice that any kind of
   * exception happens during startup is logged at WARN level and ignored.
   * 
   * @param createConnectionOnStartup
   *        <tt>true</tt> to create a connection on startup
   */
  public void setCreateConnectionOnStartup (final boolean createConnectionOnStartup)
  {
    m_bCreateConnectionOnStartup = createConnectionOnStartup;
  }

  /**
   * Gets the Pool of ConnectionPool instances which are keyed by different
   * ConnectionKeys.
   * 
   * @return this factories pool of ConnectionPool instances.
   */
  KeyedObjectPool <ConnectionKey, ConnectionPool> getConnectionsPool ()
  {
    return m_aConnectionsPool;
  }

  /**
   * Sets the number of milliseconds to sleep between runs of the idle
   * Connection eviction thread. When non-positive, no idle object eviction
   * thread will be run, and Connections will only be checked on borrow to
   * determine if they have sat idle for too long or have failed for some other
   * reason.
   * <p/>
   * By default this value is set to -1 and no expiration thread ever runs.
   * 
   * @param timeBetweenExpirationCheckMillis
   *        The time to wait between runs of the idle Connection eviction
   *        thread.
   */
  public void setTimeBetweenExpirationCheckMillis (final long timeBetweenExpirationCheckMillis)
  {
    m_aConnectionsPool.setTimeBetweenEvictionRunsMillis (timeBetweenExpirationCheckMillis);
  }

  /**
   * @return the number of milliseconds to sleep between runs of the idle
   *         connection eviction thread.
   */
  public long setTimeBetweenExpirationCheckMillis ()
  {
    return m_aConnectionsPool.getTimeBetweenEvictionRunsMillis ();
  }

  /**
   * @return the number of Connections currently in the Pool
   */
  public int getNumConnections ()
  {
    return m_aConnectionsPool.getNumIdle ();
  }

  /**
   * Delegate that creates each instance of an ConnectionPool object. Subclasses
   * can override this method to customize the type of connection pool returned.
   * 
   * @param connection
   * @return instance of a new ConnectionPool.
   * @throws JMSException
   *         In case of a JMS error
   */
  protected ConnectionPool createConnectionPool (final Connection connection) throws JMSException
  {
    return new ConnectionPool (connection);
  }
}
