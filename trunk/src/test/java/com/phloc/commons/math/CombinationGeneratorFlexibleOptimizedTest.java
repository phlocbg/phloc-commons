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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.math.CombinationGeneratorOptimized.CombinationResultList;
import com.phloc.commons.timing.StopWatch;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Test class for class {@link CombinationGeneratorFlexibleOptimized}.
 * 
 * @author philip
 */
public final class CombinationGeneratorFlexibleOptimizedTest extends AbstractCombinationGeneratorTest
{
  @Test
  @SuppressWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  public void testStringCombination ()
  {
    final List <String> aElements = ContainerHelper.newList ("A", "B", "B");

    // Allow empty
    CombinationGeneratorFlexibleOptimized <String> aGenerator = new CombinationGeneratorFlexibleOptimized <String> (3,
                                                                                                                    true);

    Set <CombinationResultList <String>> aResults = aGenerator.getCombinations (aElements);
    assertEquals (6, aResults.size ());
    assertTrue (aResults.contains (CombinationResultList.getEmpty ()));

    aResults = aGenerator.getCombinations (new ArrayList <String> ());
    assertEquals (1, aResults.size ());
    assertTrue (aResults.contains (CombinationResultList.getEmpty ()));

    // Not allowing empty
    aGenerator = new CombinationGeneratorFlexibleOptimized <String> (3, false);

    aResults = aGenerator.getCombinations (aElements);
    assertEquals (5, aResults.size ());
    assertFalse (aResults.contains (CombinationResultList.getEmpty ()));

    aResults = aGenerator.getCombinations (new ArrayList <String> ());
    assertEquals (1, aResults.size ());
    assertTrue (aResults.contains (CombinationResultList.getEmpty ()));

    try
    {
      aGenerator.getCombinations (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  @SuppressWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  public void testCtor ()
  {
    try
    {
      new CombinationGeneratorFlexibleOptimized <String> (-1, true);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testHugeDataSet ()
  {
    final StopWatch aSW = new StopWatch (true);
    // Takes approx. 490ms on PH main machine (2012-01-21)
    // With one element more the time is at approx. 1500ms
    final Set <CombinationResultList <String>> aResult = CombinationGeneratorFlexibleOptimized.getCombinations (HUGE_LIST,
                                                                                                                true);
    s_aLogger.info ("Optimized: " + aSW.stopAndGetMillis () + " ms with " + aResult.size () + " elements");
  }
}
