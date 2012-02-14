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

import java.io.Serializable;
import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract comparator class that supports a sort order. This comparator may be
 * used for <code>null</code> and non-<code>null</code> values.
 * 
 * @author philip
 * @param <DATATYPE>
 */
public abstract class AbstractComparatorNullAware <DATATYPE> implements Comparator <DATATYPE>, Serializable
{
  private ESortOrder m_eSortOrder;

  /**
   * Comparator with default sort order.
   */
  public AbstractComparatorNullAware ()
  {
    this (ESortOrder.DEFAULT);
  }

  /**
   * Constructor with sort order.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public AbstractComparatorNullAware (@Nonnull final ESortOrder eSortOrder)
  {
    if (eSortOrder == null)
      throw new NullPointerException ("sortOrder");
    m_eSortOrder = eSortOrder;
  }

  /**
   * Call this to enable sorting after the constructor was invoked.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public final AbstractComparatorNullAware <DATATYPE> setSortOrder (@Nonnull final ESortOrder eSortOrder)
  {
    if (eSortOrder == null)
      throw new NullPointerException ("sortOrder");
    m_eSortOrder = eSortOrder;
    return this;
  }

  /**
   * @return The currently assigned sort order. Never <code>null</code>.
   */
  @Nonnull
  public final ESortOrder getSortOrder ()
  {
    return m_eSortOrder;
  }

  /**
   * @param aElement1
   *        First element to compare. May be <code>null</code>.
   * @param aElement2
   *        Second element to compare. May be <code>null</code>.
   * @return a negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  protected abstract int mainCompare (@Nullable final DATATYPE aElement1, @Nullable final DATATYPE aElement2);

  public final int compare (@Nullable final DATATYPE aElement1, @Nullable final DATATYPE aElement2)
  {
    return (m_eSortOrder.isAscending () ? 1 : -1) * mainCompare (aElement1, aElement2);
  }
}
