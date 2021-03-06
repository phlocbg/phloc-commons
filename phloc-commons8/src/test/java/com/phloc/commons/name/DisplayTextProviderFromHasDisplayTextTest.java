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
package com.phloc.commons.name;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link DisplayTextProviderFromHasDisplayText}.
 * 
 * @author Philip Helger
 */
public final class DisplayTextProviderFromHasDisplayTextTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final DisplayTextProviderFromHasDisplayText aDNPFHDN = new DisplayTextProviderFromHasDisplayText ();
    assertEquals ("de1", aDNPFHDN.getDisplayText (MockHasDisplayText.createDE_EN ("de1", "en1"), L_DE));
    assertNull (aDNPFHDN.getDisplayText (null, L_DE));
  }
}
