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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A file filter that matches if the direct parent directory is public, meaning
 * it does not start with "." (hidden directory on Unix systems)
 *
 * @author Philip Helger
 */
@Deprecated
public final class FileFilterParentDirectoryPublic extends AbstractFileFilter
{
  private static final FileFilterParentDirectoryPublic s_aInstance = new FileFilterParentDirectoryPublic ();

  private FileFilterParentDirectoryPublic ()
  {}

  @Nonnull
  public static FileFilterParentDirectoryPublic getInstance ()
  {
    return s_aInstance;
  }

  public boolean matchesFilter (@Nullable final File aFile)
  {
    final File aParentFile = aFile != null ? aFile.getAbsoluteFile ().getParentFile () : null;
    return aParentFile != null && !FilenameHelper.isHiddenFilename (aParentFile);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FileFilterParentDirectoryPublic))
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
