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
package com.phloc.commons.name;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.compare.AbstractCollationComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * This is a collation {@link java.util.Comparator} for objects that implement
 * the {@link IHasDisplayText} interface.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of elements to be compared.
 */
public final class ComparatorHasDisplayText <DATATYPE extends IHasDisplayText> extends AbstractCollationComparator <DATATYPE>
{
  private final Locale m_aContentLocale;

  /**
   * Constructor.
   * 
   * @param aSortLocale
   *        The locale to be used for sorting. May be <code>null</code>.
   * @param aContentLocale
   *        The locale to be used to retrieve the display text of an object. May
   *        not be <code>null</code>.
   */
  public ComparatorHasDisplayText (@Nullable final Locale aSortLocale, @Nonnull final Locale aContentLocale)
  {
    this (aSortLocale, aContentLocale, ESortOrder.DEFAULT);
  }

  /**
   * Constructor.
   * 
   * @param aSortLocale
   *        The locale to be used for sorting. May be <code>null</code>.
   * @param aContentLocale
   *        The locale to be used to retrieve the display text of an object. May
   *        not be <code>null</code>.
   * @param eSortOrder
   *        The sort order to be used. May not be <code>null</code>.
   */
  public ComparatorHasDisplayText (@Nullable final Locale aSortLocale,
                                   @Nonnull final Locale aContentLocale,
                                   @Nonnull final ESortOrder eSortOrder)
  {
    super (aSortLocale, eSortOrder);
    m_aContentLocale = ValueEnforcer.notNull (aContentLocale, "ContentLocale");
  }

  @Nonnull
  public Locale getContentLocale ()
  {
    return m_aContentLocale;
  }

  @Override
  protected String asString (final DATATYPE aObject)
  {
    return aObject.getDisplayText (m_aContentLocale);
  }
}
