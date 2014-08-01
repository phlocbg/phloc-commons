/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.io.file.iterate;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.filter.FilterChainAND;
import com.phloc.commons.filter.collections.FilterIterator;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.filter.IFileFilter;

/**
 * Iterate over the content of a single directory. Iteration is <b>not</b>
 * recursive.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public final class FileSystemIterator extends IterableIterator <File>
{
  /**
   * Constructor.
   *
   * @param sBaseDir
   *        The base directory to iterate. May not be <code>null</code>.
   */
  public FileSystemIterator (@Nonnull final String sBaseDir)
  {
    this (new File (sBaseDir));
  }

  /**
   * Constructor.
   *
   * @param aBaseDir
   *        The base directory to iterate. May not be <code>null</code>.
   */
  public FileSystemIterator (@Nonnull final File aBaseDir)
  {
    super (FileUtils.getDirectoryContent (aBaseDir));
  }

  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final String sBaseDir,
                                                 @Nonnull final IFileFilter aFilenameFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (sBaseDir), aFilenameFilter);
  }

  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir,
                                                 @Nonnull final IFileFilter aFilenameFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir), aFilenameFilter);
  }

  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final String sBaseDir,
                                                 @Nonnull final IFileFilter... aFilenameFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (sBaseDir), new FilterChainAND <> (aFilenameFilters));
  }

  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File aBaseDir,
                                                 @Nonnull final IFileFilter... aFilenameFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (aBaseDir), new FilterChainAND <> (aFilenameFilters));
  }
}
