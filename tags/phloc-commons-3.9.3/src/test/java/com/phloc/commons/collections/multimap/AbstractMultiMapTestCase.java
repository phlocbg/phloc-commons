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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.phloc.commons.collections.ContainerHelper;

/**
 * Abstract class for class {@link IMultiMap} implementations.
 * 
 * @author philip
 */
public abstract class AbstractMultiMapTestCase
{
  protected final String getKey1 ()
  {
    return "Philip";
  }

  protected final String getValue1 ()
  {
    return "Wien";
  }

  protected final List <String> getValueList1 ()
  {
    return ContainerHelper.newList (getValue1 ());
  }

  protected final Set <String> getValueSet1 ()
  {
    return ContainerHelper.newSet (getValue1 ());
  }

  protected final Map <String, List <String>> getMapList1 ()
  {
    return ContainerHelper.newMap (getKey1 (), getValueList1 ());
  }

  protected final Map <String, Set <String>> getMapSet1 ()
  {
    return ContainerHelper.newMap (getKey1 (), getValueSet1 ());
  }

  protected final <COLLTYPE extends Collection <String>> void testEmpty (final IMultiMap <String, String, COLLTYPE> aMultiMap)
  {
    assertTrue (aMultiMap.isEmpty ());
    aMultiMap.putSingle (getKey1 (), getValue1 ());
    testOne (aMultiMap);
  }

  protected final <COLLTYPE extends Collection <String>> void testOne (final IMultiMap <String, String, COLLTYPE> aMultiMap)
  {
    assertEquals (1, aMultiMap.size ());
    aMultiMap.putSingle ("Boris", "Wien");
    assertEquals (2, aMultiMap.size ());
    aMultiMap.putSingle ("Philip", "Copenhagen");
    assertEquals (2, aMultiMap.size ());
    aMultiMap.putSingle ("Philip", "Copenhagen");
    assertEquals (2, aMultiMap.size ());

    Collection <String> aCont = aMultiMap.get ("Philip");
    assertNotNull (aCont);
    assertEquals (aCont instanceof List ? 3 : 2, aCont.size ());

    aCont = aMultiMap.get ("Boris");
    assertNotNull (aCont);
    assertEquals (1, aCont.size ());

    aCont = aMultiMap.get ("Anyone");
    assertNull (aCont);

    final Map <String, String> aMap = new ConcurrentHashMap <String, String> ();
    aMap.put ("Philip", "Vienna");
    aMap.put ("Rene", "Essling");
    assertEquals (2, aMap.size ());

    aMultiMap.putAllIn (aMap);
    assertEquals (3, aMultiMap.size ());

    aCont = aMultiMap.get ("Philip");
    assertNotNull (aCont);
    assertEquals (aCont instanceof List ? 4 : 3, aCont.size ());

    assertTrue (aMultiMap.containsSingle ("Philip", "Wien"));
    assertTrue (aMultiMap.removeSingle ("Philip", "Wien").isChanged ());
    assertFalse (aMultiMap.containsSingle ("Philip", "Wien"));
    assertFalse (aMultiMap.removeSingle ("Philip", "Wien").isChanged ());
    assertFalse (aMultiMap.containsSingle ("Alice", "Wien"));
    assertFalse (aMultiMap.removeSingle ("Alice", "Wien").isChanged ());
    assertEquals (3, aMultiMap.size ());
  }
}
