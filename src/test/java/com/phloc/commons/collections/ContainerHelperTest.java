/**
 * Copyright (C) 2006-2014 phloc systems
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

import static com.phloc.commons.collections.ContainerHelper.containsAnyNullElement;
import static com.phloc.commons.collections.ContainerHelper.getCombinedEnumeration;
import static com.phloc.commons.collections.ContainerHelper.getCombinedMap;
import static com.phloc.commons.collections.ContainerHelper.getConcatenatedInline;
import static com.phloc.commons.collections.ContainerHelper.getConcatenatedList;
import static com.phloc.commons.collections.ContainerHelper.getConcatenatedSet;
import static com.phloc.commons.collections.ContainerHelper.getDifference;
import static com.phloc.commons.collections.ContainerHelper.getEmptyEnumeration;
import static com.phloc.commons.collections.ContainerHelper.getEmptyIterator;
import static com.phloc.commons.collections.ContainerHelper.getEnumeration;
import static com.phloc.commons.collections.ContainerHelper.getFilteredMap;
import static com.phloc.commons.collections.ContainerHelper.getFirstElement;
import static com.phloc.commons.collections.ContainerHelper.getFirstKey;
import static com.phloc.commons.collections.ContainerHelper.getFirstValue;
import static com.phloc.commons.collections.ContainerHelper.getIntersected;
import static com.phloc.commons.collections.ContainerHelper.getIterator;
import static com.phloc.commons.collections.ContainerHelper.getLastElement;
import static com.phloc.commons.collections.ContainerHelper.getNotNull;
import static com.phloc.commons.collections.ContainerHelper.getReverseInlineList;
import static com.phloc.commons.collections.ContainerHelper.getReverseIterator;
import static com.phloc.commons.collections.ContainerHelper.getReverseList;
import static com.phloc.commons.collections.ContainerHelper.getReverseLookup;
import static com.phloc.commons.collections.ContainerHelper.getReverseLookupSet;
import static com.phloc.commons.collections.ContainerHelper.getSafe;
import static com.phloc.commons.collections.ContainerHelper.getSize;
import static com.phloc.commons.collections.ContainerHelper.getSorted;
import static com.phloc.commons.collections.ContainerHelper.getSortedByKey;
import static com.phloc.commons.collections.ContainerHelper.getSortedByValue;
import static com.phloc.commons.collections.ContainerHelper.getStackCopyWithoutTop;
import static com.phloc.commons.collections.ContainerHelper.getSubList;
import static com.phloc.commons.collections.ContainerHelper.getSwappedKeyValues;
import static com.phloc.commons.collections.ContainerHelper.isEmpty;
import static com.phloc.commons.collections.ContainerHelper.makeUnmodifiable;
import static com.phloc.commons.collections.ContainerHelper.makeUnmodifiableNotNull;
import static com.phloc.commons.collections.ContainerHelper.newList;
import static com.phloc.commons.collections.ContainerHelper.newListPrefilled;
import static com.phloc.commons.collections.ContainerHelper.newMap;
import static com.phloc.commons.collections.ContainerHelper.newObjectListFromArray;
import static com.phloc.commons.collections.ContainerHelper.newOrderedMap;
import static com.phloc.commons.collections.ContainerHelper.newOrderedSet;
import static com.phloc.commons.collections.ContainerHelper.newSet;
import static com.phloc.commons.collections.ContainerHelper.newSortedMap;
import static com.phloc.commons.collections.ContainerHelper.newSortedSet;
import static com.phloc.commons.collections.ContainerHelper.newStack;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableList;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableMap;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableOrderedMap;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableOrderedSet;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableSet;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableSortedMap;
import static com.phloc.commons.collections.ContainerHelper.newUnmodifiableSortedSet;
import static com.phloc.commons.collections.ContainerHelper.removeFirstElement;
import static com.phloc.commons.collections.ContainerHelper.removeLastElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

import com.phloc.commons.collections.iterate.ArrayEnumeration;
import com.phloc.commons.collections.iterate.EmptyEnumeration;
import com.phloc.commons.collections.iterate.EmptyIterator;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.IMultiMapSetBased;
import com.phloc.commons.collections.multimap.MultiHashMapArrayListBased;
import com.phloc.commons.collections.multimap.MultiHashMapHashSetBased;
import com.phloc.commons.compare.ComparatorString;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.mock.AbstractPhlocTestCase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link ContainerHelper}
 * 
 * @author Philip Helger
 */
public final class ContainerHelperTest extends AbstractPhlocTestCase
{
  @Test
  public void testGetNotNull ()
  {
    assertNotNull (getNotNull ((List <?>) null));
    assertEquals (2, getNotNull (newList ("1", "2")).size ());
    assertNotNull (getNotNull ((Set <?>) null));
    assertEquals (2, getNotNull (newSet ("1", "2")).size ());
    assertNotNull (getNotNull ((Map <?, ?>) null));
    assertEquals (1, getNotNull (newMap ("1", "2")).size ());
  }

  @Test
  public void testGetDifference ()
  {
    final List <String> l1 = newList ("Hello", "Welt", "from", "Vienna");
    final List <String> l2 = newList ("Welt", "from");

    // Result should be "Hello" and "Vienna"
    final Set <String> ret = getDifference (l1, l2);
    assertNotNull (ret);
    assertEquals (ret.size (), 2);
    assertTrue (ret.contains ("Hello"));
    assertFalse (ret.contains ("Welt"));
    assertFalse (ret.contains ("from"));
    assertTrue (ret.contains ("Vienna"));

    assertEquals (4, getDifference (l1, new Vector <String> ()).size ());
    assertEquals (4, getDifference (l1, null).size ());
    assertEquals (0, getDifference (new HashSet <String> (), l2).size ());
    assertEquals (0, getDifference (null, l2).size ());
  }

  @Test
  public void testGetIntersected ()
  {
    final List <String> l1 = newList ("Hallo", "Welt", "from", "Vienna");
    final List <String> l2 = newList ("Welt", "from");

    // Result should be "Hello" and "Vienna"
    final Set <String> ret = getIntersected (l1, l2);
    assertNotNull (ret);
    assertEquals (ret.size (), 2);
    assertFalse (ret.contains ("Hello"));
    assertTrue (ret.contains ("Welt"));
    assertTrue (ret.contains ("from"));
    assertFalse (ret.contains ("Vienna"));

    assertEquals (0, getIntersected (l1, null).size ());
    assertEquals (0, getIntersected (null, l2).size ());
  }

  @Test
  public void testMakeUnmodifiable ()
  {
    assertNull (makeUnmodifiable ((Collection <?>) null));
    assertNull (makeUnmodifiable ((List <?>) null));
    assertNull (makeUnmodifiable ((Set <?>) null));
    assertNull (makeUnmodifiable ((SortedSet <?>) null));
    assertNull (makeUnmodifiable ((Map <?, ?>) null));
    assertNull (makeUnmodifiable ((SortedMap <?, ?>) null));

    final Collection <String> c = newList ("s1", "s2");
    assertNotNull (makeUnmodifiable (c));
    assertTrue (c != makeUnmodifiable (c));
    final List <String> l = newList ("s1", "s2");
    assertNotNull (makeUnmodifiable (l));
    assertTrue (l != makeUnmodifiable (l));
    final Set <String> s = newSet ("s1", "s2");
    assertNotNull (makeUnmodifiable (s));
    assertTrue (s != makeUnmodifiable (s));
    final SortedSet <String> ss = new TreeSet <String> (s);
    assertNotNull (makeUnmodifiable (ss));
    assertTrue (ss != makeUnmodifiable (ss));
    final Map <String, String> m = newMap ("s1", "s2");
    assertNotNull (makeUnmodifiable (m));
    assertTrue (m != makeUnmodifiable (m));
    final SortedMap <String, String> sm = new TreeMap <String, String> (m);
    assertNotNull (makeUnmodifiable (sm));
    assertTrue (sm != makeUnmodifiable (sm));
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testMakeUnmodifiableNotNull ()
  {
    assertNotNull (makeUnmodifiableNotNull ((Collection <?>) null));
    assertNotNull (makeUnmodifiableNotNull ((List <?>) null));
    assertNotNull (makeUnmodifiableNotNull ((Set <?>) null));
    assertNotNull (makeUnmodifiableNotNull ((SortedSet <?>) null));
    assertNotNull (makeUnmodifiableNotNull ((Map <?, ?>) null));
    assertNotNull (makeUnmodifiableNotNull ((SortedMap <?, ?>) null));

    final Collection <String> c = newList ("s1", "s2");
    assertNotNull (makeUnmodifiableNotNull (c));
    assertTrue (c != makeUnmodifiableNotNull (c));
    final List <String> l = newList ("s1", "s2");
    assertNotNull (makeUnmodifiableNotNull (l));
    assertTrue (l != makeUnmodifiableNotNull (l));
    final Set <String> s = newSet ("s1", "s2");
    assertNotNull (makeUnmodifiableNotNull (s));
    assertTrue (s != makeUnmodifiableNotNull (s));
    final SortedSet <String> ss = new TreeSet <String> (s);
    assertNotNull (makeUnmodifiableNotNull (ss));
    assertTrue (ss != makeUnmodifiableNotNull (ss));
    final Map <String, String> m = newMap ("s1", "s2");
    assertNotNull (makeUnmodifiableNotNull (m));
    assertTrue (m != makeUnmodifiableNotNull (m));
    final SortedMap <String, String> sm = new TreeMap <String, String> (m);
    assertNotNull (makeUnmodifiableNotNull (sm));
    assertTrue (sm != makeUnmodifiableNotNull (sm));
  }

  @Test
  public void testNewMap_Empty ()
  {
    assertNotNull (newMap ());
    assertTrue (newMap ().isEmpty ());
  }

  @Test
  public void testNewMap_KeyValue ()
  {
    final Map <String, Integer> aMap = newMap ("Hallo", I5);
    assertNotNull (aMap);
    assertEquals (aMap.size (), 1);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (I5, aMap.get ("Hallo"));
  }

  @Test
  public void testNewMap_Array ()
  {
    assertNotNull (newMap ((Object []) null));
    assertTrue (newMap ((Object []) null).isEmpty ());

    try
    {
      // odd number of parameters not allowed
      newMap ("Hallo", "Welt", "from");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final Map <String, String> aMap = newMap ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aMap);
    assertEquals (aMap.size (), 2);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (aMap.get ("Hallo"), "Welt");
    assertNotNull (aMap.get ("from"));
    assertEquals (aMap.get ("from"), "Vienna");
  }

  @Test
  public void testNewMap_ArrayArray ()
  {
    try
    {
      // null keys not allowed
      newMap ((Object []) null, new String [] { "a" });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newMap (new String [] { "a" }, (Object []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // different length not allowed
      newMap (new String [0], new String [1]);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    Map <Integer, String> aMap = newMap (new Integer [] { I2, I4 }, new String [] { "Hallo", "Welt" });
    assertNotNull (aMap);
    assertEquals (2, aMap.size ());
    assertNotNull (aMap.get (I2));
    assertEquals ("Hallo", aMap.get (I2));
    assertNotNull (aMap.get (I4));
    assertEquals ("Welt", aMap.get (I4));

    aMap = newMap (new Integer [] {}, new String [] {});
    assertNotNull (aMap);
    assertEquals (0, aMap.size ());
  }

  @Test
  public void testNewMap_CollectionCollection ()
  {
    final List <String> aKeys = newList ("d", "c", "b", "a");
    final List <Integer> aValues = newList (Integer.valueOf (4),
                                            Integer.valueOf (3),
                                            Integer.valueOf (2),
                                            Integer.valueOf (1));
    try
    {
      // null keys not allowed
      newMap (null, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newMap (aKeys, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    Map <String, Integer> aMap = newMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.keySet ().containsAll (aKeys));
    assertTrue (aMap.values ().containsAll (aValues));

    try
    {
      // There are more values than keys
      aValues.add (Integer.valueOf (42));
      newMap (aKeys, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Simple test for newMap (Map)
    assertEquals (aMap, newMap (aMap));
    assertEquals (aMap, newMap (aMap.entrySet ()));

    aKeys.clear ();
    aValues.clear ();
    aMap = newMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.isEmpty ());

    // Simple test for newMap (Map)
    assertEquals (aMap, newMap (aMap));
    assertEquals (aMap, newMap (aMap.entrySet ()));
  }

  @Test
  public void testNewUnmodifiableMap_Empty ()
  {
    assertNotNull (newUnmodifiableMap ());
    assertTrue (newUnmodifiableMap ().isEmpty ());
  }

  @Test
  public void testNewUnmodifiableMap_KeyValue ()
  {
    final Map <String, Integer> aMap = newUnmodifiableMap ("Hallo", I5);
    assertNotNull (aMap);
    assertEquals (aMap.size (), 1);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (I5, aMap.get ("Hallo"));
  }

  @Test
  public void testNewUnmodifiableMap_Array ()
  {
    assertNotNull (newUnmodifiableMap ((Object []) null));
    assertTrue (newUnmodifiableMap ((Object []) null).isEmpty ());

    try
    {
      // odd number of parameters not allowed
      newUnmodifiableMap ("Hallo", "Welt", "from");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final Map <String, String> aMap = newUnmodifiableMap ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aMap);
    assertEquals (aMap.size (), 2);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (aMap.get ("Hallo"), "Welt");
    assertNotNull (aMap.get ("from"));
    assertEquals (aMap.get ("from"), "Vienna");
  }

  @Test
  public void testNewUnmodifiableMap_ArrayArray ()
  {
    try
    {
      // null keys not allowed
      newUnmodifiableMap ((Object []) null, new String [] { "a" });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newUnmodifiableMap (new String [] { "a" }, (Object []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // different length not allowed
      newUnmodifiableMap (new String [0], new String [1]);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    Map <Integer, String> aMap = newUnmodifiableMap (new Integer [] { I2, I4 }, new String [] { "Hallo", "Welt" });
    assertNotNull (aMap);
    assertEquals (2, aMap.size ());
    assertNotNull (aMap.get (I2));
    assertEquals ("Hallo", aMap.get (I2));
    assertNotNull (aMap.get (I4));
    assertEquals ("Welt", aMap.get (I4));

    aMap = newUnmodifiableMap (new Integer [] {}, new String [] {});
    assertNotNull (aMap);
    assertEquals (0, aMap.size ());
  }

  @Test
  public void testNewUnmodifiableMap_CollectionCollection ()
  {
    final List <String> aKeys = newList ("d", "c", "b", "a");
    final List <Integer> aValues = newList (Integer.valueOf (4),
                                            Integer.valueOf (3),
                                            Integer.valueOf (2),
                                            Integer.valueOf (1));
    try
    {
      // null keys not allowed
      newUnmodifiableMap (null, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newUnmodifiableMap (aKeys, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    Map <String, Integer> aMap = newUnmodifiableMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.keySet ().containsAll (aKeys));
    assertTrue (aMap.values ().containsAll (aValues));

    try
    {
      // There are more values than keys
      aValues.add (Integer.valueOf (42));
      newUnmodifiableMap (aKeys, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Simple test for newUnmodifiableMap (Map)
    assertEquals (aMap, newUnmodifiableMap (aMap));
    assertEquals (aMap, newUnmodifiableMap (aMap.entrySet ()));

    aKeys.clear ();
    aValues.clear ();
    aMap = newUnmodifiableMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.isEmpty ());

    // Simple test for newUnmodifiableMap (Map)
    assertEquals (aMap, newUnmodifiableMap (aMap));
    assertEquals (aMap, newUnmodifiableMap (aMap.entrySet ()));
  }

  @Test
  public void testNewSortedMap_Empty ()
  {
    assertNotNull (ContainerHelper.<String, String> newSortedMap ());
    assertTrue (ContainerHelper.<String, String> newSortedMap ().isEmpty ());
  }

  @Test
  public void testNewSortedMap_KeyValue ()
  {
    final SortedMap <String, Integer> aSortedMap = newSortedMap ("Hallo", I5);
    assertNotNull (aSortedMap);
    assertEquals (aSortedMap.size (), 1);
    assertNotNull (aSortedMap.get ("Hallo"));
    assertEquals (I5, aSortedMap.get ("Hallo"));
  }

  @Test
  public void testNewSortedMap_Array ()
  {
    assertNotNull (newSortedMap ((String []) null));
    assertTrue (newSortedMap ((String []) null).isEmpty ());

    try
    {
      // odd number of parameters not allowed
      newSortedMap ("Hallo", "Welt", "from");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final SortedMap <String, String> aSortedMap = newSortedMap ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aSortedMap);
    assertEquals (aSortedMap.size (), 2);
    assertNotNull (aSortedMap.get ("Hallo"));
    assertEquals (aSortedMap.get ("Hallo"), "Welt");
    assertNotNull (aSortedMap.get ("from"));
    assertEquals (aSortedMap.get ("from"), "Vienna");
  }

  @Test
  public void testNewSortedMap_ArrayArray ()
  {
    try
    {
      // null keys not allowed
      newSortedMap ((String []) null, new String [] { "a" });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newSortedMap (new String [] { "a" }, (Object []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // different length not allowed
      newSortedMap (new String [0], new String [1]);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    SortedMap <Integer, String> aSortedMap = newSortedMap (new Integer [] { I2, I4 }, new String [] { "Hallo", "Welt" });
    assertNotNull (aSortedMap);
    assertEquals (2, aSortedMap.size ());
    assertNotNull (aSortedMap.get (I2));
    assertEquals ("Hallo", aSortedMap.get (I2));
    assertNotNull (aSortedMap.get (I4));
    assertEquals ("Welt", aSortedMap.get (I4));

    aSortedMap = newSortedMap (new Integer [] {}, new String [] {});
    assertNotNull (aSortedMap);
    assertEquals (0, aSortedMap.size ());
  }

  @Test
  public void testNewSortedMap_CollectionCollection ()
  {
    final List <String> aKeys = newList ("d", "c", "b", "a");
    final List <Integer> aValues = newList (Integer.valueOf (4),
                                            Integer.valueOf (3),
                                            Integer.valueOf (2),
                                            Integer.valueOf (1));
    try
    {
      // null keys not allowed
      newSortedMap ((List <String>) null, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newSortedMap (aKeys, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    SortedMap <String, Integer> aSortedMap = newSortedMap (aKeys, aValues);
    assertNotNull (aSortedMap);
    assertTrue (aSortedMap.keySet ().containsAll (aKeys));
    assertTrue (aSortedMap.values ().containsAll (aValues));

    try
    {
      // There are more values than keys
      aValues.add (Integer.valueOf (42));
      newSortedMap (aKeys, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Simple test for newSortedMap (SortedMap)
    assertEquals (aSortedMap, newSortedMap (aSortedMap));
    assertEquals (aSortedMap, newSortedMap (aSortedMap.entrySet ()));

    aKeys.clear ();
    aValues.clear ();
    aSortedMap = newSortedMap (aKeys, aValues);
    assertNotNull (aSortedMap);
    assertTrue (aSortedMap.isEmpty ());

    // Simple test for newSortedMap (SortedMap)
    assertEquals (aSortedMap, newSortedMap (aSortedMap));
    assertEquals (aSortedMap, newSortedMap (aSortedMap.entrySet ()));
  }

  @Test
  public void testNewUnmodifiableSortedMap_Empty ()
  {
    assertNotNull (ContainerHelper.<String, String> newUnmodifiableSortedMap ());
    assertTrue (ContainerHelper.<String, String> newUnmodifiableSortedMap ().isEmpty ());
  }

  @Test
  public void testNewUnmodifiableSortedMap_KeyValue ()
  {
    final SortedMap <String, Integer> aSortedMap = newUnmodifiableSortedMap ("Hallo", I5);
    assertNotNull (aSortedMap);
    assertEquals (aSortedMap.size (), 1);
    assertNotNull (aSortedMap.get ("Hallo"));
    assertEquals (I5, aSortedMap.get ("Hallo"));
  }

  @Test
  public void testNewUnmodifiableSortedMap_Array ()
  {
    assertNotNull (newUnmodifiableSortedMap ((String []) null));
    assertTrue (newUnmodifiableSortedMap ((String []) null).isEmpty ());

    try
    {
      // odd number of parameters not allowed
      newUnmodifiableSortedMap ("Hallo", "Welt", "from");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final SortedMap <String, String> aSortedMap = newUnmodifiableSortedMap ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aSortedMap);
    assertEquals (aSortedMap.size (), 2);
    assertNotNull (aSortedMap.get ("Hallo"));
    assertEquals (aSortedMap.get ("Hallo"), "Welt");
    assertNotNull (aSortedMap.get ("from"));
    assertEquals (aSortedMap.get ("from"), "Vienna");
  }

  @Test
  public void testNewUnmodifiableSortedMap_ArrayArray ()
  {
    try
    {
      // null keys not allowed
      newUnmodifiableSortedMap ((String []) null, new String [] { "a" });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newUnmodifiableSortedMap (new String [] { "a" }, (Object []) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // different length not allowed
      newUnmodifiableSortedMap (new String [0], new String [1]);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    SortedMap <Integer, String> aSortedMap = newUnmodifiableSortedMap (new Integer [] { I2, I4 },
                                                                       new String [] { "Hallo", "Welt" });
    assertNotNull (aSortedMap);
    assertEquals (2, aSortedMap.size ());
    assertNotNull (aSortedMap.get (I2));
    assertEquals ("Hallo", aSortedMap.get (I2));
    assertNotNull (aSortedMap.get (I4));
    assertEquals ("Welt", aSortedMap.get (I4));

    aSortedMap = newUnmodifiableSortedMap (new Integer [] {}, new String [] {});
    assertNotNull (aSortedMap);
    assertEquals (0, aSortedMap.size ());
  }

  @Test
  public void testNewUnmodifiableSortedMap_CollectionCollection ()
  {
    final List <String> aKeys = newList ("d", "c", "b", "a");
    final List <Integer> aValues = newList (Integer.valueOf (4),
                                            Integer.valueOf (3),
                                            Integer.valueOf (2),
                                            Integer.valueOf (1));
    try
    {
      // null keys not allowed
      newUnmodifiableSortedMap ((List <String>) null, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newUnmodifiableSortedMap (aKeys, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    SortedMap <String, Integer> aSortedMap = newUnmodifiableSortedMap (aKeys, aValues);
    assertNotNull (aSortedMap);
    assertTrue (aSortedMap.keySet ().containsAll (aKeys));
    assertTrue (aSortedMap.values ().containsAll (aValues));

    try
    {
      // There are more values than keys
      aValues.add (Integer.valueOf (42));
      newUnmodifiableSortedMap (aKeys, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Simple test for newUnmodifiableSortedMap (SortedMap)
    assertEquals (aSortedMap, newUnmodifiableSortedMap (aSortedMap));
    assertEquals (aSortedMap, newUnmodifiableSortedMap (aSortedMap.entrySet ()));

    aKeys.clear ();
    aValues.clear ();
    aSortedMap = newUnmodifiableSortedMap (aKeys, aValues);
    assertNotNull (aSortedMap);
    assertTrue (aSortedMap.isEmpty ());

    // Simple test for newUnmodifiableSortedMap (SortedMap)
    assertEquals (aSortedMap, newUnmodifiableSortedMap (aSortedMap));
    assertEquals (aSortedMap, newUnmodifiableSortedMap (aSortedMap.entrySet ()));
  }

  @Test
  public void testNewOrderedMap_Empty ()
  {
    assertNotNull (newOrderedMap ());
    assertTrue (newOrderedMap ().isEmpty ());
  }

  @Test
  public void testNewOrderedMap_KeyValue ()
  {
    final Map <String, Integer> aMap = newOrderedMap ("Hallo", I5);
    assertNotNull (aMap);
    assertEquals (aMap.size (), 1);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (I5, aMap.get ("Hallo"));
  }

  @Test
  public void testNewOrderedMap_Array ()
  {
    assertNotNull (newOrderedMap ((Object []) null));
    assertTrue (newOrderedMap ((Object []) null).isEmpty ());

    try
    {
      // odd number of parameters not allowed
      newOrderedMap ("Hallo", "Welt", "from");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final Map <String, String> aMap = newOrderedMap ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aMap);
    assertEquals (aMap.size (), 2);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (aMap.get ("Hallo"), "Welt");
    assertNotNull (aMap.get ("from"));
    assertEquals (aMap.get ("from"), "Vienna");
  }

  @Test
  public void testNewOrderedMap_ArrayArray ()
  {
    Map <String, Integer> aMap = newOrderedMap (new String [] { "Hallo", "Alice" }, new Integer [] { I1, I2 });
    assertNotNull (aMap);
    assertEquals (2, aMap.size ());
    assertNotNull (aMap.get ("Hallo"));
    assertNotNull (aMap.get ("Alice"));
    assertEquals (I1, aMap.get ("Hallo"));
    assertEquals (I2, aMap.get ("Alice"));

    // check order
    final Iterator <String> it = aMap.keySet ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertEquals ("Hallo", it.next ());
    assertTrue (it.hasNext ());
    assertEquals ("Alice", it.next ());
    assertFalse (it.hasNext ());

    try
    {
      // key array may not be null
      newOrderedMap (null, new Integer [] { I1, I2 });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // value array may not be null
      newOrderedMap (new String [] { "Hallo", "Alice" }, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // key and value array need to have the same length
      newOrderedMap (new String [] { "Hallo", "Alice" }, new Integer [] { I1, });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    aMap = newOrderedMap (new String [] {}, new Integer [] {});
    assertNotNull (aMap);
    assertEquals (0, aMap.size ());
  }

  @Test
  public void testNewOrderedMap_CollectionCollection ()
  {
    final List <String> aKeys = newList ("d", "c", "b", "a");
    final List <Integer> aValues = newList (Integer.valueOf (4),
                                            Integer.valueOf (3),
                                            Integer.valueOf (2),
                                            Integer.valueOf (1));
    try
    {
      // null keys not allowed
      newOrderedMap (null, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newOrderedMap (aKeys, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    Map <String, Integer> aMap = newOrderedMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.keySet ().containsAll (aKeys));
    assertTrue (aMap.values ().containsAll (aValues));

    try
    {
      // There are more values than keys
      aValues.add (Integer.valueOf (42));
      newOrderedMap (aKeys, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Simple test for newMap (Map)
    assertEquals (aMap, newOrderedMap (aMap));
    assertEquals (aMap, newOrderedMap (aMap.entrySet ()));

    aKeys.clear ();
    aValues.clear ();
    aMap = newOrderedMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.isEmpty ());

    // Simple test for newMap (Map)
    assertEquals (aMap, newOrderedMap (aMap));
    assertEquals (aMap, newOrderedMap (aMap.entrySet ()));
  }

  @Test
  public void testNewUnmodifiableOrderedMap_Empty ()
  {
    assertNotNull (newUnmodifiableOrderedMap ());
    assertTrue (newUnmodifiableOrderedMap ().isEmpty ());
  }

  @Test
  public void testNewUnmodifiableOrderedMap_KeyValue ()
  {
    final Map <String, Integer> aMap = newUnmodifiableOrderedMap ("Hallo", I5);
    assertNotNull (aMap);
    assertEquals (aMap.size (), 1);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (I5, aMap.get ("Hallo"));
  }

  @Test
  public void testNewUnmodifiableOrderedMap_Array ()
  {
    assertNotNull (newUnmodifiableOrderedMap ((Object []) null));
    assertTrue (newUnmodifiableOrderedMap ((Object []) null).isEmpty ());

    try
    {
      // odd number of parameters not allowed
      newUnmodifiableOrderedMap ("Hallo", "Welt", "from");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final Map <String, String> aMap = newUnmodifiableOrderedMap ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aMap);
    assertEquals (aMap.size (), 2);
    assertNotNull (aMap.get ("Hallo"));
    assertEquals (aMap.get ("Hallo"), "Welt");
    assertNotNull (aMap.get ("from"));
    assertEquals (aMap.get ("from"), "Vienna");
  }

  @Test
  public void testNewUnmodifiableOrderedMap_ArrayArray ()
  {
    Map <String, Integer> aMap = newUnmodifiableOrderedMap (new String [] { "Hallo", "Alice" },
                                                            new Integer [] { I1, I2 });
    assertNotNull (aMap);
    assertEquals (2, aMap.size ());
    assertNotNull (aMap.get ("Hallo"));
    assertNotNull (aMap.get ("Alice"));
    assertEquals (I1, aMap.get ("Hallo"));
    assertEquals (I2, aMap.get ("Alice"));

    // check order
    final Iterator <String> it = aMap.keySet ().iterator ();
    assertNotNull (it);
    assertTrue (it.hasNext ());
    assertEquals ("Hallo", it.next ());
    assertTrue (it.hasNext ());
    assertEquals ("Alice", it.next ());
    assertFalse (it.hasNext ());

    try
    {
      // key array may not be null
      newUnmodifiableOrderedMap (null, new Integer [] { I1, I2 });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // value array may not be null
      newUnmodifiableOrderedMap (new String [] { "Hallo", "Alice" }, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // key and value array need to have the same length
      newUnmodifiableOrderedMap (new String [] { "Hallo", "Alice" }, new Integer [] { I1, });
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    aMap = newUnmodifiableOrderedMap (new String [] {}, new Integer [] {});
    assertNotNull (aMap);
    assertEquals (0, aMap.size ());
  }

  @Test
  public void testNewUnmodifiableOrderedMap_CollectionCollection ()
  {
    final List <String> aKeys = newList ("d", "c", "b", "a");
    final List <Integer> aValues = newList (Integer.valueOf (4),
                                            Integer.valueOf (3),
                                            Integer.valueOf (2),
                                            Integer.valueOf (1));
    try
    {
      // null keys not allowed
      newUnmodifiableOrderedMap (null, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // null values not allowed
      newUnmodifiableOrderedMap (aKeys, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    Map <String, Integer> aMap = newUnmodifiableOrderedMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.keySet ().containsAll (aKeys));
    assertTrue (aMap.values ().containsAll (aValues));

    try
    {
      // There are more values than keys
      aValues.add (Integer.valueOf (42));
      newUnmodifiableOrderedMap (aKeys, aValues);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    // Simple test for newMap (Map)
    assertEquals (aMap, newUnmodifiableOrderedMap (aMap));
    assertEquals (aMap, newUnmodifiableOrderedMap (aMap.entrySet ()));

    aKeys.clear ();
    aValues.clear ();
    aMap = newUnmodifiableOrderedMap (aKeys, aValues);
    assertNotNull (aMap);
    assertTrue (aMap.isEmpty ());

    // Simple test for newMap (Map)
    assertEquals (aMap, newUnmodifiableOrderedMap (aMap));
    assertEquals (aMap, newUnmodifiableOrderedMap (aMap.entrySet ()));
  }

  @Test
  public void testNewSet_Empty ()
  {
    final Set <String> aSet = newSet ();
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSet_SingleValue ()
  {
    Set <String> aSet = newSet ("Hallo");
    assertNotNull (aSet);
    assertEquals (1, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));

    aSet = newSet ((String) null);
    assertNotNull (aSet);
    assertEquals (1, aSet.size ());
    assertTrue (aSet.contains (null));
  }

  @Test
  public void testNewSet_Array ()
  {
    Set <String> aSet = newSet ("Hallo", "Welt");
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSet (new String [0]);
    assertNotNull (aSet);

    aSet = newSet ((String []) null);
    assertNotNull (aSet);
  }

  @Test
  public void testNewSetIterable ()
  {
    Set <String> aSet = newSet ((Iterable <String>) newList ("Hallo", "Welt"));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSet ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSetCollection ()
  {
    Set <String> aSet = newSet (newList ("Hallo", "Welt"));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSet (new ArrayList <String> ());
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSetIIterableIterator ()
  {
    Set <String> aSet = newSet (IterableIterator.create (newList ("Hallo", "Welt")));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSet (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSetEnumeration ()
  {
    Set <String> aSet = newSet (getEnumeration (newList ("Hallo", "Welt")));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSet (getEnumeration (new ArrayList <String> ()));
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSetIterator ()
  {
    final Iterator <String> it = newSet ("Hallo", "Welt").iterator ();
    final Set <String> aUnmodifiableSet = newSet (it);
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));
  }

  @Test
  public void testNewUnmodifiableSetEmpty ()
  {
    final Set <String> aUnmodifiableSet = newUnmodifiableSet ();
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSetSingleValue ()
  {
    Set <String> aUnmodifiableSet = newUnmodifiableSet ("Hallo");
    assertNotNull (aUnmodifiableSet);
    assertEquals (1, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));

    aUnmodifiableSet = newUnmodifiableSet ((String) null);
    assertNotNull (aUnmodifiableSet);
    assertEquals (1, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains (null));
  }

  @Test
  public void testNewUnmodifiableSetArray ()
  {
    Set <String> aUnmodifiableSet = newUnmodifiableSet ("Hallo", "Welt");
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSet (new String [0]);
    assertNotNull (aUnmodifiableSet);

    aUnmodifiableSet = newUnmodifiableSet ((String []) null);
    assertNotNull (aUnmodifiableSet);
  }

  @Test
  public void testNewUnmodifiableSetIterable ()
  {
    Set <String> aUnmodifiableSet = newUnmodifiableSet ((Iterable <String>) newList ("Hallo", "Welt"));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSet ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSetCollection ()
  {
    Set <String> aUnmodifiableSet = newUnmodifiableSet (newList ("Hallo", "Welt"));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSet (new ArrayList <String> ());
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSetIIterableIterator ()
  {
    Set <String> aUnmodifiableSet = newUnmodifiableSet (IterableIterator.create (newList ("Hallo", "Welt")));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSet (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSetEnumeration ()
  {
    Set <String> aUnmodifiableSet = newUnmodifiableSet (getEnumeration (newList ("Hallo", "Welt")));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSet (getEnumeration (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSetIterator ()
  {
    final Iterator <String> it = newUnmodifiableSet ("Hallo", "Welt").iterator ();
    final Set <String> aUnmodifiableSet = newUnmodifiableSet (it);
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));
  }

  @Test
  public void testNewSortedSet_Empty ()
  {
    final SortedSet <String> aSet = ContainerHelper.<String> newSortedSet ();
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testNewSortedSet_SingleValue ()
  {
    SortedSet <String> aSet = newSortedSet ("Hallo");
    assertNotNull (aSet);
    assertEquals (1, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));

    aSet = newSortedSet ((String) null);
    assertNotNull (aSet);
    assertEquals (1, aSet.size ());
    assertTrue (aSet.contains (null));
  }

  @Test
  public void testNewSortedSet_Array ()
  {
    SortedSet <String> aSet = newSortedSet ("Hallo", "Welt");
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSortedSet (new String [0]);
    assertNotNull (aSet);

    aSet = newSortedSet ((String []) null);
    assertNotNull (aSet);
  }

  @Test
  public void testNewSortedSetIterable ()
  {
    SortedSet <String> aSet = newSortedSet ((Iterable <String>) newList ("Hallo", "Welt"));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSortedSet ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSortedSetCollection ()
  {
    SortedSet <String> aSet = newSortedSet (newList ("Hallo", "Welt"));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSortedSet (new ArrayList <String> ());
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testNewSortedSetIIterableIterator ()
  {
    SortedSet <String> aSet = newSortedSet (IterableIterator.create (newList ("Hallo", "Welt", null)));
    assertNotNull (aSet);
    assertEquals (3, aSet.size ());
    assertNull (aSet.first ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));
    assertTrue (aSet.contains (null));

    aSet = newSortedSet (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSortedSetEnumeration ()
  {
    SortedSet <String> aSet = newSortedSet (getEnumeration (newList ("Hallo", "Welt")));
    assertNotNull (aSet);
    assertEquals (2, aSet.size ());
    assertTrue (aSet.contains ("Hallo"));
    assertTrue (aSet.contains ("Welt"));

    aSet = newSortedSet (getEnumeration (new ArrayList <String> ()));
    assertNotNull (aSet);
    assertEquals (0, aSet.size ());
  }

  @Test
  public void testNewSortedSetIterator ()
  {
    final Iterator <String> it = newSortedSet ("Hallo", "Welt").iterator ();
    final SortedSet <String> aUnmodifiableSet = newSortedSet (it);
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));
  }

  @Test
  public void testNewUnmodifiableSortedSetEmpty ()
  {
    final SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet ();
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testNewUnmodifiableSortedSetSingleValue ()
  {
    SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet ("Hallo");
    assertNotNull (aUnmodifiableSet);
    assertEquals (1, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));

    aUnmodifiableSet = newUnmodifiableSortedSet ((String) null);
    assertNotNull (aUnmodifiableSet);
    assertEquals (1, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains (null));
  }

  @Test
  public void testNewUnmodifiableSortedSetArray ()
  {
    SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet ("Hallo", "Welt");
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSortedSet (new String [0]);
    assertNotNull (aUnmodifiableSet);

    aUnmodifiableSet = newUnmodifiableSortedSet ((String []) null);
    assertNotNull (aUnmodifiableSet);
  }

  @Test
  public void testNewUnmodifiableSortedSetIterable ()
  {
    SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet ((Iterable <String>) newList ("Hallo", "Welt"));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSortedSet ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSortedSetCollection ()
  {
    SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet (newList ("Hallo", "Welt"));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSortedSet (new ArrayList <String> ());
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSortedSetIIterableIterator ()
  {
    SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet (IterableIterator.create (newList ("Hallo", "Welt")));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSortedSet (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSortedSetEnumeration ()
  {
    SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet (getEnumeration (newList ("Hallo", "Welt")));
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));

    aUnmodifiableSet = newUnmodifiableSortedSet (getEnumeration (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableSet);
    assertEquals (0, aUnmodifiableSet.size ());
  }

  @Test
  public void testNewUnmodifiableSortedSetIterator ()
  {
    final Iterator <String> it = newUnmodifiableSortedSet ("Hallo", "Welt").iterator ();
    final SortedSet <String> aUnmodifiableSet = newUnmodifiableSortedSet (it);
    assertNotNull (aUnmodifiableSet);
    assertEquals (2, aUnmodifiableSet.size ());
    assertTrue (aUnmodifiableSet.contains ("Hallo"));
    assertTrue (aUnmodifiableSet.contains ("Welt"));
  }

  @Test
  public void testNewOrderedSetEmpty ()
  {
    final Set <String> aOrderedSet = newOrderedSet ();
    assertNotNull (aOrderedSet);
    assertEquals (0, aOrderedSet.size ());
  }

  @Test
  public void testNewOrderedSetSingleValue ()
  {
    Set <String> aOrderedSet = newOrderedSet ("Hallo");
    assertNotNull (aOrderedSet);
    assertEquals (1, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));

    aOrderedSet = newOrderedSet ((String) null);
    assertNotNull (aOrderedSet);
    assertEquals (1, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains (null));
  }

  @Test
  public void testNewOrderedSetArray ()
  {
    Set <String> aOrderedSet = newOrderedSet ("Hallo", "Welt");
    assertNotNull (aOrderedSet);
    assertEquals (2, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));
    assertTrue (aOrderedSet.contains ("Welt"));

    aOrderedSet = newOrderedSet (new String [0]);
    assertNotNull (aOrderedSet);

    aOrderedSet = newOrderedSet ((String []) null);
    assertNotNull (aOrderedSet);
  }

  @Test
  public void testNewOrderedSetIterable ()
  {
    Set <String> aOrderedSet = newOrderedSet ((Iterable <String>) newList ("Hallo", "Welt"));
    assertNotNull (aOrderedSet);
    assertEquals (2, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));
    assertTrue (aOrderedSet.contains ("Welt"));

    aOrderedSet = newOrderedSet ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aOrderedSet);
    assertEquals (0, aOrderedSet.size ());
  }

  @Test
  public void testNewOrderedSetCollection ()
  {
    Set <String> aOrderedSet = newOrderedSet (newList ("Hallo", "Welt"));
    assertNotNull (aOrderedSet);
    assertEquals (2, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));
    assertTrue (aOrderedSet.contains ("Welt"));

    aOrderedSet = newOrderedSet (new ArrayList <String> ());
    assertNotNull (aOrderedSet);
    assertEquals (0, aOrderedSet.size ());
  }

  @Test
  public void testNewOrderedSetIIterableIterator ()
  {
    Set <String> aOrderedSet = newOrderedSet (IterableIterator.create (newList ("Hallo", "Welt")));
    assertNotNull (aOrderedSet);
    assertEquals (2, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));
    assertTrue (aOrderedSet.contains ("Welt"));

    aOrderedSet = newOrderedSet (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aOrderedSet);
    assertEquals (0, aOrderedSet.size ());
  }

  @Test
  public void testNewOrderedSetEnumeration ()
  {
    Set <String> aOrderedSet = newOrderedSet (getEnumeration (newList ("Hallo", "Welt")));
    assertNotNull (aOrderedSet);
    assertEquals (2, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));
    assertTrue (aOrderedSet.contains ("Welt"));

    aOrderedSet = newOrderedSet (getEnumeration (new ArrayList <String> ()));
    assertNotNull (aOrderedSet);
    assertEquals (0, aOrderedSet.size ());
  }

  @Test
  public void testNewOrderedSetIterator ()
  {
    final Iterator <String> it = newOrderedSet ("Hallo", "Welt").iterator ();
    final Set <String> aOrderedSet = newOrderedSet (it);
    assertNotNull (aOrderedSet);
    assertEquals (2, aOrderedSet.size ());
    assertTrue (aOrderedSet.contains ("Hallo"));
    assertTrue (aOrderedSet.contains ("Welt"));
  }

  @Test
  public void testNewUnmodifiableOrderedSetEmpty ()
  {
    final Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ();
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (0, aUnmodifiableOrderedSet.size ());
  }

  @Test
  public void testNewUnmodifiableOrderedSetSingleValue ()
  {
    Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ("Hallo");
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (1, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ((String) null);
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (1, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains (null));
  }

  @Test
  public void testNewUnmodifiableOrderedSetArray ()
  {
    Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ("Hallo", "Welt");
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (2, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));
    assertTrue (aUnmodifiableOrderedSet.contains ("Welt"));

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (new String [0]);
    assertNotNull (aUnmodifiableOrderedSet);

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ((String []) null);
    assertNotNull (aUnmodifiableOrderedSet);
  }

  @Test
  public void testNewUnmodifiableOrderedSetIterable ()
  {
    Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ((Iterable <String>) newList ("Hallo", "Welt"));
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (2, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));
    assertTrue (aUnmodifiableOrderedSet.contains ("Welt"));

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (0, aUnmodifiableOrderedSet.size ());
  }

  @Test
  public void testNewUnmodifiableOrderedSetCollection ()
  {
    Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (newList ("Hallo", "Welt"));
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (2, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));
    assertTrue (aUnmodifiableOrderedSet.contains ("Welt"));

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (new ArrayList <String> ());
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (0, aUnmodifiableOrderedSet.size ());
  }

  @Test
  public void testNewUnmodifiableOrderedSetIIterableIterator ()
  {
    Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (IterableIterator.create (newList ("Hallo", "Welt")));
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (2, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));
    assertTrue (aUnmodifiableOrderedSet.contains ("Welt"));

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (0, aUnmodifiableOrderedSet.size ());
  }

  @Test
  public void testNewUnmodifiableOrderedSetEnumeration ()
  {
    Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (getEnumeration (newList ("Hallo", "Welt")));
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (2, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));
    assertTrue (aUnmodifiableOrderedSet.contains ("Welt"));

    aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (getEnumeration (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (0, aUnmodifiableOrderedSet.size ());
  }

  @Test
  public void testNewUnmodifiableOrderedSetIterator ()
  {
    final Iterator <String> it = newUnmodifiableOrderedSet ("Hallo", "Welt").iterator ();
    final Set <String> aUnmodifiableOrderedSet = newUnmodifiableOrderedSet (it);
    assertNotNull (aUnmodifiableOrderedSet);
    assertEquals (2, aUnmodifiableOrderedSet.size ());
    assertTrue (aUnmodifiableOrderedSet.contains ("Hallo"));
    assertTrue (aUnmodifiableOrderedSet.contains ("Welt"));
  }

  @SuppressFBWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  @Test
  public void testNewListPrefilled ()
  {
    List <String> aList = newListPrefilled ("s", 5);
    assertNotNull (aList);
    assertEquals (5, aList.size ());
    for (int i = 0; i < 5; ++i)
      assertEquals ("s", aList.get (i));

    aList = newListPrefilled ("s", 0);
    assertNotNull (aList);
    assertEquals (0, aList.size ());

    try
    {
      newListPrefilled ("s", -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testNewListEmpty ()
  {
    final List <String> aList = newList ();
    assertNotNull (aList);
    assertEquals (0, aList.size ());
  }

  @Test
  public void testNewListSingleValue ()
  {
    List <String> aList = newList ("Hallo");
    assertNotNull (aList);
    assertEquals (1, aList.size ());
    assertTrue (aList.contains ("Hallo"));

    aList = newList ((String) null);
    assertNotNull (aList);
    assertEquals (1, aList.size ());
    assertTrue (aList.contains (null));
  }

  @Test
  public void testNewListArray ()
  {
    List <String> aList = newList ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertTrue (aList.contains ("Hallo"));
    assertTrue (aList.contains ("Welt"));
    assertTrue (aList.contains ("from"));
    assertTrue (aList.contains ("Vienna"));

    aList = newList ((String []) null);
    assertNotNull (aList);

    aList = newList (new String [0]);
    assertNotNull (aList);
  }

  @Test
  public void testNewListEnumeration ()
  {
    List <String> aList = newList (new ArrayEnumeration <String> ("Hallo", "Welt", "from", "Vienna"));
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertTrue (aList.contains ("Hallo"));
    assertTrue (aList.contains ("Welt"));
    assertTrue (aList.contains ("from"));
    assertTrue (aList.contains ("Vienna"));

    aList = newList (ContainerHelper.<String> getEmptyEnumeration ());
    assertNotNull (aList);

    aList = newList ((Enumeration <String>) null);
    assertNotNull (aList);
  }

  @Test
  public void testNewListIterator ()
  {
    final List <String> aSource = new ArrayList <String> ();
    assertTrue (aSource.add ("Hallo"));
    assertTrue (aSource.add ("Welt"));
    assertTrue (aSource.add ("from"));
    assertTrue (aSource.add ("Vienna"));

    List <String> aList = newList (aSource.iterator ());
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertTrue (aList.contains ("Hallo"));
    assertTrue (aList.contains ("Welt"));
    assertTrue (aList.contains ("from"));
    assertTrue (aList.contains ("Vienna"));

    aList = newList (ContainerHelper.<String> getEmptyIterator ());
    assertNotNull (aList);

    aList = newList ((Iterator <String>) null);
    assertNotNull (aList);
  }

  @Test
  public void testNewListCollection ()
  {
    final List <String> aSource = newList ("Hallo", "Welt", "from", "Vienna");

    List <String> aList = newList (aSource);
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertTrue (aList.contains ("Hallo"));
    assertTrue (aList.contains ("Welt"));
    assertTrue (aList.contains ("from"));
    assertTrue (aList.contains ("Vienna"));

    aList = newList (new ArrayList <String> ());
    assertNotNull (aList);

    aList = newList ((List <String>) null);
    assertNotNull (aList);
  }

  @Test
  public void testNewListIterable ()
  {
    final List <String> aSource = newList ("Hallo", "Welt", "from", "Vienna");

    List <String> aList = newList ((Iterable <String>) aSource);
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertTrue (aList.contains ("Hallo"));
    assertTrue (aList.contains ("Welt"));
    assertTrue (aList.contains ("from"));
    assertTrue (aList.contains ("Vienna"));

    aList = newList ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aList);

    aList = newList ((Iterable <String>) null);
    assertNotNull (aList);
  }

  @Test
  public void testNewListIIterableIterator ()
  {
    final List <String> aSource = newList ("Hallo", "Welt", "from", "Vienna");

    List <String> aList = newList (IterableIterator.create (aSource));
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertTrue (aList.contains ("Hallo"));
    assertTrue (aList.contains ("Welt"));
    assertTrue (aList.contains ("from"));
    assertTrue (aList.contains ("Vienna"));

    aList = newList (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aList);

    aList = newList ((IIterableIterator <String>) null);
    assertNotNull (aList);
  }

  @Test
  public void testNewUnmodifiableListEmpty ()
  {
    final List <String> aUnmodifiableList = newUnmodifiableList ();
    assertNotNull (aUnmodifiableList);
    assertEquals (0, aUnmodifiableList.size ());
  }

  @Test
  public void testNewUnmodifiableListSingleValue ()
  {
    List <String> aUnmodifiableList = newUnmodifiableList ("Hallo");
    assertNotNull (aUnmodifiableList);
    assertEquals (1, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));

    aUnmodifiableList = newUnmodifiableList ((String) null);
    assertNotNull (aUnmodifiableList);
    assertEquals (1, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains (null));
  }

  @Test
  public void testNewUnmodifiableListArray ()
  {
    List <String> aUnmodifiableList = newUnmodifiableList ("Hallo", "Welt", "from", "Vienna");
    assertNotNull (aUnmodifiableList);
    assertEquals (4, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));
    assertTrue (aUnmodifiableList.contains ("Welt"));
    assertTrue (aUnmodifiableList.contains ("from"));
    assertTrue (aUnmodifiableList.contains ("Vienna"));

    aUnmodifiableList = newUnmodifiableList ((String []) null);
    assertNotNull (aUnmodifiableList);

    aUnmodifiableList = newUnmodifiableList (new String [0]);
    assertNotNull (aUnmodifiableList);
  }

  @Test
  public void testNewUnmodifiableListEnumeration ()
  {
    List <String> aUnmodifiableList = newUnmodifiableList (new ArrayEnumeration <String> ("Hallo",
                                                                                          "Welt",
                                                                                          "from",
                                                                                          "Vienna"));
    assertNotNull (aUnmodifiableList);
    assertEquals (4, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));
    assertTrue (aUnmodifiableList.contains ("Welt"));
    assertTrue (aUnmodifiableList.contains ("from"));
    assertTrue (aUnmodifiableList.contains ("Vienna"));

    aUnmodifiableList = newUnmodifiableList (ContainerHelper.<String> getEmptyEnumeration ());
    assertNotNull (aUnmodifiableList);
  }

  @Test
  public void testNewUnmodifiableListIterator ()
  {
    final List <String> aSource = new ArrayList <String> ();
    assertTrue (aSource.add ("Hallo"));
    assertTrue (aSource.add ("Welt"));
    assertTrue (aSource.add ("from"));
    assertTrue (aSource.add ("Vienna"));

    List <String> aUnmodifiableList = newUnmodifiableList (aSource.iterator ());
    assertNotNull (aUnmodifiableList);
    assertEquals (4, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));
    assertTrue (aUnmodifiableList.contains ("Welt"));
    assertTrue (aUnmodifiableList.contains ("from"));
    assertTrue (aUnmodifiableList.contains ("Vienna"));

    aUnmodifiableList = newUnmodifiableList (ContainerHelper.<String> getEmptyIterator ());
    assertNotNull (aUnmodifiableList);
  }

  @Test
  public void testNewUnmodifiableListCollection ()
  {
    final List <String> aSource = newUnmodifiableList ("Hallo", "Welt", "from", "Vienna");

    List <String> aUnmodifiableList = newUnmodifiableList (aSource);
    assertNotNull (aUnmodifiableList);
    assertEquals (4, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));
    assertTrue (aUnmodifiableList.contains ("Welt"));
    assertTrue (aUnmodifiableList.contains ("from"));
    assertTrue (aUnmodifiableList.contains ("Vienna"));

    aUnmodifiableList = newUnmodifiableList (new ArrayList <String> ());
    assertNotNull (aUnmodifiableList);

    aUnmodifiableList = newUnmodifiableList ((List <String>) null);
    assertNotNull (aUnmodifiableList);
  }

  @Test
  public void testNewUnmodifiableListIterable ()
  {
    final List <String> aSource = newUnmodifiableList ("Hallo", "Welt", "from", "Vienna");

    List <String> aUnmodifiableList = newUnmodifiableList ((Iterable <String>) aSource);
    assertNotNull (aUnmodifiableList);
    assertEquals (4, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));
    assertTrue (aUnmodifiableList.contains ("Welt"));
    assertTrue (aUnmodifiableList.contains ("from"));
    assertTrue (aUnmodifiableList.contains ("Vienna"));

    aUnmodifiableList = newUnmodifiableList ((Iterable <String>) new ArrayList <String> ());
    assertNotNull (aUnmodifiableList);

    aUnmodifiableList = newUnmodifiableList ((Iterable <String>) null);
    assertNotNull (aUnmodifiableList);
  }

  @Test
  public void testNewUnmodifiableListIIterableIterator ()
  {
    final List <String> aSource = newUnmodifiableList ("Hallo", "Welt", "from", "Vienna");

    List <String> aUnmodifiableList = newUnmodifiableList (IterableIterator.create (aSource));
    assertNotNull (aUnmodifiableList);
    assertEquals (4, aUnmodifiableList.size ());
    assertTrue (aUnmodifiableList.contains ("Hallo"));
    assertTrue (aUnmodifiableList.contains ("Welt"));
    assertTrue (aUnmodifiableList.contains ("from"));
    assertTrue (aUnmodifiableList.contains ("Vienna"));

    aUnmodifiableList = newUnmodifiableList (IterableIterator.create (new ArrayList <String> ()));
    assertNotNull (aUnmodifiableList);

    aUnmodifiableList = newUnmodifiableList ((IIterableIterator <String>) null);
    assertNotNull (aUnmodifiableList);
  }

  @Test
  public void testNewStackSingleValue ()
  {
    NonBlockingStack <String> aStack = newStack ();
    assertNotNull (aStack);
    aStack = newStack ("Hallo");
    assertNotNull (aStack);
    assertEquals (aStack.size (), 1);
    assertTrue (aStack.contains ("Hallo"));
    assertEquals ("Hallo", aStack.peek ());
    assertEquals ("Hallo", aStack.pop ());
  }

  @Test
  public void testNewStackArray ()
  {
    final NonBlockingStack <String> aStack = newStack ("Hallo", "Welt");
    assertEquals (getStackCopyWithoutTop (aStack), newStack ("Hallo"));
    assertNotNull (aStack);
    assertEquals (aStack.size (), 2);
    assertTrue (aStack.contains ("Welt"));
    assertTrue (aStack.contains ("Hallo"));
    assertEquals ("Welt", aStack.peek ());
    assertEquals ("Welt", aStack.pop ());
    assertEquals ("Hallo", aStack.peek ());
    assertEquals ("Hallo", aStack.pop ());
    assertTrue (aStack.isEmpty ());

    assertNull (getStackCopyWithoutTop (new NonBlockingStack <String> ()));
  }

  @Test
  public void testGetSortedIterator ()
  {
    assertNotNull (getSorted ((Iterator <String>) null));

    final List <String> aList = newList ("d", "c", "b", "a");
    final List <String> aSorted = getSorted (aList.iterator ());
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "a");
    assertEquals (aSorted.get (1), "b");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  @Test
  public void testGetSortedIterable ()
  {
    assertNotNull (getSorted ((Iterable <String>) null));

    final List <String> aList = newList ("d", "c", "b", "a");
    final List <String> aSorted = getSorted (aList);
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "a");
    assertEquals (aSorted.get (1), "b");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  @Test
  public void testGetSortedIIterableIterator ()
  {
    assertNotNull (getSorted ((IIterableIterator <String>) null));

    final List <String> aList = newList ("d", "c", "b", "a");
    List <String> aSorted = getSorted (IterableIterator.create (aList));
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "a");
    assertEquals (aSorted.get (1), "b");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");

    aSorted = getSorted (IterableIterator.create (aList), new ComparatorString ());
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "a");
    assertEquals (aSorted.get (1), "b");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  private static final class MyStringCompi implements Comparator <String>, Serializable
  {
    public MyStringCompi ()
    {}

    public int compare (final String sStr1, final String sStr2)
    {
      if (sStr1.equals ("b"))
        return -1;
      if (sStr2.equals ("b"))
        return +1;
      return sStr1.compareTo (sStr2);
    }
  }

  @Test
  public void testGetSortedFromIteratorWithCompi ()
  {
    assertNotNull (getSorted ((Iterator <String>) null, new MyStringCompi ()));

    final List <String> aList = newList ("d", "c", "b", "a");

    try
    {
      // null comparator not allowed
      getSorted (aList.iterator (), (Comparator <String>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final List <String> aSorted = getSorted (aList.iterator (), new MyStringCompi ());
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "b");
    assertEquals (aSorted.get (1), "a");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  @Test
  public void testGetSortedIterableWithCompi ()
  {
    assertNotNull (getSorted ((Iterable <String>) null, new MyStringCompi ()));

    final List <String> aList = newList ("d", "c", "b", "a");

    try
    {
      // null comparator not allowed
      getSorted (aList, (Comparator <String>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final List <String> aSorted = getSorted (aList, new MyStringCompi ());
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "b");
    assertEquals (aSorted.get (1), "a");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  @Test
  public void testGetSortedArray ()
  {
    assertNotNull (getSorted ((String []) null));

    final List <String> aSorted = getSorted ("d", "c", "b", "a");
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "a");
    assertEquals (aSorted.get (1), "b");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  @Test
  public void testGetSortedArrayWithCompi ()
  {
    assertNotNull (getSorted ((String []) null, new MyStringCompi ()));

    try
    {
      // null comparator not allowed
      getSorted (new String [0], null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final List <String> aSorted = getSorted (new String [] { "d", "c", "b", "a" }, new MyStringCompi ());
    assertEquals (aSorted.size (), 4);
    assertEquals (aSorted.get (0), "b");
    assertEquals (aSorted.get (1), "a");
    assertEquals (aSorted.get (2), "c");
    assertEquals (aSorted.get (3), "d");
  }

  @Test
  public void testGetEnumeratorFromIterator ()
  {
    final List <String> aList = newList ("d", "c", "b", "a");
    Enumeration <String> aEnum = getEnumeration (aList.iterator ());
    assertTrue (aEnum.hasMoreElements ());
    assertEquals (aEnum.nextElement (), "d");
    assertTrue (aEnum.hasMoreElements ());
    assertEquals (aEnum.nextElement (), "c");
    assertTrue (aEnum.hasMoreElements ());
    assertEquals (aEnum.nextElement (), "b");
    assertTrue (aEnum.hasMoreElements ());
    assertEquals (aEnum.nextElement (), "a");
    assertFalse (aEnum.hasMoreElements ());
    assertFalse (aEnum.hasMoreElements ());

    aEnum = getEnumeration ((Iterator <String>) null);
    assertFalse (aEnum.hasMoreElements ());
  }

  @Test
  public void testIsEmpty ()
  {
    assertTrue (isEmpty ((List <?>) null));
    assertTrue (isEmpty ((Map <?, ?>) null));
    assertTrue (isEmpty (new Vector <String> ()));
    assertTrue (isEmpty (new HashMap <Short, String> ()));
    assertFalse (isEmpty (newList ("d", "c", "b", "a")));
    assertTrue (isEmpty ((Iterable <String>) new NonBlockingStack <String> ()));
    assertTrue (isEmpty (EmptyEnumeration.<Double> getInstance ()));
    assertTrue (isEmpty (EmptyIterator.<Double> getInstance ()));
  }

  @Test
  public void testGetReverseIterator ()
  {
    assertNotNull (getReverseIterator (null));
    assertFalse (getReverseIterator (null).hasNext ());

    final List <String> aList = newList ("d", "c", "b", "a");
    final Iterator <String> it = getReverseIterator (aList);
    assertTrue (it.hasNext ());
    assertEquals (it.next (), "a");
    assertTrue (it.hasNext ());
    assertEquals (it.next (), "b");
    assertTrue (it.hasNext ());
    assertEquals (it.next (), "c");
    assertTrue (it.hasNext ());
    assertEquals (it.next (), "d");
    assertFalse (it.hasNext ());
    assertFalse (it.hasNext ());
    try
    {
      it.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    try
    {
      it.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}
  }

  @Test
  public void testEmptyIterator ()
  {
    final Iterator <String> it = getEmptyIterator ();
    assertNotNull (it);
    assertFalse (it.hasNext ());

    try
    {
      it.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    try
    {
      it.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}
  }

  @Test
  public void testEmptyEnumeration ()
  {
    final Enumeration <String> aEnum = getEmptyEnumeration ();
    assertNotNull (aEnum);
    assertFalse (aEnum.hasMoreElements ());

    try
    {
      aEnum.nextElement ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}
  }

  @Test
  public void testIsEmptyCollection ()
  {
    assertTrue (isEmpty ((Collection <?>) null));
    assertTrue (isEmpty ((Map <?, ?>) null));
    assertTrue (isEmpty (new ArrayList <String> ()));
    assertTrue (isEmpty (new Vector <String> ()));
    assertTrue (isEmpty (new HashSet <String> ()));
    assertTrue (isEmpty (new TreeSet <String> ()));
    assertTrue (isEmpty (new HashMap <String, String> ()));
    assertTrue (isEmpty (new LinkedHashMap <String, String> ()));

    assertFalse (isEmpty (newList ("Hallo")));
    assertFalse (isEmpty (newMap ("Hallo", "Welt")));
  }

  @Test
  public void testSize ()
  {
    assertEquals (2, getSize (newList ("Ha", "We")));
    assertEquals (1, getSize (newMap ("Ha", "We")));
    assertEquals (0, getSize ((Collection <String>) null));
    assertEquals (0, getSize ((Map <String, Double>) null));
  }

  @Test
  public void testGetIteratorFromEnumeration ()
  {
    final Enumeration <String> aSourceEnum = Collections.enumeration (newList ("a", "b", "c", "d"));
    IIterableIterator <String> it = getIterator (aSourceEnum);
    assertNotNull (it);
    assertSame (it, it.iterator ());
    assertTrue (it.hasNext ());
    assertEquals ("a", it.next ());
    assertTrue (it.hasNext ());
    assertEquals ("b", it.next ());
    assertTrue (it.hasNext ());
    assertEquals ("c", it.next ());
    assertTrue (it.hasNext ());
    assertEquals ("d", it.next ());
    assertFalse (it.hasNext ());

    try
    {
      it.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    try
    {
      it.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}

    it = getIterator ((Enumeration <String>) null);
    assertNotNull (it);
    assertFalse (it.hasNext ());
  }

  @Test
  public void testGetFilteredMap ()
  {
    assertNull (getFilteredMap (null, newList ("a")));
    assertNull (getFilteredMap (newMap ("a", "value-of-a"), null));

    final Map <String, String> aFilteredMap = getFilteredMap (newMap ("a", "value-of-a", "b", "value-of-b"),
                                                              newList ("a"));
    assertNotNull (aFilteredMap);
    assertEquals (1, aFilteredMap.size ());
    assertTrue (aFilteredMap.containsKey ("a"));
    assertEquals ("value-of-a", aFilteredMap.get ("a"));
  }

  /**
   * Test for method getSortedByKey
   */
  @Test
  public void testGetSortedByKey ()
  {
    assertNull (getSortedByKey ((Map <String, ?>) null));
    assertNull (getSortedByKey (null, new ComparatorString (ESortOrder.DESCENDING)));

    try
    {
      // null Comparator
      getSortedByKey (newMap (), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final Map <String, String> aMap = newMap ("K2", "ValueA", "K3", "ValueB", "K1", "ValueC");
    assertNotNull (aMap);
    assertEquals (3, aMap.size ());

    Iterator <Map.Entry <String, String>> it = getSortedByKey (aMap).entrySet ().iterator ();
    assertEquals ("K1", it.next ().getKey ());
    assertEquals ("K2", it.next ().getKey ());
    assertEquals ("K3", it.next ().getKey ());

    // reverse sort
    it = getSortedByKey (aMap, new ComparatorString (ESortOrder.DESCENDING)).entrySet ().iterator ();
    assertEquals ("K3", it.next ().getKey ());
    assertEquals ("K2", it.next ().getKey ());
    assertEquals ("K1", it.next ().getKey ());
  }

  /**
   * Test for method getSortedByValue
   */
  @Test
  public void testGetSortedByValue ()
  {
    assertNull (getSortedByValue ((Map <?, String>) null));
    assertNull (getSortedByValue (null, new ComparatorString (ESortOrder.DESCENDING)));

    try
    {
      // null Comparator
      getSortedByValue (newMap (), null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    final Map <String, String> aMap = newMap ("K1", "ValueB", "K2", "ValueC", "K3", "ValueA");
    assertNotNull (aMap);
    assertEquals (3, aMap.size ());

    Iterator <Map.Entry <String, String>> it = getSortedByValue (aMap).entrySet ().iterator ();
    assertEquals ("ValueA", it.next ().getValue ());
    assertEquals ("ValueB", it.next ().getValue ());
    assertEquals ("ValueC", it.next ().getValue ());

    // reverse sort
    it = getSortedByValue (aMap, new ComparatorString (ESortOrder.DESCENDING)).entrySet ().iterator ();
    assertEquals ("ValueC", it.next ().getValue ());
    assertEquals ("ValueB", it.next ().getValue ());
    assertEquals ("ValueA", it.next ().getValue ());
  }

  /**
   * Test for method getIterator
   */
  @Test
  public void testGetIterator_Iterable ()
  {
    assertNotNull (getIterator ((ArrayList <?>) null));
    assertFalse (getIterator ((ArrayList <?>) null).hasNext ());
    assertTrue (getIterator (newList ("abc")).hasNext ());
  }

  /**
   * Test for method getIterator
   */
  @Test
  public void testGetIterator_Iterator ()
  {
    assertNotNull (getIterator ((Iterator <?>) null));
    assertFalse (getIterator ((Iterator <?>) null).hasNext ());
    assertTrue (getIterator (newList ("abc").iterator ()).hasNext ());
  }

  /**
   * Test for method getIterator
   */
  @Test
  public void testGetIterator_Array ()
  {
    assertNotNull (getIterator (new String [0]));
    assertFalse (getIterator (new String [0]).hasNext ());
    assertTrue (getIterator ("a").hasNext ());
  }

  @Test
  public void testGetReverseList ()
  {
    assertNotNull (getReverseList (null));
    assertTrue (getReverseList (null).isEmpty ());

    // Make it not sorted :)
    final List <String> aList = newList ("1", "3", "2");
    final List <String> aReverse = getReverseList (aList);
    assertNotNull (aReverse);
    assertEquals (3, aReverse.size ());
    assertEquals ("2", aReverse.get (0));
    assertEquals ("3", aReverse.get (1));
    assertEquals ("1", aReverse.get (2));

    // Check original
    assertEquals (3, aList.size ());
    assertEquals ("1", aList.get (0));
    assertEquals ("3", aList.get (1));
    assertEquals ("2", aList.get (2));
  }

  @Test
  public void testGetReverseInlineList ()
  {
    List <String> aList = newList ("1", "3", "2");

    // Sort inline
    assertSame (aList, getReverseInlineList (aList));
    assertEquals (3, aList.size ());
    assertEquals ("2", aList.get (0));
    assertEquals ("3", aList.get (1));
    assertEquals ("1", aList.get (2));

    aList = newList ();
    assertSame (aList, getReverseInlineList (aList));
    assertEquals (0, aList.size ());

    assertNull (getReverseInlineList (null));
  }

  @Test
  public void testGetCombinedEnumeration ()
  {
    Enumeration <String> e = getCombinedEnumeration (getEnumeration ("a", "b"), getEnumeration ("c", "d"));
    assertTrue (e.hasMoreElements ());
    List <String> aList = newList (e);
    assertNotNull (aList);
    assertEquals (4, aList.size ());
    assertEquals ("a", aList.get (0));
    assertEquals ("b", aList.get (1));
    assertEquals ("c", aList.get (2));
    assertEquals ("d", aList.get (3));

    e = getCombinedEnumeration (getEnumeration ("a", "b"), null);
    aList = newList (e);
    assertNotNull (aList);
    assertEquals (2, aList.size ());
    assertEquals ("a", aList.get (0));
    assertEquals ("b", aList.get (1));

    e = getCombinedEnumeration (null, getEnumeration ("c", "d"));
    aList = newList (e);
    assertNotNull (aList);
    assertEquals (2, aList.size ());
    assertEquals ("c", aList.get (0));
    assertEquals ("d", aList.get (1));

    e = getCombinedEnumeration (null, null);
    assertFalse (e.hasMoreElements ());
    aList = newList (e);
    assertNotNull (aList);
    assertTrue (aList.isEmpty ());
  }

  @Test
  public void testFirstAndLast ()
  {
    final List <String> aList = newList ("s1", "s2", "s3");
    final Set <String> aSet = new LinkedHashSet <String> (aList);

    assertNull (removeFirstElement (new ArrayList <String> ()));
    assertNull (removeFirstElement ((List <String>) null));

    assertEquals ("s1", getFirstElement (aList));
    assertEquals ("s1", getFirstElement (aSet));
    assertEquals ("s1", getFirstElement ((Iterable <String>) aSet));
    assertEquals ("s1", removeFirstElement (aList));
    assertNull (getFirstElement (new ArrayList <String> ()));
    assertNull (getFirstElement (new HashSet <String> ()));
    assertNull (getFirstElement ((Iterable <String>) new HashSet <String> ()));
    assertNull (getFirstElement ((List <String>) null));
    assertNull (getFirstElement ((Set <String>) null));
    assertNull (getFirstElement ((Iterable <String>) null));

    assertNull (removeLastElement (new ArrayList <String> ()));
    assertNull (removeLastElement ((List <String>) null));

    assertEquals ("s3", getLastElement (aList));
    assertEquals ("s3", getLastElement (aSet));
    assertEquals ("s3", getLastElement ((Iterable <String>) aSet));
    assertEquals ("s3", removeLastElement (aList));
    assertNull (getLastElement (new ArrayList <String> ()));
    assertNull (getLastElement (new HashSet <String> ()));
    assertNull (getLastElement ((Iterable <String>) new HashSet <String> ()));
    assertNull (getLastElement ((List <String>) null));
    assertNull (getLastElement ((Set <String>) null));
    assertNull (getLastElement ((Iterable <String>) null));
  }

  @Test
  public void testGetFirstElement_Map ()
  {
    final Map <String, Integer> aMap = newOrderedMap (new String [] { "a", "b" }, new Integer [] { I1, I2 });
    assertNotNull (getFirstElement (aMap));
    assertEquals ("a", getFirstElement (aMap).getKey ());
    assertEquals (I1, getFirstElement (aMap).getValue ());

    assertNull (getFirstElement (newMap ()));
    assertNull (getFirstElement ((Map <?, ?>) null));
  }

  @Test
  public void testGetFirstKey ()
  {
    final Map <String, Integer> aMap = newOrderedMap (new String [] { "a", "b" }, new Integer [] { I1, I2 });
    assertNotNull (getFirstKey (aMap));
    assertEquals ("a", getFirstKey (aMap));

    assertNull (getFirstKey (newMap ()));
    assertNull (getFirstKey ((Map <?, ?>) null));
  }

  @Test
  public void testGetFirstValue ()
  {
    final Map <String, Integer> aMap = newOrderedMap (new String [] { "a", "b" }, new Integer [] { I1, I2 });
    assertNotNull (getFirstValue (aMap));
    assertEquals (I1, getFirstValue (aMap));

    assertNull (getFirstValue (newMap ()));
    assertNull (getFirstValue ((Map <?, ?>) null));
  }

  @Test
  public void testIsEmpty_Iterable ()
  {
    assertTrue (isEmpty ((Iterable <?>) null));
    assertTrue (isEmpty ((Iterable <String>) new ArrayList <String> ()));
    assertFalse (isEmpty ((Iterable <String>) newList ("any")));
  }

  @Test
  public void testIsEmpty_Iterator ()
  {
    assertTrue (isEmpty ((Iterator <?>) null));
    assertTrue (isEmpty (new ArrayList <String> ().iterator ()));
    assertTrue (isEmpty (EmptyIterator.<String> getInstance ()));
    assertFalse (isEmpty (newList ("any").iterator ()));
  }

  @Test
  public void testIsEmpty_IIterableIterator ()
  {
    assertTrue (isEmpty ((IIterableIterator <?>) null));
    assertTrue (isEmpty (IterableIterator.create (new ArrayList <String> ())));
    assertTrue (isEmpty (IterableIterator.<String> createEmpty ()));
    assertFalse (isEmpty (IterableIterator.create (newList ("any"))));
  }

  @Test
  public void testIsEmpty_Enumeration ()
  {
    assertTrue (isEmpty ((Enumeration <?>) null));
    assertTrue (isEmpty (getEnumeration (new ArrayList <String> ())));
    assertTrue (isEmpty (EmptyEnumeration.<String> getInstance ()));
    assertFalse (isEmpty (getEnumeration (newList ("any"))));
  }

  @Test
  public void testIsEmpty_Collection ()
  {
    assertTrue (isEmpty ((Collection <?>) null));
    assertTrue (isEmpty (new ArrayList <String> ()));
    assertFalse (isEmpty (newList ("any")));
  }

  @Test
  public void testIsEmpty_Map ()
  {
    assertTrue (isEmpty ((Map <?, ?>) null));
    assertTrue (isEmpty (new HashMap <String, Double> ()));
    assertFalse (isEmpty (newMap ("any", "value")));
  }

  @Test
  public void testGetSize_Collection ()
  {
    assertEquals (0, getSize ((Collection <?>) null));
    assertEquals (0, getSize (new ArrayList <String> ()));
    assertEquals (1, getSize (newList ("any")));
  }

  @Test
  public void testGetSize_Map ()
  {
    assertEquals (0, getSize ((Map <?, ?>) null));
    assertEquals (0, getSize (new HashMap <BigDecimal, String> ()));
    assertEquals (1, getSize (newMap ("key", "value")));
  }

  @Test
  public void testGetSize_Iterable ()
  {
    assertEquals (0, getSize ((Iterable <?>) null));
    assertEquals (0, getSize ((Iterable <String>) new ArrayList <String> ()));
    assertEquals (1, getSize ((Iterable <String>) newList ("any")));
  }

  @Test
  public void testGetSize_IterableIterator ()
  {
    assertEquals (0, getSize ((IIterableIterator <?>) null));
    assertEquals (0, getSize (IterableIterator.createEmpty ()));
    assertEquals (1, getSize (IterableIterator.create (newList ("any"))));
  }

  @Test
  public void testGetSize_Iterator ()
  {
    assertEquals (0, getSize ((Iterator <?>) null));
    assertEquals (0, getSize (new ArrayList <String> ().iterator ()));
    assertEquals (1, getSize (newList ("any").iterator ()));
  }

  @Test
  public void testGetConcatenatedList_CollectionCollection ()
  {
    final List <String> a = newList ("a", "b");
    final List <String> b = newList ("c", "d");
    assertTrue (getConcatenatedList ((Collection <String>) null, (Collection <String>) null).isEmpty ());
    assertEquals (a, getConcatenatedList (a, (Collection <String>) null));
    assertEquals (b, getConcatenatedList ((Collection <String>) null, b));
    assertEquals (newList ("a", "b", "c", "d"), getConcatenatedList (a, b));
  }

  @Test
  public void testGetConcatenatedList_CollectionArray ()
  {
    final List <String> a = newList ("a", "b");
    final String [] b = ArrayHelper.newArray ("c", "d");
    assertTrue (getConcatenatedList ((Collection <String>) null, (String []) null).isEmpty ());
    assertEquals (a, getConcatenatedList (a, (String []) null));
    assertEquals (newList (b), getConcatenatedList ((Collection <String>) null, b));
    assertEquals (newList ("a", "b", "c", "d"), getConcatenatedList (a, b));
  }

  @Test
  public void testGetConcatenatedList_ArrayCollection ()
  {
    final String [] a = ArrayHelper.newArray ("a", "b");
    final List <String> b = newList ("c", "d");
    assertTrue (getConcatenatedList ((String []) null, (Collection <String>) null).isEmpty ());
    assertEquals (newList (a), getConcatenatedList (a, (Collection <String>) null));
    assertEquals (b, getConcatenatedList ((String []) null, b));
    assertEquals (newList ("a", "b", "c", "d"), getConcatenatedList (a, b));
  }

  @Test
  public void testGetConcatenatedSet_CollectionCollection ()
  {
    final Set <String> a = newSet ("a", "b");
    final Set <String> b = newSet ("c", "d");
    assertTrue (getConcatenatedSet ((Collection <String>) null, (Collection <String>) null).isEmpty ());
    assertEquals (a, getConcatenatedSet (a, (Collection <String>) null));
    assertEquals (b, getConcatenatedSet ((Collection <String>) null, b));
    assertEquals (newSet ("a", "b", "c", "d"), getConcatenatedSet (a, b));
  }

  @Test
  public void testGetConcatenatedSet_CollectionArray ()
  {
    final Set <String> a = newSet ("a", "b");
    final String [] b = ArrayHelper.newArray ("c", "d");
    assertTrue (getConcatenatedSet ((Collection <String>) null, (String []) null).isEmpty ());
    assertEquals (a, getConcatenatedSet (a, (String []) null));
    assertEquals (newSet (b), getConcatenatedSet ((Collection <String>) null, b));
    assertEquals (newSet ("a", "b", "c", "d"), getConcatenatedSet (a, b));
  }

  @Test
  public void testGetConcatenatedSet_ArrayCollection ()
  {
    final String [] a = ArrayHelper.newArray ("a", "b");
    final Set <String> b = newSet ("c", "d");
    assertTrue (getConcatenatedSet ((String []) null, (Collection <String>) null).isEmpty ());
    assertEquals (newSet (a), getConcatenatedSet (a, (Collection <String>) null));
    assertEquals (b, getConcatenatedSet ((String []) null, b));
    assertEquals (newSet ("a", "b", "c", "d"), getConcatenatedSet (a, b));
  }

  @Test
  public void testGetConcatenatedInline ()
  {
    // Array version
    List <String> aBaseList = newList ("1");
    assertSame (aBaseList, getConcatenatedInline (aBaseList, "2", "3"));
    assertEquals (3, aBaseList.size ());
    assertEquals ("1", aBaseList.get (0));
    assertEquals ("3", aBaseList.get (2));
    assertSame (aBaseList, getConcatenatedInline (aBaseList, (String []) null));
    assertEquals (3, aBaseList.size ());

    // Collection version
    aBaseList = newList ("1");
    assertSame (aBaseList, getConcatenatedInline (aBaseList, newList ("2", "3")));
    assertEquals (3, aBaseList.size ());
    assertEquals ("1", aBaseList.get (0));
    assertEquals ("3", aBaseList.get (2));
    assertSame (aBaseList, getConcatenatedInline (aBaseList, (Collection <String>) null));
    assertEquals (3, aBaseList.size ());

    // Set test
    final Set <String> aBaseSet = newSet ("1");
    assertSame (aBaseSet, getConcatenatedInline (aBaseSet, "2", "3"));
    assertEquals (3, aBaseSet.size ());
    assertTrue (aBaseSet.contains ("1"));
    assertTrue (aBaseSet.contains ("3"));
    assertSame (aBaseSet, getConcatenatedInline (aBaseSet, (String []) null));
    assertEquals (3, aBaseSet.size ());

    try
    {
      getConcatenatedInline ((List <String>) null, new String [0]);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      getConcatenatedInline ((List <String>) null, newList ("a"));
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetCombinedMap ()
  {
    final Map <String, Integer> m1 = newMap ("Hallo", I1);
    final Map <String, Integer> m2 = newMap ("Welt", I2);
    assertTrue (getCombinedMap (null, null).isEmpty ());
    assertEquals (m1, getCombinedMap (m1, null));
    assertEquals (m2, getCombinedMap (null, m2));

    final Map <String, Integer> m12 = getCombinedMap (m1, m2);
    assertNotNull (m12);
    assertEquals (2, m12.size ());
    assertTrue (m12.containsKey ("Hallo"));
    assertTrue (m12.containsKey ("Welt"));

    final Map <String, Integer> m1212 = getCombinedMap (m12, m12);
    assertNotNull (m1212);
    assertEquals (2, m1212.size ());
    assertTrue (m1212.containsKey ("Hallo"));
    assertTrue (m1212.containsKey ("Welt"));
  }

  @Test
  public void testNewObjectListFromArray ()
  {
    assertNotNull (newObjectListFromArray (new Integer [] { Integer.valueOf (2), Integer.valueOf (0x7f) },
                                           Integer.class));
    assertNotNull (newObjectListFromArray (new boolean [] { true, false }, boolean.class));
    assertNotNull (newObjectListFromArray (new byte [] { (byte) 2, (byte) 0x7f }, byte.class));
    assertNotNull (newObjectListFromArray (new char [] { 'a', 'Z' }, char.class));
    assertNotNull (newObjectListFromArray (new double [] { 3.14, 47.11 }, double.class));
    assertNotNull (newObjectListFromArray (new float [] { 3.14f, 47.11f }, float.class));
    assertNotNull (newObjectListFromArray (new int [] { 314, 4711 }, int.class));
    assertNotNull (newObjectListFromArray (new long [] { 314, 4711 }, long.class));
    assertNotNull (newObjectListFromArray (new short [] { 123, 255 }, short.class));

    assertNull (newObjectListFromArray (null, Integer.class));
    assertNull (newObjectListFromArray (null, boolean.class));
    assertNull (newObjectListFromArray (null, byte.class));
    assertNull (newObjectListFromArray (null, char.class));
    assertNull (newObjectListFromArray (null, double.class));
    assertNull (newObjectListFromArray (null, float.class));
    assertNull (newObjectListFromArray (null, int.class));
    assertNull (newObjectListFromArray (null, long.class));
    assertNull (newObjectListFromArray (null, short.class));
  }

  @Test
  @SuppressFBWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  public void testGetSubList ()
  {
    assertNotNull (getSubList (null, 0, 5));

    try
    {
      // start index may not be < 0
      getSubList (new ArrayList <String> (), -1, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // length not be < 0
      getSubList (new ArrayList <String> (), 0, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final List <String> aSource = newList ("a", "b", "c", "d");

    List <String> aSubList = getSubList (aSource, 0, 2);
    assertNotNull (aSubList);
    assertEquals (2, aSubList.size ());
    assertTrue (aSubList.contains ("a"));
    assertTrue (aSubList.contains ("b"));
    assertFalse (aSubList.contains ("c"));
    assertFalse (aSubList.contains ("d"));

    aSubList = getSubList (aSource, 1, 2);
    assertNotNull (aSubList);
    assertEquals (2, aSubList.size ());
    assertFalse (aSubList.contains ("a"));
    assertTrue (aSubList.contains ("b"));
    assertTrue (aSubList.contains ("c"));
    assertFalse (aSubList.contains ("d"));

    aSubList = getSubList (aSource, 2, 2);
    assertNotNull (aSubList);
    assertEquals (2, aSubList.size ());
    assertFalse (aSubList.contains ("a"));
    assertFalse (aSubList.contains ("b"));
    assertTrue (aSubList.contains ("c"));
    assertTrue (aSubList.contains ("d"));

    aSubList = getSubList (aSource, 3, 2);
    assertNotNull (aSubList);
    assertEquals (1, aSubList.size ());
    assertFalse (aSubList.contains ("a"));
    assertFalse (aSubList.contains ("b"));
    assertFalse (aSubList.contains ("c"));
    assertTrue (aSubList.contains ("d"));

    aSubList = getSubList (aSource, 4, 2);
    assertNotNull (aSubList);
  }

  @Test
  public void testGetSwappedKeyValues ()
  {
    final Map <String, Integer> aMap = newMap (new String [] { "a", "b", "c" }, new Integer [] { I0, I1, I2 });
    final Map <Integer, String> aMap2 = getSwappedKeyValues (aMap);
    assertEquals (aMap.size (), aMap2.size ());
    assertEquals (aMap, getSwappedKeyValues (aMap2));
    assertNull (getSwappedKeyValues (newMap ()));
    assertNull (getSwappedKeyValues (null));
  }

  @Test
  public void testGetReverseLookupSet ()
  {
    assertNull (getReverseLookupSet (new MultiHashMapArrayListBased <String, Integer> ()));
    assertNull (getReverseLookupSet (null));

    final IMultiMapListBased <String, Integer> aMap = new MultiHashMapArrayListBased <String, Integer> ();
    aMap.putSingle ("a", I0);
    aMap.putSingle ("a", I1);
    aMap.putSingle ("a", I2);
    aMap.putSingle ("b", I0);
    aMap.putSingle ("b", I1);
    aMap.putSingle ("b", I2);
    assertEquals (2, aMap.size ());

    final IMultiMapSetBased <Integer, String> aMap2 = getReverseLookupSet (aMap);
    assertEquals (3, aMap2.size ());
    for (final Map.Entry <Integer, Set <String>> aEntry : aMap2.entrySet ())
      assertEquals (2, aEntry.getValue ().size ());
  }

  @Test
  public void testGetReverseLookup ()
  {
    assertNull (getReverseLookup (new MultiHashMapHashSetBased <String, Integer> ()));
    assertNull (getReverseLookup (null));

    final IMultiMapSetBased <String, Integer> aMap = new MultiHashMapHashSetBased <String, Integer> ();
    aMap.putSingle ("a", I0);
    aMap.putSingle ("a", I1);
    aMap.putSingle ("a", I2);
    aMap.putSingle ("b", I0);
    aMap.putSingle ("b", I1);
    aMap.putSingle ("b", I2);
    assertEquals (2, aMap.size ());

    final IMultiMapSetBased <Integer, String> aMap2 = getReverseLookup (aMap);
    assertEquals (3, aMap2.size ());
    for (final Map.Entry <Integer, Set <String>> aEntry : aMap2.entrySet ())
      assertEquals (2, aEntry.getValue ().size ());
  }

  @Test
  public void testGetSafe ()
  {
    assertNull (getSafe (null, 0));
    assertNull (getSafe (null, -1));
    assertNull (getSafe (null, 1));

    final List <String> aList = newList ("a", "b");
    assertNull (getSafe (aList, -1));
    assertEquals ("a", getSafe (aList, 0));
    assertNull (getSafe (aList, 2));

    assertEquals ("x", getSafe (aList, -1, "x"));
    assertEquals ("a", getSafe (aList, 0, "x"));
    assertEquals ("x", getSafe (aList, 2, "x"));
  }

  @Test
  public void testContainsNullElement ()
  {
    assertFalse (containsAnyNullElement ((List <String>) null));
    assertFalse (containsAnyNullElement (new ArrayList <String> ()));
    assertFalse (containsAnyNullElement (newList ("a")));
    assertFalse (containsAnyNullElement (newList ("a", "b", "c")));
    assertTrue (containsAnyNullElement (newList (null, "a")));
    assertTrue (containsAnyNullElement (newList ("a", null)));
    assertTrue (containsAnyNullElement (newList ((String) null)));
    assertTrue (containsAnyNullElement (newList (null, Integer.valueOf (5))));
  }

  @Test
  public void testNewBooleanContainer ()
  {
    assertTrue (ContainerHelper.newBooleanList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableBooleanList ().isEmpty ());
    assertTrue (ContainerHelper.newBooleanSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableBooleanSet ().isEmpty ());
    assertTrue (ContainerHelper.newBooleanOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableBooleanOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newBooleanSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableBooleanSortedSet ().isEmpty ());

    final boolean [] aValues = new boolean [] { true, true };
    assertEquals (2, ContainerHelper.newBooleanList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableBooleanList (aValues).size ());
    assertEquals (1, ContainerHelper.newBooleanSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableBooleanSet (aValues).size ());
    assertEquals (1, ContainerHelper.newBooleanOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableBooleanOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newBooleanSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableBooleanSortedSet (aValues).size ());
  }

  @Test
  public void testNewByteContainer ()
  {
    assertTrue (ContainerHelper.newByteList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableByteList ().isEmpty ());
    assertTrue (ContainerHelper.newByteSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableByteSet ().isEmpty ());
    assertTrue (ContainerHelper.newByteOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableByteOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newByteSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableByteSortedSet ().isEmpty ());

    final byte [] aValues = new byte [] { 3, 3 };
    assertEquals (2, ContainerHelper.newByteList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableByteList (aValues).size ());
    assertEquals (1, ContainerHelper.newByteSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableByteSet (aValues).size ());
    assertEquals (1, ContainerHelper.newByteOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableByteOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newByteSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableByteSortedSet (aValues).size ());
  }

  @Test
  public void testNewCharContainer ()
  {
    assertTrue (ContainerHelper.newCharList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableCharList ().isEmpty ());
    assertTrue (ContainerHelper.newCharSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableCharSet ().isEmpty ());
    assertTrue (ContainerHelper.newCharOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableCharOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newCharSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableCharSortedSet ().isEmpty ());

    final char [] aValues = new char [] { 'x', 'x' };
    assertEquals (2, ContainerHelper.newCharList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableCharList (aValues).size ());
    assertEquals (1, ContainerHelper.newCharSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableCharSet (aValues).size ());
    assertEquals (1, ContainerHelper.newCharOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableCharOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newCharSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableCharSortedSet (aValues).size ());
  }

  @Test
  public void testNewDoubleContainer ()
  {
    assertTrue (ContainerHelper.newDoubleList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableDoubleList ().isEmpty ());
    assertTrue (ContainerHelper.newDoubleSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableDoubleSet ().isEmpty ());
    assertTrue (ContainerHelper.newDoubleOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableDoubleOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newDoubleSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableDoubleSortedSet ().isEmpty ());

    final double [] aValues = new double [] { 1.1, 1.1 };
    assertEquals (2, ContainerHelper.newDoubleList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableDoubleList (aValues).size ());
    assertEquals (1, ContainerHelper.newDoubleSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableDoubleSet (aValues).size ());
    assertEquals (1, ContainerHelper.newDoubleOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableDoubleOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newDoubleSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableDoubleSortedSet (aValues).size ());
  }

  @Test
  public void testNewFloatContainer ()
  {
    assertTrue (ContainerHelper.newFloatList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableFloatList ().isEmpty ());
    assertTrue (ContainerHelper.newFloatSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableFloatSet ().isEmpty ());
    assertTrue (ContainerHelper.newFloatOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableFloatOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newFloatSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableFloatSortedSet ().isEmpty ());

    final float [] aValues = new float [] { 3.2f, 3.2f };
    assertEquals (2, ContainerHelper.newFloatList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableFloatList (aValues).size ());
    assertEquals (1, ContainerHelper.newFloatSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableFloatSet (aValues).size ());
    assertEquals (1, ContainerHelper.newFloatOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableFloatOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newFloatSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableFloatSortedSet (aValues).size ());
  }

  @Test
  public void testNewIntContainer ()
  {
    assertTrue (ContainerHelper.newIntList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableIntList ().isEmpty ());
    assertTrue (ContainerHelper.newIntSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableIntSet ().isEmpty ());
    assertTrue (ContainerHelper.newIntOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableIntOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newIntSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableIntSortedSet ().isEmpty ());

    final int [] aValues = new int [] { 5, 5 };
    assertEquals (2, ContainerHelper.newIntList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableIntList (aValues).size ());
    assertEquals (1, ContainerHelper.newIntSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableIntSet (aValues).size ());
    assertEquals (1, ContainerHelper.newIntOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableIntOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newIntSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableIntSortedSet (aValues).size ());
  }

  @Test
  public void testNewLongContainer ()
  {
    assertTrue (ContainerHelper.newLongList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableLongList ().isEmpty ());
    assertTrue (ContainerHelper.newLongSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableLongSet ().isEmpty ());
    assertTrue (ContainerHelper.newLongOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableLongOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newLongSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableLongSortedSet ().isEmpty ());

    final long [] aValues = new long [] { 17, 17 };
    assertEquals (2, ContainerHelper.newLongList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableLongList (aValues).size ());
    assertEquals (1, ContainerHelper.newLongSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableLongSet (aValues).size ());
    assertEquals (1, ContainerHelper.newLongOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableLongOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newLongSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableLongSortedSet (aValues).size ());
  }

  @Test
  public void testNewShortContainer ()
  {
    assertTrue (ContainerHelper.newShortList ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableShortList ().isEmpty ());
    assertTrue (ContainerHelper.newShortSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableShortSet ().isEmpty ());
    assertTrue (ContainerHelper.newShortOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableShortOrderedSet ().isEmpty ());
    assertTrue (ContainerHelper.newShortSortedSet ().isEmpty ());
    assertTrue (ContainerHelper.newUnmodifiableShortSortedSet ().isEmpty ());

    final short [] aValues = new short [] { 5, 5 };
    assertEquals (2, ContainerHelper.newShortList (aValues).size ());
    assertEquals (2, ContainerHelper.newUnmodifiableShortList (aValues).size ());
    assertEquals (1, ContainerHelper.newShortSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableShortSet (aValues).size ());
    assertEquals (1, ContainerHelper.newShortOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableShortOrderedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newShortSortedSet (aValues).size ());
    assertEquals (1, ContainerHelper.newUnmodifiableShortSortedSet (aValues).size ());
  }
}
