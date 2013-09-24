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
import javax.jms.MessageProducer;

/**
 * A pooled {@link MessageProducer}
 */
public class PooledProducer implements MessageProducer
{
  private final MessageProducer m_aMessageProducer;
  private final Destination m_aDestination;

  private int m_nDeliveryMode;
  private boolean m_bDisableMessageID;
  private boolean m_bDisableMessageTimestamp;
  private int m_nPriority;
  private long m_nTimeToLive;

  public PooledProducer (final MessageProducer messageProducer, final Destination destination) throws JMSException
  {
    m_aMessageProducer = messageProducer;
    m_aDestination = destination;

    m_nDeliveryMode = messageProducer.getDeliveryMode ();
    m_bDisableMessageID = messageProducer.getDisableMessageID ();
    m_bDisableMessageTimestamp = messageProducer.getDisableMessageTimestamp ();
    m_nPriority = messageProducer.getPriority ();
    m_nTimeToLive = messageProducer.getTimeToLive ();
  }

  @Override
  public void close () throws JMSException
  {}

  @Override
  public void send (final Destination destination, final Message message) throws JMSException
  {
    send (destination, message, getDeliveryMode (), getPriority (), getTimeToLive ());
  }

  @Override
  public void send (final Message message) throws JMSException
  {
    send (m_aDestination, message, getDeliveryMode (), getPriority (), getTimeToLive ());
  }

  @Override
  public void send (final Message message, final int deliveryMode, final int priority, final long timeToLive) throws JMSException
  {
    send (m_aDestination, message, deliveryMode, priority, timeToLive);
  }

  @Override
  public void send (final Destination destination,
                    final Message message,
                    final int deliveryMode,
                    final int priority,
                    final long timeToLive) throws JMSException
  {
    final MessageProducer messageProducer = getMessageProducer ();

    // just in case let only one thread send at once
    synchronized (messageProducer)
    {
      messageProducer.send (destination != null ? destination : m_aDestination,
                            message,
                            deliveryMode,
                            priority,
                            timeToLive);
    }
  }

  @Override
  public Destination getDestination ()
  {
    return m_aDestination;
  }

  @Override
  public int getDeliveryMode ()
  {
    return m_nDeliveryMode;
  }

  @Override
  public void setDeliveryMode (final int deliveryMode)
  {
    m_nDeliveryMode = deliveryMode;
  }

  @Override
  public boolean getDisableMessageID ()
  {
    return m_bDisableMessageID;
  }

  @Override
  public void setDisableMessageID (final boolean disableMessageID)
  {
    m_bDisableMessageID = disableMessageID;
  }

  @Override
  public boolean getDisableMessageTimestamp ()
  {
    return m_bDisableMessageTimestamp;
  }

  @Override
  public void setDisableMessageTimestamp (final boolean disableMessageTimestamp)
  {
    m_bDisableMessageTimestamp = disableMessageTimestamp;
  }

  @Override
  public int getPriority ()
  {
    return m_nPriority;
  }

  @Override
  public void setPriority (final int priority)
  {
    m_nPriority = priority;
  }

  @Override
  public long getTimeToLive ()
  {
    return m_nTimeToLive;
  }

  @Override
  public void setTimeToLive (final long timeToLive)
  {
    m_nTimeToLive = timeToLive;
  }

  // Implementation methods
  // -------------------------------------------------------------------------
  protected MessageProducer getMessageProducer ()
  {
    return m_aMessageProducer;
  }

  @Override
  public String toString ()
  {
    return "PooledProducer { " + m_aMessageProducer + " }";
  }
}
