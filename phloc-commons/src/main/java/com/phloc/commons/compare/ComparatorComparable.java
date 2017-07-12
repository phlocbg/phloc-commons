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

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link Comparator} for {@link Comparable}
 * objects.
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 */
public class ComparatorComparable <DATATYPE extends Comparable <? super DATATYPE>> extends
                                                                                   AbstractComparator <DATATYPE>
{
  private static final long serialVersionUID = -4871334266238185319L;

  /**
   * Comparator with default sort order and no nested comparator.
   */
  public ComparatorComparable ()
  {
    super ();
  }

  /**
   * Constructor with sort order.
   * 
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorComparable (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  /**
   * Comparator with default sort order and a nested comparator.
   * 
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public ComparatorComparable (@Nullable final Comparator <? super DATATYPE> aNestedComparator)
  {
    super (aNestedComparator);
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
  public ComparatorComparable (@Nonnull final ESortOrder eSortOrder,
                               @Nullable final Comparator <? super DATATYPE> aNestedComparator)
  {
    super (eSortOrder, aNestedComparator);
  }

  @Override
  protected final int mainCompare (@Nullable final DATATYPE aElement1, @Nullable final DATATYPE aElement2)
  {
    return CompareUtils.nullSafeCompare (aElement1, aElement2, isNullValuesComeFirst ());
  }
}
