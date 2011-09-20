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
package com.phloc.commons.tree.withid.utils;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.hierarchy.IHierarchyWalkerCallback;
import com.phloc.commons.parent.IChildrenProvider;
import com.phloc.commons.parent.impl.ChildrenProviderHasChildren;
import com.phloc.commons.tree.IBasicTree;
import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * Statically walk tree with ID
 * 
 * @author philip
 */
@Immutable
public final class TreeWalkerWithID
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TreeWalkerWithID s_aInstance = new TreeWalkerWithID ();

  private TreeWalkerWithID ()
  {}

  private static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void _walkTree (@Nonnull final ITEMTYPE aTreeItem,
                                                                                                                       @Nonnull final IChildrenProvider <ITEMTYPE> aChildrenResolver,
                                                                                                                       @Nonnull final IHierarchyWalkerCallback <? super ITEMTYPE> aCallback)
  {
    aCallback.onItemBeforeChildren (aTreeItem);
    if (aChildrenResolver.hasChildren (aTreeItem))
      for (final ITEMTYPE aChildItem : aChildrenResolver.getChildren (aTreeItem))
      {
        aCallback.onLevelDown ();
        // recursive call
        _walkTree (aChildItem, aChildrenResolver, aCallback);
        aCallback.onLevelUp ();
      }
    aCallback.onItemAfterChildren (aTreeItem);
  }

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void walkTree (@Nonnull final IBasicTree <ITEMTYPE> aTree,
                                                                                                                     @Nonnull final IHierarchyWalkerCallback <? super ITEMTYPE> aCallback)
  {
    walkTree (aTree, new ChildrenProviderHasChildren <ITEMTYPE> (), aCallback);
  }

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void walkTree (@Nonnull final IBasicTree <ITEMTYPE> aTree,
                                                                                                                     @Nonnull final IChildrenProvider <ITEMTYPE> aChildrenResolver,
                                                                                                                     @Nonnull final IHierarchyWalkerCallback <? super ITEMTYPE> aCallback)
  {
    if (aTree == null)
      throw new NullPointerException ("tree");

    walkSubTree (aTree.getRootItem (), aChildrenResolver, aCallback);
  }

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void walkSubTree (@Nonnull final ITEMTYPE aTreeItem,
                                                                                                                        @Nonnull final IHierarchyWalkerCallback <? super ITEMTYPE> aCallback)
  {
    walkSubTree (aTreeItem, new ChildrenProviderHasChildren <ITEMTYPE> (), aCallback);
  }

  public static <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> void walkSubTree (@Nonnull final ITEMTYPE aTreeItem,
                                                                                                                        @Nonnull final IChildrenProvider <ITEMTYPE> aChildrenResolver,
                                                                                                                        @Nonnull final IHierarchyWalkerCallback <? super ITEMTYPE> aCallback)
  {
    if (aTreeItem == null)
      throw new NullPointerException ("treeItem");
    if (aCallback == null)
      throw new NullPointerException ("callback");

    aCallback.begin ();
    try
    {
      if (aChildrenResolver.hasChildren (aTreeItem))
        for (final ITEMTYPE aChildItem : aChildrenResolver.getChildren (aTreeItem))
          _walkTree (aChildItem, aChildrenResolver, aCallback);
    }
    finally
    {
      aCallback.end ();
    }
  }
}
