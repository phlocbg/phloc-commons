/**
 * Copyright (C) 2006-2015 phloc systems
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
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.combine.ICombinator;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.withid.unique.AbstractTreeItemWithUniqueIDFactory;

/**
 * The default folder tree item factory implementation.
 *
 * @author Philip Helger
 * @param <KEYTYPE>
 *        Key type
 * @param <DATATYPE>
 *        Value type
 * @param <COLLTYPE>
 *        Collection type consisting of value elements
 * @param <ITEMTYPE>
 *        the implementation item type
 */
@NotThreadSafe
public abstract class AbstractFolderTreeItemFactory <KEYTYPE, DATATYPE, COLLTYPE extends Collection <DATATYPE>, ITEMTYPE extends BasicFolderTreeItem <KEYTYPE, DATATYPE, COLLTYPE, ITEMTYPE>> extends AbstractTreeItemWithUniqueIDFactory <KEYTYPE, COLLTYPE, ITEMTYPE> implements IFolderTreeItemFactory <KEYTYPE, DATATYPE, COLLTYPE, ITEMTYPE>
{
  private final ICombinator <KEYTYPE> m_aKeyCombinator;

  /**
   * Ctor
   * 
   * @param aKeyCombinator
   *        Key combinator
   */
  public AbstractFolderTreeItemFactory (@Nullable final ICombinator <KEYTYPE> aKeyCombinator)
  {
    this.m_aKeyCombinator = aKeyCombinator;
  }

  @Override
  @Nonnull
  protected final KEYTYPE internalGetItemID (@Nonnull final ITEMTYPE aItem)
  {
    return aItem.getGlobalUniqueDataID ();
  }

  @Override
  @Nullable
  public final ICombinator <KEYTYPE> getKeyCombinator ()
  {
    return this.m_aKeyCombinator;
  }

  /**
   * Creates a root item
   * 
   * @return The created root item
   */
  @Nonnull
  protected abstract ITEMTYPE internalCreateRoot ();

  /*
   * This implementation is different, because the root object is also put into
   * the item store.
   */
  @Override
  @Nonnull
  public final ITEMTYPE createRoot ()
  {
    final ITEMTYPE aItem = internalCreateRoot ();
    return addToItemStore (aItem.getGlobalUniqueDataID (), aItem);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AbstractFolderTreeItemFactory <?, ?, ?, ?> rhs = (AbstractFolderTreeItemFactory <?, ?, ?, ?>) o;
    return EqualsUtils.equals (this.m_aKeyCombinator, rhs.m_aKeyCombinator);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (this.m_aKeyCombinator).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .appendIfNotNull ("keyCombinator", this.m_aKeyCombinator)
                            .toString ();
  }
}
