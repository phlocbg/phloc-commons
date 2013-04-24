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

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;

/**
 * Test class for class {@link DynamicValueBoolean}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueBooleanTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueBoolean aDTD = new DynamicValueBoolean ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueBoolean (true);
    assertEquals (Boolean.TRUE, aDTD.getValue ());
    aDTD.setValue (Boolean.TRUE);
    assertEquals (Boolean.TRUE, aDTD.getValue ());
    aDTD.setValue (true);
    assertEquals (Boolean.TRUE, aDTD.getValue ());
    aDTD.setValue (Boolean.FALSE);
    assertEquals (Boolean.FALSE, aDTD.getValue ());
    assertEquals ("false", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("true"));
    assertEquals (Boolean.TRUE, aDTD.getValue ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("abc die Katze lief im Schnee"));
    assertEquals (Boolean.FALSE, aDTD.getValue ());
    aDTD.setValue ("ABC");
    assertEquals (Boolean.FALSE, aDTD.getValue ());

    // Check display text
    assertEquals ("false", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueBoolean (true),
                                                                    new DynamicValueBoolean (true));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueBoolean (true),
                                                                        new DynamicValueBoolean (false));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueBoolean (true),
                                                                        new DynamicValueBigInteger ());
  }
}
