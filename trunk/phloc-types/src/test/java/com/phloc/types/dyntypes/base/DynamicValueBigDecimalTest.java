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

import java.math.BigDecimal;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;

/**
 * Test class for class {@link DynamicValueBigDecimal}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueBigDecimalTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueBigDecimal aDTD = new DynamicValueBigDecimal ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueBigDecimal (BigDecimal.TEN);
    assertEquals (BigDecimal.TEN, aDTD.getValue ());
    aDTD.setValue (Integer.valueOf (3));
    assertEquals (new BigDecimal ("3.0"), aDTD.getValue ());
    aDTD.setValue ("3.3");
    assertEquals (new BigDecimal ("3.3"), aDTD.getValue ());
    aDTD.setValue ("123456789123456789123456789123456789");
    assertEquals ("123456789123456789123456789123456789", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("-123456789123456789123456789123456789"));
    assertEquals ("-123456789123456789123456789123456789", aDTD.getValue ().toString ());
    assertEquals (ESuccess.FAILURE, aDTD.setAsSerializationText ("this is not a number"));
    assertEquals ("-123456789123456789123456789123456789", aDTD.getValue ().toString ());

    // Check display text
    assertEquals ("-123456789123456789123456789123456789", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueBigDecimal (BigDecimal.TEN),
                                                                    new DynamicValueBigDecimal (BigDecimal.TEN));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueBigDecimal (BigDecimal.TEN),
                                                                        new DynamicValueBigDecimal (BigDecimal.ONE));
  }
}
