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

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;

/**
 * A polling file monitor implementation. Use
 * {@link FileMonitorManager#createFileMonitor(IFileListener)} to use this class
 * effectively.
 * 
 * @author Philip Helger
 */
public class FileMonitor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FileMonitor.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  /**
   * Map from filename to File being monitored.
   */
  private final Map <String, FileMonitorAgent> m_aMonitorMap = new HashMap <String, FileMonitorAgent> ();

  /**
   * File objects to be removed from the monitor map.
   */
  private final Stack <File> m_aDeleteStack = new Stack <File> ();

  /**
   * File objects to be added to the monitor map.
   */
  private final Stack <File> m_aAddStack = new Stack <File> ();

  /**
   * A flag used to determine if adding files to be monitored should be
   * recursive.
   */
  private boolean m_bRecursive;

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
  public EChange addMonitoredFile (@Nonnull final File aFile)
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
  public EChange removeMonitoredFile (@Nonnull final File aFile)
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

  @Nonnull
  @ReturnsMutableCopy
  Collection <FileMonitorAgent> getAllAgents ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aMonitorMap.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  void applyPendingRemovals ()
  {
    // Remove listener for all deleted files
    while (!m_aDeleteStack.isEmpty ())
      removeMonitoredFile (m_aDeleteStack.pop ());
  }

  void applyPendingAdds ()
  {
    // Add listener for all added files
    while (!m_aAddStack.isEmpty ())
      addMonitoredFile (m_aAddStack.pop ());
  }
}
