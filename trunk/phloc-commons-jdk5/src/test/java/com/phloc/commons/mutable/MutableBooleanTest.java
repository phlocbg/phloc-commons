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
package com.phloc.commons.mutable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * Test class for class {@link MutableBoolean}.
 * 
 * @author Philip Helger
 */
public final class MutableBooleanTest
{
  @Test
  public void testMutableBoolean ()
  {
    final MutableBoolean x = new MutableBoolean ();
    assertFalse (x.booleanValue ());
    assertSame (Boolean.FALSE, x.getAsBoolean ());
    assertTrue (x.set (true).isChanged ());
    assertTrue (x.booleanValue ());
    assertSame (Boolean.TRUE, x.getAsBoolean ());
    assertFalse (x.set (true).isChanged ());
    assertTrue (x.booleanValue ());
    assertSame (Boolean.TRUE, x.getAsBoolean ());
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new MutableBoolean (true),
                                                                    new MutableBoolean (true));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new MutableBoolean (true),
                                                                        new MutableBoolean (false));
    PhlocTestUtils.testGetClone (new MutableBoolean (true));
  }

  @Test
  public void testTypeConversion ()
  {
    final MutableBoolean x = new MutableBoolean (true);
    final Boolean b = TypeConverter.convertIfNecessary (x, Boolean.class);
    assertNotNull (b);
    assertTrue (b.booleanValue ());
    assertTrue (TypeConverter.convertToBoolean (x));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (x,
                                                                    TypeConverter.convert (true, MutableBoolean.class));
  }
}
