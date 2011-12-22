/**
 * Copyright (C) 2006-2011 phloc systems
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ReadonlyPair}.
 * 
 * @author philip
 */
public final class ReadonlyPairTest
{
  @Test
  public void testCtor ()
  {
    ReadonlyPair <String, Double> aPair = new ReadonlyPair <String, Double> (null, null);
    assertNull (aPair.getFirst ());
    assertNull (aPair.getSecond ());

    IReadonlyPair <String, Double> aPair2 = new ReadonlyPair <String, Double> (aPair);
    assertNull (aPair2.getFirst ());
    assertNull (aPair2.getSecond ());

    aPair = new ReadonlyPair <String, Double> ("x", Double.valueOf (1.2));
    assertEquals ("x", aPair.getFirst ());
    assertEquals (Double.valueOf (1.2), aPair.getSecond ());

    aPair2 = new ReadonlyPair <String, Double> (aPair);
    assertEquals ("x", aPair2.getFirst ());
    assertEquals (Double.valueOf (1.2), aPair2.getSecond ());
  }

  @Test
  public void testEquals ()
  {
    ReadonlyPair <String, Double> aPair = new ReadonlyPair <String, Double> (null, null);

    ReadonlyPair <String, Double> aPair2 = new ReadonlyPair <String, Double> ("Hallo", null);
    assertFalse (aPair.equals (aPair2));

    aPair2 = new ReadonlyPair <String, Double> (null, Double.valueOf (3.14));
    assertFalse (aPair.equals (aPair2));

    aPair2 = new ReadonlyPair <String, Double> ("Nice text", Double.valueOf (3.14));
    assertFalse (aPair.equals (aPair2));

    // change pair1
    aPair = new ReadonlyPair <String, Double> ("Nix da", null);

    aPair2 = new ReadonlyPair <String, Double> (null, null);
    assertFalse (aPair.equals (aPair2));

    aPair2 = new ReadonlyPair <String, Double> ("Hallo", null);
    assertFalse (aPair.equals (aPair2));

    aPair2 = new ReadonlyPair <String, Double> (null, Double.valueOf (3.14));
    assertFalse (aPair.equals (aPair2));

    aPair2 = new ReadonlyPair <String, Double> ("Nice text", Double.valueOf (3.14));
    assertFalse (aPair.equals (aPair2));

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ReadonlyPair <String, String> (null, null),
                                                                    new ReadonlyPair <String, Double> (null, null));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ReadonlyPair <String, String> ("Nix da", null),
                                                                    new ReadonlyPair <String, Double> ("Nix da", null));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ReadonlyPair <String, String> (null, "Nix da"),
                                                                    new ReadonlyPair <String, String> (null, "Nix da"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ReadonlyPair <String, String> ("Nix", "da"),
                                                                    new ReadonlyPair <String, String> ("Nix", "da"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ReadonlyPair <String, String> ("Nix", "da"),
                                                                        new ReadonlyPair <String, String> ("Nix", "da2"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (ReadonlyPair.create ("Nix", "da"),
                                                                        ReadonlyPair.create ("Nix2", "da"));
  }
}
