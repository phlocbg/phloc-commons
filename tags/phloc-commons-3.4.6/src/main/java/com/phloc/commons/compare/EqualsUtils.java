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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.lang.ClassHelper;

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

  /**
   * Special equals implementation for AtomicBoolean because it does not
   * implement equals.
   * 
   * @param aObj1
   *        First AtomicBoolean. May not be <code>null</code>.
   * @param aObj2
   *        Second AtomicBoolean. May not be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean equals (@Nonnull final AtomicBoolean aObj1, @Nonnull final AtomicBoolean aObj2)
  {
    return aObj1.get () == aObj2.get ();
  }

  /**
   * Special equals implementation for AtomicBoolean because it does not
   * implement equals.
   * 
   * @param aObj1
   *        First AtomicBoolean. May be <code>null</code>.
   * @param aObj2
   *        Second AtomicBoolean. May be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean nullSafeEquals (@Nullable final AtomicBoolean aObj1, @Nullable final AtomicBoolean aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  /**
   * Special equals implementation for AtomicInteger because it does not
   * implement equals.
   * 
   * @param aObj1
   *        First AtomicInteger. May not be <code>null</code>.
   * @param aObj2
   *        Second AtomicInteger. May not be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean equals (@Nonnull final AtomicInteger aObj1, @Nonnull final AtomicInteger aObj2)
  {
    return aObj1.get () == aObj2.get ();
  }

  /**
   * Special equals implementation for AtomicInteger because it does not
   * implement equals.
   * 
   * @param aObj1
   *        First AtomicInteger. May be <code>null</code>.
   * @param aObj2
   *        Second AtomicInteger. May be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean nullSafeEquals (@Nullable final AtomicInteger aObj1, @Nullable final AtomicInteger aObj2)
  {
    return (aObj1 == aObj2) || (aObj1 != null && aObj2 != null && equals (aObj1, aObj2));
  }

  /**
   * Special equals implementation for AtomicLong because it does not implement
   * equals.
   * 
   * @param aObj1
   *        First AtomicLong. May not be <code>null</code>.
   * @param aObj2
   *        Second AtomicLong. May not be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean equals (@Nonnull final AtomicLong aObj1, @Nonnull final AtomicLong aObj2)
  {
    return aObj1.get () == aObj2.get ();
  }

  /**
   * Special equals implementation for AtomicLong because it does not implement
   * equals.
   * 
   * @param aObj1
   *        First AtomicLong. May be <code>null</code>.
   * @param aObj2
   *        Second AtomicLong. May be <code>null</code>.
   * @return <code>true</code> if they are equals, <code>false</code> otherwise.
   */
  public static boolean nullSafeEquals (@Nullable final AtomicLong aObj1, @Nullable final AtomicLong aObj2)
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

  public static boolean equals (@Nonnull final AtomicBoolean [] aObj1, @Nonnull final AtomicBoolean [] aObj2)
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

  public static boolean nullSafeEquals (@Nullable final AtomicBoolean [] aObj1, @Nullable final AtomicBoolean [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final AtomicInteger [] aObj1, @Nonnull final AtomicInteger [] aObj2)
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

  public static boolean nullSafeEquals (@Nullable final AtomicInteger [] aObj1, @Nullable final AtomicInteger [] aObj2)
  {
    return aObj1 == null ? aObj2 == null : equals (aObj1, aObj2);
  }

  public static boolean equals (@Nonnull final AtomicLong [] aObj1, @Nonnull final AtomicLong [] aObj2)
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

  public static boolean nullSafeEquals (@Nullable final AtomicLong [] aObj1, @Nullable final AtomicLong [] aObj2)
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

  private static boolean _equalsTypeSpecific (@Nullable final Object aObj1,
                                              @Nullable final Object aObj2,
                                              final boolean bNullable,
                                              final boolean bRecursivelyHandleCollections)
  {
    if (aObj1 == aObj2)
      return true;

    if (bNullable && (aObj1 == null || aObj2 == null))
      return false;

    final Class <?> aClass = aObj1.getClass ();

    // If types are not equal, objects cannot be equal
    if (!aClass.equals (aObj2.getClass ()))
      return false;

    if (aClass.equals (AtomicBoolean.class))
      return equals ((AtomicBoolean) aObj1, (AtomicBoolean) aObj2);
    if (aClass.equals (AtomicBoolean [].class))
      return equals ((AtomicBoolean []) aObj1, (AtomicBoolean []) aObj2);
    if (aClass.equals (AtomicInteger.class))
      return equals ((AtomicInteger) aObj1, (AtomicInteger) aObj2);
    if (aClass.equals (AtomicInteger [].class))
      return equals ((AtomicInteger []) aObj1, (AtomicInteger []) aObj2);
    if (aClass.equals (AtomicLong.class))
      return equals ((AtomicLong) aObj1, (AtomicLong) aObj2);
    if (aClass.equals (AtomicLong [].class))
      return equals ((AtomicLong []) aObj1, (AtomicLong []) aObj2);
    if (aClass.equals (BigDecimal.class))
      return equals ((BigDecimal) aObj1, (BigDecimal) aObj2);
    if (aClass.equals (BigDecimal [].class))
      return equals ((BigDecimal []) aObj1, (BigDecimal []) aObj2);
    if (aClass.equals (boolean [].class))
      return equals ((boolean []) aObj1, (boolean []) aObj2);
    if (aClass.equals (byte [].class))
      return equals ((byte []) aObj1, (byte []) aObj2);
    if (aClass.equals (char [].class))
      return equals ((char []) aObj1, (char []) aObj2);
    if (aClass.equals (Double.class))
      return equals ((Double) aObj1, (Double) aObj2);
    if (aClass.equals (double [].class))
      return equals ((double []) aObj1, (double []) aObj2);
    if (aClass.equals (Double [].class))
      return equals ((Double []) aObj1, (Double []) aObj2);
    if (aClass.equals (Float.class))
      return equals ((Float) aObj1, (Float) aObj2);
    if (aClass.equals (float [].class))
      return equals ((float []) aObj1, (float []) aObj2);
    if (aClass.equals (Float [].class))
      return equals ((Float []) aObj1, (Float []) aObj2);
    if (aClass.equals (int [].class))
      return equals ((int []) aObj1, (int []) aObj2);
    if (aClass.equals (long [].class))
      return equals ((long []) aObj1, (long []) aObj2);
    if (aClass.equals (short [].class))
      return equals ((short []) aObj1, (short []) aObj2);
    if (aClass.equals (StringBuffer.class))
      return equals ((StringBuffer) aObj1, (StringBuffer) aObj2);
    if (aClass.equals (StringBuffer [].class))
      return equals ((StringBuffer []) aObj1, (StringBuffer []) aObj2);
    if (aClass.equals (StringBuilder.class))
      return equals ((StringBuilder) aObj1, (StringBuilder) aObj2);
    if (aClass.equals (StringBuilder [].class))
      return equals ((StringBuilder []) aObj1, (StringBuilder []) aObj2);
    if (aClass.equals (URL.class))
      return equals ((URL) aObj1, (URL) aObj2);
    if (aClass.equals (URL [].class))
      return equals ((URL []) aObj1, (URL []) aObj2);

    if (bRecursivelyHandleCollections)
    {
      if (aObj1 instanceof Collection <?>)
      {
        // Special handling for collections
        final Collection <?> aColl1 = (Collection <?>) aObj1;
        final Collection <?> aColl2 = (Collection <?>) aObj2;
        if (aColl1.size () != aColl2.size ())
          return false;

        if (aObj1 instanceof Set <?>)
        {
          // Special handling, because order is undefined
          return aObj1.equals (aObj2);
        }

        // Lists and queues with defined order
        final Iterator <?> it1 = aColl1.iterator ();
        final Iterator <?> it2 = aColl2.iterator ();
        while (it1.hasNext ())
        {
          final Object aChild1 = it1.next ();
          final Object aChild2 = it2.next ();

          // Recursive call
          if (!_equalsTypeSpecific (aChild1, aChild2, bNullable, bRecursivelyHandleCollections))
            return false;
        }
        return true;
      }

      if (aObj1 instanceof Map <?, ?>)
      {
        // Special handling for collections
        final Map <?, ?> aMap1 = (Map <?, ?>) aObj1;
        final Map <?, ?> aMap2 = (Map <?, ?>) aObj2;
        if (aMap1.size () != aMap2.size ())
          return false;

        // The order may be different, even if the contained elements are
        // identical (e.g. in case of has collisions in a HashMap)
        for (final Entry <?, ?> aEntry1 : aMap1.entrySet ())
        {
          final Object aKey1 = aEntry1.getKey ();
          final Object aValue2 = aMap2.get (aKey1);
          if (aValue2 == null && !aMap2.containsKey (aKey1))
          {
            // No such key1 in collection2
            return false;
          }

          // Recursive call for value
          if (!_equalsTypeSpecific (aEntry1.getValue (), aValue2, bNullable, bRecursivelyHandleCollections))
            return false;
        }
        return true;
      }
    }

    // For array -> use generic Arrays.equals
    if (ClassHelper.isArrayClass (aClass))
      return equals ((Object []) aObj1, (Object []) aObj2);

    // All other classes use the default equals(Object,Object)
    return equals (aObj1, aObj2);
  }

  /**
   * This is a specific version of an equals implementation that looks for the
   * type of the objects and than invokes the correct method. E.g. if the passed
   * objects are both of type {@link URL} the special overload
   * {@link #equals(URL, URL)} is invoked. Also if the passed element is a
   * {@link Collection} or a {@link Map} than all contained elements are
   * recursively checked, using this method.
   * 
   * @param aObj1
   *        First object to compare. May not be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May not be <code>null</code>.
   * @return <code>true</code> if they are equal, <code>false</code> otherwise
   */
  public static boolean equalsTypeSpecific (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return _equalsTypeSpecific (aObj1, aObj2, false, true);
  }

  /**
   * This is a specific version of an equals implementation that looks for the
   * type of the objects and than invokes the correct method. E.g. if the passed
   * objects are both of type {@link URL} the special overload
   * {@link #equals(URL, URL)} is invoked. This version contains no specific
   * handling for {@link Collection} and {@link Map} objects.
   * 
   * @param aObj1
   *        First object to compare. May not be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May not be <code>null</code>.
   * @return <code>true</code> if they are equal, <code>false</code> otherwise
   */
  public static boolean equalsTypeSpecificNoCollections (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return _equalsTypeSpecific (aObj1, aObj2, false, false);
  }

  public static boolean nullSafeEqualsTypeSpecific (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return _equalsTypeSpecific (aObj1, aObj2, true, true);
  }

  public static boolean nullSafeEqualsTypeSpecificNoCollections (@Nullable final Object aObj1,
                                                                 @Nullable final Object aObj2)
  {
    return _equalsTypeSpecific (aObj1, aObj2, true, false);
  }
}
