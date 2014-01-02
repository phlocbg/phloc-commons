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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * A {@link MessageConsumer} which was created by {@link PooledSession}.
 */
public class PooledMessageConsumer implements MessageConsumer
{
  private final PooledSession m_aSession;
  private final MessageConsumer m_aDelegate;

  /**
   * Wraps the message consumer.
   * 
   * @param session
   *        the pooled session
   * @param delegate
   *        the created consumer to wrap
   */
  public PooledMessageConsumer (final PooledSession session, final MessageConsumer delegate)
  {
    m_aSession = session;
    m_aDelegate = delegate;
  }

  @Override
  public void close () throws JMSException
  {
    // ensure session removes consumer as its closed now
    m_aSession.onConsumerClose (m_aDelegate);
    m_aDelegate.close ();
  }

  @Override
  public MessageListener getMessageListener () throws JMSException
  {
    return m_aDelegate.getMessageListener ();
  }

  @Override
  public String getMessageSelector () throws JMSException
  {
    return m_aDelegate.getMessageSelector ();
  }

  @Override
  public Message receive () throws JMSException
  {
    return m_aDelegate.receive ();
  }

  @Override
  public Message receive (final long timeout) throws JMSException
  {
    return m_aDelegate.receive (timeout);
  }

  @Override
  public Message receiveNoWait () throws JMSException
  {
    return m_aDelegate.receiveNoWait ();
  }

  @Override
  public void setMessageListener (final MessageListener listener) throws JMSException
  {
    m_aDelegate.setMessageListener (listener);
  }

  @Override
  public String toString ()
  {
    return "PooledMessageConsumer { " + m_aDelegate + " }";
  }
}
