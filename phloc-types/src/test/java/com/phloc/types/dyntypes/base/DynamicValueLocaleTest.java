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
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Test class for class {@link DynamicValueLocale}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueLocaleTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    DynamicValueLocale aDTD = new DynamicValueLocale ();
    assertNull (aDTD.getValue ());
    aDTD = new DynamicValueLocale (L_DE);
    assertEquals (L_DE, aDTD.getValue ());
    assertTrue (aDTD.setValue (L_EN).isChanged ());
    assertEquals (L_EN, aDTD.getValue ());
    assertFalse (aDTD.setValue (L_EN).isChanged ());
    assertEquals (L_EN, aDTD.getValue ());
    assertEquals ("en", aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS, aDTD.setAsSerializationText ("de"));
    assertEquals (L_DE, aDTD.getValue ());
    assertEquals (ESuccess.FAILURE, aDTD.setAsSerializationText ("abc die Katze lief im Schnee"));
    assertEquals (L_DE, aDTD.getValue ());
    try
    {
      aDTD.setValue ("");
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
    assertEquals (L_DE, aDTD.getValue ());

    // Check display text
    assertEquals ("Deutsch", aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueLocale (L_DE),
                                                                    new DynamicValueLocale (L_DE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueLocale (L_EN),
                                                                        new DynamicValueLocale (L_EN_GB));
  }
}
