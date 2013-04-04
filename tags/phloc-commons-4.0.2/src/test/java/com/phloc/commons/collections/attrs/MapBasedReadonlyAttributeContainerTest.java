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
package com.phloc.commons.collections.attrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MapBasedReadonlyAttributeContainer}.
 * 
 * @author philip
 */
public final class MapBasedReadonlyAttributeContainerTest
{
  @Test
  public void testInit ()
  {
    MapBasedReadonlyAttributeContainer x = new MapBasedReadonlyAttributeContainer (ContainerHelper.newMap (new String [] {},
                                                                                                           new Object [] {}));
    assertNotNull (x.getAllAttributeNames ());
    assertTrue (x.getAllAttributeNames ().isEmpty ());
    assertTrue (x.containsNoAttribute ());

    x = new MapBasedReadonlyAttributeContainer (ContainerHelper.newMap (new String [] { "key", "key2", "key3" },
                                                                        new Object [] { "value", "value2", null }));
    assertEquals (3, x.getAttributeCount ());
    assertFalse (x.containsNoAttribute ());
    assertEquals ("value2", x.getAttributeObject ("key2"));
    assertEquals ("value2", x.getAttributeAsString ("key2"));
    assertEquals ("value2", x.getCastedAttribute ("key2"));
    assertEquals ("def", x.getCastedAttribute ("key none", "def"));
    assertTrue (x.containsAttribute ("key2"));
    assertTrue (x.containsAttribute ("key3"));
    assertNull (x.getAttributeObject ("key3"));
    assertFalse (x.getAllAttributes ().isEmpty ());
    assertFalse (x.getAllAttributeNames ().isEmpty ());
    assertTrue (x.getAttributeNames ().hasMoreElements ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new MapBasedReadonlyAttributeContainer (ContainerHelper.newMap (new String [] { "key",
                                                                                                                                                   "key2" },
                                                                                                                                    new Object [] { "value",
                                                                                                                                                   "value2" })),
                                                                    new MapBasedReadonlyAttributeContainer (ContainerHelper.newMap (new String [] { "key",
                                                                                                                                                   "key2" },
                                                                                                                                    new Object [] { "value",
                                                                                                                                                   "value2" })));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new MapBasedReadonlyAttributeContainer (ContainerHelper.newMap (new String [] { "key",
                                                                                                                                                       "key2" },
                                                                                                                                        new Object [] { "value",
                                                                                                                                                       "value2" })),
                                                                        new MapBasedAttributeContainer (ContainerHelper.newMap (new String [] { "key",
                                                                                                                                               "key2" },
                                                                                                                                new Object [] { "value",
                                                                                                                                               "value" })));

    try
    {
      new MapBasedReadonlyAttributeContainer ((Map <String, Object>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
