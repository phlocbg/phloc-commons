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
package com.phloc.types.beans;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.types.beans.PropertyDescriptorCache.PDState;

/**
 * Test class for class {@link PropertyDescriptorCache}
 * 
 * @author Philip Helger
 */
public final class PropertyDescriptorCacheTest
{
  @Test
  public void testAll ()
  {
    assertNotNull (PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int"));
    assertNotNull (PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int"));
    assertNotNull (PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int", "getInt", "setInt"));
    assertNotNull (PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int", "getInt", "setInt"));
    assertNotNull (PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int", "getInt"));
    assertNotNull (PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int", "getInt"));
  }

  @Test
  public void testPDState ()
  {
    final PDState s = new PDState (MockBean.class, "int", null, null);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (s, new PDState (MockBean.class, "int", null, null));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (s, new PDState (MockBean.class,
                                                                                        "int2",
                                                                                        null,
                                                                                        null));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (s, new PDState (MockBean.class,
                                                                                        "int",
                                                                                        "getInt",
                                                                                        null));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (s, new PDState (MockBean.class,
                                                                                        "int",
                                                                                        null,
                                                                                        "setInt"));
  }

  @Test
  public void testInvalid ()
  {
    try
    {
      // no class
      PropertyDescriptorCache.getPropertyDescriptor (null, "int");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      // empty field
      PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // no such field
      PropertyDescriptorCache.getPropertyDescriptor (MockBean.class, "int2");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
