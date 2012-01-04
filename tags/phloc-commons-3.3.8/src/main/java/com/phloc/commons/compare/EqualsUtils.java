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
package com.phloc.commons.compare;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * A small helper class that provides helper methods for easy
 * <code>equals</code> method generation
 * 
 * @author philip
 */
@Immutable
public final class EqualsUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final EqualsUtils s_aInstance = new EqualsUtils ();

  private EqualsUtils ()
  {}

  public static boolean equals (final boolean aObj1, final boolean aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final byte aObj1, final byte aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final char aObj1, final char aObj2)
  {
    return aObj1 == aObj2;
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
  public static boolean equals (final double aObj1, final double aObj2)
  {
    // Special overload for "double" required!
    return (aObj1 == aObj2) || (Double.doubleToLongBits (aObj1) == Double.doubleToLongBits (aObj2));
  }

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
  public static boolean equals (final float aObj1, final float aObj2)
  {
    // Special overload for "float" required!
    return (aObj1 == aObj2) || (Float.floatToIntBits (aObj1) == Float.floatToIntBits (aObj2));
  }

  public static boolean equals (final int aObj1, final int aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final long aObj1, final long aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final short aObj1, final short aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (@Nonnull final Object aObj1, @Nonnull final Object aObj2)
  {
    return aObj1.equals (aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  /**
   * Special equals implementation for BigDecimal because
   * <code>BigDecimal.equals</code> returns <code>false</code> if they have a
   * different scale so that "5.5" is not equal "5.50".
   * 
   * @param aObj1
   *        First BigDecimal. May not be <code>null</code>.
   * @param aObj2
   *        Second BigDecimal. May not be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean equals (@Nonnull final BigDecimal aObj1, @Nonnull final BigDecimal aObj2)
  {
    if (aObj1 == aObj2)
      return true;
    final int nMaxScale = Math.max (aObj1.scale (), aObj2.scale ());
    return aObj1.setScale (nMaxScale).equals (aObj2.setScale (nMaxScale));
  }

  /**
   * Special equals implementation for BigDecimal because
   * <code>BigDecimal.equals</code> returns <code>false</code> if they have a
   * different scale so that "5.5" is not equal "5.50".<br>
   * This version is <code>null</code>-safe.
   * 
   * @param aObj1
   *        First BigDecimal. May be <code>null</code>.
   * @param aObj2
   *        Second BigDecimal. May be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final BigDecimal aObj1, @Nullable final BigDecimal aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  public static boolean equals (@Nonnull final Double aObj1, @Nonnull final Double aObj2)
  {
    // Special overload for "Double" required!
    return aObj1.compareTo (aObj2) == 0;
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final Double aObj1, @Nullable final Double aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  public static boolean equals (@Nonnull final Float aObj1, @Nonnull final Float aObj2)
  {
    // Special overload for "Float" required!
    return aObj1.compareTo (aObj2) == 0;
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final Float aObj1, @Nullable final Float aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  public static boolean equals (@Nonnull final StringBuffer aObj1, @Nonnull final StringBuffer aObj2)
  {
    // StringBuffer does not implement equals!
    return aObj1.toString ().equals (aObj2.toString ());
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final StringBuffer aObj1, @Nullable final StringBuffer aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  public static boolean equals (@Nonnull final StringBuilder aObj1, @Nonnull final StringBuilder aObj2)
  {
    // StringBuilder does not implement equals!
    return aObj1.toString ().equals (aObj2.toString ());
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "RC_REF_COMPARISON" })
  public static boolean nullSafeEquals (@Nullable final StringBuilder aObj1, @Nullable final StringBuilder aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
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
  public static boolean equals (@Nonnull final URL aObj1, @Nonnull final URL aObj2)
  {
    // Avoid using the host lookup
    return aObj1.toExternalForm ().equals (aObj2.toExternalForm ());
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
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  public static boolean equals (@Nullable final Object [] aObj1, @Nullable final Object [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final Object [] aObj1, @Nullable final Object [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final BigDecimal [] aObj1, @Nonnull final BigDecimal [] aObj2)
  {
    if (aObj1 != aObj2)
    {
      if (aObj1 == null || aObj2 == null)
        return false;
      final int nLength = aObj1.length;
      if (nLength != aObj2.length)
        return false;
      for (int i = 0; i < nLength; i++)
        if (!nullSafeEquals (aObj1[i], aObj2[i]))
          return false;
    }
    return true;
  }

  public static boolean nullSafeEquals (@Nullable final BigDecimal [] aObj1, @Nullable final BigDecimal [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final Double [] aObj1, @Nonnull final Double [] aObj2)
  {
    if (aObj1 != aObj2)
    {
      if (aObj1 == null || aObj2 == null)
        return false;
      final int nLength = aObj1.length;
      if (nLength != aObj2.length)
        return false;
      for (int i = 0; i < nLength; i++)
        if (!nullSafeEquals (aObj1[i], aObj2[i]))
          return false;
    }
    return true;
  }

  public static boolean nullSafeEquals (@Nullable final Double [] aObj1, @Nullable final Double [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final Float [] aObj1, @Nonnull final Float [] aObj2)
  {
    if (aObj1 != aObj2)
    {
      if (aObj1 == null || aObj2 == null)
        return false;
      final int nLength = aObj1.length;
      if (nLength != aObj2.length)
        return false;
      for (int i = 0; i < nLength; i++)
        if (!nullSafeEquals (aObj1[i], aObj2[i]))
          return false;
    }
    return true;
  }

  public static boolean nullSafeEquals (@Nullable final Float [] aObj1, @Nullable final Float [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final StringBuffer [] aObj1, @Nonnull final StringBuffer [] aObj2)
  {
    if (aObj1 != aObj2)
    {
      if (aObj1 == null || aObj2 == null)
        return false;
      final int nLength = aObj1.length;
      if (nLength != aObj2.length)
        return false;
      for (int i = 0; i < nLength; i++)
        if (!nullSafeEquals (aObj1[i], aObj2[i]))
          return false;
    }
    return true;
  }

  public static boolean nullSafeEquals (@Nullable final StringBuffer [] aObj1, @Nullable final StringBuffer [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final StringBuilder [] aObj1, @Nonnull final StringBuilder [] aObj2)
  {
    if (aObj1 != aObj2)
    {
      if (aObj1 == null || aObj2 == null)
        return false;
      final int nLength = aObj1.length;
      if (nLength != aObj2.length)
        return false;
      for (int i = 0; i < nLength; i++)
        if (!nullSafeEquals (aObj1[i], aObj2[i]))
          return false;
    }
    return true;
  }

  public static boolean nullSafeEquals (@Nullable final StringBuilder [] aObj1, @Nullable final StringBuilder [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final URL [] aObj1, @Nonnull final URL [] aObj2)
  {
    if (aObj1 != aObj2)
    {
      if (aObj1 == null || aObj2 == null)
        return false;
      final int nLength = aObj1.length;
      if (nLength != aObj2.length)
        return false;
      for (int i = 0; i < nLength; i++)
        if (!nullSafeEquals (aObj1[i], aObj2[i]))
          return false;
    }
    return true;
  }

  public static boolean nullSafeEquals (@Nullable final URL [] aObj1, @Nullable final URL [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final boolean [] aObj1, @Nonnull final boolean [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final boolean [] aObj1, @Nullable final boolean [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final byte [] aObj1, @Nonnull final byte [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final byte [] aObj1, @Nullable final byte [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final char [] aObj1, @Nonnull final char [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final char [] aObj1, @Nullable final char [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final double [] aObj1, @Nonnull final double [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final double [] aObj1, @Nullable final double [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final float [] aObj1, @Nonnull final float [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final float [] aObj1, @Nullable final float [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final int [] aObj1, @Nonnull final int [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final int [] aObj1, @Nullable final int [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final long [] aObj1, @Nonnull final long [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final long [] aObj1, @Nullable final long [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final short [] aObj1, @Nonnull final short [] aObj2)
  {
    return Arrays.equals (aObj1, aObj2);
  }

  public static boolean nullSafeEquals (@Nullable final short [] aObj1, @Nullable final short [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static boolean nullSafeEqualsIgnoreCase (@Nullable final String sObj1, @Nullable final String sObj2)
  {
    return sObj1 == null ? sObj2 == null : sObj1.equalsIgnoreCase (sObj2);
  }
}
