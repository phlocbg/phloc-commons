/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.types.dyntypes;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Interface for handling objects of arbitrary types inspired by
 * {@link java.beans.PropertyEditor} for easy handling of dynamically typed
 * objects incl. their serialization as String.
 * 
 * @author Philip Helger
 */
public interface IDynamicValueEditor
{
  /**
   * Get the property value.
   * 
   * @return The value of the property. Primitive types such as "int" will be
   *         wrapped as the corresponding object type such as
   *         "java.lang.Integer".
   */
  @Nullable
  Object getValue ();

  /**
   * Get the property value and convert it to the desired type using the type
   * converter.
   * 
   * @param aDstClass
   *        , The required destination class. May not be <code>null</code>.
   * @return The value of the property. Primitive types such as "int" will be
   *         wrapped as the corresponding object type such as
   *         "java.lang.Integer".
   * @throws TypeConverterException
   *         if the contained value cannot be converted to the destination class
   */
  @Nullable
  <V> V getCastedValue (@Nonnull Class <V> aDstClass) throws TypeConverterException;

  /**
   * Set (or change) the object that is to be edited. Primitive types such as
   * "int" must be wrapped as the corresponding object type such as
   * "java.lang.Integer".
   * 
   * @param aValue
   *        The new target object.
   * @return {@link EChange}
   * @throws TypeConverterException
   *         If the passed value cannot be converted to the native type.
   */
  @Nonnull
  EChange setValue (@Nullable Object aValue) throws TypeConverterException;

  /**
   * Gets the property value as text.
   * 
   * @return The property value as a string. Returns <code>null</code> if the
   *         value can't be expressed as a string (e.g. because ot is
   *         <code>null</code>).
   */
  @Nullable
  String getAsSerializationText ();

  /**
   * Set the property value by parsing a given String.
   * 
   * @param sText
   *        The string to be parsed.
   * @return {@link ESuccess#SUCCESS} if the parsing was successful, or
   *         {@link ESuccess#FAILURE} if something went wrong.
   */
  @Nonnull
  ESuccess setAsSerializationText (@Nullable String sText);

  /**
   * Gets the property value as display text.
   * 
   * @param aDisplayLocale
   *        The display locale to be used to render this value. May not be
   *        <code>null</code>.
   * @return The property value as a user-friendly string or <code>null</code>
   *         if the input value is <code>null</code>.
   */
  @Nullable
  String getAsDisplayText (@Nonnull Locale aDisplayLocale);
}
