package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.regex.RegExHelper;

/**
 * A filter that only accepts certain file names, based on a regular expression.
 * If at least one regular expressions is fulfilled, the file is accepted. The
 * filter is applied on directories and files!
 * 
 * @author philip
 */
@ThreadSafe
public final class FilenameFilterMatchAnyRegEx implements FilenameFilter
{
  private final String [] m_aRegExs;

  public FilenameFilterMatchAnyRegEx (@Nonnull @Nonempty final String [] aRegExs)
  {
    if (ArrayHelper.isEmpty (aRegExs))
      throw new IllegalArgumentException ("empty array passed");
    m_aRegExs = aRegExs;
  }

  public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
  {
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    if (sRealName != null)
      for (final String sRegEx : m_aRegExs)
        if (RegExHelper.stringMatchesPattern (sRegEx, sRealName))
          return true;
    return false;
  }
}
