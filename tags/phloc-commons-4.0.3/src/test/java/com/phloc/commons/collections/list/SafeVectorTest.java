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
package com.phloc.commons.collections.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.factory.FactoryNewInstance;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link SafeVector}.
 * 
 * @author philip
 */
public final class SafeVectorTest
{
  @Test
  public void testSafeVectorNullFactory ()
  {
    // create with a "null"-creating factory
    final SafeVector <String> sv = new SafeVector <String> ();
    assertEquals (sv.size (), 0);

    // no such index 0 -> create using the factory
    assertNull (sv.get (0));
    assertEquals (sv.size (), 1);

    // no such index 10 -> create using the factory
    assertNull (sv.get (10));
    assertEquals (sv.size (), 11);

    // check if all other elements are also null
    for (final String sMember : sv)
      assertNull (sMember);
  }

  @Test
  public void testSafeVectorNewInstanceFactory ()
  {
    try
    {
      new SafeVector <String> (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    // create with a "null"-creating factory
    final SafeVector <String> sv = new SafeVector <String> (FactoryNewInstance.create (String.class));
    assertEquals (sv.size (), 0);

    // no such index 0 -> create using the factory
    assertNotNull (sv.get (0));
    assertEquals (sv.size (), 1);

    // no such index 10 -> create using the factory
    assertNotNull (sv.get (10));
    assertEquals (sv.size (), 11);

    // check if all other elements are also not null
    for (final String sMember : sv)
      assertNotNull (sMember);
  }

  @Test
  public void testEqualsAndHashcode ()
  {
    final SafeVector <String> sl = new SafeVector <String> ();
    sl.set (10, "any");
    assertNull (sl.get (9));

    final SafeVector <String> sl2 = new SafeVector <String> ();
    sl2.set (10, "any");

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (sl, sl2);
    sl2.set (9, "x");
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (sl, sl2);
  }
}
