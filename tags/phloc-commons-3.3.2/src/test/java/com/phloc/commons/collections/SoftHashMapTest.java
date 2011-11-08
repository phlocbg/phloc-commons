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
package com.phloc.commons.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test for class {@link SoftHashMap}
 * 
 * @author philip
 */
public final class SoftHashMapTest
{
  static class SimpleEntry <K, V> implements Entry <K, V>
  {
    K m_aKey;
    V m_aValue;

    public SimpleEntry (final K key, final V value)
    {
      this.m_aKey = key;
      this.m_aValue = value;
    }

    public SimpleEntry (final Entry <K, V> e)
    {
      this.m_aKey = e.getKey ();
      this.m_aValue = e.getValue ();
    }

    public K getKey ()
    {
      return m_aKey;
    }

    public V getValue ()
    {
      return m_aValue;
    }

    public V setValue (final V value)
    {
      final V oldValue = this.m_aValue;
      this.m_aValue = value;
      return oldValue;
    }

    @Override
    public boolean equals (final Object o)
    {
      if (!(o instanceof Map.Entry))
        return false;
      final Map.Entry <?, ?> e = (Map.Entry <?, ?>) o;
      return CompareUtils.nullSafeEquals (m_aKey, e.getKey ()) && CompareUtils.nullSafeEquals (m_aValue, e.getValue ());
    }

    @Override
    public int hashCode ()
    {
      return ((m_aKey == null) ? 0 : m_aKey.hashCode ()) ^ ((m_aValue == null) ? 0 : m_aValue.hashCode ());
    }

    @Override
    public String toString ()
    {
      return m_aKey + "=" + m_aValue;
    }
  }

  @Test
  public void testAll ()
  {
    new SoftHashMap <String, String> (100);
    new SoftHashMap <String, String> (100, .75f);
    final Map <String, String> aCache = new SoftHashMap <String, String> ();
    for (int i = 0; i < 10; ++i)
    {
      aCache.put ("key" + i, "value" + i);
      aCache.put ("key" + i, "value" + i);
    }
    assertEquals (10, aCache.size ());
    assertFalse (aCache.entrySet ().isEmpty ());
    for (final Map.Entry <String, String> aEntry : aCache.entrySet ())
    {
      assertNotNull (aEntry.getKey ());
      assertNotNull (aEntry.getValue ());
      PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aEntry,
                                                                      new SimpleEntry <String, String> (aEntry.getKey (),
                                                                                                        aEntry.getValue ()));
      aEntry.setValue (aEntry.getValue () + "-2");
    }
    assertNotNull (aCache.remove ("key0"));
    assertEquals (9, aCache.size ());
    assertNull (aCache.get ("key0"));
    assertEquals ("value1-2", aCache.get ("key1"));
    assertNull (aCache.remove ("key0"));
    assertEquals (9, aCache.size ());
    aCache.clear ();
    assertEquals (0, aCache.size ());
    assertTrue (aCache.isEmpty ());

    Iterator <Map.Entry <String, String>> it = aCache.entrySet ().iterator ();
    assertFalse (it.hasNext ());
    aCache.put ("K", "V");
    it = aCache.entrySet ().iterator ();
    assertTrue (it.hasNext ());
    it.remove ();
    assertTrue (aCache.isEmpty ());

    aCache.put ("K", "V");
    final Map.Entry <String, String> aEntry = ContainerHelper.getFirstElement (aCache.entrySet ());
    assertTrue (aCache.entrySet ().remove (aEntry));
    assertFalse (aCache.entrySet ().remove (aEntry));
    assertTrue (aCache.isEmpty ());
  }
}
