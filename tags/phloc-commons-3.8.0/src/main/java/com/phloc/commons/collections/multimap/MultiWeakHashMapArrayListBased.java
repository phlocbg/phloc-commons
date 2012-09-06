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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Multi map based on {@link WeakHashMap} and {@link ArrayList} values.<br>
 * 
 * @author philip
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 */
@NotThreadSafe
public class MultiWeakHashMapArrayListBased <KEYTYPE, VALUETYPE> extends
                                                                 AbstractMultiWeakHashMapListBased <KEYTYPE, VALUETYPE>
{
  public MultiWeakHashMapArrayListBased ()
  {}

  public MultiWeakHashMapArrayListBased (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    super (aKey, aValue);
  }

  public MultiWeakHashMapArrayListBased (@Nullable final KEYTYPE aKey, @Nullable final List <VALUETYPE> aCollection)
  {
    super (aKey, aCollection);
  }

  public MultiWeakHashMapArrayListBased (@Nullable final Map <? extends KEYTYPE, ? extends List <VALUETYPE>> aCont)
  {
    super (aCont);
  }

  @Override
  @Nonnull
  protected final List <VALUETYPE> createNewCollection ()
  {
    return new ArrayList <VALUETYPE> ();
  }
}
