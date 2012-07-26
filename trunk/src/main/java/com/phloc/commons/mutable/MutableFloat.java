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

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Object wrapper around a float so that it can be passed a final object but is
 * mutable.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MutableFloat implements IMutableNumeric <MutableFloat>
{
  public static final float DEFAULT_VALUE = 0.0f;

  private float m_fValue;

  /**
   * Initialize with default value {@value #DEFAULT_VALUE}
   */
  public MutableFloat ()
  {
    this (DEFAULT_VALUE);
  }

  public MutableFloat (final float fValue)
  {
    m_fValue = fValue;
  }

  public float floatValue ()
  {
    return m_fValue;
  }

  @Nonnull
  public Float getAsFloat ()
  {
    return Float.valueOf (m_fValue);
  }

  /**
   * Increment by 1 and return the modified value.
   * 
   * @return The by 1 incremented value.
   */
  public float inc ()
  {
    return inc (1f);
  }

  public float inc (final float fDelta)
  {
    m_fValue += fDelta;
    return m_fValue;
  }

  public float dec ()
  {
    return inc (-1f);
  }

  public float dec (final float fDelta)
  {
    return inc (-fDelta);
  }

  @Nonnull
  public EChange set (final float fValue)
  {
    if (EqualsUtils.equals (fValue, m_fValue))
      return EChange.UNCHANGED;
    m_fValue = fValue;
    return EChange.CHANGED;
  }

  public boolean is0 ()
  {
    return EqualsUtils.equals (m_fValue, 0f);
  }

  public boolean isNot0 ()
  {
    return !is0 ();
  }

  public boolean isSmaller0 ()
  {
    return Double.compare (m_fValue, 0f) < 0;
  }

  public boolean isSmallerOrEqual0 ()
  {
    return Double.compare (m_fValue, 0f) <= 0;
  }

  public boolean isGreater0 ()
  {
    return Double.compare (m_fValue, 0f) > 0;
  }

  public boolean isGreaterOrEqual0 ()
  {
    return Double.compare (m_fValue, 0f) >= 0;
  }

  public int compareTo (final MutableFloat rhs)
  {
    return Double.compare (m_fValue, rhs.m_fValue);
  }

  @Nonnull
  public MutableFloat getClone ()
  {
    return new MutableFloat (m_fValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MutableFloat))
      return false;
    final MutableFloat rhs = (MutableFloat) o;
    return EqualsUtils.equals (m_fValue, rhs.m_fValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_fValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_fValue).toString ();
  }
}
