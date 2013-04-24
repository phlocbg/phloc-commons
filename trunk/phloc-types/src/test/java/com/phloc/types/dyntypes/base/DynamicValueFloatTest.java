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
import com.phloc.commons.mock.PhlocAssert;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Test class for class {@link DynamicValueFloat}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueFloatTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueFloat aDTD = new DynamicValueFloat ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueFloat (1.2f);
    PhlocAssert.assertEquals (1.2f, aDTD.getValue ());
    aDTD.setValue (Float.valueOf (3.14f));
    PhlocAssert.assertEquals (3.14f, aDTD.getValue ());
    aDTD.setValue (Integer.valueOf (3));
    PhlocAssert.assertEquals (3f, aDTD.getValue ());
    aDTD.setValue (12.34f);
    PhlocAssert.assertEquals (12.34f, aDTD.getValue ());
    aDTD.setValue (Float.valueOf (3.14f));
    assertEquals ("3.14", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("-42.4711"));
    PhlocAssert.assertEquals (-42.4711f, aDTD.getValue ());
    assertEquals (ESuccess.FAILURE, aDTD.setAsSerializationText ("abc die Katze lief im Schnee"));
    PhlocAssert.assertEquals (-42.4711f, aDTD.getValue ());
    try
    {
      aDTD.setValue ("ABC");
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
    PhlocAssert.assertEquals (-42.4711f, aDTD.getValue ());

    // Check display text
    assertEquals ("-42,471", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueFloat (1.2f),
                                                                    new DynamicValueFloat (1.2f));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueFloat (1.2f),
                                                                        new DynamicValueFloat (1.3f));
  }
}
