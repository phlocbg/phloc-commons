package com.phloc.commons.actor.experimental;

/**
 * A message between actors.
 * 
 * @author BFEIGENB
 */
public interface IActorMessage
{
  /** Get the sender of the message. */
  IActor getSource ();

  /** Get the subject (AKA command) of the message. */
  String getSubject ();

  /** Get any parameter data associated with the message. */
  Object getData ();
}
