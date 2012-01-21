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
package com.phloc.commons.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.ContainerHelper;

/**
 * Utility class for generating all possible combinations of elements for a
 * specified number of available slots. Duplicates in the passed elements will
 * be treated as different individuals and hence deliver duplicate result
 * solutions. This generator will only return complete result sets filling all
 * slots.
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        element type
 */
public final class CombinationGenerator <DATATYPE> implements Iterator <List <DATATYPE>>
{
  private final List <DATATYPE> m_aElements;
  private final int [] m_aIndexResult;
  private final BigInteger m_aTotalCombinations;
  private BigInteger m_aCombinationsLeft;
  private final boolean m_bUseLong;
  private final long m_nTotalCombinations;
  private long m_nCombinationsLeft;

  /**
   * Ctor
   * 
   * @param aElements
   *        the elements to fill into the slots for creating all combinations
   *        (must not be empty!)
   * @param nSlotCount
   *        the number of slots to use (must not be greater than the element
   *        count!)
   */
  public CombinationGenerator (@Nonnull final List <DATATYPE> aElements, @Nonnegative final int nSlotCount)
  {
    if (ContainerHelper.isEmpty (aElements))
      throw new IllegalArgumentException ("No elements passed");
    if (nSlotCount < 0)
      throw new IllegalArgumentException ("To small slot count: " + nSlotCount);
    if (nSlotCount > aElements.size ())
      throw new IllegalArgumentException ("To few elements for specified slots: " + aElements.size ());

    m_aElements = new ArrayList <DATATYPE> (aElements);
    m_aIndexResult = new int [nSlotCount];
    final BigInteger aElementFactorial = FactorialHelper.getAnyFactorialLinear (m_aElements.size ());
    final BigInteger aSlotFactorial = FactorialHelper.getAnyFactorialLinear (nSlotCount);
    final BigInteger aOverflowFactorial = FactorialHelper.getAnyFactorialLinear (m_aElements.size () - nSlotCount);
    m_aTotalCombinations = aElementFactorial.divide (aSlotFactorial.multiply (aOverflowFactorial));
    // Can we use the fallback to long?
    m_bUseLong = m_aTotalCombinations.compareTo (CGlobal.BIGINT_MAX_LONG) < 0;
    m_nTotalCombinations = m_bUseLong ? m_aTotalCombinations.longValue () : CGlobal.ILLEGAL_ULONG;
    reset ();
  }

  /**
   * Reset the generator
   */
  public void reset ()
  {
    for (int i = 0; i < m_aIndexResult.length; i++)
      m_aIndexResult[i] = i;
    m_aCombinationsLeft = m_aTotalCombinations;
    m_nCombinationsLeft = m_nTotalCombinations;
  }

  /**
   * @return number of combinations not yet generated
   */
  @Nonnull
  public BigInteger getCombinationsLeft ()
  {
    return m_bUseLong ? BigInteger.valueOf (m_nCombinationsLeft) : m_aCombinationsLeft;
  }

  /**
   * @return whether or not there are more combinations left
   */
  public boolean hasNext ()
  {
    return m_bUseLong ? m_nCombinationsLeft > 0 : m_aCombinationsLeft.compareTo (BigInteger.ZERO) > 0;
  }

  /**
   * @return total number of combinations
   */
  @Nonnull
  public BigInteger getTotalCombinations ()
  {
    return m_aTotalCombinations;
  }

  /**
   * Generate next combination (algorithm from Rosen p. 286)
   * 
   * @return the next combination as List of the size specified for slots filled
   *         with elements from the original list
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <DATATYPE> next ()
  {
    // Not for the very first item
    final boolean bFirstItem = m_bUseLong ? m_nCombinationsLeft == m_nTotalCombinations
                                         : m_aCombinationsLeft.equals (m_aTotalCombinations);
    if (!bFirstItem)
    {
      final int nElementCount = m_aElements.size ();
      final int nSlotCount = m_aIndexResult.length;

      int i = nSlotCount - 1;
      while (m_aIndexResult[i] == (nElementCount - nSlotCount + i))
      {
        i--;
      }
      m_aIndexResult[i]++;
      for (int j = i + 1; j < nSlotCount; j++)
      {
        m_aIndexResult[j] = m_aIndexResult[i] + j - i;
      }
    }

    // One combination less
    if (m_bUseLong)
      m_nCombinationsLeft--;
    else
      m_aCombinationsLeft = m_aCombinationsLeft.subtract (BigInteger.ONE);

    // Build result list
    final List <DATATYPE> aResult = new ArrayList <DATATYPE> (m_aIndexResult.length);
    for (final int nIndex : m_aIndexResult)
      aResult.add (m_aElements.get (nIndex));
    return aResult;
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }
}
