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

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Abstract multi map based on {@link java.util.concurrent.ConcurrentHashMap}
 * and {@link java.util.Set} values.<br>
 * Important note: <code>null</code> keys are not allowed here!
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public abstract class AbstractMultiConcurrentHashMapSetBased <KEYTYPE, VALUETYPE> extends AbstractMultiConcurrentHashMap <KEYTYPE, VALUETYPE, Set <VALUETYPE>> implements IMultiMapSetBased <KEYTYPE, VALUETYPE>
{
  /**
   * Ctor
   */
  public AbstractMultiConcurrentHashMapSetBased ()
  {}

  /**
   * Ctor
   * 
   * @param aKey
   *        Key
   * @param aValue
   *        Value
   */
  public AbstractMultiConcurrentHashMapSetBased (@Nonnull final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    super (aKey, aValue);
  }

  /**
   * Ctor
   * 
   * @param aKey
   *        Key
   * @param aCollection
   *        Collection
   */
  public AbstractMultiConcurrentHashMapSetBased (@Nonnull final KEYTYPE aKey,
                                                 @Nonnull final Set <VALUETYPE> aCollection)
  {
    super (aKey, aCollection);
  }

  /**
   * Ctor
   * 
   * @param aCont
   *        Container
   */
  public AbstractMultiConcurrentHashMapSetBased (@Nullable final Map <? extends KEYTYPE, ? extends Set <VALUETYPE>> aCont)
  {
    super (aCont);
  }
}
