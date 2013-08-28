/**
 * Copyright (C) 2006-2013 phloc systems
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
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.filter.collections.FilterIterator;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.filter.FileFilterToIFilterAdapter;

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

  /**
   * Create a new non-recursive file system iterator that uses a certain
   * {@link FilenameFilter}.
   * 
   * @param sBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFilenameFilter
   *        The filter to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final String sBaseDir,
                                                 @Nonnull final FilenameFilter aFilenameFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (sBaseDir),
                                      new FileFilterToIFilterAdapter (aFilenameFilter));
  }

  /**
   * Create a new non-recursive file system iterator that uses a certain
   * {@link FilenameFilter}.
   * 
   * @param fBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFilenameFilter
   *        The filter to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir,
                                                 @Nonnull final FilenameFilter aFilenameFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir),
                                      new FileFilterToIFilterAdapter (aFilenameFilter));
  }

  /**
   * Create a new non-recursive file system iterator that uses multiple
   * {@link FilenameFilter} objects that all need to match.
   * 
   * @param sBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFilenameFilters
   *        The filters to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final String sBaseDir,
                                                 @Nonnull final FilenameFilter... aFilenameFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (sBaseDir),
                                      FileFilterToIFilterAdapter.getANDChained (aFilenameFilters));
  }

  /**
   * Create a new non-recursive file system iterator that uses multiple
   * {@link FilenameFilter} objects that all need to match.
   * 
   * @param fBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFilenameFilters
   *        The filters to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir,
                                                 @Nonnull final FilenameFilter... aFilenameFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir),
                                      FileFilterToIFilterAdapter.getANDChained (aFilenameFilters));
  }

  /**
   * Create a new non-recursive file system iterator that uses a certain
   * {@link FileFilter}.
   * 
   * @param sBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFileFilter
   *        The filter to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final String sBaseDir, @Nonnull final FileFilter aFileFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (sBaseDir), new FileFilterToIFilterAdapter (aFileFilter));
  }

  /**
   * Create a new non-recursive file system iterator that uses a certain
   * {@link FileFilter}.
   * 
   * @param fBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFileFilter
   *        The filter to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir, @Nonnull final FileFilter aFileFilter)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir), new FileFilterToIFilterAdapter (aFileFilter));
  }

  /**
   * Create a new non-recursive file system iterator that uses multiple
   * {@link FileFilter} objects that all need to match.
   * 
   * @param sBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFileFilters
   *        The filters to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final String sBaseDir,
                                                 @Nonnull final FileFilter... aFileFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (sBaseDir),
                                      FileFilterToIFilterAdapter.getANDChained (aFileFilters));
  }

  /**
   * Create a new non-recursive file system iterator that uses multiple
   * {@link FileFilter} objects that all need to match.
   * 
   * @param fBaseDir
   *        The directory to iterate. May not be <code>null</code>.
   * @param aFileFilters
   *        The filters to use. May not be <code>null</code>.
   * @return The matching iterator.
   */
  @Nonnull
  public static IIterableIterator <File> create (@Nonnull final File fBaseDir,
                                                 @Nonnull final FileFilter... aFileFilters)
  {
    return new FilterIterator <File> (new FileSystemIterator (fBaseDir),
                                      FileFilterToIFilterAdapter.getANDChained (aFileFilters));
  }
}
