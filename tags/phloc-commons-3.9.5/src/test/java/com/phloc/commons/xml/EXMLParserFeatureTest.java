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
package com.phloc.commons.xml;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link EXMLParserFeature}.
 * 
 * @author philip
 */
public final class EXMLParserFeatureTest
{
  @Test
  public void testAll ()
  {
    for (final EXMLParserFeature e : EXMLParserFeature.values ())
    {
      assertTrue (StringHelper.hasText (e.getName ()));
      assertSame (e, EXMLParserFeature.valueOf (e.name ()));
    }
  }
}
