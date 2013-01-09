/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.collections.multimap;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;

import com.phloc.commons.state.EChange;

/**
 * Base interface for a multi map (one key with several values).
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Key type
 * @param <VALUETYPE>
 *        Element type
 * @param <COLLTYPE>
 *        Container type containing value types
 */
public interface IMultiMap <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends
                                                                                         Map <KEYTYPE, COLLTYPE>
{
  /**
   * Add a single value into the container identified by the passed key.
   * 
   * @param aKey
   *        The key to use
   * @param aValue
   *        The value to be added
   * @return {@link EChange}
   */
  @Nonnull
  EChange putSingle (@Nonnull (when = When.MAYBE) KEYTYPE aKey, @Nullable VALUETYPE aValue);

  /**
   * Add all values into the container identified by the passed key-value-map.
   * 
   * @param aMap
   *        The key-value-map to use
   * @return {@link EChange}
   */
  @Nonnull
  EChange putAllIn (@Nonnull Map <? extends KEYTYPE, ? extends VALUETYPE> aMap);

  @Nonnull
  EChange removeSingle (@Nonnull (when = When.MAYBE) KEYTYPE aKey, @Nullable VALUETYPE aValue);

  boolean containsSingle (@Nonnull (when = When.MAYBE) KEYTYPE aKey, @Nullable VALUETYPE aValue);

  /**
   * @return The total number of contained values. Always &ge; 0.
   */
  @Nonnegative
  long getTotalValueCount ();
}
