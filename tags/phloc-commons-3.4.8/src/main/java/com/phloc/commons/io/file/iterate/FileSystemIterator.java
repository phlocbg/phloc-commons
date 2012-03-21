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
package com.phloc.commons.io.file.iterate;

import java.io.File;
import java.io.FileFilter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.filter.collections.FilterIterator;
import com.phloc.commons.io.file.filter.FileFilterToIFilterAdapter;

/**
 * Iterate over the content of a single directory. Iteration is <b>not</b>
 * recursive.
 * 
 * @author philip
 */
@NotThreadSafe
public final class FileSystemIterator extends IterableIterator <File>
{
  @Nonnull
  private static File [] _getFileList (@Nonnull final File aBaseDir)
  {
    final File [] ret = aBaseDir.listFiles ();
    return ret != null ? ret : new File [0];
  }

  /**
   * Constructor.
   * 
   * @param aBaseDir
   *        The base directory to iterate. May not be <code>null</code>.
   */
  public FileSystemIterator (@Nonnull final File aBaseDir)
  {
    super (_getFileList (aBaseDir));
  }

  /**
   * Create a new non-recursive file system iterator that uses a certain filter.
   * 
   * @param fBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFilter
   *        The filter to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir, @Nonnull final FileFilter aFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir), new FileFilterToIFilterAdapter (aFilter));
  }

  /**
   * Create a new non-recursive file system iterator that uses multiples filters
   * that all need to match.
   * 
   * @param fBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFilters
   *        The filters to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir, @Nonnull final FileFilter... aFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir),
                                      FileFilterToIFilterAdapter.getANDChained (aFilters));
  }
}
