package com.phloc.commons.actor.experimental;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

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

  public DefaultActorMessage (final String subject, final int nData)
  {
    this (subject, Integer.valueOf (nData));
  }

  public DefaultActorMessage (final String subject, final Object data)
  {
    this (subject);
    m_aData = data;
  }

  private DefaultActorMessage (final IActor aSource, final String subject, final Object data)
  {
    this (subject, data);
    m_aSource = aSource;
  }

  public DefaultActorMessage (final String subject)
  {
    this ();
    m_sSubject = subject;
  }

  protected DefaultActorMessage ()
  {}

  @Override
  public IActor getSource ()
  {
    return m_aSource;
  }

  /** Sets the sender of this message; can be null. */
  public void setSource (final IActor source)
  {
    m_aSource = source;
  }

  @Override
  public String getSubject ()
  {
    return m_sSubject;
  }

  /** Sets the subject (command) this message implies; can be null. */
  public void setSubject (final String subject)
  {
    m_sSubject = subject;
  }

  @Override
  public Object getData ()
  {
    return m_aData;
  }

  /** Sets data associated with this message; can be null. */
  public void setData (final Object data)
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
  public IActorMessage assignSender (final IActor sender)
  {
    return new DefaultActorMessage (sender, m_sSubject, m_aData);
  }

  public boolean subjectMatches (final String s, final boolean bIsRegEx)
  {
    return bIsRegEx ? RegExHelper.stringMatchesPattern (s, m_sSubject) : EqualsUtils.equals (s, m_sSubject);
  }

  public void addMessageListener (final IActorMessageListener l)
  {
    if (!m_aListeners.contains (l))
      m_aListeners.add (l);
  }

  public void removeMessageListener (final IActorMessageListener l)
  {
    m_aListeners.remove (l);
  }

  public void fireMessageListeners (final ActorMessageEvent e)
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
