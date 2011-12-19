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

import java.math.BigDecimal;

import org.junit.Test;

import com.phloc.commons.mock.PhlocAssert;

/**
 * Test class for class {@link MathHelper}.
 * 
 * @author philip
 */
public final class MathHelperTest
{
  @Test
  public void testGetDividedDouble ()
  {
    PhlocAssert.assertEquals (1.5, MathHelper.getDividedDouble (3, 2));
    PhlocAssert.assertEquals (Double.NaN, MathHelper.getDividedDouble (5, 0));
    PhlocAssert.assertEquals (Double.NaN, MathHelper.getDividedDouble (0, 0));
  }

  @Test
  public void testGetDividedBigDecimal ()
  {
    assertEquals (new BigDecimal (1.5), MathHelper.getDividedBigDecimal (3, 2));

    try
    {
      MathHelper.getDividedBigDecimal (5, 0);
      fail ();
    }
    catch (final ArithmeticException ex)
    {
      // expected
    }

    try
    {
      MathHelper.getDividedBigDecimal (0, 0);
      fail ();
    }
    catch (final ArithmeticException ex)
    {
      // expected
    }
  }

  @Test
  public void testIntDivide ()
  {
    assertEquals (3, MathHelper.getIntDividedCeil (5, 2));
    assertEquals (2, MathHelper.getIntDividedFloor (5, 2));
    assertEquals (-2, MathHelper.getIntDividedCeil (-5, 2));
    assertEquals (-3, MathHelper.getIntDividedFloor (-5, 2));

    assertEquals (2, MathHelper.getIntDividedCeil (4, 2));
    assertEquals (2, MathHelper.getIntDividedFloor (4, 2));
    assertEquals (-2, MathHelper.getIntDividedCeil (-4, 2));
    assertEquals (-2, MathHelper.getIntDividedFloor (-4, 2));
  }

  @Test
  public void testLongDivide ()
  {
    assertEquals (3, MathHelper.getLongDividedCeil (5, 2));
    assertEquals (2, MathHelper.getLongDividedFloor (5, 2));
    assertEquals (-2, MathHelper.getLongDividedCeil (-5, 2));
    assertEquals (-3, MathHelper.getLongDividedFloor (-5, 2));

    assertEquals (2, MathHelper.getLongDividedCeil (4, 2));
    assertEquals (2, MathHelper.getLongDividedFloor (4, 2));
    assertEquals (-2, MathHelper.getLongDividedCeil (-4, 2));
    assertEquals (-2, MathHelper.getLongDividedFloor (-4, 2));
  }

  @Test
  public void testLongToInt ()
  {
    assertEquals (Integer.MIN_VALUE, MathHelper.longToInt (Integer.MIN_VALUE, 5));
    assertEquals (-1, MathHelper.longToInt (-1, 5));
    assertEquals (0, MathHelper.longToInt (0, 5));
    assertEquals (1, MathHelper.longToInt (1, 5));
    assertEquals (Integer.MAX_VALUE, MathHelper.longToInt (Integer.MAX_VALUE, 5));

    assertEquals (5, MathHelper.longToInt (Integer.MIN_VALUE - 1L, 5));
    assertEquals (5, MathHelper.longToInt (Integer.MAX_VALUE + 1L, 5));
    assertEquals (5, MathHelper.longToInt (Long.MIN_VALUE, 5));
    assertEquals (5, MathHelper.longToInt (Long.MAX_VALUE, 5));
  }

  @Test
  public void testGetMaxInt ()
  {
    assertEquals (5, MathHelper.getMaxInt (5));
    assertEquals (5, MathHelper.getMaxInt (5, 5, 5, 5));
    assertEquals (5, MathHelper.getMaxInt (5, 3, 2, 1));
    assertEquals (7, MathHelper.getMaxInt (5, 3, 7, 4));
  }

  @Test
  public void testGetMaxLong ()
  {
    assertEquals (5, MathHelper.getMaxLong (5));
    assertEquals (5, MathHelper.getMaxLong (5, 5, 5, 5));
    assertEquals (5, MathHelper.getMaxLong (5, 3, 2, 1));
    assertEquals (7, MathHelper.getMaxLong (5, 3, 7, 4));
  }

  @Test
  public void testGetMaxDouble ()
  {
    PhlocAssert.assertEquals (5, MathHelper.getMaxDouble (5));
    PhlocAssert.assertEquals (5, MathHelper.getMaxDouble (5, 5, 5, 5));
    PhlocAssert.assertEquals (5, MathHelper.getMaxDouble (5, 3, 2, 1));
    PhlocAssert.assertEquals (7, MathHelper.getMaxDouble (5, 3, 7, 4));
  }

  @Test
  public void testGetMinInt ()
  {
    assertEquals (5, MathHelper.getMinInt (5));
    assertEquals (5, MathHelper.getMinInt (5, 5, 5, 5));
    assertEquals (1, MathHelper.getMinInt (5, 3, 2, 1));
    assertEquals (3, MathHelper.getMinInt (5, 3, 7, 4));
  }

  @Test
  public void testGetMinLong ()
  {
    assertEquals (5, MathHelper.getMinLong (5));
    assertEquals (5, MathHelper.getMinLong (5, 5, 5, 5));
    assertEquals (1, MathHelper.getMinLong (5, 3, 2, 1));
    assertEquals (3, MathHelper.getMinLong (5, 3, 7, 4));
  }

  @Test
  public void testGetMinDouble ()
  {
    PhlocAssert.assertEquals (5, MathHelper.getMinDouble (5));
    PhlocAssert.assertEquals (5, MathHelper.getMinDouble (5, 5, 5, 5));
    PhlocAssert.assertEquals (1, MathHelper.getMinDouble (5, 3, 2, 1));
    PhlocAssert.assertEquals (3, MathHelper.getMinDouble (5, 3, 7, 4));
  }
}
