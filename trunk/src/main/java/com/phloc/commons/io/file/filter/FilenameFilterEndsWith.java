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
 * A filename filter that checks whether a file has the specified extension. The
 * implementation is done via {@link String#endsWith(String)} so it is case
 * sensitive.
 * 
 * @author philip
 */
@ThreadSafe
public final class FilenameFilterEndsWith implements FilenameFilter
{
  private final String m_sExt;

  /**
   * @param sExt
   *        The extension to use. May neither be <code>null</code> nor empty.
   */
  public FilenameFilterEndsWith (@Nonnull @Nonempty final String sExt)
  {
    if (StringHelper.hasNoText (sExt))
      throw new IllegalArgumentException ("extension may not be empty");
    m_sExt = sExt;
  }

  public boolean accept (@Nullable final File aDir, @Nullable final String sName)
  {
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    return sRealName != null && sRealName.endsWith (m_sExt);
  }
}
