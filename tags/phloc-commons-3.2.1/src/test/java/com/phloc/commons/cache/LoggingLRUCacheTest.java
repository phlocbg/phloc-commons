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
package com.phloc.commons.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link LoggingLRUCache}.
 * 
 * @author philip
 */
public final class LoggingLRUCacheTest
{
  @Test
  public void testAll ()
  {
    final LoggingLRUCache <String, String> c = new LoggingLRUCache <String, String> ("name", 5);
    assertEquals ("name", c.getCacheName ());
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (c, new LoggingLRUCache <String, String> ("name", 5));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (c,
                                                                        new LoggingLRUCache <String, String> ("name2",
                                                                                                              5));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (c,
                                                                        new LoggingLRUCache <String, String> ("name", 6));

    // Check overflow
    for (int i = 0; i < c.getMaxSize () + 1; ++i)
      c.put (Integer.toString (i), Integer.toString (i));
    assertEquals (c.getMaxSize (), c.size ());

    try
    {
      new LoggingLRUCache <String, String> ("", 5);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
