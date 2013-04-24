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
 * Test class for class {@link DynamicValueDouble}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueDoubleTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueDouble aDTD = new DynamicValueDouble ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueDouble (1.2);
    PhlocAssert.assertEquals (1.2, aDTD.getValue ());
    aDTD.setValue (Double.valueOf (3.14));
    PhlocAssert.assertEquals (3.14, aDTD.getValue ());
    aDTD.setValue (Integer.valueOf (3));
    PhlocAssert.assertEquals (3d, aDTD.getValue ());
    aDTD.setValue (12.34d);
    PhlocAssert.assertEquals (12.34d, aDTD.getValue ());
    aDTD.setValue (Double.valueOf (3.14));
    assertEquals ("3.14", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("-42.4711"));
    PhlocAssert.assertEquals (-42.4711, aDTD.getValue ());
    assertEquals (ESuccess.FAILURE, aDTD.setAsSerializationText ("abc die Katze lief im Schnee"));
    PhlocAssert.assertEquals (-42.4711, aDTD.getValue ());
    try
    {
      aDTD.setValue ("ABC");
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
    PhlocAssert.assertEquals (-42.4711, aDTD.getValue ());

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
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueDouble (1.2),
                                                                    new DynamicValueDouble (1.2));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueDouble (1.2),
                                                                        new DynamicValueDouble (1.3));
  }
}
