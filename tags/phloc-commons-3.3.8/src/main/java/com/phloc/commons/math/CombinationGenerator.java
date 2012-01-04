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
  private final List <DATATYPE> m_aElements = new ArrayList <DATATYPE> ();
  private final int m_nSlotCount;
  private final int [] m_aIndexResult;
  private BigInteger m_aCombinationsLeft;
  private final BigInteger m_aTotalCombinations;

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

    m_aElements.addAll (aElements);
    m_nSlotCount = nSlotCount;
    m_aIndexResult = new int [nSlotCount];
    final BigInteger aElementFactorial = FactorialHelper.getAnyFactorialLinear (m_aElements.size ());
    final BigInteger aSlotFactorial = FactorialHelper.getAnyFactorialLinear (nSlotCount);
    final BigInteger nOverflowFactorial = FactorialHelper.getAnyFactorialLinear (m_aElements.size () - nSlotCount);
    m_aTotalCombinations = aElementFactorial.divide (aSlotFactorial.multiply (nOverflowFactorial));
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
  }

  /**
   * @return number of combinations not yet generated
   */
  @Nonnull
  public BigInteger getCombinationsLeft ()
  {
    return m_aCombinationsLeft;
  }

  /**
   * @return whether or not there are more combinations left
   */
  public boolean hasNext ()
  {
    return m_aCombinationsLeft.compareTo (BigInteger.ZERO) > 0;
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
    if (!m_aCombinationsLeft.equals (m_aTotalCombinations))
    {
      // Not for the very first item
      int i = m_nSlotCount - 1;
      while (m_aIndexResult[i] == m_aElements.size () - m_nSlotCount + i)
      {
        i--;
      }
      m_aIndexResult[i] = m_aIndexResult[i] + 1;
      for (int j = i + 1; j < m_nSlotCount; j++)
      {
        m_aIndexResult[j] = m_aIndexResult[i] + j - i;
      }
    }

    // One combination less
    m_aCombinationsLeft = m_aCombinationsLeft.subtract (BigInteger.ONE);

    final List <DATATYPE> aResult = new ArrayList <DATATYPE> ();
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
