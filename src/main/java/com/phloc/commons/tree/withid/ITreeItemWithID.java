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
package com.phloc.commons.tree.withid;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.id.IHasID;
import com.phloc.commons.state.EChange;
import com.phloc.commons.tree.IABasicTreeItem;

/**
 * Represents a basic tree item with an ID. Never use this class directly but
 * provide a sub interface that specifies the generic T parameter.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The type of the ID.
 * @param <VALUETYPE>
 *        The type of the value.
 * @param <ITEMTYPE>
 *        The implementation type.
 */
public interface ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> extends
                                                                                                                       IABasicTreeItem <VALUETYPE, ITEMTYPE>,
                                                                                                                       IHasID <KEYTYPE>
{
  /**
   * @return The factory used to create instances of this interface.
   */
  @Nonnull
  ITreeItemWithIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> getFactory ();

  /**
   * @return The data ID of this item. May be <code>null</code> depending on the
   *         validator.
   */
  KEYTYPE getID ();

  /**
   * Find the direct child item with the given ID
   * 
   * @param aDataID
   *        The ID to search. May be <code>null</code>.
   * @return <code>null</code> if this item has no child with the given ID the
   *         item otherwise
   */
  @Nullable
  ITEMTYPE getChildItemOfDataID (@Nullable KEYTYPE aDataID);

  /**
   * Add an existing direct child to this tree item. Use only internally!
   * 
   * @param aDataID
   *        The data ID to use. May not be <code>null</code>.
   * @param aChild
   *        The child to be added. May not be <code>null</code>.
   * @param bAllowOverwrite
   *        if <code>true</code> existing elements are overwritten.
   * @return {@link EChange}
   */
  @Nonnull
  EChange internalAddChild (@Nonnull KEYTYPE aDataID, @Nonnull ITEMTYPE aChild, boolean bAllowOverwrite);

  /**
   * Add a direct child item to this item. If another item with the same ID is
   * already contained, the item is automatically overwritten.
   * 
   * @param aDataID
   *        ID of the item to generate. May not be <code>null</code>.
   * @param aData
   *        The data associated with this item.
   * @return the created tree item
   */
  @Nonnull
  ITEMTYPE createChildItem (@Nonnull KEYTYPE aDataID, VALUETYPE aData);

  /**
   * Add a direct child item to this item.
   * 
   * @param aDataID
   *        ID of the item to generate. May not be <code>null</code>.
   * @param aData
   *        The data associated with this item.
   * @param bAllowOverwrite
   *        If <code>true</code> a potential existing child item with the same
   *        ID is overwritten.
   * @return the created tree item or <code>null</code> if the data ID is
   *         already in use and bAllowOverwrite is <code>false</code>
   */
  @Nullable
  ITEMTYPE createChildItem (KEYTYPE aDataID, VALUETYPE aData, boolean bAllowOverwrite);

  /**
   * Remove the passed node as a direct child node from this node.
   * 
   * @param aDataID
   *        The ID of the element to be removed. May not be <code>null</code.
   * @return {@link EChange}
   */
  @Nonnull
  EChange removeChild (KEYTYPE aDataID);

  /**
   * Remove all children from this node.
   * 
   * @return {@link EChange}
   */
  @Nonnull
  EChange removeAllChildren ();

  /**
   * Reorder the child items based on the item itself.
   * 
   * @param aComparator
   *        The comparator use. May not be <code>null</code>.
   */
  void reorderChildrenByItems (@Nonnull Comparator <? super ITEMTYPE> aComparator);
}
