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
package com.phloc.commons.id;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.AbstractPartComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * This is a collation {@link java.util.Comparator} for objects that implement
 * the {@link IHasID} interface with a class that does NOT implements
 * {@link Comparable}.
 *
 * @author Philip Helger
 * @param <IDTYPE>
 *        The type of the ID
 * @param <DATATYPE>
 *        The type of elements to be compared.
 */
@Deprecated
public class ComparatorHasID <IDTYPE, DATATYPE extends IHasID <IDTYPE>> extends AbstractPartComparator <DATATYPE, IDTYPE>
{
  /**
   * Comparator with default sort order and no nested comparator.
   *
   * @param aIDComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public ComparatorHasID (@Nonnull final Comparator <? super IDTYPE> aIDComparator)
  {
    super (aIDComparator);
  }

  /**
   * Constructor with sort order and no nested comparator.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aIDComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public ComparatorHasID (@Nonnull final ESortOrder eSortOrder, @Nonnull final Comparator <? super IDTYPE> aIDComparator)
  {
    super (eSortOrder, aIDComparator);
  }

  @Override
  @Nullable
  protected IDTYPE getPart (final DATATYPE aObject)
  {
    return aObject.getID ();
  }
}
