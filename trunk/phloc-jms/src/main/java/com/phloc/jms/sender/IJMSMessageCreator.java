package com.phloc.jms.sender;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Interface for creating arbitrary JMS messages for sending.
 * 
 * @author Philip Helger
 */
public interface IJMSMessageCreator
{
  /**
   * Create a new JMS message.
   * 
   * @param aSession
   *        The JMS session to use. Never <code>null</code>.
   * @return Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  @Nonnull
  Message createMessage (@Nonnull Session aSession) throws JMSException;
}
