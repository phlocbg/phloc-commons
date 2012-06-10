package com.phloc.commons.actor.experimental;

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
  void onMessage (ActorMessageEvent e);
}
