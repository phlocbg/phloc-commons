package com.phloc.commons.io.monitor;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

/**
 * An event fired when a file is changed.
 * 
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS
 *         team</a>
 * @author Philip Helger
 */
public class FileChangeEvent
{
  /**
   * The file object
   */
  private final File m_aFile;

  public FileChangeEvent (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");
    m_aFile = aFile;
  }

  /**
   * Returns the file that changed.
   * 
   * @return The file that was changed. Never <code>null</code>.
   */
  @Nonnull
  public File getFile ()
  {
    return m_aFile;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("file", m_aFile).toString ();
  }
}
