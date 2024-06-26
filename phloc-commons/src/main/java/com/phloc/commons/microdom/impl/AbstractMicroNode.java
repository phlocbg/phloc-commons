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
package com.phloc.commons.microdom.impl;

import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.EMicroEvent;
import com.phloc.commons.microdom.IHasElementName;
import com.phloc.commons.microdom.IMicroCDATA;
import com.phloc.commons.microdom.IMicroComment;
import com.phloc.commons.microdom.IMicroContainer;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroEntityReference;
import com.phloc.commons.microdom.IMicroEvent;
import com.phloc.commons.microdom.IMicroEventTarget;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroProcessingInstruction;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * This is an abstract base class for the micro document object model. It
 * implements a set of common methods required for all object types. Especially
 * for the parent/child handling, the sub class AbstractMicroNodeWithChildren
 * provides some additional features.
 * 
 * @author Philip Helger
 */
public abstract class AbstractMicroNode implements IMicroNode
{
  /** The parent node of this node. */
  private AbstractMicroNodeWithChildren m_aParentNode;
  private Map <EMicroEvent, Set <IMicroEventTarget>> m_aEventTargets;

  @Override
  @OverrideOnDemand
  public String getNodeValue ()
  {
    return "";
  }

  /**
   * Callback that is invoked once a child is to be appended.
   * 
   * @param aChildNode
   *        The appended child node.
   */
  @OverrideOnDemand
  protected void onAppendChild (@Nonnull final AbstractMicroNode aChildNode)
  {
    throw new MicroException ("Cannot append children in class " + getClass ().getName ());
  }

  /**
   * Callback that is invoked once a child is to be inserted before another
   * child.
   * 
   * @param aChildNode
   *        The new child node to be inserted.
   * @param aSuccessor
   *        The node before which the new node will be inserted.
   */
  @OverrideOnDemand
  protected void onInsertBefore (@Nonnull final AbstractMicroNode aChildNode, @Nonnull final IMicroNode aSuccessor)
  {
    throw new MicroException ("Cannot insert children in class " + getClass ().getName ());
  }

  /**
   * Callback that is invoked once a child is to be inserted after another
   * child.
   * 
   * @param aChildNode
   *        The new child node to be inserted.
   * @param aPredecessor
   *        The node after which the new node will be inserted.
   */
  @OverrideOnDemand
  protected void onInsertAfter (@Nonnull final AbstractMicroNode aChildNode, @Nonnull final IMicroNode aPredecessor)
  {
    throw new MicroException ("Cannot insert children in class " + getClass ().getName ());
  }

  /**
   * Callback that is invoked once a child is to be inserted at the specified
   * index.
   * 
   * @param nIndex
   *        The index where the node should be inserted.
   * @param aChildNode
   *        The new child node to be inserted.
   */
  @OverrideOnDemand
  protected void onInsertAtIndex (@Nonnegative final int nIndex, @Nonnull final AbstractMicroNode aChildNode)
  {
    throw new MicroException ("Cannot insert children in class " + getClass ().getName ());
  }

  @Override
  @Nullable
  public final <NODETYPE extends IMicroNode> NODETYPE appendChild (@Nullable final NODETYPE aChildNode)
  {
    if (aChildNode != null)
      onAppendChild ((AbstractMicroNode) aChildNode);
    return aChildNode;
  }

  @Override
  @Nullable
  public final <NODETYPE extends IMicroNode> NODETYPE insertBefore (@Nullable final NODETYPE aChildNode,
                                                                    @Nonnull final IMicroNode aSuccessor)
  {
    if (aChildNode != null)
      onInsertBefore ((AbstractMicroNode) aChildNode, aSuccessor);
    return aChildNode;
  }

  @Override
  @Nullable
  public final <NODETYPE extends IMicroNode> NODETYPE insertAfter (@Nullable final NODETYPE aChildNode,
                                                                   @Nonnull final IMicroNode aPredecessor)
  {
    if (aChildNode != null)
      onInsertAfter ((AbstractMicroNode) aChildNode, aPredecessor);
    return aChildNode;
  }

  @Override
  @Nullable
  public final <NODETYPE extends IMicroNode> NODETYPE insertAtIndex (@Nonnegative final int nIndex,
                                                                     @Nullable final NODETYPE aChildNode)
  {
    if (aChildNode != null)
      onInsertAtIndex (nIndex, (AbstractMicroNode) aChildNode);
    return aChildNode;
  }

  @Override
  @Nonnull
  public final IMicroText appendText (@Nullable final CharSequence sText)
  {
    final MicroText aNode = new MicroText (sText, false);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroText appendText (@Nonnull final char [] aChars)
  {
    return appendText (aChars, 0, aChars.length);
  }

  @Override
  @Nonnull
  public final IMicroText appendText (@Nonnull final char [] aChars,
                                      @Nonnegative final int nOfs,
                                      @Nonnegative final int nLen)
  {
    final MicroText aNode = new MicroText (aChars, nOfs, nLen, false);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroText appendTextWithConversion (@Nullable final Object aValue)
  {
    // Throws IlliegalArgumentException when no conversion is available
    final String sValue = TypeConverter.convertIfNecessary (aValue, String.class);
    return appendText (sValue);
  }

  @Override
  @Nonnull
  public final IMicroText appendIgnorableWhitespaceText (@Nullable final CharSequence sText)
  {
    final MicroText aNode = new MicroText (sText, true);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroText appendIgnorableWhitespaceText (@Nonnull final char [] aChars)
  {
    return appendIgnorableWhitespaceText (aChars, 0, aChars.length);
  }

  @Override
  @Nonnull
  public final IMicroText appendIgnorableWhitespaceText (@Nonnull final char [] aChars,
                                                         @Nonnegative final int nOfs,
                                                         @Nonnegative final int nLen)
  {
    final MicroText aNode = new MicroText (aChars, nOfs, nLen, true);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroCDATA appendCDATA (@Nullable final CharSequence sText)
  {
    final MicroCDATA aNode = new MicroCDATA (sText);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroCDATA appendCDATA (@Nonnull final char [] aChars)
  {
    return appendCDATA (aChars, 0, aChars.length);
  }

  @Override
  @Nonnull
  public final IMicroCDATA appendCDATA (@Nonnull final char [] aChars,
                                        @Nonnegative final int nOfs,
                                        @Nonnegative final int nLen)
  {
    final MicroCDATA aNode = new MicroCDATA (aChars, nOfs, nLen);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroCDATA appendCDATAWithConversion (@Nullable final Object aValue)
  {
    // Throws IlliegalArgumentException when no conversion is available
    final String sValue = TypeConverter.convertIfNecessary (aValue, String.class);
    return appendCDATA (sValue);
  }

  @Override
  @Nonnull
  public final IMicroComment appendComment (@Nullable final CharSequence sText)
  {
    final MicroComment aNode = new MicroComment (sText);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroComment appendComment (@Nonnull final char [] aChars)
  {
    return appendComment (aChars, 0, aChars.length);
  }

  @Override
  @Nonnull
  public final IMicroComment appendComment (@Nonnull final char [] aChars,
                                            @Nonnegative final int nOfs,
                                            @Nonnegative final int nLen)
  {
    final MicroComment aNode = new MicroComment (aChars, nOfs, nLen);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroComment appendCommentWithConversion (@Nullable final Object aValue)
  {
    // Throws IlliegalArgumentException when no conversion is available
    final String sValue = TypeConverter.convertIfNecessary (aValue, String.class);
    return appendComment (sValue);
  }

  @Override
  @Nonnull
  public final IMicroEntityReference appendEntityReference (@Nonnull @Nonempty final String sName)
  {
    final MicroEntityReference aNode = new MicroEntityReference (sName);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroElement appendElement (@Nonnull @Nonempty final String sTagName)
  {
    return appendElement (null, sTagName);
  }

  @Override
  @Nonnull
  public final IMicroElement appendElement (@Nullable final String sNamespaceURI,
                                            @Nonnull @Nonempty final String sTagName)
  {
    final MicroElement aNode = new MicroElement (sNamespaceURI, sTagName);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroElement appendElement (@Nonnull final IHasElementName aElementNameProvider)
  {
    return appendElement (null, aElementNameProvider);
  }

  @Override
  @Nonnull
  public final IMicroElement appendElement (@Nullable final String sNamespaceURI,
                                            @Nonnull final IHasElementName aElementNameProvider)
  {
    ValueEnforcer.notNull (aElementNameProvider, "ElementNameProvider");

    return appendElement (sNamespaceURI, aElementNameProvider.getElementName ());
  }

  @Override
  @Nonnull
  public final IMicroProcessingInstruction appendProcessingInstruction (@Nonnull @Nonempty final String sTarget,
                                                                        @Nullable final String sData)
  {
    final MicroProcessingInstruction aNode = new MicroProcessingInstruction (sTarget, sData);
    onAppendChild (aNode);
    return aNode;
  }

  @Override
  @Nonnull
  public final IMicroContainer appendContainer ()
  {
    final MicroContainer aNode = new MicroContainer ();
    onAppendChild (aNode);
    return aNode;
  }

  /**
   * Callback when a child is removed.
   * 
   * @param aChild
   *        The child that is removed.
   * @return {@link EChange#CHANGED} if something changed
   */
  @OverrideOnDemand
  @Nonnull
  protected EChange onRemoveChild (final IMicroNode aChild)
  {
    throw new MicroException ("Cannot remove child from this node: " + getClass ().getName ());
  }

  @Override
  @Nonnull
  public final EChange removeChild (@Nonnull final IMicroNode aChild)
  {
    ValueEnforcer.notNull (aChild, "Child");
    return onRemoveChild (aChild);
  }

  /**
   * Remove the child not at the specified index.
   * 
   * @param nIndex
   *        The 0-based index of the item to be removed.
   * @return {@link EChange#CHANGED} if the node was successfully removed,
   *         {@link EChange#UNCHANGED} otherwise.
   */
  @OverrideOnDemand
  @Nonnull
  protected EChange onRemoveChildAtIndex (final int nIndex)
  {
    throw new MicroException ("Cannot remove child from this node: " + getClass ().getName ());
  }

  @Override
  @Nonnull
  public final EChange removeChildAtIndex (@Nonnegative final int nIndex)
  {
    return onRemoveChildAtIndex (nIndex);
  }

  /**
   * Remove all children from this node.
   * 
   * @return {@link EChange#CHANGED} if at least one child was present, and was
   *         successfully removed, {@link EChange#UNCHANGED} otherwise.
   */
  @OverrideOnDemand
  @Nonnull
  protected EChange onRemoveAllChildren ()
  {
    throw new MicroException ("Cannot remove all children from this node: " + getClass ().getName ());
  }

  @Override
  @Nonnull
  public final EChange removeAllChildren ()
  {
    return onRemoveAllChildren ();
  }

  @Override
  @Nonnull
  public final EChange replaceChild (@Nonnull final IMicroNode aOldChild, @Nonnull final IMicroNode aNewChild)
  {
    ValueEnforcer.notNull (aOldChild, "OldChild");
    ValueEnforcer.notNull (aNewChild, "NewChild");

    if (aOldChild.equals (aNewChild))
      return EChange.UNCHANGED;
    insertBefore (aNewChild, aOldChild);
    removeChild (aOldChild);
    return EChange.CHANGED;
  }

  @Override
  @OverrideOnDemand
  public boolean hasChildren ()
  {
    return false;
  }

  @Override
  @OverrideOnDemand
  @Nullable
  public List <IMicroNode> getChildren ()
  {
    return null;
  }

  @Override
  @OverrideOnDemand
  @Nullable
  public IMicroNode getChildAtIndex (@Nonnegative final int nIndex)
  {
    return null;
  }

  @Override
  @OverrideOnDemand
  @Nonnegative
  public int getChildCount ()
  {
    return 0;
  }

  @Override
  @OverrideOnDemand
  @Nullable
  public IMicroNode getFirstChild ()
  {
    return null;
  }

  @Override
  @OverrideOnDemand
  @Nullable
  public IMicroNode getLastChild ()
  {
    return null;
  }

  @Override
  @OverrideOnDemand
  @Nullable
  public List <IMicroNode> getAllChildrenRecursive ()
  {
    return null;
  }

  @Override
  @Nullable
  public final IMicroNode getPreviousSibling ()
  {
    if (this.m_aParentNode == null)
      return null;
    final List <IMicroNode> aParentChildren = this.m_aParentNode.directGetChildren ();
    final int nIndex = aParentChildren.indexOf (this);
    if (nIndex == -1)
      throw new IllegalStateException ("this is no part of it's parents children");
    return ContainerHelper.getSafe (aParentChildren, nIndex - 1);
  }

  @Override
  @Nullable
  public final IMicroNode getNextSibling ()
  {
    if (this.m_aParentNode == null)
      return null;
    final List <IMicroNode> aParentChildren = this.m_aParentNode.directGetChildren ();
    final int nIndex = aParentChildren.indexOf (this);
    if (nIndex == -1)
      throw new IllegalStateException ("this is no part of it's parents children");
    return ContainerHelper.getSafe (aParentChildren, nIndex + 1);
  }

  @Override
  public final boolean hasParent ()
  {
    return this.m_aParentNode != null;
  }

  @Override
  @Nullable
  public final IMicroNode getParent ()
  {
    return this.m_aParentNode;
  }

  final void resetParentNode ()
  {
    this.m_aParentNode = null;
  }

  final void setParentNode (@Nonnull final AbstractMicroNodeWithChildren aParentNode)
  {
    if (aParentNode == null)
      throw new MicroException ("No parent node passed!");
    if (aParentNode == this)
      throw new MicroException ("Node cannot have itself as parent: " + toString ());
    if (this.m_aParentNode != null)
      throw new MicroException ("Node already has a parent: " + toString ());
    this.m_aParentNode = aParentNode;
  }

  @Override
  @Nonnull
  public final IMicroNode detachFromParent ()
  {
    if (this.m_aParentNode != null)
    {
      if (this.m_aParentNode.removeChild (this).isUnchanged ())
        throw new IllegalStateException ("Failed to remove this from parents child list");
      resetParentNode ();
    }
    return this;
  }

  @Override
  @Nullable
  public final IMicroElement getParentElementWithName (@Nullable final String sTagName)
  {
    IMicroNode aParent = this.m_aParentNode;
    while (aParent != null && aParent.isElement ())
    {
      final IMicroElement aParentElement = (IMicroElement) aParent;
      if (aParentElement.getTagName ().equals (sTagName))
        return aParentElement;
      aParent = aParent.getParent ();
    }
    return null;
  }

  @Override
  @Nullable
  public final IMicroElement getParentElementWithName (@Nullable final String sNamespaceURI,
                                                       @Nullable final String sTagName)
  {
    IMicroNode aParent = this.m_aParentNode;
    while (aParent != null && aParent.isElement ())
    {
      final IMicroElement aParentElement = (IMicroElement) aParent;
      if (aParentElement.hasNamespaceURI (sNamespaceURI) && aParentElement.getTagName ().equals (sTagName))
        return aParentElement;
      aParent = aParent.getParent ();
    }
    return null;
  }

  /*
   * Note: the implementations with "this instanceof IMicroXXX" is faster than
   * doing either "getType ().equals (EMicroNodeType....)" and faster than
   * having "return false;" in here and "return true;" in the respective
   * implementation classes.
   */

  @Override
  public final boolean isDocument ()
  {
    return this instanceof IMicroDocument;
  }

  @Override
  public final boolean isDocumentType ()
  {
    return this instanceof IMicroDocumentType;
  }

  @Override
  public final boolean isText ()
  {
    return this instanceof IMicroText;
  }

  @Override
  public final boolean isCDATA ()
  {
    return this instanceof IMicroCDATA;
  }

  @Override
  public final boolean isComment ()
  {
    return this instanceof IMicroComment;
  }

  @Override
  public final boolean isEntityReference ()
  {
    return this instanceof IMicroEntityReference;
  }

  @Override
  public final boolean isElement ()
  {
    return this instanceof IMicroElement;
  }

  @Override
  public final boolean isProcessingInstruction ()
  {
    return this instanceof IMicroProcessingInstruction;
  }

  @Override
  public final boolean isContainer ()
  {
    return this instanceof IMicroContainer;
  }

  final void internalTriggerEvent (@Nonnull final EMicroEvent eEventType, @Nonnull final IMicroEvent aEvent)
  {
    // Any event targets present?
    if (this.m_aEventTargets != null && !this.m_aEventTargets.isEmpty ())
    {
      // Get all event handler
      final Set <IMicroEventTarget> aTargets = this.m_aEventTargets.get (eEventType);
      if (aTargets != null && !aTargets.isEmpty ())
      {
        // fire the event
        for (final IMicroEventTarget aTarget : aTargets)
          aTarget.handleEvent (aEvent);
      }
    }

    // Bubble to parent
    if (this.m_aParentNode != null)
      this.m_aParentNode.internalTriggerEvent (eEventType, aEvent);
  }

  /**
   * Handles a micro event
   * 
   * @param eEventType
   *        Type of the event
   * @param aSourceNode
   *        Source node
   * @param aTargetNode
   *        Target node
   */
  protected final void onEvent (@Nonnull final EMicroEvent eEventType,
                                @Nonnull final IMicroNode aSourceNode,
                                @Nonnull final IMicroNode aTargetNode)
  {
    // Create the event only once
    internalTriggerEvent (eEventType, new MicroEvent (eEventType, aSourceNode, aTargetNode));
  }

  @Override
  @Nonnull
  public EChange registerEventTarget (@Nonnull final EMicroEvent eEventType, @Nonnull final IMicroEventTarget aTarget)
  {
    ValueEnforcer.notNull (eEventType, "EventType");
    ValueEnforcer.notNull (aTarget, "EventTarget");

    if (this.m_aEventTargets == null)
      this.m_aEventTargets = new EnumMap <EMicroEvent, Set <IMicroEventTarget>> (EMicroEvent.class);
    Set <IMicroEventTarget> aSet = this.m_aEventTargets.get (eEventType);
    if (aSet == null)
    {
      aSet = new LinkedHashSet <IMicroEventTarget> ();
      this.m_aEventTargets.put (eEventType, aSet);
    }
    return EChange.valueOf (aSet.add (aTarget));
  }

  @Override
  @Nonnull
  public EChange unregisterEventTarget (@Nonnull final EMicroEvent eEventType, @Nonnull final IMicroEventTarget aTarget)
  {
    ValueEnforcer.notNull (eEventType, "EventType");
    ValueEnforcer.notNull (aTarget, "EventTarget");

    if (this.m_aEventTargets != null && !this.m_aEventTargets.isEmpty ())
    {
      final Set <IMicroEventTarget> aSet = this.m_aEventTargets.get (eEventType);
      if (aSet != null && !aSet.isEmpty ())
        return EChange.valueOf (aSet.remove (aTarget));
    }
    return EChange.UNCHANGED;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("parentNodeName",
                                                         this.m_aParentNode == null ? null
                                                                                    : this.m_aParentNode.getNodeName ())
                                       .appendIfNotNull ("eventTargets", this.m_aEventTargets)
                                       .toString ();
  }
}
