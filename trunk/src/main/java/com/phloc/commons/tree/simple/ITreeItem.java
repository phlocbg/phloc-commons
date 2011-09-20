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

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;
import com.phloc.commons.tree.IABasicTreeItem;

/**
 * Base interface both for normal tree items and tree items with ID.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 */
public interface ITreeItem <VALUETYPE, ITEMTYPE extends ITreeItem <VALUETYPE, ITEMTYPE>> extends
                                                                                         IABasicTreeItem <VALUETYPE, ITEMTYPE>
{
  /**
   * @return The factory used to create instances of this interface.
   */
  @Nonnull
  ITreeItemFactory <VALUETYPE, ITEMTYPE> getFactory ();

  /**
   * Add an existing child to this tree item. Use only internally!
   * 
   * @param aChild
   *        The child to be added. May not be <code>null</code>.
   * @return {@link EChange#UNCHANGED} if the child is already contained,
   *         {@link EChange#CHANGED} upon success.
   */
  @Nonnull
  EChange internalAddChild (@Nonnull ITEMTYPE aChild);

  /**
   * Add a child item to this item.
   * 
   * @param aData
   *        the data associated with this item
   * @return the created TreeItem object
   */
  @Nonnull
  ITEMTYPE createChildItem (@Nullable VALUETYPE aData);

  /**
   * Remove the passed node as a child node from this node.
   * 
   * @param aChild
   *        The child to be removed. May not be <code>null</code.
   * @return {@link EChange#CHANGED} if the removal succeeded,
   *         {@link EChange#UNCHANGED} otherwise
   */
  @Nonnull
  EChange removeChild (@Nonnull ITEMTYPE aChild);

  /**
   * Reorder the child items based on the item itself.
   * 
   * @param aComparator
   *        The comparator use. May not be <code>null</code>.
   */
  void reorderChildItems (@Nonnull Comparator <? super ITEMTYPE> aComparator);
}
