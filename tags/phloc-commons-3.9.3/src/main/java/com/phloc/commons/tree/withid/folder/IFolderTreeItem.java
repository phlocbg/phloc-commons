/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.tree.withid.folder;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * This interface represents a single folder within a directory tree. Each
 * folder has a list of file items (or leaves).
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Key type
 * @param <VALUETYPE>
 *        Value type
 * @param <COLLTYPE>
 *        Collection type consisting of value elements
 * @param <ITEMTYPE>
 *        the implementation item type
 */
public interface IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>, ITEMTYPE extends IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE>> extends
                                                                                                                                                                          ITreeItemWithID <KEYTYPE, COLLTYPE, ITEMTYPE>
{
  /**
   * @return The globally unique ID of this folder. This can e.g. be an
   *         aggregation of the ID with a separator and the parent's unique
   *         name.
   */
  @Nonnull
  KEYTYPE getGlobalUniqueDataID ();
}
