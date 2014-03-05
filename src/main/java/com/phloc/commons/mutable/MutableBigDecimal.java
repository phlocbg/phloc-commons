/**
 * Copyright (C) 2006-2014 phloc systems
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

import java.math.BigDecimal;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.math.MathHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Object wrapper around a {@link BigDecimal} so that it can be passed a final
 * object but is mutable.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class MutableBigDecimal extends Number implements IMutableNumeric <MutableBigDecimal>
{
  /** The default value if the default constructor is used. */
  public static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;

  private BigDecimal m_aValue;

  /**
   * Initialize with default value {@value #DEFAULT_VALUE}
   */
  public MutableBigDecimal ()
  {
    this (DEFAULT_VALUE);
  }

  public MutableBigDecimal (@Nonnull final BigDecimal aValue)
  {
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aValue = aValue;
  }

  @Nonnull
  public BigDecimal getAsBigDecimal ()
  {
    return m_aValue;
  }

  @Override
  public double doubleValue ()
  {
    return m_aValue.doubleValue ();
  }

  @Nonnull
  public Double getAsDouble ()
  {
    return Double.valueOf (doubleValue ());
  }

  @Override
  public float floatValue ()
  {
    return m_aValue.floatValue ();
  }

  @Override
  public int intValue ()
  {
    return m_aValue.intValue ();
  }

  @Override
  public long longValue ()
  {
    return m_aValue.longValue ();
  }

  /**
   * Increment by 1 and return the modified value.
   * 
   * @return The by 1 incremented value.
   */
  @Nonnull
  public BigDecimal inc ()
  {
    return inc (BigDecimal.ONE);
  }

  @Nonnull
  public BigDecimal inc (@Nonnull final BigDecimal aDelta)
  {
    m_aValue = m_aValue.add (aDelta);
    return m_aValue;
  }

  @Nonnull
  public BigDecimal dec ()
  {
    return inc (CGlobal.BIGDEC_MINUS_ONE);
  }

  @Nonnull
  public BigDecimal dec (@Nonnull final BigDecimal aDelta)
  {
    return inc (aDelta.negate ());
  }

  @Nonnull
  public EChange set (@Nonnull final BigDecimal aValue)
  {
    if (EqualsUtils.equals (aValue, m_aValue))
      return EChange.UNCHANGED;
    m_aValue = aValue;
    return EChange.CHANGED;
  }

  public boolean is0 ()
  {
    return MathHelper.isEqualToZero (m_aValue);
  }

  public boolean isNot0 ()
  {
    return MathHelper.isNotEqualToZero (m_aValue);
  }

  public boolean isSmaller0 ()
  {
    return MathHelper.isLowerThanZero (m_aValue);
  }

  public boolean isSmallerOrEqual0 ()
  {
    return MathHelper.isLowerOrEqualThanZero (m_aValue);
  }

  public boolean isGreater0 ()
  {
    return MathHelper.isGreaterThanZero (m_aValue);
  }

  public boolean isGreaterOrEqual0 ()
  {
    return MathHelper.isGreaterOrEqualThanZero (m_aValue);
  }

  public int compareTo (@Nonnull final MutableBigDecimal rhs)
  {
    return m_aValue.compareTo (rhs.m_aValue);
  }

  @Nonnull
  public MutableBigDecimal getClone ()
  {
    return new MutableBigDecimal (m_aValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableBigDecimal))
      return false;
    final MutableBigDecimal rhs = (MutableBigDecimal) o;
    return EqualsUtils.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue).toString ();
  }
}
