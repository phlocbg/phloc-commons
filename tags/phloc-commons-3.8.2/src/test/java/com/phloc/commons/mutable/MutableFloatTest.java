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
package com.phloc.commons.mutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MutableFloat}.
 * 
 * @author philip
 */
public final class MutableFloatTest
{
  private static final double DELTA = 0.00001;

  @Test
  public void testMutableFloat ()
  {
    final MutableFloat x = new MutableFloat ();
    assertEquals (x.floatValue (), 0, DELTA);
    assertEquals (x.getAsFloat (), Float.valueOf (0));
    assertFalse (x.isNot0 ());
    assertTrue (x.is0 ());

    x.inc ();
    assertEquals (x.floatValue (), 1, DELTA);
    assertFalse (x.hashCode () == x.floatValue ());

    x.inc (5);
    assertEquals (x.floatValue (), 6, DELTA);
    assertFalse (x.hashCode () == x.floatValue ());

    x.inc (-2);
    assertEquals (x.floatValue (), 4, DELTA);
    assertFalse (x.hashCode () == x.floatValue ());

    x.dec ();
    assertEquals (x.floatValue (), 3, DELTA);
    assertFalse (x.hashCode () == x.floatValue ());

    x.dec (5);
    assertEquals (x.floatValue (), -2, DELTA);
    assertTrue (x.isNot0 ());
    assertFalse (x.is0 ());
    assertFalse (x.hashCode () == x.floatValue ());

    assertTrue (x.set (4711).isChanged ());
    assertFalse (x.set (4711).isChanged ());
    assertEquals (x.floatValue (), 4711, DELTA);

    assertEquals (-1, new MutableFloat (4).compareTo (new MutableFloat (5)));
    assertEquals (0, new MutableFloat (5).compareTo (new MutableFloat (5)));
    assertEquals (+1, new MutableFloat (6).compareTo (new MutableFloat (5)));

    assertNotNull (x.toString ());
    assertTrue (x.toString ().contains (Float.toString (x.floatValue ())));

    x.set (-1);
    assertFalse (x.isGreater0 ());
    x.set (0);
    assertFalse (x.isGreater0 ());
    x.set (1);
    assertTrue (x.isGreater0 ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new MutableFloat (3.1415f),
                                                                    new MutableFloat (3.1415f));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new MutableFloat (3.1415f),
                                                                        new MutableFloat (3.141f));
    PhlocTestUtils.testGetClone (new MutableFloat (47.11f));
  }
}
