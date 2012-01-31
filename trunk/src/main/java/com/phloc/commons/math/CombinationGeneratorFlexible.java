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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Utility class for generating all possible combinations of elements for a
 * specified number of available slots. Duplicates in the passed elements will
 * not deliver duplicate result solutions. This implementation is flexible in
 * terms of handling the slots. This means it will also return result sets where
 * not all slots are filled.
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        Element type
 */
@Immutable
public final class CombinationGeneratorFlexible <DATATYPE>
{
  private final boolean m_bAllowEmpty;
  private final int m_nSlotCount;

  /**
   * Ctor
   * 
   * @param nSlotCount
   *        the number of slots to use
   * @param bAllowEmpty
   *        whether or not to include an empty result set (no slot filled)
   */
  public CombinationGeneratorFlexible (@Nonnegative final int nSlotCount, final boolean bAllowEmpty)
  {
    if (nSlotCount < 0)
      throw new IllegalArgumentException ("Passed slot count is too small: " + nSlotCount);
    m_nSlotCount = nSlotCount;
    m_bAllowEmpty = bAllowEmpty;
  }

  /**
   * Generates all combinations without duplicates.
   * 
   * @param aElements
   *        the elements to distribute to the specified slots (may be empty!)
   * @return a set of slot allocations representing all possible combinations
   */
  @Nonnull
  @ReturnsMutableCopy
  public Set <List <DATATYPE>> getCombinations (@Nonnull final List <DATATYPE> aElements)
  {
    if (aElements == null)
      throw new NullPointerException ("elements");

    final Set <List <DATATYPE>> aAllResults = new HashSet <List <DATATYPE>> ();
    for (int nSlotCount = m_bAllowEmpty ? 0 : 1; nSlotCount <= m_nSlotCount; nSlotCount++)
    {
      if (aElements.isEmpty ())
      {
        aAllResults.add (new ArrayList <DATATYPE> ());
      }
      else
      {
        // Add all permutations for the current slot count
        CombinationGenerator.addAllPermutations (aElements, nSlotCount, aAllResults);
      }
    }
    return aAllResults;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <DATATYPE> Set <List <DATATYPE>> getCombinations (@Nonnull final List <DATATYPE> aElements,
                                                                  final boolean bAllowEmpty)
  {
    return new CombinationGeneratorFlexible <DATATYPE> (aElements.size (), bAllowEmpty).getCombinations (aElements);
  }
}
