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
package com.phloc.types.dyntypes.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.factory.FactoryNull;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Test class for class {@link DynamicValueString}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueStringTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueString aDTD = new DynamicValueString ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueString ("any");
    assertEquals ("any", aDTD.getValue ());
    aDTD.setValue (String.valueOf ((short) 3));
    assertEquals ("3", aDTD.getValue ());
    aDTD.setValue (Integer.valueOf (4));
    assertEquals ("4", aDTD.getValue ());
    aDTD.setValue ("xyz");
    assertEquals ("xyz", aDTD.getValue ());
    aDTD.setValue (String.valueOf ((short) 3));
    assertEquals ("3", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("-42"));
    assertEquals ("-42", aDTD.getValue ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("abc die Katze lief im Schnee"));
    assertEquals ("abc die Katze lief im Schnee", aDTD.getValue ());
    try
    {
      aDTD.setValue (FactoryNull.getInstance ());
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
    assertEquals ("abc die Katze lief im Schnee", aDTD.getValue ());

    // Check display text
    assertEquals ("abc die Katze lief im Schnee", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueString ("1"),
                                                                    new DynamicValueString ("1"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueString ("2"),
                                                                        new DynamicValueString ("3"));
  }
}
