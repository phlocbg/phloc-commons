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
package com.phloc.commons.microdom;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Represents a single element (=tag) of a document.
 * 
 * @author philip
 */
public interface IMicroElement extends IMicroNodeWithChildren
{
  /**
   * @return <code>true</code> if this element has at least one attribute,
   *         <code>false</code> otherwise
   */
  boolean hasAttributes ();

  /**
   * Check if this element has an attribute with the specified name.
   * 
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @return <code>true</code> if such an attribute is present,
   *         <code>false</code> otherwise
   */
  boolean hasAttribute (@Nullable String sAttrName);

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned.
   * 
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @return The assigned attribute value or <code>null</code>.
   */
  @Nullable
  String getAttribute (@Nullable String sAttrName);

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned. The attribute value is
   * converted via the {@link com.phloc.commons.typeconvert.TypeConverter} to
   * the desired destination class. If no such attribute is present,
   * <code>null</code> is returned.
   * 
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @param aDstClass
   *        The destination class.
   * @return The assigned attribute value or <code>null</code>.
   * @throws ClassCastException
   *         if the value cannot be converted
   */
  @Nullable
  <DSTTYPE> DSTTYPE getAttributeWithConversion (@Nullable String sAttrName, @Nonnull Class <DSTTYPE> aDstClass);

  /**
   * Get an unmodifiable map of all attributes. Is ensured to be not
   * <code>null</code> if {@link #hasAttributes()} returns <code>true</code>.
   * 
   * @return May be <code>null</code>.
   */
  @Nullable
  Map <String, String> getAttributes ();

  /**
   * Set an attribute value of this element.
   * 
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param sAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  IMicroElement setAttribute (@Nonnull String sAttrName, @Nullable String sAttrValue);

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(name, Integer.toString (nValue))</code>.
   * 
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  IMicroElement setAttribute (@Nonnull String sAttrName, @Nullable int nAttrValue);

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(name, Long.toString (nValue))</code>.
   * 
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  IMicroElement setAttribute (@Nonnull String sAttrName, @Nullable long nAttrValue);

  /**
   * Set an attribute value of this element. If the type of the value is not
   * {@link String}, the {@link com.phloc.commons.typeconvert.TypeConverter} is
   * invoked to convert it to a {@link String} object.
   * 
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param aAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  IMicroElement setAttributeWithConversion (@Nonnull String sAttrName, @Nullable Object aAttrValue);

  /**
   * Remove the attribute with the given name.
   * 
   * @param sAttrName
   *        The name of the attribute to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the attribute was removed,
   *         {@link EChange#UNCHANGED} if no such attribute exists at this
   *         element.
   */
  @Nonnull
  EChange removeAttribute (@Nullable String sAttrName);

  /**
   * Remove all attributes from this element
   * 
   * @return {@link EChange}.
   */
  @Nonnull
  EChange removeAllAttributes ();

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
   * Check if this element has the specified namespace URI.
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May not be <code>null</code>.
   * @return <code>true</code> if this element has the specified namespace,
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
   * Get a list of all direct child elements! Text nodes and other other child
   * nodes are not returned with this call!
   * 
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements ();

  /**
   * Get a list of all direct child elements having the specified tag name!
   * 
   * @param sTagName
   *        The tag name to check. May not be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements (@Nullable String sTagName);

  /**
   * Get a list of all direct child elements having the specified namespace and
   * the specified tag name!
   * 
   * @param sNamespaceURI
   *        The namespace URI to check. May not be <code>null</code>.
   * @param sLocalName
   *        The tag name to check. May not be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IMicroElement> getChildElements (@Nullable String sNamespaceURI, @Nullable String sLocalName);

  boolean hasChildElements ();

  boolean hasChildElements (@Nullable String sTagName);

  boolean hasChildElements (@Nullable String sNamespaceURI, @Nullable String sLocalName);

  /**
   * @return The first child element or <code>null</code> if this element has no
   *         child element
   */
  @Nullable
  IMicroElement getFirstChildElement ();

  /**
   * Get the first child element with the given tag name.
   * 
   * @param sTagName
   *        The tag name of the element to search.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  IMicroElement getFirstChildElement (@Nullable String sTagName);

  /**
   * Get the first child element with the given tag name and the given
   * namespace.
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
   * Get the concatenated text content of all direct {@link IMicroText} child
   * nodes of this element.
   * 
   * @return <code>null</code> if the element contains no text node as child
   */
  @Nullable
  String getTextContent ();

  /**
   * Get the concatenated text content of all direct {@link IMicroText} child
   * nodes of this element. The value is converted via the
   * {@link com.phloc.commons.typeconvert.TypeConverter} to the desired
   * destination class.
   * 
   * @return <code>null</code> if the element contains no text node as child
   */
  @Nullable
  <DSTTYPE> DSTTYPE getTextContentWithConversion (@Nonnull Class <DSTTYPE> aDstClass);

  @Nonnull
  IMicroElement getClone ();
}
