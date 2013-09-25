package com.phloc.commons.io.monitor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.NonBlockingStack;

/**
 * File monitor agent.
 * 
 * @author commons-vfs
 * @author Philip Helger
 */
final class FileMonitorAgent
{
  private final DefaultFileMonitor m_aMonitor;
  private final File m_aFile;

  private boolean m_bExists;
  private long m_nTimestamp;
  private final Set <String> m_aChildren = new HashSet <String> ();

  FileMonitorAgent (@Nonnull final DefaultFileMonitor aMonitor, @Nonnull final File aFile)
  {
    m_aMonitor = aMonitor;
    m_aFile = aFile;
    m_bExists = aFile.exists ();
    if (m_bExists)
    {
      m_nTimestamp = aFile.lastModified ();
      resetChildrenList ();
    }
    else
      m_nTimestamp = -1;
  }

  void resetChildrenList ()
  {
    final File [] aChildren = m_aFile.listFiles ();
    if (aChildren != null)
      for (final File aChild : aChildren)
        m_aChildren.add (aChild.getAbsolutePath ());
  }

  /**
   * Recursively fires create events for all children if recursive descent is
   * enabled. Otherwise the create event is only fired for the initial File.
   * 
   * @param aCreatedFile
   *        The child to add.
   */
  private void _onFileCreateRecursive (@Nonnull final File aCreatedFile)
  {
    // Add
    m_aMonitor.onFileCreated (aCreatedFile);

    if (m_aMonitor.isRecursive ())
    {
      final File [] aChildren = aCreatedFile.listFiles ();
      if (aChildren != null)
        for (final File aChild : aChildren)
          _onFileCreateRecursive (aChild);
    }
  }

  /**
   * Only checks for new children. If children are removed, they'll eventually
   * be checked.
   */
  private void _checkForNewChildren ()
  {
    // See which new children are not listed in the current children
    // map.
    final Set <String> aNewChildren = new HashSet <String> ();
    final NonBlockingStack <File> aCreatedChildren = new NonBlockingStack <File> ();

    final File [] aNewChildrenList = m_aFile.listFiles ();
    if (aNewChildrenList != null)
      for (final File aNewChild : aNewChildrenList)
      {
        final String sKey = aNewChild.getAbsolutePath ();
        aNewChildren.add (sKey);
        // If the child's not there
        if (!m_aChildren.contains (sKey))
          aCreatedChildren.push (aNewChild);
      }

    m_aChildren.clear ();
    m_aChildren.addAll (aNewChildren);

    // If there were missing children
    while (!aCreatedChildren.isEmpty ())
      _onFileCreateRecursive (aCreatedChildren.pop ());
  }

  void checkForModifications ()
  {
    final boolean bExistsNow = m_aFile.exists ();
    if (m_bExists)
    {
      // File previously existed
      if (!bExistsNow)
      {
        // The file was deleted
        m_bExists = bExistsNow;
        m_nTimestamp = -1;

        // Mark it as deleted
        m_aMonitor.onFileDeleted (m_aFile);
      }
      else
      {
        // File previously existed and still exists

        // Check the timestamp to see if it has been modified
        final long nNewTimestamp = m_aFile.lastModified ();
        if (m_nTimestamp != nNewTimestamp)
        {
          m_nTimestamp = nNewTimestamp;
          m_aMonitor.onFileChanged (m_aFile);
        }
      }
    }
    else
    {
      if (bExistsNow)
      {
        // File was created
        m_bExists = bExistsNow;
        m_nTimestamp = m_aFile.lastModified ();
        m_aMonitor.onFileCreated (m_aFile);
      }
    }

    _checkForNewChildren ();
  }
}
