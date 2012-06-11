package com.phloc.commons.actor.experimental;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A default message implementation.
 * 
 * @author BFEIGENB
 */
public class DefaultActorMessage implements IActorMessage
{
  private IActor m_aSource;
  private String m_sSubject;
  private Object m_aData;
  private long m_nDelayUntil = CGlobal.ILLEGAL_ULONG;
  private final List <IActorMessageListener> m_aListeners = new LinkedList <IActorMessageListener> ();

  protected DefaultActorMessage ()
  {}

  public DefaultActorMessage (@Nullable final String sSubject)
  {
    this ();
    m_sSubject = sSubject;
  }

  public DefaultActorMessage (@Nullable final String sSubject, final int nData)
  {
    this (sSubject, Integer.valueOf (nData));
  }

  public DefaultActorMessage (@Nullable final String sSubject, @Nullable final Object data)
  {
    this (sSubject);
    m_aData = data;
  }

  private DefaultActorMessage (@Nullable final IActor aSource,
                               @Nullable final String sSubject,
                               @Nullable final Object data)
  {
    this (sSubject, data);
    m_aSource = aSource;
  }

  @Nullable
  public IActor getSource ()
  {
    return m_aSource;
  }

  /**
   * Sets the sender of this message; can be null.
   */
  public void setSource (@Nullable final IActor source)
  {
    m_aSource = source;
  }

  @Nullable
  public String getSubject ()
  {
    return m_sSubject;
  }

  /** Sets the sSubject (command) this message implies; can be null. */
  public void setSubject (@Nullable final String sSubject)
  {
    m_sSubject = sSubject;
  }

  @Nullable
  public Object getData ()
  {
    return m_aData;
  }

  /** Sets data associated with this message; can be null. */
  public void setData (@Nullable final Object data)
  {
    m_aData = data;
  }

  /** Get the delay value. */
  public long getDelayUntil ()
  {
    return m_nDelayUntil;
  }

  /**
   * Used to delay message execution until some moment in time has passed.
   * 
   * @param delayUntil
   *        future time (in millis since epoch)
   **/
  public void setDelayUntil (final long delayUntil)
  {
    final long now = new Date ().getTime ();
    if (delayUntil <= now)
      throw new IllegalArgumentException ("value should be in the future: " + delayUntil + " vs. " + now);
    m_nDelayUntil = delayUntil;
  }

  /** Set the sender of a clone of this message. */
  @Nonnull
  public IActorMessage assignSender (@Nullable final IActor sender)
  {
    return new DefaultActorMessage (sender, m_sSubject, m_aData);
  }

  public boolean subjectMatches (@Nonnull final String sSubject, final boolean bIsRegEx)
  {
    return bIsRegEx ? RegExHelper.stringMatchesPattern (sSubject, m_sSubject) : EqualsUtils.equals (sSubject,
                                                                                                    m_sSubject);
  }

  public void addMessageListener (@Nullable final IActorMessageListener aListener)
  {
    if (aListener != null)
      if (!m_aListeners.contains (aListener))
        m_aListeners.add (aListener);
  }

  public void removeMessageListener (@Nullable final IActorMessageListener aListener)
  {
    m_aListeners.remove (aListener);
  }

  public void fireMessageListeners (@Nonnull final ActorMessageEvent e)
  {
    for (final IActorMessageListener aListener : m_aListeners)
      aListener.onMessage (e);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("source", m_aSource)
                                       .append ("subject", m_sSubject)
                                       .append ("data", m_aData)
                                       .append ("delayUntil", m_nDelayUntil)
                                       .append ("listeners", m_aListeners)
                                       .toString ();
  }
}
