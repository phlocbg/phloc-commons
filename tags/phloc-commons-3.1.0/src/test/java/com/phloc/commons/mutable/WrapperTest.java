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
package com.phloc.commons.mutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link Wrapper}.
 * 
 * @author philip
 */
public final class WrapperTest
{
  @Test
  public void testWrapper ()
  {
    // Default ctor
    final Wrapper <String> w = new Wrapper <String> ();
    assertNull (w.get ());
    w.set ("Hi");
    assertNotNull (w.get ());
    assertEquals ("Hi", w.get ());
    assertNotNull (w.toString ());

    // ctor with value
    final Wrapper <String> w2 = new Wrapper <String> ("Ha");
    assertEquals ("Ha", w2.get ());
    assertNotNull (w2.toString ());

    // copy ctor
    final Wrapper <String> w3 = new Wrapper <String> (w);
    assertEquals (w.get (), w3.get ());
    assertEquals ("Hi", w3.get ());
    assertTrue (w.set ("Ho").isChanged ());
    assertEquals ("Hi", w3.get ());
    assertNotNull (w3.toString ());
    assertTrue (w.set ("Ho").isUnchanged ());
    assertEquals ("Hi", w3.get ());
    assertNotNull (w3.toString ());

    // illegal ctor
    try
    {
      new Wrapper <String> ((IReadonlyWrapper <String>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (Wrapper.create ("any"), Wrapper.create ("any"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (Wrapper.create ("any"),
                                                                        Wrapper.create ("other"));
  }
}
