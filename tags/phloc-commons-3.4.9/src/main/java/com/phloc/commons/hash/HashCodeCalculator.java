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
package com.phloc.commons.hash;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * This class provides the hash code generation for different data types.
 * 
 * @author philip
 */
@Immutable
public final class HashCodeCalculator
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final HashCodeCalculator s_aInstance = new HashCodeCalculator ();

  /**
   * Each value is multiplied with this value. 31 because it can easily be
   * optimized to <code>(1 &lt;&lt; 5) - 1</code>.
   */
  private static final int MULTIPLIER = 31;

  /**
   * The hash code value to be used for <code>null</code> values. Do not use 0
   * as e.g. <code>BigDecimal ("0")</code> also results in a 0 hash code.
   */
  private static final int HASHCODE_NULL = 129;

  private HashCodeCalculator ()
  {}

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final boolean x)
  {
    return append (nPrevHashCode, x ? 1231 : 1237);
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final byte x)
  {
    return append (nPrevHashCode, (int) x);
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final char x)
  {
    return append (nPrevHashCode, (int) x);
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final double x)
  {
    return append (nPrevHashCode, x == 0.0 ? 0L : Double.doubleToLongBits (x));
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final float x)
  {
    return append (nPrevHashCode, x == 0.0F ? 0 : Float.floatToIntBits (x));
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final int x)
  {
    return nPrevHashCode * MULTIPLIER + x;
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final long x)
  {
    final int nTemp = append (nPrevHashCode, (int) (x >>> 32));
    return append (nTemp, (int) (x & 0xffffffff));
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, final short x)
  {
    return append (nPrevHashCode, (int) x);
  }

  /**
   * Object hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final Object x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    if (x.getClass ().isArray ())
    {
      // Handle contained array type correctly
      final Class <?> aComponentType = x.getClass ().getComponentType ();
      if (aComponentType.equals (boolean.class))
        return append (nPrevHashCode, (boolean []) x);
      if (aComponentType.equals (byte.class))
        return append (nPrevHashCode, (byte []) x);
      if (aComponentType.equals (char.class))
        return append (nPrevHashCode, (char []) x);
      if (aComponentType.equals (double.class))
        return append (nPrevHashCode, (double []) x);
      if (aComponentType.equals (float.class))
        return append (nPrevHashCode, (float []) x);
      if (aComponentType.equals (int.class))
        return append (nPrevHashCode, (int []) x);
      if (aComponentType.equals (long.class))
        return append (nPrevHashCode, (long []) x);
      if (aComponentType.equals (short.class))
        return append (nPrevHashCode, (short []) x);

      // Generic object array
      return append (nPrevHashCode, (Object []) x);
    }

    // Special cases
    if (x instanceof Iterable <?>)
      return append (nPrevHashCode, (Iterable <?>) x);
    if (x instanceof Map <?, ?>)
      return append (nPrevHashCode, (Map <?, ?>) x);
    if (x instanceof StringBuilder)
      return append (nPrevHashCode, (StringBuilder) x);
    if (x instanceof StringBuffer)
      return append (nPrevHashCode, (StringBuffer) x);

    return append (nPrevHashCode, x.hashCode ());
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final boolean [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final boolean aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final byte [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final byte aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final char [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final char aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final double [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final double aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final float [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final float aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final int [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final int aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final long [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final long aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final short [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ());
    for (final short aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Array hash code generation.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        Array to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final Object [] x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element arrays
    int ret = append (nPrevHashCode, x.getClass ().hashCode ());
    ret = append (ret, x.length);
    for (final Object aObject : x)
      ret = append (ret, aObject);
    return ret;
  }

  /**
   * Type specific hash code generation because parameter class has no
   * overloaded equals method.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        object to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final StringBuffer x)
  {
    return append (nPrevHashCode, x == null ? HASHCODE_NULL : x.toString ().hashCode ());
  }

  /**
   * Type specific hash code generation because parameter class has no
   * overloaded equals method.
   * 
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        object to add
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final StringBuilder x)
  {
    return append (nPrevHashCode, x == null ? HASHCODE_NULL : x.toString ().hashCode ());
  }

  /**
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        to be included in the hash code generation.
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final Iterable <?> x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element objects
    int ret = append (nPrevHashCode, x.hashCode ());
    for (final Object aItem : x)
      ret = append (ret, aItem);
    return ret;
  }

  /**
   * @param nPrevHashCode
   *        The previous hash code used as the basis for calculation
   * @param x
   *        to be included in the hash code generation.
   * @return The updated hash code
   */
  @Nonnull
  public static int append (final int nPrevHashCode, @Nullable final Map <?, ?> x)
  {
    if (x == null)
      return append (nPrevHashCode, HASHCODE_NULL);

    // Append class to ensure change for 0-element objects
    int ret = append (nPrevHashCode, x.hashCode ());
    for (final Map.Entry <?, ?> aEntry : x.entrySet ())
    {
      ret = append (ret, aEntry.getKey ());
      ret = append (ret, aEntry.getValue ());
    }
    return ret;
  }
}
