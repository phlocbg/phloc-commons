/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.deadlock;

import java.lang.management.ThreadInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public final class ThreadDeadlockInfo
{
  private final ThreadInfo m_aThreadInfo;
  private final Thread m_aThread;
  private final StackTraceElement [] m_aStackTrace;

  public ThreadDeadlockInfo (@Nonnull final ThreadInfo aThreadInfo,
                             @Nonnull final Thread aThread,
                             @Nullable final StackTraceElement [] aStackTrace)
  {
    if (aThreadInfo == null)
      throw new NullPointerException ("threadInfo");
    if (aThread == null)
      throw new NullPointerException ("thread");
    m_aThreadInfo = aThreadInfo;
    m_aThread = aThread;
    m_aStackTrace = ArrayHelper.getCopy (aStackTrace);
  }

  /**
   * @return The {@link ThreadInfo} as returned from JMX bean
   */
  @Nonnull
  public ThreadInfo getThreadInfo ()
  {
    return m_aThreadInfo;
  }

  /**
   * @return The underlying thread. Never <code>null</code>.
   */
  @Nonnull
  public Thread getThread ()
  {
    return m_aThread;
  }

  public boolean hasStackTrace ()
  {
    return m_aStackTrace != null;
  }

  /**
   * @return The stack trace at the time the dead lock was found. May be
   *         <code>null</code> for certain system threads. Use
   *         <code>getThread ().getStackTrace ()</code> to retrieve the current
   *         stack trace.
   */
  @Nullable
  @ReturnsMutableCopy
  public StackTraceElement [] getStackTrace ()
  {
    return ArrayHelper.getCopy (m_aStackTrace);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("threadInfo", m_aThreadInfo)
                                       .append ("thread", m_aThread)
                                       .appendIfNotNull ("stackTrace", m_aStackTrace)
                                       .toString ();
  }
}
