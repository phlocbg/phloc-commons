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
 * Test class for class {@link SetDataType}.
 * 
 * @author Philip Helger
 */
public final class SetDataTypeTest
{
  @Test
  public void testBasic ()
  {
    final SetDataType aSDT = new SetDataType (SimpleDataTypeRegistry.DT_INT);
    assertFalse (aSDT.isSimple ());
    assertTrue (aSDT.isComplex ());
    assertFalse (aSDT.isList ());
    assertTrue (aSDT.isSet ());
    assertEquals (SimpleDataTypeRegistry.DT_INT, aSDT.getNestedDataType ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aSDT,
                                                                    new SetDataType (SimpleDataTypeRegistry.DT_INT));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aSDT,
                                                                        new SetDataType (SimpleDataTypeRegistry.DT_LONG));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aSDT,
                                                                        new ListDataType (SimpleDataTypeRegistry.DT_INT));
  }
}
