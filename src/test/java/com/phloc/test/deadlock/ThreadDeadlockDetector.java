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
package com.phloc.test.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.collections.ArrayHelper;

public final class ThreadDeadlockDetector
{
  /**
   * This is called whenever a problem with threads is detected.
   */
  public interface ThreadDeadlockListener
  {
    void deadlockDetected (Thread [] aDeadlockedThreads);
  }

  private final Timer m_aThreadCheck = new Timer ("ThreadDeadlockDetector", true);
  private final ThreadMXBean m_aMBean = ManagementFactory.getThreadMXBean ();
  private final Collection <ThreadDeadlockListener> m_aListeners = new CopyOnWriteArraySet <ThreadDeadlockListener> ();

  /**
   * The number of milliseconds between checking for deadlocks. It may be
   * expensive to check for deadlocks, and it is not critical to know so
   * quickly.
   */
  private static final long DEFAULT_DEADLOCK_CHECK_PERIOD = 10 * CGlobal.MILLISECONDS_PER_SECOND;

  public ThreadDeadlockDetector ()
  {
    this (DEFAULT_DEADLOCK_CHECK_PERIOD);
  }

  public ThreadDeadlockDetector (@Nonnegative final long nDeadlockCheckPeriod)
  {
    m_aThreadCheck.schedule (new TimerTask ()
    {
      @Override
      public void run ()
      {
        final long [] aThreadIDs = m_aMBean.isSynchronizerUsageSupported () ? m_aMBean.findDeadlockedThreads ()
                                                                           : m_aMBean.findMonitorDeadlockedThreads ();
        if (!ArrayHelper.isEmpty (aThreadIDs))
        {
          final Map <Thread, StackTraceElement []> aAllStackTraces = Thread.getAllStackTraces ();
          final Thread [] aThreads = new Thread [aThreadIDs.length];
          for (int i = 0; i < aThreads.length; i++)
          {
            // ThreadInfo
            final ThreadInfo aThreadInfo = m_aMBean.getThreadInfo (aThreadIDs[i]);
            // Find matching thread
            for (final Thread aThread : aAllStackTraces.keySet ())
              if (aThread.getId () == aThreadInfo.getThreadId ())
              {
                aThreads[i] = aThread;
                break;
              }
            if (aThreads[i] == null)
              throw new IllegalStateException ("Deadlocked Thread not found as defined by " + aThreadInfo);
          }

          // Invoke all listeners
          for (final ThreadDeadlockListener aListener : m_aListeners)
            aListener.deadlockDetected (aThreads);
        }
      }
    }, 10, nDeadlockCheckPeriod);
  }

  public boolean addListener (@Nonnull final ThreadDeadlockListener aListener)
  {
    if (aListener == null)
      throw new NullPointerException ("listener");
    return m_aListeners.add (aListener);
  }

  public boolean removeListener (@Nonnull final ThreadDeadlockListener aListener)
  {
    return m_aListeners.remove (aListener);
  }
}
