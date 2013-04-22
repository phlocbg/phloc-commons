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
package com.phloc.commons.compare;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A small helper class that provides comparison methods that are
 * <code>null</code> safe. Also provides secure ways to compare double and float
 * values.
 * 
 * @author Philip Helger
 */
@Immutable
public final class CompareUtils
{
  /** By default <code>null</code> values fome first */
  public static final boolean DEFAULT_NULL_VALUES_COME_FIRST = true;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CompareUtils s_aInstance = new CompareUtils ();

  private CompareUtils ()
  {}

  /**
   * Special version for boolean, <code>true</code> before <code>false</code>
   * 
   * @param b1
   *        First value
   * @param b2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compareTrueBeforeFalse (final boolean b1, final boolean b2)
  {
    return b1 == b2 ? 0 : b1 ? +1 : -1;
  }

  /**
   * Special version for boolean, <code>true</code> before <code>false</code>
   * 
   * @param b1
   *        First value
   * @param b2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compareFalseBeforeTrue (final boolean b1, final boolean b2)
  {
    return b1 == b2 ? 0 : b1 ? -1 : +1;
  }

  /**
   * Special version for byte
   * 
   * @param n1
   *        First value
   * @param n2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final byte n1, final byte n2)
  {
    return n1 < n2 ? -1 : n1 > n2 ? +1 : 0;
  }

  /**
   * Special version for byte
   * 
   * @param n1
   *        First value
   * @param n2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final char n1, final char n2)
  {
    return n1 < n2 ? -1 : n1 > n2 ? +1 : 0;
  }

  /**
   * Special version for doubles
   * 
   * @param d1
   *        First value
   * @param d2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final double d1, final double d2)
  {
    return Double.compare (d1, d2);
  }

  /**
   * Special version for floats
   * 
   * @param f1
   *        First value
   * @param f2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final float f1, final float f2)
  {
    return Float.compare (f1, f2);
  }

  /**
   * Special version for int
   * 
   * @param n1
   *        First value
   * @param n2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final int n1, final int n2)
  {
    return n1 < n2 ? -1 : n1 > n2 ? +1 : 0;
  }

  /**
   * Special version for long
   * 
   * @param n1
   *        First value
   * @param n2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final long n1, final long n2)
  {
    return n1 < n2 ? -1 : n1 > n2 ? +1 : 0;
  }

  /**
   * Special version for short
   * 
   * @param n1
   *        First value
   * @param n2
   *        Second value
   * @return -1, 0 or + 1
   */
  public static int compare (final short n1, final short n2)
  {
    return n1 < n2 ? -1 : n1 > n2 ? +1 : 0;
  }

  /**
   * Compare the passed items and handle <code>null</code> values correctly. A
   * <code>null</code> value is always smaller than a non-<code>null</code>
   * value.
   * 
   * @param <DATATYPE>
   *        Any comparable object to be used. Both need to be of the same type.
   * @param aObj1
   *        First object to compare. May be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May be <code>null</code>.
   * @return 0 if they are equal (or both <code>null</code>), -1 or +1.
   */
  public static <DATATYPE extends Comparable <? super DATATYPE>> int nullSafeCompare (@Nullable final DATATYPE aObj1,
                                                                                      @Nullable final DATATYPE aObj2)
  {
    // Legacy behaviour: null values come first
    return nullSafeCompare (aObj1, aObj2, DEFAULT_NULL_VALUES_COME_FIRST);
  }

  /**
   * Compare the passed items and handle <code>null</code> values correctly. A
   * <code>null</code> value is always smaller than a non-<code>null</code>
   * value.
   * 
   * @param <DATATYPE>
   *        Any comparable object to be used. Both need to be of the same type.
   * @param aObj1
   *        First object to compare. May be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May be <code>null</code>.
   * @param bNullValuesComeFirst
   *        if <code>true</code> <code>null</code> values are ordered before
   *        non-<code>null</code> values
   * @return 0 if they are equal (or both <code>null</code>), -1 or +1.
   */
  public static <DATATYPE extends Comparable <? super DATATYPE>> int nullSafeCompare (@Nullable final DATATYPE aObj1,
                                                                                      @Nullable final DATATYPE aObj2,
                                                                                      final boolean bNullValuesComeFirst)
  {
    return aObj1 == aObj2 ? 0 : aObj1 == null ? (bNullValuesComeFirst ? -1 : +1)
                                             : aObj2 == null ? (bNullValuesComeFirst ? +1 : -1)
                                                            : aObj1.compareTo (aObj2);
  }

  /**
   * Compare the passed items and handle <code>null</code> values correctly. A
   * <code>null</code> value is always smaller than a non-<code>null</code>
   * value.
   * 
   * @param <DATATYPE>
   *        Any object to be used. Both need to be of the same type.
   * @param aObj1
   *        First object to compare. May be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May be <code>null</code>.
   * @param aComp
   *        The comparator to be used if both parameters are not
   *        <code>null</code>. The comparator itself may not be
   *        <code>null</code>.
   * @return 0 if they are equal (or both <code>null</code>), -1 or +1.
   */
  public static <DATATYPE> int nullSafeCompare (@Nullable final DATATYPE aObj1,
                                                @Nullable final DATATYPE aObj2,
                                                @Nonnull final Comparator <DATATYPE> aComp)
  {
    // Legacy behaviour: null values come first
    return nullSafeCompare (aObj1, aObj2, aComp, DEFAULT_NULL_VALUES_COME_FIRST);
  }

  /**
   * Compare the passed items and handle <code>null</code> values correctly. A
   * <code>null</code> value is always smaller than a non-<code>null</code>
   * value.
   * 
   * @param <DATATYPE>
   *        Any object to be used. Both need to be of the same type.
   * @param aObj1
   *        First object to compare. May be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May be <code>null</code>.
   * @param aComp
   *        The comparator to be used if both parameters are not
   *        <code>null</code>. The comparator itself may not be
   *        <code>null</code>.
   * @param bNullValuesComeFirst
   *        if <code>true</code> <code>null</code> values are ordered before
   *        non-<code>null</code> values
   * @return 0 if they are equal (or both <code>null</code>), -1 or +1.
   */
  public static <DATATYPE> int nullSafeCompare (@Nullable final DATATYPE aObj1,
                                                @Nullable final DATATYPE aObj2,
                                                @Nonnull final Comparator <DATATYPE> aComp,
                                                final boolean bNullValuesComeFirst)
  {
    return aObj1 == aObj2 ? 0 : aObj1 == null ? (bNullValuesComeFirst ? -1 : +1)
                                             : aObj2 == null ? (bNullValuesComeFirst ? +1 : -1) : aComp.compare (aObj1,
                                                                                                                 aObj2);
  }

  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Locale aSortLocale)
  {
    // Legacy behaviour: null values come first
    return nullSafeCompare (sStr1, sStr2, aSortLocale, DEFAULT_NULL_VALUES_COME_FIRST);
  }

  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Locale aSortLocale,
                                     final boolean bNullValuesComeFirst)
  {
    return nullSafeCompare (sStr1, sStr2, CollatorUtils.getCollatorSpaceBeforeDot (aSortLocale), bNullValuesComeFirst);
  }

  @SuppressFBWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Collator aCollator)
  {
    // Legacy behaviour: null values come first
    return nullSafeCompare (sStr1, sStr2, aCollator, DEFAULT_NULL_VALUES_COME_FIRST);
  }

  @SuppressFBWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Collator aCollator,
                                     final boolean bNullValuesComeFirst)
  {
    return sStr1 == sStr2 ? 0 : sStr1 == null ? (bNullValuesComeFirst ? -1 : +1)
                                             : sStr2 == null ? (bNullValuesComeFirst ? +1 : -1)
                                                            : aCollator.compare (sStr1, sStr2);
  }
}
