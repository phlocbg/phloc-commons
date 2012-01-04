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

/**
 * Test class for class {@link CombinationGeneratorFlexible}.
 * 
 * @author philip
 */
public final class CombinationGeneratorFlexibleTest
{
  @Test
  public void testStringCombination ()
  {
    final List <String> aElements = ContainerHelper.newList ("A", "B", "B");

    // Allow empty
    CombinationGeneratorFlexible <String> aGenerator = new CombinationGeneratorFlexible <String> (3, true);

    Set <List <String>> aResults = aGenerator.getCombinations (aElements);
    assertEquals (6, aResults.size ());
    assertTrue (aResults.contains (new ArrayList <String> ()));

    aResults = aGenerator.getCombinations (new ArrayList <String> ());
    assertEquals (1, aResults.size ());
    assertTrue (aResults.contains (new ArrayList <String> ()));

    // Not allowing empty
    aGenerator = new CombinationGeneratorFlexible <String> (3, false);

    aResults = aGenerator.getCombinations (aElements);
    assertEquals (5, aResults.size ());
    assertFalse (aResults.contains (new ArrayList <String> ()));

    aResults = aGenerator.getCombinations (new ArrayList <String> ());
    assertEquals (1, aResults.size ());
    assertTrue (aResults.contains (new ArrayList <String> ()));

    try
    {
      aGenerator.getCombinations (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testCtor ()
  {
    try
    {
      new CombinationGeneratorFlexible <String> (-1, true);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
