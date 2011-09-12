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
package com.phloc.commons.hash;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.exceptions.LoggedRuntimeException;

/**
 * A small hash code creation class based on the article found in the net. See
 * <a href=
 * "http://www.angelikalanger.com/Articles/JavaSpektrum/03.HashCode/03.HashCode.html"
 * >this article</a> for details.<br>
 * After calling {@link #append(Object)} for all objects use
 * {@link #getHashCode()} to retrieve the calculated hash code. Once the hash
 * code was calculated no modifications are allowed.
 * 
 * @author philip
 */
@NotThreadSafe
public final class HashCodeGenerator implements IHashCodeGenerator
{
  /** Use a prime number as the start. */
  private static final int HASHCODE_INITIAL = 17;

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

  /**
   * Once the hash code generation has been queried, no further changes may be
   * done. This flag indicates, whether new items can be added or not.
   */
  private boolean m_bClosed = false;

  /** The current hash code value. */
  private int m_nHC = HASHCODE_INITIAL;

  /**
   * This is a sanity constructor that allows for any object to be passed in the
   * constructor (e.g. <code>this</code>) from which the class is extracted as
   * the initial value of the hash code.
   * 
   * @param aSrcObject
   *        The source object from which the class is extracted. May not be
   *        <code>null</code>.
   */
  public HashCodeGenerator (@Nonnull final Object aSrcObject)
  {
    this (aSrcObject instanceof Class <?> ? (Class <?>) aSrcObject : aSrcObject.getClass ());
  }

  /**
   * This constructor requires a class name, because in case a class has no
   * instance variables the hash code may be the same for different instances of
   * different classes.
   * 
   * @param aClass
   *        The class this instance is about to create a hash code for. May not
   *        be <code>null</code>.
   */
  public HashCodeGenerator (@Nonnull final Class <?> aClass)
  {
    if (aClass == null)
      throw new NullPointerException ("class");
    // Use the class name
    append (aClass.getName ());

    // Is it an array class? If so add the component class name.
    final Class <?> aComponentType = aClass.getComponentType ();
    if (aComponentType != null)
      append (aComponentType.getName ());
  }

  private HashCodeGenerator (final int nSuperHashCode)
  {
    m_nHC = nSuperHashCode;
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final boolean x)
  {
    return append (x ? 1231 : 1237);
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final byte x)
  {
    return append ((int) x);
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final char x)
  {
    return append ((int) x);
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final double x)
  {
    return append (x == 0.0 ? 0L : Double.doubleToLongBits (x));
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final float x)
  {
    return append (x == 0.0F ? 0 : Float.floatToIntBits (x));
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final int x)
  {
    if (m_bClosed)
      throw new IllegalStateException ("Hash code cannot be changed anymore!");
    m_nHC = m_nHC * MULTIPLIER + x;

    if (m_nHC == ILLEGAL_HASHCODE)
      throw new LoggedRuntimeException ("yippie");

    return this;
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final long x)
  {
    append ((int) (x >>> 32));
    return append ((int) (x & 0xffffffff));
  }

  /**
   * Atomic type hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (final short x)
  {
    return append ((int) x);
  }

  /**
   * Object hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final Enum <?> x)
  {
    return append (x == null ? HASHCODE_NULL : x.hashCode ());
  }

  /**
   * Object hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final Object x)
  {
    return append (x == null ? HASHCODE_NULL : x.hashCode ());
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final boolean [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final byte [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final char [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final double [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final float [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final int [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final long [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final short [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final Enum <?> [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Array hash code generation.
   * 
   * @param x
   *        Array to add
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final Object [] x)
  {
    return append (x == null ? HASHCODE_NULL : Arrays.hashCode (x));
  }

  /**
   * Type specific hash code generation because parameter class has no
   * overloaded equals method.
   * 
   * @param x
   *        object to add
   * @return this
   */
  @Nonnull
  public IHashCodeGenerator append (@Nullable final StringBuffer x)
  {
    return append (x == null ? HASHCODE_NULL : x.toString ().hashCode ());
  }

  /**
   * Type specific hash code generation because parameter class has no
   * overloaded equals method.
   * 
   * @param x
   *        object to add
   * @return this
   */
  @Nonnull
  public IHashCodeGenerator append (@Nullable final StringBuilder x)
  {
    return append (x == null ? HASHCODE_NULL : x.toString ().hashCode ());
  }

  /**
   * @param x
   *        to be included in the hash code generation.
   * @return this
   */
  @Nonnull
  public HashCodeGenerator append (@Nullable final Iterable <?> x)
  {
    if (x == null)
      return append (HASHCODE_NULL);
    for (final Object aItem : x)
      append (aItem);
    return this;
  }

  /**
   * Retrieve the final hash code. Once this method has been called, no further
   * calls to append can be done since the hash value is locked!
   * 
   * @return The finally completed hash code. The returned value is never
   *         {@link #ILLEGAL_HASHCODE}. If the calculated hash code would be
   *         {@link #ILLEGAL_HASHCODE} it is changed to -1 instead.
   */
  public int getHashCode ()
  {
    m_bClosed = true;

    // This is for the very rare case, that the calculated hash code results in
    // an illegal value.
    if (m_nHC == ILLEGAL_HASHCODE)
      m_nHC = -1;
    return m_nHC;
  }

  /**
   * @return The same as {@link #getHashCode()} but as an {@link Integer}
   *         object. Never <code>null</code>.
   */
  @Nonnull
  public Integer getHashCodeObj ()
  {
    return Integer.valueOf (getHashCode ());
  }

  /**
   * Create a {@link HashCodeGenerator} for derived classes where the base class
   * also uses the {@link HashCodeGenerator}. This avoid calculating the hash
   * code of the class name more than once.
   * 
   * @param nSuperHashCode
   *        Always pass in <code>super.hashCode ()</code>
   * @return Never <code>null</code>
   */
  @Nonnull
  public static HashCodeGenerator getDerived (final int nSuperHashCode)
  {
    if (nSuperHashCode == ILLEGAL_HASHCODE)
      throw new IllegalArgumentException ("Passed hash code is invalid!");
    return new HashCodeGenerator (nSuperHashCode);
  }
}
