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
package com.phloc.commons.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is a small helper class implementing {@link Enumeration} for array input
 * data.
 * 
 * @author philip
 * @param <ELEMENTTYPE>
 *        Type of object to iterate.
 */
@NotThreadSafe
public final class ArrayEnumeration <ELEMENTTYPE> implements Enumeration <ELEMENTTYPE>
{
  private ELEMENTTYPE [] m_aArray;
  private int m_nIndex;

  /**
   * Constructor iterating over the whole array
   * 
   * @param aArray
   *        The array to enumerate. May not be <code>null</code>.
   */
  public ArrayEnumeration (@Nonnull final ELEMENTTYPE... aArray)
  {
    this (aArray, 0, aArray.length);
  }

  /**
   * Constructor iterating an array partially.
   * 
   * @param aArray
   *        The array to be enumerated. May not be <code>null</code>.
   * @param nStartIndex
   *        The index of the first element to be enumerated.
   * @param nLength
   *        The maximum number of elements to be iterated.
   */
  public ArrayEnumeration (@Nonnull final ELEMENTTYPE [] aArray,
                           @Nonnegative final int nStartIndex,
                           @Nonnegative final int nLength)
  {
    if (aArray == null)
      throw new NullPointerException ("array");
    if (nStartIndex < 0)
      throw new IllegalArgumentException ("Start index may not be < 0: " + nStartIndex);
    if (nLength < 0)
      throw new IllegalArgumentException ("Length may not be < 0: " + nLength);
    m_nIndex = 0;
    m_aArray = ArrayHelper.getCopy (aArray, nStartIndex, nLength);
  }

  public boolean hasMoreElements ()
  {
    return m_nIndex < m_aArray.length;
  }

  @Nullable
  public ELEMENTTYPE nextElement ()
  {
    if (!hasMoreElements ())
      throw new NoSuchElementException ();
    final ELEMENTTYPE ret = m_aArray[m_nIndex];
    ++m_nIndex;
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ArrayEnumeration <?>))
      return false;
    final ArrayEnumeration <?> rhs = (ArrayEnumeration <?>) o;
    return EqualsUtils.nullSafeEquals (m_aArray, rhs.m_aArray) && m_nIndex == rhs.m_nIndex;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aArray).append (m_nIndex).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("array", m_aArray).append ("index", m_nIndex).toString ();
  }
}