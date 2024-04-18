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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.state.EChange;

/**
 * Abstract multi map based on {@link java.util.LinkedHashMap}.
 * 
 * @author Philip Helger
 * @param <KEYTYPE>
 *        key type
 * @param <VALUETYPE>
 *        value type
 * @param <COLLTYPE>
 *        contained collection type
 */
@NotThreadSafe
public abstract class AbstractMultiLinkedHashMap <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends LinkedHashMap <KEYTYPE, COLLTYPE> implements IMultiMap <KEYTYPE, VALUETYPE, COLLTYPE>
{
  /**
   * Ctor
   */
  public AbstractMultiLinkedHashMap ()
  {}

  /**
   * Ctor
   * 
   * @param aKey
   *        Key
   * @param aValue
   *        Value
   */
  public AbstractMultiLinkedHashMap (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
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
  public AbstractMultiLinkedHashMap (@Nullable final KEYTYPE aKey, @Nullable final COLLTYPE aCollection)
  {
    put (aKey, aCollection);
  }

  /**
   * Ctor
   * 
   * @param aCont
   *        Map
   */
  public AbstractMultiLinkedHashMap (@Nullable final Map <? extends KEYTYPE, ? extends COLLTYPE> aCont)
  {
    if (aCont != null)
      putAll (aCont);
  }

  /**
   * Creates a new collection
   * 
   * @return The created collection
   */
  @Nonnull
  protected abstract COLLTYPE createNewCollection ();

  @Override
  @Nonnull
  public COLLTYPE getOrCreate (@Nullable final KEYTYPE aKey)
  {
    COLLTYPE aCont = get (aKey);
    if (aCont == null)
    {
      aCont = createNewCollection ();
      super.put (aKey, aCont);
    }
    return aCont;
  }

  @Override
  @Nonnull
  public final EChange putSingle (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    return EChange.valueOf (getOrCreate (aKey).add (aValue));
  }

  @Override
  @Nonnull
  public final EChange putAllIn (@Nonnull final Map <? extends KEYTYPE, ? extends VALUETYPE> aMap)
  {
    EChange eChange = EChange.UNCHANGED;
    for (final Map.Entry <? extends KEYTYPE, ? extends VALUETYPE> aEntry : aMap.entrySet ())
      eChange = eChange.or (putSingle (aEntry.getKey (), aEntry.getValue ()));
    return eChange;
  }

  @Override
  @Nonnull
  public final EChange removeSingle (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    final Collection <VALUETYPE> aCont = get (aKey);
    return aCont == null ? EChange.UNCHANGED : EChange.valueOf (aCont.remove (aValue));
  }

  @Override
  public final boolean containsSingle (@Nullable final KEYTYPE aKey, @Nullable final VALUETYPE aValue)
  {
    final Collection <VALUETYPE> aCont = get (aKey);
    return aCont != null && aCont.contains (aValue);
  }

  @Override
  @Nonnegative
  public final long getTotalValueCount ()
  {
    return MultiMapHelper.getTotalValueCount (this);
  }
}
