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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link SystemHelper}.
 * 
 * @author philip
 */
public final class SystemHelperTest
{
  @Test
  public void testAll ()
  {
    assertTrue (SystemHelper.getNumberOfProcessors () >= 1);
    assertNotNull (SystemHelper.getOperatingSystem ());
    assertNotNull (SystemHelper.getOperatingSystemName ());
    assertNotNull (SystemHelper.getSystemLocale ());
    assertNotNull (SystemHelper.getSystemCharset ());
    assertNotNull (SystemHelper.getSystemCharsetName ());
    assertNotNull (SystemHelper.getJavaVersion ());
    assertNotNull (SystemHelper.getJVMVendor ());
    assertNotNull (SystemHelper.getProcessorArchitecture ());
    assertTrue (SystemHelper.getFreeMemory () > 0);
    assertTrue (SystemHelper.getMaxMemory () > 0);
    assertTrue (SystemHelper.getTotalMemory () > 0);
  }
}