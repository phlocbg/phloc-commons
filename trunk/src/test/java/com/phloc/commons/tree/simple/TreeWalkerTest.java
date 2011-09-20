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
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.mutable.MutableInt;

/**
 * Test class for class {@link TreeWalker}.
 * 
 * @author philip
 */
public final class TreeWalkerTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (TreeWalkerTest.class);

  private static void _fillTree (final DefaultTreeItem <String> aParentItem, final int nLevels, final int nItemsPerLevel)
  {
    if (nLevels > 0)
      for (int i = 0; i < nItemsPerLevel; ++i)
      {
        final DefaultTreeItem <String> aChild = aParentItem.createChildItem ("any");
        _fillTree (aChild, nLevels - 1, nItemsPerLevel);
        assertTrue (aChild.isSameOrChildOf (aParentItem));
        assertFalse (aParentItem.isSameOrChildOf (aChild));
      }
  }

  private static DefaultTree <String> _createTree (final int nLevels, final int nItemsPerLevel)
  {
    final DefaultTree <String> t = new DefaultTree <String> ();
    _fillTree (t.getRootItem (), nLevels, nItemsPerLevel);
    return t;
  }

  // = sum[exp:1-y][nBase^exp]
  private static long _powSum (final int nBase, final int y)
  {
    long ret = 0;
    for (int i = 1; i <= y; ++i)
      ret += (long) Math.pow (nBase, i);
    return ret;
  }

  @Test
  public void testTreeWalk ()
  {
    final int nItemsPerLevel = 5;
    // 8 levels: fast
    // 9 levels: ok
    // 10 levels: out-of-memory
    for (int nLevel = 1; nLevel < 9; ++nLevel)
    {
      // Item count is e.g.: 5^4 + 5^3 + 5^2 + 5^1
      // Where 5 is the items-per-level and 4 is the level-count
      final long nExpected = _powSum (nItemsPerLevel, nLevel);
      s_aLogger.info ("Creating tree with " + nExpected + " items");

      // count at before children
      final MutableInt mi = new MutableInt ();
      TreeWalker.walkTree (_createTree (nLevel, nItemsPerLevel),
                           new DefaultHierarchyWalkerCallback <DefaultTreeItem <String>> ()
                           {
                             @Override
                             public void onItemBeforeChildren (final DefaultTreeItem <String> aItem)
                             {
                               if (getLevel () < 0)
                                 throw new IllegalStateException ();
                               mi.inc ();
                             }
                           });
      assertEquals (nExpected, mi.intValue ());

      // count at before children
      mi.set (0);
      TreeWalker.walkSubTree (_createTree (nLevel, nItemsPerLevel).getRootItem (),
                              new DefaultHierarchyWalkerCallback <DefaultTreeItem <String>> ()
                              {
                                @Override
                                public void onItemBeforeChildren (final DefaultTreeItem <String> aItem)
                                {
                                  if (getLevel () < 0)
                                    throw new IllegalStateException ();
                                  mi.inc ();
                                }
                              });
      assertEquals (nExpected, mi.intValue ());

      // count at after children
      mi.set (0);
      TreeWalker.walkTree (_createTree (nLevel, nItemsPerLevel),
                           new DefaultHierarchyWalkerCallback <DefaultTreeItem <String>> ()
                           {
                             @Override
                             public void onItemAfterChildren (final DefaultTreeItem <String> aItem)
                             {
                               if (getLevel () < 0)
                                 throw new IllegalStateException ();
                               mi.inc ();
                             }
                           });
      assertEquals (nExpected, mi.intValue ());

      // count at after children
      mi.set (0);
      TreeWalker.walkSubTree (_createTree (nLevel, nItemsPerLevel).getRootItem (),
                              new DefaultHierarchyWalkerCallback <DefaultTreeItem <String>> ()
                              {
                                @Override
                                public void onItemAfterChildren (final DefaultTreeItem <String> aItem)
                                {
                                  if (getLevel () < 0)
                                    throw new IllegalStateException ();
                                  mi.inc ();
                                }
                              });
      assertEquals (nExpected, mi.intValue ());
    }
  }
}
