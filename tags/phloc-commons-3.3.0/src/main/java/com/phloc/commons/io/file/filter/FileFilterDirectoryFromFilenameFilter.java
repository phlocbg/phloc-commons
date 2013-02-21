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
import java.io.FileFilter;
import java.io.FilenameFilter;

import javax.annotation.Nullable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

public final class FileFilterDirectoryFromFilenameFilter implements FileFilter
{
  private final FilenameFilter m_aFnFilter;

  public FileFilterDirectoryFromFilenameFilter (final FilenameFilter aFnFilter)
  {
    m_aFnFilter = aFnFilter;
  }

  public boolean accept (@Nullable final File aFile)
  {
    return aFile != null && aFile.isDirectory () && m_aFnFilter.accept (aFile.getParentFile (), aFile.getName ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileFilterDirectoryFromFilenameFilter))
      return false;
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
    return new ToStringGenerator (this).append ("filter", m_aFnFilter).toString ();
  }
}