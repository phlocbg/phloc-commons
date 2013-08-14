/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.jms.wrapper;

import java.io.Serializable;

import javax.annotation.Nonnull;
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
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import com.phloc.commons.string.ToStringGenerator;

public abstract class SessionWrapper extends AbstractWrappedJMS implements Session
{
  private final Session m_aWrapped;

  public SessionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final Session aWrapped)
  {
    super (aWrapper);
    if (aWrapped == null)
      throw new NullPointerException ("wrapped");
    m_aWrapped = aWrapped;
  }

  @Nonnull
  protected Session getWrapped ()
  {
    return m_aWrapped;
  }

  @Nonnull
  public BytesMessage createBytesMessage () throws JMSException
  {
    final BytesMessage aMessage = m_aWrapped.createBytesMessage ();
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public MapMessage createMapMessage () throws JMSException
  {
    final MapMessage aMessage = m_aWrapped.createMapMessage ();
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public Message createMessage () throws JMSException
  {
    final Message aMessage = m_aWrapped.createMessage ();
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public ObjectMessage createObjectMessage () throws JMSException
  {
    final ObjectMessage aMessage = m_aWrapped.createObjectMessage ();
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public ObjectMessage createObjectMessage (final Serializable object) throws JMSException
  {
    final ObjectMessage aMessage = m_aWrapped.createObjectMessage (object);
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public StreamMessage createStreamMessage () throws JMSException
  {
    final StreamMessage aMessage = m_aWrapped.createStreamMessage ();
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public TextMessage createTextMessage () throws JMSException
  {
    final TextMessage aMessage = m_aWrapped.createTextMessage ();
    return getWrapper ().wrap (aMessage);
  }

  @Nonnull
  public TextMessage createTextMessage (final String text) throws JMSException
  {
    final TextMessage aMessage = m_aWrapped.createTextMessage (text);
    return getWrapper ().wrap (aMessage);
  }

  public boolean getTransacted () throws JMSException
  {
    return m_aWrapped.getTransacted ();
  }

  public int getAcknowledgeMode () throws JMSException
  {
    return m_aWrapped.getAcknowledgeMode ();
  }

  public void commit () throws JMSException
  {
    m_aWrapped.commit ();
  }

  public void rollback () throws JMSException
  {
    m_aWrapped.rollback ();
  }

  public void close () throws JMSException
  {
    m_aWrapped.close ();
  }

  public void recover () throws JMSException
  {
    m_aWrapped.recover ();
  }

  public MessageListener getMessageListener () throws JMSException
  {
    return m_aWrapped.getMessageListener ();
  }

  public void setMessageListener (final MessageListener listener) throws JMSException
  {
    m_aWrapped.setMessageListener (listener);
  }

  public void run ()
  {
    m_aWrapped.run ();
  }

  @Nonnull
  public MessageProducer createProducer (final Destination destination) throws JMSException
  {
    final MessageProducer aProducer = m_aWrapped.createProducer (destination);
    return getWrapper ().wrap (aProducer);
  }

  @Nonnull
  public MessageConsumer createConsumer (final Destination destination) throws JMSException
  {
    final MessageConsumer aConsumer = m_aWrapped.createConsumer (destination);
    return getWrapper ().wrap (aConsumer);
  }

  @Nonnull
  public MessageConsumer createConsumer (final Destination destination, final String messageSelector) throws JMSException
  {
    final MessageConsumer aConsumer = m_aWrapped.createConsumer (destination, messageSelector);
    return getWrapper ().wrap (aConsumer);
  }

  @Nonnull
  public MessageConsumer createConsumer (final Destination destination,
                                         final String messageSelector,
                                         final boolean NoLocal) throws JMSException
  {
    final MessageConsumer aConsumer = m_aWrapped.createConsumer (destination, messageSelector, NoLocal);
    return getWrapper ().wrap (aConsumer);
  }

  @Nonnull
  public Queue createQueue (final String queueName) throws JMSException
  {
    final Queue aQueue = m_aWrapped.createQueue (queueName);
    return getWrapper ().wrap (aQueue);
  }

  @Nonnull
  public Topic createTopic (final String topicName) throws JMSException
  {
    final Topic aTopic = m_aWrapped.createTopic (topicName);
    return getWrapper ().wrap (aTopic);
  }

  @Nonnull
  public TopicSubscriber createDurableSubscriber (final Topic topic, final String name) throws JMSException
  {
    final TopicSubscriber aTopicSubscriber = m_aWrapped.createDurableSubscriber (topic, name);
    return getWrapper ().wrap (aTopicSubscriber);
  }

  @Nonnull
  public TopicSubscriber createDurableSubscriber (final Topic topic,
                                                  final String name,
                                                  final String messageSelector,
                                                  final boolean noLocal) throws JMSException
  {
    final TopicSubscriber aTopicSubscriber = m_aWrapped.createDurableSubscriber (topic, name, messageSelector, noLocal);
    return getWrapper ().wrap (aTopicSubscriber);
  }

  @Nonnull
  public QueueBrowser createBrowser (final Queue queue) throws JMSException
  {
    final QueueBrowser aQueueBrowser = m_aWrapped.createBrowser (queue);
    return getWrapper ().wrap (aQueueBrowser);
  }

  @Nonnull
  public QueueBrowser createBrowser (final Queue queue, final String messageSelector) throws JMSException
  {
    final QueueBrowser aQueueBrowser = m_aWrapped.createBrowser (queue, messageSelector);
    return getWrapper ().wrap (aQueueBrowser);
  }

  @Nonnull
  public TemporaryQueue createTemporaryQueue () throws JMSException
  {
    final TemporaryQueue aQueue = m_aWrapped.createTemporaryQueue ();
    return getWrapper ().wrap (aQueue);
  }

  @Nonnull
  public TemporaryTopic createTemporaryTopic () throws JMSException
  {
    final TemporaryTopic aTopic = m_aWrapped.createTemporaryTopic ();
    return getWrapper ().wrap (aTopic);
  }

  public void unsubscribe (final String name) throws JMSException
  {
    m_aWrapped.unsubscribe (name);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("wrapped", m_aWrapped).toString ();
  }
}
