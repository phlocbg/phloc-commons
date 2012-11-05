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
package com.phloc.commons.tree.utils.build;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.parent.IChildrenProvider;
import com.phloc.commons.parent.IHasParent;
import com.phloc.commons.parent.IParentProvider;
import com.phloc.commons.parent.impl.ParentProviderHasParent;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.DefaultTreeWithID;

/**
 * Utility classes for building a tree from flat collections.
 * 
 * @author philip
 */
@Immutable
public final class TreeWithIDBuilder
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TreeWithIDBuilder s_aInstance = new TreeWithIDBuilder ();

  private TreeWithIDBuilder ()
  {}

  @Nonnull
  private static <KEYTYPE, VALUETYPE extends IHasID <KEYTYPE>> DefaultTreeWithID <KEYTYPE, VALUETYPE> _buildTree (@Nonnull final List <VALUETYPE> aOpen,
                                                                                                                  @Nonnull final IParentProvider <VALUETYPE> aParentResolver)
  {
    final DefaultTreeWithID <KEYTYPE, VALUETYPE> aTree = new DefaultTreeWithID <KEYTYPE, VALUETYPE> ();
    final Map <KEYTYPE, DefaultTreeItemWithID <KEYTYPE, VALUETYPE>> aIDMap = new HashMap <KEYTYPE, DefaultTreeItemWithID <KEYTYPE, VALUETYPE>> ();
    int nMovedToBackCount = 0;
    while (!aOpen.isEmpty ())
    {
      // get first element
      final VALUETYPE aCurrent = aOpen.remove (0);
      final VALUETYPE aParent = aParentResolver.getParent (aCurrent);

      final KEYTYPE aCurrentID = aCurrent.getID ();
      if (aParent == null)
      {
        // it is a root item
        final DefaultTreeItemWithID <KEYTYPE, VALUETYPE> aNewItem = aTree.getRootItem ().createChildItem (aCurrentID,
                                                                                                          aCurrent);
        aIDMap.put (aCurrentID, aNewItem);
        nMovedToBackCount = 0;
      }
      else
      {
        final KEYTYPE aParentID = aParent.getID ();
        if (aIDMap.containsKey (aParentID))
        {
          // it's a subordinated ID
          final DefaultTreeItemWithID <KEYTYPE, VALUETYPE> aParentItem = aIDMap.get (aParentID);
          final DefaultTreeItemWithID <KEYTYPE, VALUETYPE> aNewItem = aParentItem.createChildItem (aCurrentID, aCurrent);
          aIDMap.put (aCurrentID, aNewItem);
          nMovedToBackCount = 0;
        }
        else
        {
          // element is unknown -> move element to the end
          aOpen.add (aCurrent);
          nMovedToBackCount++;
          if (nMovedToBackCount == aOpen.size ())
            throw new IllegalStateException ("The hierarchy is illegal. It contains elements that fit nowhere in the tree: " +
                                             aOpen);
        }
      }
    }
    return aTree;
  }

  /**
   * A generic method to build a tree of objects.
   * 
   * @param <KEYTYPE>
   *        The tree key type.
   * @param <VALUETYPE>
   *        The tree item value type.
   * @param aAll
   *        A linear list of objects to build the tree from. May not be
   *        <code>null</code>.
   * @param aParentResolver
   *        The callback method to determine the parental object of a given
   *        object. May not be <code>null</code>.
   * @return A tree with all the objects. Never <code>null</code>.
   * @throws IllegalStateException
   *         if the hierarchy cannot be determined because an object references
   *         a parent that is not in the list!
   */
  @Nonnull
  public static <KEYTYPE, VALUETYPE extends IHasID <KEYTYPE>> DefaultTreeWithID <KEYTYPE, VALUETYPE> buildTree (@Nonnull final Collection <? extends VALUETYPE> aAll,
                                                                                                                @Nonnull final IParentProvider <VALUETYPE> aParentResolver)
  {
    if (aAll == null)
      throw new NullPointerException ("all");
    if (aParentResolver == null)
      throw new NullPointerException ("parentResolver");

    return _buildTree (ContainerHelper.newList (aAll), aParentResolver);
  }

  /**
   * A generic method to build a tree of objects.
   * 
   * @param <KEYTYPE>
   *        The tree key type.
   * @param <VALUETYPE>
   *        The tree item value type.
   * @param aAll
   *        A linear list of objects to build the tree from. May not be
   *        <code>null</code>.
   * @param aParentResolver
   *        The callback method to determine the parental object of a given
   *        object. May not be <code>null</code>.
   * @return A tree with all the objects. Never <code>null</code>.
   * @throws IllegalStateException
   *         if the hierarchy cannot be determined because an object references
   *         a parent that is not in the list!
   */
  @Nonnull
  public static <KEYTYPE, VALUETYPE extends IHasID <KEYTYPE>> DefaultTreeWithID <KEYTYPE, VALUETYPE> buildTree (@Nonnull final VALUETYPE [] aAll,
                                                                                                                @Nonnull final IParentProvider <VALUETYPE> aParentResolver)
  {
    if (aAll == null)
      throw new NullPointerException ("all");
    if (aParentResolver == null)
      throw new NullPointerException ("parentResolver");

    return _buildTree (ContainerHelper.newList (aAll), aParentResolver);
  }

  /**
   * A generic method to build a tree of objects.
   * 
   * @param <KEYTYPE>
   *        The tree key type.
   * @param <VALUETYPE>
   *        The tree item value type.
   * @param aAll
   *        A linear list of objects to build the tree from. May not be
   *        <code>null</code>.
   * @return A tree with all the objects. Never <code>null</code>.
   * @throws IllegalStateException
   *         if the hierarchy cannot be determined because an object references
   *         a parent that is not in the list!
   */
  @Nonnull
  public static <KEYTYPE, VALUETYPE extends IHasParent <VALUETYPE> & IHasID <KEYTYPE>> DefaultTreeWithID <KEYTYPE, VALUETYPE> buildTree (@Nonnull final Collection <? extends VALUETYPE> aAll)
  {
    if (aAll == null)
      throw new NullPointerException ("all");

    return buildTree (aAll, new ParentProviderHasParent <VALUETYPE> ());
  }

  private static <KEYTYPE, VALUETYPE extends IHasID <KEYTYPE>> void _buildTreeRecursive (@Nullable final DefaultTreeItemWithID <KEYTYPE, VALUETYPE> aParentItem,
                                                                                         @Nonnull final IChildrenProvider <VALUETYPE> aChildrenResolver)
  {
    if (aParentItem != null)
    {
      final VALUETYPE aParentObject = aParentItem.getData ();
      if (aChildrenResolver.hasChildren (aParentObject))
        for (final VALUETYPE aChild : aChildrenResolver.getChildren (aParentObject))
        {
          final DefaultTreeItemWithID <KEYTYPE, VALUETYPE> aItem = aParentItem.createChildItem (aChild.getID (), aChild);
          _buildTreeRecursive (aItem, aChildrenResolver);
        }
    }
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE extends IHasID <KEYTYPE>> DefaultTreeWithID <KEYTYPE, VALUETYPE> buildTree (@Nonnull final IChildrenProvider <VALUETYPE> aChildrenResolver)
  {
    if (aChildrenResolver == null)
      throw new NullPointerException ("childrenResolver");

    final DefaultTreeWithID <KEYTYPE, VALUETYPE> aTree = new DefaultTreeWithID <KEYTYPE, VALUETYPE> ();

    // get all root objects
    if (aChildrenResolver.hasChildren (null))
      for (final VALUETYPE aRootObject : aChildrenResolver.getChildren (null))
      {
        // it is a root item
        final DefaultTreeItemWithID <KEYTYPE, VALUETYPE> aItem = aTree.getRootItem ()
                                                                      .createChildItem (aRootObject.getID (),
                                                                                        aRootObject);
        _buildTreeRecursive (aItem, aChildrenResolver);
      }
    return aTree;
  }
}
