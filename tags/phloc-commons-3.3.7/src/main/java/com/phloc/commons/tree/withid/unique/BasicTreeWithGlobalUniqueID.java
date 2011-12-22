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
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.withid.BasicTreeWithID;
import com.phloc.commons.tree.withid.ITreeItemWithID;

/**
 * A managed tree is a specialized version of the tree, where each item is
 * required to have a unique ID so that item searching can be performed quite
 * easily.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The type of the key elements for the tree.
 * @param <VALUETYPE>
 *        The type of the elements contained in the tree
 */
@NotThreadSafe
public class BasicTreeWithGlobalUniqueID <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> extends
                                                                                                                               BasicTreeWithID <KEYTYPE, VALUETYPE, ITEMTYPE> implements
                                                                                                                                                                             ITreeWithGlobalUniqueID <KEYTYPE, VALUETYPE, ITEMTYPE>
{
  private final ITreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> m_aFactory;

  public BasicTreeWithGlobalUniqueID (@Nonnull final ITreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> aFactory)
  {
    super (aFactory);
    m_aFactory = aFactory;
  }

  /**
   * @return The factory used for creation. For internal use only.
   */
  @Nonnull
  protected final ITreeItemWithUniqueIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> getFactory ()
  {
    return m_aFactory;
  }

  @Nullable
  public final ITEMTYPE getItemWithID (final KEYTYPE aDataID)
  {
    return m_aFactory.getItemOfDataID (aDataID);
  }

  @Nullable
  public final ITEMTYPE getChildWithID (@Nullable final ITEMTYPE aCurrentItem, @Nullable final KEYTYPE aDataID)
  {
    final ITEMTYPE aItem = aCurrentItem != null ? aCurrentItem : getRootItem ();
    return aItem.getChildItemOfDataID (aDataID);
  }

  @Nonnull
  @ReturnsImmutableObject
  public final Collection <ITEMTYPE> getAllItems ()
  {
    return m_aFactory.getAllItems ();
  }

  public final boolean isItemSameOrDescendant (@Nullable final KEYTYPE aParentItemID,
                                               @Nullable final KEYTYPE aChildItemID)
  {
    final ITEMTYPE aSearchParent = getItemWithID (aParentItemID);
    if (aSearchParent == null)
      return false;

    final ITEMTYPE aChild = getItemWithID (aChildItemID);
    if (aChild == null)
      return false;

    return aChild.isSameOrChildOf (aSearchParent);
  }

  public boolean hasChildren (@Nullable final ITEMTYPE aItem)
  {
    return aItem == null ? getRootItem ().hasChildren () : aItem.hasChildren ();
  }

  @Nonnegative
  public int getChildCount (@Nullable final ITEMTYPE aItem)
  {
    return aItem == null ? getRootItem ().getChildCount () : aItem.getChildCount ();
  }

  @Nullable
  public List <? extends ITEMTYPE> getChildren (@Nullable final ITEMTYPE aItem)
  {
    return aItem == null ? getRootItem ().getChildren () : aItem.getChildren ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final BasicTreeWithGlobalUniqueID <?, ?, ?> rhs = (BasicTreeWithGlobalUniqueID <?, ?, ?>) o;
    return m_aFactory.equals (rhs.m_aFactory);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aFactory).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("factory", m_aFactory).toString ();
  }
}
