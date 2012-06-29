/**
 * Copyright (C) 2006-2012 phloc systems
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

import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Contains methods to create default {@link FilenameFilter} objects.
 * 
 * @author philip
 */
@Immutable
@Deprecated
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
  @Deprecated
  public static FilenameFilter getEndsWithFilter (@Nonnull final String sExt)
  {
    return new FilenameFilterEndsWith (sExt);
  }

  /**
   * @return A filter that accepts all public directories (directories who's
   *         name does not start with a dot!)
   */
  @Nonnull
  @Deprecated
  public static FilenameFilter getPublicDirectoryFilter ()
  {
    return new FilenameFilterPublicDirectory ();
  }

  /**
   * @return A filter that only accepts certain file names, based on a regular
   *         expression. If at least one regular expressions is fulfilled, the
   *         file is accepted. The filter is applied on directories and files!
   */
  @Nonnull
  @Deprecated
  public static FilenameFilter getMatchRegExFilter (@Nonnull @Nonempty final String... aRegExs)
  {
    return new FilenameFilterMatchAnyRegEx (aRegExs);
  }

  /**
   * @return A filter that rejects certain file names, based on a regular
   *         expression. If at least one regular expressions is fulfilled, the
   *         file is rejected. The filter is applied on directories and files!
   */
  @Nonnull
  @Deprecated
  public static FilenameFilter getIgnoreMatchRegExFilter (@Nonnull @Nonempty final String... aRegExs)
  {
    return new FilenameFilterMatchNoRegEx (aRegExs);
  }
}
