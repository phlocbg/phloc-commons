package com.phloc.commons.concurrent.deadlock;

import java.lang.management.LockInfo;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ThreadDeadlockDetection
{
  protected static final Logger LOG = LoggerFactory.getLogger (ThreadDeadlockDetection.class); // NOPMD
  private static final String SEPARATOR_SINGLE = "------------------------"; //$NON-NLS-1$
  private static final String SEPARATOR_DOUBLE = "======================================"; //$NON-NLS-1$
  protected static final AtomicBoolean USE_MONITOR = new AtomicBoolean (false); // NOPMD
  protected static final AtomicBoolean DEADLOCKED = new AtomicBoolean (false); // NOPMD

  protected final static ThreadDeadlockDetector DETECTOR = new ThreadDeadlockDetector ();

  static
  {
    DETECTOR.addListener (new IThreadDeadlockListener ()
    {
      @Override
      public void deadlockDetected (final Thread [] aDeadlockedThreads)
      {
        DEADLOCKED.getAndSet (true);
        LOG.error (SEPARATOR_DOUBLE);
        LOG.error ("Deadlocked Threads:");//$NON-NLS-1$
        for (final Thread aThread : aDeadlockedThreads)
        {
          LOG.error (SEPARATOR_SINGLE);
          LOG.error (aThread.toString ()); // NOPMDs
          for (final StackTraceElement aSTE : aThread.getStackTrace ())
          {
            LOG.error ("    " + aSTE); //$NON-NLS-1$
          }
        }
        if (USE_MONITOR.get ())
        {
          final ThreadMonitor aMonitor = new ThreadMonitor ();
          aMonitor.findDeadlock ();
        }
        LOG.error (SEPARATOR_DOUBLE);
      }

      @Override
      public void dumpLockInfo (final long nThreadID, final LockInfo [] aLocks)
      {
        LOG.error ("Locks of Thread " + nThreadID); //$NON-NLS-1$
        LOG.error (SEPARATOR_SINGLE);
        LOG.error ("    Locked synchronizers: count = " + aLocks.length); //$NON-NLS-1$
        for (final LockInfo aLockInfo : aLocks)
        {
          LOG.error ("      - " + aLockInfo); //$NON-NLS-1$
        }
        LOG.error (SEPARATOR_DOUBLE);
      }
    });
  }

  private ThreadDeadlockDetection ()
  {
    // private
  }

  public static void start ()
  {
    start (null, false);
  }

  public static void start (final boolean bUseMonitor)
  {
    start (null, bUseMonitor);
  }

  private static void start (final Integer aPeriodMS, final boolean bUseMonitor)
  {
    USE_MONITOR.set (bUseMonitor);
    if (aPeriodMS == null)
    {
      DETECTOR.start ();
    }
    else
    {
      DETECTOR.start (aPeriodMS.intValue ());
    }
  }

  public static void end ()
  {
    DETECTOR.end ();
  }

  public static boolean isDeadLocked ()
  {
    return DEADLOCKED.get ();
  }
}
