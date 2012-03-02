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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.math.CombinationGeneratorOptimized.CombinationResultList;

/**
 * Utility class for generating all possible combinations of elements for a
 * specified number of available slots. Duplicates in the passed elements will
 * be treated as different individuals and hence deliver duplicate result
 * solutions. This generator will only return complete result sets filling all
 * slots.
 * 
 * @author Boris Gregorcic, philip
 * @param <DATATYPE>
 *        element type
 */
public final class CombinationGeneratorOptimized <DATATYPE> implements
                                                            IIterableIterator <CombinationResultList <DATATYPE>>
{
  @MustImplementEqualsAndHashcode
  public static final class CombinationResultList <DATATYPE>
  {
    private static final CombinationResultList <Object> s_aEmpty = new CombinationResultList <Object> (0).finish ();

    private final Object [] m_aData;
    private int m_nIndex = 0;
    private Integer m_aHashCode;

    public CombinationResultList (@Nonnegative final int nLength)
    {
      m_aData = new Object [nLength];
    }

    public void add (@Nullable final Object aObject)
    {
      m_aData[m_nIndex++] = aObject;
    }

    @Nonnull
    public CombinationResultList <DATATYPE> finish ()
    {
      if (m_nIndex != m_aData.length)
        throw new IllegalStateException ("Not all elements where added!");

      return this;
    }

    @Override
    public boolean equals (final Object o)
    {
      if (o == this)
        return true;
      if (!(o instanceof CombinationResultList <?>))
        return false;
      // Try to be as performant as possible, even if this is potentially wrong!
      final CombinationResultList <?> rhs = (CombinationResultList <?>) o;
      return Arrays.equals (m_aData, rhs.m_aData);
    }

    @Override
    public int hashCode ()
    {
      if (m_aHashCode == null)
      {
        int nHC = HashCodeGenerator.INITIAL_HASHCODE * 31 + m_aData.length;
        for (int i = 0; i < m_aData.length; ++i)
        {
          final Object aObject = m_aData[i];
          nHC = nHC * 31 + (aObject == null ? 1231 : aObject.hashCode ());
        }
        m_aHashCode = Integer.valueOf (nHC);
      }
      return m_aHashCode.intValue ();
    }

    @Nonnull
    public static <T> CombinationResultList <T> getEmpty ()
    {
      return GenericReflection.<CombinationResultList <Object>, CombinationResultList <T>> uncheckedCast (s_aEmpty);
    }
  }

  private final Object [] m_aElements;
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
  public CombinationGeneratorOptimized (@Nonnull @Nonempty final List <DATATYPE> aElements,
                                        @Nonnegative final int nSlotCount)
  {
    if (ContainerHelper.isEmpty (aElements))
      throw new IllegalArgumentException ("No elements passed");
    if (nSlotCount < 0)
      throw new IllegalArgumentException ("To small slot count: " + nSlotCount);
    if (nSlotCount > aElements.size ())
      throw new IllegalArgumentException ("To few elements for specified slots: " + aElements.size ());

    m_aElements = aElements.toArray ();
    m_aIndexResult = new int [nSlotCount];
    final BigInteger aElementFactorial = FactorialHelper.getAnyFactorialLinear (m_aElements.length);
    final BigInteger aSlotFactorial = FactorialHelper.getAnyFactorialLinear (nSlotCount);
    final BigInteger aOverflowFactorial = FactorialHelper.getAnyFactorialLinear (m_aElements.length - nSlotCount);
    m_aTotalCombinations = aElementFactorial.divide (aSlotFactorial.multiply (aOverflowFactorial));
    // Can we use the fallback to long? Is much faster than using BigInteger
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
  public CombinationResultList <DATATYPE> next ()
  {
    // Not for the very first item, as the first item is the original order
    final boolean bFirstItem = m_bUseLong ? m_nCombinationsLeft == m_nTotalCombinations
                                         : m_aCombinationsLeft.equals (m_aTotalCombinations);
    if (!bFirstItem)
    {
      final int nElementCount = m_aElements.length;
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
    final CombinationResultList <DATATYPE> aResult = new CombinationResultList <DATATYPE> (m_aIndexResult.length);
    for (final int nIndexResult : m_aIndexResult)
      aResult.add (m_aElements[nIndexResult]);
    return aResult.finish ();
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public Iterator <CombinationResultList <DATATYPE>> iterator ()
  {
    return this;
  }

  /**
   * Get a list of all permutations of the input elements.
   * 
   * @param aInput
   *        Input list.
   * @param nSlotCount
   *        Slot count.
   * @return The list of all permutations. Beware: the resulting list may be
   *         quite large and may contain duplicates if the input list contains
   *         duplicate elements!
   */
  @Nonnull
  public static <DATATYPE> List <CombinationResultList <DATATYPE>> getAllPermutations (@Nonnull @Nonempty final List <DATATYPE> aInput,
                                                                                       @Nonnegative final int nSlotCount)
  {
    final List <CombinationResultList <DATATYPE>> aResultList = new ArrayList <CombinationResultList <DATATYPE>> ();
    addAllPermutations (aInput, nSlotCount, aResultList);
    return aResultList;
  }

  /**
   * Fill a list with all permutations of the input elements.
   * 
   * @param aInput
   *        Input list.
   * @param nSlotCount
   *        Slot count.
   * @param aResultList
   *        The list to be filled with all permutations. Beware: this list may
   *        be quite large and may contain duplicates if the input list contains
   *        duplicate elements! Note: this list is not cleared before filling
   */
  @Nonnull
  public static <DATATYPE> void addAllPermutations (@Nonnull @Nonempty final List <DATATYPE> aInput,
                                                    @Nonnegative final int nSlotCount,
                                                    @Nonnull final Collection <CombinationResultList <DATATYPE>> aResultList)
  {
    for (final CombinationResultList <DATATYPE> aPermutation : new CombinationGeneratorOptimized <DATATYPE> (aInput,
                                                                                                             nSlotCount))
      aResultList.add (aPermutation);
  }
}
