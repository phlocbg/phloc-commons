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
package com.phloc.commons.collections.multimap;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Multi map based on {@link ConcurrentHashMap} and {@link TreeSet} values.<br>
 * Important note: <code>null</code> keys are not allowed here!
 * 
 * @author philip
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public class MultiConcurrentHashMapTreeSetBased <KEYTYPE, VALUETYPE> extends
                                                                     AbstractMultiConcurrentHashMapSetBased <KEYTYPE, VALUETYPE>
{
  public MultiConcurrentHashMapTreeSetBased ()
  {}

  public MultiConcurrentHashMapTreeSetBased (@Nonnull final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    super (aKey, aValue);
  }

  public MultiConcurrentHashMapTreeSetBased (@Nonnull final KEYTYPE aKey, @Nonnull final Set <VALUETYPE> aCollection)
  {
    super (aKey, aCollection);
  }

  public MultiConcurrentHashMapTreeSetBased (@Nullable final Map <? extends KEYTYPE, ? extends Set <VALUETYPE>> aCont)
  {
    super (aCont);
  }

  @Override
  @Nonnull
  protected final Set <VALUETYPE> createNewCollection ()
  {
    return new TreeSet <VALUETYPE> ();
  }
}
