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
package com.phloc.commons.tree.withid.folder;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.phloc.commons.combine.ICombinator;
import com.phloc.commons.tree.withid.unique.BasicTreeWithGlobalUniqueID;

/**
 * Root class for a simple tree. The elements of the tree are not sorted by any
 * means.
 * 
 * @param <KEYTYPE>
 *        The type of the element keys.
 * @param <VALUETYPE>
 *        The type of the elements contained in the tree
 * @param <COLLTYPE>
 *        the collection type consisting of value elements
 * @author philip
 */
public class FolderTree <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends
                                                                                      BasicTreeWithGlobalUniqueID <KEYTYPE, COLLTYPE, IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE>> implements
                                                                                                                                                                                     IFolderTree <KEYTYPE, VALUETYPE, COLLTYPE>
{
  /**
   * Constructor that creates a {@link DefaultFolderTreeItemFactory} using the
   * passed key combinator.
   * 
   * @param aKeyCombinator
   *        The key combinator to be used. May not be <code>null</code>.
   */
  public FolderTree (@Nonnull final ICombinator <KEYTYPE> aKeyCombinator)
  {
    this (new DefaultFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE> (aKeyCombinator));
  }

  /**
   * Constructor
   * 
   * @param aFactory
   *        The item factory to use. May not be <code>null</code>.
   */
  public FolderTree (@Nonnull final IFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE> aFactory)
  {
    super (aFactory);
  }
}
