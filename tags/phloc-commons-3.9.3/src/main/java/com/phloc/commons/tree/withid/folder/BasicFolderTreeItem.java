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
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.combine.ICombinator;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.withid.BasicTreeItemWithID;

/**
 * Base implementation of the {@link IFolderTreeItem} interface.
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
@NotThreadSafe
public class BasicFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>, ITEMTYPE extends BasicFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE>> extends
                                                                                                                                                                              BasicTreeItemWithID <KEYTYPE, COLLTYPE, ITEMTYPE> implements
                                                                                                                                                                                                                               IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE>
{
  // Combinator to create a global unique ID.
  private final ICombinator <KEYTYPE> m_aKeyCombinator;

  /**
   * Constructor for root object
   * 
   * @param aFactory
   *        The item factory to use.
   */
  public BasicFolderTreeItem (@Nonnull final IFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE> aFactory)
  {
    super (aFactory);
    m_aKeyCombinator = aFactory.getKeyCombinator ();
  }

  /**
   * Constructor for root object
   * 
   * @param aFactory
   *        The item factory to use.
   * @param aDataID
   *        The data ID of the root item.
   */
  public BasicFolderTreeItem (@Nonnull final IFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE> aFactory,
                              @Nullable final KEYTYPE aDataID)
  {
    super (aFactory, aDataID);
    m_aKeyCombinator = aFactory.getKeyCombinator ();
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
  public BasicFolderTreeItem (@Nonnull final ITEMTYPE aParent, @Nonnull final KEYTYPE aDataID)
  {
    super (aParent, aDataID);
    m_aKeyCombinator = aParent.m_aKeyCombinator;
  }

  @Nonnull
  public final KEYTYPE getGlobalUniqueDataID ()
  {
    if (m_aKeyCombinator == null)
      return getID ();

    final ITEMTYPE aParent = getParent ();
    return aParent == null ? getID () : m_aKeyCombinator.combine (aParent.getGlobalUniqueDataID (), getID ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final BasicFolderTreeItem <?, ?, ?, ?> rhs = (BasicFolderTreeItem <?, ?, ?, ?>) o;
    return EqualsUtils.equals (m_aKeyCombinator, rhs.m_aKeyCombinator);
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
