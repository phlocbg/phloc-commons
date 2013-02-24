/**
 * Copyright (C) 2006-2013 phloc systems
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
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link CombinationGenerator}.
 *
 * @author philip
 */
public final class CombinationGeneratorTest
{
  private static final String A = "A";
  private static final String B = "B";
  private static final String C = "C";

  @Test
  public void testStringCombination ()
  {
    final List <String> aElements = ContainerHelper.newList (A, B, B, C);
    final CombinationGenerator <String> x = new CombinationGenerator <String> (aElements, 3);
    assertEquals (BigInteger.valueOf (4), x.getTotalCombinations ());
    assertEquals (BigInteger.valueOf (4), x.getCombinationsLeft ());

    final List <List <String>> aResultsWithDuplicates = new ArrayList <List <String>> ();
    final Set <List <String>> aResultsWithoutDuplicates = new HashSet <List <String>> ();
    while (x.hasNext ())
    {
      final List <String> aResult = x.next ();
      aResultsWithDuplicates.add (aResult);
      aResultsWithoutDuplicates.add (aResult);
    }
    assertEquals (4, aResultsWithDuplicates.size ());
    assertEquals (3, aResultsWithoutDuplicates.size ());

    try
    {
      x.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}
  }

  @Test
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testCtor ()
  {
    try
    {
      new CombinationGenerator <String> (null, 3);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new CombinationGenerator <String> (ContainerHelper.<String> newList (), 3);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new CombinationGenerator <String> (ContainerHelper.newList ("a"), 3);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new CombinationGenerator <String> (ContainerHelper.newList ("a", "b"), -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
