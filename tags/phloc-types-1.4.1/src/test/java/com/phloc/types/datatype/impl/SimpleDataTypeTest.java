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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link SimpleDataType}.
 * 
 * @author Philip Helger
 */
public final class SimpleDataTypeTest
{
  @Test
  public void testIsPrimitiveWrapperType ()
  {
    assertTrue (SimpleDataTypeRegistry.DT_BOOLEAN.isPrimitiveWrapperType ());
    assertFalse (SimpleDataTypeRegistry.DT_BIGDECIMAL.isPrimitiveWrapperType ());
    assertFalse (SimpleDataTypeRegistry.DT_BOOLEAN_ARRAY.isPrimitiveWrapperType ());
  }

  @Test
  public void testIsArrayType ()
  {
    assertFalse (SimpleDataTypeRegistry.DT_BOOLEAN.isArrayType ());
    assertFalse (SimpleDataTypeRegistry.DT_BIGDECIMAL.isArrayType ());
    assertTrue (SimpleDataTypeRegistry.DT_BOOLEAN_ARRAY.isArrayType ());
  }

  @Test
  public void testIsSimpleOrComplex ()
  {
    assertTrue (SimpleDataTypeRegistry.DT_BOOLEAN.isSimple ());
    assertTrue (SimpleDataTypeRegistry.DT_BIGDECIMAL.isSimple ());
    assertTrue (SimpleDataTypeRegistry.DT_BOOLEAN_ARRAY.isSimple ());

    assertFalse (SimpleDataTypeRegistry.DT_BOOLEAN.isComplex ());
    assertFalse (SimpleDataTypeRegistry.DT_BIGDECIMAL.isComplex ());
    assertFalse (SimpleDataTypeRegistry.DT_BOOLEAN_ARRAY.isComplex ());
  }
}
