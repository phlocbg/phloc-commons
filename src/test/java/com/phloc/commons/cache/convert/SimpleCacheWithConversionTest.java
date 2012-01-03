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
package com.phloc.commons.cache.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.convert.UnidirectionalConverterMapGet;

/**
 * Test class for class {@link SimpleCacheWithConversion}.
 * 
 * @author philip
 */
public final class SimpleCacheWithConversionTest
{
  @Test
  public void testAll ()
  {
    final Map <String, Integer> aMap = ContainerHelper.newMap ("In", Integer.valueOf (1));
    final SimpleCacheWithConversion <String, Integer> aCache = new SimpleCacheWithConversion <String, Integer> ("test");
    assertEquals ("test", aCache.getName ());
    // Get from map
    assertEquals (Integer.valueOf (1),
                  aCache.getFromCache ("In", new UnidirectionalConverterMapGet <String, Integer> (aMap)));
    // Use cached value
    assertEquals (Integer.valueOf (1),
                  aCache.getFromCache ("In", new UnidirectionalConverterMapGet <String, Integer> (aMap)));
    // Use cached value
    assertEquals (Integer.valueOf (1), aCache.getFromCache ("In"));
    // No such cached value
    assertNull (aCache.getFromCache ("Gibts Ned"));
    try
    {
      // Cannot convert the passed key!
      aCache.getFromCache ("Gibts Ned", new UnidirectionalConverterMapGet <String, Integer> (aMap));
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    // No such cached value
    assertNull (aCache.getFromCache ("Gibts Ned"));
  }
}
