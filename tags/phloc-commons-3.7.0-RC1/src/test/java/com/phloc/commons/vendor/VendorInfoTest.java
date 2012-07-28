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
package com.phloc.commons.vendor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

/**
 * Test class for class {@link VendorInfo}.
 * 
 * @author philip
 */
public final class VendorInfoTest
{
  @Test
  public void testHeaderLines ()
  {
    final List <String> aList = VendorInfo.getFileHeaderLines ();
    assertNotNull (aList);
    assertFalse (aList.isEmpty ());
  }

  @Test
  public void testInceptionYear ()
  {
    assertEquals (VendorInfo.DEFAULT_INCEPTION_YEAR, VendorInfo.getInceptionYear ());
    VendorInfo.setInceptionYear (1999);
    assertEquals (1999, VendorInfo.getInceptionYear ());
    VendorInfo.setInceptionYear (VendorInfo.DEFAULT_INCEPTION_YEAR);
  }
}
