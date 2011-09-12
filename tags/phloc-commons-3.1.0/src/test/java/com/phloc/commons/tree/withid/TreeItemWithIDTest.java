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
package com.phloc.commons.tree.withid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.tree.sort.ComparatorTreeItemKeyComparable;
import com.phloc.commons.tree.sort.ComparatorTreeItemValueComparable;

/**
 * Test class for class {@link TreeItemWithID}.
 * 
 * @author philip
 */
public final class TreeItemWithIDTest
{
  @Test
  public void testBasic ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();

    // tree root
    assertNotNull (t.getRootItem ());
    assertTrue (t.getRootItem ().isRootItem ());
    assertTrue (t.getRootItem ().isSameOrChildOf (t.getRootItem ()));

    // root item
    final ITreeItemWithID <String, String> ti = t.getRootItem ().createChildItem ("root", "Hallo");
    assertNotNull (ti);
    assertEquals ("root", ti.getID ());
    assertEquals ("Hallo", ti.getData ());
    assertFalse (ti.hasChildren ());
    assertEquals (0, ti.getChildCount ());
    assertTrue (ContainerHelper.isEmpty (ti.getChildren ()));
    assertSame (t.getRootItem (), ti.getParent ());
    assertFalse (ti.isRootItem ());
    assertTrue (ti.isSameOrChildOf (t.getRootItem ()));
    assertFalse (t.getRootItem ().isSameOrChildOf (ti));

    // level 1
    final ITreeItemWithID <String, String> ti1 = ti.createChildItem ("root1", "Welt");
    assertNotNull (ti1);
    assertEquals ("root1", ti1.getID ());
    assertEquals ("Welt", ti1.getData ());
    assertFalse (ti1.hasChildren ());
    assertEquals (0, ti1.getChildCount ());
    assertTrue (ContainerHelper.isEmpty (ti1.getChildren ()));
    assertSame (ti, ti1.getParent ());

    assertTrue (ti.hasChildren ());
    assertEquals (1, ti.getChildCount ());
    assertTrue (ti.getChildren ().contains (ti1));

    // test get child of ID
    assertNotNull (ti.getChildItemOfDataID ("root1"));
    assertNull (ti.getChildItemOfDataID ("any"));
    assertNull (ti.getChildItemOfDataID (""));
    assertNull (ti.getChildItemOfDataID (null));
    assertEquals ("Welt", ti.getChildItemOfDataID ("root1").getData ());
  }

  @Test
  public void testAdd ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();

    // root item
    final ITreeItemWithID <String, String> ti = t.getRootItem ().createChildItem ("root", "Hallo");
    assertNotNull (ti);

    // level 1
    final ITreeItemWithID <String, String> ti1 = ti.createChildItem ("root1", "Welt");
    assertNotNull (ti1);
    assertEquals ("Welt", ti.getChildItemOfDataID ("root1").getData ());

    // do not overwrite
    assertNull (ti.createChildItem ("root1", "Hudriwudri", false));
    assertEquals ("Welt", ti.getChildItemOfDataID ("root1").getData ());

    // overwrite
    assertNotNull (ti.createChildItem ("root1", "Hudriwudri", true));
    assertEquals ("Hudriwudri", ti.getChildItemOfDataID ("root1").getData ());

    try
    {
      // null ID not allowed
      ti.createChildItem (null, "Data!!");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testDelete ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();
    // No children present
    assertFalse (t.getRootItem ().removeChild ("root1").isChanged ());

    // root item
    final ITreeItemWithID <String, String> ti = t.getRootItem ().createChildItem ("root", "Hallo");
    assertNotNull (ti.createChildItem ("root1", "Welt"));

    // item not present
    assertTrue (ti.removeChild ("root").isUnchanged ());

    // delete child first
    assertTrue (ti.removeChild ("root1").isChanged ());
    assertTrue (t.getRootItem ().removeChild ("root").isChanged ());
    assertFalse (t.getRootItem ().hasChildren ());

    t.getRootItem ().createChildItem ("x2", "y2");
    assertTrue (t.getRootItem ().hasChildren ());
    assertTrue (t.getRootItem ().removeAllChildren ().isChanged ());
    assertFalse (t.getRootItem ().hasChildren ());
    assertFalse (t.getRootItem ().removeAllChildren ().isChanged ());
  }

  @Test
  public void testReorderByItem ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();

    // root item
    final ITreeItemWithID <String, String> ti = t.getRootItem ().createChildItem ("root", "Hallo");

    // no items yet....
    assertFalse (ti.hasChildren ());
    ti.reorderChildrenByItems (new ComparatorTreeItemValueComparable <String, ITreeItemWithID <String, String>> ());
    assertFalse (ti.hasChildren ());

    // add 2 items
    assertNotNull (ti.createChildItem ("id2", "Welt2"));
    assertNotNull (ti.createChildItem ("id1", "Welt1"));
    assertTrue (ti.hasChildren ());

    // check current order
    assertEquals (2, ti.getChildCount ());
    assertEquals ("id2", ti.getChildren ().get (0).getID ());
    assertEquals ("Welt2", ti.getChildren ().get (0).getData ());
    assertEquals ("id1", ti.getChildren ().get (1).getID ());
    assertEquals ("Welt1", ti.getChildren ().get (1).getData ());

    // reorder
    ti.reorderChildrenByItems (new ComparatorTreeItemValueComparable <String, ITreeItemWithID <String, String>> ());

    // check new order
    assertEquals (2, ti.getChildCount ());
    assertEquals ("id1", ti.getChildren ().get (0).getID ());
    assertEquals ("Welt1", ti.getChildren ().get (0).getData ());
    assertEquals ("id2", ti.getChildren ().get (1).getID ());
    assertEquals ("Welt2", ti.getChildren ().get (1).getData ());
  }

  @Test
  public void testReorderByKey ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();

    // root item
    final ITreeItemWithID <String, String> ti = t.getRootItem ().createChildItem ("root", "Hallo");

    // no items yet....
    assertFalse (ti.hasChildren ());

    // add 2 items
    assertNotNull (ti.createChildItem ("id2", "Welt2"));
    assertNotNull (ti.createChildItem ("id1", "Welt1"));
    assertTrue (ti.hasChildren ());

    // check current order
    assertEquals (2, ti.getChildCount ());
    assertEquals ("id2", ti.getChildren ().get (0).getID ());
    assertEquals ("Welt2", ti.getChildren ().get (0).getData ());
    assertEquals ("id1", ti.getChildren ().get (1).getID ());
    assertEquals ("Welt1", ti.getChildren ().get (1).getData ());

    // reorder
    ti.reorderChildrenByItems (new ComparatorTreeItemKeyComparable <String, String, ITreeItemWithID <String, String>> ());

    // check new order
    assertEquals (2, ti.getChildCount ());
    assertEquals ("id1", ti.getChildren ().get (0).getID ());
    assertEquals ("Welt1", ti.getChildren ().get (0).getData ());
    assertEquals ("id2", ti.getChildren ().get (1).getID ());
    assertEquals ("Welt2", ti.getChildren ().get (1).getData ());
  }

  @Test
  public void testCtor ()
  {
    final TreeWithID <String, Object> t = new TreeWithID <String, Object> ();
    assertNotNull (t.getRootItem ());
    assertTrue (t.toString ().length () > 0);

    try
    {
      new TreeItemWithID <Object, Object> (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new TreeItemWithID <Object, Object> (null, "dataid");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testStdMethods ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();
    final TreeWithID <String, String> t2 = new TreeWithID <String, String> ();
    t2.getRootItem ().createChildItem ("dataid", "Data");

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (t.getRootItem (),
                                                                    new TreeWithID <String, String> ().getRootItem ());
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (t.getRootItem (), t2.getRootItem ());
  }

  @Test
  public void testInvalid ()
  {
    final TreeWithID <String, String> t = new TreeWithID <String, String> ();

    try
    {
      t.getRootItem ().getChildAtIndex (0);
      fail ();
    }
    catch (final IndexOutOfBoundsException ex)
    {}
    try
    {
      t.getRootItem ().isSameOrChildOf (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      t.getRootItem ().changeParent (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      t.getRootItem ().removeChild (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testChangeParent ()
  {
    final ITreeItemWithID <String, String> root = new TreeWithID <String, String> ().getRootItem ();
    final ITreeItemWithID <String, String> root2 = root.createChildItem ("root", "root");
    final ITreeItemWithID <String, String> child1 = root2.createChildItem ("child1", "child1");

    // same parent
    assertTrue (child1.changeParent (root2).isSuccess ());

    // Cannot invert hierarchy
    assertTrue (root2.changeParent (child1).isFailure ());

    // success
    assertTrue (child1.changeParent (root).isSuccess ());

    assertEquals (2, root.getChildCount ());
    assertEquals (0, root2.getChildCount ());
    assertEquals (0, child1.getChildCount ());

    assertNull (root.getParent ());
    assertSame (root, root2.getParent ());
    assertSame (root, child1.getParent ());
  }
}
