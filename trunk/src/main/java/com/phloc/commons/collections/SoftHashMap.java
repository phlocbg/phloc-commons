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
package com.phloc.commons.collections;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasSize;
import com.phloc.commons.equals.EqualsUtils;

/**
 * A hash map that uses SoftReferences to store the elements
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Key type
 * @param <VALUETYPE>
 *        Element type
 */
public final class SoftHashMap <KEYTYPE, VALUETYPE> extends AbstractMap <KEYTYPE, VALUETYPE> implements IHasSize
{
  private static final class SoftEntry <KEYTYPE, VALUETYPE> extends SoftReference <VALUETYPE>
  {
    // necessary so that freed objects can be removed
    private final KEYTYPE m_aKey;

    private SoftEntry (@Nullable final KEYTYPE aKey, final VALUETYPE aValue, final ReferenceQueue <VALUETYPE> aRefQueue)
    {
      super (aValue, aRefQueue);
      m_aKey = aKey;
    }

    @Nullable
    public KEYTYPE getKey ()
    {
      return m_aKey;
    }
  }

  private final class EntrySet extends AbstractSet <Map.Entry <KEYTYPE, VALUETYPE>> implements IHasSize
  {
    private final Set <Map.Entry <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>>> m_aSet = m_aMap.entrySet ();

    @Override
    public int size ()
    {
      int ret = 0;
      for (final Iterator <Map.Entry <KEYTYPE, VALUETYPE>> i = iterator (); i.hasNext (); i.next ())
        ret++;
      return ret;
    }

    @Override
    public boolean isEmpty ()
    {
      // default implementation computes size which is inefficient
      return !iterator ().hasNext ();
    }

    @Override
    public boolean remove (final Object aObject)
    {
      _expungeStaleEntries ();
      return super.remove (aObject);
    }

    @Override
    public Iterator <Map.Entry <KEYTYPE, VALUETYPE>> iterator ()
    {
      return new Iterator <Map.Entry <KEYTYPE, VALUETYPE>> ()
      {
        @Nonnull
        private final Iterator <Map.Entry <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>>> m_aIT = m_aSet.iterator ();
        @Nullable
        private Map.Entry <KEYTYPE, VALUETYPE> m_aNext;
        @Nullable
        private VALUETYPE m_aValue;

        /*
         * Strong reference to key, so that the GC will leave it alone as long
         * as this Entry exists
         */
        public boolean hasNext ()
        {
          while (m_aIT.hasNext ())
          {
            final Map.Entry <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>> aOriginalEntry = m_aIT.next ();
            final SoftEntry <KEYTYPE, VALUETYPE> aSoftEntry = aOriginalEntry.getValue ();
            m_aValue = null;
            if (aSoftEntry != null)
            {
              m_aValue = aSoftEntry.get ();
              if (m_aValue == null)
              {
                /* Weak key has been cleared by GC */
                continue;
              }
            }
            m_aNext = new Map.Entry <KEYTYPE, VALUETYPE> ()
            {
              public KEYTYPE getKey ()
              {
                return aOriginalEntry.getKey ();
              }

              @Nullable
              public VALUETYPE getValue ()
              {
                return m_aValue;
              }

              @Nullable
              public VALUETYPE setValue (@Nullable final VALUETYPE aNewValue)
              {
                final VALUETYPE aOldValue = m_aValue;
                m_aValue = aNewValue;
                aOriginalEntry.setValue (new SoftEntry <KEYTYPE, VALUETYPE> (aOriginalEntry.getKey (),
                                                                             aNewValue,
                                                                             m_aRefQueue));
                return aOldValue;
              }

              @Override
              public boolean equals (final Object o)
              {
                if (o == this)
                  return true;
                if (!(o instanceof Map.Entry <?, ?>))
                  return false;
                final Map.Entry <?, ?> rhs = (Map.Entry <?, ?>) o;
                return EqualsUtils.equals (getKey (), rhs.getKey ()) &&
                       EqualsUtils.equals (getValue (), rhs.getValue ());
              }

              @Override
              public int hashCode ()
              {
                // Same implementation as AbstractMap.SimpleEntry!
                final KEYTYPE aKey = getKey ();
                final VALUETYPE aValue = getValue ();
                return (aKey == null ? 0 : aKey.hashCode ()) ^ (aValue == null ? 0 : aValue.hashCode ());
              }
            };
            return true;
          }
          return false;
        }

        @Nonnull
        public Map.Entry <KEYTYPE, VALUETYPE> next ()
        {
          if (m_aNext == null && !hasNext ())
            throw new NoSuchElementException ();
          final Entry <KEYTYPE, VALUETYPE> aRet = m_aNext;
          m_aNext = null;
          return aRet;
        }

        public void remove ()
        {
          m_aIT.remove ();
        }
      };
    }
  }

  private final Map <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>> m_aMap;
  private final ReferenceQueue <VALUETYPE> m_aRefQueue = new ReferenceQueue <VALUETYPE> ();
  private Set <Map.Entry <KEYTYPE, VALUETYPE>> m_aEntrySet;

  public SoftHashMap ()
  {
    m_aMap = new HashMap <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>> ();
  }

  public SoftHashMap (final int nInitialCapacity)
  {
    m_aMap = new HashMap <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>> (nInitialCapacity);
  }

  public SoftHashMap (final int nInitialCapacity, final float fLoadFactor)
  {
    m_aMap = new HashMap <KEYTYPE, SoftEntry <KEYTYPE, VALUETYPE>> (nInitialCapacity, fLoadFactor);
  }

  @Override
  public VALUETYPE get (final Object aKey)
  {
    final Reference <VALUETYPE> res = m_aMap.get (aKey);
    return res == null ? null : res.get ();
  }

  @Override
  public VALUETYPE put (final KEYTYPE aKey, final VALUETYPE aValue)
  {
    _expungeStaleEntries ();
    final SoftEntry <KEYTYPE, VALUETYPE> aNewEntry = new SoftEntry <KEYTYPE, VALUETYPE> (aKey, aValue, m_aRefQueue);
    final SoftEntry <KEYTYPE, VALUETYPE> aOldEntry = m_aMap.put (aKey, aNewEntry);
    return aOldEntry == null ? null : aOldEntry.get ();
  }

  @Override
  public Set <Map.Entry <KEYTYPE, VALUETYPE>> entrySet ()
  {
    if (m_aEntrySet == null)
      m_aEntrySet = new EntrySet ();
    return m_aEntrySet;
  }

  private void _expungeStaleEntries ()
  {
    Reference <? extends VALUETYPE> r;
    while ((r = m_aRefQueue.poll ()) != null)
    {
      final SoftEntry <?, ?> e = (SoftEntry <?, ?>) r;
      m_aMap.remove (e.getKey ());
    }
  }

  @Override
  public int size ()
  {
    return entrySet ().size ();
  }

  @Override
  public VALUETYPE remove (final Object key)
  {
    _expungeStaleEntries ();
    final SoftEntry <KEYTYPE, VALUETYPE> res = m_aMap.remove (key);
    return res == null ? null : res.get ();
  }

  @Override
  public void clear ()
  {
    // clear out ref queue. We don't need to expunge entries
    // since table is getting cleared.
    while (m_aRefQueue.poll () != null)
    {}

    m_aMap.clear ();

    // Allocation of array may have caused GC, which may have caused
    // additional entries to go stale. Removing these entries from the
    // reference queue will make them eligible for reclamation.
    while (m_aRefQueue.poll () != null)
    {}
  }
}
