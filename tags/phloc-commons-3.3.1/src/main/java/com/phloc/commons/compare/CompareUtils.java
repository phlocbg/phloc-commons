/**
 * Copyright (C) 2006-2011 phloc systems
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

import java.math.BigDecimal;
import java.net.URL;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * A small helper class that provides comparison methods that are
 * <code>null</code> safe. Also provides secure ways to compare double and float
 * values.
 * 
 * @author philip
 */
@Immutable
public final class CompareUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CompareUtils s_aInstance = new CompareUtils ();

  private CompareUtils ()
  {}

  /**
   * Check if two float values are equal. This is necessary, because in some
   * cases, the "==" operator returns wrong results.
   * 
   * @param aObj1
   *        First float
   * @param aObj2
   *        Second float
   * @return <code>true</code> if they are equal.
   */
  public static boolean safeEquals (final float aObj1, final float aObj2)
  {
    // Special overload for "float" required!
    return (aObj1 == aObj2) || (Float.floatToIntBits (aObj1) == Float.floatToIntBits (aObj2));
  }

  /**
   * Check if two double values are equal. This is necessary, because in some
   * cases, the "==" operator returns wrong results.
   * 
   * @param aObj1
   *        First double
   * @param aObj2
   *        Second double
   * @return <code>true</code> if they are equal.
   */
  public static boolean safeEquals (final double aObj1, final double aObj2)
  {
    // Special overload for "double" required!
    return (aObj1 == aObj2) || (Double.doubleToLongBits (aObj1) == Double.doubleToLongBits (aObj2));
  }

  /**
   * Special equals implementation for URLs because <code>URL.equals</code>
   * performs a host lookup.<br>
   * <a href=
   * "http://michaelscharf.blogspot.com/2006/11/javaneturlequals-and-hashcode-make.html"
   * >Click here for details</a>
   * 
   * @param aObj1
   *        First URL. May not be <code>null</code>.
   * @param aObj2
   *        Second URL. May not be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean safeEquals (@Nonnull final URL aObj1, @Nonnull final URL aObj2)
  {
    return (aObj1 == aObj2) || aObj1.toExternalForm ().equals (aObj2.toExternalForm ());
  }

  /**
   * Special equals implementation for URLs because <code>URL.equals</code>
   * performs a host lookup.<br>
   * <a href=
   * "http://michaelscharf.blogspot.com/2006/11/javaneturlequals-and-hashcode-make.html"
   * >Click here for details</a>. This version is <code>null</code>-safe.
   * 
   * @param aObj1
   *        First URL. May be <code>null</code>.
   * @param aObj2
   *        Second URL. May be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean nullSafeEquals (@Nullable final URL aObj1, @Nullable final URL aObj2)
  {
    return (aObj1 == aObj2) ||
           (aObj1 != null && aObj2 != null && aObj1.toExternalForm ().equals (aObj2.toExternalForm ()));
  }

  /**
   * Special equals implementation for BigDecimal because
   * <code>BigDecimal.equals</code> returns false if they have a different scale
   * so that "5.5" is not equal "5.50".
   * 
   * @param aObj1
   *        First BigDecimal. May not be <code>null</code>.
   * @param aObj2
   *        Second BigDecimal. May not be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean safeEquals (@Nonnull final BigDecimal aObj1, @Nonnull final BigDecimal aObj2)
  {
    if (aObj1 == aObj2)
      return true;
    final int nMaxScale = Math.max (aObj1.scale (), aObj2.scale ());
    return aObj1.setScale (nMaxScale).equals (aObj2.setScale (nMaxScale));
  }

  /**
   * Special equals implementation for BigDecimal because
   * <code>BigDecimal.equals</code> returns false if they have a different scale
   * so that "5.5" is not equal "5.50".<br>
   * This version is <code>null</code>-safe.
   * 
   * @param aObj1
   *        First BigDecimal. May be <code>null</code>.
   * @param aObj2
   *        Second BigDecimal. May be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean nullSafeEquals (@Nullable final BigDecimal aObj1, @Nullable final BigDecimal aObj2)
  {
    if (aObj1 == aObj2)
      return true;
    if (aObj1 == null || aObj2 == null)
      return false;
    final int nMaxScale = Math.max (aObj1.scale (), aObj2.scale ());
    return aObj1.setScale (nMaxScale).equals (aObj2.setScale (nMaxScale));
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
    return aObj1 == aObj2 ? 0 : aObj1 == null ? -1 : aObj2 == null ? +1 : aObj1.compareTo (aObj2);
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
    if (aObj1 == aObj2)
      return 0;
    return aObj1 == null ? -1 : aObj2 == null ? +1 : aComp.compare (aObj1, aObj2);
  }

  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Locale aSortLocale)
  {
    return nullSafeCompare (sStr1, sStr2, CollatorUtils.getCollatorSpaceBeforeDot (aSortLocale));
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Collator aCollator)
  {
    if (sStr1 == sStr2)
      return 0;
    return sStr1 == null ? -1 : sStr2 == null ? +1 : aCollator.compare (sStr1, sStr2);
  }

  /**
   * Compare both object for equality. If both objects are <code>null</code>
   * they are considered equal as well.
   * 
   * @param aObj1
   *        The first object to compare. May be <code>null</code>.
   * @param aObj2
   *        The second object to compare. May be <code>null</code>.
   * @return <code>true</code> if both are null or both are equal,
   *         <code>false</code> otherwise.
   */
  public static boolean nullSafeEquals (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return aObj1 == null ? aObj2 == null : aObj1.equals (aObj2);
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final Float aObj1, @Nullable final Float aObj2)
  {
    // Special overload for "Float" required!
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && aObj1.compareTo (aObj2) == 0);
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final Double aObj1, @Nullable final Double aObj2)
  {
    // Special overload for "Double" required!
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && aObj1.compareTo (aObj2) == 0);
  }

  public static boolean nullSafeEquals (@Nullable final Object [] aObj1, @Nullable final Object [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final boolean [] aObj1, @Nullable final boolean [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final byte [] aObj1, @Nullable final byte [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final char [] aObj1, @Nullable final char [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final double [] aObj1, @Nullable final double [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final float [] aObj1, @Nullable final float [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final int [] aObj1, @Nullable final int [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final long [] aObj1, @Nullable final long [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final short [] aObj1, @Nullable final short [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : Arrays.equals (aObj1, aObj2);
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static boolean nullSafeEqualsIgnoreCase (@Nullable final String sObj1, @Nullable final String sObj2)
  {
    return sObj1 == null ? sObj2 == null : sObj1.equalsIgnoreCase (sObj2);
  }
}
