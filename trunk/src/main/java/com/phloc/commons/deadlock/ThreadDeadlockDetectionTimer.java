/**
 * Copyright (C) 2006-2012 phloc systems
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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.state.EChange;

/**
 * A dead lock detector based on JMX
 * 
 * @author philip
 */
public final class ThreadDeadlockDetectionTimer
{
  /**
   * The number of milliseconds between checking for deadlocks. It may be
   * expensive to check for deadlocks, and it is not critical to know so
   * quickly.
   */
  private static final long DEFAULT_DEADLOCK_CHECK_PERIOD = 10 * CGlobal.MILLISECONDS_PER_SECOND;
  private static final Logger s_aLogger = LoggerFactory.getLogger (ThreadDeadlockDetectionTimer.class);

  private final ThreadDeadlockDetectionTask m_aTimerTask;
  private final Timer m_aThreadCheck = new Timer ("ThreadDeadlockDetector", true);

  private static final class ThreadDeadlockDetectionTask extends TimerTask
  {
    private final ThreadMXBean m_aMBean = ManagementFactory.getThreadMXBean ();
    final Set <IThreadDeadlockListener> m_aListeners = new CopyOnWriteArraySet <IThreadDeadlockListener> ();

    @Override
    public void run ()
    {
      final long [] aThreadIDs = m_aMBean.isSynchronizerUsageSupported () ? m_aMBean.findDeadlockedThreads ()
                                                                         : m_aMBean.findMonitorDeadlockedThreads ();
      if (!ArrayHelper.isEmpty (aThreadIDs))
      {
        final Map <Thread, StackTraceElement []> aAllStackTraces = Thread.getAllStackTraces ();
        final ThreadDeadlockInfo [] aThreads = new ThreadDeadlockInfo [aThreadIDs.length];
        for (int i = 0; i < aThreads.length; i++)
        {
          // ThreadInfo
          final ThreadInfo aThreadInfo = m_aMBean.getThreadInfo (aThreadIDs[i]);

          // Find matching thread and stack trace
          Thread aFoundThread = null;
          StackTraceElement [] aFoundStackTrace = null;
          for (final Map.Entry <Thread, StackTraceElement []> aEnrty : aAllStackTraces.entrySet ())
            if (aEnrty.getKey ().getId () == aThreadInfo.getThreadId ())
            {
              aFoundThread = aEnrty.getKey ();
              aFoundStackTrace = aEnrty.getValue ();
              break;
            }
          if (aFoundThread == null)
            throw new IllegalStateException ("Deadlocked Thread not found as defined by " + aThreadInfo.toString ());

          // Remember
          aThreads[i] = new ThreadDeadlockInfo (aThreadInfo, aFoundThread, aFoundStackTrace);
        }

        // Invoke all listeners
        for (final IThreadDeadlockListener aListener : m_aListeners)
          aListener.onDeadlockDetected (aThreads);

        if (m_aListeners.isEmpty ())
          s_aLogger.warn ("Found a deadlock of " + aThreads.length + " threads but no listeners are present!");
      }
    }
  }

  public ThreadDeadlockDetectionTimer ()
  {
    this (DEFAULT_DEADLOCK_CHECK_PERIOD);
  }

  public ThreadDeadlockDetectionTimer (@Nonnegative final long nDeadlockCheckPeriod)
  {
    m_aTimerTask = new ThreadDeadlockDetectionTask ();
    m_aThreadCheck.schedule (m_aTimerTask, 10, nDeadlockCheckPeriod);
    s_aLogger.info ("Deadlock detector started!");
  }

  /**
   * Cancel the deadlock detection task
   */
  public void stop ()
  {
    if (m_aTimerTask.cancel ())
    {
      s_aLogger.info ("Deadlock detector stopped!");
    }
  }

  @Nonnull
  public EChange addListener (@Nonnull final IThreadDeadlockListener aListener)
  {
    if (aListener == null)
      throw new NullPointerException ("listener");
    return EChange.valueOf (m_aTimerTask.m_aListeners.add (aListener));
  }

  @Nonnull
  public EChange removeListener (@Nullable final IThreadDeadlockListener aListener)
  {
    return EChange.valueOf (m_aTimerTask.m_aListeners.remove (aListener));
  }

  @Nonnull
  public EChange removeAllListeners ()
  {
    if (m_aTimerTask.m_aListeners.isEmpty ())
      return EChange.UNCHANGED;
    m_aTimerTask.m_aListeners.clear ();
    return EChange.CHANGED;
  }

  @Nonnegative
  public int getListenerCount ()
  {
    return m_aTimerTask.m_aListeners.size ();
  }
}
