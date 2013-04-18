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

import com.phloc.commons.CDefault;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Object wrapper around a long so that it can be passed a final object but is
 * mutable.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MutableLong extends Number implements IMutableInteger <MutableLong>
{
  /** The default value if the default constructor is used. */
  public static final long DEFAULT_VALUE = CDefault.DEFAULT_LONG;

  private long m_nValue;

  /**
   * Initialize with default value {@value #DEFAULT_VALUE}.
   */
  public MutableLong ()
  {
    this (DEFAULT_VALUE);
  }

  /**
   * Initialize with a certain value.
   * 
   * @param aValue
   *        The value to be used.
   */
  public MutableLong (@Nonnull final Long aValue)
  {
    this (aValue.longValue ());
  }

  /**
   * Initialize with a certain value.
   * 
   * @param nValue
   *        The value to be used.
   */
  public MutableLong (final long nValue)
  {
    m_nValue = nValue;
  }

  @Override
  public long longValue ()
  {
    return m_nValue;
  }

  @Nonnull
  public Long getAsLong ()
  {
    return Long.valueOf (m_nValue);
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
  public int intValue ()
  {
    return (int) m_nValue;
  }

  /**
   * Increment by 1 and return the modified value.
   * 
   * @return The by 1 incremented value.
   */
  public long inc ()
  {
    return inc (1);
  }

  public long inc (final long nDelta)
  {
    m_nValue += nDelta;
    return m_nValue;
  }

  public long dec ()
  {
    return inc (-1);
  }

  public long dec (final long nDelta)
  {
    return inc (-nDelta);
  }

  @Nonnull
  public EChange set (final long nValue)
  {
    if (nValue == m_nValue)
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

  public int compareTo (@Nonnull final MutableLong rhs)
  {
    return CompareUtils.compare (m_nValue, rhs.m_nValue);
  }

  @Nonnull
  public MutableLong getClone ()
  {
    return new MutableLong (m_nValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableLong))
      return false;
    final MutableLong rhs = (MutableLong) o;
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
