package com.phloc.commons.io.monitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.NonBlockingStack;

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
public class DefaultFileMonitor implements Runnable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultFileMonitor.class);
  private static final long DEFAULT_DELAY = 1000;
  private static final int DEFAULT_MAX_FILES = 1000;

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
  private final NonBlockingStack <File> m_aDeleteStack = new NonBlockingStack <File> ();

  /**
   * File objects to be added to the monitor map.
   */
  private final NonBlockingStack <File> m_aAddStack = new NonBlockingStack <File> ();

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

  public DefaultFileMonitor (@Nonnull final IFileListener aListener)
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
   */
  public void setRecursive (final boolean bRecursive)
  {
    m_bRecursive = bRecursive;
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
   */
  public void setDelay (final long nDelay)
  {
    m_nDelay = nDelay > 0 ? nDelay : DEFAULT_DELAY;
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
   */
  public void setChecksPerRun (final int nChecksPerRun)
  {
    m_nChecksPerRun = nChecksPerRun;
  }

  /**
   * Access method to get the current {@link IFileListener} object notified when
   * there are changes with the files added.
   * 
   * @return The listener.
   */
  @Nonnull
  protected final IFileListener getFileListener ()
  {
    return m_aListener;
  }

  /**
   * Adds a file to be monitored.
   * 
   * @param aFile
   *        The File to add.
   */
  private void _internalAddFile (@Nonnull final File aFile)
  {
    synchronized (m_aMonitorMap)
    {
      final String sKey = aFile.getAbsolutePath ();
      if (!m_aMonitorMap.containsKey (sKey))
      {
        m_aMonitorMap.put (sKey, new FileMonitorAgent (this, aFile));
        if (m_bRecursive)
        {
          // Traverse the children
          final File [] aChildren = aFile.listFiles ();
          if (aChildren != null)
            for (final File aChild : aChildren)
            {
              // Add depth first
              addFile (aChild);
            }
        }
      }
    }
  }

  /**
   * Adds a file to be monitored.
   * 
   * @param aFile
   *        The File to monitor.
   */
  public void addFile (@Nonnull final File aFile)
  {
    _internalAddFile (aFile);

    // Traverse the children
    final File [] aChildren = aFile.listFiles ();
    if (aChildren != null)
      for (final File aChild : aChildren)
        _internalAddFile (aChild);
  }

  /**
   * Removes a file from being monitored.
   * 
   * @param aFile
   *        The File to remove from monitoring.
   */
  public void removeFile (@Nonnull final File aFile)
  {
    synchronized (m_aMonitorMap)
    {
      final String sKey = aFile.getAbsolutePath ();
      if (m_aMonitorMap.get (sKey) != null)
      {
        m_aMonitorMap.remove (sKey);

        final File aParent = aFile.getParentFile ();
        if (aParent != null)
        {
          // Not the root
          final FileMonitorAgent aParentAgent = m_aMonitorMap.get (aParent.getAbsolutePath ());
          if (aParentAgent != null)
            aParentAgent.resetChildrenList ();
        }
      }
    }
  }

  /**
   * Queues a file for addition to be monitored.
   * 
   * @param aFile
   *        The File to add.
   */
  void onFileCreated (@Nonnull final File aFile)
  {
    // Don't fire if it's a folder because new file children
    // and deleted files in a folder have their own event triggered.
    // Fire create event
    try
    {
      m_aListener.fileCreated (new FileChangeEvent (aFile));
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to invoke listener", ex);
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
    // Fire delete event
    try
    {
      m_aListener.fileDeleted (new FileChangeEvent (aFile));
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to invoke listener", ex);
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
    // Don't fire if it's a folder because new file children
    // and deleted files in a folder have their own event triggered.
    try
    {
      m_aListener.fileChanged (new FileChangeEvent (aFile));
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to invoke listener", ex);
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

      // For each entry in the map
      String [] aFileKeys;
      synchronized (m_aMonitorMap)
      {
        aFileKeys = m_aMonitorMap.keySet ().toArray (new String [0]);
      }
      for (int nFileNameIndex = 0; nFileNameIndex < aFileKeys.length; nFileNameIndex++)
      {
        final String sKey = aFileKeys[nFileNameIndex];
        FileMonitorAgent aMonitorAgent;
        synchronized (m_aMonitorMap)
        {
          aMonitorAgent = m_aMonitorMap.get (sKey);
        }
        if (aMonitorAgent != null)
          aMonitorAgent.checkForModifications ();

        if (getChecksPerRun () > 0)
        {
          if ((nFileNameIndex % getChecksPerRun ()) == 0)
          {
            try
            {
              Thread.sleep (getDelay ());
            }
            catch (final InterruptedException e)
            {
              // Woke up.
            }
          }
        }

        if (m_aMonitorThread.isInterrupted () || !m_bShouldRun)
          continue mainloop;
      }

      // Add listener for all added files
      while (!m_aAddStack.isEmpty ())
        addFile (m_aAddStack.pop ());

      try
      {
        Thread.sleep (getDelay ());
      }
      catch (final InterruptedException e)
      {
        continue;
      }
    }

    m_bShouldRun = true;
  }
}
