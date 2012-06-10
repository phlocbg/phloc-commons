package com.phloc.commons.actor.experimental;

import java.util.EventObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Sent when a message is received.
 * 
 * @author BFEIGENB
 */
public class ActorMessageEvent extends EventObject
{
  private final IActorMessage m_aMessage;
  private final EMessageStatus m_eStatus;
  private final Throwable m_aThrowable;

  public ActorMessageEvent (@Nonnull final Object aSource,
                            @Nonnull final IActorMessage aMessage,
                            @Nonnull final EMessageStatus eStatus)
  {
    this (aSource, aMessage, eStatus, null);
  }

  public ActorMessageEvent (@Nonnull final Object aSource,
                            @Nonnull final IActorMessage aMessage,
                            @Nonnull final EMessageStatus eStatus,
                            @Nullable final Throwable aThrowable)
  {
    super (aSource);
    if (aSource == null)
      throw new NullPointerException ("source");
    if (aMessage == null)
      throw new NullPointerException ("message");
    if (eStatus == null)
      throw new NullPointerException ("status");
    m_aMessage = aMessage;
    m_eStatus = eStatus;
    m_aThrowable = aThrowable;
  }

  @Nonnull
  public IActorMessage getMessage ()
  {
    return m_aMessage;
  }

  @Nonnull
  public EMessageStatus getStatus ()
  {
    return m_eStatus;
  }

  @Nullable
  public Throwable getThrowable ()
  {
    return m_aThrowable;
  }
}
