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
package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.filter.FilterChainAND;
import com.phloc.commons.filter.FilterChainOR;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Converts {@link FileFilter} and {@link FilenameFilter} objects to an
 * {@link IFilter} object.
 *
 * @author Philip Helger
 */
@Immutable
@Deprecated
public final class FileFilterToIFilterAdapter implements IFilter <File>
{
  private final FileFilter m_aFileFilter;

  public FileFilterToIFilterAdapter (@Nonnull final FilenameFilter aFilenameFilter)
  {
    this ((FileFilter) new FileFilterFromFilenameFilter (aFilenameFilter));
  }

  public FileFilterToIFilterAdapter (@Nonnull final FileFilter aFileFilter)
  {
    m_aFileFilter = ValueEnforcer.notNull (aFileFilter, "FileFilter");
  }

  @Nonnull
  public FileFilter getFileFilter ()
  {
    return m_aFileFilter;
  }

  public boolean matchesFilter (@Nullable final File aFile)
  {
    return aFile != null && m_aFileFilter.accept (aFile);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("fileFilter", m_aFileFilter).toString ();
  }

  @Nullable
  public static IFilter <File> create (@Nullable final FileFilter aFileFilter)
  {
    return aFileFilter == null ? null : p -> p != null && aFileFilter.accept (p);
  }

  @Nullable
  public static IFilter <File> create (@Nullable final FilenameFilter aFileFilter)
  {
    return aFileFilter == null ? null : new FileFilterToIFilterAdapter (aFileFilter);
  }

  @Nonnull
  @Deprecated
  public static IFilter <File> getANDChained (@Nonnull final FileFilter... aFileFilters)
  {
    ValueEnforcer.notEmpty (aFileFilters, "FileFilters");

    final List <IFilter <File>> aFilters = new ArrayList <IFilter <File>> ();
    for (final FileFilter aFileFilter : aFileFilters)
      aFilters.add (new FileFilterToIFilterAdapter (aFileFilter));
    return new FilterChainAND <File> (aFilters);
  }

  @Nonnull
  @Deprecated
  public static IFilter <File> getANDChained (@Nonnull final FilenameFilter... aFilenameFilters)
  {
    ValueEnforcer.notEmpty (aFilenameFilters, "FilenameFilters");

    final List <IFilter <File>> aFilters = new ArrayList <IFilter <File>> ();
    for (final FilenameFilter aFilenameFilter : aFilenameFilters)
      aFilters.add (new FileFilterToIFilterAdapter (aFilenameFilter));
    return new FilterChainAND <File> (aFilters);
  }

  @Nonnull
  public static IFilter <File> getANDChained (@Nonnull final IFileFilter... aFilenameFilters)
  {
    ValueEnforcer.notEmpty (aFilenameFilters, "FilenameFilters");
    return new FilterChainAND <File> (aFilenameFilters);
  }

  @Nonnull
  @Deprecated
  public static IFilter <File> getORChained (@Nonnull final FileFilter... aFileFilters)
  {
    ValueEnforcer.notEmpty (aFileFilters, "FileFilters");

    final List <IFilter <File>> aFilters = new ArrayList <IFilter <File>> ();
    for (final FileFilter aFileFilter : aFileFilters)
      aFilters.add (new FileFilterToIFilterAdapter (aFileFilter));
    return new FilterChainOR <File> (aFilters);
  }

  @Nonnull
  @Deprecated
  public static IFilter <File> getORChained (@Nonnull final FilenameFilter... aFilenameFilters)
  {
    ValueEnforcer.notEmpty (aFilenameFilters, "FilenameFilters");

    final List <IFilter <File>> aFilters = new ArrayList <IFilter <File>> ();
    for (final FilenameFilter aFilenameFilter : aFilenameFilters)
      aFilters.add (new FileFilterToIFilterAdapter (aFilenameFilter));
    return new FilterChainOR <File> (aFilters);
  }

  @Nonnull
  public static IFilter <File> getORChained (@Nonnull final IFileFilter... aFilenameFilters)
  {
    ValueEnforcer.notEmpty (aFilenameFilters, "FilenameFilters");
    return new FilterChainOR <File> (aFilenameFilters);
  }
}
