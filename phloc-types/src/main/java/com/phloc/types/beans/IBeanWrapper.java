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
package com.phloc.types.beans;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.ESuccess;

/**
 * Interface for a wrapper class around a Java bean that allows for easy access
 * via reflection.
 * 
 * @author Philip Helger
 */
public interface IBeanWrapper
{
  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  @Nullable
  Object getPropertyValue (@Nonnull String sFieldName);

  /**
   * Get the property value in the desired class. How the conversion happens is
   * implementation dependent.
   * 
   * @param sFieldName
   *        The field name to be retrieved.
   * @param aDstClass
   *        The destination class to be used. May not be <code>null</code>.
   * @return The destination class value.
   * @throws ClassCastException
   *         in case simple casting is used.
   */
  @Nullable
  <DSTTYPE> DSTTYPE getTypedPropertyValue (@Nonnull String sFieldName, @Nonnull Class <DSTTYPE> aDstClass);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  boolean getPropertyValueBoolean (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  byte getPropertyValueByte (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  char getPropertyValueChar (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  double getPropertyValueDouble (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  float getPropertyValueFloat (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  int getPropertyValueInt (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  long getPropertyValueLong (@Nonnull String sFieldName);

  /**
   * @param sFieldName
   *        The field name to be retrieved
   * @return The current object value
   */
  short getPropertyValueShort (@Nonnull String sFieldName);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, @Nullable Object aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, boolean aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, byte aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, char aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, double aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, float aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, int aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, long aValue);

  /**
   * Change the value of a property
   * 
   * @param sFieldName
   *        The field name to be changed
   * @param aValue
   *        The new value
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess setPropertyValue (@Nonnull String sFieldName, short aValue);

  /**
   * Set many property values at once.
   * 
   * @param aProperties
   *        The values to be set. The key is the field name, and the value is
   *        the respective new value.
   * @return {@link ESuccess#SUCCESS} if setting all fields succeeded or
   *         {@link ESuccess#FAILURE} if setting at least one field failed.
   */
  @Nonnull
  ESuccess setPropertyValues (@Nullable Map <String, ?> aProperties);
}
