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
package com.phloc.commons.collections.pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.AbstractPartComparatorComparable;
import com.phloc.commons.compare.ESortOrder;

/**
 * Comparator comparing {@link IReadonlyPair} objects by the first element
 * 
 * @author Philip Helger
 * @param <DATA1TYPE>
 *        pair first type
 * @param <DATA2TYPE>
 *        pair second type
 */
public class ComparatorPairFirst <DATA1TYPE extends Comparable <? super DATA1TYPE>, DATA2TYPE> extends
                                                                                               AbstractPartComparatorComparable <IReadonlyPair <DATA1TYPE, DATA2TYPE>, DATA1TYPE>
{
  public ComparatorPairFirst ()
  {
    super ();
  }

  public ComparatorPairFirst (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  @Override
  @Nullable
  protected DATA1TYPE getPart (@Nonnull final IReadonlyPair <DATA1TYPE, DATA2TYPE> aObject)
  {
    return aObject.getFirst ();
  }
}
