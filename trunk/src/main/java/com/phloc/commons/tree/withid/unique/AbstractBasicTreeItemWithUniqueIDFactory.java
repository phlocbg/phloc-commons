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
package com.phloc.commons.tree.withid.unique;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.withid.IBasicTreeItemWithID;

/**
 * An abstract tree item factory that maintains a unique ID over all items. It
 * does not put the root item in the data store!
 * 
 * @author philip
 */
public abstract class AbstractBasicTreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE extends IBasicTreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> implements
                                                                                                                                                          ITreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE>
{
  private final Map <KEYTYPE, ITEMTYPE> m_aItemStore;

  public AbstractBasicTreeItemWithUniqueIDFactory ()
  {
    this (new HashMap <KEYTYPE, ITEMTYPE> ());
  }

  public AbstractBasicTreeItemWithUniqueIDFactory (@Nonnull final Map <KEYTYPE, ITEMTYPE> aItemStore)
  {
    if (aItemStore == null)
      throw new NullPointerException ("itemStore");
    m_aItemStore = aItemStore;
  }

  @Nonnull
  protected final ITEMTYPE addToItemStore (@Nonnull final KEYTYPE aDataID, @Nonnull final ITEMTYPE aItem)
  {
    // Is the ID already in use?
    if (m_aItemStore.containsKey (aDataID))
      throw new IllegalArgumentException ("An item with ID " + aDataID + " is already contained!");
    m_aItemStore.put (aDataID, aItem);
    return aItem;
  }

  @Nonnull
  protected abstract ITEMTYPE internalCreate (@Nonnull final ITEMTYPE aParent, @Nonnull final KEYTYPE aDataID);

  @Nonnull
  public final ITEMTYPE create (@Nonnull final ITEMTYPE aParent, @Nonnull final KEYTYPE aDataID)
  {
    if (aParent == null)
      throw new NullPointerException ("parent may not be null - use createRoot instead!");

    // Create and store the item via the default factory
    final ITEMTYPE aTreeItem = internalCreate (aParent, aDataID);
    return addToItemStore (aDataID, aTreeItem);
  }

  public final void onRemoveItem (@Nonnull final ITEMTYPE aItem)
  {
    // Remove item from item store
    m_aItemStore.remove (aItem.getID ());
  }

  public final void onAddItem (@Nonnull final ITEMTYPE aItem)
  {
    // Add item to item store
    addToItemStore (aItem.getID (), aItem);
  }

  @Nullable
  public final ITEMTYPE getItemOfDataID (@Nullable final KEYTYPE aDataID)
  {
    return m_aItemStore.get (aDataID);
  }

  @Nonnull
  @ReturnsImmutableObject
  public final Collection <ITEMTYPE> getAllItems ()
  {
    // Avoid unintentional modification of internal item store
    return ContainerHelper.makeUnmodifiable (m_aItemStore.values ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractBasicTreeItemWithUniqueIDFactory <?, ?, ?> rhs = (AbstractBasicTreeItemWithUniqueIDFactory <?, ?, ?>) o;
    return m_aItemStore.equals (rhs.m_aItemStore);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aItemStore).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("itemStoreKeys", m_aItemStore.keySet ()).toString ();
  }
}
