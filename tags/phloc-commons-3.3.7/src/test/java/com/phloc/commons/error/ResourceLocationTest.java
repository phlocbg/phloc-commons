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
package com.phloc.commons.error;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ResourceLocation}.
 * 
 * @author philip
 */
public final class ResourceLocationTest
{
  @Test
  public void testBasic ()
  {
    ResourceLocation re = new ResourceLocation ("xx", "field");
    assertEquals ("xx", re.getResourceID ());
    assertEquals (IResourceLocation.ILLEGAL_NUMBER, re.getLineNumber ());
    assertEquals (IResourceLocation.ILLEGAL_NUMBER, re.getColumnNumber ());
    assertEquals ("field", re.getField ());
    assertEquals ("xx @ field", re.getAsString ());

    re = new ResourceLocation ("xx", 5, IResourceLocation.ILLEGAL_NUMBER, "field");
    assertEquals ("xx", re.getResourceID ());
    assertEquals (5, re.getLineNumber ());
    assertEquals (IResourceLocation.ILLEGAL_NUMBER, re.getColumnNumber ());
    assertEquals ("field", re.getField ());
    assertEquals ("xx(5:?) @ field", re.getAsString ());

    re = new ResourceLocation ("xx", 5, 7, "field");
    assertEquals ("xx", re.getResourceID ());
    assertEquals (5, re.getLineNumber ());
    assertEquals (7, re.getColumnNumber ());
    assertEquals ("field", re.getField ());
    assertEquals ("xx(5:7) @ field", re.getAsString ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ResourceLocation ("xx", "field"),
                                                                    new ResourceLocation ("xx", "field"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ResourceLocation ("xx", "field"),
                                                                        new ResourceLocation ("xx2", "field"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ResourceLocation ("xx", "field"),
                                                                        new ResourceLocation ("xx", "field2"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ResourceLocation ("xx", 0, 1, "field"),
                                                                        new ResourceLocation ("xx", 0, 0, "field"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ResourceLocation ("xx", 0, 1, "field"),
                                                                        new ResourceLocation ("xx", 1, 1, "field"));
  }
}
