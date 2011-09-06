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
package com.phloc.commons.tree.sort;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.compare.ComparatorString;
import com.phloc.commons.tree.simple.ITreeItem;
import com.phloc.commons.tree.simple.Tree;
import com.phloc.commons.tree.withid.ITreeItemWithID;
import com.phloc.commons.tree.withid.TreeWithID;

/**
 * Test class for class {@link TreeSorter}
 * 
 * @author philip
 */
public final class TreeSorterTest
{
  @Test
  public void testTree ()
  {
    final Tree <String> aTree = new Tree <String> ();
    assertNotNull (aTree.getRootItem ());
    final ITreeItem <String> i1 = aTree.getRootItem ().createChildItem ("Windows");
    i1.createChildItem ("sxs");
    i1.createChildItem ("temp");
    i1.createChildItem ("System32");
    final ITreeItem <String> i2 = aTree.getRootItem ().createChildItem ("Program Files");
    i2.createChildItem ("Eclipse");
    i2.createChildItem ("Apache Software Foundation");

    // Sort all items by String
    TreeSorter.sort (aTree, new ComparatorString ());

    assertEquals (2, aTree.getRootItem ().getChildCount ());
    final List <? extends ITreeItem <String>> aChildren = aTree.getRootItem ().getChildren ();
    assertSame (i2, aChildren.get (0));
    assertSame (i1, aChildren.get (1));
    // Test Apache (children must also be sorted)
    assertEquals (2, i2.getChildCount ());
    assertEquals ("Apache Software Foundation", i2.getChildAtIndex (0).getData ());
    assertEquals ("Eclipse", i2.getChildAtIndex (1).getData ());
    // Test Windows
    assertEquals (3, i1.getChildCount ());
    assertEquals ("sxs", i1.getChildAtIndex (0).getData ());
    assertEquals ("System32", i1.getChildAtIndex (1).getData ());
    assertEquals ("temp", i1.getChildAtIndex (2).getData ());
  }

  @Test
  public void testTreeWithIDValue ()
  {
    final TreeWithID <String, String> aTree = new TreeWithID <String, String> ();
    assertNotNull (aTree.getRootItem ());
    final ITreeItemWithID <String, String> i1 = aTree.getRootItem ().createChildItem ("r1", "Windows");
    i1.createChildItem ("w1", "sxs");
    i1.createChildItem ("w2", "temp");
    i1.createChildItem ("w3", "System32");
    final ITreeItemWithID <String, String> i2 = aTree.getRootItem ().createChildItem ("r2", "Program Files");
    i2.createChildItem ("p1", "Eclipse");
    i2.createChildItem ("p2", "Apache Software Foundation");

    // Sort all items by value
    TreeSorter.sortByValues (aTree, new ComparatorString ());

    assertEquals (2, aTree.getRootItem ().getChildCount ());
    List <? extends ITreeItemWithID <String, String>> aChildren = aTree.getRootItem ().getChildren ();
    assertSame (i2, aChildren.get (0));
    assertSame (i1, aChildren.get (1));
    // Test Apache (children must also be sorted)
    assertEquals (2, i2.getChildCount ());
    assertEquals ("Apache Software Foundation", i2.getChildAtIndex (0).getData ());
    assertEquals ("Eclipse", i2.getChildAtIndex (1).getData ());
    // Test Windows
    assertEquals (3, i1.getChildCount ());
    assertEquals ("sxs", i1.getChildAtIndex (0).getData ());
    assertEquals ("System32", i1.getChildAtIndex (1).getData ());
    assertEquals ("temp", i1.getChildAtIndex (2).getData ());

    // Sort all items by keys
    TreeSorter.sortByKeys (aTree, new ComparatorString ());

    assertEquals (2, aTree.getRootItem ().getChildCount ());
    aChildren = aTree.getRootItem ().getChildren ();
    assertSame (i1, aChildren.get (0));
    assertSame (i2, aChildren.get (1));
    // Test Windows
    assertEquals (3, i1.getChildCount ());
    assertEquals ("sxs", i1.getChildAtIndex (0).getData ());
    assertEquals ("temp", i1.getChildAtIndex (1).getData ());
    assertEquals ("System32", i1.getChildAtIndex (2).getData ());
    // Test Apache (children must also be sorted)
    assertEquals (2, i2.getChildCount ());
    assertEquals ("Eclipse", i2.getChildAtIndex (0).getData ());
    assertEquals ("Apache Software Foundation", i2.getChildAtIndex (1).getData ());
  }
}
