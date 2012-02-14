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
package com.phloc.commons.convert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.pair.IReadonlyPair;

/**
 * A unidirectional converter that extracts the second element from an
 * {@link IReadonlyPair}.
 * 
 * @author philip
 */
public final class UnidirectionalConverterPairSecond <DATA1TYPE, DATA2TYPE> implements
                                                                            IUnidirectionalConverter <IReadonlyPair <DATA1TYPE, DATA2TYPE>, DATA2TYPE>
{
  @Nullable
  public DATA2TYPE convert (@Nullable final IReadonlyPair <DATA1TYPE, DATA2TYPE> aPair)
  {
    return aPair == null ? null : aPair.getSecond ();
  }

  /**
   * Get a generic data converter that extracts the second element out of a
   * pair.
   * 
   * @param <FIRST>
   *        First type of the pair
   * @param <SECOND>
   *        Second type of the pair.
   * @return The data converter capable of extracting the second item out of a
   *         pair.
   */
  @Nonnull
  public static <FIRST, SECOND> UnidirectionalConverterPairSecond <FIRST, SECOND> create ()
  {
    return new UnidirectionalConverterPairSecond <FIRST, SECOND> ();
  }
}
