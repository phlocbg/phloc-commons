package com.phloc.commons.io.monitor;

import javax.annotation.Nonnull;

/**
 * Listens for changes to a file.
 * 
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS
 *         team</a>
 * @author Philip Helger
 */
public interface IFileListener
{
  /**
   * Called when a file is created.
   * 
   * @param event
   *        The FileChangeEvent.
   * @throws Exception
   *         if an error occurs.
   */
  void fileCreated (@Nonnull FileChangeEvent event) throws Exception;

  /**
   * Called when a file is deleted.
   * 
   * @param event
   *        The FileChangeEvent.
   * @throws Exception
   *         if an error occurs.
   */
  void fileDeleted (@Nonnull FileChangeEvent event) throws Exception;

  /**
   * Called when a file is changed.
   * 
   * @param event
   *        The FileChangeEvent.
   * @throws Exception
   *         if an error occurs.
   */
  void fileChanged (@Nonnull FileChangeEvent event) throws Exception;
}
