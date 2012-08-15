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
import java.io.FilenameFilter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A filter that accepts all public directories (directories who's name does not
 * start with a dot!)
 * 
 * @author philip
 */
@ThreadSafe
public final class FilenameFilterPublicDirectory implements FilenameFilter
{
  private static final FilenameFilterPublicDirectory s_aInstance = new FilenameFilterPublicDirectory ();

  private FilenameFilterPublicDirectory ()
  {}

  public boolean accept (@Nonnull final File aDir, @Nonnull final String sName)
  {
    // Ignore hidden directories
    final String sRealName = FilenameHelper.getSecureFilename (sName);
    return new File (aDir, sRealName).isDirectory () && !StringHelper.startsWith (sRealName, '.');
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }

  @Nonnull
  public static FilenameFilterPublicDirectory getInstance ()
  {
    return s_aInstance;
  }
}
