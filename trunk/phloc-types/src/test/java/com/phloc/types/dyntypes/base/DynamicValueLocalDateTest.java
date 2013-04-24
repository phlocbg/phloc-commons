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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.datetime.PDTFactory;

/**
 * Test class for class {@link DynamicValueLocalDate}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueLocalDateTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final DynamicValueLocalDate aDTD = new DynamicValueLocalDate ();
    assertNull (aDTD.getValue ());
    LocalDate aNow = PDTFactory.getCurrentLocalDate ();
    aDTD.setValue (aNow);
    assertEquals (aNow, aDTD.getValue ());
    aNow = PDTFactory.createLocalDate (2010, 5, 12);
    aDTD.setValue (aNow);
    assertEquals (aNow, aDTD.getValue ());

    // Set as java.util.Date
    assertTrue (aDTD.setValue (new Date (1270936800000L)).isChanged ());
    assertEquals (PDTFactory.createLocalDate (2010, DateTimeConstants.APRIL, 11), aDTD.getValue ());
    // Set as GregorianCalendar
    assertTrue (aDTD.setValue (new GregorianCalendar (2010, Calendar.MAY, 13)).isChanged ());
    assertEquals (PDTFactory.createLocalDate (2010, DateTimeConstants.MAY, 13), aDTD.getValue ());
    // Set as Long
    assertTrue (aDTD.setValue (Long.valueOf (1270936800000L)).isChanged ());
    assertEquals (PDTFactory.createLocalDate (2010, DateTimeConstants.APRIL, 11), aDTD.getValue ());
    // Set as String
    assertTrue (aDTD.setValue ("2010-04-09").isChanged ());
    assertEquals (PDTFactory.createLocalDate (2010, DateTimeConstants.APRIL, 9), aDTD.getValue ());

    // Serialization
    assertEquals ("1270764000000", aDTD.getAsSerializationText ());
    assertTrue (aDTD.setAsSerializationText ("1270764000000").isSuccess ());
    assertEquals ("2010-04-09", aDTD.getValue ().toString ());
    assertFalse (aDTD.setAsSerializationText ("this is not a number").isSuccess ());
    assertEquals ("2010-04-09", aDTD.getValue ().toString ());

    // Check display text
    assertEquals ("09.04.2010", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getValue ());

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aDTD, new DynamicValueLocalDate ());
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aDTD,
                                                                        new DynamicValueLocalDate (PDTFactory.getCurrentLocalDate ()));
  }
}
