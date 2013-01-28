/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.commons.mock;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * Extensions for the default JUnit assertions.
 * 
 * @author philip
 */
@Immutable
public final class PhlocAssert
{
  private static final double DOUBLE_ALLOWED_ROUNDING_DIFFERENCE = 0.001;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final PhlocAssert s_aInstance = new PhlocAssert ();

  private PhlocAssert ()
  {}

  public static void assertEquals (final double x, final double y)
  {
    assertEquals (null, x, y);
  }

  public static void assertEquals (final double x, final Double y)
  {
    if (y == null)
      throw new NullPointerException ("y");
    assertEquals (null, x, y.doubleValue ());
  }

  public static void assertEquals (final String sUserMsg, final double x, final double y)
  {
    if (Double.compare (x, y) != 0 && Math.abs (x - y) > DOUBLE_ALLOWED_ROUNDING_DIFFERENCE)
      throw new IllegalArgumentException ("<" +
                                          x +
                                          "> is not equal to <" +
                                          y +
                                          ">" +
                                          (StringHelper.hasText (sUserMsg) ? ": " + sUserMsg : ""));
  }

  public static void assertEquals (final float x, final float y)
  {
    assertEquals (null, x, y);
  }

  public static void assertEquals (final float x, final Float y)
  {
    if (y == null)
      throw new NullPointerException ("y");
    assertEquals (null, x, y.floatValue ());
  }

  public static void assertEquals (final String sUserMsg, final float x, final float y)
  {
    if (Float.compare (x, y) != 0 && Math.abs (x - y) > DOUBLE_ALLOWED_ROUNDING_DIFFERENCE)
      throw new IllegalArgumentException ("<" +
                                          x +
                                          "> is not equal to <" +
                                          y +
                                          ">" +
                                          (StringHelper.hasText (sUserMsg) ? ": " + sUserMsg : ""));
  }
}