package com.phloc.jms;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base interface for a JMS connection factory.
 * 
 * @author Philip Helger
 */
public interface IJMSFactory
{
  /**
   * Create a new connection start it
   * 
   * @return Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  @Nonnull
  Connection createConnection () throws JMSException;

  /**
   * Create a new connection start it or not
   * 
   * @param bStartConnection
   *        <code>true</code> if the new connection should be started
   * @return Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  @Nonnull
  Connection createConnection (boolean bStartConnection) throws JMSException;

  /**
   * Shutdown the whole JMS factory. Afterwards no connection can be created
   * anymore.
   */
  void shutdown ();
}
