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
package com.phloc.commons.io.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A filename filter that checks whether a file has the specified name. The
 * implementation is done via {@link String#equalsIgnoreCase(String)} so it is
 * case insensitive.
 * 
 * @author philip
 */
@ThreadSafe
public final class FilenameFilterEqualsIgnoreCase implements FilenameFilter
{
  private final String m_sFilename;

  /**
   * @param sFilename
   *        The extension to use. May neither be <code>null</code> nor empty.
   */
  public FilenameFilterEqualsIgnoreCase (@Nonnull @Nonempty final String sFilename)
  {
    if (StringHelper.hasNoText (sFilename))
      throw new IllegalArgumentException ("filename");
    m_sFilename = sFilename;
  }

  public boolean accept (@Nullable final File aDir, @Nullable final String sName)
  {
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    return sRealName != null && sRealName.equalsIgnoreCase (m_sFilename);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filename", m_sFilename).toString ();
  }
}
