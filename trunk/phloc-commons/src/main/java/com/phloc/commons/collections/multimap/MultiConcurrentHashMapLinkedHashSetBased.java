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

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Multi map based on {@link java.util.concurrent.ConcurrentHashMap} and
 * {@link LinkedHashSet} values.<br>
 * Important note: <code>null</code> keys are not allowed here!
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public class MultiConcurrentHashMapLinkedHashSetBased <KEYTYPE, VALUETYPE> extends AbstractMultiConcurrentHashMapSetBased <KEYTYPE, VALUETYPE>
{
  public MultiConcurrentHashMapLinkedHashSetBased ()
  {}

  public MultiConcurrentHashMapLinkedHashSetBased (@Nonnull final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    super (aKey, aValue);
  }

  public MultiConcurrentHashMapLinkedHashSetBased (@Nonnull final KEYTYPE aKey,
                                                   @Nonnull final Set <VALUETYPE> aCollection)
  {
    super (aKey, aCollection);
  }

  public MultiConcurrentHashMapLinkedHashSetBased (@Nullable final Map <? extends KEYTYPE, ? extends Set <VALUETYPE>> aCont)
  {
    super (aCont);
  }

  @Override
  @Nonnull
  protected final Set <VALUETYPE> createNewCollection ()
  {
    return new LinkedHashSet <VALUETYPE> ();
  }
}
