package com.phloc.jms.simple;

import java.io.Closeable;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.jms.IJMSFactory;
import com.phloc.jms.JMSUtils;

public class JMSMessageListenerPool implements Closeable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSMessageListenerPool.class);

  private final Connection m_aConnection;
  private final Session m_aSession;

  public JMSMessageListenerPool (@Nonnull final IJMSFactory aJMSFactory) throws JMSException
  {
    if (aJMSFactory == null)
      throw new NullPointerException ("JMSFactory");

    // Create a Connection
    m_aConnection = aJMSFactory.createConnection ();

    // Create a Session
    m_aSession = m_aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);
  }

  public void close ()
  {
    JMSUtils.close (m_aConnection);
    s_aLogger.info ("Closed MessageListener pool");
  }

  @Nonnull
  public Destination registerMessageListener (@Nonnull @Nonempty final String sQueueName,
                                              @Nonnull final MessageListener aListener)
  {
    if (StringHelper.hasNoText (sQueueName))
      throw new IllegalArgumentException ("queueName");
    if (aListener == null)
      throw new NullPointerException ("listener");

    try
    {
      // Create the destination queue
      final Destination aDestination = m_aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Queue
      final MessageConsumer aConsumer = m_aSession.createConsumer (aDestination);
      aConsumer.setMessageListener (aListener);
      s_aLogger.info ("Successfully registered listener for queue '" + sQueueName + "'");

      return aDestination;
    }
    catch (final JMSException ex)
    {
      throw new IllegalStateException ("Failed to register listener for queue '" + sQueueName + "'", ex);
    }
  }

  /**
   * @param sQueueName
   *        Queue name to listen
   * @param sCorrelationID
   *        JMS correlation ID to match
   * @param aListener
   *        The main listener
   * @param bOnce
   *        <code>true</code> if this is a one-time action in which case the
   *        created destination can be closed after the first retrieval.
   * @return The destination used
   */
  @Nonnull
  public Destination registerMessageListenerForCorrelationID (@Nonnull @Nonempty final String sQueueName,
                                                              @Nonnull @Nonempty final String sCorrelationID,
                                                              @Nonnull final MessageListener aListener,
                                                              final boolean bOnce)
  {
    if (StringHelper.hasNoText (sQueueName))
      throw new IllegalArgumentException ("queueName");
    if (StringHelper.hasNoText (sCorrelationID))
      throw new IllegalArgumentException ("correlationID");
    if (aListener == null)
      throw new NullPointerException ("listener");

    try
    {
      // Create the destination queue
      final Destination aDestination = m_aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Queue
      final MessageConsumer aConsumer = m_aSession.createConsumer (aDestination, "JMSCorrelationID = '" +
                                                                                 sCorrelationID +
                                                                                 "'");
      if (bOnce)
      {
        aConsumer.setMessageListener (new MessageListener ()
        {
          public void onMessage (final Message aMessage)
          {
            try
            {
              aListener.onMessage (aMessage);
            }
            finally
            {
              // Close this specific consumer
              JMSUtils.close (aConsumer);
            }
          }
        });
      }
      else
      {
        // Use listener as is
        aConsumer.setMessageListener (aListener);
      }

      s_aLogger.info ("Successfully registered listener for queue '" +
                      sQueueName +
                      "' and correlation ID '" +
                      sCorrelationID +
                      "'");
      return aDestination;
    }
    catch (final JMSException ex)
    {
      throw new IllegalStateException ("Failed to register listener for queue '" +
                                       sQueueName +
                                       "' and correlation ID '" +
                                       sCorrelationID +
                                       "'", ex);
    }
  }
}
