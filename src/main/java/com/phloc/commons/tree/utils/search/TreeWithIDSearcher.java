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
package com.phloc.commons.tree.utils.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.tree.IBasicTree;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * A utility class that helps searching items within trees.
 * 
 * @author philip
 */
@Immutable
public final class TreeWithIDSearcher
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TreeWithIDSearcher s_aInstance = new TreeWithIDSearcher ();

  private TreeWithIDSearcher ()
  {}

  /**
   * Fill all items with the same ID by linearly scanning of the tree.
   * 
   * @param <KEYTYPE>
   *        tree ID type
   * @param <VALUETYPE>
   *        tree data type
   * @param <ITEMTYPE>
   *        tree item type
   * @param aTree
   *        The tree to search. May not be <code>null</code>.
   * @param aSearchID
   *        The ID to search. May not be <code>null</code>.
   * @return A non-<code>null</code> list with all matching items.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> List <ITEMTYPE> findAllItemsWithIDRecursive (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                                                   @Nullable final KEYTYPE aSearchID)
  {
    return findAllItemsWithIDRecursive (aTree.getRootItem (), aSearchID);
  }

  /**
   * Fill all items with the same ID by linearly scanning the tree.
   * 
   * @param <KEYTYPE>
   *        tree ID type
   * @param <VALUETYPE>
   *        tree data type
   * @param <ITEMTYPE>
   *        tree item type
   * @param aTreeItem
   *        The tree item to search. May not be <code>null</code>.
   * @param aSearchID
   *        The ID to search. May not be <code>null</code>.
   * @return A non-<code>null</code> list with all matching items.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> List <ITEMTYPE> findAllItemsWithIDRecursive (@Nonnull final ITEMTYPE aTreeItem,
                                                                                                                                                   @Nullable final KEYTYPE aSearchID)
  {
    final List <ITEMTYPE> aRetList = new ArrayList <ITEMTYPE> ();
    TreeWalker.walkSubTree (aTreeItem, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (@Nonnull final ITEMTYPE aItem)
      {
        if (aItem.getID ().equals (aSearchID))
          aRetList.add (aItem);
      }
    });
    return aRetList;
  }
}
