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
package com.phloc.commons.text.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ResourceBundleTextProvider}.
 * 
 * @author philip
 */
public final class ResourceBundleTextProviderTest extends AbstractPhlocTestCase
{
  @Test
  public void testISO8859 ()
  {
    final ResourceBundleKey key = new ResourceBundleKey ("properties/test-iso8859", "key1");
    assertEquals ("properties/test-iso8859", key.getBundleName ());
    assertEquals ("key1", key.getKey ());
    assertEquals ("äöü", key.getString (L_DE));

    final ResourceBundleTextProvider aRBT = new ResourceBundleTextProvider (key);
    assertEquals ("äöü", aRBT.getText (L_DE));
    assertEquals ("äöü", aRBT.getTextWithLocaleFallback (L_DE));
    assertEquals ("äöü", aRBT.getTextWithArgs (L_DE, "egal"));
    assertEquals ("äöü", aRBT.getTextWithLocaleFallbackAndArgs (L_DE, "egal"));

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ResourceBundleTextProvider (key),
                                                                    new ResourceBundleTextProvider (key));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ResourceBundleTextProvider (key),
                                                                    new ResourceBundleTextProvider ("properties/test-iso8859",
                                                                                                    "key1"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ResourceBundleTextProvider (key),
                                                                        new ResourceBundleTextProvider ("properties/test-iso8859",
                                                                                                        "key2"));

    try
    {
      new ResourceBundleTextProvider (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
