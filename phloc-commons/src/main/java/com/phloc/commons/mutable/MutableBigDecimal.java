/**
 * Copyright (C) 2006-2015 phloc systems
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
import com.phloc.commons.ValueEnforcer;
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
   * Initialize with default value 0
   */
  public MutableBigDecimal ()
  {
    this (DEFAULT_VALUE);
  }

  public MutableBigDecimal (@Nonnull final BigDecimal aValue)
  {
    this.m_aValue = ValueEnforcer.notNull (aValue, "Value");
  }

  @Nonnull
  public BigDecimal getAsBigDecimal ()
  {
    return this.m_aValue;
  }

  @Override
  public double doubleValue ()
  {
    return this.m_aValue.doubleValue ();
  }

  @Nonnull
  public Double getAsDouble ()
  {
    return Double.valueOf (doubleValue ());
  }

  @Override
  public float floatValue ()
  {
    return this.m_aValue.floatValue ();
  }

  @Override
  public int intValue ()
  {
    return this.m_aValue.intValue ();
  }

  @Override
  public long longValue ()
  {
    return this.m_aValue.longValue ();
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
    this.m_aValue = this.m_aValue.add (aDelta);
    return this.m_aValue;
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
    if (EqualsUtils.equals (aValue, this.m_aValue))
      return EChange.UNCHANGED;
    this.m_aValue = aValue;
    return EChange.CHANGED;
  }

  @Override
  public boolean is0 ()
  {
    return MathHelper.isEqualToZero (this.m_aValue);
  }

  @Override
  public boolean isNot0 ()
  {
    return MathHelper.isNotEqualToZero (this.m_aValue);
  }

  @Override
  public boolean isSmaller0 ()
  {
    return MathHelper.isLowerThanZero (this.m_aValue);
  }

  @Override
  public boolean isSmallerOrEqual0 ()
  {
    return MathHelper.isLowerOrEqualThanZero (this.m_aValue);
  }

  @Override
  public boolean isGreater0 ()
  {
    return MathHelper.isGreaterThanZero (this.m_aValue);
  }

  @Override
  public boolean isGreaterOrEqual0 ()
  {
    return MathHelper.isGreaterOrEqualThanZero (this.m_aValue);
  }

  @Override
  public int compareTo (@Nonnull final MutableBigDecimal rhs)
  {
    return this.m_aValue.compareTo (rhs.m_aValue);
  }

  @Override
  @Nonnull
  public MutableBigDecimal getClone ()
  {
    return new MutableBigDecimal (this.m_aValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableBigDecimal))
      return false;
    final MutableBigDecimal rhs = (MutableBigDecimal) o;
    return EqualsUtils.equals (this.m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", this.m_aValue).toString ();
  }
}
