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
package com.phloc.commons.compare;

import java.text.Collator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;

/**
 * A simple collation aware comparator that compares objects by their "toString"
 * representation.
 * 
 * @author philip
 */
public class ComparatorAsString extends AbstractCollationComparator <Object>
{
  public ComparatorAsString ()
  {
    super ((Locale) null);
  }

  public ComparatorAsString (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  public ComparatorAsString (@Nullable final Locale aSortLocale)
  {
    super (aSortLocale);
  }

  public ComparatorAsString (@Nonnull final Collator aCollator)
  {
    super (aCollator);
  }

  @Override
  protected String asString (@Nullable final Object aValue)
  {
    return StringHelper.toString (aValue, "");
  }
}
