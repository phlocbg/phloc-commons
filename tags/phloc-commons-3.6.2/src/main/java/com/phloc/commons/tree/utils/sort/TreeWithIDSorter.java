/**
 * Copyright (C) 2006-2012 phloc systems
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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.tree.IBasicTree;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.ITreeItemWithID;
import com.phloc.commons.tree.withid.ITreeWithID;

/**
 * Sort {@link ITreeWithID} instances recursively - either by ID or by value
 * 
 * @author philip
 */
@Immutable
public final class TreeWithIDSorter
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TreeWithIDSorter s_aInstance = new TreeWithIDSorter ();

  private TreeWithIDSorter ()
  {}

  private static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void _sort (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                   @Nonnull final Comparator <? super ITEMTYPE> aComparator)
  {
    if (aTree == null)
      throw new NullPointerException ("tree");
    if (aComparator == null)
      throw new NullPointerException ("comparator");

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aComparator);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (@Nullable final ITEMTYPE aTreeItem)
      {
        if (aTreeItem != null)
          aTreeItem.reorderChildrenByItems (aComparator);
      }
    });
  }

  /**
   * Sort each level of the passed tree on the ID with the specified comparator.
   * 
   * @param aTree
   *        The tree to be sorted.
   * @param aKeyComparator
   *        The comparator to be used for sorting the tree item keys on each
   *        level.
   */
  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByID (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                     @Nonnull final Comparator <? super KEYTYPE> aKeyComparator)
  {
    final ComparatorTreeItemID <KEYTYPE, VALUETYPE, ITEMTYPE> aItemComp = new ComparatorTreeItemID <KEYTYPE, VALUETYPE, ITEMTYPE> (aKeyComparator);
    _sort (aTree, aItemComp);
  }

  /**
   * Sort each level of the passed tree on the ID with the specified comparator.
   * This method assumes that the IDs in the tree item implement the
   * {@link Comparable} interface.
   * 
   * @param aTree
   *        The tree to be sorted.
   */
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByID (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree)
  {
    final ComparatorTreeItemIDComparable <KEYTYPE, VALUETYPE, ITEMTYPE> aItemComp = new ComparatorTreeItemIDComparable <KEYTYPE, VALUETYPE, ITEMTYPE> ();
    _sort (aTree, aItemComp);
  }

  /**
   * Sort each level of the passed tree on the value with the specified
   * comparator.
   * 
   * @param aTree
   *        The tree to be sorted.
   * @param aValueComparator
   *        The comparator to be used for sorting the tree item keys on each
   *        level.
   */
  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByValue (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                        @Nonnull final Comparator <? super VALUETYPE> aValueComparator)
  {
    final ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> aItemComp = new ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> (aValueComparator);
    _sort (aTree, aItemComp);
  }

  /**
   * Sort each level of the passed tree on the value with the specified
   * comparator. This method assumes that the values in the tree item implement
   * the {@link Comparable} interface.
   * 
   * @param aTree
   *        The tree to be sorted.
   */
  public static <KEYTYPE, VALUETYPE extends Comparable <? super VALUETYPE>, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByValue (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree)
  {
    final ComparatorTreeItemValueComparable <VALUETYPE, ITEMTYPE> aItemComp = new ComparatorTreeItemValueComparable <VALUETYPE, ITEMTYPE> ();
    _sort (aTree, aItemComp);
  }
}
