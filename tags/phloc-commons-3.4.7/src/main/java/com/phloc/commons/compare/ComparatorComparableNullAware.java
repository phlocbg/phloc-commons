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
package com.phloc.commons.compare;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is another *lol* class: a {@link Comparator} for {@link Comparable}
 * objects. In comparison to {@link ComparatorComparable} this class can handle
 * <code>null</code> values.
 * 
 * @author philip
 */
public class ComparatorComparableNullAware <DATATYPE extends Comparable <? super DATATYPE>> extends
                                                                                            AbstractComparatorNullAware <DATATYPE>
{
  public static final boolean DEFAULT_NULL_VALUES_COME_FIRST = true;
  private boolean m_bNullValuesComeFirst;

  /**
   * Default constructor. Default values are sorted to the beginning of the
   * list.
   */
  public ComparatorComparableNullAware ()
  {
    this (DEFAULT_NULL_VALUES_COME_FIRST);
  }

  /**
   * Constructor with a sort order. If the sort order is ascending,
   * <code>null</code> values are at the beginning of the list. If sort order is
   * descending, <code>null</code> values are put to the end of the list.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorComparableNullAware (@Nonnull final ESortOrder eSortOrder)
  {
    this (eSortOrder, DEFAULT_NULL_VALUES_COME_FIRST);
  }

  public ComparatorComparableNullAware (final boolean bNullValuesComeFirst)
  {
    super ();
    m_bNullValuesComeFirst = bNullValuesComeFirst;
  }

  public ComparatorComparableNullAware (@Nonnull final ESortOrder eSortOrder, final boolean bNullValuesComeFirst)
  {
    super (eSortOrder);
    m_bNullValuesComeFirst = bNullValuesComeFirst;
  }

  @Override
  protected final int mainCompare (@Nullable final DATATYPE aElement1, @Nullable final DATATYPE aElement2)
  {
    if (aElement1 == aElement2)
      return 0;
    if (aElement1 == null)
      return m_bNullValuesComeFirst ? -1 : +1;
    if (aElement2 == null)
      return m_bNullValuesComeFirst ? +1 : -1;
    return aElement1.compareTo (aElement2);
  }
}
