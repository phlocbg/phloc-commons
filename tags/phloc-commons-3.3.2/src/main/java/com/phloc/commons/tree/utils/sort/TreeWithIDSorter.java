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

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByValue (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                        @Nonnull final Comparator <? super VALUETYPE> aComparator)
  {
    final ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> (aComparator);

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aRealComp);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildrenByItems (aRealComp);
      }
    });
  }

  public static <KEYTYPE, VALUETYPE extends Comparable <? super VALUETYPE>, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByValue (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree)
  {
    final ComparatorTreeItemValueComparable <VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemValueComparable <VALUETYPE, ITEMTYPE> ();

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aRealComp);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildrenByItems (aRealComp);
      }
    });
  }

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByID (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree,
                                                                                                                     @Nonnull final Comparator <? super KEYTYPE> aComparator)
  {
    final ComparatorTreeItemID <KEYTYPE, VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemID <KEYTYPE, VALUETYPE, ITEMTYPE> (aComparator);

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aRealComp);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildrenByItems (aRealComp);
      }
    });
  }

  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByID (@Nonnull final IBasicTree <VALUETYPE, ITEMTYPE> aTree)
  {
    final ComparatorTreeItemIDComparable <KEYTYPE, VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemIDComparable <KEYTYPE, VALUETYPE, ITEMTYPE> ();

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aRealComp);

    // and now start iterating
    TreeWalker.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildrenByItems (aRealComp);
      }
    });
  }
}
