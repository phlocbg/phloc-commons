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
package com.phloc.commons.compare;

import java.io.Serializable;
import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;

/**
 * Abstract comparator class that supports a sort order. This comparator may
 * only be applied on non-<code>null</code> values.
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type to be compared
 */
public abstract class AbstractComparator <DATATYPE> implements Comparator <DATATYPE>, Serializable
{
  private static final long serialVersionUID = 4275761747174521536L;
  private ESortOrder m_eSortOrder;
  private Comparator <? super DATATYPE> m_aNestedComparator;
  private boolean m_bNullValuesComeFirst = GlobalCompareSettings.getInstance ().isSortNullValuesFirst ();

  /**
   * Comparator with default sort order and no nested comparator.
   */
  public AbstractComparator ()
  {
    this (ESortOrder.DEFAULT, null);
  }

  /**
   * Constructor with sort order.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public AbstractComparator (@Nonnull final ESortOrder eSortOrder)
  {
    this (eSortOrder, null);
  }

  /**
   * Comparator with default sort order and a nested comparator.
   * 
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public AbstractComparator (@Nullable final Comparator <? super DATATYPE> aNestedComparator)
  {
    this (ESortOrder.DEFAULT, aNestedComparator);
  }

  /**
   * Comparator with sort order and a nested comparator.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public AbstractComparator (@Nonnull final ESortOrder eSortOrder,
                             @Nullable final Comparator <? super DATATYPE> aNestedComparator)
  {
    this.m_eSortOrder = ValueEnforcer.notNull (eSortOrder, "SortOrder");
    this.m_aNestedComparator = aNestedComparator;
  }

  /**
   * Call this to enable sorting after the constructor was invoked.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final AbstractComparator <DATATYPE> setSortOrder (@Nonnull final ESortOrder eSortOrder)
  {
    this.m_eSortOrder = ValueEnforcer.notNull (eSortOrder, "SortOrder");
    return this;
  }

  /**
   * @return The currently assigned sort order. Never <code>null</code>.
   */
  @Nonnull
  public final ESortOrder getSortOrder ()
  {
    return this.m_eSortOrder;
  }

  /**
   * @param aElement1
   *        First element to compare. No information on the <code>null</code>
   *        status.
   * @param aElement2
   *        Second element to compare. No information on the <code>null</code>
   *        status.
   * @return a negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  protected abstract int mainCompare (final DATATYPE aElement1, final DATATYPE aElement2);

  /**
   * @return Whether or not null is considered smaller compared to other values
   */
  public final boolean isNullValuesComeFirst ()
  {
    return this.m_bNullValuesComeFirst;
  }

  /**
   * @param bNullValuesComeFirst
   *        Whether or not null is considered smaller compared to other values
   * @return This comparator for chaining
   */
  @Nonnull
  public final AbstractComparator <DATATYPE> setNullValuesComeFirst (final boolean bNullValuesComeFirst)
  {
    this.m_bNullValuesComeFirst = bNullValuesComeFirst;
    return this;
  }

  @Override
  public final int compare (final DATATYPE aElement1, final DATATYPE aElement2)
  {
    int nCompare = mainCompare (aElement1, aElement2);
    if (nCompare == 0 && this.m_aNestedComparator != null)
    {
      // Invoke the nested comparator for 2nd level comparison
      nCompare = this.m_aNestedComparator.compare (aElement1, aElement2);
    }

    // Apply sort order
    return (this.m_eSortOrder.isAscending () ? 1 : -1) * nCompare;
  }
}
