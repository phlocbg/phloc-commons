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
package com.phloc.commons.collections.iterate;

import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is a small helper class for iterating over arrays of double.
 * 
 * @author Philip Helger
 */
public final class ArrayIteratorDouble
{
  private final double [] m_aArray;
  private int m_nIndex;

  public ArrayIteratorDouble (@Nonnull final double... aArray)
  {
    ValueEnforcer.notNull (aArray, "Array");
    m_nIndex = 0;
    m_aArray = ArrayHelper.getCopy (aArray);
  }

  @Deprecated
  public ArrayIteratorDouble (@Nonnull final double [] aArray,
                              @Nonnegative final int nBegin,
                              @Nonnegative final int nEnd)
  {
    ValueEnforcer.notNull (aArray, "Array");
    ValueEnforcer.isGE0 (nBegin, "Begin");
    ValueEnforcer.isGE0 (nEnd, "End");
    if (nEnd < nBegin)
      throw new IllegalArgumentException ("Begin (" + nBegin + ") must be between 0 and < end (" + nEnd + ")");
    m_nIndex = 0;

    final int nLength = nEnd - nBegin;
    m_aArray = ArrayHelper.getCopy (aArray, nBegin, nLength);
  }

  /**
   * Private constructor with offset and length
   * 
   * @param bUnused
   *        Marker to differentiate between the constructor with begin and end
   * @param aArray
   *        Source array
   * @param nOfs
   *        Offset. Must be &ge; 0.
   * @param nLength
   *        Lenght. Must be &ge; 0.
   */
  private ArrayIteratorDouble (final boolean bUnused,
                               @Nonnull final double [] aArray,
                               @Nonnegative final int nOfs,
                               @Nonnegative final int nLength)
  {
    ValueEnforcer.notNull (aArray, "Array");
    ValueEnforcer.isGE0 (nOfs, "Offset");
    ValueEnforcer.isGE0 (nLength, "Length");

    m_nIndex = 0;
    m_aArray = ArrayHelper.getCopy (aArray, nOfs, nLength);
  }

  public boolean hasNext ()
  {
    return m_nIndex < m_aArray.length;
  }

  public double next ()
  {
    if (!hasNext ())
      throw new NoSuchElementException ();
    return m_aArray[m_nIndex++];
  }

  @UnsupportedOperation
  @Deprecated
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ArrayIteratorDouble))
      return false;
    final ArrayIteratorDouble rhs = (ArrayIteratorDouble) o;
    return EqualsUtils.equals (m_aArray, rhs.m_aArray) && m_nIndex == rhs.m_nIndex;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aArray).append (m_nIndex).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("array", Arrays.toString (m_aArray))
                                       .append ("index", m_nIndex)
                                       .toString ();
  }

  @Nonnull
  public static ArrayIteratorDouble createOfsLen (@Nonnull final double [] aArray,
                                                  @Nonnegative final int nOfs,
                                                  @Nonnegative final int nLength)
  {
    return new ArrayIteratorDouble (true, aArray, nOfs, nLength);
  }

  @Nonnull
  public static ArrayIteratorDouble createBeginEnd (@Nonnull final double [] aArray,
                                                    @Nonnegative final int nBegin,
                                                    @Nonnegative final int nEnd)
  {
    return new ArrayIteratorDouble (aArray, nBegin, nEnd);
  }
}
