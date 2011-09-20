/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;

/**
 * Contains methods to create default {@link FilenameFilter} objects.
 * 
 * @author philip
 */
@Immutable
public final class FilenameFilterFactory
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final FilenameFilterFactory s_aInstance = new FilenameFilterFactory ();

  private FilenameFilterFactory ()
  {}

  /**
   * Get a file filter that checks whether a file has the specified extension.
   * The implementation is done via {@link String#endsWith(String)} so it is
   * case sensitive.
   * 
   * @param sExt
   *        The extension to use. May neither be <code>null</code> nor empty.
   * @return A non-<code>null</code> file name filter.
   */
  @Nonnull
  public static FilenameFilter getEndsWithFilter (@Nonnull final String sExt)
  {
    if (StringHelper.hasNoText (sExt))
      throw new IllegalArgumentException ("extension may not be empty");

    return new FilenameFilter ()
    {
      public boolean accept (@Nullable final File aDir, @Nullable final String sName)
      {
        final String sRealName = FilenameHelper.getSecureFilename (sName);
        return sRealName != null && sRealName.endsWith (sExt);
      }
    };
  }

  /**
   * @return A filter that accepts all public directories (directories who's
   *         name does not start with a dot!)
   */
  @Nonnull
  public static FilenameFilter getPublicDirectoryFilter ()
  {
    return new FilenameFilter ()
    {
      public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
      {
        // Ignore hidden directories
        final String sRealName = FilenameHelper.getSecureFilename (sName);
        return new File (aDir, sRealName).isDirectory () && !StringHelper.startsWith (sRealName, '.');
      }
    };
  }

  /**
   * @return A filter that only accepts certain file names, based on a regular
   *         expression. If at least one regular expressions is fulfilled, the
   *         file is accepted. The filter is applied on directories and files!
   */
  @Nonnull
  public static FilenameFilter getMatchRegExFilter (@Nonnull @Nonempty final String... aRegExs)
  {
    if (ArrayHelper.isEmpty (aRegExs))
      throw new IllegalArgumentException ("empty array passed");

    return new FilenameFilter ()
    {
      public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
      {
        final String sRealName = FilenameHelper.getSecureFilename (sName);
        if (sRealName != null)
          for (final String sRegEx : aRegExs)
            if (RegExHelper.stringMatchesPattern (sRegEx, sRealName))
              return true;
        return false;
      }
    };
  }

  /**
   * @return A filter that rejects certain file names, based on a regular
   *         expression. If at least one regular expressions is fulfilled, the
   *         file is rejected. The filter is applied on directories and files!
   */
  @Nonnull
  public static FilenameFilter getIgnoreMatchRegExFilter (@Nonnull @Nonempty final String... aRegExs)
  {
    if (ArrayHelper.isEmpty (aRegExs))
      throw new IllegalArgumentException ("empty array passed");

    return new FilenameFilter ()
    {
      public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
      {
        final String sRealName = FilenameHelper.getSecureFilename (sName);
        if (sRealName == null)
          return false;
        for (final String sRegEx : aRegExs)
          if (RegExHelper.stringMatchesPattern (sRegEx, sRealName))
            return false;
        return true;
      }
    };
  }
}
