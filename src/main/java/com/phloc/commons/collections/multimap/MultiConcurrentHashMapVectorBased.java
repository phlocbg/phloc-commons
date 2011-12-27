/**
 * Copyright (C) 2006-2011 phloc systems
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
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Multi map based on {@link ConcurrentHashMap} and {@link Vector} values.<br>
 * Important note: <code>null</code> keys are not allowed here!
 * 
 * @author philip
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@ThreadSafe
public class MultiConcurrentHashMapVectorBased <KEYTYPE, VALUETYPE> extends
                                                                    AbstractMultiConcurrentHashMapListBased <KEYTYPE, VALUETYPE>
{
  public MultiConcurrentHashMapVectorBased ()
  {}

  public MultiConcurrentHashMapVectorBased (@Nonnull final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    super (aKey, aValue);
  }

  public MultiConcurrentHashMapVectorBased (@Nonnull final KEYTYPE aKey, @Nonnull final List <VALUETYPE> aCollection)
  {
    super (aKey, aCollection);
  }

  public MultiConcurrentHashMapVectorBased (@Nullable final Map <? extends KEYTYPE, ? extends List <VALUETYPE>> aCont)
  {
    super (aCont);
  }

  @Override
  @Nonnull
  protected final List <VALUETYPE> createNewCollection ()
  {
    return new Vector <VALUETYPE> ();
  }
}
