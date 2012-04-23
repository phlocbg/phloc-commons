/**
 * Copyright (C) 2006-2012 phloc systems
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Basic tree item with ID implementation, independent of the implementation
 * type.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        tree item key type
 * @param <VALUETYPE>
 *        tree item value type
 * @param <ITEMTYPE>
 *        tree item implementation type
 */
@NotThreadSafe
public class BasicTreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>> implements
                                                                                                                       ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>
{
  // item factory
  private final ITreeItemWithIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> m_aFactory;

  // parent tree item
  private ITEMTYPE m_aParent;

  // ID of this item
  private final KEYTYPE m_aDataID;

  // the data to be stored
  private VALUETYPE m_aData;

  // child map & list
  private Map <KEYTYPE, ITEMTYPE> m_aChildMap = null;
  private List <ITEMTYPE> m_aChildList = null;

  /**
   * Constructor for root object
   */
  public BasicTreeItemWithID (@Nonnull final ITreeItemWithIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> aFactory)
  {
    this (aFactory, null);
  }

  /**
   * Constructor for root object
   */
  public BasicTreeItemWithID (@Nonnull final ITreeItemWithIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> aFactory,
                              @Nullable final KEYTYPE aDataID)
  {
    if (aFactory == null)
      throw new NullPointerException ("factory");
    m_aFactory = aFactory;
    m_aDataID = aDataID;
  }

  /**
   * Constructor for normal elements
   * 
   * @param aParent
   *        Parent item. May never be <code>null</code> since only the root has
   *        no parent.
   * @param aDataID
   *        The ID of the new item. May not be <code>null</code>.
   */
  public BasicTreeItemWithID (@Nonnull final ITEMTYPE aParent, @Nonnull final KEYTYPE aDataID)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");
    if (!(aParent instanceof BasicTreeItemWithID <?, ?, ?>))
      throw new IllegalArgumentException ("Parent is no BasicTreeItemWithID");
    if (aDataID == null)
      throw new NullPointerException ("dataID");
    if (aParent.getFactory () == null)
      throw new IllegalStateException ("Parent item has no factory!");
    m_aParent = aParent;
    m_aFactory = m_aParent.getFactory ();
    m_aDataID = aDataID;
  }

  @Nonnull
  public final ITreeItemWithIDFactory <KEYTYPE, VALUETYPE, ITEMTYPE> getFactory ()
  {
    return m_aFactory;
  }

  /**
   * This method is called to validate a data ID object. This method may be
   * overloaded in derived classes. The default implementation accepts all
   * values.
   * 
   * @param aDataID
   *        The value to validate.
   * @return <code>true</code> if the ID is valid, <code>false</code> otherwise.
   */
  @OverrideOnDemand
  protected boolean isValidDataID (final KEYTYPE aDataID)
  {
    return true;
  }

  /**
   * This method is called to validate a data object. This method may be
   * overloaded in derived classes. The default implementation accepts all
   * values.
   * 
   * @param aData
   *        The value to validate.
   * @return <code>true</code> if the ID is valid, <code>false</code> otherwise.
   */
  @OverrideOnDemand
  protected boolean isValidData (final VALUETYPE aData)
  {
    return true;
  }

  public final boolean isRootItem ()
  {
    return m_aParent == null;
  }

  @Nonnull
  private ITEMTYPE _asT (@Nonnull final BasicTreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE> aItem)
  {
    return GenericReflection.<BasicTreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE>, ITEMTYPE> uncheckedCast (aItem);
  }

  /**
   * @return The parent item of this item or <code>null</code> if this is the
   *         root item.
   */
  @Nullable
  public final ITEMTYPE getParent ()
  {
    return m_aParent;
  }

  @Nullable
  public final KEYTYPE getID ()
  {
    return m_aDataID;
  }

  @Nullable
  public final VALUETYPE getData ()
  {
    return m_aData;
  }

  public final boolean hasChildren ()
  {
    return m_aChildMap != null && !m_aChildMap.isEmpty ();
  }

  @Nonnegative
  public final int getChildCount ()
  {
    return m_aChildMap == null ? 0 : m_aChildMap.size ();
  }

  @Nullable
  @ReturnsImmutableObject
  public final List <ITEMTYPE> getChildren ()
  {
    return m_aChildList == null ? null : ContainerHelper.makeUnmodifiable (m_aChildList);
  }

  @Nullable
  public final ITEMTYPE getChildAtIndex (@Nonnegative final int nIndex)
  {
    if (m_aChildList == null)
      throw new IndexOutOfBoundsException ("Tree item has no children!");
    return m_aChildList.get (nIndex);
  }

  public final void setData (@Nullable final VALUETYPE aData)
  {
    if (!isValidData (aData))
      throw new IllegalArgumentException ("The passed data object is invalid!");
    m_aData = aData;
  }

  public final ITEMTYPE createChildItem (@Nullable final KEYTYPE aDataID, @Nullable final VALUETYPE aData)
  {
    return createChildItem (aDataID, aData, true);
  }

  public final ITEMTYPE createChildItem (@Nullable final KEYTYPE aDataID,
                                         @Nullable final VALUETYPE aData,
                                         final boolean bAllowOverwrite)
  {
    if (!isValidDataID (aDataID))
      throw new IllegalArgumentException ("Illegal data ID provided");

    ITEMTYPE aItem = getChildItemOfDataID (aDataID);
    if (aItem != null)
    {
      // ID already exists
      if (!bAllowOverwrite)
        return null;

      // just change data of existing item
      aItem.setData (aData);
    }
    else
    {
      // create new item
      aItem = m_aFactory.create (_asT (this), aDataID);
      aItem.setData (aData);
      if (m_aChildMap == null)
      {
        m_aChildMap = new HashMap <KEYTYPE, ITEMTYPE> ();
        m_aChildList = new ArrayList <ITEMTYPE> ();
      }
      m_aChildMap.put (aDataID, aItem);
      m_aChildList.add (aItem);
    }
    return aItem;
  }

  @Nullable
  public final ITEMTYPE getChildItemOfDataID (@Nullable final KEYTYPE aDataID)
  {
    return m_aChildMap == null ? null : m_aChildMap.get (aDataID);
  }

  public final boolean isSameOrChildOf (@Nonnull final ITEMTYPE aParent)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    ITreeItemWithID <KEYTYPE, VALUETYPE, ITEMTYPE> aCur = this;
    while (aCur != null)
    {
      // Do not use "equals" because it recursively compares all children!
      if (aCur == aParent)
        return true;
      aCur = aCur.getParent ();
    }
    return false;
  }

  @Nonnull
  public final ESuccess changeParent (@Nonnull final ITEMTYPE aNewParent)
  {
    if (aNewParent == null)
      throw new NullPointerException ("newParent");

    // no change so far
    if (getParent () == aNewParent)
      return ESuccess.SUCCESS;

    // cannot make a child of this, this' new parent.
    final ITEMTYPE aThis = _asT (this);
    if (aNewParent.isSameOrChildOf (aThis))
      return ESuccess.FAILURE;

    // add this to the new parent
    if (m_aParent.removeChild (getID ()).isUnchanged ())
      throw new IllegalStateException ("Failed to remove this from parent!");

    // Remember new parent!
    m_aParent = aNewParent;
    return ESuccess.valueOfChange (aNewParent.internalAddChild (getID (), aThis, false));
  }

  @Nonnull
  public final EChange internalAddChild (@Nonnull final KEYTYPE aDataID,
                                         @Nonnull final ITEMTYPE aChild,
                                         final boolean bAllowOverwrite)
  {
    if (aChild == null)
      throw new NullPointerException ("child");

    // Ensure children are present
    if (m_aChildMap != null)
    {
      if (!bAllowOverwrite && m_aChildMap.containsKey (aDataID))
        return EChange.UNCHANGED;
    }
    else
    {
      m_aChildMap = new HashMap <KEYTYPE, ITEMTYPE> ();
      m_aChildList = new ArrayList <ITEMTYPE> ();
    }

    m_aChildMap.put (aDataID, aChild);
    m_aChildList.add (aChild);
    m_aFactory.onAddItem (aChild);
    return EChange.CHANGED;
  }

  @Nonnull
  public final EChange removeChild (@Nonnull final KEYTYPE aDataID)
  {
    if (aDataID == null)
      throw new NullPointerException ("dataID");

    // Any children present
    if (m_aChildMap == null)
      return EChange.UNCHANGED;

    // Main removal
    final ITEMTYPE aItem = m_aChildMap.remove (aDataID);
    if (aItem == null)
      return EChange.UNCHANGED;
    if (!m_aChildList.remove (aItem))
      throw new IllegalStateException ("Failed to remove item from list: " + aItem);

    // Notify factory
    m_aFactory.onRemoveItem (aItem);
    return EChange.CHANGED;
  }

  @Nonnull
  public final EChange removeAllChildren ()
  {
    if (m_aChildMap == null || m_aChildMap.isEmpty ())
      return EChange.UNCHANGED;

    // Remember all children
    final List <ITEMTYPE> aAllChildren = new ArrayList <ITEMTYPE> (m_aChildList);

    // Remove all children
    m_aChildMap.clear ();
    m_aChildList.clear ();

    // Notify factory after removal
    for (final ITEMTYPE aItem : aAllChildren)
      m_aFactory.onRemoveItem (aItem);
    return EChange.CHANGED;
  }

  public final void reorderChildrenByItems (@Nonnull final Comparator <? super ITEMTYPE> aComparator)
  {
    if (m_aChildList != null)
      ContainerHelper.getSortedInline (m_aChildList, aComparator);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final BasicTreeItemWithID <?, ?, ?> rhs = (BasicTreeItemWithID <?, ?, ?>) o;
    return EqualsUtils.equals (m_aDataID, rhs.m_aDataID) &&
           EqualsUtils.equals (m_aData, rhs.m_aData) &&
           EqualsUtils.equals (m_aChildMap, rhs.m_aChildMap);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aData).append (m_aDataID).append (m_aChildMap).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("dataID", m_aDataID)
                                       .append ("data", m_aData)
                                       .append ("children", m_aChildMap)
                                       .toString ();
  }
}
