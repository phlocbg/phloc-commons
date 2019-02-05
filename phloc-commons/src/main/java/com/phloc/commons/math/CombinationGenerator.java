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
package com.phloc.commons.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.lang.GenericReflection;

/**
 * Utility class for generating all possible combinations of elements for a
 * specified number of available slots. Duplicates in the passed elements will
 * be treated as different individuals and hence deliver duplicate result
 * solutions. This generator will only return complete result sets filling all
 * slots.
 * 
 * @author Boris Gregorcic
 * @author Philip Helger
 * @param <DATATYPE>
 *        element type
 */
public final class CombinationGenerator <DATATYPE> implements IIterableIterator <List <DATATYPE>>
{
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
  public CombinationGenerator (@Nonnull @Nonempty final List <DATATYPE> aElements, @Nonnegative final int nSlotCount)
  {
    ValueEnforcer.notEmpty (aElements, "Elements");
    ValueEnforcer.isBetweenInclusive (nSlotCount, "SlotCount", 0, aElements.size ());

    this.m_aElements = aElements.toArray ();
    this.m_aIndexResult = new int [nSlotCount];
    final BigInteger aElementFactorial = FactorialHelper.getAnyFactorialLinear (this.m_aElements.length);
    final BigInteger aSlotFactorial = FactorialHelper.getAnyFactorialLinear (nSlotCount);
    final BigInteger aOverflowFactorial = FactorialHelper.getAnyFactorialLinear (this.m_aElements.length - nSlotCount);
    this.m_aTotalCombinations = aElementFactorial.divide (aSlotFactorial.multiply (aOverflowFactorial));
    // Can we use the fallback to long? Is much faster than using BigInteger
    this.m_bUseLong = this.m_aTotalCombinations.compareTo (CGlobal.BIGINT_MAX_LONG) < 0;
    this.m_nTotalCombinations = this.m_bUseLong ? this.m_aTotalCombinations.longValue () : CGlobal.ILLEGAL_ULONG;
    reset ();
  }

  /**
   * Reset the generator
   */
  public void reset ()
  {
    for (int i = 0; i < this.m_aIndexResult.length; i++)
      this.m_aIndexResult[i] = i;
    this.m_aCombinationsLeft = this.m_aTotalCombinations;
    this.m_nCombinationsLeft = this.m_nTotalCombinations;
  }

  /**
   * @return number of combinations not yet generated
   */
  @Nonnull
  public BigInteger getCombinationsLeft ()
  {
    return this.m_bUseLong ? BigInteger.valueOf (this.m_nCombinationsLeft) : this.m_aCombinationsLeft;
  }

  /**
   * @return whether or not there are more combinations left
   */
  @Override
  public boolean hasNext ()
  {
    return this.m_bUseLong ? this.m_nCombinationsLeft > 0 : this.m_aCombinationsLeft.compareTo (BigInteger.ZERO) > 0;
  }

  /**
   * @return total number of combinations
   */
  @Nonnull
  public BigInteger getTotalCombinations ()
  {
    return this.m_aTotalCombinations;
  }

  /**
   * Generate next combination (algorithm from Rosen p. 286)
   * 
   * @return the next combination as List of the size specified for slots filled
   *         with elements from the original list
   */
  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <DATATYPE> next ()
  {
    // Not for the very first item, as the first item is the original order
    final boolean bFirstItem = this.m_bUseLong ? this.m_nCombinationsLeft == this.m_nTotalCombinations
                                               : this.m_aCombinationsLeft.equals (this.m_aTotalCombinations);
    if (!bFirstItem)
    {
      final int nElementCount = this.m_aElements.length;
      final int nSlotCount = this.m_aIndexResult.length;

      int i = nSlotCount - 1;
      while (this.m_aIndexResult[i] == (nElementCount - nSlotCount + i))
      {
        i--;
      }
      this.m_aIndexResult[i]++;
      for (int j = i + 1; j < nSlotCount; j++)
      {
        this.m_aIndexResult[j] = this.m_aIndexResult[i] + j - i;
      }
    }

    // One combination less
    if (this.m_bUseLong)
      this.m_nCombinationsLeft--;
    else
      this.m_aCombinationsLeft = this.m_aCombinationsLeft.subtract (BigInteger.ONE);

    // Build result list
    final List <DATATYPE> aResult = new ArrayList <DATATYPE> (this.m_aIndexResult.length);
    for (final int nIndex : this.m_aIndexResult)
      aResult.add (GenericReflection.<Object, DATATYPE> uncheckedCast (this.m_aElements[nIndex]));
    return aResult;
  }

  @Override
  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  @Nonnull
  public Iterator <List <DATATYPE>> iterator ()
  {
    return this;
  }

  /**
   * Get a list of all permutations of the input elements.
   * 
   * @param <DATATYPE>
   *        data type
   * @param aInput
   *        Input list.
   * @param nSlotCount
   *        Slot count.
   * @return The list of all permutations. Beware: the resulting list may be
   *         quite large and may contain duplicates if the input list contains
   *         duplicate elements!
   */
  @Nonnull
  public static <DATATYPE> List <List <DATATYPE>> getAllPermutations (@Nonnull @Nonempty final List <DATATYPE> aInput,
                                                                      @Nonnegative final int nSlotCount)
  {
    final List <List <DATATYPE>> aResultList = new ArrayList <List <DATATYPE>> ();
    addAllPermutations (aInput, nSlotCount, aResultList);
    return aResultList;
  }

  /**
   * Fill a list with all permutations of the input elements.
   * 
   * @param <DATATYPE>
   *        data type
   * @param aInput
   *        Input list.
   * @param nSlotCount
   *        Slot count.
   * @param aResultList
   *        The list to be filled with all permutations. Beware: this list may
   *        be quite large and may contain duplicates if the input list contains
   *        duplicate elements! Note: this list is not cleared before filling
   */
  public static <DATATYPE> void addAllPermutations (@Nonnull @Nonempty final List <DATATYPE> aInput,
                                                    @Nonnegative final int nSlotCount,
                                                    @Nonnull final Collection <List <DATATYPE>> aResultList)
  {
    for (final List <DATATYPE> aPermutation : new CombinationGenerator <DATATYPE> (aInput, nSlotCount))
      aResultList.add (aPermutation);
  }
}
