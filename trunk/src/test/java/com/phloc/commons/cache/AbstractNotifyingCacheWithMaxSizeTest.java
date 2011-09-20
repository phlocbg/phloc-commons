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

import org.junit.Test;

/**
 * Test class for class {@link AbstractNotifyingCacheWithMaxSize}.
 * 
 * @author philip
 */
public final class AbstractNotifyingCacheWithMaxSizeTest
{
  @Test
  public void testAll ()
  {
    final MockNotifyingCacheWithMaxSize c = new MockNotifyingCacheWithMaxSize (5);
    assertEquals (5, c.getMaxSize ());

    for (int i = 0; i < c.getMaxSize () + 1; ++i)
      c.getFromCache ("key" + i);
    assertEquals (5, c.size ());
  }
}
