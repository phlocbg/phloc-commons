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
package com.phloc.commons.thirdparty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.version.Version;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Test class for class {@link CustomLicense}.
 * 
 * @author philip
 */
public final class CustomLicenseTest
{
  @Test
  @SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testAll ()
  {
    final CustomLicense cl = new CustomLicense ("cl1", "License", new Version ("1.2"), "url");
    assertEquals ("cl1", cl.getID ());
    assertEquals ("License", cl.getDisplayName ());
    assertEquals ("1.2", cl.getVersion ().getAsString ());
    assertEquals ("url", cl.getURL ());

    final CustomLicense cl2 = new CustomLicense ("cl1", "License", null, null);
    assertEquals ("cl1", cl2.getID ());
    assertEquals ("License", cl2.getDisplayName ());
    assertNull (cl2.getVersion ());
    assertNull (cl2.getURL ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (cl2, new CustomLicense ("cl1",
                                                                                            "License",
                                                                                            null,
                                                                                            null));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (cl, new CustomLicense ("cl1",
                                                                                           "License",
                                                                                           new Version ("1.2"),
                                                                                           "url"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (cl, new CustomLicense ("cl12",
                                                                                               "License",
                                                                                               new Version ("1.2"),
                                                                                               "url"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (cl, new CustomLicense ("cl1",
                                                                                               "License2",
                                                                                               new Version ("1.2"),
                                                                                               "url"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (cl, new CustomLicense ("cl1",
                                                                                               "License",
                                                                                               new Version ("1.1"),
                                                                                               "url"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (cl, new CustomLicense ("cl1",
                                                                                               "License",
                                                                                               null,
                                                                                               "url"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (cl, new CustomLicense ("cl1",
                                                                                               "License",
                                                                                               new Version ("1.2"),
                                                                                               "url2"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (cl, new CustomLicense ("cl1",
                                                                                               "License",
                                                                                               new Version ("1.2"),
                                                                                               null));

    try
    {
      new CustomLicense (null, "name", null, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new CustomLicense ("id", null, null, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
