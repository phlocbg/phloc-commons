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

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;

/**
 * This class is an {@link AbstractComparator} that extracts a certain data
 * element from the main object to compare.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of elements to be compared.
 * @param <PARTTYPE>
 *        The part type that is extracted from the data element and compared
 */
public abstract class AbstractPartComparator <DATATYPE, PARTTYPE> extends AbstractComparator <DATATYPE>
{
  private final Comparator <? super PARTTYPE> m_aPartComparator;

  /**
   * Comparator with default sort order and no nested comparator.
   *
   * @param aPartComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public AbstractPartComparator (@Nonnull final Comparator <? super PARTTYPE> aPartComparator)
  {
    super ();
    m_aPartComparator = ValueEnforcer.notNull (aPartComparator, "PartComparator");
  }

  /**
   * Constructor with sort order and no nested comparator.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aPartComparator
   *        The comparator for comparing the IDs. May not be <code>null</code>.
   */
  public AbstractPartComparator (@Nonnull final ESortOrder eSortOrder,
                                 @Nonnull final Comparator <? super PARTTYPE> aPartComparator)
  {
    super (eSortOrder);
    m_aPartComparator = ValueEnforcer.notNull (aPartComparator, "PartComparator");
  }

  @Nullable
  protected abstract PARTTYPE getPart (DATATYPE aObject);

  @Override
  protected final int mainCompare (final DATATYPE aElement1, final DATATYPE aElement2)
  {
    final PARTTYPE aPart1 = getPart (aElement1);
    final PARTTYPE aPart2 = getPart (aElement2);
    return CompareUtils.nullSafeCompare (aPart1, aPart2, m_aPartComparator);
  }
}
