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
package com.phloc.commons.tree.simple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

@NotThreadSafe
public class BasicTreeItem <VALUETYPE, ITEMTYPE extends IBasicTreeItem <VALUETYPE, ITEMTYPE>> implements
                                                                                              IBasicTreeItem <VALUETYPE, ITEMTYPE>
{
  // item factory
  private final ITreeItemFactory <VALUETYPE, ITEMTYPE> m_aFactory;

  // parent tree item
  private ITEMTYPE m_aParent;

  // the data to be stored
  private VALUETYPE m_aData;

  // child list
  private List <ITEMTYPE> m_aChildren = null;

  /**
   * Constructor for root object.
   */
  public BasicTreeItem (@Nonnull final ITreeItemFactory <VALUETYPE, ITEMTYPE> aFactory)
  {
    if (aFactory == null)
      throw new NullPointerException ("factory");
    m_aFactory = aFactory;
    m_aParent = null;
    m_aData = null;
  }

  /**
   * Constructor for normal elements.
   * 
   * @param aParent
   *        Parent item to use. May never be <code>null</code> since only the
   *        root has no parent and for the root item a special no-argument
   *        constructor is present.
   */
  public BasicTreeItem (@Nonnull final ITEMTYPE aParent)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");
    if (!(aParent instanceof BasicTreeItem <?, ?>))
      throw new IllegalArgumentException ("Parent is no BasicTreeItem");
    if (aParent.getFactory () == null)
      throw new IllegalStateException ("parent item has no factory");
    m_aParent = aParent;
    m_aFactory = aParent.getFactory ();
    m_aData = null;
  }

  @Nonnull
  public final ITreeItemFactory <VALUETYPE, ITEMTYPE> getFactory ()
  {
    return m_aFactory;
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

  @Nullable
  public final VALUETYPE getData ()
  {
    return m_aData;
  }

  public final void setData (@Nullable final VALUETYPE aData)
  {
    if (!isValidData (aData))
      throw new IllegalArgumentException ("The passed data object is invalid!");
    m_aData = aData;
  }

  public final boolean isRootItem ()
  {
    return m_aParent == null;
  }

  @Nullable
  public final ITEMTYPE getParent ()
  {
    return m_aParent;
  }

  @Nonnull
  private ITEMTYPE _asT (@Nonnull final BasicTreeItem <VALUETYPE, ITEMTYPE> aItem)
  {
    return GenericReflection.<BasicTreeItem <VALUETYPE, ITEMTYPE>, ITEMTYPE> uncheckedCast (aItem);
  }

  /**
   * Add a child item to this item.
   * 
   * @param aData
   *        the data associated with this item
   * @return the created TreeItem object or <code>null</code> if the ID is
   *         already in use and bAllowOverwrite is false
   */
  @Nonnull
  public final ITEMTYPE createChildItem (@Nullable final VALUETYPE aData)
  {
    // create new item
    final ITEMTYPE aItem = m_aFactory.create (_asT (this));
    aItem.setData (aData);
    internalAddChild (aItem);
    return aItem;
  }

  public final boolean hasChildren ()
  {
    return m_aChildren != null && !m_aChildren.isEmpty ();
  }

  @Nullable
  @ReturnsImmutableObject
  public final List <ITEMTYPE> getChildren ()
  {
    return m_aChildren == null ? null : ContainerHelper.makeUnmodifiable (m_aChildren);
  }

  @Nullable
  public final ITEMTYPE getChildAtIndex (@Nonnegative final int nIndex)
  {
    if (m_aChildren == null)
      throw new IndexOutOfBoundsException ("Tree item has no children!");
    return m_aChildren.get (nIndex);
  }

  @Nonnegative
  public final int getChildCount ()
  {
    return m_aChildren != null ? m_aChildren.size () : 0;
  }

  public final boolean isSameOrChildOf (@Nonnull final ITEMTYPE aParent)
  {
    if (aParent == null)
      throw new NullPointerException ("parent");

    IBasicTreeItem <VALUETYPE, ITEMTYPE> aCur = this;
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
    if (getParent ().removeChild (aThis).isUnchanged ())
      throw new IllegalStateException ("Failed to remove this from parent!");

    // Remember new parent!
    m_aParent = aNewParent;
    return ESuccess.valueOfChange (aNewParent.internalAddChild (aThis));
  }

  @Nonnull
  public final EChange internalAddChild (@Nonnull final ITEMTYPE aChild)
  {
    if (aChild == null)
      throw new NullPointerException ("child");

    // Ensure children are present
    if (m_aChildren == null)
      m_aChildren = new ArrayList <ITEMTYPE> ();

    return EChange.valueOf (m_aChildren.add (aChild));
  }

  @Nonnull
  public final EChange removeChild (@Nonnull final ITEMTYPE aChild)
  {
    if (aChild == null)
      throw new NullPointerException ("child");

    return EChange.valueOf (m_aChildren != null && m_aChildren.remove (aChild));
  }

  public final void reorderChildItems (@Nonnull final Comparator <? super ITEMTYPE> aComparator)
  {
    if (m_aChildren != null)
      m_aChildren = ContainerHelper.getSorted (m_aChildren, aComparator);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof BasicTreeItem <?, ?>))
      return false;
    final BasicTreeItem <?, ?> rhs = (BasicTreeItem <?, ?>) o;
    return CompareUtils.nullSafeEquals (m_aData, rhs.m_aData) &&
           CompareUtils.nullSafeEquals (m_aChildren, rhs.m_aChildren);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aData).append (m_aChildren).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("data", m_aData).append ("children", m_aChildren).toString ();
  }
}
