package com.phloc.jms.simple;

import java.io.Closeable;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
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

  public void registerMessageListener (@Nonnull @Nonempty final String sQueueName,
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
    }
    catch (final JMSException ex)
    {
      throw new IllegalStateException ("Failed to register listener for queue '" + sQueueName + "'", ex);
    }
  }
}
