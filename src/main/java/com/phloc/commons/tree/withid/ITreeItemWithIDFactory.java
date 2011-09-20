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

import javax.annotation.Nonnull;

import com.phloc.commons.factory.IHierarchicalFactoryWithParameter;
import com.phloc.commons.factory.IHierarchicalRootFactory;

/**
 * A factory interface that creates tree items.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The key type.
 * @param <VALUETYPE>
 *        The value type to be contained in tree items.
 */
public interface ITreeItemWithIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> extends
                                                                                                                              IHierarchicalFactoryWithParameter <ITEMTYPE, KEYTYPE>,
                                                                                                                              IHierarchicalRootFactory <ITEMTYPE>
{
  /**
   * To be called once a tree item is removed from the owning tree. This method
   * is mainly important for the tree with globally unique IDs.
   * 
   * @param aItem
   *        The item that was removed.
   */
  void onRemoveItem (@Nonnull ITEMTYPE aItem);

  /**
   * To be called once a tree item is added to the owning tree. This method is
   * mainly important for the tree with globally unique IDs.
   * 
   * @param aItem
   *        The item that was added.
   */
  void onAddItem (@Nonnull ITEMTYPE aItem);
}
