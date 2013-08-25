package com.phloc.jms.sender;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.StringHelper;
import com.phloc.jms.IJMSFactory;
import com.phloc.jms.JMSUtils;

public class JMSSimpleHandler
{
  public static final boolean DEFAULT_PERSISTENT = false;

  private final IJMSFactory m_aFactory;
  private final boolean m_bPersistent;

  public JMSSimpleHandler (@Nonnull final IJMSFactory aFactory)
  {
    this (aFactory, DEFAULT_PERSISTENT);
  }

  public JMSSimpleHandler (@Nonnull final IJMSFactory aFactory, final boolean bPersistent)
  {
    m_aFactory = aFactory;
    m_bPersistent = bPersistent;
  }

  @OverrideOnDemand
  protected void onException (@Nonnull final JMSException ex)
  {
    ex.printStackTrace ();
  }

  public void sendNonTransactional (@Nonnull @Nonempty final String sQueueName,
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
    }
    catch (final JMSException ex)
    {
      onException (ex);
    }
    finally
    {
      JMSUtils.close (aConnection);
    }
  }

  public void sendTransactional (@Nonnull @Nonempty final String sQueueName,
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
    }
    catch (final JMSException ex)
    {
      onException (ex);
    }
    finally
    {
      JMSUtils.close (aConnection);
    }
  }

  public void receiveNonTransactional (@Nonnull @Nonempty final String sQueueName,
                                       @Nonnull final IJMSMessageHandler aMsgHandler)
  {
    if (StringHelper.hasNoText (sQueueName))
      throw new IllegalArgumentException ("queueName");
    if (aMsgHandler == null)
      throw new NullPointerException ("msgHandler");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      final Destination aDestination = aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Topic or Queue
      final MessageConsumer aConsumer = aSession.createConsumer (aDestination);

      // Wait for a message
      final Message aMessage = aConsumer.receive ();
      aMsgHandler.handleMessage (aMessage);

      // No commit for non-transacted sessions
    }
    catch (final JMSException ex)
    {
      onException (ex);
    }
    finally
    {
      JMSUtils.close (aConnection);
    }
  }
}
