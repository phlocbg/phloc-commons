package com.phloc.jms.sender;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Interface for handling arbitrary received JMS messages
 * 
 * @author Philip Helger
 */
public interface IJMSMessageHandler
{
  /**
   * Handle a received JMS message.
   * 
   * @param aMessage
   *        The JMS message to be handled. Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  void handleMessage (@Nonnull Message aMessage) throws JMSException;
}
