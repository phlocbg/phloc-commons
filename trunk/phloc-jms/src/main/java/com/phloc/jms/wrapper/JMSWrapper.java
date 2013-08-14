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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicConnection;
import javax.jms.XATopicSession;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Utility class to wrap JMS objects. It may be derived to change the wrapping
 * logic (e.g. for pooling)
 * 
 * @author Philip Helger
 */
@Immutable
public class JMSWrapper
{
  /**
   * Check if the passed object is already a JMS wrapped object by checking if
   * it implements {@link IJMSWrapper}.
   * 
   * @param aObject
   *        The object to be checked. May be <code>null</code>.
   * @return <code>true</code> if the object is not <code>null</code> and
   *         already a wrapped JMS object, <code>false</code> otherwise.
   */
  public static boolean isWrapped (@Nullable final Object aObject)
  {
    return aObject instanceof IJMSWrapper;
  }

  @Nonnull
  public Connection wrap (@Nonnull final Connection aConnection)
  {
    if (aConnection instanceof XAQueueConnection)
      return wrap ((XAQueueConnection) aConnection);
    if (aConnection instanceof XATopicConnection)
      return wrap ((XATopicConnection) aConnection);
    if (aConnection instanceof QueueConnection)
      return wrap ((QueueConnection) aConnection);
    if (aConnection instanceof TopicConnection)
      return wrap ((TopicConnection) aConnection);
    throw new IllegalStateException ("Unsupported connection: " + aConnection);
  }

  @Nonnull
  public XAQueueConnection wrap (@Nonnull final XAQueueConnection aConnection)
  {
    if (isWrapped (aConnection))
      return aConnection;
    return new XAQueueConnectionWrapper (this, aConnection);
  }

  @Nonnull
  public XATopicConnection wrap (@Nonnull final XATopicConnection aConnection)
  {
    if (isWrapped (aConnection))
      return aConnection;
    return new XATopicConnectionWrapper (this, aConnection);
  }

  @Nonnull
  public QueueConnection wrap (@Nonnull final QueueConnection aConnection)
  {
    if (isWrapped (aConnection))
      return aConnection;
    return new QueueConnectionWrapper (this, aConnection);
  }

  @Nonnull
  public TopicConnection wrap (@Nonnull final TopicConnection aConnection)
  {
    if (isWrapped (aConnection))
      return aConnection;
    return new TopicConnectionWrapper (this, aConnection);
  }

  @Nonnull
  public Session wrap (@Nonnull final Session aSession)
  {
    if (aSession instanceof QueueSession)
      return wrap ((QueueSession) aSession);
    if (aSession instanceof TopicSession)
      return wrap ((TopicSession) aSession);
    if (aSession instanceof XASession)
      return wrap ((XASession) aSession);
    throw new IllegalStateException ("Unsupported session: " + aSession);
  }

  @Nonnull
  public XASession wrap (@Nonnull final XASession aSession)
  {
    if (aSession instanceof XAQueueSession)
      return wrap ((XAQueueSession) aSession);
    if (aSession instanceof XATopicSession)
      return wrap ((XATopicSession) aSession);
    throw new IllegalStateException ("Unsupported XA session: " + aSession);
  }

  @Nonnull
  public QueueSession wrap (@Nonnull final QueueSession aSession)
  {
    if (isWrapped (aSession))
      return aSession;
    return new QueueSessionWrapper (this, aSession);
  }

  @Nonnull
  public TopicSession wrap (@Nonnull final TopicSession aSession)
  {
    if (isWrapped (aSession))
      return aSession;
    return new TopicSessionWrapper (this, aSession);
  }

  @Nonnull
  public XAQueueSession wrap (@Nonnull final XAQueueSession aSession)
  {
    if (isWrapped (aSession))
      return aSession;
    return new XAQueueSessionWrapper (this, aSession);
  }

  @Nonnull
  public XATopicSession wrap (@Nonnull final XATopicSession aSession)
  {
    if (isWrapped (aSession))
      return aSession;
    return new XATopicSessionWrapper (this, aSession);
  }

  @Nonnull
  public MessageProducer wrap (@Nonnull final MessageProducer aMessageProducer)
  {
    if (isWrapped (aMessageProducer))
      return aMessageProducer;
    return new MessageProducerWrapper (this, aMessageProducer);
  }

  @Nonnull
  public QueueSender wrap (@Nonnull final QueueSender aQueueSender)
  {
    if (isWrapped (aQueueSender))
      return aQueueSender;
    return new QueueSenderWrapper (this, aQueueSender);
  }

  @Nonnull
  public TopicPublisher wrap (@Nonnull final TopicPublisher aTopicPublisher)
  {
    if (isWrapped (aTopicPublisher))
      return aTopicPublisher;
    return new TopicPublisherWrapper (this, aTopicPublisher);
  }

  @Nonnull
  public MessageConsumer wrap (@Nonnull final MessageConsumer aMessageConsumer)
  {
    if (isWrapped (aMessageConsumer))
      return aMessageConsumer;
    return new MessageConsumerWrapper (this, aMessageConsumer);
  }

  @Nonnull
  public QueueReceiver wrap (@Nonnull final QueueReceiver aQueueReceiver)
  {
    if (isWrapped (aQueueReceiver))
      return aQueueReceiver;
    return new QueueReceiverWrapper (this, aQueueReceiver);
  }

  @Nonnull
  public TopicSubscriber wrap (@Nonnull final TopicSubscriber aTopicSubscriber)
  {
    if (isWrapped (aTopicSubscriber))
      return aTopicSubscriber;
    return new TopicSubscriberWrapper (this, aTopicSubscriber);
  }

  @Nonnull
  public Queue wrap (@Nonnull final Queue aQueue)
  {
    if (isWrapped (aQueue))
      return aQueue;
    return new QueueWrapper (this, aQueue);
  }

  @Nonnull
  public Topic wrap (@Nonnull final Topic aTopic)
  {
    if (isWrapped (aTopic))
      return aTopic;
    return new TopicWrapper (this, aTopic);
  }

  @Nonnull
  public TemporaryQueue wrap (@Nonnull final TemporaryQueue aTemporaryQueue)
  {
    if (isWrapped (aTemporaryQueue))
      return aTemporaryQueue;
    return new TemporaryQueueWrapper (this, aTemporaryQueue);
  }

  @Nonnull
  public TemporaryTopic wrap (@Nonnull final TemporaryTopic aTemporaryTopic)
  {
    if (isWrapped (aTemporaryTopic))
      return aTemporaryTopic;
    return new TemporaryTopicWrapper (this, aTemporaryTopic);
  }

  @Nonnull
  public Message wrap (@Nonnull final Message aMessage)
  {
    if (isWrapped (aMessage))
      return aMessage;
    return new MessageWrapper (this, aMessage);
  }

  @Nonnull
  public BytesMessage wrap (@Nonnull final BytesMessage aMessage)
  {
    if (isWrapped (aMessage))
      return aMessage;
    return new BytesMessageWrapper (this, aMessage);
  }

  @Nonnull
  public MapMessage wrap (@Nonnull final MapMessage aMessage)
  {
    if (isWrapped (aMessage))
      return aMessage;
    return new MapMessageWrapper (this, aMessage);
  }

  @Nonnull
  public ObjectMessage wrap (@Nonnull final ObjectMessage aMessage)
  {
    if (isWrapped (aMessage))
      return aMessage;
    return new ObjectMessageWrapper (this, aMessage);
  }

  @Nonnull
  public StreamMessage wrap (@Nonnull final StreamMessage aMessage)
  {
    if (isWrapped (aMessage))
      return aMessage;
    return new StreamMessageWrapper (this, aMessage);
  }

  @Nonnull
  public TextMessage wrap (@Nonnull final TextMessage aMessage)
  {
    if (isWrapped (aMessage))
      return aMessage;
    return new TextMessageWrapper (this, aMessage);
  }

  @Nonnull
  public ConnectionConsumer wrap (@Nonnull final ConnectionConsumer aConnectionConsumer)
  {
    if (isWrapped (aConnectionConsumer))
      return aConnectionConsumer;
    return new ConnectionConsumerWrapper (this, aConnectionConsumer);
  }

  @Nonnull
  public QueueBrowser wrap (@Nonnull final QueueBrowser aQueueBrowser)
  {
    if (isWrapped (aQueueBrowser))
      return aQueueBrowser;
    return new QueueBrowserWrapper (this, aQueueBrowser);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
