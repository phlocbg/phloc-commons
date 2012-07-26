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
package com.phloc.commons.mutable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ICloneable;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Object wrapper around a char so that it can be passed a final object but is
 * mutable.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MutableChar implements Comparable <MutableChar>, ICloneable <MutableChar>, IMutableInteger
{
  public static final char DEFAULT_VALUE = 0;

  private char m_cValue;

  /**
   * Initialize with default value {@value #DEFAULT_VALUE}.
   */
  public MutableChar ()
  {
    this (DEFAULT_VALUE);
  }

  /**
   * Initialize with a certain int value. If the value does not fit into a char,
   * the value is cut!
   * 
   * @param cValue
   *        The value to be used.
   */
  public MutableChar (final int cValue)
  {
    this ((char) cValue);
  }

  /**
   * Initialize with a certain value.
   * 
   * @param cValue
   *        The value to be used.
   */
  public MutableChar (final char cValue)
  {
    m_cValue = cValue;
  }

  public char charValue ()
  {
    return m_cValue;
  }

  @Nonnull
  public Character getAsCharacter ()
  {
    return Character.valueOf (m_cValue);
  }

  /**
   * Increment by 1 and return the modified value.
   * 
   * @return The by 1 incremented value.
   */
  public int inc ()
  {
    return inc (1);
  }

  public int inc (final int nDelta)
  {
    m_cValue += nDelta;
    return m_cValue;
  }

  public int dec ()
  {
    return inc (-1);
  }

  public int dec (final int nDelta)
  {
    return inc (-nDelta);
  }

  @Nonnull
  public EChange set (final int cValue)
  {
    return set ((char) cValue);
  }

  @Nonnull
  public EChange set (final char cValue)
  {
    if (m_cValue == cValue)
      return EChange.UNCHANGED;
    m_cValue = cValue;
    return EChange.CHANGED;
  }

  public boolean is0 ()
  {
    return m_cValue == 0;
  }

  public boolean isNot0 ()
  {
    return m_cValue != 0;
  }

  public boolean isSmaller0 ()
  {
    return m_cValue < 0;
  }

  public boolean isSmallerOrEqual0 ()
  {
    return m_cValue <= 0;
  }

  public boolean isGreater0 ()
  {
    return m_cValue > 0;
  }

  public boolean isGreaterOrEqual0 ()
  {
    return m_cValue >= 0;
  }

  public boolean isEven ()
  {
    return (m_cValue % 2) == 0;
  }

  public boolean isOdd ()
  {
    return (m_cValue % 2) != 0;
  }

  public int compareTo (final MutableChar rhs)
  {
    return m_cValue == rhs.m_cValue ? 0 : m_cValue < rhs.m_cValue ? -1 : +1;
  }

  @Nonnull
  public MutableChar getClone ()
  {
    return new MutableChar (m_cValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableChar))
      return false;
    final MutableChar rhs = (MutableChar) o;
    return m_cValue == rhs.m_cValue;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_cValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_cValue).toString ();
  }
}
