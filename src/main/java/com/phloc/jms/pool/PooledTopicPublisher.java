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
package com.phloc.jms.pool;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

/**
 * A {@link TopicPublisher} instance that is created and managed by a
 * PooledConnection.
 */
public class PooledTopicPublisher extends PooledProducer implements TopicPublisher
{
  public PooledTopicPublisher (final TopicPublisher messageProducer, final Destination destination) throws JMSException
  {
    super (messageProducer, destination);
  }

  @Override
  public Topic getTopic () throws JMSException
  {
    return getTopicPublisher ().getTopic ();
  }

  @Override
  public void publish (final Message message) throws JMSException
  {
    getTopicPublisher ().publish ((Topic) getDestination (), message);
  }

  @Override
  public void publish (final Message message, final int deliveryMode, final int priority, final long timeToLive) throws JMSException
  {
    getTopicPublisher ().publish ((Topic) getDestination (), message, deliveryMode, priority, timeToLive);
  }

  @Override
  public void publish (final Topic topic, final Message message) throws JMSException
  {
    getTopicPublisher ().publish (topic, message);
  }

  @Override
  public void publish (final Topic topic,
                       final Message message,
                       final int deliveryMode,
                       final int priority,
                       final long timeToLive) throws JMSException
  {
    getTopicPublisher ().publish (topic, message, deliveryMode, priority, timeToLive);
  }

  protected TopicPublisher getTopicPublisher ()
  {
    return (TopicPublisher) getMessageProducer ();
  }
}
