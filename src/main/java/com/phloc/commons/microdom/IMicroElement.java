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
package com.phloc.commons.microdom;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Represents a single element (=tag) of a document.
 * 
 * @author philip
 */
public interface IMicroElement extends IMicroNodeWithChildren, IMicroAttributeOwner <IMicroElement>
{
  /**
   * Get the namespace URI of this element
   * 
   * @return May be <code>null</code> if this element has no namespace URI.
   */
  @Nullable
  String getNamespaceURI ();

  /**
   * Set a new namespace URI for this element.
   * 
   * @param sNamespaceURI
   *        The namespace URI to be set. May be <code>null</code> or empty to
   *        indicate that the namespace should be removed.
   * @return {@link EChange}
   */
  @Nonnull
  EChange setNamespaceURI (@Nullable String sNamespaceURI);

  /**
   * Check if this element has a specified namespace URI.
   * 
   * @return <code>true</code> if this element has a specified namespace URI,
   *         <code>false</code> otherwise
   */
  boolean hasNamespaceURI ();

  /**
   * Check if this element has no namespace URI.
   * 
   * @return <code>true</code> if this element has no namespace URI,
   *         <code>false</code> otherwise
   */
  boolean hasNoNamespaceURI ();

  /**
   * Check if this element has the specified namespace URI.
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May not be <code>null</code>.
   * @return <code>true</code> if this element has the specified namespace URI,
   *         <code>false</code> otherwise
   */
  boolean hasNamespaceURI (@Nullable String sNamespaceURI);

  /**
   * Get the local name of the element. Is the same name as returned by
   * {@link #getTagName()} but it is only present, if a namespace URI is
   * present.
   * 
   * @return May be <code>null</code> if no namespace is present.
   */
  @Nullable
  String getLocalName ();

  /**
   * Get the name of the tag. It never contains XML schema prefixes or the like.
   * Is the same as {@link #getLocalName()} if a namespace URI is present.
   * 
   * @return May not be <code>null</code>.
   */
  @Nonnull
  String getTagName ();

  /**
   * Get a list of all direct child elements. Text nodes and other other child
   * nodes are not returned with this call. Micro container children are
   * inlined.
   * 
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements ();

  /**
   * Get a list of all direct child elements having the specified tag name.
   * Micro container children are inlined.
   * 
   * @param sTagName
   *        The tag name to check. May be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements (@Nullable String sTagName);

  /**
   * Get a list of all direct child elements having the specified namespace and
   * the specified tag name. Micro container children are inlined.
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May be <code>null</code>.
   * @param sLocalName
   *        The tag name to check. May be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements (@Nullable String sNamespaceURI, @Nullable String sLocalName);

  /**
   * Get a list of all direct child elements having the specified tag name.
   * Micro container children are inlined.
   * 
   * @param aElementNameProvider
   *        Element name provider. May not be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements (@Nonnull IHasElementName aElementNameProvider);

  /**
   * Get a list of all direct child elements having the specified namespace and
   * the specified tag name. Micro container children are inlined.
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May be <code>null</code>.
   * @param aElementNameProvider
   *        Element name provider. May not be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements (@Nullable String sNamespaceURI, @Nonnull IHasElementName aElementNameProvider);

  /**
   * Recursively get all child elements. Micro container children are inlined.
   * 
   * @return A list containing all recursively contained elements. May not be
   *         <code>null</code> but empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getAllChildElementsRecursive ();

  /**
   * Check if this element has at least one child element. Micro container
   * children are also checked.
   * 
   * @return <code>true</code> if this element has at least one child element
   */
  boolean hasChildElements ();

  /**
   * Check if this element has at least one child element with the specified tag
   * name. Micro container children are also checked.
   * 
   * @param sTagName
   *        The tag name to check. May be <code>null</code>.
   * @return <code>true</code> if this element has at least one child element
   *         with the specified tag name
   */
  boolean hasChildElements (@Nullable String sTagName);

  /**
   * Check if this element has at least one child element with the specified
   * namespace URI and tag name. Micro container children are also checked.
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May be <code>null</code>.
   * @param sLocalName
   *        The tag name to check. May be <code>null</code>.
   * @return <code>true</code> if this element has at least one child element
   *         with the specified namespace URI and tag name
   */
  boolean hasChildElements (@Nullable String sNamespaceURI, @Nullable String sLocalName);

  /**
   * Check if this element has at least one child element with the specified tag
   * name. Micro container children are also checked.
   * 
   * @param aElementNameProvider
   *        Element name provider. May not be <code>null</code>.
   * @return <code>true</code> if this element has at least one child element
   *         with the specified tag name
   */
  boolean hasChildElements (@Nonnull IHasElementName aElementNameProvider);

  /**
   * Check if this element has at least one child element with the specified
   * namespace URI and tag name. Micro container children are also checked.
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May be <code>null</code>.
   * @param aElementNameProvider
   *        Element name provider. May not be <code>null</code>.
   * @return <code>true</code> if this element has at least one child element
   *         with the specified namespace URI and tag name
   */
  boolean hasChildElements (@Nullable String sNamespaceURI, @Nonnull IHasElementName aElementNameProvider);

  /**
   * Get the first child element of this element. Micro container children are
   * also checked.
   * 
   * @return The first child element or <code>null</code> if this element has no
   *         child element.
   */
  @Nullable
  IMicroElement getFirstChildElement ();

  /**
   * Get the first child element with the given tag name. Micro container
   * children are also checked.
   * 
   * @param sTagName
   *        The tag name of the element to search. May be <code>null</code>.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  IMicroElement getFirstChildElement (@Nullable String sTagName);

  /**
   * Get the first child element with the given tag name and the given
   * namespace. Micro container children are also checked.
   * 
   * @param sNamespaceURI
   *        The namespace URL to search.
   * @param sLocalName
   *        The tag name of the element to search.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  IMicroElement getFirstChildElement (@Nullable String sNamespaceURI, @Nullable String sLocalName);

  /**
   * Get the first child element with the given tag name. Micro container
   * children are also checked.
   * 
   * @param aElementNameProvider
   *        Element name provider. May not be <code>null</code>.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  IMicroElement getFirstChildElement (@Nonnull IHasElementName aElementNameProvider);

  /**
   * Get the first child element with the given tag name and the given
   * namespace. Micro container children are also checked.
   * 
   * @param sNamespaceURI
   *        The namespace URL to search.
   * @param aElementNameProvider
   *        Element name provider. May not be <code>null</code>.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  IMicroElement getFirstChildElement (@Nullable String sNamespaceURI, @Nonnull IHasElementName aElementNameProvider);

  /**
   * {@inheritDoc}
   */
  @Nonnull
  IMicroElement getClone ();
}
