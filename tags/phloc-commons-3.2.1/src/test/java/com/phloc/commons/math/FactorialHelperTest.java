/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

/**
 * Test class for class {@link FactorialHelper}.
 * 
 * @author philip
 */
public final class FactorialHelperTest
{
  @Test
  public void testSmall ()
  {
    for (int i = FactorialHelper.PREDEFINED_MIN_INDEX; i <= FactorialHelper.PREDEFINED_MAX_INDEX; ++i)
    {
      final long n = FactorialHelper.getSmallFactorial (i);
      final BigInteger x = FactorialHelper.getAnyFactorialLinear (i);
      assertEquals (x, new BigInteger (Long.toString (n)));
    }

    try
    {
      FactorialHelper.getSmallFactorial (FactorialHelper.PREDEFINED_MIN_INDEX - 1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      FactorialHelper.getSmallFactorial (FactorialHelper.PREDEFINED_MAX_INDEX + 1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testGetAnyFactorialLinear ()
  {
    final BigInteger y = FactorialHelper.getAnyFactorialLinear (FactorialHelper.PREDEFINED_MAX_INDEX + 1);
    assertEquals (FactorialHelper.getAnyFactorialLinear (FactorialHelper.PREDEFINED_MAX_INDEX)
                                 .multiply (new BigInteger (Long.toString (FactorialHelper.PREDEFINED_MAX_INDEX + 1))),
                  y);

    final BigInteger z = FactorialHelper.getAnyFactorialLinear (FactorialHelper.PREDEFINED_MAX_INDEX + 2);
    assertEquals (FactorialHelper.getAnyFactorialLinear (FactorialHelper.PREDEFINED_MAX_INDEX)
                                 .multiply (new BigInteger (Long.toString (FactorialHelper.PREDEFINED_MAX_INDEX + 1)))
                                 .multiply (new BigInteger (Long.toString (FactorialHelper.PREDEFINED_MAX_INDEX + 2))),
                  z);

    try
    {
      FactorialHelper.getAnyFactorialLinear (-1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
