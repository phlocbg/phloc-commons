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
package com.phloc.commons.tree.utils.sort;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.tree.IBasicTree;
import com.phloc.commons.tree.simple.ITree;
import com.phloc.commons.tree.simple.ITreeItem;
import com.phloc.commons.tree.utils.walk.TreeWalker;

/**
 * Sort {@link ITree} instances recursively.
 * 
 * @author philip
 */
@Immutable
public final class TreeSorter
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TreeSorter s_aInstance = new TreeSorter ();

  private TreeSorter ()
  {}

  /**
   * Sort each level of the passed tree with the specified comparator.
   * 
   * @param aTree
   *        The tree to be sorted.
   * @param aComparator
   *        The comparator to be used for sorting the tree items on each level.
   */
  public static <VALUETYPE, ITEMTYPE extends ITreeItem <VALUETYPE, ITEMTYPE>> void sort (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                         @Nonnull final Comparator <? super VALUETYPE> aComparator)
  {
    final ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> (aComparator);

    // sort root manually
    aTree.getRootItem ().reorderChildItems (aRealComp);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (@Nonnull final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildItems (aRealComp);
      }
    });
  }

  /**
   * Sort each level of the passed tree with the specified comparator. This
   * method assumes that the values in the tree item implement the
   * {@link Comparable} interface.
   * 
   * @param aTree
   *        The tree to be sorted.
   */
  public static <VALUETYPE extends Comparable <? super VALUETYPE>, ITEMTYPE extends ITreeItem <VALUETYPE, ITEMTYPE>> void sort (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree)
  {
    final ComparatorTreeItemValueComparable <VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemValueComparable <VALUETYPE, ITEMTYPE> ();

    // sort root manually
    aTree.getRootItem ().reorderChildItems (aRealComp);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (@Nonnull final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildItems (aRealComp);
      }
    });
  }
}
