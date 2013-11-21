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
package com.phloc.commons.io.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.concurrent.ThreadUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.timing.StopWatch;

/**
 * This class manages all the available {@link FileMonitor} objects.
 * 
 * @author Philip Helger
 */
public class FileMonitorManager implements Runnable
{
  public static final long DEFAULT_DELAY = 1000;
  public static final int DEFAULT_MAX_FILES = 1000;

  private static final Logger s_aLogger = LoggerFactory.getLogger (FileMonitorManager.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  /**
   * Map from FileName to File being monitored.
   */
  private final List <FileMonitor> m_aMonitorList = new ArrayList <FileMonitor> ();

  /**
   * The low priority thread used for checking the files being monitored.
   */
  private Thread m_aMonitorThread;

  /**
   * A flag used to determine if the monitor thread should be running. used for
   * inter-thread communication
   */
  private volatile boolean m_bShouldRun = true;

  /**
   * Set the delay between checks
   */
  private long m_nDelay = DEFAULT_DELAY;

  /**
   * Set the number of files to check until a delay will be inserted
   */
  private int m_nChecksPerRun = DEFAULT_MAX_FILES;

  public FileMonitorManager ()
  {}

  /**
   * Get the delay between runs.
   * 
   * @return The delay period.
   */
  public long getDelay ()
  {
    return m_nDelay;
  }

  /**
   * Set the delay between runs.
   * 
   * @param nDelay
   *        The delay period.
   * @return this
   */
  @Nonnull
  public FileMonitorManager setDelay (final long nDelay)
  {
    m_nDelay = nDelay > 0 ? nDelay : DEFAULT_DELAY;
    return this;
  }

  /**
   * get the number of files to check per run.
   * 
   * @return The number of files to check per iteration.
   */
  public int getChecksPerRun ()
  {
    return m_nChecksPerRun;
  }

  /**
   * set the number of files to check per run. a additional delay will be added
   * if there are more files to check
   * 
   * @param nChecksPerRun
   *        a value less than 1 will disable this feature
   * @return this
   */
  @Nonnull
  public FileMonitorManager setChecksPerRun (final int nChecksPerRun)
  {
    m_nChecksPerRun = nChecksPerRun;
    return this;
  }

  @Nonnull
  public FileMonitor createFileMonitor (@Nonnull final IFileListener aListener)
  {
    final FileMonitor aMonitor = new FileMonitor (aListener);
    addFileMonitor (aMonitor);
    return aMonitor;
  }

  public void addFileMonitor (@Nonnull final FileMonitor aMonitor)
  {
    if (aMonitor == null)
      throw new NullPointerException ("Monitor");

    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aMonitorList.add (aMonitor);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange removeFileMonitor (@Nonnull final FileMonitor aMonitor)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (m_aMonitorList.remove (aMonitor));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <FileMonitor> getAllFileMonitors ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aMonitorList);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Starts monitoring the files
   * 
   * @throws IllegalStateException
   *         if the monitoring is already running
   * @see #isRunning()
   * @see #stop()
   */
  public void start ()
  {
    if (m_aMonitorThread != null || !m_bShouldRun)
      throw new IllegalStateException ("Thread is already running!");

    m_aMonitorThread = new Thread (this, "FileMonitor");
    m_aMonitorThread.setDaemon (true);
    m_aMonitorThread.setPriority (Thread.MIN_PRIORITY);
    m_aMonitorThread.start ();
    s_aLogger.info ("Started FileMonitor thread");
  }

  /**
   * Stops monitoring the files.
   */
  public void stop ()
  {
    if (m_aMonitorThread != null)
    {
      m_bShouldRun = false;
      // A thread should never be restarted
      m_aMonitorThread = null;
      s_aLogger.info ("Stopped FileMonitor thread");
    }
  }

  /**
   * @return <code>true</code> if the monitoring thread is running,
   *         <code>false</code> if not.
   */
  public boolean isRunning ()
  {
    return m_aMonitorThread != null && m_bShouldRun;
  }

  /**
   * Asks the agent for each file being monitored to check its file for changes.
   */
  public void run ()
  {
    if (m_aMonitorThread != null)
      mainloop: while (!m_aMonitorThread.isInterrupted () && m_bShouldRun)
      {
        final StopWatch aSW = new StopWatch (true);

        // Create a copy to avoid concurrent modification
        int nFileNameIndex = 0;
        for (final FileMonitor aMonitor : getAllFileMonitors ())
        {
          // Remove listener for all deleted files
          aMonitor.applyPendingRemovals ();

          // For all monitored files
          for (final FileMonitorAgent aAgent : aMonitor.getAllAgents ())
          {
            aAgent.checkForModifications ();

            final int nChecksPerRun = getChecksPerRun ();

            if (nChecksPerRun > 0 && (nFileNameIndex % nChecksPerRun) == 0)
              ThreadUtils.sleep (getDelay ());

            if (m_aMonitorThread.isInterrupted () || !m_bShouldRun)
              continue mainloop;

            ++nFileNameIndex;
          }

          // Add listener for all added files
          aMonitor.applyPendingAdds ();
        }

        ThreadUtils.sleep (getDelay ());

        if (s_aLogger.isDebugEnabled ())
          s_aLogger.debug ("Checking for file modifications took " + aSW.stopAndGetMillis () + " ms");
      }

    // Allow for restart
    m_bShouldRun = true;
  }
}
