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
package com.phloc.commons.thirdparty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for class {@link ThirdPartyModuleRegistry}.
 * 
 * @author Philip Helger
 */
public final class ThirdPartyModuleRegistryTest
{
  @Test
  public void testAll ()
  {
    assertNotNull (ThirdPartyModuleRegistry.getAllRegisteredThirdPartyModules ());
    assertEquals (3, ThirdPartyModuleRegistry.getAllRegisteredThirdPartyModules ().size ());

    try
    {
      ThirdPartyModuleRegistry.registerThirdPartyModule (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
