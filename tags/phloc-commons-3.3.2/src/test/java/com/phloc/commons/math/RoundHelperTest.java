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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.math.RoundingMode;

import org.junit.Test;

import com.phloc.commons.math.RoundHelper.EDecimalType;
import com.phloc.commons.mock.PhlocAssert;

/**
 * Test class for class {@link RoundHelper}.
 * 
 * @author philip
 */
public final class RoundHelperTest
{
  @Test
  public void testDecimalType ()
  {
    for (final EDecimalType e : EDecimalType.values ())
      assertSame (e, EDecimalType.valueOf (e.name ()));
  }

  @Test
  public void testGetRounded ()
  {
    final double d = 1.1;
    try
    {
      // Negative scale
      RoundHelper.getRounded (d, -1, RoundingMode.CEILING, EDecimalType.FIX);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      RoundHelper.getRounded (d, 1, null, EDecimalType.FIX);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      // Negative scale
      RoundHelper.getRounded (d, 1, RoundingMode.CEILING, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetRoundedUpFix ()
  {
    PhlocAssert.assertEquals (1, RoundHelper.getRoundedUpFix (0.9, 0));
    PhlocAssert.assertEquals (1, RoundHelper.getRoundedUpFix (1.0, 0));
    PhlocAssert.assertEquals (1, RoundHelper.getRoundedUpFix (1.1, 0));
    PhlocAssert.assertEquals (1, RoundHelper.getRoundedUpFix (1.4, 0));
    PhlocAssert.assertEquals (1, RoundHelper.getRoundedUpFix (1.4999, 0));
    PhlocAssert.assertEquals (1.50, RoundHelper.getRoundedUpFix (1.4999, 2));
    PhlocAssert.assertEquals (1.50, RoundHelper.getRoundedUpFix2 (1.4999));
    PhlocAssert.assertEquals (2, RoundHelper.getRoundedUpFix (1.5, 0));
    PhlocAssert.assertEquals (Double.NaN, RoundHelper.getRoundedUpFix (Double.NaN, 0));
    PhlocAssert.assertEquals (Double.POSITIVE_INFINITY, RoundHelper.getRoundedUpFix (Double.POSITIVE_INFINITY, 0));
    PhlocAssert.assertEquals (Double.NEGATIVE_INFINITY, RoundHelper.getRoundedUpFix (Double.NEGATIVE_INFINITY, 0));
  }

  @Test
  public void testGetFormatted ()
  {
    final double d = 1.1;
    try
    {
      RoundHelper.getFormatted (d, -1, EDecimalType.FIX);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      RoundHelper.getFormatted (d, 1, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetRoundedEvenExp ()
  {
    PhlocAssert.assertEquals (9E-1, RoundHelper.getRoundedEvenExp (0.9, 0));
    PhlocAssert.assertEquals (1E0, RoundHelper.getRoundedEvenExp (1.0, 0));
    PhlocAssert.assertEquals (1E0, RoundHelper.getRoundedEvenExp (1.1, 0));
    PhlocAssert.assertEquals (1E0, RoundHelper.getRoundedEvenExp (1.4, 0));
    PhlocAssert.assertEquals (1E0, RoundHelper.getRoundedEvenExp (1.4999, 0));
    PhlocAssert.assertEquals (1.50E0, RoundHelper.getRoundedEvenExp (1.4999, 2));
    PhlocAssert.assertEquals (1.50E0, RoundHelper.getRoundedEvenExp2 (1.4999));
    PhlocAssert.assertEquals (2E0, RoundHelper.getRoundedEvenExp (1.5, 0));
  }

  @Test
  public void testGetFormattedFix ()
  {
    assertEquals ("1", RoundHelper.getFormattedFix (0.9, 0));
    assertEquals ("1", RoundHelper.getFormattedFix (1.0, 0));
    assertEquals ("1", RoundHelper.getFormattedFix (1.1, 0));
    assertEquals ("1", RoundHelper.getFormattedFix (1.4, 0));
    assertEquals ("1", RoundHelper.getFormattedFix (1.4999, 0));
    assertEquals ("2", RoundHelper.getFormattedFix (1.5, 0));

    assertEquals ("0,90", RoundHelper.getFormattedFix2 (0.9));
    assertEquals ("1,00", RoundHelper.getFormattedFix2 (1.0));
    assertEquals ("1,10", RoundHelper.getFormattedFix2 (1.1));
    assertEquals ("1,40", RoundHelper.getFormattedFix2 (1.4));
    assertEquals ("1,50", RoundHelper.getFormattedFix2 (1.4999));
    assertEquals ("1,50", RoundHelper.getFormattedFix2 (1.5));
    assertEquals (Double.toString (Double.NaN), RoundHelper.getFormattedFix (Double.NaN, 0));
    assertEquals (Double.toString (Double.POSITIVE_INFINITY), RoundHelper.getFormattedFix (Double.POSITIVE_INFINITY, 0));
    assertEquals (Double.toString (Double.NEGATIVE_INFINITY), RoundHelper.getFormattedFix (Double.NEGATIVE_INFINITY, 0));
  }

  @Test
  public void testGetFormattedExp ()
  {
    assertEquals ("9E-1", RoundHelper.getFormattedExp (0.9, 0));
    assertEquals ("1E0", RoundHelper.getFormattedExp (1.0, 0));
    assertEquals ("1E0", RoundHelper.getFormattedExp (1.1, 0));
    assertEquals ("1E0", RoundHelper.getFormattedExp (1.4, 0));
    assertEquals ("1E0", RoundHelper.getFormattedExp (1.4999, 0));
    assertEquals ("2E0", RoundHelper.getFormattedExp (1.5, 0));

    assertEquals ("9,00E-1", RoundHelper.getFormattedExp2 (0.9));
    assertEquals ("1,00E0", RoundHelper.getFormattedExp2 (1.0));
    assertEquals ("1,10E0", RoundHelper.getFormattedExp2 (1.1));
    assertEquals ("1,40E0", RoundHelper.getFormattedExp2 (1.4));
    assertEquals ("1,50E0", RoundHelper.getFormattedExp2 (1.4999));
    assertEquals ("1,50E0", RoundHelper.getFormattedExp2 (1.5));
  }
}
