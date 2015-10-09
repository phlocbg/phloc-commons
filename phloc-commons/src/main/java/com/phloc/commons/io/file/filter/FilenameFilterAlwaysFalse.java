/**
 * Copyright (C) 2006-2015 phloc systems
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

import com.phloc.commons.string.ToStringGenerator;

/**
 * A filename filter that declines all files.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class FilenameFilterAlwaysFalse implements FilenameFilter
{
  private static final FilenameFilterAlwaysFalse s_aInstance = new FilenameFilterAlwaysFalse ();

  private FilenameFilterAlwaysFalse ()
  {}

  @Nonnull
  public static FilenameFilterAlwaysFalse getInstance ()
  {
    return s_aInstance;
  }

  public boolean accept (@Nullable final File aDir, @Nullable final String sName)
  {
    return false;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
