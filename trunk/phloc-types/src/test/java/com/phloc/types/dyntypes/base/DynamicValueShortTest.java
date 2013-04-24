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

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Test class for class {@link DynamicValueShort}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueShortTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueShort aDTD = new DynamicValueShort ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueShort ((short) 1);
    assertEquals (1, aDTD.getValue ().shortValue ());
    aDTD.setValue (Short.valueOf ((short) 3));
    assertEquals (3, aDTD.getValue ().shortValue ());
    aDTD.setValue (Integer.valueOf (4));
    assertEquals (4, aDTD.getValue ().shortValue ());
    aDTD.setValue ((short) 12);
    assertEquals (12, aDTD.getValue ().shortValue ());
    aDTD.setValue (Short.valueOf ((short) 3));
    assertEquals ("3", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("-42"));
    assertEquals (-42, aDTD.getValue ().shortValue ());
    assertEquals (ESuccess.FAILURE, aDTD.setAsSerializationText ("abc die Katze lief im Schnee"));
    assertEquals (-42, aDTD.getValue ().shortValue ());
    try
    {
      aDTD.setValue ("ABC");
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
    assertEquals (-42, aDTD.getValue ().shortValue ());

    // Check display text
    assertEquals ("-42", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueShort ((short) 1),
                                                                    new DynamicValueShort ((short) 1));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueShort ((short) 2),
                                                                        new DynamicValueShort ((short) 3));
  }
}
