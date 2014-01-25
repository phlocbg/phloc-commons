/**
 * Copyright (C) 2013-2014 phloc systems
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
package com.phloc.settings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.name.IHasName;

/**
 * Read-only settings
 * 
 * @author philip
 */
public interface IReadonlySettings extends IHasName, IHasSize
{
  /**
   * @return A non-<code>null</code> set of all available field names in any
   *         order.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <String> getAllFieldNames ();

  /**
   * Check if a value is present for the given field name.
   * 
   * @param sFieldName
   *        The field name to check.
   * @return <code>true</code> if any value (even <code>null</code>) is set,
   *         <code>false</code> otherwise.
   */
  boolean containsField (@Nullable String sFieldName);

  /**
   * Get the value associated with the passed field name as an untyped object.
   * 
   * @param sFieldName
   *        The field name to be queried. May be <code>null</code> resulting in
   *        a <code>null</code> return value
   * @return <code>null</code> if no such field exists
   */
  @Nullable
  Object getValue (@Nullable String sFieldName);

  /**
   * Get the value associated with the passed field name with the specified
   * type. Internally the {@link com.phloc.commons.typeconvert.TypeConverter} is
   * used to convert the value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field exists
   * @see #getTypedValue(String, Class, Object)
   */
  @Nullable
  <DATATYPE> DATATYPE getTypedValue (@Nullable String sFieldName, @Nonnull Class <DATATYPE> aDstClass);

  /**
   * Get the value associated with the passed field name with the specified type
   * or the passed default value if no such field exists. Internally the
   * {@link com.phloc.commons.typeconvert.TypeConverter} is used to convert the
   * value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param aDefault
   *        The default value to be returned if the field does not exist. May be
   *        <code>null</code>.
   * @return The passed default value if no such field exists
   * @see #getTypedValue(String, Class)
   */
  @Nullable
  <DATATYPE> DATATYPE getTypedValue (@Nullable String sFieldName,
                                     @Nonnull Class <DATATYPE> aDstClass,
                                     @Nullable DATATYPE aDefault);

  /**
   * Get the value of the passed field as a boolean value.
   * 
   * @param sFieldName
   *        The field name to be retrieved. May be <code>null</code>.
   * @param bDefault
   *        The default value to be returned if no such field exists.
   * @return The associated value or the default value.
   */
  boolean getBooleanValue (@Nullable String sFieldName, boolean bDefault);

  /**
   * Get the value of the passed field as a int value.
   * 
   * @param sFieldName
   *        The field name to be retrieved. May be <code>null</code>.
   * @param nDefault
   *        The default value to be returned if no such field exists.
   * @return The associated value or the default value.
   */
  int getIntValue (@Nullable String sFieldName, int nDefault);

  /**
   * Get the value of the passed field as a long value.
   * 
   * @param sFieldName
   *        The field name to be retrieved. May be <code>null</code>.
   * @param nDefault
   *        The default value to be returned if no such field exists.
   * @return The associated value or the default value.
   */
  long getLongValue (@Nullable String sFieldName, long nDefault);

  /**
   * Get the field value as a {@link BigInteger} value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is present
   * @see #getBigIntegerValue(String, BigInteger)
   */
  @Nullable
  BigInteger getBigIntegerValue (@Nullable String sFieldName);

  /**
   * Get the field value as a {@link BigInteger} value or the passed default
   * value if no such field is present.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param aDefault
   *        The default value to be returned, if no such field is present
   * @return The default value if no such field is present
   * @see #getBigIntegerValue(String)
   */
  @Nullable
  BigInteger getBigIntegerValue (@Nullable String sFieldName, @Nullable BigInteger aDefault);

  /**
   * Get the value of the passed field as a float value.
   * 
   * @param sFieldName
   *        The field name to be retrieved. May be <code>null</code>.
   * @param fDefault
   *        The default value to be returned if no such field exists.
   * @return The associated value or the default value.
   */
  float getFloatValue (@Nullable String sFieldName, float fDefault);

  /**
   * Get the value of the passed field as a double value.
   * 
   * @param sFieldName
   *        The field name to be retrieved. May be <code>null</code>.
   * @param dDefault
   *        The default value to be returned if no such field exists.
   * @return The associated value or the default value.
   */
  double getDoubleValue (@Nullable String sFieldName, double dDefault);

  /**
   * Get the field value as a {@link BigDecimal} value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is present
   * @see #getBigDecimalValue(String, BigDecimal)
   */
  @Nullable
  BigDecimal getBigDecimalValue (@Nullable String sFieldName);

  /**
   * Get the field value as a {@link BigDecimal} value or the passed default
   * value if no such field is present.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param aDefault
   *        The default value to be returned, if no such field is present
   * @return The default value if no such field is present
   * @see #getBigDecimalValue(String)
   */
  @Nullable
  BigDecimal getBigDecimalValue (@Nullable String sFieldName, @Nullable BigDecimal aDefault);

  /**
   * Get the field value as a {@link String} value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is present
   * @see #getStringValue(String, String)
   */
  @Nullable
  String getStringValue (@Nullable String sFieldName);

  /**
   * Get the field value as a {@link String} value or the passed default value
   * if no such field is present.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param sDefault
   *        The default value to be returned, if no such field is present
   * @return The default value if no such field is present
   * @see #getStringValue(String)
   */
  @Nullable
  String getStringValue (@Nullable String sFieldName, @Nullable String sDefault);

  /**
   * Get the field value as a {@link LocalDate} value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is present
   * @see #getDateValue(String, LocalDate)
   */
  @Nullable
  LocalDate getDateValue (@Nullable String sFieldName);

  /**
   * Get the field value as a {@link LocalDate} value or the passed default
   * value if no such field is present.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param aDefault
   *        The default value to be returned, if no such field is present
   * @return The default value if no such field is present
   * @see #getDateValue(String)
   */
  @Nullable
  LocalDate getDateValue (@Nullable String sFieldName, @Nullable LocalDate aDefault);

  /**
   * Get the field value as a {@link LocalTime} value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is present
   * @see #getTimeValue(String, LocalTime)
   */
  @Nullable
  LocalTime getTimeValue (@Nullable String sFieldName);

  /**
   * Get the field value as a {@link LocalTime} value or the passed default
   * value if no such field is present.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param aDefault
   *        The default value to be returned, if no such field is present
   * @return The default value if no such field is present
   * @see #getTimeValue(String)
   */
  @Nullable
  LocalTime getTimeValue (@Nullable String sFieldName, @Nullable LocalTime aDefault);

  /**
   * Get the field value as a {@link DateTime} value.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is present
   * @see #getDateTimeValue(String, DateTime)
   */
  @Nullable
  DateTime getDateTimeValue (@Nullable String sFieldName);

  /**
   * Get the field value as a {@link DateTime} value or the passed default value
   * if no such field is present.
   * 
   * @param sFieldName
   *        The field name to query. May be <code>null</code> resulting in the
   *        passed default value to be the return value.
   * @param aDefault
   *        The default value to be returned, if no such field is present
   * @return The default value if no such field is present
   * @see #getDateTimeValue(String)
   */
  @Nullable
  DateTime getDateTimeValue (@Nullable String sFieldName, @Nullable DateTime aDefault);

  /**
   * Get a nested settings value. This is like retrieving a map.
   * 
   * @param sFieldName
   *        The field to retrieve. May be <code>null</code> resulting in a
   *        <code>null</code> return value.
   * @return <code>null</code> if no such field is available.
   */
  @Nullable
  IReadonlySettings getSettingsValue (@Nullable String sFieldName);
}
