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
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.combine.ICombinator;

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
public class DefaultFolderTree <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends
                                                                                             AbstractFolderTree <KEYTYPE, VALUETYPE, COLLTYPE, DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE>>
{
  /**
   * Constructor that creates a {@link DefaultFolderTreeItemFactory} using the
   * passed key combinator.
   * 
   * @param aKeyCombinator
   *        The key combinator to be used. May be <code>null</code>.
   */
  public DefaultFolderTree (@Nullable final ICombinator <KEYTYPE> aKeyCombinator)
  {
    this (new DefaultFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE> (aKeyCombinator));
  }

  /**
   * Constructor
   * 
   * @param aFactory
   *        The item factory to use. May not be <code>null</code>.
   */
  public DefaultFolderTree (@Nonnull final IFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE, DefaultFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE>> aFactory)
  {
    super (aFactory);
  }

  /**
   * Create a new {@link DefaultFolderTree} using a set as the container.
   * 
   * @param aKeyCombinator
   *        The key combinator to be used
   * @return The created default folder tree
   */
  @Nonnull
  public static <K, V> DefaultFolderTree <K, V, Set <V>> createForSet (@Nonnull final ICombinator <K> aKeyCombinator)
  {
    return new DefaultFolderTree <K, V, Set <V>> (aKeyCombinator);
  }
}
