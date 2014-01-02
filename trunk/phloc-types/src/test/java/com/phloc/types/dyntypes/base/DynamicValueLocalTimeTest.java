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
package com.phloc.types.dyntypes.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.LocalTime;
import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.datetime.PDTFactory;

/**
 * Test class for class {@link DynamicValueLocalTime}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueLocalTimeTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final DynamicValueLocalTime aDTD = new DynamicValueLocalTime ();
    assertNull (aDTD.getValue ());
    LocalTime aNow = PDTFactory.getCurrentLocalTime ();
    aDTD.setValue (aNow);
    assertEquals (aNow, aDTD.getValue ());
    aNow = PDTFactory.createLocalTime (15, 45, 5);
    aDTD.setValue (aNow);
    assertEquals (aNow, aDTD.getValue ());

    // Set as java.util.Time
    assertTrue (aDTD.setValue (new Date (123456000L)).isChanged ());
    assertEquals (PDTFactory.createLocalTime (11, 17, 36), aDTD.getValue ());
    // Set as GregorianCalendar
    assertTrue (aDTD.setValue (new GregorianCalendar (2010, Calendar.MAY, 13, 12, 15)).isChanged ());
    assertEquals (PDTFactory.createLocalTime (12, 15), aDTD.getValue ());
    // Set as Long
    assertTrue (aDTD.setValue (Long.valueOf (123456000L)).isChanged ());
    assertEquals (PDTFactory.createLocalTime (11, 17, 36), aDTD.getValue ());
    // Set as String
    assertTrue (aDTD.setValue ("12:15:00.000").isChanged ());
    assertEquals (PDTFactory.createLocalTime (12, 15), aDTD.getValue ());

    // Serialization
    assertEquals ("40500000", aDTD.getAsSerializationText ());
    assertTrue (aDTD.setAsSerializationText ("40500000").isSuccess ());
    assertEquals ("12:15:00.000", aDTD.getValue ().toString ());
    assertFalse (aDTD.setAsSerializationText ("this is not a number").isSuccess ());
    assertEquals ("12:15:00.000", aDTD.getValue ().toString ());

    // Check display text
    assertEquals ("12:15:00", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getValue ());

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aDTD, new DynamicValueLocalTime ());
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aDTD,
                                                                        new DynamicValueLocalTime (PDTFactory.getCurrentLocalTime ()));
  }
}
