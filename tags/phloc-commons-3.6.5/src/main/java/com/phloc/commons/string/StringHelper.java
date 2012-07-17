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
package com.phloc.commons.string;//NOPMD

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.CheckForSigned;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.math.MathHelper;

/**
 * Generic string transformation and helper methods. If you need to modify a
 * string, start looking in this class.
 * 
 * @author philip
 */
@Immutable
public final class StringHelper
{
  public static final int DEFAULT_RADIX = 10;

  private static final int [] s_aSizeTableInt = { 9,
                                                 99,
                                                 999,
                                                 9999,
                                                 99999,
                                                 999999,
                                                 9999999,
                                                 99999999,
                                                 999999999,
                                                 Integer.MAX_VALUE };
  private static final long [] s_aSizeTableLong = { 9L,
                                                   99L,
                                                   999L,
                                                   9999L,
                                                   99999L,
                                                   999999L,
                                                   9999999L,
                                                   99999999L,
                                                   999999999L,
                                                   9999999999L,
                                                   99999999999L,
                                                   999999999999L,
                                                   9999999999999L,
                                                   99999999999999L,
                                                   999999999999999L,
                                                   9999999999999999L,
                                                   99999999999999999L,
                                                   999999999999999999L,
                                                   Long.MAX_VALUE };
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final StringHelper s_aInstance = new StringHelper ();

  private StringHelper ()
  {}

  /**
   * Try to interpret the passed object as boolean. This works only if the
   * passed object is either a {@link String} or a {@link Boolean}.
   * 
   * @param aObject
   *        The object to be interpreted. May be <code>null</code>.
   * @return <code>false</code> if the passed object cannot be interpreted as a
   *         boolean.
   */
  public static boolean parseBool (@Nullable final Object aObject)
  {
    return parseBool (aObject, false);
  }

  /**
   * Try to interpret the passed object as boolean. This works only if the
   * passed object is either a {@link String} or a {@link Boolean}.
   * 
   * @param aObject
   *        The object to be interpreted. May be <code>null</code>.
   * @param bDefault
   *        The default value to be returned, if the object cannot be
   *        interpreted.
   * @return The boolean representation or the default value if the passed
   *         object cannot be interpreted as a boolean.
   */
  public static boolean parseBool (@Nullable final Object aObject, final boolean bDefault)
  {
    if (aObject instanceof Boolean)
      return ((Boolean) aObject).booleanValue ();
    if (aObject instanceof String)
      return parseBool ((String) aObject);
    return bDefault;
  }

  /**
   * Parse the given {@link String} as boolean value. All values that are equal
   * to "true" (ignoring case) will result in <code>true</code> return values.
   * All other values result in <code>false</code> return values.
   * 
   * @param sStr
   *        The string to be interpreted. May be <code>null</code>.
   * @return <code>true</code> if the passed string matches "true" (ignoring
   *         case), <code>false</code> otherwise.
   * @see Boolean#parseBoolean(String)
   */
  public static boolean parseBool (@Nullable final String sStr)
  {
    return Boolean.parseBoolean (sStr);
  }

  /**
   * Try to interpret the passed object as boolean. This works only if the
   * passed object is either a {@link String} or a {@link Boolean}.
   * 
   * @param aObject
   *        The object to be interpreted. May be <code>null</code>.
   * @return <code>null</code> if the passed object cannot be interpreted as a
   *         boolean, any other {@link Boolean} otherwise.
   */
  @Nullable
  public static Boolean parseBoolObj (@Nullable final Object aObject)
  {
    return parseBoolObj (aObject, null);
  }

  /**
   * Try to interpret the passed object as boolean. This works only if the
   * passed object is either a {@link String} or a {@link Boolean}.
   * 
   * @param aObject
   *        The object to be interpreted. May be <code>null</code>.
   * @param aDefault
   *        The default value to be returned, if the passed string cannot be
   *        interpreted. May be <code>null</code>.
   * @return The passed default value if the passed object cannot be interpreted
   *         as a boolean, any other {@link Boolean} otherwise.
   */
  @Nullable
  public static Boolean parseBoolObj (@Nullable final Object aObject, @Nullable final Boolean aDefault)
  {
    if (aObject instanceof Boolean)
      return (Boolean) aObject;
    if (aObject instanceof String)
      return parseBoolObj ((String) aObject);
    return aDefault;
  }

  @Nonnull
  public static Boolean parseBoolObj (@Nullable final String sStr)
  {
    return Boolean.valueOf (sStr);
  }

  /**
   * Parse the given String as byte with radix 10.
   * 
   * @param aObject
   *        The Object to parse. May be <code>null</code>.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the string does not represent a valid value.
   */
  public static byte parseByte (@Nullable final Object aObject, final byte nDefault)
  {
    return parseByte (aObject, DEFAULT_RADIX, nDefault);
  }

  /**
   * Parse the given String as byte.
   * 
   * @param aObject
   *        The Object to parse. May be <code>null</code>.
   * @param nRadix
   *        The radix to use.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the string does not represent a valid value.
   */
  public static byte parseByte (@Nullable final Object aObject, final int nRadix, final byte nDefault)
  {
    if (aObject instanceof Number)
      return ((Number) aObject).byteValue ();
    return parseByte (String.valueOf (aObject), nRadix, nDefault);
  }

  /**
   * Parse the given String as byte with radix 10.
   * 
   * @param sStr
   *        The String to parse. May be <code>null</code>.
   * @param nDefault
   *        The value to be returned if the string cannot be converted to a
   *        valid value.
   * @return The passed default parameter if the string does not represent a
   *         valid value.
   */
  public static byte parseByte (@Nullable final String sStr, final byte nDefault)
  {
    return parseByte (sStr, DEFAULT_RADIX, nDefault);
  }

  /**
   * Parse the given String as byte.
   * 
   * @param sStr
   *        The String to parse. May be <code>null</code>.
   * @param nRadix
   *        The radix to use.
   * @param nDefault
   *        The value to be returned if the string cannot be converted to a
   *        valid value.
   * @return The passed default parameter if the string does not represent a
   *         valid value.
   */
  public static byte parseByte (@Nullable final String sStr, final int nRadix, final byte nDefault)
  {
    if (sStr != null)
      try
      {
        return Byte.parseByte (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return nDefault;
  }

  /**
   * Parse the given object as {@link Byte}.
   * 
   * @param aObject
   *        The object to parse.
   * @return <code>null</code> if the object does not represent a valid value.
   */
  @Nullable
  public static Byte parseByteObj (@Nullable final Object aObject)
  {
    return parseByteObj (aObject, null);
  }

  @Nullable
  public static Byte parseByteObj (@Nullable final Object aObject, @Nullable final Byte aDefault)
  {
    return parseByteObj (aObject, DEFAULT_RADIX, aDefault);
  }

  @Nullable
  public static Byte parseByteObj (@Nullable final Object aObject, final int nRadix, @Nullable final Byte aDefault)
  {
    if (aObject == null)
      return aDefault;
    if (aObject instanceof Number)
      return Byte.valueOf (((Number) aObject).byteValue ());
    return parseByteObj (String.valueOf (aObject), nRadix, aDefault);
  }

  /**
   * Parse the given String as {@link Byte}.
   * 
   * @param sStr
   *        The String to parse.
   * @return <code>null</code> if the string does not represent a valid value.
   */
  @Nullable
  public static Byte parseByteObj (@Nullable final String sStr)
  {
    return parseByteObj (sStr, null);
  }

  /**
   * Parse the given String as {@link Byte} with radix 10.
   * 
   * @param sStr
   *        The String to parse.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static Byte parseByteObj (@Nullable final String sStr, @Nullable final Byte aDefault)
  {
    return parseByteObj (sStr, DEFAULT_RADIX, aDefault);
  }

  /**
   * Parse the given String as {@link Byte}.
   * 
   * @param sStr
   *        The String to parse.
   * @param nRadix
   *        The radix to use.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static Byte parseByteObj (@Nullable final String sStr, final int nRadix, @Nullable final Byte aDefault)
  {
    if (sStr != null)
      try
      {
        return Byte.valueOf (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return aDefault;
  }

  /**
   * Parse the given String as int with radix 10.
   * 
   * @param aObject
   *        The Object to parse. May be <code>null</code>.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the string does not represent a valid value.
   */
  public static int parseInt (@Nullable final Object aObject, final int nDefault)
  {
    return parseInt (aObject, DEFAULT_RADIX, nDefault);
  }

  /**
   * Parse the given String as int.
   * 
   * @param aObject
   *        The Object to parse. May be <code>null</code>.
   * @param nRadix
   *        The radix to use.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the string does not represent a valid value.
   */
  public static int parseInt (@Nullable final Object aObject, final int nRadix, final int nDefault)
  {
    if (aObject instanceof Number)
      return ((Number) aObject).intValue ();
    return parseInt (String.valueOf (aObject), nRadix, nDefault);
  }

  /**
   * Parse the given String as int with radix 10.
   * 
   * @param sStr
   *        The String to parse. May be <code>null</code>.
   * @param nDefault
   *        The value to be returned if the string cannot be converted to a
   *        valid value.
   * @return The passed default parameter if the string does not represent a
   *         valid value.
   */
  public static int parseInt (@Nullable final String sStr, final int nDefault)
  {
    return parseInt (sStr, DEFAULT_RADIX, nDefault);
  }

  /**
   * Parse the given String as int.
   * 
   * @param sStr
   *        The String to parse. May be <code>null</code>.
   * @param nRadix
   *        The radix to use.
   * @param nDefault
   *        The value to be returned if the string cannot be converted to a
   *        valid value.
   * @return The passed default parameter if the string does not represent a
   *         valid value.
   */
  public static int parseInt (@Nullable final String sStr, final int nRadix, final int nDefault)
  {
    if (sStr != null)
      try
      {
        return Integer.parseInt (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return nDefault;
  }

  /**
   * Parse the given object as integer.
   * 
   * @param aObject
   *        The object to parse.
   * @return <code>null</code> if the object does not represent a valid value.
   */
  @Nullable
  public static Integer parseIntObj (@Nullable final Object aObject)
  {
    return parseIntObj (aObject, null);
  }

  @Nullable
  public static Integer parseIntObj (@Nullable final Object aObject, @Nullable final Integer aDefault)
  {
    return parseIntObj (aObject, DEFAULT_RADIX, aDefault);
  }

  @Nullable
  public static Integer parseIntObj (@Nullable final Object aObject, final int nRadix, @Nullable final Integer aDefault)
  {
    if (aObject == null)
      return aDefault;
    if (aObject instanceof Number)
      return Integer.valueOf (((Number) aObject).intValue ());
    return parseIntObj (String.valueOf (aObject), nRadix, aDefault);
  }

  /**
   * Parse the given String as integer.
   * 
   * @param sStr
   *        The String to parse.
   * @return <code>null</code> if the string does not represent a valid value.
   */
  @Nullable
  public static Integer parseIntObj (@Nullable final String sStr)
  {
    return parseIntObj (sStr, null);
  }

  /**
   * Parse the given String as integer with radix 10.
   * 
   * @param sStr
   *        The String to parse.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static Integer parseIntObj (@Nullable final String sStr, @Nullable final Integer aDefault)
  {
    return parseIntObj (sStr, DEFAULT_RADIX, aDefault);
  }

  /**
   * Parse the given String as integer.
   * 
   * @param sStr
   *        The String to parse.
   * @param nRadix
   *        The radix to use.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static Integer parseIntObj (@Nullable final String sStr, final int nRadix, @Nullable final Integer aDefault)
  {
    if (sStr != null)
      try
      {
        return Integer.valueOf (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return aDefault;
  }

  /**
   * Parse the given object as long with radix 10.
   * 
   * @param aObject
   *        The object to parse.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the object does not represent a valid value.
   */
  public static long parseLong (@Nullable final Object aObject, final long nDefault)
  {
    return parseLong (aObject, DEFAULT_RADIX, nDefault);
  }

  /**
   * Parse the given object as long.
   * 
   * @param aObject
   *        The object to parse.
   * @param nRadix
   *        The radix to use.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the object does not represent a valid value.
   */
  public static long parseLong (@Nullable final Object aObject, final int nRadix, final long nDefault)
  {
    if (aObject == null)
      return nDefault;
    if (aObject instanceof Number)
      return ((Number) aObject).longValue ();
    return parseLong (String.valueOf (aObject), nRadix, nDefault);
  }

  /**
   * Parse the given String as long with radix 10.
   * 
   * @param sStr
   *        The string to parse.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default if the string does not represent a valid value.
   */
  public static long parseLong (@Nullable final String sStr, final long nDefault)
  {
    return parseLong (sStr, DEFAULT_RADIX, nDefault);
  }

  /**
   * Parse the given String as long.
   * 
   * @param sStr
   *        The string to parse.
   * @param nRadix
   *        The radix to use.
   * @param nDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default if the string does not represent a valid value.
   */
  public static long parseLong (@Nullable final String sStr, final int nRadix, final long nDefault)
  {
    if (sStr != null)
      try
      {
        return Long.parseLong (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return nDefault;
  }

  @Nullable
  public static Long parseLongObj (@Nullable final Object aObject)
  {
    return parseLongObj (aObject, null);
  }

  @Nullable
  public static Long parseLongObj (@Nullable final Object aObject, @Nullable final Long aDefault)
  {
    return parseLongObj (aObject, DEFAULT_RADIX, aDefault);
  }

  @Nullable
  public static Long parseLongObj (@Nullable final Object aObject, final int nRadix, @Nullable final Long aDefault)
  {
    if (aObject == null)
      return aDefault;
    if (aObject instanceof Number)
      return Long.valueOf (((Number) aObject).longValue ());
    return parseLongObj (String.valueOf (aObject), nRadix, aDefault);
  }

  /**
   * Parse the given String as long.
   * 
   * @param sStr
   *        The string to parse.
   * @return <code>null</code> if the string does not represent a valid value.
   */
  @Nullable
  public static Long parseLongObj (@Nullable final String sStr)
  {
    return parseLongObj (sStr, null);
  }

  @Nullable
  public static Long parseLongObj (@Nullable final String sStr, @Nullable final Long aDefault)
  {
    return parseLongObj (sStr, DEFAULT_RADIX, aDefault);
  }

  @Nullable
  public static Long parseLongObj (@Nullable final String sStr, final int nRadix, @Nullable final Long aDefault)
  {
    if (sStr != null)
      try
      {
        return Long.valueOf (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return aDefault;
  }

  /**
   * Parse the given object as double. Note: both the locale independent form of
   * a double can be parsed here (e.g. 4.523) as well as a localized form using
   * the comma as the decimal separator (e.g. the German 4,523).
   * 
   * @param aObject
   *        The object to parse.
   * @param dDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the object does not represent a valid value.
   */
  public static double parseDouble (@Nullable final Object aObject, final double dDefault)
  {
    if (aObject == null)
      return dDefault;
    if (aObject instanceof Number)
      return ((Number) aObject).doubleValue ();
    return parseDouble (String.valueOf (aObject), dDefault);
  }

  /**
   * Parse the given string as double.
   * 
   * @param sStr
   *        The string to parse.
   * @param dDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the string does not represent a valid value.
   */
  public static double parseDouble (@Nullable final String sStr, final double dDefault)
  {
    // parseDouble throws a NPE if parameter is null
    if (sStr != null && sStr.length () > 0)
      try
      {
        // Single point where we replace "," with "." for parsing!
        return Double.parseDouble (replaceAll (sStr, ',', '.'));
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return dDefault;
  }

  @Nullable
  public static Double parseDoubleObj (@Nullable final Object aObject)
  {
    return parseDoubleObj (aObject, null);
  }

  @Nullable
  public static Double parseDoubleObj (@Nullable final Object aObject, @Nullable final Double aDefault)
  {
    final double dValue = parseDouble (aObject, Double.NaN);
    return Double.isNaN (dValue) ? aDefault : Double.valueOf (dValue);
  }

  @Nullable
  public static Double parseDoubleObj (@Nullable final String sStr)
  {
    return parseDoubleObj (sStr, null);
  }

  @Nullable
  public static Double parseDoubleObj (@Nullable final String sStr, @Nullable final Double aDefault)
  {
    final double dValue = parseDouble (sStr, Double.NaN);
    return Double.isNaN (dValue) ? aDefault : Double.valueOf (dValue);
  }

  /**
   * Parse the given object as float. Note: both the locale independent form of
   * a float can be parsed here (e.g. 4.523) as well as a localized form using
   * the comma as the decimal separator (e.g. the German 4,523).
   * 
   * @param aObject
   *        The object to parse.
   * @param fDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the object does not represent a valid value.
   */
  public static float parseFloat (@Nullable final Object aObject, final float fDefault)
  {
    if (aObject == null)
      return fDefault;
    if (aObject instanceof Number)
      return ((Number) aObject).floatValue ();
    return parseFloat (String.valueOf (aObject), fDefault);
  }

  /**
   * Parse the given string as float.
   * 
   * @param sStr
   *        The string to parse.
   * @param fDefault
   *        The default value to be returned if the passed object could not be
   *        converted to a valid value.
   * @return The default value if the string does not represent a valid value.
   */
  public static float parseFloat (@Nullable final String sStr, final float fDefault)
  {
    // parseDouble throws a NPE if parameter is null
    if (sStr != null && sStr.length () > 0)
      try
      {
        // Single point where we replace "," with "." for parsing!
        return Float.parseFloat (replaceAll (sStr, ',', '.'));
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return fDefault;
  }

  @Nullable
  public static Float parseFloatObj (@Nullable final Object aObject)
  {
    return parseFloatObj (aObject, null);
  }

  @Nullable
  public static Float parseFloatObj (@Nullable final Object aObject, @Nullable final Float aDefault)
  {
    final float fValue = parseFloat (aObject, Float.NaN);
    return Float.isNaN (fValue) ? aDefault : Float.valueOf (fValue);
  }

  @Nullable
  public static Float parseFloatObj (@Nullable final String sStr)
  {
    return parseFloatObj (sStr, null);
  }

  @Nullable
  public static Float parseFloatObj (@Nullable final String sStr, @Nullable final Float aDefault)
  {
    final float fValue = parseFloat (sStr, Float.NaN);
    return Float.isNaN (fValue) ? aDefault : Float.valueOf (fValue);
  }

  /**
   * Parse the given String as {@link BigInteger} with the default radix
   * {@value #DEFAULT_RADIX}.
   * 
   * @param sStr
   *        The String to parse.
   * @return <code>null</code> if the string does not represent a valid value.
   */
  @Nullable
  public static BigInteger parseBigInteger (@Nullable final String sStr)
  {
    return parseBigInteger (sStr, DEFAULT_RADIX, null);
  }

  /**
   * Parse the given String as {@link BigInteger}.
   * 
   * @param sStr
   *        The String to parse.
   * @param nRadix
   *        The radix to use.
   * @return <code>null</code> if the string does not represent a valid value.
   */
  @Nullable
  public static BigInteger parseBigInteger (@Nullable final String sStr, @Nonnegative final int nRadix)
  {
    return parseBigInteger (sStr, nRadix, null);
  }

  /**
   * Parse the given String as {@link BigInteger} with the default radix
   * {@value #DEFAULT_RADIX}.
   * 
   * @param sStr
   *        The String to parse.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static BigInteger parseBigInteger (@Nullable final String sStr, @Nullable final BigInteger aDefault)
  {
    return parseBigInteger (sStr, DEFAULT_RADIX, aDefault);
  }

  /**
   * Parse the given String as {@link BigInteger}.
   * 
   * @param sStr
   *        The String to parse.
   * @param nRadix
   *        The radix to use.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static BigInteger parseBigInteger (@Nullable final String sStr,
                                            @Nonnegative final int nRadix,
                                            @Nullable final BigInteger aDefault)
  {
    if (sStr != null)
      try
      {
        return new BigInteger (sStr, nRadix);
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return aDefault;
  }

  /**
   * Parse the given String as {@link BigDecimal}.
   * 
   * @param sStr
   *        The String to parse.
   * @return <code>null</code> if the string does not represent a valid value.
   */
  @Nullable
  public static BigDecimal parseBigDecimal (@Nullable final String sStr)
  {
    return parseBigDecimal (sStr, null);
  }

  /**
   * Parse the given String as {@link BigDecimal}.
   * 
   * @param sStr
   *        The String to parse.
   * @param aDefault
   *        The default value to be returned if the passed string could not be
   *        converted to a valid value.
   * @return <code>aDefault</code> if the string does not represent a valid
   *         value.
   */
  @Nullable
  public static BigDecimal parseBigDecimal (@Nullable final String sStr, @Nullable final BigDecimal aDefault)
  {
    if (sStr != null)
      try
      {
        return new BigDecimal (sStr.replace (',', '.'));
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // Fall through
      }
    return aDefault;
  }

  /**
   * Checks if the given string is a numeric string that can be converted to a
   * long value with radix 10.
   * 
   * @param sStr
   *        the string to check
   * @return <code>true</code> if the value can be converted to a valid value
   */
  public static boolean isInt (@Nullable final String sStr)
  {
    if (sStr != null)
      try
      {
        Integer.parseInt (sStr);
        return true;
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // fall through
      }
    return false;
  }

  /**
   * Checks if the given string is a numeric string that can be converted to a
   * long value with radix 10.
   * 
   * @param sStr
   *        the string to check
   * @return <code>true</code> if the value can be converted to a valid value
   */
  public static boolean isLong (@Nullable final String sStr)
  {
    if (sStr != null)
      try
      {
        Long.parseLong (sStr);
        return true;
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // fall through
      }
    return false;
  }

  /**
   * Checks if the given string is a numeric string that can be converted to an
   * unsigned long value with radix 10.
   * 
   * @param sStr
   *        the string to check
   * @return <code>true</code> if the value can be converted to a valid value
   */
  public static boolean isUnsignedInt (@Nullable final String sStr)
  {
    if (sStr != null)
      try
      {
        final int ret = Integer.parseInt (sStr);
        return ret >= 0;
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // fall through
      }
    return false;
  }

  /**
   * Checks if the given string is a numeric string that can be converted to an
   * unsigned long value with radix 10.
   * 
   * @param sStr
   *        the string to check
   * @return <code>true</code> if the value can be converted to a valid value
   */
  public static boolean isUnsignedLong (@Nullable final String sStr)
  {
    if (sStr != null)
      try
      {
        final long ret = Long.parseLong (sStr);
        return ret >= 0;
      }
      catch (final NumberFormatException ex)// NOPMD
      {
        // fall through
      }
    return false;
  }

  /**
   * Checks if the given string is a double string that can be converted to a
   * double value.
   * 
   * @param sStr
   *        the string to check
   * @return <code>true</code> if the value can be converted to a valid value
   */
  public static boolean isDouble (@Nullable final String sStr)
  {
    return !Double.isNaN (parseDouble (sStr, Double.NaN));
  }

  /**
   * Check if the string is <code>null</code> or empty.
   * 
   * @param aCS
   *        The string to check. May be <code>null</code>.
   * @return <code>true</code> if the string is <code>null</code> or empty
   */
  public static boolean hasNoText (@Nullable final CharSequence aCS)
  {
    return aCS == null || aCS.length () == 0;
  }

  /**
   * Check if the string is <code>null</code> or empty.
   * 
   * @param aCS
   *        The string to check. May be <code>null</code>.
   * @return <code>true</code> if the string is <code>null</code> or empty
   */
  public static boolean isEmpty (@Nullable final CharSequence aCS)
  {
    return hasNoText (aCS);
  }

  /**
   * Check if the string is <code>null</code> or empty after trimming.
   * 
   * @param s
   *        The string to check. May be <code>null</code>.
   * @return <code>true</code> if the string is <code>null</code> or empty or
   *         consists only of whitespaces.
   */
  public static boolean hasNoTextAfterTrim (@Nullable final String s)
  {
    return s == null || s.trim ().length () == 0;
  }

  /**
   * Check if the string is <code>null</code> or empty after trimming.
   * 
   * @param s
   *        The string to check. May be <code>null</code>.
   * @return <code>true</code> if the string is <code>null</code> or empty or
   *         consists only of whitespaces.
   */
  public static boolean isEmptyAfterTrim (@Nullable final String s)
  {
    return hasNoTextAfterTrim (s);
  }

  /**
   * Check if the string contains any char.
   * 
   * @param aCS
   *        The string to check
   * @return true if the string contains at least one char public static boolean
   *         hasText (@Nullable final CharSequence s)
   */
  public static boolean hasText (@Nullable final CharSequence aCS)
  {
    return aCS != null && aCS.length () > 0;
  }

  /**
   * Check if the string contains any char.
   * 
   * @param aCS
   *        The string to check
   * @return true if the string contains at least one char public static boolean
   *         hasText (@Nullable final CharSequence s)
   */
  public static boolean isNotEmpty (@Nullable final CharSequence aCS)
  {
    return hasText (aCS);
  }

  /**
   * Check if the string neither <code>null</code> nor empty after trimming.
   * 
   * @param s
   *        The string to check. May be <code>null</code>.
   * @return <code>true</code> if the string is neither <code>null</code> nor
   *         empty nor consists only of whitespaces.
   */
  public static boolean hasTextAfterTrim (@Nullable final String s)
  {
    return s != null && s.trim ().length () > 0;
  }

  /**
   * Check if the string neither <code>null</code> nor empty after trimming.
   * 
   * @param s
   *        The string to check. May be <code>null</code>.
   * @return <code>true</code> if the string is neither <code>null</code> nor
   *         empty nor consists only of whitespaces.
   */
  public static boolean isNotEmptyAfterTrim (@Nullable final String s)
  {
    return hasTextAfterTrim (s);
  }

  /**
   * @deprecated Use {@link #getLeadingZero(Byte,int)} instead
   */
  @Deprecated
  @Nullable
  public static String leadingZero (@Nullable final Byte aValue, final int nChars)
  {
    return getLeadingZero (aValue, nChars);
  }

  @Nullable
  public static String getLeadingZero (@Nullable final Byte aValue, final int nChars)
  {
    return aValue == null ? null : getLeadingZero (aValue.byteValue (), nChars);
  }

  /**
   * @deprecated Use {@link #getLeadingZero(Integer,int)} instead
   */
  @Deprecated
  @Nullable
  public static String leadingZero (@Nullable final Integer aValue, final int nChars)
  {
    return getLeadingZero (aValue, nChars);
  }

  @Nullable
  public static String getLeadingZero (@Nullable final Integer aValue, final int nChars)
  {
    return aValue == null ? null : getLeadingZero (aValue.longValue (), nChars);
  }

  /**
   * @deprecated Use {@link #getLeadingZero(Long,int)} instead
   */
  @Deprecated
  @Nullable
  public static String leadingZero (@Nullable final Long aValue, final int nChars)
  {
    return getLeadingZero (aValue, nChars);
  }

  @Nullable
  public static String getLeadingZero (@Nullable final Long aValue, final int nChars)
  {
    return aValue == null ? null : getLeadingZero (aValue.longValue (), nChars);
  }

  /**
   * @deprecated Use {@link #getLeadingZero(Short,int)} instead
   */
  @Deprecated
  @Nullable
  public static String leadingZero (@Nullable final Short aValue, final int nChars)
  {
    return getLeadingZero (aValue, nChars);
  }

  @Nullable
  public static String getLeadingZero (@Nullable final Short aValue, final int nChars)
  {
    return aValue == null ? null : getLeadingZero (aValue.shortValue (), nChars);
  }

  /**
   * @deprecated Use {@link #getLeadingZero(int,int)} instead
   */
  @Deprecated
  @Nonnull
  public static String leadingZero (final int nValue, final int nChars)
  {
    return getLeadingZero (nValue, nChars);
  }

  @Nonnull
  public static String getLeadingZero (final int nValue, final int nChars)
  {
    final boolean bNeg = nValue < 0;
    final String sValue = Integer.toString (MathHelper.abs (nValue));
    if (sValue.length () >= nChars)
      return bNeg ? '-' + sValue : sValue;

    // prepend '0's
    final StringBuilder aSB = new StringBuilder ((bNeg ? 1 : 0) + nChars);
    if (bNeg)
      aSB.append ('-');
    for (int i = 0; i < nChars - sValue.length (); ++i)
      aSB.append ('0');
    return aSB.append (sValue).toString ();
  }

  /**
   * @deprecated Use {@link #getLeadingZero(long,int)} instead
   */
  @Deprecated
  @Nonnull
  public static String leadingZero (final long nValue, final int nChars)
  {
    return getLeadingZero (nValue, nChars);
  }

  @Nonnull
  public static String getLeadingZero (final long nValue, final int nChars)
  {
    final boolean bNeg = nValue < 0;
    final String sValue = Long.toString (MathHelper.abs (nValue));
    if (sValue.length () >= nChars)
      return bNeg ? '-' + sValue : sValue;

    // prepend '0's
    final StringBuilder aSB = new StringBuilder ((bNeg ? 1 : 0) + nChars);
    if (bNeg)
      aSB.append ('-');
    for (int i = 0; i < nChars - sValue.length (); ++i)
      aSB.append ('0');
    return aSB.append (sValue).toString ();
  }

  /**
   * @deprecated Use {@link #getLeadingZero(String,int)} instead
   */
  @Deprecated
  @Nonnull
  public static String leadingZero (@Nonnull final String sValue, final int nChars)
  {
    return getLeadingZero (sValue, nChars);
  }

  @Nonnull
  public static String getLeadingZero (@Nonnull final String sValue, final int nChars)
  {
    final int nLen = sValue.length ();
    if (nLen >= nChars)
      return sValue;

    // prepend '0's
    final StringBuilder aSB = new StringBuilder (nChars);
    for (int i = 0; i < nChars - nLen; ++i)
      aSB.append ('0');
    return aSB.append (sValue).toString ();
  }

  /**
   * Get the matching hex digit.
   * 
   * @param n
   *        The value to get the hex digit from. Must be between 0 and 15.
   * @return The hex character (one of 0-9 or a-f), or '\0' if the value could
   *         not be converted
   */
  public static char getHexChar (final int n)
  {
    return Character.forDigit (n, CGlobal.HEX_RADIX);
  }

  /**
   * Convert a byte array to a hexadecimal encoded string.
   * 
   * @param aInput
   *        The byte array to be converted to a String. May not be
   *        <code>null</code>.
   * @return The String representation of the byte array.
   * @deprecated Use {@link #getHexEncoded(byte[])} instead
   */
  @Deprecated
  @Nonnull
  public static String hexEncode (@Nonnull final byte [] aInput)
  {
    return getHexEncoded (aInput);
  }

  /**
   * Convert a byte array to a hexadecimal encoded string.
   * 
   * @param aInput
   *        The byte array to be converted to a String. May not be
   *        <code>null</code>.
   * @return The String representation of the byte array.
   */
  @Nonnull
  public static String getHexEncoded (@Nonnull final byte [] aInput)
  {
    if (aInput == null)
      throw new NullPointerException ("input");

    return getHexEncoded (aInput, 0, aInput.length);
  }

  /**
   * Convert a byte array to a hexadecimal encoded string.
   * 
   * @param aInput
   *        The byte array to be converted to a String. May not be
   *        <code>null</code>.
   * @param nOfs
   *        Byte array offset
   * @param nLen
   *        Number of bytes to encode
   * @return The String representation of the byte array.
   * @deprecated Use {@link #getHexEncoded(byte[],int,int)} instead
   */
  @Deprecated
  @Nonnull
  public static String hexEncode (@Nonnull final byte [] aInput, final int nOfs, final int nLen)
  {
    return getHexEncoded (aInput, nOfs, nLen);
  }

  /**
   * Convert a byte array to a hexadecimal encoded string.
   * 
   * @param aInput
   *        The byte array to be converted to a String. May not be
   *        <code>null</code>.
   * @param nOfs
   *        Byte array offset
   * @param nLen
   *        Number of bytes to encode
   * @return The String representation of the byte array.
   */
  @Nonnull
  public static String getHexEncoded (@Nonnull final byte [] aInput, final int nOfs, final int nLen)
  {
    if (aInput == null)
      throw new NullPointerException ("input");
    if (nOfs < 0 || nLen < 0 || (nOfs + nLen) > aInput.length)
      throw new IllegalArgumentException ("ofs:" + nOfs + ";len=" + nLen + ";bufLen=" + aInput.length);

    final StringBuilder aSB = new StringBuilder (nLen * 2);
    for (int i = nOfs; i < (nOfs + nLen); ++i)
    {
      final byte b = aInput[i];
      final char c1 = getHexChar ((b & 0xf0) >> 4);
      final char c2 = getHexChar (b & 0x0f);
      aSB.append (c1).append (c2);
    }
    return aSB.toString ();
  }

  /**
   * Get the decimal value of the passed hex character
   * 
   * @param c
   *        The hex char to convert
   * @return A value between 0 and 15, or -1 if the input character is not a hex
   *         char!
   */
  @CheckForSigned
  public static int getHexValue (@Nonnegative final char c)
  {
    return Character.digit (c, CGlobal.HEX_RADIX);
  }

  /**
   * @param cHigh
   *        High hex part
   * @param cLow
   *        Low hex part
   * @return A value between 0 and 255, or -1 if any input character is not a
   *         hex char!
   */
  public static int getHexByte (@Nonnegative final char cHigh, @Nonnegative final char cLow)
  {
    final int nHex1 = getHexValue (cHigh);
    final int nHex2 = getHexValue (cLow);
    return nHex1 < 0 || nHex2 < 0 ? CGlobal.ILLEGAL_UINT : (nHex1 << 4) | nHex2;
  }

  /**
   * @deprecated Use {@link #getHexDecoded(String)} instead
   */
  @Deprecated
  @Nonnull
  public static byte [] hexDecode (@Nonnull final String sInput)
  {
    return getHexDecoded (sInput);
  }

  @Nonnull
  public static byte [] getHexDecoded (@Nonnull final String sInput)
  {
    if (sInput == null)
      throw new NullPointerException ("input");
    return getHexDecoded (sInput.toCharArray ());
  }

  @Nonnull
  public static byte [] getHexDecoded (@Nonnull final char [] aInput)
  {
    if (aInput == null)
      throw new NullPointerException ("input");
    if ((aInput.length % 2) > 0)
      throw new IllegalArgumentException ("Passed chars have no even length: " + aInput.length);

    final byte [] ret = new byte [aInput.length / 2];
    int nRetIdx = 0;
    for (int i = 0; i < aInput.length; i += 2)
    {
      final char c0 = aInput[i];
      final char c1 = aInput[i + 1];
      final int nHexByte = getHexByte (c0, c1);
      if (nHexByte == -1)
        throw new IllegalArgumentException ("Failed to convert '" + c0 + "' or '" + c1 + "' to a hex value!");
      ret[nRetIdx++] = (byte) nHexByte;
    }
    return ret;
  }

  /**
   * @deprecated Use {@link #getHexString(byte)} instead
   */
  @Deprecated
  @Nonnull
  public static String hexString (final byte nValue)
  {
    return getHexString (nValue);
  }

  @Nonnull
  public static String getHexString (final byte nValue)
  {
    return Integer.toString (nValue & 0xff, CGlobal.HEX_RADIX);
  }

  /**
   * @deprecated Use {@link #getHexStringLeadingZero(byte,int)} instead
   */
  @Deprecated
  public static String hexStringLeadingZero (final byte nValue, final int nDigits)
  {
    return getHexStringLeadingZero (nValue, nDigits);
  }

  public static String getHexStringLeadingZero (final byte nValue, final int nDigits)
  {
    return getLeadingZero (getHexString (nValue), nDigits);
  }

  /**
   * @deprecated Use {@link #getHexString(int)} instead
   */
  @Deprecated
  public static String hexString (final int nValue)
  {
    return getHexString (nValue);
  }

  public static String getHexString (final int nValue)
  {
    return Integer.toString (nValue, CGlobal.HEX_RADIX);
  }

  /**
   * @deprecated Use {@link #getHexStringLeadingZero(int,int)} instead
   */
  @Deprecated
  public static String hexStringLeadingZero (final int nValue, final int nDigits)
  {
    return getHexStringLeadingZero (nValue, nDigits);
  }

  public static String getHexStringLeadingZero (final int nValue, final int nDigits)
  {
    return getLeadingZero (getHexString (nValue), nDigits);
  }

  /**
   * @deprecated Use {@link #getHexString(long)} instead
   */
  @Deprecated
  public static String hexString (final long nValue)
  {
    return getHexString (nValue);
  }

  public static String getHexString (final long nValue)
  {
    return Long.toString (nValue, CGlobal.HEX_RADIX);
  }

  /**
   * @deprecated Use {@link #getHexStringLeadingZero(long,int)} instead
   */
  @Deprecated
  public static String hexStringLeadingZero (final long nValue, final int nDigits)
  {
    return getHexStringLeadingZero (nValue, nDigits);
  }

  public static String getHexStringLeadingZero (final long nValue, final int nDigits)
  {
    return getLeadingZero (getHexString (nValue), nDigits);
  }

  /**
   * @deprecated Use {@link #getHexString(short)} instead
   */
  @Deprecated
  public static String hexString (final short nValue)
  {
    return getHexString (nValue);
  }

  public static String getHexString (final short nValue)
  {
    return Integer.toString (nValue & 0xffff, CGlobal.HEX_RADIX);
  }

  /**
   * @deprecated Use {@link #getHexStringLeadingZero(short,int)} instead
   */
  @Deprecated
  public static String hexStringLeadingZero (final short nValue, final int nDigits)
  {
    return getHexStringLeadingZero (nValue, nDigits);
  }

  public static String getHexStringLeadingZero (final short nValue, final int nDigits)
  {
    return getLeadingZero (getHexString (nValue), nDigits);
  }

  @Nonnegative
  public static int getLeadingWhitespaceCount (@Nullable final String s)
  {
    int ret = 0;
    if (s != null)
    {
      final int nMax = s.length ();
      while (ret < nMax && Character.isWhitespace (s.charAt (ret)))
        ++ret;
    }
    return ret;
  }

  @Nonnegative
  public static int getTrailingWhitespaceCount (@Nullable final String s)
  {
    int ret = 0;
    if (s != null)
    {
      int nLast = s.length () - 1;
      while (nLast >= 0 && Character.isWhitespace (s.charAt (nLast)))
      {
        ++ret;
        --nLast;
      }
    }
    return ret;
  }

  @Nonnegative
  public static int getLeadingCharCount (@Nullable final String s, final char c)
  {
    int ret = 0;
    if (s != null)
    {
      final int nMax = s.length ();
      while (ret < nMax && s.charAt (ret) == c)
        ++ret;
    }
    return ret;
  }

  @Nonnegative
  public static int getTrailingCharCount (@Nullable final String s, final char c)
  {
    int ret = 0;
    if (s != null)
    {
      int nLast = s.length () - 1;
      while (nLast >= 0 && s.charAt (nLast) == c)
      {
        ++ret;
        --nLast;
      }
    }
    return ret;
  }

  /**
   * Get a concatenated String from all elements of the passed container,
   * separated by the specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param aElements
   *        The container to convert. May be <code>null</code> or empty.
   * @return The concatenated string.
   * @deprecated Use {@link #getImploded(String,Iterable)} instead
   */
  @Deprecated
  @Nonnull
  public static String implode (@Nonnull final String sSep, @Nullable final Iterable <?> aElements)
  {
    return getImploded (sSep, aElements);
  }

  /**
   * Get a concatenated String from all elements of the passed container,
   * separated by the specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param aElements
   *        The container to convert. May be <code>null</code> or empty.
   * @return The concatenated string.
   */
  @Nonnull
  public static String getImploded (@Nonnull final String sSep, @Nullable final Iterable <?> aElements)
  {
    if (sSep == null)
      throw new NullPointerException ("separator");

    final StringBuilder aSB = new StringBuilder ();
    if (aElements != null)
    {
      int nIndex = 0;
      for (final Object sElement : aElements)
      {
        if (nIndex++ > 0)
          aSB.append (sSep);
        aSB.append (sElement);
      }
    }
    return aSB.toString ();
  }

  /**
   * Get a concatenated String from all elements of the passed map, separated by
   * the specified separator string.
   * 
   * @param sSepOuter
   *        The separator to use for separating the map entries. May not be
   *        <code>null</code>.
   * @param sSepInner
   *        The separator to use for separating the key from the value. May not
   *        be <code>null</code>.
   * @param aElements
   *        The map to convert. May be <code>null</code> or empty.
   * @return The concatenated string.
   * @deprecated Use {@link #getImploded(String,String,Map)} instead
   */
  @Deprecated
  @Nonnull
  public static String implode (@Nonnull final String sSepOuter,
                                @Nonnull final String sSepInner,
                                @Nullable final Map <?, ?> aElements)
  {
    return getImploded (sSepOuter, sSepInner, aElements);
  }

  /**
   * Get a concatenated String from all elements of the passed map, separated by
   * the specified separator string.
   * 
   * @param sSepOuter
   *        The separator to use for separating the map entries. May not be
   *        <code>null</code>.
   * @param sSepInner
   *        The separator to use for separating the key from the value. May not
   *        be <code>null</code>.
   * @param aElements
   *        The map to convert. May be <code>null</code> or empty.
   * @return The concatenated string.
   */
  @Nonnull
  public static String getImploded (@Nonnull final String sSepOuter,
                                    @Nonnull final String sSepInner,
                                    @Nullable final Map <?, ?> aElements)
  {
    if (sSepOuter == null)
      throw new NullPointerException ("outerSeparator");
    if (sSepInner == null)
      throw new NullPointerException ("innerSeparator");

    final StringBuilder aSB = new StringBuilder ();
    if (aElements != null)
    {
      int nIndex = 0;
      for (final Map.Entry <?, ?> aElement : aElements.entrySet ())
      {
        if (nIndex++ > 0)
          aSB.append (sSepOuter);
        aSB.append (aElement.getKey ()).append (sSepInner).append (aElement.getValue ());
      }
    }
    return aSB.toString ();
  }

  /**
   * Get a concatenated String from all elements of the passed array, separated
   * by the specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param aElements
   *        The container to convert. May be <code>null</code> or empty.
   * @return The concatenated string.
   * @param <ELEMENTTYPE>
   *        The type of elements to be imploded.
   * @deprecated Use {@link #getImploded(String,ELEMENTTYPE...)} instead
   */
  @Deprecated
  @Nonnull
  public static <ELEMENTTYPE> String implode (@Nonnull final String sSep, @Nullable final ELEMENTTYPE... aElements)
  {
    return getImploded (sSep, aElements);
  }

  /**
   * Get a concatenated String from all elements of the passed array, separated
   * by the specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param aElements
   *        The container to convert. May be <code>null</code> or empty.
   * @return The concatenated string.
   * @param <ELEMENTTYPE>
   *        The type of elements to be imploded.
   */
  @Nonnull
  public static <ELEMENTTYPE> String getImploded (@Nonnull final String sSep, @Nullable final ELEMENTTYPE... aElements)
  {
    if (sSep == null)
      throw new NullPointerException ("separator");
    if (aElements == null)
      return "";
    return getImploded (sSep, aElements, 0, aElements.length);
  }

  /**
   * Get a concatenated String from all elements of the passed array, separated
   * by the specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param aElements
   *        The container to convert. May be <code>null</code> or empty.
   * @param nOfs
   *        The offset to start from.
   * @param nLen
   *        The number of elements to implode.
   * @return The concatenated string.
   * @param <ELEMENTTYPE>
   *        The type of elements to be imploded.
   * @deprecated Use {@link #getImploded(String,ELEMENTTYPE[],int,int)} instead
   */
  @Deprecated
  @Nonnull
  public static <ELEMENTTYPE> String implode (@Nonnull final String sSep,
                                              @Nullable final ELEMENTTYPE [] aElements,
                                              @Nonnegative final int nOfs,
                                              @Nonnegative final int nLen)
  {
    return getImploded (sSep, aElements, nOfs, nLen);
  }

  /**
   * Get a concatenated String from all elements of the passed array, separated
   * by the specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param aElements
   *        The container to convert. May be <code>null</code> or empty.
   * @param nOfs
   *        The offset to start from.
   * @param nLen
   *        The number of elements to implode.
   * @return The concatenated string.
   * @param <ELEMENTTYPE>
   *        The type of elements to be imploded.
   */
  @Nonnull
  public static <ELEMENTTYPE> String getImploded (@Nonnull final String sSep,
                                                  @Nullable final ELEMENTTYPE [] aElements,
                                                  @Nonnegative final int nOfs,
                                                  @Nonnegative final int nLen)
  {
    if (sSep == null)
      throw new NullPointerException ("separator");
    if (nOfs < 0 || nLen < 0 || (aElements != null && (nOfs + nLen) > aElements.length))
      throw new IllegalArgumentException ("ofs:" + nOfs + ";len=" + nLen + ";bufLen=" + ArrayHelper.getSize (aElements));

    final StringBuilder aSB = new StringBuilder ();
    if (aElements != null)
    {
      for (int i = nOfs; i < nOfs + nLen; ++i)
      {
        final ELEMENTTYPE sElement = aElements[i];
        if (i > nOfs)
          aSB.append (sSep);
        aSB.append (sElement);
      }
    }
    return aSB.toString ();
  }

  @Nonnull
  private static <COLLTYPE extends Collection <String>> COLLTYPE _getExploded (@Nonnull final String sSep,
                                                                               @Nullable final String sElements,
                                                                               @Nonnull final COLLTYPE aCollection)
  {
    if (sSep == null)
      throw new NullPointerException ("separator");

    if (hasText (sElements))
    {
      // Do not use RegExPool.stringReplacePattern because of package
      // dependencies
      // Do not use String.split because it trims empty tokens from the end
      int nStartIndex = 0;
      int nMatchIndex;
      while ((nMatchIndex = sElements.indexOf (sSep, nStartIndex)) >= 0)
      {
        aCollection.add (sElements.substring (nStartIndex, nMatchIndex));
        nStartIndex = nMatchIndex + sSep.length ();
      }
      aCollection.add (sElements.substring (nStartIndex));
    }
    return aCollection;
  }

  /**
   * Take a concatenated String and return a {@link List} of all elements in the
   * passed string, using specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param sElements
   *        The concatenated String to convert. May be <code>null</code> or
   *        empty.
   * @return The {@link List} represented by the passed string. Never
   *         <code>null</code>. If the passed input string is <code>null</code>
   *         or "" an empty list is returned.
   * @deprecated Use {@link #getExploded(String,String)} instead
   */
  @Deprecated
  @Nonnull
  public static List <String> explode (@Nonnull final String sSep, @Nullable final String sElements)
  {
    return getExploded (sSep, sElements);
  }

  /**
   * Take a concatenated String and return a {@link List} of all elements in the
   * passed string, using specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param sElements
   *        The concatenated String to convert. May be <code>null</code> or
   *        empty.
   * @return The {@link List} represented by the passed string. Never
   *         <code>null</code>. If the passed input string is <code>null</code>
   *         or "" an empty list is returned.
   */
  @Nonnull
  public static List <String> getExploded (@Nonnull final String sSep, @Nullable final String sElements)
  {
    return _getExploded (sSep, sElements, new ArrayList <String> ());
  }

  /**
   * Take a concatenated String and return a {@link Set} of all elements in the
   * passed string, using specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param sElements
   *        The concatenated String to convert. May be <code>null</code> or
   *        empty.
   * @return The {@link Set} represented by the passed string. Never
   *         <code>null</code>. If the passed input string is <code>null</code>
   *         or "" an empty list is returned.
   * @deprecated Use {@link #getExplodedToSet(String,String)} instead
   */
  @Deprecated
  @Nonnull
  public static Set <String> explodeToSet (@Nonnull final String sSep, @Nullable final String sElements)
  {
    return getExplodedToSet (sSep, sElements);
  }

  /**
   * Take a concatenated String and return a {@link Set} of all elements in the
   * passed string, using specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param sElements
   *        The concatenated String to convert. May be <code>null</code> or
   *        empty.
   * @return The {@link Set} represented by the passed string. Never
   *         <code>null</code>. If the passed input string is <code>null</code>
   *         or "" an empty list is returned.
   */
  @Nonnull
  public static Set <String> getExplodedToSet (@Nonnull final String sSep, @Nullable final String sElements)
  {
    return _getExploded (sSep, sElements, new HashSet <String> ());
  }

  /**
   * Take a concatenated String and return an ordered {@link Set} of all
   * elements in the passed string, using specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param sElements
   *        The concatenated String to convert. May be <code>null</code> or
   *        empty.
   * @return The ordered {@link Set} represented by the passed string. Never
   *         <code>null</code>. If the passed input string is <code>null</code>
   *         or "" an empty list is returned.
   * @deprecated Use {@link #getExplodedToOrderedSet(String,String)} instead
   */
  @Deprecated
  @Nonnull
  public static Set <String> explodeToOrderedSet (@Nonnull final String sSep, @Nullable final String sElements)
  {
    return getExplodedToOrderedSet (sSep, sElements);
  }

  /**
   * Take a concatenated String and return an ordered {@link Set} of all
   * elements in the passed string, using specified separator string.
   * 
   * @param sSep
   *        The separator to use. May not be <code>null</code>.
   * @param sElements
   *        The concatenated String to convert. May be <code>null</code> or
   *        empty.
   * @return The ordered {@link Set} represented by the passed string. Never
   *         <code>null</code>. If the passed input string is <code>null</code>
   *         or "" an empty list is returned.
   */
  @Nonnull
  public static Set <String> getExplodedToOrderedSet (@Nonnull final String sSep, @Nullable final String sElements)
  {
    return _getExploded (sSep, sElements, new LinkedHashSet <String> ());
  }

  /**
   * Get the passed string element repeated for a certain number of times. Each
   * string element is simply appended at the end of the string.
   * 
   * @param cElement
   *        The character to get repeated.
   * @param nRepeats
   *        The number of repetitions to retrieve. May not be &lt; 0.
   * @return A non-<code>null</code> string containing the string element for
   *         the given number of times.
   */
  @Nonnull
  public static String getRepeated (final char cElement, @Nonnegative final int nRepeats)
  {
    if (nRepeats < 0)
      throw new IllegalArgumentException ("Repeat count must be >= 0.");

    if (nRepeats == 0)
      return "";
    if (nRepeats == 1)
      return Character.toString (cElement);

    final char [] aElement = new char [nRepeats];
    Arrays.fill (aElement, cElement);
    return new String (aElement);
  }

  /**
   * Get the passed string element repeated for a certain number of times. Each
   * string element is simply appended at the end of the string.
   * 
   * @param sElement
   *        The string to get repeated. May not be <code>null</code>.
   * @param nRepeats
   *        The number of repetitions to retrieve. May not be &lt; 0.
   * @return A non-<code>null</code> string containing the string element for
   *         the given number of times.
   */
  @Nonnull
  public static String getRepeated (@Nonnull final String sElement, @Nonnegative final int nRepeats)
  {
    if (sElement == null)
      throw new NullPointerException ("element");
    if (nRepeats < 0)
      throw new IllegalArgumentException ("Repeat count must be >= 0");

    if (sElement.length () == 0 || nRepeats == 0)
      return "";
    if (nRepeats == 1)
      return sElement;

    // use character version
    if (sElement.length () == 1)
      return getRepeated (sElement.charAt (0), nRepeats);

    // combine via StringBuilder
    final StringBuilder ret = new StringBuilder (sElement.length () * nRepeats);
    for (int i = 0; i < nRepeats; ++i)
      ret.append (sElement);
    return ret.toString ();
  }

  /**
   * Concatenate the strings sFront and sEnd. If either front or back is null or
   * empty
   * 
   * @param sFront
   *        Front string. May be <code>null</code>.
   * @param sEnd
   *        May be <code>null</code>.
   * @return The concatenated string.
   * @deprecated Use {@link #getConcatenatedOnDemand(String,String)} instead
   */
  @Deprecated
  @Nonnull
  public static String concatenateOnDemand (@Nullable final String sFront, @Nullable final String sEnd)
  {
    return getConcatenatedOnDemand (sFront, sEnd);
  }

  /**
   * Concatenate the strings sFront and sEnd. If either front or back is null or
   * empty
   * 
   * @param sFront
   *        Front string. May be <code>null</code>.
   * @param sEnd
   *        May be <code>null</code>.
   * @return The concatenated string.
   */
  @Nonnull
  public static String getConcatenatedOnDemand (@Nullable final String sFront, @Nullable final String sEnd)
  {
    if (sFront == null)
      return sEnd == null ? "" : sEnd;
    if (sEnd == null)
      return sFront;
    return sFront + sEnd;
  }

  /**
   * Concatenate the strings sFront and sEnd by the "sSep" string. If either
   * front or back is <code>null</code> or empty, the separator is not applied.
   * 
   * @param sFront
   *        Front string. May be <code>null</code>.
   * @param sSep
   *        Separator string. May be <code>null</code>.
   * @param sEnd
   *        May be <code>null</code>.
   * @return The concatenated string.
   * @deprecated Use {@link #getConcatenatedOnDemand(String,String,String)}
   *             instead
   */
  @Deprecated
  @Nonnull
  public static String concatenateOnDemand (@Nullable final String sFront,
                                            @Nullable final String sSep,
                                            @Nullable final String sEnd)
  {
    return getConcatenatedOnDemand (sFront, sSep, sEnd);
  }

  /**
   * Concatenate the strings sFront and sEnd by the "sSep" string. If either
   * front or back is <code>null</code> or empty, the separator is not applied.
   * 
   * @param sFront
   *        Front string. May be <code>null</code>.
   * @param sSep
   *        Separator string. May be <code>null</code>.
   * @param sEnd
   *        May be <code>null</code>.
   * @return The concatenated string.
   */
  @Nonnull
  public static String getConcatenatedOnDemand (@Nullable final String sFront,
                                                @Nullable final String sSep,
                                                @Nullable final String sEnd)
  {
    final StringBuilder aSB = new StringBuilder ();
    if (hasText (sFront))
    {
      aSB.append (sFront);
      if (hasText (sSep) && hasText (sEnd))
        aSB.append (sSep);
    }
    if (hasText (sEnd))
      aSB.append (sEnd);
    return aSB.toString ();
  }

  public static boolean startsWith (@Nullable final CharSequence aCS, final char c)
  {
    return hasText (aCS) && aCS.charAt (0) == c;
  }

  public static boolean startsWithAny (@Nullable final CharSequence aCS, @Nullable final char [] aChars)
  {
    if (hasText (aCS) && aChars != null)
    {
      final char cFirst = aCS.charAt (0);
      for (final char c : aChars)
        if (cFirst == c)
          return true;
    }
    return false;
  }

  public static boolean startsWithIgnoreCase (@Nullable final CharSequence aCS, final char c)
  {
    return hasText (aCS) && Character.toLowerCase (aCS.charAt (0)) == Character.toLowerCase (c);
  }

  public static boolean startsWith (@Nullable final CharSequence aCS, @Nullable final CharSequence aSearch)
  {
    return aCS != null &&
           aSearch != null &&
           aCS.length () >= aSearch.length () &&
           aCS.subSequence (0, aSearch.length ()).equals (aSearch);
  }

  public static boolean startsWithIgnoreCase (@Nullable final String sStr, @Nullable final String sSearch)
  {
    return sStr != null &&
           sSearch != null &&
           sStr.length () >= sSearch.length () &&
           sStr.substring (0, sSearch.length ()).equalsIgnoreCase (sSearch);
  }

  public static boolean endsWith (@Nullable final CharSequence aCS, final char c)
  {
    return hasText (aCS) && getLastChar (aCS) == c;
  }

  public static boolean endsWithAny (@Nullable final CharSequence aCS, @Nullable final char [] aChars)
  {
    if (hasText (aCS) && aChars != null)
    {
      final char cLast = getLastChar (aCS);
      for (final char c : aChars)
        if (cLast == c)
          return true;
    }
    return false;
  }

  public static boolean endsWith (@Nullable final CharSequence aCS, @Nullable final CharSequence aSearch)
  {
    return aCS != null &&
           aSearch != null &&
           aCS.length () >= aSearch.length () &&
           aCS.subSequence (aCS.length () - aSearch.length (), aCS.length ()).equals (aSearch);
  }

  public static boolean endsWithIgnoreCase (@Nullable final CharSequence aCS, final char c)
  {
    return hasText (aCS) && Character.toLowerCase (getLastChar (aCS)) == Character.toLowerCase (c);
  }

  public static boolean endsWithIgnoreCase (@Nullable final String sStr, @Nullable final String sSearch)
  {
    return sStr != null &&
           sSearch != null &&
           sStr.length () >= sSearch.length () &&
           sStr.substring (sStr.length () - sSearch.length (), sStr.length ()).equalsIgnoreCase (sSearch);
  }

  /**
   * Get the index of sSearch within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @return The index of sSearch within sText or -1 if sSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(String)
   * @deprecated Use {@link #getIndexOf(String,String)} instead
   */
  @Deprecated
  public static int indexOf (@Nullable final String sText, @Nullable final String sSearch)
  {
    return getIndexOf (sText, sSearch);
  }

  /**
   * Get the index of sSearch within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @return The index of sSearch within sText or -1 if sSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(String)
   */
  public static int getIndexOf (@Nullable final String sText, @Nullable final String sSearch)
  {
    return sText != null && sSearch != null && sText.length () >= sSearch.length () ? sText.indexOf (sSearch) : -1;
  }

  /**
   * Get the index of cSearch within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param cSearch
   *        The character to search for. May be <code>null</code>.
   * @return The index of sSearch within sText or -1 if cSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(int)
   * @deprecated Use {@link #getIndexOf(String,char)} instead
   */
  @Deprecated
  public static int indexOf (@Nullable final String sText, final char cSearch)
  {
    return getIndexOf (sText, cSearch);
  }

  /**
   * Get the index of cSearch within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param cSearch
   *        The character to search for. May be <code>null</code>.
   * @return The index of sSearch within sText or -1 if cSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(int)
   */
  public static int getIndexOf (@Nullable final String sText, final char cSearch)
  {
    return sText != null && sText.length () >= 1 ? sText.indexOf (cSearch) : -1;
  }

  /**
   * Get the index of sSearch within sText ignoring case.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @param aSortLocale
   *        The locale to be used for case unifying.
   * @return The index of sSearch within sText or -1 if sSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(String)
   * @deprecated Use {@link #getIndexOfIgnoreCase(String,String,Locale)} instead
   */
  @Deprecated
  public static int indexOfIgnoreCase (@Nullable final String sText,
                                       @Nullable final String sSearch,
                                       @Nonnull final Locale aSortLocale)
  {
    return getIndexOfIgnoreCase (sText, sSearch, aSortLocale);
  }

  /**
   * Get the index of sSearch within sText ignoring case.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @param aSortLocale
   *        The locale to be used for case unifying.
   * @return The index of sSearch within sText or -1 if sSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(String)
   */
  public static int getIndexOfIgnoreCase (@Nullable final String sText,
                                          @Nullable final String sSearch,
                                          @Nonnull final Locale aSortLocale)
  {
    return sText != null && sSearch != null && sText.length () >= sSearch.length ()
                                                                                   ? sText.toLowerCase (aSortLocale)
                                                                                          .indexOf (sSearch.toLowerCase (aSortLocale))
                                                                                   : -1;
  }

  /**
   * Get the index of cSearch within sText ignoring case.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param cSearch
   *        The char to search for. May be <code>null</code>.
   * @param aSortLocale
   *        The locale to be used for case unifying.
   * @return The index of sSearch within sText or -1 if sSearch was not found or
   *         if any parameter was <code>null</code>.
   * @see String#indexOf(int)
   */
  public static int getIndexOfIgnoreCase (@Nullable final String sText,
                                          final char cSearch,
                                          @Nonnull final Locale aSortLocale)
  {
    return sText != null && sText.length () >= 1 ? sText.toLowerCase (aSortLocale)
                                                        .indexOf (Character.toLowerCase (cSearch)) : -1;
  }

  /**
   * Check if sSearch is contained within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @return <code>true</code> if sSearch is contained in sText,
   *         <code>false</code> otherwise.
   * @see String#contains(CharSequence)
   */
  public static boolean contains (@Nullable final String sText, @Nullable final String sSearch)
  {
    return getIndexOf (sText, sSearch) > -1;
  }

  /**
   * Check if cSearch is contained within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param cSearch
   *        The character to search for. May be <code>null</code>.
   * @return <code>true</code> if cSearch is contained in sText,
   *         <code>false</code> otherwise.
   * @see String#contains(CharSequence)
   */
  public static boolean contains (@Nullable final String sText, final char cSearch)
  {
    return getIndexOf (sText, cSearch) > -1;
  }

  /**
   * Check if sSearch is contained within sText ignoring case.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @param aSortLocale
   *        The locale to be used for case unifying.
   * @return <code>true</code> if sSearch is contained in sText,
   *         <code>false</code> otherwise.
   * @see String#contains(CharSequence)
   */
  public static boolean containsIgnoreCase (@Nullable final String sText,
                                            @Nullable final String sSearch,
                                            @Nonnull final Locale aSortLocale)
  {
    return getIndexOfIgnoreCase (sText, sSearch, aSortLocale) > -1;
  }

  /**
   * Check if cSearch is contained within sText ignoring case.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param cSearch
   *        The char to search for. May be <code>null</code>.
   * @param aSortLocale
   *        The locale to be used for case unifying.
   * @return <code>true</code> if sSearch is contained in sText,
   *         <code>false</code> otherwise.
   * @see String#indexOf(int)
   */
  public static boolean containsIgnoreCase (@Nullable final String sText,
                                            final char cSearch,
                                            @Nonnull final Locale aSortLocale)
  {
    return getIndexOfIgnoreCase (sText, cSearch, aSortLocale) > -1;
  }

  /**
   * Check if any of the passed searched characters is contained in the input
   * char array.
   * 
   * @param aInput
   *        The input char array. May be <code>null</code>.
   * @param aSearchChars
   *        The char array to search. May not be <code>null</code>.
   * @return <code>true</code> if at least any of the search char is contained
   *         in the input char array, <code>false</code> otherwise.
   * @deprecated Use {@link #containsAny(char[],char[])} instead
   */
  @Deprecated
  public static boolean multiContains (@Nonnull final char [] aInput, @Nonnull final char [] aSearchChars)
  {
    return containsAny (aInput, aSearchChars);
  }

  /**
   * Check if any of the passed searched characters is contained in the input
   * char array.
   * 
   * @param aInput
   *        The input char array. May be <code>null</code>.
   * @param aSearchChars
   *        The char array to search. May not be <code>null</code>.
   * @return <code>true</code> if at least any of the search char is contained
   *         in the input char array, <code>false</code> otherwise.
   */
  public static boolean containsAny (@Nonnull final char [] aInput, @Nonnull final char [] aSearchChars)
  {
    if (aSearchChars == null)
      throw new NullPointerException ("searchChars");

    if (aInput != null)
      for (final char cIn : aInput)
        for (final char cSearch : aSearchChars)
          if (cIn == cSearch)
            return true;
    return false;
  }

  /**
   * Check if any of the passed searched characters in contained in the input
   * string.
   * 
   * @param sInput
   *        The input string. May be <code>null</code>.
   * @param aSearchChars
   *        The char array to search. May not be <code>null</code>.
   * @return <code>true</code> if at least any of the search char is contained
   *         in the input char array, <code>false</code> otherwise.
   */
  public static boolean containsAny (@Nonnull final String sInput, @Nonnull final char [] aSearchChars)
  {
    return sInput == null ? false : containsAny (sInput.toCharArray (), aSearchChars);
  }

  /**
   * Count the number of occurrences of sSearch within sText.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @return A non-negative number of occurrences.
   */
  @Nonnegative
  public static int getOccurrenceCount (@Nullable final String sText, @Nullable final String sSearch)
  {
    int ret = 0;
    final int nTextLength = getLength (sText);
    final int nSearchLength = getLength (sSearch);
    if (nTextLength > 0 && nSearchLength > 0 && nTextLength >= nSearchLength)
    {
      int nLastIndex = 0, nIndex;
      do
      {
        // Start searching from the last result
        nIndex = getIndexOf (sText.substring (nLastIndex), sSearch);
        if (nIndex > -1)
        {
          // Match found
          ++ret;

          // Identify the next starting position (relative index + number of
          // search strings)
          nLastIndex += nIndex + nSearchLength;
        }
      } while (nIndex != -1);
    }
    return ret;
  }

  /**
   * Count the number of occurrences of sSearch within sText ignoring case.
   * 
   * @param sText
   *        The text to search in. May be <code>null</code>.
   * @param sSearch
   *        The text to search for. May be <code>null</code>.
   * @param aSortLocale
   *        The locale to be used for case unifying.
   * @return A non-negative number of occurrences.
   */
  @Nonnegative
  public static int getOccurrenceCountIgnoreCase (@Nullable final String sText,
                                                  @Nullable final String sSearch,
                                                  @Nonnull final Locale aSortLocale)
  {
    return sText != null && sSearch != null ? getOccurrenceCount (sText.toLowerCase (aSortLocale),
                                                                  sSearch.toLowerCase (aSortLocale)) : 0;
  }

  /**
   * Remove any leading whitespaces from the passed string.
   * 
   * @param s
   *        the String to be trimmed
   * @return the original String with all leading whitespaces removed
   */
  @Nullable
  @CheckReturnValue
  public static String trimLeadingWhitespaces (@Nullable final String s)
  {
    final int n = getLeadingWhitespaceCount (s);
    return n == 0 ? s : s.substring (n, s.length ());
  }

  /**
   * Remove any trailing whitespaces from the passed string.
   * 
   * @param s
   *        the String to be cut
   * @return the original String with all trailing whitespaces removed
   */
  @Nullable
  @CheckReturnValue
  public static String trimTrailingWhitespaces (@Nullable final String s)
  {
    final int n = getTrailingWhitespaceCount (s);
    return n == 0 ? s : s.substring (0, s.length () - n);
  }

  /**
   * Trim the passed lead from the source value. If the source value does not
   * start with the passed lead, nothing happens.
   * 
   * @param sSrc
   *        The input source string
   * @param sLead
   *        The string to be trimmed of the beginning
   * @return The trimmed string, or the original input string, if the lead was
   *         not found
   */
  @Nullable
  @CheckReturnValue
  public static String trimStart (@Nullable final String sSrc, @Nullable final String sLead)
  {
    return startsWith (sSrc, sLead) ? sSrc.substring (sLead.length (), sSrc.length ()) : sSrc;
  }

  /**
   * Trim the passed tail from the source value. If the source value does not
   * end with the passed tail, nothing happens.
   * 
   * @param sSrc
   *        The input source string
   * @param sTail
   *        The string to be trimmed of the end
   * @return The trimmed string, or the original input string, if the tail was
   *         not found
   */
  @Nullable
  @CheckReturnValue
  public static String trimEnd (@Nullable final String sSrc, @Nullable final String sTail)
  {
    return endsWith (sSrc, sTail) ? sSrc.substring (0, sSrc.length () - sTail.length ()) : sSrc;
  }

  /**
   * Trim the passed string, if it is not <code>null</code>.
   * 
   * @param s
   *        The string to be trimmed. May be <code>null</code>.
   * @return <code>null</code> if the input string was <code>null</code>, the
   *         non-<code>null</code> trimmed string otherwise.
   * @see String#trim()
   */
  @Nullable
  public static String trim (@Nullable final String s)
  {
    return hasNoText (s) ? s : s.trim ();
  }

  public static char getFirstChar (@Nullable final CharSequence aCS)
  {
    return hasText (aCS) ? aCS.charAt (0) : CGlobal.ILLEGAL_CHAR;
  }

  public static char getFirstChar (@Nullable final char [] aChars)
  {
    return ArrayHelper.getFirst (aChars, CGlobal.ILLEGAL_CHAR);
  }

  public static char getLastChar (@Nullable final CharSequence aCS)
  {
    final int nLength = getLength (aCS);
    return nLength > 0 ? aCS.charAt (nLength - 1) : CGlobal.ILLEGAL_CHAR;
  }

  public static char getLastChar (@Nullable final char [] aChars)
  {
    return ArrayHelper.getLast (aChars, CGlobal.ILLEGAL_CHAR);
  }

  @Nonnegative
  public static int getCharCount (@Nullable final String s, final char cSearch)
  {
    return s == null ? 0 : getCharCount (s.toCharArray (), cSearch);
  }

  @Nonnegative
  public static int getCharCount (@Nullable final char [] aChars, final char cSearch)
  {
    int ret = 0;
    if (aChars != null)
      for (final char c : aChars)
        if (c == cSearch)
          ++ret;
    return ret;
  }

  @Nonnegative
  public static int getLineCount (@Nullable final String s)
  {
    return getLineCount (s, '\n');
  }

  @Nonnegative
  public static int getLineCount (@Nullable final String s, final char cLineSep)
  {
    return 1 + getCharCount (s, cLineSep);
  }

  /**
   * Get the number of characters the passed value would occupy in a string
   * representation.<br>
   * Copied from java.lang.Integer#StringSize
   * 
   * @param nValue
   *        The integer value to check. May be be positive or negative.
   * @return Number of characters required. Alyways &gt; 0.
   */
  @Nonnegative
  public static int getCharacterCount (final int nValue)
  {
    // index is always one character less than the real size; that's why nPrefix
    // is 1 more!
    final int nPrefix = nValue < 0 ? 2 : 1;
    final int nRealValue = MathHelper.abs (nValue);

    for (int nIndex = 0;; nIndex++)
      if (nRealValue <= s_aSizeTableInt[nIndex])
        return nPrefix + nIndex;
  }

  /**
   * Get the number of characters the passed value would occupy in a string
   * representation.
   * 
   * @param nValue
   *        The long value to check. May be be positive or negative.
   * @return Number of characters required. Always &gt; 0.
   */
  @Nonnegative
  public static int getCharacterCount (final long nValue)
  {
    // index is always one character less than the real size; that's why nPrefix
    // is 1 more!
    final int nPrefix = nValue < 0 ? 2 : 1;
    final long nRealValue = MathHelper.abs (nValue);

    for (int nIndex = 0;; nIndex++)
      if (nRealValue <= s_aSizeTableLong[nIndex])
        return nPrefix + nIndex;
  }

  /**
   * @deprecated Use {@link #getCutAfterLength(String,int,String)} instead
   */
  @Deprecated
  @Nonnull
  public static String cutAfterLength (@Nonnull final String sValue,
                                       @Nonnegative final int nMaxLength,
                                       @Nullable final String sNewSuffix)
  {
    return getCutAfterLength (sValue, nMaxLength, sNewSuffix);
  }

  @Nonnull
  public static String getCutAfterLength (@Nonnull final String sValue,
                                          @Nonnegative final int nMaxLength,
                                          @Nullable final String sNewSuffix)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    if (nMaxLength < 0)
      throw new IllegalArgumentException ("Illegal max length");

    if (sValue.length () <= nMaxLength)
      return sValue;
    if (isEmpty (sNewSuffix))
      return sValue.substring (0, nMaxLength);
    return sValue.substring (0, nMaxLength) + sNewSuffix;
  }

  /**
   * Same as {@link #replaceAll(String, String, CharSequence)} but allowing for
   * a <code>null</code> new-value, which is than interpreted as an empty string
   * instead.
   * 
   * @param sInputString
   *        The input string where the text should be replace. If this parameter
   *        is <code>null</code> or empty, no replacement is done.
   * @param sSearchText
   *        The string to be replaced. May neither be <code>null</code> nor
   *        empty.
   * @param aReplacementText
   *        The string with the replacement. May be <code>null</code> or empty.
   * @return The input string as is, if the input string is empty or if the
   *         string to be replaced is not contained.
   */
  public static String replaceAllSafe (@Nullable final String sInputString,
                                       @Nonnull final String sSearchText,
                                       @Nullable final CharSequence aReplacementText)
  {
    return replaceAll (sInputString, sSearchText, getNotNull (aReplacementText, ""));
  }

  /**
   * This is a fast replacement for
   * {@link String#replace(CharSequence, CharSequence)}. The problem with the
   * mentioned {@link String} method is, that is uses internally regular
   * expressions which use a synchronized block to compile the patterns. This
   * method is inherently thread safe since {@link String} is immutable and
   * we're operating on different temporary {@link StringBuilder} objects.
   * 
   * @param sInputString
   *        The input string where the text should be replace. If this parameter
   *        is <code>null</code> or empty, no replacement is done.
   * @param sSearchText
   *        The string to be replaced. May neither be <code>null</code> nor
   *        empty.
   * @param aReplacementText
   *        The string with the replacement. May not be <code>null</code> but
   *        may be empty.
   * @return The input string as is, if the input string is empty or if the
   *         search pattern and the replacement are equal or if the string to be
   *         replaced is not contained.
   */
  @Nullable
  public static String replaceAll (@Nullable final String sInputString,
                                   @Nonnull final String sSearchText,
                                   @Nonnull final CharSequence aReplacementText)
  {
    if (hasNoText (sSearchText))
      throw new IllegalArgumentException ("Value to replace may not be empty!");
    if (aReplacementText == null)
      throw new NullPointerException ("newText");

    // Is input string empty?
    if (hasNoText (sInputString))
      return sInputString;

    // Replace old with the same new?
    final int nOldLength = sSearchText.length ();
    final int nNewLength = aReplacementText.length ();
    if (nOldLength == nNewLength)
    {
      // Any change?
      if (sSearchText.equals (aReplacementText))
        return sInputString;

      if (nOldLength == 1)
      {
        // Use char version which is more efficient
        return replaceAll (sInputString, sSearchText.charAt (0), aReplacementText.charAt (0));
      }
    }

    // Does the old text occur anywhere?
    int nIndex = sInputString.indexOf (sSearchText, 0);
    if (nIndex == -1)
      return sInputString;

    // build output buffer
    final StringBuilder ret = new StringBuilder (nOldLength >= nNewLength ? sInputString.length ()
                                                                         : sInputString.length () * 2);
    int nOldIndex = 0;
    do
    {
      ret.append (sInputString, nOldIndex, nIndex).append (aReplacementText);
      nIndex += nOldLength;
      nOldIndex = nIndex;
    } while ((nIndex = sInputString.indexOf (sSearchText, nIndex)) != -1);
    ret.append (sInputString, nOldIndex, sInputString.length ());
    return ret.toString ();
  }

  /**
   * This is a fast replacement for
   * {@link String#replace(CharSequence, CharSequence)} for characters. The
   * problem with the mentioned String method is, that is uses internally
   * regular expressions which use a synchronized block to compile the patterns.
   * This method is inherently thread safe since {@link String} is immutable and
   * we're operating on different temporary {@link StringBuilder} objects.
   * 
   * @param sInputString
   *        The input string where the text should be replace. If this parameter
   *        is <code>null</code> or empty, no replacement is done.
   * @param cSearchChar
   *        The character to be replaced. May neither be <code>null</code> nor
   *        empty.
   * @param cReplacementChar
   *        The character with the replacement. May not be <code>null</code> but
   *        may be empty.
   * @return The input string as is, if the input string is empty or if the
   *         search pattern and the replacement are equal or if the string to be
   *         replaced is not contained.
   */
  @Nullable
  public static String replaceAll (@Nullable final String sInputString,
                                   final char cSearchChar,
                                   final char cReplacementChar)
  {
    // Is input string empty?
    if (hasNoText (sInputString))
      return sInputString;

    // Replace old with the same new?
    if (cSearchChar == cReplacementChar)
      return sInputString;

    // Does the old text occur anywhere?
    int nIndex = sInputString.indexOf (cSearchChar, 0);
    if (nIndex == -1)
      return sInputString;

    // build output buffer
    final StringBuilder ret = new StringBuilder (sInputString.length ());
    int nOldIndex = 0;
    do
    {
      ret.append (sInputString, nOldIndex, nIndex).append (cReplacementChar);
      nIndex++;
      nOldIndex = nIndex;
    } while ((nIndex = sInputString.indexOf (cSearchChar, nIndex)) != -1);
    ret.append (sInputString, nOldIndex, sInputString.length ());
    return ret.toString ();
  }

  /**
   * Just calls <code>replaceAll</code> as long as there are still replacements
   * found
   * 
   * @param sInputString
   *        The input string where the text should be replace. If this parameter
   *        is <code>null</code> or empty, no replacement is done.
   * @param sSearchText
   *        The string to be replaced. May neither be <code>null</code> nor
   *        empty.
   * @param sReplacementText
   *        The string with the replacement. May not be <code>null</code> but
   *        may be empty.
   * @return The input string as is, if the input string is empty or if the
   *         string to be replaced is not contained.
   */
  @Nullable
  public static String replaceAllRepeatedly (@Nullable final String sInputString,
                                             @Nonnull final String sSearchText,
                                             @Nonnull final String sReplacementText)
  {
    if (hasNoText (sSearchText))
      throw new IllegalArgumentException ("searchText");
    if (sReplacementText == null)
      throw new NullPointerException ("replacementText");
    if (sReplacementText.indexOf (sSearchText) >= 0)
      throw new IllegalArgumentException ("Loop detection: replacementText must not contain searchText");

    // Is input string empty?
    if (hasNoText (sInputString))
      return sInputString;

    String sRet = sInputString;
    String sLastLiteral;
    do
    {
      sLastLiteral = sRet;
      sRet = replaceAll (sRet, sSearchText, sReplacementText);
    } while (!sLastLiteral.equals (sRet));
    return sRet;
  }

  /**
   * Get the result length (in characters) when replacing all patterns with the
   * replacements on the passed input array.
   * 
   * @param aInputString
   *        Input char array. May not be <code>null</code>.
   * @param aSearchChars
   *        The one-character search patterns. May not be <code>null</code>.
   * @param aReplacementStrings
   *        The replacements to be performed. May not be <code>null</code>. The
   *        first dimension of this array must have exactly the same amount of
   *        elements as the patterns parameter array.
   * @return {@link CGlobal#ILLEGAL_UINT} if no replacement was needed, and
   *         therefore the length of the input array could be used.
   */
  public static int getReplaceMultipleResultLength (@Nonnull final char [] aInputString,
                                                    @Nonnull @Nonempty final char [] aSearchChars,
                                                    @Nonnull @Nonempty final char [][] aReplacementStrings)
  {
    int nResultLen = 0;
    boolean bAnyReplacement = false;
    for (final char cInput : aInputString)
    {
      // In case no replacement is found use a single char
      int nReplacementLength = 1;
      for (int nIndex = 0; nIndex < aSearchChars.length; nIndex++)
        if (cInput == aSearchChars[nIndex])
        {
          nReplacementLength = aReplacementStrings[nIndex].length;
          bAnyReplacement = true;
          break;
        }
      nResultLen += nReplacementLength;
    }
    return bAnyReplacement ? nResultLen : CGlobal.ILLEGAL_UINT;
  }

  /**
   * Optimized replace method that replaces a set of characters with a set of
   * strings. This method was created for efficient XML special character
   * replacements!
   * 
   * @param sInputString
   *        The input string.
   * @param aSearchChars
   *        The characters to replace.
   * @param aReplacementStrings
   *        The new strings to be inserted instead. Must have the same array
   *        length as aPatterns.
   * @return The replaced version of the string or an empty char array if the
   *         input string was <code>null</code>.
   */
  @Nonnull
  public static char [] replaceMultiple (@Nullable final String sInputString,
                                         @Nonnull final char [] aSearchChars,
                                         @Nonnull final char [][] aReplacementStrings)
  {
    if (aSearchChars == null)
      throw new NullPointerException ("search");
    if (aReplacementStrings == null)
      throw new NullPointerException ("replacements");
    if (aSearchChars.length != aReplacementStrings.length)
      throw new IllegalArgumentException ("array length mismatch");

    // Any input text?
    if (hasNoText (sInputString))
      return new char [0];

    // Get char array
    final char [] aInput = sInputString.toCharArray ();

    // Any replacement patterns?
    if (aSearchChars.length == 0)
      return aInput;

    // get result length
    final int nResultLen = getReplaceMultipleResultLength (aInput, aSearchChars, aReplacementStrings);

    // nothing to replace in here?
    if (nResultLen == CGlobal.ILLEGAL_UINT)
      return aInput;

    // build result
    final char [] aOutput = new char [nResultLen];
    int nOutputIndex = 0;

    // For all input chars
    for (final char cInput : aInput)
    {
      boolean bFoundReplacement = false;
      for (int nPatternIndex = 0; nPatternIndex < aSearchChars.length; nPatternIndex++)
      {
        if (cInput == aSearchChars[nPatternIndex])
        {
          final char [] aReplacement = aReplacementStrings[nPatternIndex];
          final int nReplacementLength = aReplacement.length;
          System.arraycopy (aReplacement, 0, aOutput, nOutputIndex, nReplacementLength);
          nOutputIndex += nReplacementLength;
          bFoundReplacement = true;
          break;
        }
      }
      if (!bFoundReplacement)
      {
        // copy char as is
        aOutput[nOutputIndex++] = cInput;
      }
    }

    return aOutput;
  }

  /**
   * Specialized version of {@link #replaceMultiple(String, char[], char[][])}
   * where the object where the output should be appended is passed in as a
   * parameter. This has the advantage, that not length calculation needs to
   * take place!
   * 
   * @param sInputString
   *        The input string.
   * @param aSearchChars
   *        The characters to replace.
   * @param aReplacementStrings
   *        The new strings to be inserted instead. Must have the same array
   *        length as aPatterns.
   * @param aTarget
   *        Where the replaced objects should be written to. May not be
   *        <code>null</code>.
   * @return The number of replacements performed. Always &ge; 0.
   * @throws IOException
   */
  @Nonnegative
  public static int replaceMultipleTo (@Nullable final String sInputString,
                                       @Nonnull final char [] aSearchChars,
                                       @Nonnull final char [][] aReplacementStrings,
                                       @Nonnull final Writer aTarget) throws IOException
  {
    if (aSearchChars == null)
      throw new NullPointerException ("patterns");
    if (aReplacementStrings == null)
      throw new NullPointerException ("replacements");
    if (aSearchChars.length != aReplacementStrings.length)
      throw new IllegalArgumentException ("array length mismatch");
    if (aTarget == null)
      throw new NullPointerException ("target");

    if (hasNoText (sInputString))
      return 0;

    if (aSearchChars.length == 0)
    {
      // No modifications required
      aTarget.write (sInputString);
      return 0;
    }

    final char [] aInput = sInputString.toCharArray ();

    // for all input string characters
    int nFirstNonReplace = 0;
    int nInputIndex = 0;
    int nTotalReplacements = 0;
    for (final char cInput : aInput)
    {
      for (int nPatternIndex = 0; nPatternIndex < aSearchChars.length; nPatternIndex++)
      {
        if (cInput == aSearchChars[nPatternIndex])
        {
          if (nFirstNonReplace < nInputIndex)
            aTarget.write (aInput, nFirstNonReplace, nInputIndex - nFirstNonReplace);
          nFirstNonReplace = nInputIndex + 1;
          aTarget.write (aReplacementStrings[nPatternIndex]);
          ++nTotalReplacements;
          break;
        }
      }
      nInputIndex++;
    }
    if (nFirstNonReplace < nInputIndex)
      aTarget.write (aInput, nFirstNonReplace, nInputIndex - nFirstNonReplace);
    return nTotalReplacements;
  }

  /**
   * Perform all string replacements on the input string as defined by the
   * passed map. All replacements are done using
   * {@link #replaceAll(String,String,CharSequence)} which is ok.
   * 
   * @param sInputString
   *        The input string where the text should be replaced. May be
   *        <code>null</code>.
   * @param aTransTable
   *        The map with the replacements to execute. If <code>null</code> is
   *        passed, the input string is not altered.
   * @return <code>null</code> if the input string was <code>null</code>.
   */
  @Nullable
  public static String replaceMultiple (@Nullable final String sInputString,
                                        @Nullable final Map <String, String> aTransTable)
  {
    if (hasNoText (sInputString) || aTransTable == null || aTransTable.isEmpty ())
      return sInputString;

    String sOutput = sInputString;
    for (final Entry <String, String> aEntry : aTransTable.entrySet ())
      sOutput = replaceAll (sOutput, aEntry.getKey (), aEntry.getValue ());
    return sOutput;
  }

  /**
   * Perform all string replacements on the input string as defined by the
   * passed map. All replacements are done using
   * {@link #replaceAll(String,String,CharSequence)} which is ok.
   * 
   * @param sInputString
   *        The input string where the text should be replaced. May be
   *        <code>null</code>.
   * @param aSearchTexts
   *        The texts to be searched. If <code>null</code> is passed, the input
   *        string is not altered.
   * @param aReplacementTexts
   *        The texts to be used as the replacements. This array must have
   *        exactly the same number of elements than the searched texts! If
   *        <code>null</code> is passed, the input string is not altered.
   * @return <code>null</code> if the input string was <code>null</code>. The
   *         unmodified input string if no search/replace patterns where
   *         provided.
   */
  @SuppressWarnings ("null")
  @Nullable
  public static String replaceMultiple (@Nullable final String sInputString,
                                        @Nullable final String [] aSearchTexts,
                                        @Nullable final String [] aReplacementTexts)
  {
    if (hasNoText (sInputString))
      return sInputString;

    final int nSearchTextLength = aSearchTexts == null ? 0 : aSearchTexts.length;
    final int nReplacementTextLength = aReplacementTexts == null ? 0 : aReplacementTexts.length;
    if (nSearchTextLength != nReplacementTextLength)
      throw new IllegalArgumentException ("Array length mismatch!");

    // Nothing to replace?
    if (nSearchTextLength == 0)
      return sInputString;

    String sOutput = sInputString;
    for (int nIndex = 0; nIndex < nSearchTextLength; ++nIndex)
      sOutput = replaceAll (sOutput, aSearchTexts[nIndex], aReplacementTexts[nIndex]);
    return sOutput;
  }

  /**
   * Get the length of the passed character sequence.
   * 
   * @param aCS
   *        The character sequence who's length is to be determined. May be
   *        <code>null</code>.
   * @return 0 if the parameter is <code>null</code>, its length otherwise.
   * @see CharSequence#length()
   * @deprecated Use {@link #getLength(CharSequence)} instead
   */
  @Deprecated
  @Nonnegative
  public static int length (@Nullable final CharSequence aCS)
  {
    return getLength (aCS);
  }

  /**
   * Get the length of the passed character sequence.
   * 
   * @param aCS
   *        The character sequence who's length is to be determined. May be
   *        <code>null</code>.
   * @return 0 if the parameter is <code>null</code>, its length otherwise.
   * @see CharSequence#length()
   */
  @Nonnegative
  public static int getLength (@Nullable final CharSequence aCS)
  {
    return aCS == null ? 0 : aCS.length ();
  }

  /**
   * Get the passed string but never return <code>null</code>. If the passed
   * parameter is <code>null</code> an empty string is returned.
   * 
   * @param s
   *        The parameter to be not <code>null</code>.
   * @return An empty string if the passed parameter is <code>null</code>, the
   *         passed string otherwise.
   */
  @Nonnull
  public static String getNotNull (@Nullable final String s)
  {
    return getNotNull (s, "");
  }

  /**
   * Get the passed string but never return <code>null</code>. If the passed
   * parameter is <code>null</code> the second parameter is returned.
   * 
   * @param s
   *        The parameter to be not <code>null</code>.
   * @param sDefaultIfNull
   *        The value to be used of the first parameter is <code>null</code>.
   *        May be <code>null</code> but in this case the call to this method is
   *        obsolete.
   * @return The passed default value if the string is <code>null</code>,
   *         otherwise the input string.
   */
  @Nullable
  public static String getNotNull (@Nullable final String s, final String sDefaultIfNull)
  {
    return s == null ? sDefaultIfNull : s;
  }

  /**
   * Get the passed {@link CharSequence} but never return <code>null</code>. If
   * the passed parameter is <code>null</code> an empty string is returned.
   * 
   * @param s
   *        The parameter to be not <code>null</code>.
   * @return An empty string if the passed parameter is <code>null</code>, the
   *         passed {@link CharSequence} otherwise.
   */
  @Nonnull
  public static CharSequence getNotNull (@Nullable final CharSequence s)
  {
    return getNotNull (s, "");
  }

  /**
   * Get the passed {@link CharSequence} but never return <code>null</code>. If
   * the passed parameter is <code>null</code> the second parameter is returned.
   * 
   * @param s
   *        The parameter to be not <code>null</code>.
   * @param sDefaultIfNull
   *        The value to be used of the first parameter is <code>null</code>.
   *        May be <code>null</code> but in this case the call to this method is
   *        obsolete.
   * @return The passed default value if the string is <code>null</code>,
   *         otherwise the input {@link CharSequence}.
   */
  @Nullable
  public static CharSequence getNotNull (@Nullable final CharSequence s, final CharSequence sDefaultIfNull)
  {
    return s == null ? sDefaultIfNull : s;
  }

  /**
   * Convert the passed object to a string using the {@link Object#toString()}
   * method.
   * 
   * @param aObject
   *        The value to be converted. May be <code>null</code>.
   * @return An empty string in case the passed object was <code>null</code>.
   *         Never <code>null</code>.
   * @see Object#toString()
   * @deprecated Use {@link #getToString(Object)} instead
   */
  @Deprecated
  @Nonnull
  public static String toString (@Nullable final Object aObject)
  {
    return getToString (aObject);
  }

  /**
   * Convert the passed object to a string using the {@link Object#toString()}
   * method.
   * 
   * @param aObject
   *        The value to be converted. May be <code>null</code>.
   * @return An empty string in case the passed object was <code>null</code>.
   *         Never <code>null</code>.
   * @see Object#toString()
   */
  @Nonnull
  public static String getToString (@Nullable final Object aObject)
  {
    return getToString (aObject, "");
  }

  /**
   * Convert the passed object to a string using the {@link Object#toString()}
   * method or otherwise return the passed default value.
   * 
   * @param aObject
   *        The value to be converted. May be <code>null</code>.
   * @param sNullValue
   *        The value to be returned in case the passed object is
   *        <code>null</code>. May be <code>null</code> itself.
   * @return The passed default value in case the passed object was
   *         <code>null</code> or the result of {@link Object#toString()} on the
   *         passed object.
   * @see Object#toString()
   * @deprecated Use {@link #getToString(Object,String)} instead
   */
  @Deprecated
  @Nullable
  public static String toString (@Nullable final Object aObject, @Nullable final String sNullValue)
  {
    return getToString (aObject, sNullValue);
  }

  /**
   * Convert the passed object to a string using the {@link Object#toString()}
   * method or otherwise return the passed default value.
   * 
   * @param aObject
   *        The value to be converted. May be <code>null</code>.
   * @param sNullValue
   *        The value to be returned in case the passed object is
   *        <code>null</code>. May be <code>null</code> itself.
   * @return The passed default value in case the passed object was
   *         <code>null</code> or the result of {@link Object#toString()} on the
   *         passed object.
   * @see Object#toString()
   */
  @Nullable
  public static String getToString (@Nullable final Object aObject, @Nullable final String sNullValue)
  {
    return aObject == null ? sNullValue : aObject.toString ();
  }
}
