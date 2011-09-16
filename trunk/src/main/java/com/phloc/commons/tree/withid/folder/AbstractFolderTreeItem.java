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
import com.phloc.commons.tree.withid.BasicTreeItemWithID;

/**
 * Abstract generic implementation of the {@link IFolderTreeItem} interface.
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
public abstract class AbstractFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE extends Collection <VALUETYPE>, ITEMTYPE extends AbstractFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE>> extends
                                                                                                                                                                                             BasicTreeItemWithID <KEYTYPE, COLLTYPE, ITEMTYPE> implements
                                                                                                                                                                                                                                              IFolderTreeItem <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE>
{
  // Combinator to create a global unique ID.
  private final ICombinator <KEYTYPE> m_aKeyCombinator;

  /**
   * Constructor for root object
   * 
   * @param aKeyCombinator
   *        The combinator for arbitrary keys.
   * @param aFactory
   *        The item factory to use.
   */
  public AbstractFolderTreeItem (@Nonnull final ICombinator <KEYTYPE> aKeyCombinator,
                                 @Nonnull final IFolderTreeItemFactory <KEYTYPE, VALUETYPE, COLLTYPE, ITEMTYPE> aFactory)
  {
    super (aFactory);
    if (aKeyCombinator == null)
      throw new NullPointerException ("keyCombinator");
    m_aKeyCombinator = aKeyCombinator;
  }

  /**
   * Constructor for normal elements
   * 
   * @param aKeyCombinator
   *        The combinator for arbitrary keys.
   * @param aParent
   *        Parent item. May never be <code>null</code> since only the root has
   *        no parent.
   * @param aDataID
   *        The ID of the new item. May not be <code>null</code>.
   */
  public AbstractFolderTreeItem (@Nonnull final ICombinator <KEYTYPE> aKeyCombinator,
                                 @Nonnull final ITEMTYPE aParent,
                                 @Nonnull final KEYTYPE aDataID)
  {
    super (aParent, aDataID);
    if (aKeyCombinator == null)
      throw new NullPointerException ("keyCombinator");
    m_aKeyCombinator = aKeyCombinator;
  }

  @Nonnull
  public final KEYTYPE getGlobalUniqueDataID ()
  {
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
    final AbstractFolderTreeItem <?, ?, ?, ?> rhs = (AbstractFolderTreeItem <?, ?, ?, ?>) o;
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
