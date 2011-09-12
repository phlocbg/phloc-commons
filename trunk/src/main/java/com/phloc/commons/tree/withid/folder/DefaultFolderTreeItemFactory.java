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
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.withid.unique.AbstractBasicTreeItemWithUniqueIDFactory;

/**
 * The default folder tree item factory implementation.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Key type
 * @param <VALUETYPE>
 *        Value type
 * @param <COLLTYPE>
 *        Collection type consisting of value elements
 */
public class DefaultFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>> extends
                                                                                                        AbstractBasicTreeItemWithUniqueIDFactory <KEYTYPE, COLLTYPE, IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE>> implements
                                                                                                                                                                                                                    IFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE>
{
  private final ICombinator <KEYTYPE> m_aKeyCombinator;

  public DefaultFolderTreeItemFactory (@Nonnull final ICombinator <KEYTYPE> aKeyCombinator)
  {
    if (aKeyCombinator == null)
      throw new NullPointerException ("keyCombinator");
    m_aKeyCombinator = aKeyCombinator;
  }

  /*
   * This implementation is different, because the root object is also put into
   * the item store.
   */
  @Nonnull
  public final IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> createRoot ()
  {
    final IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> aItem = new FolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> (m_aKeyCombinator,
                                                                                                                    this);
    return addToItemStore (aItem.getGlobalUniqueDataID (), aItem);
  }

  @Override
  @Nonnull
  protected IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> internalCreate (@Nonnull final IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> aParent,
                                                                           @Nonnull final KEYTYPE aDataID)
  {
    return new FolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE> (m_aKeyCombinator, aParent, aDataID);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final DefaultFolderTreeItemFactory <?, ?, ?> rhs = (DefaultFolderTreeItemFactory <?, ?, ?>) o;
    return m_aKeyCombinator.equals (rhs.m_aKeyCombinator);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aKeyCombinator).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("keyCombinator", m_aKeyCombinator).toString ();
  }
}
