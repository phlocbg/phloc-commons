package com.phloc.commons.actor.experimental;

import javax.annotation.Nonnull;

/**
 * Listener for message reception.
 * 
 * @author BFEIGENB
 */
public interface IActorMessageListener
{
  /**
   * Call-back for message reception.
   * 
   * @param e
   *        event
   */
  void onMessage (@Nonnull ActorMessageEvent e);
}
