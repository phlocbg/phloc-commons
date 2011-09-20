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
package com.phloc.commons.tree.utils.walk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerDynamicCallback;
import com.phloc.commons.hierarchy.EHierarchyCallbackReturn;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.tree.utils.walk.TreeWalkerDynamicWithID;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.DefaultTreeWithID;

/**
 * Test class for class {@link TreeWalkerDynamicWithID}.
 * 
 * @author philip
 */
public final class TreeWalkerDynamicWithIDTest
{
  public static final class MockTreeWalkerDynamicCallback extends
                                                         DefaultHierarchyWalkerDynamicCallback <DefaultTreeItemWithID <String, Object>>
  {
    private final MutableInt m_aMI;

    public MockTreeWalkerDynamicCallback (final MutableInt mi)
    {
      m_aMI = mi;
    }

    @Override
    public EHierarchyCallbackReturn onItemBeforeChildren (final DefaultTreeItemWithID <String, Object> aItem)
    {
      if (getLevel () < 0)
        throw new IllegalStateException ();
      m_aMI.inc ();
      return EHierarchyCallbackReturn.CONTINUE;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (TreeWalkerDynamicWithIDTest.class);

  private static void _fillTreeWithID (final DefaultTreeItemWithID <String, Object> aParentItem,
                                       final int nLevels,
                                       final int nItemsPerLevel)
  {
    if (nLevels > 0)
      for (int i = 0; i < nItemsPerLevel; ++i)
      {
        // Ensure that each item has its own unique ID within the subtree!
        final DefaultTreeItemWithID <String, Object> aChild = aParentItem.createChildItem (Integer.toString (i),
                                                                                           Double.valueOf (Double.POSITIVE_INFINITY));
        _fillTreeWithID (aChild, nLevels - 1, nItemsPerLevel);
        assertTrue (aChild.isSameOrChildOf (aParentItem));
        assertFalse (aParentItem.isSameOrChildOf (aChild));
      }
  }

  private static DefaultTreeWithID <String, Object> _createTreeWithID (final int nLevels, final int nItemsPerLevel)
  {
    final DefaultTreeWithID <String, Object> t = new DefaultTreeWithID <String, Object> ();
    _fillTreeWithID (t.getRootItem (), nLevels, nItemsPerLevel);
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
  public void testTreeWithIDWalk ()
  {
    final int nItemsPerLevel = 5;
    // 7 levels: fast
    // 8 levels: ok
    // 9 levels: out-of-memory
    for (int nLevel = 1; nLevel < 8; ++nLevel)
    {
      // Item count is e.g.: 5^4 + 5^3 + 5^2 + 5^1
      // Where 5 is the items-per-level and 4 is the level-count
      final long nExpected = _powSum (nItemsPerLevel, nLevel);
      s_aLogger.info ("Creating tree with " + nExpected + " items");

      // count at before children
      final MutableInt mi = new MutableInt ();
      TreeWalkerDynamicWithID.walkTree (_createTreeWithID (nLevel, nItemsPerLevel),
                                        new MockTreeWalkerDynamicCallback (mi));
      assertEquals (nExpected, mi.intValue ());

      // count at before children
      mi.set (0);
      TreeWalkerDynamicWithID.walkSubTree (_createTreeWithID (nLevel, nItemsPerLevel).getRootItem (),
                                           new MockTreeWalkerDynamicCallback (mi));
      assertEquals (nExpected, mi.intValue ());

      // count at after children
      mi.set (0);
      TreeWalkerDynamicWithID.walkTree (_createTreeWithID (nLevel, nItemsPerLevel),
                                        new MockTreeWalkerDynamicCallback (mi));
      assertEquals (nExpected, mi.intValue ());

      // count at after children
      mi.set (0);
      TreeWalkerDynamicWithID.walkSubTree (_createTreeWithID (nLevel, nItemsPerLevel).getRootItem (),
                                           new MockTreeWalkerDynamicCallback (mi));
      assertEquals (nExpected, mi.intValue ());

      try
      {
        TreeWalkerDynamicWithID.walkTree ((DefaultTreeWithID <String, Object>) null,
                                          new MockTreeWalkerDynamicCallback (mi));
        fail ();
      }
      catch (final NullPointerException ex)
      {}
      try
      {
        TreeWalkerDynamicWithID.walkSubTree ((DefaultTreeItemWithID <String, Object>) null,
                                             new MockTreeWalkerDynamicCallback (mi));
        fail ();
      }
      catch (final NullPointerException ex)
      {}
      try
      {
        TreeWalkerDynamicWithID.walkSubTree (_createTreeWithID (nLevel, nItemsPerLevel).getRootItem (), null);
        fail ();
      }
      catch (final NullPointerException ex)
      {}
    }
  }
}
