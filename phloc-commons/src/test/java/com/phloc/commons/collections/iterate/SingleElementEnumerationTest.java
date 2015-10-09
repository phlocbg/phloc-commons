/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.collections.iterate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link SingleElementEnumeration}
 * 
 * @author Philip Helger
 */
public final class SingleElementEnumerationTest
{
  @Test
  public void testAll ()
  {
    final SingleElementEnumeration <String> eit = new SingleElementEnumeration <String> ("any");
    assertTrue (eit.hasMoreElements ());

    assertEquals ("any", eit.nextElement ());
    try
    {
      eit.nextElement ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}
    assertFalse (eit.hasMoreElements ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new SingleElementEnumeration <String> ("any"),
                                                                    new SingleElementEnumeration <String> ("any"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new SingleElementEnumeration <String> ("any"),
                                                                        new SingleElementEnumeration <String> ("any2"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new SingleElementEnumeration <String> ("any"),
                                                                        new SingleElementEnumeration <Integer> (Integer.valueOf (1)));
  }
}
