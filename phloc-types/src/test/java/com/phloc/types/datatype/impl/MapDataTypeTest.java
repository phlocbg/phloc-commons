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
package com.phloc.types.datatype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MapDataType}.
 * 
 * @author Philip Helger
 */
public final class MapDataTypeTest
{
  @Test
  public void testBasic ()
  {
    final MapDataType aLDT = new MapDataType (SimpleDataTypeRegistry.DT_INT, SimpleDataTypeRegistry.DT_STRING);
    assertFalse (aLDT.isSimple ());
    assertTrue (aLDT.isComplex ());
    assertEquals (SimpleDataTypeRegistry.DT_INT, aLDT.getKeyDataType ());
    assertEquals (SimpleDataTypeRegistry.DT_STRING, aLDT.getValueDataType ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aLDT,
                                                                    new MapDataType (SimpleDataTypeRegistry.DT_INT,
                                                                                     SimpleDataTypeRegistry.DT_STRING));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aLDT,
                                                                        new MapDataType (SimpleDataTypeRegistry.DT_INT,
                                                                                         SimpleDataTypeRegistry.DT_LONG));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aLDT,
                                                                        new MapDataType (SimpleDataTypeRegistry.DT_BYTE,
                                                                                         SimpleDataTypeRegistry.DT_STRING));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aLDT,
                                                                        new ListDataType (SimpleDataTypeRegistry.DT_INT));
  }
}
