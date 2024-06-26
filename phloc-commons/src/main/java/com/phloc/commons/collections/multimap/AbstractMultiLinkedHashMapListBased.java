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
package com.phloc.commons.collections.multimap;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.state.EChange;

/**
 * Abstract multi map based on {@link java.util.LinkedHashMap} and
 * {@link java.util.List} values.<br>
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public abstract class AbstractMultiLinkedHashMapListBased <KEYTYPE, VALUETYPE> extends AbstractMultiLinkedHashMap <KEYTYPE, VALUETYPE, List <VALUETYPE>> implements IMultiMapListBased <KEYTYPE, VALUETYPE>
{
  /**
   * Ctor
   */
  public AbstractMultiLinkedHashMapListBased ()
  {}

  /**
   * Ctor
   * 
   * @param aKey
   *        Key
   * @param aValue
   *        Value
   */
  public AbstractMultiLinkedHashMapListBased (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    putSingle (aKey, aValue);
  }

  /**
   * Ctor
   * 
   * @param aKey
   *        Key
   * @param aCollection
   *        Value
   */
  public AbstractMultiLinkedHashMapListBased (@Nullable final KEYTYPE aKey,
                                              @Nullable final List <VALUETYPE> aCollection)
  {
    put (aKey, aCollection);
  }

  /**
   * Ctor
   * 
   * @param aCont
   *        Map
   */
  public AbstractMultiLinkedHashMapListBased (@Nullable final Map <? extends KEYTYPE, ? extends List <VALUETYPE>> aCont)
  {
    if (aCont != null)
      putAll (aCont);
  }

  @Override
  @Nonnull
  public final EChange putSingle (@Nullable final KEYTYPE aKey,
                                  @Nullable final VALUETYPE aValue,
                                  @Nonnegative final int nIndex)
  {
    getOrCreate (aKey).add (nIndex, aValue);
    return EChange.UNCHANGED;
  }
}
