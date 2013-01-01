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
package com.phloc.commons.mutable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Object wrapper around an int so that it can be passed a final object but is
 * mutable.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MutableInt extends Number implements IMutableInteger <MutableInt>
{
  public static final int DEFAULT_VALUE = 0;

  private int m_nValue;

  /**
   * Initialize with default value {@value #DEFAULT_VALUE}.
   */
  public MutableInt ()
  {
    this (DEFAULT_VALUE);
  }

  /**
   * Initialize with a certain value.
   * 
   * @param aValue
   *        The value to be used.
   */
  public MutableInt (@Nonnull final Integer aValue)
  {
    this (aValue.intValue ());
  }

  /**
   * Initialize with a certain value.
   * 
   * @param nValue
   *        The value to be used.
   */
  public MutableInt (final int nValue)
  {
    m_nValue = nValue;
  }

  @Override
  public int intValue ()
  {
    return m_nValue;
  }

  @Nonnull
  public Integer getAsInteger ()
  {
    return Integer.valueOf (m_nValue);
  }

  @Override
  public float floatValue ()
  {
    return m_nValue;
  }

  @Override
  public double doubleValue ()
  {
    return m_nValue;
  }

  @Override
  public long longValue ()
  {
    return m_nValue;
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
    m_nValue += nDelta;
    return m_nValue;
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
  public EChange set (final int nValue)
  {
    if (m_nValue == nValue)
      return EChange.UNCHANGED;
    m_nValue = nValue;
    return EChange.CHANGED;
  }

  public boolean is0 ()
  {
    return m_nValue == 0;
  }

  public boolean isNot0 ()
  {
    return m_nValue != 0;
  }

  public boolean isSmaller0 ()
  {
    return m_nValue < 0;
  }

  public boolean isSmallerOrEqual0 ()
  {
    return m_nValue <= 0;
  }

  public boolean isGreater0 ()
  {
    return m_nValue > 0;
  }

  public boolean isGreaterOrEqual0 ()
  {
    return m_nValue >= 0;
  }

  public boolean isEven ()
  {
    return (m_nValue % 2) == 0;
  }

  public boolean isOdd ()
  {
    return (m_nValue % 2) != 0;
  }

  public int compareTo (final MutableInt rhs)
  {
    return m_nValue == rhs.m_nValue ? 0 : m_nValue < rhs.m_nValue ? -1 : +1;
  }

  @Nonnull
  public MutableInt getClone ()
  {
    return new MutableInt (m_nValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableInt))
      return false;
    final MutableInt rhs = (MutableInt) o;
    return m_nValue == rhs.m_nValue;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_nValue).toString ();
  }
}
