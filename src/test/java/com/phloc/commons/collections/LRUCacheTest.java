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
package com.phloc.commons.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test for class {@link LRUCache}
 * 
 * @author Philip Helger
 */
public final class LRUCacheTest
{
  private static final int MAX_SIZE = 5;

  @Test
  public void testLRUCache ()
  {
    final LRUCache <Integer, String> aCache = new LRUCache <Integer, String> (MAX_SIZE);
    for (int i = 0; i < MAX_SIZE * 2; ++i)
    {
      if (i < MAX_SIZE)
        assertEquals (aCache.size (), i);
      else
        assertEquals (aCache.size (), MAX_SIZE);
      assertNull (aCache.put (Integer.valueOf (i), "Hallo Welt"));
    }
  }
}
