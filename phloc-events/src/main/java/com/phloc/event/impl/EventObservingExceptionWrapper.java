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
package com.phloc.event.impl;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.event.IEvent;

public final class EventObservingExceptionWrapper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (EventObservingExceptionWrapper.class);
  private final String m_sMessage;
  private final IEvent m_aEvent;
  private final Throwable m_aThrowable;

  public EventObservingExceptionWrapper (@Nonnull final Object aObserver,
                                         @Nonnull final IEvent aEvent,
                                         @Nonnull final Throwable aThrowable)
  {
    if (aObserver == null)
      throw new IllegalArgumentException ("observer");
    if (aEvent == null)
      throw new IllegalArgumentException ("event");
    if (aThrowable == null)
      throw new IllegalArgumentException ("throwable");

    m_sMessage = "Failed to notify " + aObserver;
    m_aEvent = aEvent;
    m_aThrowable = aThrowable;

    s_aLogger.warn ("An exception occured while notifying observer " + aObserver + " on event " + aEvent);
  }

  @Nonnull
  @Nonempty
  public String getMessage ()
  {
    return m_sMessage;
  }

  @Nonnull
  public IEvent getEvent ()
  {
    return m_aEvent;
  }

  @Nonnull
  public Throwable getThrowable ()
  {
    return m_aThrowable;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("msg", m_sMessage)
                                       .append ("event", m_aEvent)
                                       .append ("throwable", m_aThrowable)
                                       .toString ();
  }
}
