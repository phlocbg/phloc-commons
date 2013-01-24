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
package com.phloc.commons.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link EJVMVendor}.
 * 
 * @author philip
 */
public final class EJVMVendorTest
{
  @Test
  public void testSun ()
  {
    for (final EJVMVendor e : EJVMVendor.values ())
      assertSame (e, EJVMVendor.valueOf (e.name ()));

    final EJVMVendor eVendor = EJVMVendor.getCurrentVendor ();
    assertTrue (eVendor.isSun ());
    assertTrue (EJVMVendor.SUN_CLIENT.isSun ());
    assertTrue (EJVMVendor.SUN_SERVER.isSun ());
    assertFalse (EJVMVendor.UNKNOWN.isSun ());
  }
}
