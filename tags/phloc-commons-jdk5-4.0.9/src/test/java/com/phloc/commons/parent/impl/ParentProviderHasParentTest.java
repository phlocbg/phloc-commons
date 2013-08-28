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
package com.phloc.commons.parent.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.parent.MockHasParent;

/**
 * Test class for class {@link ParentProviderHasParent}.
 * 
 * @author Philip Helger
 */
public final class ParentProviderHasParentTest
{
  @Test
  public void testAll ()
  {
    final ParentProviderHasParent <MockHasParent> aPRHP = new ParentProviderHasParent <MockHasParent> ();
    assertNull (aPRHP.getParent (null));
    assertEquals (new MockHasParent ("ab").getID (), aPRHP.getParent (new MockHasParent ("abc")).getID ());
    assertEquals (new MockHasParent ("a").getID (), aPRHP.getParent (new MockHasParent ("ab")).getID ());
    assertNull (aPRHP.getParent (new MockHasParent ("a")));
    assertNull (aPRHP.getParent (new MockHasParent ("")));
  }
}
