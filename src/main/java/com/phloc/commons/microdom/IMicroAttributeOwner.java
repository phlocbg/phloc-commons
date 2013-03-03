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

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Represents methods for objects that have attributes
 * 
 * @author philip
 * @param <IMPLTYPE>
 *        The real implementation type
 */
public interface IMicroAttributeOwner <IMPLTYPE extends IMicroAttributeOwner <IMPLTYPE>>
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
   * @deprecated Use {@link #getAllAttributes()} instead
   */
  @Deprecated
  @Nullable
  @ReturnsMutableCopy
  Map <String, String> getAttributes ();

  /**
   * Get a map of all attribute names and values. Is ensured to be not
   * <code>null</code> if {@link #hasAttributes()} returns <code>true</code>.
   * 
   * @return May be <code>null</code>.
   */
  @Nullable
  @ReturnsMutableCopy
  Map <String, String> getAllAttributes ();

  /**
   * Get a set of all attribute names. Is ensured to be not <code>null</code> if
   * {@link #hasAttributes()} returns <code>true</code>.
   * 
   * @return May be <code>null</code>.
   */
  @Nullable
  @ReturnsMutableCopy
  Set <String> getAllAttributeNames ();

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
  IMPLTYPE setAttribute (@Nonnull String sAttrName, @Nullable String sAttrValue);

  /**
   * Set an attribute value of this element.
   * 
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param aAttrValueProvider
   *        The attribute value provider. May not be <code>null</code>. If the
   *        contained attribute value is <code>null</code> the attribute is
   *        removed (if present)
   * @return this
   */
  @Nonnull
  IMPLTYPE setAttribute (@Nonnull String sAttrName, @Nonnull IHasAttributeValue aAttrValueProvider);

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
  IMPLTYPE setAttribute (@Nonnull String sAttrName, int nAttrValue);

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
  IMPLTYPE setAttribute (@Nonnull String sAttrName, long nAttrValue);

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
  IMPLTYPE setAttributeWithConversion (@Nonnull String sAttrName, @Nullable Object aAttrValue);

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
}
