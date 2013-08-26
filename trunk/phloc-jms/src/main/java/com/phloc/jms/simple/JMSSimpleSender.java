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
package com.phloc.jms.simple;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.jms.IJMSFactory;
import com.phloc.jms.JMSUtils;

/**
 * A simple sender for JMS messages.
 * 
 * @author Philip Helger
 */
public class JMSSimpleSender
{
  public static final boolean DEFAULT_PERSISTENT = false;

  private final IJMSFactory m_aFactory;
  private final boolean m_bPersistent;

  public JMSSimpleSender (@Nonnull final IJMSFactory aFactory)
  {
    this (aFactory, DEFAULT_PERSISTENT);
  }

  public JMSSimpleSender (@Nonnull final IJMSFactory aFactory, final boolean bPersistent)
  {
    if (aFactory == null)
      throw new NullPointerException ("Factory");
    m_aFactory = aFactory;
    m_bPersistent = bPersistent;
  }

  @OverrideOnDemand
  protected void onException (@Nonnull final JMSException ex)
  {
    ex.printStackTrace ();
  }

  @Nonnull
  public ESuccess sendNonTransactional (@Nonnull @Nonempty final String sQueueName,
                                        @Nonnull final IJMSMessageCreator aMsgCreator)
  {
    if (StringHelper.hasNoText (sQueueName))
      throw new IllegalArgumentException ("queueName");
    if (aMsgCreator == null)
      throw new NullPointerException ("msgCreator");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      final Queue aDestination = aSession.createQueue (sQueueName);

      // Create a MessageProducer from the Session to the Topic or Queue
      final MessageProducer aProducer = aSession.createProducer (aDestination);
      aProducer.setDeliveryMode (m_bPersistent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);

      // Create a messages
      final Message aMessage = aMsgCreator.createMessage (aSession);
      if (aMessage == null)
        throw new IllegalStateException ("Failed to create message");

      // Tell the producer to send the message
      aProducer.send (aMessage);

      // No commit for non-transacted sessions
      return ESuccess.SUCCESS;
    }
    catch (final JMSException ex)
    {
      onException (ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      JMSUtils.close (aConnection);
    }
  }

  @Nonnull
  public ESuccess sendTransactional (@Nonnull @Nonempty final String sQueueName,
                                     @Nonnull final IJMSMessageCreator aMsgCreator)
  {
    if (StringHelper.hasNoText (sQueueName))
      throw new IllegalArgumentException ("queueName");
    if (aMsgCreator == null)
      throw new NullPointerException ("msgCreator");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (true, -1);

      // Create the destination (Topic or Queue)
      final Queue aDestination = aSession.createQueue (sQueueName);

      // Create a MessageProducer from the Session to the Topic or Queue
      final MessageProducer aProducer = aSession.createProducer (aDestination);
      aProducer.setDeliveryMode (m_bPersistent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);

      // Create a messages
      final Message aMessage = aMsgCreator.createMessage (aSession);
      if (aMessage == null)
        throw new IllegalStateException ("Failed to create message");

      // Tell the producer to send the message
      aProducer.send (aMessage);

      // commit for transacted sessions
      aSession.commit ();
      return ESuccess.SUCCESS;
    }
    catch (final JMSException ex)
    {
      onException (ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      JMSUtils.close (aConnection);
    }
  }
}
