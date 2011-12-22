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
package com.phloc.commons.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.parent.IHasChildrenSorted;
import com.phloc.commons.parent.IHasParent;
import com.phloc.commons.state.ESuccess;

/**
 * Base interface both for normal tree items and tree items with ID.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        Data type of the items.
 * @param <ITEMTYPE>
 *        tree item type
 */
public interface IBasicTreeItem <VALUETYPE, ITEMTYPE extends IBasicTreeItem <VALUETYPE, ITEMTYPE>> extends
                                                                                                   IHasParent <ITEMTYPE>,
                                                                                                   IHasChildrenSorted <ITEMTYPE>
{
  /**
   * @return the data associated with this node. May be <code>null</code>.
   */
  @Nullable
  VALUETYPE getData ();

  /**
   * Change the data associated with this node.
   * 
   * @param aData
   *        The data associated with this node. May be <code>null</code>.
   * @throws IllegalArgumentException
   *         If the data is not valid (depending on any custom validator).
   */
  void setData (@Nullable VALUETYPE aData);

  /**
   * @return <code>true</code> if this is the internal root item without a
   *         parent, <code>false</code> if this is a public item.
   */
  boolean isRootItem ();

  /**
   * Check if this item is the same or a child of the passed item. This is not
   * limited to direct children but to children on all levels.
   * 
   * @param aParent
   *        The parent item to check whether this is a child of it. May not be
   *        <code>null</code>.
   * @return <code>true</code> if <code>this</code> is the same or a child of
   *         aParent.
   */
  boolean isSameOrChildOf (@Nonnull ITEMTYPE aParent);

  /**
   * Change the parent node of this node to another node (subordination).
   * 
   * @param aNewParent
   *        The new parent to use. May not be <code>null</code>. To make it a
   *        root item, pass the owning tree's root item.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess changeParent (@Nonnull ITEMTYPE aNewParent);
}
