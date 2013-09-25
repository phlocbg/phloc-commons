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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.concurrent.ThreadUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.timing.StopWatch;

/**
 * A polling file monitor implementation.<br />
 * <br />
 * The DefaultFileMonitor is a Thread based polling file system monitor with a 1
 * second delay.<br />
 * <br />
 * <b>Design:</b>
 * <p>
 * There is a Map of monitors known as FileMonitorAgents. With the thread
 * running, each FileMonitorAgent object is asked to "check" on the file it is
 * responsible for. To do this check, the cache is cleared.
 * </p>
 * <ul>
 * <li>If the file existed before the refresh and it no longer exists, a delete
 * event is fired.</li>
 * <li>If the file existed before the refresh and it still exists, check the
 * last modified timestamp to see if that has changed.</li>
 * <li>If it has, fire a change event.</li>
 * </ul>
 * <p>
 * With each file delete, the FileMonitorAgent of the parent is asked to
 * re-build its list of children, so that they can be accurately checked when
 * there are new children.<br/>
 * New files are detected during each "check" as each file does a check for new
 * children. If new children are found, create events are fired recursively if
 * recursive descent is enabled.
 * </p>
 * <p>
 * For performance reasons, added a delay that increases as the number of files
 * monitored increases. The default is a delay of 1 second for every 1000 files
 * processed.
 * </p>
 * <br />
 * <b>Example usage:</b><br />
 * 
 * <pre>
 * FileSystemManager fsManager = VFS.getManager();
 * File listendir = fsManager.resolveFile("/home/username/monitored/");
 * <p/>
 * DefaultFileMonitor fm = new DefaultFileMonitor(new CustomFileListener());
 * fm.setRecursive(true);
 * fm.addFile(listendir);
 * fm.start();
 * </pre>
 * 
 * <i>(where CustomFileListener is a class that implements the FileListener
 * interface.)</i>
 * 
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS
 *         team</a>
 * @author Philip Helger
 */
public class FileMonitor implements Runnable
{
  public static final long DEFAULT_DELAY = 1000;
  public static final int DEFAULT_MAX_FILES = 1000;

  private static final Logger s_aLogger = LoggerFactory.getLogger (FileMonitor.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  /**
   * Map from FileName to File being monitored.
   */
  private final Map <String, FileMonitorAgent> m_aMonitorMap = new HashMap <String, FileMonitorAgent> ();

  /**
   * The low priority thread used for checking the files being monitored.
   */
  private Thread m_aMonitorThread;

  /**
   * File objects to be removed from the monitor map.
   */
  private final Stack <File> m_aDeleteStack = new Stack <File> ();

  /**
   * File objects to be added to the monitor map.
   */
  private final Stack <File> m_aAddStack = new Stack <File> ();

  /**
   * A flag used to determine if the monitor thread should be running. used for
   * inter-thread communication
   */
  private volatile boolean m_bShouldRun = true;

  /**
   * A flag used to determine if adding files to be monitored should be
   * recursive.
   */
  private boolean m_bRecursive;

  /**
   * Set the delay between checks
   */
  private long m_nDelay = DEFAULT_DELAY;

  /**
   * Set the number of files to check until a delay will be inserted
   */
  private int m_nChecksPerRun = DEFAULT_MAX_FILES;

  /**
   * A listener object that if set, is notified on file creation and deletion.
   */
  private final IFileListener m_aListener;

  public FileMonitor (@Nonnull final IFileListener aListener)
  {
    if (aListener == null)
      throw new NullPointerException ("Listener");
    m_aListener = aListener;
  }

  /**
   * Access method to get the recursive setting when adding files for
   * monitoring.
   * 
   * @return <code>true</code> if monitoring is enabled for children.
   */
  public boolean isRecursive ()
  {
    return m_bRecursive;
  }

  /**
   * Access method to set the recursive setting when adding files for
   * monitoring.
   * 
   * @param bRecursive
   *        true if monitoring should be enabled for children.
   * @return this
   */
  @Nonnull
  public FileMonitor setRecursive (final boolean bRecursive)
  {
    m_bRecursive = bRecursive;
    return this;
  }

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
  public FileMonitor setDelay (final long nDelay)
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
  public FileMonitor setChecksPerRun (final int nChecksPerRun)
  {
    m_nChecksPerRun = nChecksPerRun;
    return this;
  }

  /**
   * Adds a file to be monitored.
   * 
   * @param aFile
   *        The File to add.
   * @param bRecursive
   *        Scan recursively?
   * @return {@link EChange}
   */
  @Nonnull
  private EChange _recursiveAddFile (@Nonnull final File aFile, final boolean bRecursive)
  {
    final String sKey = aFile.getAbsolutePath ();
    m_aRWLock.readLock ().lock ();
    try
    {
      // Check if already contained
      if (m_aMonitorMap.containsKey (sKey))
        return EChange.UNCHANGED;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Try again in write lock
      if (m_aMonitorMap.containsKey (sKey))
        return EChange.UNCHANGED;

      m_aMonitorMap.put (sKey, new FileMonitorAgent (this, aFile));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (bRecursive)
    {
      // Traverse the children depth first
      final File [] aChildren = aFile.listFiles ();
      if (aChildren != null)
        for (final File aChild : aChildren)
          _recursiveAddFile (aChild, true);
    }

    return EChange.CHANGED;
  }

  /**
   * Adds a file to be monitored.
   * 
   * @param aFile
   *        The File to monitor.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange addFile (@Nonnull final File aFile)
  {
    // Not recursive, because the direct children are added anyway
    if (_recursiveAddFile (aFile, false).isUnchanged ())
      return EChange.UNCHANGED;

    // Traverse the direct children anyway
    final File [] aChildren = aFile.listFiles ();
    if (aChildren != null)
      for (final File aChild : aChildren)
        _recursiveAddFile (aChild, m_bRecursive);

    m_aRWLock.readLock ().lock ();
    try
    {
      s_aLogger.info ("Added " +
                      (m_bRecursive ? "recursive " : "") +
                      "monitoring for file changes in " +
                      aFile.getAbsolutePath () +
                      " - monitoring " +
                      m_aMonitorMap.size () +
                      " files and directories in total");
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
    return EChange.CHANGED;
  }

  /**
   * Removes a file from being monitored.
   * 
   * @param aFile
   *        The File to remove from monitoring.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange removeFile (@Nonnull final File aFile)
  {
    final String sKey = aFile.getAbsolutePath ();
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMonitorMap.remove (sKey) == null)
      {
        // File not monitored
        return EChange.UNCHANGED;
      }
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    m_aRWLock.readLock ().lock ();
    try
    {
      s_aLogger.info ("Removed " +
                      (m_bRecursive ? "recursive " : "") +
                      "monitoring for file changes in " +
                      aFile.getAbsolutePath () +
                      " - monitoring " +
                      m_aMonitorMap.size () +
                      " files and directories in total");
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    final File aParent = aFile.getParentFile ();
    if (aParent != null)
    {
      // Not the root
      final String sParentKey = aParent.getAbsolutePath ();
      FileMonitorAgent aParentAgent;
      m_aRWLock.readLock ().lock ();
      try
      {
        aParentAgent = m_aMonitorMap.get (sParentKey);
      }
      finally
      {
        m_aRWLock.readLock ().unlock ();
      }
      if (aParentAgent != null)
        aParentAgent.resetChildrenList ();
    }
    return EChange.CHANGED;
  }

  /**
   * Queues a file for addition to be monitored.
   * 
   * @param aFile
   *        The File to add.
   */
  void onFileCreated (@Nonnull final File aFile)
  {
    try
    {
      m_aListener.onFileCreated (new FileChangeEvent (aFile));
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to invoke onFileCreated listener", t);
    }

    m_aAddStack.push (aFile);
  }

  /**
   * Queues a file for removal from being monitored.
   * 
   * @param aFile
   *        The File to be removed from being monitored.
   */
  void onFileDeleted (@Nonnull final File aFile)
  {
    try
    {
      m_aListener.onFileDeleted (new FileChangeEvent (aFile));
    }
    catch (final Throwable t)
      {
      s_aLogger.error ("Failed to invoke onFileDeleted listener", t);
    }

    m_aDeleteStack.push (aFile);
  }

  /**
   * Call on modification
   * 
   * @param aFile
   *        The File that was changed
   */
  void onFileChanged (@Nonnull final File aFile)
  {
    try
    {
      m_aListener.onFileChanged (new FileChangeEvent (aFile));
    }
    catch (final Throwable t)
      {
      s_aLogger.error ("Failed to invoke onFileChanged listener", t);
    }
  }

  /**
   * Starts monitoring the files that have been added.
   */
  public void start ()
  {
    if (m_aMonitorThread == null)
    {
      m_aMonitorThread = new Thread (this, "DefaultFileMonitor");
      m_aMonitorThread.setDaemon (true);
      m_aMonitorThread.setPriority (Thread.MIN_PRIORITY);
    }
    m_aMonitorThread.start ();
  }

  public boolean isRunning ()
  {
    return m_aMonitorThread != null && m_aMonitorThread.isAlive () && m_bShouldRun;
  }

  /**
   * Stops monitoring the files that have been added.
   */
  public void stop ()
  {
    m_bShouldRun = false;
  }

  /**
   * Asks the agent for each file being monitored to check its file for changes.
   */
  public void run ()
  {
    mainloop: while (!m_aMonitorThread.isInterrupted () && m_bShouldRun)
    {
      // Remove listener for all deleted files
      while (!m_aDeleteStack.isEmpty ())
        removeFile (m_aDeleteStack.pop ());

      final StopWatch aSW = new StopWatch (true);

      // Create a copy to avoid concurrent modification
      final Collection <FileMonitorAgent> aMonitors;
      m_aRWLock.readLock ().lock ();
      try
      {
        aMonitors = ContainerHelper.newList (m_aMonitorMap.values ());
      }
      finally
      {
        m_aRWLock.readLock ().unlock ();
      }

      int nFileNameIndex = 0;
      for (final FileMonitorAgent aMonitor : aMonitors)
      {
        aMonitor.checkForModifications ();

        final int nChecksPerRun = getChecksPerRun ();
        if (nChecksPerRun > 0 && (nFileNameIndex % nChecksPerRun) == 0)
          ThreadUtils.sleep (getDelay ());

        if (m_aMonitorThread.isInterrupted () || !m_bShouldRun)
          continue mainloop;

        ++nFileNameIndex;
      }
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Checking for file modifications took " + aSW.stopAndGetMillis () + " ms");

      // Add listener for all added files
      while (!m_aAddStack.isEmpty ())
        addFile (m_aAddStack.pop ());

      ThreadUtils.sleep (getDelay ());
    }

    m_bShouldRun = true;
  }
}
