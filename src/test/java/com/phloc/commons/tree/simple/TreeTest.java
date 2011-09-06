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
package com.phloc.commons.tree.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link Tree}.
 * 
 * @author philip
 */
public final class TreeTest
{
  private static final class ComparatorTreeItem <T extends Comparable <? super T>> extends
                                                                                   AbstractComparator <ITreeItem <T>>
  {
    ComparatorTreeItem ()
    {}

    @Override
    protected int mainCompare (final ITreeItem <T> aItem1, final ITreeItem <T> aItem2)
    {
      return CompareUtils.nullSafeCompare (aItem1.getData (), aItem2.getData ());
    }
  }

  @Test
  public void testBasic ()
  {
    final Tree <String> t = new Tree <String> ();

    // root item
    final ITreeItem <String> ti = t.getRootItem ().createChildItem ("Hallo");
    assertNotNull (ti);
    assertEquals ("Hallo", ti.getData ());
    assertFalse (ti.hasChildren ());
    assertEquals (0, ti.getChildCount ());
    assertNull (ti.getChildren ());
    assertSame (t.getRootItem (), ti.getParent ());

    // level 1
    final ITreeItem <String> ti1 = ti.createChildItem ("Welt");
    assertNotNull (ti1);
    assertEquals ("Welt", ti1.getData ());
    assertFalse (ti1.hasChildren ());
    assertEquals (0, ti1.getChildCount ());
    assertNull (ti1.getChildren ());
    assertSame (ti, ti1.getParent ());

    assertTrue (ti.hasChildren ());
    assertEquals (1, ti.getChildCount ());
    assertTrue (ti.getChildren ().contains (ti1));
  }

  @Test
  public void testEqualsHashCode ()
  {
    final Tree <String> t = new Tree <String> ();
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (t, new Tree <String> ());
    t.getRootItem ().createChildItem ("data");
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (t, new Tree <String> ());
  }

  @Test
  public void testDelete ()
  {
    final Tree <String> t = new Tree <String> ();

    // root item
    final ITreeItem <String> ti = t.getRootItem ().createChildItem ("Hallo");
    final ITreeItem <String> ti1 = ti.createChildItem ("Welt");

    // item not present
    assertTrue (ti.removeChild (ti).isUnchanged ());

    // delete child first
    assertTrue (ti.removeChild (ti1).isChanged ());
    assertTrue (t.getRootItem ().removeChild (ti).isChanged ());
  }

  @Test
  public void testReorder ()
  {
    final Tree <String> t = new Tree <String> ();

    // root item
    final ITreeItem <String> ti = t.getRootItem ().createChildItem ("Hallo");

    // no items yet....
    assertFalse (ti.hasChildren ());
    ti.reorderChildItems (new ComparatorTreeItem <String> ());
    assertFalse (ti.hasChildren ());

    // add 2 items
    assertNotNull (ti.createChildItem ("Welt2"));
    assertNotNull (ti.createChildItem ("Welt1"));
    assertTrue (ti.hasChildren ());

    // check current order
    assertEquals (2, ti.getChildCount ());
    assertEquals ("Welt2", ti.getChildren ().get (0).getData ());
    assertEquals ("Welt1", ti.getChildren ().get (1).getData ());

    // reorder
    ti.reorderChildItems (new ComparatorTreeItem <String> ());

    // check new order
    assertEquals (2, ti.getChildCount ());
    assertEquals ("Welt1", ti.getChildren ().get (0).getData ());
    assertEquals ("Welt2", ti.getChildren ().get (1).getData ());
  }

  @Test
  public void testChangeParent ()
  {
    final Tree <String> t1 = new Tree <String> ();
    final ITreeItem <String> t1i1 = t1.getRootItem ().createChildItem ("Hallo");
    final ITreeItem <String> t1i2 = t1i1.createChildItem ("Hallo");
    final Tree <String> t2 = new Tree <String> ();
    final ITreeItem <String> t2i1 = t2.getRootItem ().createChildItem ("Hallo");
    final ITreeItem <String> t2i2 = t2i1.createChildItem ("Hallo");

    // cannot work
    assertTrue (t2i1.changeParent (t2i1).isFailure ());
    assertTrue (t2i1.changeParent (t2i2).isFailure ());

    // preconditions
    assertFalse (t1i2.hasChildren ());
    assertTrue (t2i1.hasChildren ());

    // perform
    assertTrue (t2i1.getChildren ().contains (t2i2));
    assertTrue (t2i2.changeParent (t1i2).isSuccess ());
    assertTrue (t1i2.getChildren ().contains (t2i2));
    assertSame (t1i2, t2i2.getParent ());
    assertFalse (t2i1.getChildren ().contains (t2i2));

    // postconditions
    assertTrue (t1i2.hasChildren ());
    assertFalse (t2i1.hasChildren ());
  }

  @Test
  public void testCtor ()
  {
    final Tree <String> t = new Tree <String> ();
    assertNotNull (t.getRootItem ());
    assertTrue (t.toString ().length () > 0);
  }
}
