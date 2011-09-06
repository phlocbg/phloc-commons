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
package com.phloc.commons.gfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ScalableSize}.
 * 
 * @author philip
 */
public final class ScalableSizeTest
{
  @Test
  public void testCtor ()
  {
    final ScalableSize aID = new ScalableSize (320, 240);
    assertEquals (320, aID.getWidth ());
    assertEquals (240, aID.getHeight ());

    try
    {
      new ScalableSize (300, -1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new ScalableSize (-1, 300);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ScalableSize (320, 240), new ScalableSize (320,
                                                                                                                   240));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ScalableSize (320, 240),
                                                                        new ScalableSize (321, 240));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new ScalableSize (320, 240),
                                                                        new ScalableSize (320, 241));
  }

  @Test
  public void testGetBestMatchingSize ()
  {
    final ScalableSize aID = new ScalableSize (320, 240);

    // Width
    ScalableSize aID2 = aID.getBestMatchingSize (160, 180);
    assertEquals (160, aID2.getWidth ());
    assertEquals (120, aID2.getHeight ());

    // Height
    aID2 = aID.getBestMatchingSize (260, 120);
    assertEquals (160, aID2.getWidth ());
    assertEquals (120, aID2.getHeight ());

    // WIdth
    aID2 = aID.getBestMatchingSize (48, 120);
    assertEquals (48, aID2.getWidth ());
    assertEquals (36, aID2.getHeight ());

    assertEquals (aID, aID.getBestMatchingSize (640, 481));
    assertEquals (aID, aID.getBestMatchingSize (641, 480));

    try
    {
      // <=0 not allowed
      aID2.getBestMatchingSize (0, 100);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // <=0 not allowed
      aID2.getBestMatchingSize (100, 0);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testGetScaled ()
  {
    final ScalableSize aID = new ScalableSize (100, 150);
    ScalableSize aID2 = aID.getScaledToWidth (150);
    assertEquals (150, aID2.getWidth ());
    assertEquals (225, aID2.getHeight ());
    aID2 = aID.getScaledToHeight (300);
    assertEquals (200, aID2.getWidth ());
    assertEquals (300, aID2.getHeight ());
    aID2 = aID.getScaledToWidth (50);
    assertEquals (50, aID2.getWidth ());
    assertEquals (75, aID2.getHeight ());
    assertSame (aID2, aID2.getScaledToWidth (50));
    aID2 = aID.getScaledToHeight (75);
    assertEquals (50, aID2.getWidth ());
    assertEquals (75, aID2.getHeight ());
    assertSame (aID2, aID2.getScaledToHeight (75));

    try
    {
      aID2.getScaledToWidth (0);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      aID2.getScaledToHeight (0);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
