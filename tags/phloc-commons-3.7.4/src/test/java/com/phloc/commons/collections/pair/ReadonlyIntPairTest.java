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
package com.phloc.commons.collections.pair;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ReadonlyIntPair}.
 * 
 * @author philip
 */
public final class ReadonlyIntPairTest
{
  @Test
  public void testCtor ()
  {
    ReadonlyIntPair aPair = new ReadonlyIntPair (0, 0);
    assertEquals (0, aPair.getFirst ());
    assertEquals (0, aPair.getSecond ());

    ReadonlyIntPair aPair2 = new ReadonlyIntPair (aPair);
    assertEquals (0, aPair2.getFirst ());
    assertEquals (0, aPair2.getSecond ());

    aPair = new ReadonlyIntPair (5, 2);
    assertEquals (5, aPair.getFirst ());
    assertEquals (2, aPair.getSecond ());

    aPair2 = new ReadonlyIntPair (aPair);
    assertEquals (5, aPair2.getFirst ());
    assertEquals (2, aPair2.getSecond ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ReadonlyIntPair (5, -30),
                                                                    new ReadonlyIntPair (5, -30));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ReadonlyIntPair (5, -30),
                                                                        new ReadonlyIntPair (-5, -30));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ReadonlyIntPair (5, -30),
                                                                        new ReadonlyIntPair (5, 30));
  }
}
