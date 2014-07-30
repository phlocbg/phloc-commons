/**
 * Copyright (C) 2006-2014 phloc systems
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
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.state.EChange;

/**
 * This is the main dead lock detector, based on JMX {@link ThreadMXBean}
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class ThreadDeadlockDetector
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ThreadDeadlockDetector.class);
  private final ThreadMXBean m_aMBean = ManagementFactory.getThreadMXBean ();
  private final Set <IThreadDeadlockListener> m_aListeners = new CopyOnWriteArraySet <IThreadDeadlockListener> ();

  public void run ()
  {
    long [] aThreadIDs;
    // IFJDK5
    // aThreadIDs = m_aMBean.findMonitorDeadlockedThreads ();
    // ELSE
    aThreadIDs = m_aMBean.isSynchronizerUsageSupported () ? m_aMBean.findDeadlockedThreads ()
                                                         : m_aMBean.findMonitorDeadlockedThreads ();
    // ENDIF
    if (ArrayHelper.isNotEmpty (aThreadIDs))
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

  @Nonnull
  public EChange addListener (@Nonnull final IThreadDeadlockListener aListener)
  {
    ValueEnforcer.notNull (aListener, "Listener");

    return EChange.valueOf (m_aListeners.add (aListener));
  }

  @Nonnull
  public EChange removeListener (@Nullable final IThreadDeadlockListener aListener)
  {
    return EChange.valueOf (m_aListeners.remove (aListener));
  }

  @Nonnull
  public EChange removeAllListeners ()
  {
    if (m_aListeners.isEmpty ())
      return EChange.UNCHANGED;
    m_aListeners.clear ();
    return EChange.CHANGED;
  }

  @Nonnegative
  public int getListenerCount ()
  {
    return m_aListeners.size ();
  }
}
