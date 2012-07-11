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
package com.phloc.commons.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link ObjectType}.
 *
 * @author philip
 */
public final class ObjectTypeTest
{
  @Test
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testAll () throws Exception
  {
    final ObjectType x = new ObjectType ("any");
    assertEquals ("any", x.getObjectTypeName ());
    assertEquals (0, x.compareTo (x));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (x, new ObjectType ("any"));

    final ObjectType y = new ObjectType ("any2");
    assertEquals (-1, x.compareTo (y));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (x, y);

    // Serialization
    PhlocTestUtils.testDefaultSerialization (x);

    try
    {
      // null not allowed
      new ObjectType (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // empty not allowed
      new ObjectType ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
