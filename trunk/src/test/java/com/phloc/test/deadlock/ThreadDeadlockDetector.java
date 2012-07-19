package com.phloc.test.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnull;

public final class ThreadDeadlockDetector
{
  /**
   * This is called whenever a problem with threads is detected.
   */
  public interface Listener
  {
    void deadlockDetected (Thread [] aDeadlockedThreads);
  }

  private final Timer m_aThreadCheck = new Timer ("ThreadDeadlockDetector", true);
  private final ThreadMXBean m_aMBean = ManagementFactory.getThreadMXBean ();
  private final Collection <Listener> m_aListeners = new CopyOnWriteArraySet <Listener> ();

  /**
   * The number of milliseconds between checking for deadlocks. It may be
   * expensive to check for deadlocks, and it is not critical to know so
   * quickly.
   */
  private static final int DEFAULT_DEADLOCK_CHECK_PERIOD = 10000;

  public ThreadDeadlockDetector ()
  {
    this (DEFAULT_DEADLOCK_CHECK_PERIOD);
  }

  public ThreadDeadlockDetector (final int nDeadlockCheckPeriod)
  {
    m_aThreadCheck.schedule (new TimerTask ()
    {
      @Override
      public void run ()
      {
        final long [] ids = m_aMBean.isSynchronizerUsageSupported () ? m_aMBean.findDeadlockedThreads ()
                                                                    : m_aMBean.findMonitorDeadlockedThreads ();
        if (ids != null && ids.length > 0)
        {
          final Thread [] aThreads = new Thread [ids.length];
          for (int i = 0; i < aThreads.length; i++)
          {
            // ThreadInfo
            final ThreadInfo aThreadInfo = m_aMBean.getThreadInfo (ids[i]);
            // Find matching thread
            for (final Thread aThread : Thread.getAllStackTraces ().keySet ())
              if (aThread.getId () == aThreadInfo.getThreadId ())
              {
                aThreads[i] = aThread;
                break;
              }
            if (aThreads[i] == null)
              throw new IllegalStateException ("Deadlocked Thread not found");
          }

          // Invoke all listeners
          for (final Listener aListener : m_aListeners)
            aListener.deadlockDetected (aThreads);
        }
      }
    },
                             10,
                             nDeadlockCheckPeriod);
  }

  public boolean addListener (@Nonnull final Listener aListener)
  {
    return m_aListeners.add (aListener);
  }

  public boolean removeListener (@Nonnull final Listener aListener)
  {
    return m_aListeners.remove (aListener);
  }
}
