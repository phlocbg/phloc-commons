package com.phloc.commons.actor.experimental;

import javax.annotation.Nullable;

/**
 * A message between actors.
 * 
 * @author BFEIGENB
 */
public interface IActorMessage
{
  /**
   * @return Get the sender of the message.
   */
  @Nullable
  IActor getSource ();

  /**
   * @return Get the subject (AKA command) of the message.
   */
  @Nullable
  String getSubject ();

  /**
   * @return Get any parameter data associated with the message.
   */
  @Nullable
  Object getData ();
}
