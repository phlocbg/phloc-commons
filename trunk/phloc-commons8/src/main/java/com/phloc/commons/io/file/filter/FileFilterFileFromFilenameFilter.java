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
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A special file filter that uses and external filename filter to determine the
 * validity. This filter only works for files.
 *
 * @author Philip Helger
 */
@Deprecated
public final class FileFilterFileFromFilenameFilter extends AbstractFileFilter
{
  private final FilenameFilter m_aFilenameFilter;

  public FileFilterFileFromFilenameFilter (@Nonnull final FilenameFilter aFilenameFilter)
  {
    m_aFilenameFilter = ValueEnforcer.notNull (aFilenameFilter, "FilenameFilter");
  }

  @Nonnull
  public final FilenameFilter getFilenameFilter ()
  {
    return m_aFilenameFilter;
  }

  public boolean matchesFilter (@Nullable final File aFile)
  {
    return aFile != null &&
           FileUtils.existsFile (aFile) &&
           m_aFilenameFilter.accept (aFile.getParentFile (), aFile.getName ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileFilterFileFromFilenameFilter))
      return false;
    // FilenameFilter does not necessarily implement equals/hashCode :(
    return true;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filenameFilter", m_aFilenameFilter).toString ();
  }
}
