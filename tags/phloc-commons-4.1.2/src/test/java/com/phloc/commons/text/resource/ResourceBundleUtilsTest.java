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
package com.phloc.commons.text.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ResourceBundleUtils}.
 * 
 * @author Philip Helger
 */
public final class ResourceBundleUtilsTest extends AbstractPhlocTestCase
{
  @Test
  public void testGetString ()
  {
    assertEquals ("äöü", ResourceBundleUtils.getString ("properties/test-iso8859", L_DE, "key1"));
    assertNull (ResourceBundleUtils.getString ("properties/test-iso8859-noway", L_DE, "key1"));
    assertEquals ("äöü", ResourceBundleUtils.getString ("properties/test-iso8859", L_FR, "key1"));
    assertNull (ResourceBundleUtils.getString ("properties/test-iso8859", L_DE, "key-noway"));
  }

  @Test
  public void testGetUtf8String ()
  {
    assertEquals ("äöü", ResourceBundleUtils.getUtf8String ("properties/test-utf8", L_DE, "key1"));
    assertNull (ResourceBundleUtils.getUtf8String ("properties/test-utf8-noway", L_DE, "key1"));
    assertEquals ("äöü", ResourceBundleUtils.getUtf8String ("properties/test-utf8", L_FR, "key1"));
    assertNull (ResourceBundleUtils.getUtf8String ("properties/test-utf8", L_DE, "key-noway"));
  }
}
