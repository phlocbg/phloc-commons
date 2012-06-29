package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;

/**
 * A filter that accepts all public directories (directories who's name does not
 * start with a dot!)
 * 
 * @author philip
 */
@ThreadSafe
public final class FilenameFilterPublicDirectory implements FilenameFilter
{
  public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
  {
    // Ignore hidden directories
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    return new File (aDir, sRealName).isDirectory () && !StringHelper.startsWith (sRealName, '.');
  }
}
