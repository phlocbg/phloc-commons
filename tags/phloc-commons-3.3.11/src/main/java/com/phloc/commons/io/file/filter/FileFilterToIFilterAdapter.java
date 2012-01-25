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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.filter.FilterChainAND;
import com.phloc.commons.filter.FilterChainOR;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Converts an {@link FileFilter} to an {@link IFilter} object.
 * 
 * @author philip
 */
@Immutable
public final class FileFilterToIFilterAdapter implements IFilter <File>
{
  private final FileFilter m_aFF;

  public FileFilterToIFilterAdapter (@Nonnull final FileFilter aFF)
  {
    if (aFF == null)
      throw new NullPointerException ("fileFilter");
    m_aFF = aFF;
  }

  public boolean matchesFilter (@Nullable final File aFile)
  {
    return m_aFF.accept (aFile);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("fileFilter", m_aFF).toString ();
  }

  @Nonnull
  public static IFilter <File> getANDChained (@Nonnull final FileFilter... aFileFilters)
  {
    if (ArrayHelper.isEmpty (aFileFilters))
      throw new IllegalArgumentException ("fileFilters");

    final List <IFilter <File>> aFilters = new ArrayList <IFilter <File>> ();
    for (final FileFilter aFF : aFileFilters)
      aFilters.add (new FileFilterToIFilterAdapter (aFF));
    return new FilterChainAND <File> (aFilters);
  }

  @Nonnull
  public static IFilter <File> getORChained (@Nonnull final FileFilter... aFileFilters)
  {
    if (ArrayHelper.isEmpty (aFileFilters))
      throw new IllegalArgumentException ("fileFilters");

    final List <IFilter <File>> aFilters = new ArrayList <IFilter <File>> ();
    for (final FileFilter aFF : aFileFilters)
      aFilters.add (new FileFilterToIFilterAdapter (aFF));
    return new FilterChainOR <File> (aFilters);
  }
}
