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
package com.phloc.commons.collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.commons.collections.multimap.MultiHashMapHashSetBased;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.mock.AbstractPhlocTestCase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test for {@link LockedContainerHelper}
 *
 * @author Boris Gregorcic
 */
@SuppressWarnings ("static-method")
public class LockedContainerHelperTest extends AbstractPhlocTestCase
{
  private static final String KEY1 = "key1"; //$NON-NLS-1$
  private static final String KEY2 = "key2"; //$NON-NLS-1$
  private static final String VALUE1 = "value1"; //$NON-NLS-1$
  private static final String VALUE2 = "value2"; //$NON-NLS-1$
  private static final Integer VALUE3 = Integer.valueOf (3);

  private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock ();
  private static final Map <String, String> MAP_ORDERED = ContainerHelper.newOrderedMap (KEY1, VALUE1, KEY2, VALUE2);
  private static final Map <String, String> MAP = ContainerHelper.newMap (KEY1, VALUE1, KEY2, VALUE2);
  private static final Set <String> SET = ContainerHelper.newSet (KEY1, VALUE1, KEY2, VALUE2);
  private static final List <String> LIST = ContainerHelper.newList (KEY1, VALUE1, KEY2, VALUE2);

  /**
   * Test for
   * {@link LockedContainerHelper#add(Collection, Object, ReentrantReadWriteLock)}
   * passing a null container
   */
  @Test (expected = NullPointerException.class)
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testAddNullContainer ()
  {
    LockedContainerHelper.add (null, VALUE1, LOCK);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#add(Collection, Object, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testAdd ()
  {
    final Collection <String> aObjects = ContainerHelper.newSet ();
    Assert.assertFalse (LockedContainerHelper.add (aObjects, null, LOCK));
    Assert.assertTrue (LockedContainerHelper.add (aObjects, VALUE1, LOCK));
    Assert.assertFalse (LockedContainerHelper.add (aObjects, VALUE1, LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#addAll(Collection, Collection, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testAddAll ()
  {
    final Collection <String> aObjects = ContainerHelper.newSet ();
    Assert.assertFalse (LockedContainerHelper.addAll (aObjects, null, LOCK));
    Assert.assertTrue (LockedContainerHelper.addAll (aObjects, ContainerHelper.newList (VALUE1, VALUE2), LOCK));
    Assert.assertFalse (LockedContainerHelper.addAll (aObjects, ContainerHelper.newList (VALUE1, VALUE2), LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#remove(Collection, Object, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testRemove ()
  {
    final Collection <String> aObjects = ContainerHelper.newSet ();
    Assert.assertFalse (LockedContainerHelper.remove (aObjects, null, LOCK));
    Assert.assertFalse (LockedContainerHelper.remove (aObjects, VALUE1, LOCK));
    LockedContainerHelper.add (aObjects, VALUE1, LOCK);
    Assert.assertTrue (LockedContainerHelper.remove (aObjects, VALUE1, LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#removeAll(Collection, Collection, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testRemoveAll ()
  {
    final Collection <String> aObjects = ContainerHelper.newSet ();
    Assert.assertFalse (LockedContainerHelper.removeAll (aObjects, null, LOCK));
    Assert.assertFalse (LockedContainerHelper.removeAll (aObjects, ContainerHelper.newList (VALUE1, VALUE2), LOCK));
    LockedContainerHelper.addAll (aObjects, ContainerHelper.newList (VALUE1, VALUE2), LOCK);
    Assert.assertTrue (LockedContainerHelper.removeAll (aObjects, ContainerHelper.newList (VALUE1, VALUE2), LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#put(Map, Object, Object, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testPut ()
  {
    final Map <String, String> aObjects = ContainerHelper.newMap ();
    Assert.assertTrue (aObjects.isEmpty ());
    Assert.assertNull (LockedContainerHelper.put (null, KEY1, VALUE1, LOCK));
    Assert.assertNull (LockedContainerHelper.put (aObjects, KEY1, VALUE1, LOCK));
    Assert.assertFalse (aObjects.isEmpty ());
  }

  /**
   * Test for
   * {@link LockedContainerHelper#put(Map, Object, Object, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testPutAll ()
  {
    final Map <String, String> aObjects = ContainerHelper.newMap ();
    final Map <String, String> aObjectsToAdd = ContainerHelper.newMap ();
    aObjectsToAdd.put (KEY1, VALUE1);
    aObjectsToAdd.put (KEY2, VALUE2);
    Assert.assertTrue (aObjects.isEmpty ());

    LockedContainerHelper.putAll (null, null, LOCK);
    Assert.assertTrue (aObjects.isEmpty ());

    LockedContainerHelper.putAll (aObjects, null, LOCK);
    Assert.assertTrue (aObjects.isEmpty ());

    LockedContainerHelper.putAll (aObjects, aObjectsToAdd, LOCK);
    Assert.assertFalse (aObjects.isEmpty ());
  }

  /**
   * Test for
   * {@link LockedContainerHelper#remove(Map, Object, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testRemoveMap ()
  {
    final Map <String, String> aObjects = ContainerHelper.newMap ();
    Assert.assertTrue (aObjects.isEmpty ());
    Assert.assertNull (LockedContainerHelper.remove ((Map <String, String>) null, KEY1, LOCK));
    Assert.assertNull (LockedContainerHelper.remove (aObjects, null, LOCK));
    Assert.assertNull (LockedContainerHelper.remove (aObjects, KEY1, LOCK));
    LockedContainerHelper.put (aObjects, KEY1, VALUE1, LOCK);
    Assert.assertEquals (LockedContainerHelper.remove (aObjects, KEY1, LOCK), VALUE1);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#isEmpty(Collection, ReentrantReadWriteLock)}
   */
  @Test
  public void testIsEmpty ()
  {
    Assert.assertTrue (LockedContainerHelper.isEmpty (ContainerHelper.newSet (), LOCK));
    Assert.assertFalse (LockedContainerHelper.isEmpty (ContainerHelper.newSet (VALUE1), LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getSize(Collection, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetSize ()
  {
    Assert.assertEquals (LockedContainerHelper.getSize (ContainerHelper.newSet (), LOCK), 0);
    Assert.assertEquals (LockedContainerHelper.getSize (ContainerHelper.newSet (VALUE1), LOCK), 1);
  }

  /**
   * Test for {@link LockedContainerHelper#isEmpty(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testIsEmptyMap ()
  {
    Assert.assertTrue (LockedContainerHelper.isEmpty (ContainerHelper.newMap (), LOCK));
    Assert.assertFalse (LockedContainerHelper.isEmpty (ContainerHelper.newMap (KEY1, VALUE1), LOCK));

  }

  /**
   * Test for {@link LockedContainerHelper#getSize(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetSizeMap ()
  {
    Assert.assertEquals (LockedContainerHelper.getSize (ContainerHelper.newMap (), LOCK), 0);
    Assert.assertEquals (LockedContainerHelper.getSize (ContainerHelper.newMap (KEY1, VALUE1), LOCK), 1);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#clear(Collection, ReentrantReadWriteLock)}
   */
  @Test
  public void testClear ()
  {
    final Collection <String> aObjects = ContainerHelper.newSet ("bla"); //$NON-NLS-1$
    Assert.assertFalse (aObjects.isEmpty ());
    LockedContainerHelper.clear (aObjects, LOCK);
    Assert.assertTrue (aObjects.isEmpty ());
  }

  /**
   * Test for {@link LockedContainerHelper#clear(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testClearMap ()
  {
    final Map <String, String> aObjects = ContainerHelper.newMap ("bla", "foo"); //$NON-NLS-1$ //$NON-NLS-2$
    Assert.assertFalse (aObjects.isEmpty ());
    LockedContainerHelper.clear (aObjects, LOCK);
    Assert.assertTrue (aObjects.isEmpty ());
  }

  @Test
  public void testContains ()
  {
    final Set <String> aElements = ContainerHelper.newSet ("foo", "bar");
    Assert.assertFalse (LockedContainerHelper.contains (aElements, "narf", LOCK));
    Assert.assertTrue (LockedContainerHelper.contains (aElements, "foo", LOCK));
  }

  @Test
  public void testContainsAll ()
  {
    final Set <String> aElements = ContainerHelper.newSet ("foo", "bar", "narf");
    Assert.assertTrue (LockedContainerHelper.containsAll (aElements, ContainerHelper.newSet (), LOCK));
    Assert.assertFalse (LockedContainerHelper.containsAll (aElements, ContainerHelper.newSet ("a", "b"), LOCK));
    Assert.assertFalse (LockedContainerHelper.containsAll (aElements, ContainerHelper.newSet ("a", "foo"), LOCK));
    Assert.assertTrue (LockedContainerHelper.containsAll (aElements, ContainerHelper.newSet ("foo", "narf"), LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getMultiMapEntry(com.phloc.commons.collections.multimap.AbstractMultiHashMapSetBased, Object, ReentrantReadWriteLock)}
   * passing a <code>null</code> map
   */
  @Test (expected = NullPointerException.class)
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testGetMultiMapEntryNull ()
  {
    LockedContainerHelper.getMultiMapEntry (null, KEY1, LOCK);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getMultiMapEntry(com.phloc.commons.collections.multimap.AbstractMultiHashMapSetBased, Object, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetMultiMapEntry ()
  {
    final MultiHashMapHashSetBased <String, String> aMap = new MultiHashMapHashSetBased <String, String> ();
    aMap.putSingle (KEY1, VALUE1);
    {
      final Set <String> aResults = LockedContainerHelper.getMultiMapEntry (aMap, KEY1, LOCK);
      Assert.assertNotNull (aResults);
      Assert.assertFalse (aResults.isEmpty ());
      Assert.assertTrue (aResults.contains (VALUE1));
    }
    {
      final Set <String> aResults = LockedContainerHelper.getMultiMapEntry (aMap, KEY2, LOCK);
      Assert.assertNotNull (aResults);
      Assert.assertTrue (aResults.isEmpty ());
    }
  }

  /**
   * Test for {@link LockedContainerHelper#getSet(Set, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetSet ()
  {
    final Set <String> aSet = ContainerHelper.newSet (VALUE1, VALUE2);
    final Set <String> aNewSet = LockedContainerHelper.getSet (aSet, LOCK);
    Assert.assertFalse (aSet == aNewSet);
    Assert.assertTrue (EqualsUtils.equals (aSet, aNewSet));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getStack(java.util.Stack, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetStack ()
  {
    final Stack <String> aStack = new Stack <String> ();
    aStack.add (VALUE1);
    final Stack <String> aNewStack = LockedContainerHelper.getStack (aStack, LOCK);
    Assert.assertFalse (aStack == aNewStack);
    Assert.assertEquals (aStack.size (), aNewStack.size ());
    Assert.assertEquals (aStack.firstElement (), aNewStack.firstElement ());
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getByKey(String, Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetByKey ()
  {
    Assert.assertEquals (LockedContainerHelper.getByKey (KEY2, MAP, LOCK), VALUE2);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getByKeyCasted(String, Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetByKeyCasted ()
  {
    final Map <String, Object> aMap = ContainerHelper.newMap ();
    aMap.put (KEY1, VALUE1);
    aMap.put (KEY2, VALUE3);
    Assert.assertEquals (LockedContainerHelper.getByKeyCasted (KEY1, aMap, LOCK), VALUE1);
    Assert.assertEquals (LockedContainerHelper.getByKeyCasted (KEY2, aMap, LOCK), VALUE3);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getCollection(java.util.Collection, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetCollection ()
  {
    Assert.assertTrue (EqualsUtils.equals (LockedContainerHelper.getCollection (SET, LOCK), SET));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getList(java.util.Collection, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetList ()
  {
    Assert.assertEquals (LockedContainerHelper.getList (LIST, LOCK), LIST);
  }

  /**
   * Test for {@link LockedContainerHelper#getMap(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetMap ()
  {
    Assert.assertEquals (LockedContainerHelper.getMap (MAP, LOCK), MAP);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getValues(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetValues ()
  {
    final Set <String> aValues = LockedContainerHelper.getValues (MAP, LOCK);
    Assert.assertNotNull (aValues);
    Assert.assertEquals (aValues.size (), 2);
    Assert.assertTrue (aValues.contains (VALUE1));
    Assert.assertTrue (aValues.contains (VALUE2));
  }

  /**
   * Test for {@link LockedContainerHelper#getKeys(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetKeys ()
  {
    final Collection <String> aKeys = LockedContainerHelper.getKeys (MAP, LOCK);
    Assert.assertNotNull (aKeys);
    Assert.assertEquals (aKeys.size (), 2);
    Assert.assertTrue (aKeys.contains (KEY1));
    Assert.assertTrue (aKeys.contains (KEY2));
  }

  /**
   * Test for {@link LockedContainerHelper#getKeys(Map, ReentrantReadWriteLock)}
   */
  @Test
  public void testGetKeysLinked ()
  {
    final Collection <String> aKeys = LockedContainerHelper.getKeys (MAP_ORDERED, LOCK);
    Assert.assertNotNull (aKeys);
    Assert.assertEquals (aKeys.size (), 2);
    Assert.assertTrue (aKeys.contains (KEY1));
    Assert.assertTrue (aKeys.contains (KEY2));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#get(List, int, ReentrantReadWriteLock)}
   * passing a <code>null</code> list
   */
  @Test (expected = NullPointerException.class)
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testGetNull ()
  {
    LockedContainerHelper.get (null, 2, LOCK);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#get(List, int, ReentrantReadWriteLock)}
   */
  @Test
  public void testGet ()
  {
    final List <String> aList = ContainerHelper.newList (VALUE1, VALUE2);
    Assert.assertEquals (LockedContainerHelper.get (aList, 0, LOCK), VALUE1);
    Assert.assertEquals (LockedContainerHelper.get (aList, 1, LOCK), VALUE2);
    Assert.assertNull (LockedContainerHelper.get (aList, 2, LOCK));
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getFirst(Collection, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testGetFirst ()
  {
    Assert.assertEquals (LockedContainerHelper.getFirst (LIST, LOCK), KEY1);
    Assert.assertEquals (LockedContainerHelper.getFirst (null, LOCK), null);
  }

  /**
   * Test for
   * {@link LockedContainerHelper#getFirstValue(Map, ReentrantReadWriteLock)}
   */
  @Test
  @SuppressFBWarnings (value = { "NP_NONNULL_PARAM_VIOLATION" })
  public void testGetFirstValue ()
  {
    Assert.assertEquals (LockedContainerHelper.getFirstValue (MAP_ORDERED, LOCK), VALUE1);
    Assert.assertEquals (LockedContainerHelper.getFirstValue (null, LOCK), null);
  }
}
