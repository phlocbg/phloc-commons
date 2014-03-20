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
package com.phloc.commons.compare;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.phloc.commons.mock.PhlocAssert;

/**
 * Test class
 *
 * @author Philip Helger
 */
public final class AbstractNumericComparatorTest
{
  @Test
  public void testAll ()
  {
    final Double [] x = new Double [] { Double.valueOf (3),
                                       Double.valueOf (3),
                                       Double.valueOf (-56),
                                       Double.valueOf (1) };

    // default: sort ascending
    List <Double> l = Arrays.stream (x).sorted ().collect (Collectors.toList ());
    assertNotNull (l);
    PhlocAssert.assertEquals (-56, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (3).doubleValue ());

    // Explicitly sort descending
    l = Arrays.stream (x)
        .sorted (Comparator.comparing (p -> Double.valueOf (-p.doubleValue ())))
        .collect (Collectors.toList ());
    assertNotNull (l);
    PhlocAssert.assertEquals (3, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (-56, l.get (3).doubleValue ());
  }
}
