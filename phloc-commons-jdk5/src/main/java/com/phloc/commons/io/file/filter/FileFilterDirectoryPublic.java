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

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A file filter that matches if the passed file is a directory and is public,
 * meaning it does not start with "." (hidden directory on Unix systems)
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class FileFilterDirectoryPublic extends AbstractFileFilter
{
  private static final FileFilterDirectoryPublic s_aInstance = new FileFilterDirectoryPublic ();

  private FileFilterDirectoryPublic ()
  {}

  public static FileFilterDirectoryPublic getInstance ()
  {
    return s_aInstance;
  }

  public boolean accept (@Nullable final File aFile)
  {
    return aFile != null && aFile.isDirectory () && !FilenameHelper.isHiddenFilename (aFile);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileFilterDirectoryPublic))
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
    return new ToStringGenerator (this).toString ();
  }
}
