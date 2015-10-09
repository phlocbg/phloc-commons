/**
 * Copyright (C) 2006-2015 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.commons.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IHasErrorLevels;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IClearable;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Keeps a set of {@link LogMessage} objects in memory, offering an API similar
 * to SLF4J.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class InMemoryLogger implements Iterable <LogMessage>, IHasSize, IClearable, IHasErrorLevels, Serializable
{
  private final List <LogMessage> m_aMessages = new ArrayList <LogMessage> ();

  /**
   * Override this method to create a different LogMessage object or to filter
   * certain log messages.
   * 
   * @param eErrorLevel
   *        Error level. Never <code>null</code>.
   * @param aMsg
   *        The message object. Never <code>null</code>.
   * @param t
   *        An optional exception. May be <code>null</code>.
   * @return The returned value. May be <code>null</code> in which case the
   *         message will not be logged.
   */
  @Nullable
  @OverrideOnDemand
  protected LogMessage createLogMessage (@Nonnull final EErrorLevel eErrorLevel,
                                         @Nonnull final Serializable aMsg,
                                         @Nullable final Throwable t)
  {
    return new LogMessage (eErrorLevel, aMsg, t);
  }

  /**
   * Callback method that is invoked after a message was added.
   * 
   * @param aLogMessage
   *        The added log message. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void onAddLogMessage (@Nonnull final LogMessage aLogMessage)
  {}

  public void log (@Nonnull final EErrorLevel eErrorLevel, @Nonnull final Serializable aMsg)
  {
    log (eErrorLevel, aMsg, null);
  }

  public void log (@Nonnull final EErrorLevel eErrorLevel, @Nonnull final Serializable aMsg, @Nullable final Throwable t)
  {
    final LogMessage aLogMessage = createLogMessage (eErrorLevel, aMsg, t);
    if (aLogMessage != null)
    {
      m_aMessages.add (aLogMessage);
      onAddLogMessage (aLogMessage);
    }
  }

  public void error (@Nonnull final Serializable aMsg)
  {
    error (aMsg, null);
  }

  public void error (@Nonnull final Serializable aMsg, @Nullable final Throwable t)
  {
    log (EErrorLevel.ERROR, aMsg, t);
  }

  public void warn (@Nonnull final Serializable aMsg)
  {
    warn (aMsg, null);
  }

  public void warn (@Nonnull final Serializable aMsg, @Nullable final Throwable t)
  {
    log (EErrorLevel.WARN, aMsg, t);
  }

  public void info (@Nonnull final Serializable aMsg)
  {
    log (EErrorLevel.INFO, aMsg, null);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <LogMessage> getAllMessages ()
  {
    return ContainerHelper.newList (m_aMessages);
  }

  @Nonnull
  public Iterator <LogMessage> iterator ()
  {
    return m_aMessages.iterator ();
  }

  @Nonnegative
  public int size ()
  {
    return m_aMessages.size ();
  }

  public boolean isEmpty ()
  {
    return m_aMessages.isEmpty ();
  }

  public boolean containsOnlySuccess ()
  {
    if (m_aMessages.isEmpty ())
      return false;
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isFailure ())
        return false;
    return true;
  }

  public boolean containsAtLeastOneSuccess ()
  {
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isSuccess ())
        return true;
    return false;
  }

  public boolean containsNoSuccess ()
  {
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isSuccess ())
        return false;
    return true;
  }

  @Nonnegative
  public int getSuccessCount ()
  {
    int ret = 0;
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isSuccess ())
        ret++;
    return ret;
  }

  public boolean containsOnlyFailure ()
  {
    if (m_aMessages.isEmpty ())
      return false;
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isSuccess ())
        return false;
    return true;
  }

  public boolean containsAtLeastOneFailure ()
  {
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isFailure ())
        return true;
    return false;
  }

  public boolean containsNoFailure ()
  {
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isFailure ())
        return false;
    return true;
  }

  @Nonnegative
  public int getFailureCount ()
  {
    int ret = 0;
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isFailure ())
        ret++;
    return ret;
  }

  public boolean containsOnlyError ()
  {
    if (m_aMessages.isEmpty ())
      return false;
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isNoError ())
        return false;
    return true;
  }

  public boolean containsAtLeastOneError ()
  {
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isError ())
        return true;
    return false;
  }

  public boolean containsNoError ()
  {
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isError ())
        return false;
    return true;
  }

  @Nonnegative
  public int getErrorCount ()
  {
    int ret = 0;
    for (final LogMessage aMessage : m_aMessages)
      if (aMessage.isError ())
        ret++;
    return ret;
  }

  @Nonnull
  public EErrorLevel getMostSevereErrorLevel ()
  {
    EErrorLevel eRet = EErrorLevel.SUCCESS;
    for (final LogMessage aMessage : m_aMessages)
    {
      final EErrorLevel eCur = aMessage.getErrorLevel ();
      if (eCur.isMoreSevereThan (eRet))
      {
        eRet = eCur;
        if (eRet == EErrorLevel.HIGHEST)
          break;
      }
    }
    return eRet;
  }

  @Nonnull
  public EChange clear ()
  {
    if (m_aMessages.isEmpty ())
      return EChange.UNCHANGED;
    m_aMessages.clear ();
    return EChange.CHANGED;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("messages", m_aMessages).toString ();
  }
}
