package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;

/**
 * A filename filter that checks whether a file has the specified name. The
 * implementation is done via {@link String#equalsIgnoreCase(String)} so it is
 * case insensitive.
 * 
 * @author philip
 */
@ThreadSafe
public final class FilenameFilterEqualsIgnoreCase implements FilenameFilter
{
  private final String m_sFilename;

  /**
   * @param sFilename
   *        The extension to use. May neither be <code>null</code> nor empty.
   */
  public FilenameFilterEqualsIgnoreCase (@Nonnull @Nonempty final String sFilename)
  {
    if (StringHelper.hasNoText (sFilename))
      throw new IllegalArgumentException ("filename");
    m_sFilename = sFilename;
  }

  public boolean accept (@Nullable final File aDir, @Nullable final String sName)
  {
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    return sRealName != null && sRealName.equalsIgnoreCase (m_sFilename);
  }
}
