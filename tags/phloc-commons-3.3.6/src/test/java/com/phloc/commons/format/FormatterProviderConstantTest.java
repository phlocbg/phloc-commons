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
package com.phloc.commons.format;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.format.impl.BracketFormatter;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link FormatterProviderConstant}.
 * 
 * @author philip
 */
public final class FormatterProviderConstantTest
{
  @Test
  public void testAll ()
  {
    final FormatterProviderConstant fp = new FormatterProviderConstant (new BracketFormatter ());
    final IFormatter f1 = fp.getFormatter ();
    assertNotNull (f1);
    assertTrue (f1 instanceof BracketFormatter);
    final IFormatter f2 = fp.getFormatter ();
    assertSame (f1, f2);
    PhlocTestUtils.testToStringImplementation (fp);
  }
}
