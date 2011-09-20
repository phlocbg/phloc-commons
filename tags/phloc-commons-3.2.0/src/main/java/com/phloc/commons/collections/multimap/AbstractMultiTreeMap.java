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

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.state.EChange;

@NotThreadSafe
public abstract class AbstractMultiTreeMap <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends
                                                                                                         TreeMap <KEYTYPE, COLLTYPE> implements
                                                                                                                                    IMultiMap <KEYTYPE, VALUETYPE, COLLTYPE>
{
  @Nonnull
  protected abstract COLLTYPE createNewCollection ();

  @Nonnull
  public final EChange putSingle (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    COLLTYPE aCont = get (aKey);
    if (aCont == null)
    {
      aCont = createNewCollection ();
      super.put (aKey, aCont);
    }
    return EChange.valueOf (aCont.add (aValue));
  }

  @Nonnull
  public final EChange putAllIn (@Nonnull final Map <? extends KEYTYPE, ? extends VALUETYPE> aMap)
  {
    EChange eChange = EChange.UNCHANGED;
    for (final Map.Entry <? extends KEYTYPE, ? extends VALUETYPE> aEntry : aMap.entrySet ())
      eChange = eChange.or (putSingle (aEntry.getKey (), aEntry.getValue ()));
    return eChange;
  }

  @Nonnull
  public final EChange removeSingle (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    final Collection <VALUETYPE> aCont = get (aKey);
    return aCont == null ? EChange.UNCHANGED : EChange.valueOf (aCont.remove (aValue));
  }

  public final boolean containsSingle (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    final Collection <VALUETYPE> aCont = get (aKey);
    return aCont == null ? false : aCont.contains (aValue);
  }
}
