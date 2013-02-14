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
import com.phloc.commons.microdom.IMicroNodeWithChildren;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
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

  private void _afterRemoveChildOfThis (@Nonnull final IMicroNode aChildNode)
  {
    if (m_aChildren.contains (aChildNode))
      throw new IllegalStateException ("Child " + aChildNode + " is contained more than once in it's parents list");

    if (m_aChildren.isEmpty ())
      m_aChildren = null;
    ((AbstractMicroNode) aChildNode).resetParentNode ();
    onEvent (EMicroEvent.NODE_REMOVED, this, aChildNode);
  }

  @Override
  @Nonnull
  protected final EChange onRemoveChild (@Nonnull final IMicroNode aChildNode)
  {
    if (!aChildNode.hasParent ())
      throw new MicroException ("The passed child node to be removed has no parent!");

    if (m_aChildren == null || !m_aChildren.remove (aChildNode))
      return EChange.UNCHANGED;

    _afterRemoveChildOfThis (aChildNode);
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected final EChange onRemoveChildAtIndex (@Nonnegative final int nIndex)
  {
    // Resolve index - may be invalid
    final IMicroNode aChildNode = getChildAtIndex (nIndex);
    if (aChildNode == null)
      return EChange.UNCHANGED;

    if (!aChildNode.hasParent ())
      throw new MicroException ("Internal inconsistency: the passed child node to be removed has no parent!");

    // Main removal
    if (m_aChildren.remove (nIndex) != aChildNode)
      throw new MicroException ("Internal inconsistency: remove resulted in an illegal object!");

    _afterRemoveChildOfThis (aChildNode);
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected final EChange onRemoveAllChildren ()
  {
    if (m_aChildren == null || m_aChildren.isEmpty ())
      return EChange.UNCHANGED;

    // Trigger the method manually so that all events etc. are fired
    // Don't access m_aChildren directly because it is reset to null in
    // removeChildAtIndex
    while (hasChildren ())
      removeChildAtIndex (0);

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
    final int nChildCount = getChildCount ();
    return nChildCount == 0 ? null : m_aChildren.get (nChildCount - 1);
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

  @Nullable
  public String getTextContent ()
  {
    if (!hasChildren ())
      return null;

    final StringBuilder aSB = new StringBuilder ();
    for (final IMicroNode aChild : directGetChildren ())
      if (aChild.isText ())
      {
        // ignore whitespace-only content
        if (!((IMicroText) aChild).isElementContentWhitespace ())
          aSB.append (aChild.getNodeValue ());
      }
      else
        if (aChild.isCDATA ())
        {
          aSB.append (aChild.getNodeValue ());
        }
        else
          if (aChild instanceof IMicroNodeWithChildren)
          {
            // Recursive call
            final String sTextContent = ((IMicroNodeWithChildren) aChild).getTextContent ();
            if (StringHelper.hasText (sTextContent))
              aSB.append (sTextContent);
          }
    return aSB.toString ();
  }

  @OverridingMethodsMustInvokeSuper
  public boolean isEqualContent (@Nullable final IMicroNode o)
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
