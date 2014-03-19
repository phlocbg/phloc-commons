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
package com.phloc.commons.compare;

import java.text.Collator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A simple collation aware comparator that compares String objects.
 *
 * @author Philip Helger
 */
public class ComparatorString extends AbstractCollationComparator <String>
{
  /**
   * Comparator with default locale {@link Collator} and default sort order.
   */
  public ComparatorString ()
  {
    super ();
  }

  /**
   * Comparator with default locale {@link Collator}.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorString (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  /**
   * Comparator with default sort order and specified sort locale.
   *
   * @param aSortLocale
   *        The locale to use. May be <code>null</code>.
   */
  public ComparatorString (@Nullable final Locale aSortLocale)
  {
    super (aSortLocale);
  }

  /**
   * Constructor with locale and sort order.
   *
   * @param aSortLocale
   *        The locale to use. May be <code>null</code>.
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorString (@Nullable final Locale aSortLocale, @Nonnull final ESortOrder eSortOrder)
  {
    super (aSortLocale, eSortOrder);
  }

  /**
   * Constructor with {@link Collator} using the default sort order
   *
   * @param aCollator
   *        The {@link Collator} to use. May not be <code>null</code>.
   */
  public ComparatorString (@Nonnull final Collator aCollator)
  {
    super (aCollator);
  }

  /**
   * Constructor with {@link Collator} and sort order.
   *
   * @param aCollator
   *        The {@link Collator} to use. May not be <code>null</code>.
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorString (@Nonnull final Collator aCollator, @Nonnull final ESortOrder eSortOrder)
  {
    super (aCollator, eSortOrder);
  }

  @Override
  protected String asString (@Nullable final String sValue)
  {
    return sValue;
  }
}
