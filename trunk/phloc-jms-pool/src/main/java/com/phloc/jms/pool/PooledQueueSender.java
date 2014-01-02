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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

/**
 * {@link QueueSender} instance that is created and managed by the
 * PooledConnection.
 */
public class PooledQueueSender extends PooledProducer implements QueueSender
{
  public PooledQueueSender (final QueueSender messageProducer, final Destination destination) throws JMSException
  {
    super (messageProducer, destination);
  }

  @Override
  public void send (final Queue queue,
                    final Message message,
                    final int deliveryMode,
                    final int priority,
                    final long timeToLive) throws JMSException
  {
    getQueueSender ().send (queue, message, deliveryMode, priority, timeToLive);
  }

  @Override
  public void send (final Queue queue, final Message message) throws JMSException
  {
    getQueueSender ().send (queue, message);
  }

  @Override
  public Queue getQueue () throws JMSException
  {
    return getQueueSender ().getQueue ();
  }

  protected QueueSender getQueueSender ()
  {
    return (QueueSender) getMessageProducer ();
  }
}
