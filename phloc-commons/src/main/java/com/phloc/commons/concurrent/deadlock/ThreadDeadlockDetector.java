package com.phloc.commons.concurrent.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadDeadlockDetector
{
  private final Timer m_aThreadCheck = new Timer ("ThreadDeadlockDetector", true); //$NON-NLS-1$
  private final ThreadMXBean m_aMBean = ManagementFactory.getThreadMXBean ();
  private final Collection <IThreadDeadlockListener> m_aListeners = new CopyOnWriteArraySet <IThreadDeadlockListener> ();
  private static final Logger LOG = LoggerFactory.getLogger (ThreadDeadlockDetector.class);
  /**
   * The number of milliseconds between checking for deadlocks. It may be
   * expensive to check for deadlocks, and it is not critical to know so
   * quickly.
   */
  private static final int DEFAULT_DEADLOCK_CHECK_PERIOD = 10000;

  public void start ()
  {
    start (DEFAULT_DEADLOCK_CHECK_PERIOD);
  }

  public void start (final int nPeriodMS)
  {
    LOG.info ("DEAD LOCK DETECTION is ACTIVE at a rate of " + nPeriodMS); //$NON-NLS-1$
    this.m_aThreadCheck.schedule (new TimerTask ()
    {
      @Override
      public void run ()
      {
        checkForDeadlocks ();
      }
    }, 10, nPeriodMS);
  }

  public void end ()
  {
    this.m_aThreadCheck.cancel ();
    LOG.info ("DEAD LOCK DETECTION is INACTIVE"); //$NON-NLS-1$
  }

  protected void checkForDeadlocks ()
  {
    final long [] ids = findDeadlockedThreads ();
    if (ids != null && ids.length > 0)
    {
      final Thread [] threads = new Thread [ids.length];
      for (int i = 0; i < threads.length; i++)
      {
        threads[i] = findMatchingThread (this.m_aMBean.getThreadInfo (ids[i]));
      }
      fireDeadlockDetected (threads);
    }
  }

  private long [] findDeadlockedThreads ()
  {
    long [] aThreads = null;
    if (this.m_aMBean.isSynchronizerUsageSupported ())
    {
      try
      {
        aThreads = this.m_aMBean.findDeadlockedThreads ();
      }
      catch (final SecurityException aEx)
      {
        // ignore
      }
    }
    if (aThreads != null && aThreads.length > 0)
    {
      return aThreads;
    }
    try
    {
      return this.m_aMBean.findMonitorDeadlockedThreads ();
    }
    catch (final SecurityException aEx)
    {
      return null;
    }
  }

  private void fireDeadlockDetected (final Thread [] threads)
  {

    final long [] aIDs = new long [threads.length];
    int nIndex = 0;
    for (final Thread aThread : threads)
    {
      aIDs[nIndex++] = aThread.getId ();
    }
    final ThreadInfo [] aThreadInfo = this.m_aMBean.getThreadInfo (aIDs);

    for (final IThreadDeadlockListener aListener : this.m_aListeners)
    {
      aListener.deadlockDetected (threads);

      for (final ThreadInfo ti : aThreadInfo)
      {
        aListener.dumpLockInfo (ti.getThreadId (), ti.getLockedSynchronizers ());
      }

    }
  }

  @Nonnull
  private static Thread findMatchingThread (final ThreadInfo inf)
  {
    for (final Thread aThread : Thread.getAllStackTraces ().keySet ())
    {
      if (aThread.getId () == inf.getThreadId ())
      {
        return aThread;
      }
    }
    throw new IllegalStateException ("Deadlocked Thread not found"); //$NON-NLS-1$
  }

  public boolean addListener (@Nonnull final IThreadDeadlockListener aListener)
  {
    return this.m_aListeners.add (aListener);
  }

  public boolean removeListener (@Nonnull final IThreadDeadlockListener aListener)
  {
    return this.m_aListeners.remove (aListener);
  }
}
