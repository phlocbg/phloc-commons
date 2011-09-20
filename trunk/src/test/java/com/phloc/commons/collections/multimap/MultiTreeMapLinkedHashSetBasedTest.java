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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Test class for class {@link MultiTreeMapLinkedHashSetBased}.
 * 
 * @author philip
 */
public final class MultiTreeMapLinkedHashSetBasedTest
{
  @Test
  public void testAll ()
  {
    final IMultiMapSetBased <String, String> aMultiMap = new MultiTreeMapLinkedHashSetBased <String, String> ();
    aMultiMap.putSingle ("Philip", "Wien");
    assertEquals (1, aMultiMap.size ());
    aMultiMap.putSingle ("Boris", "Wien");
    assertEquals (2, aMultiMap.size ());
    aMultiMap.putSingle ("Philip", "Copenhagen");
    assertEquals (2, aMultiMap.size ());

    // insert duplicate
    aMultiMap.putSingle ("Philip", "Copenhagen");
    assertEquals (2, aMultiMap.size ());

    Collection <String> aSet = aMultiMap.get ("Philip");
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());

    aSet = aMultiMap.get ("Boris");
    assertNotNull (aSet);
    assertEquals (1, aSet.size ());

    aSet = aMultiMap.get ("Anyone");
    assertNull (aSet);

    final Map <String, String> aMap = new TreeMap <String, String> ();
    aMap.put ("Philip", "Vienna");
    aMap.put ("Rene", "Essling");
    assertEquals (2, aMap.size ());

    aMultiMap.putAllIn (aMap);
    assertEquals (3, aMultiMap.size ());

    aSet = aMultiMap.get ("Philip");
    assertNotNull (aSet);
    assertEquals (3, aSet.size ());

    assertTrue (aMultiMap.containsSingle ("Philip", "Wien"));
    assertTrue (aMultiMap.removeSingle ("Philip", "Wien").isChanged ());
    assertFalse (aMultiMap.containsSingle ("Philip", "Wien"));
    assertFalse (aMultiMap.removeSingle ("Philip", "Wien").isChanged ());
    assertFalse (aMultiMap.containsSingle ("Alice", "Wien"));
    assertFalse (aMultiMap.removeSingle ("Alice", "Wien").isChanged ());
  }
}
