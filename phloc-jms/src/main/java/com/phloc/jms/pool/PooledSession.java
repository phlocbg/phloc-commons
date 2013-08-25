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

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.XASession;
import javax.transaction.xa.XAResource;

import org.apache.commons.pool.KeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.jms.JMSUtils;

public class PooledSession implements TopicSession, QueueSession
{
  private static final transient Logger LOG = LoggerFactory.getLogger (PooledSession.class);

  private final SessionKey m_aKey;
  private final KeyedObjectPool <SessionKey, PooledSession> m_aSessionPool;
  private final CopyOnWriteArrayList <MessageConsumer> m_aConsumers = new CopyOnWriteArrayList <MessageConsumer> ();
  private final CopyOnWriteArrayList <QueueBrowser> m_aBrowsers = new CopyOnWriteArrayList <QueueBrowser> ();
  private final CopyOnWriteArrayList <IPooledSessionEventListener> m_aSessionEventListeners = new CopyOnWriteArrayList <IPooledSessionEventListener> ();

  private Session m_aSession;
  private MessageProducer m_aMessageProducer;
  private QueueSender m_aQueueSender;
  private TopicPublisher m_aTopicPublisher;
  private boolean m_bTransactional = true;
  private boolean m_bIgnoreClose;
  private boolean m_bIsXA;

  public PooledSession (final SessionKey key,
                        final Session session,
                        final KeyedObjectPool <SessionKey, PooledSession> sessionPool)
  {
    this.m_aKey = key;
    this.m_aSession = session;
    this.m_aSessionPool = sessionPool;
    this.m_bTransactional = key.isTransacted ();
  }

  public void addSessionEventListener (final IPooledSessionEventListener listener)
  {
    // only add if really needed
    if (!m_aSessionEventListeners.contains (listener))
    {
      this.m_aSessionEventListeners.add (listener);
    }
  }

  protected boolean isIgnoreClose ()
  {
    return m_bIgnoreClose;
  }

  protected void setIgnoreClose (final boolean ignoreClose)
  {
    m_bIgnoreClose = ignoreClose;
  }

  public void close () throws JMSException
  {
    if (!m_bIgnoreClose)
    {
      boolean invalidate = false;
      try
      {
        // lets reset the session
        getInternalSession ().setMessageListener (null);

        // Close any consumers and browsers that may have been created.
        for (final MessageConsumer consumer : m_aConsumers)
        {
          consumer.close ();
        }

        for (final QueueBrowser browser : m_aBrowsers)
        {
          browser.close ();
        }

        if (m_bTransactional && !m_bIsXA)
        {
          try
          {
            getInternalSession ().rollback ();
          }
          catch (final JMSException e)
          {
            invalidate = true;
            LOG.warn ("Caught exception trying rollback() when putting session back into the pool, will invalidate. " +
                      e, e);
          }
        }
      }
      catch (final JMSException ex)
      {
        invalidate = true;
        LOG.warn ("Caught exception trying close() when putting session back into the pool, will invalidate. " + ex, ex);
      }
      finally
      {
        m_aConsumers.clear ();
        m_aBrowsers.clear ();
        for (final IPooledSessionEventListener listener : this.m_aSessionEventListeners)
        {
          listener.onSessionClosed (this);
        }
        m_aSessionEventListeners.clear ();
      }

      if (invalidate)
      {
        // lets close the session and not put the session back into the pool
        // instead invalidate it so the pool can create a new one on demand.
        if (m_aSession != null)
        {
          try
          {
            m_aSession.close ();
          }
          catch (final JMSException e1)
          {
            LOG.trace ("Ignoring exception on close as discarding session: " + e1, e1);
          }
          m_aSession = null;
        }
        try
        {
          m_aSessionPool.invalidateObject (m_aKey, this);
        }
        catch (final Exception e)
        {
          throw JMSUtils.createException ("Invalidation failed", e);
        }
      }
      else
      {
        try
        {
          m_aSessionPool.returnObject (m_aKey, this);
        }
        catch (final Exception e)
        {
          throw JMSUtils.createException ("Return failed", e);
        }
      }
    }
  }

  public void commit () throws JMSException
  {
    getInternalSession ().commit ();
  }

  public BytesMessage createBytesMessage () throws JMSException
  {
    return getInternalSession ().createBytesMessage ();
  }

  public MapMessage createMapMessage () throws JMSException
  {
    return getInternalSession ().createMapMessage ();
  }

  public Message createMessage () throws JMSException
  {
    return getInternalSession ().createMessage ();
  }

  public ObjectMessage createObjectMessage () throws JMSException
  {
    return getInternalSession ().createObjectMessage ();
  }

  public ObjectMessage createObjectMessage (final Serializable serializable) throws JMSException
  {
    return getInternalSession ().createObjectMessage (serializable);
  }

  public Queue createQueue (final String s) throws JMSException
  {
    return getInternalSession ().createQueue (s);
  }

  public StreamMessage createStreamMessage () throws JMSException
  {
    return getInternalSession ().createStreamMessage ();
  }

  public TemporaryQueue createTemporaryQueue () throws JMSException
  {
    TemporaryQueue result;

    result = getInternalSession ().createTemporaryQueue ();

    // Notify all of the listeners of the created temporary Queue.
    for (final IPooledSessionEventListener listener : this.m_aSessionEventListeners)
    {
      listener.onTemporaryQueueCreate (result);
    }

    return result;
  }

  public TemporaryTopic createTemporaryTopic () throws JMSException
  {
    TemporaryTopic result;

    result = getInternalSession ().createTemporaryTopic ();

    // Notify all of the listeners of the created temporary Topic.
    for (final IPooledSessionEventListener listener : this.m_aSessionEventListeners)
    {
      listener.onTemporaryTopicCreate (result);
    }

    return result;
  }

  public void unsubscribe (final String s) throws JMSException
  {
    getInternalSession ().unsubscribe (s);
  }

  public TextMessage createTextMessage () throws JMSException
  {
    return getInternalSession ().createTextMessage ();
  }

  public TextMessage createTextMessage (final String s) throws JMSException
  {
    return getInternalSession ().createTextMessage (s);
  }

  public Topic createTopic (final String s) throws JMSException
  {
    return getInternalSession ().createTopic (s);
  }

  public int getAcknowledgeMode () throws JMSException
  {
    return getInternalSession ().getAcknowledgeMode ();
  }

  public boolean getTransacted () throws JMSException
  {
    return getInternalSession ().getTransacted ();
  }

  public void recover () throws JMSException
  {
    getInternalSession ().recover ();
  }

  public void rollback () throws JMSException
  {
    getInternalSession ().rollback ();
  }

  public XAResource getXAResource ()
  {
    if (m_aSession == null)
      throw new IllegalStateException ("Session is closed");
    // changed!
    return ((XASession) m_aSession).getXAResource ();
  }

  public void run ()
  {
    if (m_aSession != null)
      m_aSession.run ();
  }

  // Consumer related methods
  // -------------------------------------------------------------------------

  public QueueBrowser createBrowser (final Queue queue) throws JMSException
  {
    return addQueueBrowser (getInternalSession ().createBrowser (queue));
  }

  public QueueBrowser createBrowser (final Queue queue, final String selector) throws JMSException
  {
    return addQueueBrowser (getInternalSession ().createBrowser (queue, selector));
  }

  public MessageConsumer createConsumer (final Destination destination) throws JMSException
  {
    return addConsumer (getInternalSession ().createConsumer (destination));
  }

  public MessageConsumer createConsumer (final Destination destination, final String selector) throws JMSException
  {
    return addConsumer (getInternalSession ().createConsumer (destination, selector));
  }

  public MessageConsumer createConsumer (final Destination destination, final String selector, final boolean noLocal) throws JMSException
  {
    return addConsumer (getInternalSession ().createConsumer (destination, selector, noLocal));
  }

  public TopicSubscriber createDurableSubscriber (final Topic topic, final String selector) throws JMSException
  {
    return addTopicSubscriber (getInternalSession ().createDurableSubscriber (topic, selector));
  }

  public TopicSubscriber createDurableSubscriber (final Topic topic,
                                                  final String name,
                                                  final String selector,
                                                  final boolean noLocal) throws JMSException
  {
    return addTopicSubscriber (getInternalSession ().createDurableSubscriber (topic, name, selector, noLocal));
  }

  public MessageListener getMessageListener () throws JMSException
  {
    return getInternalSession ().getMessageListener ();
  }

  public void setMessageListener (final MessageListener messageListener) throws JMSException
  {
    getInternalSession ().setMessageListener (messageListener);
  }

  public TopicSubscriber createSubscriber (final Topic topic) throws JMSException
  {
    return addTopicSubscriber (((TopicSession) getInternalSession ()).createSubscriber (topic));
  }

  public TopicSubscriber createSubscriber (final Topic topic, final String selector, final boolean local) throws JMSException
  {
    return addTopicSubscriber (((TopicSession) getInternalSession ()).createSubscriber (topic, selector, local));
  }

  public QueueReceiver createReceiver (final Queue queue) throws JMSException
  {
    return addQueueReceiver (((QueueSession) getInternalSession ()).createReceiver (queue));
  }

  public QueueReceiver createReceiver (final Queue queue, final String selector) throws JMSException
  {
    return addQueueReceiver (((QueueSession) getInternalSession ()).createReceiver (queue, selector));
  }

  // Producer related methods
  // -------------------------------------------------------------------------

  public MessageProducer createProducer (final Destination destination) throws JMSException
  {
    return new PooledProducer (getMessageProducer (), destination);
  }

  public QueueSender createSender (final Queue queue) throws JMSException
  {
    return new PooledQueueSender (getQueueSender (), queue);
  }

  public TopicPublisher createPublisher (final Topic topic) throws JMSException
  {
    return new PooledTopicPublisher (getTopicPublisher (), topic);
  }

  /**
   * Callback invoked when the consumer is closed.
   * <p/>
   * This is used to keep track of an explicit closed consumer created by this
   * session, by which we know do not need to keep track of the consumer, as its
   * already closed.
   * 
   * @param consumer
   *        the consumer which is being closed
   */
  protected void onConsumerClose (final MessageConsumer consumer)
  {
    m_aConsumers.remove (consumer);
  }

  public Session getInternalSession () throws JMSException
  {
    if (m_aSession == null)
    {
      throw new JMSException ("The session has already been closed");
    }
    return m_aSession;
  }

  public MessageProducer getMessageProducer () throws JMSException
  {
    if (m_aMessageProducer == null)
    {
      m_aMessageProducer = getInternalSession ().createProducer (null);
    }
    return m_aMessageProducer;
  }

  public QueueSender getQueueSender () throws JMSException
  {
    if (m_aQueueSender == null)
    {
      m_aQueueSender = ((QueueSession) getInternalSession ()).createSender (null);
    }
    return m_aQueueSender;
  }

  public TopicPublisher getTopicPublisher () throws JMSException
  {
    if (m_aTopicPublisher == null)
    {
      m_aTopicPublisher = ((TopicSession) getInternalSession ()).createPublisher (null);
    }
    return m_aTopicPublisher;
  }

  private QueueBrowser addQueueBrowser (final QueueBrowser browser)
  {
    m_aBrowsers.add (browser);
    return browser;
  }

  private MessageConsumer addConsumer (final MessageConsumer consumer)
  {
    m_aConsumers.add (consumer);
    // must wrap in PooledMessageConsumer to ensure the onConsumerClose method
    // is
    // invoked when the returned consumer is closed, to avoid memory leak in
    // this
    // session class in case many consumers is created
    return new PooledMessageConsumer (this, consumer);
  }

  private TopicSubscriber addTopicSubscriber (final TopicSubscriber subscriber)
  {
    m_aConsumers.add (subscriber);
    return subscriber;
  }

  private QueueReceiver addQueueReceiver (final QueueReceiver receiver)
  {
    m_aConsumers.add (receiver);
    return receiver;
  }

  public void setIsXa (final boolean isXa)
  {
    this.m_bIsXA = isXa;
  }

  @Override
  public String toString ()
  {
    return "PooledSession { " + m_aSession + " }";
  }
}
