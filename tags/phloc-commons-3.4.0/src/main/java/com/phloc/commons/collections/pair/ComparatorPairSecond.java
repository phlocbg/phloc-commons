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
package com.phloc.commons.collections.pair;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.compare.ESortOrder;

/**
 * Comparator comparing {@link IReadonlyPair} objects by the second element
 * 
 * @author philip
 * @param <DATA1TYPE>
 *        pair first type
 * @param <DATA2TYPE>
 *        pair second type
 */
public class ComparatorPairSecond <DATA1TYPE, DATA2TYPE extends Comparable <? super DATA2TYPE>> extends
                                                                                                AbstractComparator <IReadonlyPair <DATA1TYPE, DATA2TYPE>>
{
  public ComparatorPairSecond ()
  {
    this (ESortOrder.DEFAULT);
  }

  public ComparatorPairSecond (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  @Override
  protected final int mainCompare (@Nonnull final IReadonlyPair <DATA1TYPE, DATA2TYPE> aPair1,
                                   @Nonnull final IReadonlyPair <DATA1TYPE, DATA2TYPE> aPair2)
  {
    return CompareUtils.nullSafeCompare (aPair1.getSecond (), aPair2.getSecond ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <V1, V2 extends Comparable <? super V2>> List <IReadonlyPair <V1, V2>> getSortedByPairSecond (@Nonnull final Collection <? extends IReadonlyPair <V1, V2>> aList)
  {
    // get sorted entry list
    return ContainerHelper.getSorted (aList, new ComparatorPairSecond <V1, V2> ());
  }
}
