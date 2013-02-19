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
package com.phloc.commons.format.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link StringSkipPrefixAndSuffixFormatter}.
 * 
 * @author philip
 */
public final class StringSkipPrefixAndSuffixFormatterTest
{
  @Test
  public void testAll ()
  {
    final StringSkipPrefixAndSuffixFormatter fp = new StringSkipPrefixAndSuffixFormatter ("a", "o");
    assertEquals ("bc", fp.getFormattedValue ("abco"));
    assertEquals ("bc", fp.getFormattedValue ("abc"));
    assertEquals ("bc", fp.getFormattedValue ("bco"));
    assertEquals ("bc", fp.getFormattedValue ("bc"));
    PhlocTestUtils.testToStringImplementation (fp);
  }
}
