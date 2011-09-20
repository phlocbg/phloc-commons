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
package com.phloc.commons.mutable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ICloneable;
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
public final class MutableDouble implements Comparable <MutableDouble>, ICloneable <MutableDouble>
{
  public static final double DEFAULT_VALUE = 0;

  private double m_dValue;

  /**
   * Initialize with default value {@value #DEFAULT_VALUE}
   */
  public MutableDouble ()
  {
    this (DEFAULT_VALUE);
  }

  public MutableDouble (final double dValue)
  {
    m_dValue = dValue;
  }

  public double doubleValue ()
  {
    return m_dValue;
  }

  @Nonnull
  public Double getAsDouble ()
  {
    return Double.valueOf (m_dValue);
  }

  /**
   * Increment by 1 and return the modified value.
   * 
   * @return The by 1 incremented value.
   */
  public double inc ()
  {
    return inc (1);
  }

  public double inc (final double dDelta)
  {
    m_dValue += dDelta;
    return m_dValue;
  }

  public double dec ()
  {
    return inc (-1);
  }

  public double dec (final double nDelta)
  {
    return inc (-nDelta);
  }

  @Nonnull
  public EChange set (final double dValue)
  {
    if (CompareUtils.safeEquals (dValue, m_dValue))
      return EChange.UNCHANGED;
    m_dValue = dValue;
    return EChange.CHANGED;
  }

  public boolean is0 ()
  {
    return CompareUtils.safeEquals (m_dValue, 0);
  }

  public boolean isNot0 ()
  {
    return !is0 ();
  }

  public boolean isGreater0 ()
  {
    return Double.compare (m_dValue, 0) > 0;
  }

  public int compareTo (final MutableDouble rhs)
  {
    return Double.compare (m_dValue, rhs.m_dValue);
  }

  @Nonnull
  public MutableDouble getClone ()
  {
    return new MutableDouble (m_dValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableDouble))
      return false;
    final MutableDouble rhs = (MutableDouble) o;
    return CompareUtils.safeEquals (m_dValue, rhs.m_dValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_dValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_dValue).toString ();
  }
}
