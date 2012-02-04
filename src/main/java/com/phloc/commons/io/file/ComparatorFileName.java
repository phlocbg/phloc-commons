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
package com.phloc.commons.io.file;

import java.io.File;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.compare.AbstractCollationComparator;

/**
 * Sort files by their base name.
 * 
 * @author philip
 */
@NotThreadSafe
public final class ComparatorFileName extends AbstractCollationComparator <File>
{
  public ComparatorFileName (@Nullable final Locale aSortLocale)
  {
    super (aSortLocale);
  }

  @Override
  protected String internalGetAsString (@Nonnull final File aObject)
  {
    return aObject.getName ();
  }
}
