package com.phloc.jms.simple;

import javax.annotation.Nonnull;
import javax.jms.Destination;
import javax.jms.MessageConsumer;

import com.phloc.commons.string.ToStringGenerator;

/**
 * A pair of JMS destination and JMS message consumer.
 * 
 * @author Philip Helger
 */
public class JMSDestinationAndConsumer
{
  private final Destination m_aDestination;
  private final MessageConsumer m_aConsumer;

  /**
   * @param aDestination
   *        Destination. May not be <code>null</code>.
   * @param aConsumer
   *        Message consumer. May not be <code>null</code>.
   */
  public JMSDestinationAndConsumer (@Nonnull final Destination aDestination, @Nonnull final MessageConsumer aConsumer)
  {
    if (aDestination == null)
      throw new NullPointerException ("destination");
    if (aConsumer == null)
      throw new NullPointerException ("consumer");

    m_aDestination = aDestination;
    m_aConsumer = aConsumer;
  }

  /**
   * @return The JMS destination. Never <code>null</code>.
   */
  @Nonnull
  public Destination getDestination ()
  {
    return m_aDestination;
  }

  /**
   * @return The JMS message consumer. Never <code>null</code>.
   */
  @Nonnull
  public MessageConsumer getConsumer ()
  {
    return m_aConsumer;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("destination", m_aDestination)
                                       .append ("consumer", m_aConsumer)
                                       .toString ();
  }
}
