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

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a proxy {@link Connection} which is-a {@link TopicConnection} and
 * {@link QueueConnection} which is pooled and on {@link #close()} will return
 * its reference to the ConnectionPool backing it. <b>NOTE</b> this
 * implementation is only intended for use when sending messages. It does not
 * deal with pooling of consumers; for that look at a library like <a
 * href="http://jencks.org/">Jencks</a> such as in <a
 * href="http://jencks.org/Message+Driven+POJOs">this example</a>
 */
public class PooledConnection implements TopicConnection, QueueConnection, IPooledSessionEventListener
{
  private static final transient Logger s_aLogger = LoggerFactory.getLogger (PooledConnection.class);

  private ConnectionPool m_aPool;
  private volatile boolean m_bStopped;
  private final List <TemporaryQueue> m_aConnTempQueues = new CopyOnWriteArrayList <TemporaryQueue> ();
  private final List <TemporaryTopic> m_aConnTempTopics = new CopyOnWriteArrayList <TemporaryTopic> ();
  private final List <PooledSession> m_aLoanedSessions = new CopyOnWriteArrayList <PooledSession> ();

  /**
   * Creates a new PooledConnection instance that uses the given ConnectionPool
   * to create and manage its resources. The ConnectionPool instance can be
   * shared amongst many PooledConnection instances.
   * 
   * @param pool
   *        The connection and pool manager backing this proxy connection
   *        object.
   */
  public PooledConnection (@Nonnull final ConnectionPool pool)
  {
    m_aPool = pool;
    m_aPool.incrementReferenceCount ();
  }

  /**
   * Factory method to create a new instance.
   */
  public PooledConnection newInstance ()
  {
    return new PooledConnection (m_aPool);
  }

  @Override
  public void close () throws JMSException
  {
    cleanupConnectionTemporaryDestinations ();
    cleanupAllLoanedSessions ();
    if (m_aPool != null)
    {
      m_aPool.decrementReferenceCount ();
      m_aPool = null;
    }
  }

  @Override
  public void start () throws JMSException
  {
    assertNotClosed ();
    m_aPool.start ();
  }

  @Override
  public void stop () throws JMSException
  {
    m_bStopped = true;
  }

  @Override
  public ConnectionConsumer createConnectionConsumer (final Destination destination,
                                                      final String messageSelector,
                                                      final ServerSessionPool sessionPool,
                                                      final int maxMessages) throws JMSException
  {
    return getConnection ().createConnectionConsumer (destination, messageSelector, sessionPool, maxMessages);
  }

  @Override
  public ConnectionConsumer createConnectionConsumer (final Topic topic,
                                                      final String messageSelector,
                                                      final ServerSessionPool sessionPool,
                                                      final int maxMessages) throws JMSException
  {
    return getConnection ().createConnectionConsumer (topic, messageSelector, sessionPool, maxMessages);
  }

  @Override
  public ConnectionConsumer createDurableConnectionConsumer (final Topic topic,
                                                             final String subscriptionName,
                                                             final String messageSelector,
                                                             final ServerSessionPool serverSessionPool,
                                                             final int maxMessages) throws JMSException
  {
    return getConnection ().createDurableConnectionConsumer (topic,
                                                             subscriptionName,
                                                             messageSelector,
                                                             serverSessionPool,
                                                             maxMessages);
  }

  @Override
  public String getClientID () throws JMSException
  {
    return getConnection ().getClientID ();
  }

  @Override
  public ExceptionListener getExceptionListener () throws JMSException
  {
    return getConnection ().getExceptionListener ();
  }

  @Override
  public ConnectionMetaData getMetaData () throws JMSException
  {
    return getConnection ().getMetaData ();
  }

  @Override
  public void setExceptionListener (final ExceptionListener exceptionListener) throws JMSException
  {
    getConnection ().setExceptionListener (exceptionListener);
  }

  @Override
  public void setClientID (final String clientID) throws JMSException
  {

    // ignore repeated calls to setClientID() with the same client id
    // this could happen when a JMS component such as Spring that uses a
    // PooledConnectionFactory shuts down and reinitializes.
    if (this.getConnection ().getClientID () == null || !this.getClientID ().equals (clientID))
    {
      getConnection ().setClientID (clientID);
    }
  }

  @Override
  public ConnectionConsumer createConnectionConsumer (final Queue queue,
                                                      final String selector,
                                                      final ServerSessionPool serverSessionPool,
                                                      final int maxMessages) throws JMSException
  {
    return getConnection ().createConnectionConsumer (queue, selector, serverSessionPool, maxMessages);
  }

  // Session factory methods
  // -------------------------------------------------------------------------
  @Override
  public QueueSession createQueueSession (final boolean transacted, final int ackMode) throws JMSException
  {
    return (QueueSession) createSession (transacted, ackMode);
  }

  @Override
  public TopicSession createTopicSession (final boolean transacted, final int ackMode) throws JMSException
  {
    return (TopicSession) createSession (transacted, ackMode);
  }

  @Override
  public Session createSession (final boolean transacted, final int ackMode) throws JMSException
  {
    PooledSession result;
    result = (PooledSession) m_aPool.createSession (transacted, ackMode);

    // Store the session so we can close the sessions that this PooledConnection
    // created in order to ensure that consumers etc are closed per the JMS
    // contract.
    m_aLoanedSessions.add (result);

    // Add a event listener to the session that notifies us when the session
    // creates / destroys temporary destinations and closes etc.
    result.addSessionEventListener (this);
    return result;
  }

  // Implementation methods
  // -------------------------------------------------------------------------

  @Override
  public void onTemporaryQueueCreate (final TemporaryQueue tempQueue)
  {
    m_aConnTempQueues.add (tempQueue);
  }

  @Override
  public void onTemporaryTopicCreate (final TemporaryTopic tempTopic)
  {
    m_aConnTempTopics.add (tempTopic);
  }

  @Override
  public void onSessionClosed (final PooledSession session)
  {
    if (session != null)
      m_aLoanedSessions.remove (session);
  }

  public Connection getConnection () throws JMSException
  {
    assertNotClosed ();
    return m_aPool.getConnection ();
  }

  protected void assertNotClosed () throws JMSException
  {
    if (m_bStopped || m_aPool == null)
      throw new JMSException ("Already closed!");
  }

  protected Session createSession (@Nonnull final SessionKey key) throws JMSException
  {
    return getConnection ().createSession (key.isTransacted (), key.getAckMode ());
  }

  @Override
  public String toString ()
  {
    return "PooledConnection { " + m_aPool + " }";
  }

  /**
   * Remove all of the temporary destinations created for this connection. This
   * is important since the underlying connection may be reused over a long
   * period of time, accumulating all of the temporary destinations from each
   * use. However, from the perspective of the lifecycle from the client's view,
   * close() closes the connection and, therefore, deletes all of the temporary
   * destinations created.
   */
  protected void cleanupConnectionTemporaryDestinations ()
  {
    for (final TemporaryQueue tempQueue : m_aConnTempQueues)
    {
      try
      {
        tempQueue.delete ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.info ("failed to delete Temporary Queue \"" +
                        tempQueue.toString () +
                        "\" on closing pooled connection: " +
                        ex.getMessage ());
      }
    }
    m_aConnTempQueues.clear ();

    for (final TemporaryTopic tempTopic : m_aConnTempTopics)
    {
      try
      {
        tempTopic.delete ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.info ("failed to delete Temporary Topic \"" +
                        tempTopic.toString () +
                        "\" on closing pooled connection: " +
                        ex.getMessage ());
      }
    }
    m_aConnTempTopics.clear ();
  }

  /**
   * The PooledSession tracks all Sessions that it created and now we close
   * them. Closing the PooledSession will return the internal Session to the
   * Pool of Session after cleaning up all the resources that the Session had
   * allocated for this PooledConnection.
   */
  protected void cleanupAllLoanedSessions ()
  {
    for (final PooledSession session : m_aLoanedSessions)
    {
      try
      {
        session.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.info ("failed to close laoned Session \"" +
                        session +
                        "\" on closing pooled connection: " +
                        ex.getMessage ());
      }
    }
    m_aLoanedSessions.clear ();
  }

  /**
   * @return the total number of Pooled session including idle sessions that are
   *         not currently loaned out to any client.
   */
  public int getNumSessions ()
  {
    return m_aPool.getNumSessions ();
  }

  /**
   * @return the number of Sessions that are currently checked out of this
   *         Connection's session pool.
   */
  public int getNumActiveSessions ()
  {
    return m_aPool.getNumActiveSessions ();
  }

  /**
   * @return the number of Sessions that are idle in this Connection's sessions
   *         pool.
   */
  public int getNumtIdleSessions ()
  {
    return m_aPool.getNumIdleSessions ();
  }
}
