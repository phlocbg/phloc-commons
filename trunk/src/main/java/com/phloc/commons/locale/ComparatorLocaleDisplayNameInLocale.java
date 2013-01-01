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
package com.phloc.commons.locale;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.AbstractCollationComparator;

/**
 * {@link java.util.Comparator} that sorts {@link Locale} objects by their
 * display name in a given locale.
 * 
 * @author philip
 */
public final class ComparatorLocaleDisplayNameInLocale extends AbstractCollationComparator <Locale>
{
  private final Locale m_aContentLocale;

  public ComparatorLocaleDisplayNameInLocale (@Nullable final Locale aSortLocale, @Nonnull final Locale aContentLocale)
  {
    super (aSortLocale);
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    m_aContentLocale = aContentLocale;
  }

  @Override
  protected String asString (@Nullable final Locale aLocale)
  {
    return LocaleUtils.getLocaleDisplayName (aLocale, m_aContentLocale);
  }
}
