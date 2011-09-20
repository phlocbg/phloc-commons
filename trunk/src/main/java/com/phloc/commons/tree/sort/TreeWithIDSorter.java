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

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.tree.IBasicTree;
import com.phloc.commons.tree.withid.ITreeItemWithID;
import com.phloc.commons.tree.withid.utils.TreeWalkerWithID;

/**
 * Sort tree items by using a specified converter.
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

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByValues (@Nonnull final IBasicTree <ITEMTYPE> aTree,
                                                                                                                         @Nonnull final Comparator <? super VALUETYPE> aComparator)
  {
    final ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemValue <VALUETYPE, ITEMTYPE> (aComparator);

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aRealComp);

    // and now start iterating
    TreeWalkerWithID.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildrenByItems (aRealComp);
      }
    });
  }

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void sortByKeys (@Nonnull final IBasicTree <ITEMTYPE> aTree,
                                                                                                                       @Nonnull final Comparator <? super KEYTYPE> aComparator)
  {
    final ComparatorTreeItemKey <KEYTYPE, VALUETYPE, ITEMTYPE> aRealComp = new ComparatorTreeItemKey <KEYTYPE, VALUETYPE, ITEMTYPE> (aComparator);

    // sort root manually
    aTree.getRootItem ().reorderChildrenByItems (aRealComp);

    // and now start iterating
    TreeWalkerWithID.walkTree (aTree, new DefaultHierarchyWalkerCallback <ITEMTYPE> ()
    {
      @Override
      public void onItemBeforeChildren (final ITEMTYPE aTreeItem)
      {
        aTreeItem.reorderChildrenByItems (aRealComp);
      }
    });
  }
}
