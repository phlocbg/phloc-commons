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
package com.phloc.types.datatype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.types.datatype.IDataType;

/**
 * Test class for class {@link DataTypeParser}.
 * 
 * @author Philip Helger
 */
public final class DataTypeParserTest
{
  private static void _parse (final IDataType aDT)
  {
    final String sID = aDT.getID ();
    assertTrue (sID.length () > 0);
    assertEquals (aDT, DataTypeParser.getDataTypeFromString (sID));
    if (aDT.isComplex ())
      PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aDT, DataTypeParser.getDataTypeFromString (sID));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aDT, new ListDataType (aDT));
  }

  @Test
  public void testParse ()
  {
    for (final String sID : SimpleDataTypeRegistry.getAllRegisteredSimpleDataTypeIDs ())
    {
      final IDataType aDT = SimpleDataTypeRegistry.getSimpleDataTypeOfID (sID);
      _parse (aDT);
      _parse (new ListDataType (aDT));
      _parse (new SetDataType (aDT));
      _parse (new MapDataType (aDT, aDT));
      _parse (new MapDataType (aDT, new ListDataType (aDT)));
      _parse (new MapDataType (aDT, new MapDataType (aDT, new ListDataType (aDT))));
      _parse (new MapDataType (new MapDataType (aDT, new SetDataType (aDT)), new MapDataType (aDT,
                                                                                              new ListDataType (aDT))));
    }
  }
}
