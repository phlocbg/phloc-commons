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
package com.phloc.commons.microdom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.EMicroEvent;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Basic implementation class for the micro document object model. It overrides
 * all methods required for correct parent/child handling.
 * 
 * @author philip
 */
abstract class AbstractMicroNodeWithChildren extends AbstractMicroNode
{
  /** The list of child elements. May be <code>null</code>. */
  private List <IMicroNode> m_aChildren;

  /**
   * @return The writable list of all child nodes - handle with care. May be
   *         <code>null</code>.
   */
  @Nullable
  @ReturnsMutableObject (reason = "efficient access")
  final List <IMicroNode> directGetChildren ()
  {
    return m_aChildren;
  }

  private void _afterInsertAsChildOfThis (@Nonnull final AbstractMicroNode aChildNode)
  {
    aChildNode.setParentNode (this);
    onEvent (EMicroEvent.NODE_INSERTED, this, aChildNode);
  }

  @Override
  protected void onAppendChild (@Nonnull final AbstractMicroNode aChildNode)
  {
    if (aChildNode.isDocument ())
      throw new MicroException ("Cannot add document to documents");
    if (m_aChildren == null)
      m_aChildren = new ArrayList <IMicroNode> ();
    m_aChildren.add (aChildNode);
    _afterInsertAsChildOfThis (aChildNode);
  }

  @Override
  protected final void onInsertBefore (@Nonnull final AbstractMicroNode aChildNode, @Nonnull final IMicroNode aSuccessor)
  {
    if (aChildNode.isDocument ())
      throw new MicroException ("Cannot add document to nodes");
    if (aSuccessor == null || m_aChildren == null)
      throw new MicroException ("Cannot add before element which is not contained!");
    final int nIndex = m_aChildren.lastIndexOf (aSuccessor);
    if (nIndex == -1)
      throw new MicroException ("Cannot add before element which is not contained!");
    m_aChildren.add (nIndex, aChildNode);
    _afterInsertAsChildOfThis (aChildNode);
  }

  @Override
  protected final void onInsertAfter (@Nonnull final AbstractMicroNode aChildNode,
                                      @Nonnull final IMicroNode aPredecessor)
  {
    if (aChildNode.isDocument ())
      throw new MicroException ("Cannot add document to nodes");
    if (aPredecessor == null || m_aChildren == null)
      throw new MicroException ("Cannot add after element which is not contained!");
    final int nIndex = m_aChildren.lastIndexOf (aPredecessor);
    if (nIndex == -1)
      throw new MicroException ("Cannot add after element which is not contained!");
    m_aChildren.add (nIndex + 1, aChildNode);
    _afterInsertAsChildOfThis (aChildNode);
  }

  @Override
  protected final void onInsertAtIndex (@Nonnegative final int nIndex, @Nonnull final AbstractMicroNode aChildNode)
  {
    if (nIndex < 0)
      throw new MicroException ("Cannot insert element at index " + nIndex + "!");
    if (aChildNode.isDocument ())
      throw new MicroException ("Cannot add document to nodes");
    if (m_aChildren == null)
      m_aChildren = new ArrayList <IMicroNode> ();
    m_aChildren.add (Math.min (nIndex, m_aChildren.size ()), aChildNode);
    _afterInsertAsChildOfThis (aChildNode);
  }

  @Override
  @Nonnull
  protected final EChange onRemoveChild (@Nonnull final IMicroNode aChild)
  {
    if (!aChild.hasParent ())
      throw new MicroException ("The passed child node to be removed has no parent!");

    if (m_aChildren == null || !m_aChildren.remove (aChild))
      return EChange.UNCHANGED;

    if (m_aChildren.contains (aChild))
      throw new IllegalStateException ("Child " + aChild + " is contained more than once in it's parents list");

    ((AbstractMicroNode) aChild).resetParentNode ();
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected final EChange onRemoveChildAtIndex (@Nonnegative final int nIndex)
  {
    final IMicroNode aChild = getChildAtIndex (nIndex);
    if (aChild == null)
      return EChange.UNCHANGED;

    if (!aChild.hasParent ())
      throw new MicroException ("The passed child node to be removed has no parent!");

    if (m_aChildren.remove (nIndex) == null)
      return EChange.UNCHANGED;

    if (m_aChildren.contains (aChild))
      throw new IllegalStateException ("Child " + aChild + " is contained more than once in it's parents list");

    ((AbstractMicroNode) aChild).resetParentNode ();
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected final EChange onRemoveAllChildren ()
  {
    if (m_aChildren == null || m_aChildren.isEmpty ())
      return EChange.UNCHANGED;

    for (final IMicroNode aChild : m_aChildren)
    {
      if (!aChild.hasParent ())
        throw new MicroException ("One child node to be removed has no parent!");
      ((AbstractMicroNode) aChild).resetParentNode ();
    }
    m_aChildren = null;
    return EChange.CHANGED;
  }

  @Override
  public final boolean hasChildren ()
  {
    return m_aChildren != null && !m_aChildren.isEmpty ();
  }

  @Override
  @Nullable
  @ReturnsMutableCopy
  public final List <IMicroNode> getChildren ()
  {
    return m_aChildren == null ? null : ContainerHelper.newList (m_aChildren);
  }

  @Override
  @Nullable
  public final IMicroNode getChildAtIndex (@Nonnegative final int nIndex)
  {
    return ContainerHelper.getSafe (m_aChildren, nIndex);
  }

  @Override
  public final int getChildCount ()
  {
    return m_aChildren == null ? 0 : m_aChildren.size ();
  }

  @Override
  @Nullable
  public final IMicroNode getFirstChild ()
  {
    return hasChildren () ? m_aChildren.get (0) : null;
  }

  @Override
  @Nullable
  public final IMicroNode getLastChild ()
  {
    return hasChildren () ? m_aChildren.get (m_aChildren.size () - 1) : null;
  }

  private void _fillListPrefix (@Nonnull final IMicroNode aCurNode, @Nonnull final List <IMicroNode> aNodes)
  {
    if (aCurNode.hasChildren ())
      for (final IMicroNode aCurrent : aCurNode.getChildren ())
      {
        aNodes.add (aCurrent);
        _fillListPrefix (aCurrent, aNodes);
      }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public final List <IMicroNode> getAllChildrenRecursive ()
  {
    final List <IMicroNode> ret = new ArrayList <IMicroNode> ();
    _fillListPrefix (this, ret);
    return ret;
  }

  @OverridingMethodsMustInvokeSuper
  public boolean isEqualContent (final IMicroNode o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractMicroNodeWithChildren rhs = (AbstractMicroNodeWithChildren) o;
    if (m_aChildren == null && rhs.m_aChildren == null)
      return true;
    if (m_aChildren == null || rhs.m_aChildren == null)
      return false;
    final int nMax = m_aChildren.size ();
    final int nMaxRhs = rhs.m_aChildren.size ();
    if (nMax != nMaxRhs)
      return false;
    for (int i = 0; i < nMax; ++i)
      if (!m_aChildren.get (i).isEqualContent (rhs.m_aChildren.get (i)))
        return false;
    return true;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("childrenCount", m_aChildren == null ? 0 : m_aChildren.size ())
                            .toString ();
  }
}
