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
package com.phloc.commons.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Helper class for rounding numeric values in a flexible way.
 * 
 * @author philip
 */
@Immutable
public final class RoundHelper
{
  public static enum EDecimalType
  {
    /** Fixed number representation */
    FIX,

    /** Exponential number representation */
    EXP;
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final RoundHelper s_aInstance = new RoundHelper ();

  private RoundHelper ()
  {}

  /**
   * Source: http://www.luschny.de/java/doubleformat.html
   * 
   * @param dValue
   *        The value to be rounded
   * @param nScale
   *        The precision of the decimal scale. If type is
   *        {@link EDecimalType#FIX} the decimal scale, else the (carrying scale
   *        - 1). Should be &ge; 0.
   * @param eType
   *        The formatting type. May not be <code>null</code>.
   * @param eMode
   *        The rounding mode to be used. May not be <code>null</code>.
   * @return the rounded value according to the specified rules. For NaN and
   *         infinite values, the input value is returned as is.
   */
  public static double getRounded (final double dValue,
                                   @Nonnegative final int nScale,
                                   @Nonnull final RoundingMode eMode,
                                   @Nonnull final EDecimalType eType)
  {
    if (nScale < 0)
      throw new IllegalArgumentException ("Scale is too small: " + nScale);
    if (eMode == null)
      throw new NullPointerException ("roundingMode");
    if (eType == null)
      throw new NullPointerException ("type");

    if (Double.isNaN (dValue) || Double.isInfinite (dValue))
      return dValue;

    final BigDecimal bd = BigDecimal.valueOf (dValue);
    if (eType == EDecimalType.EXP)
    {
      final BigDecimal bc = new BigDecimal (bd.unscaledValue (), bd.precision () - 1);
      return bc.setScale (nScale, eMode).scaleByPowerOfTen (bc.scale () - bd.scale ()).doubleValue ();
    }
    return bd.setScale (nScale, eMode).doubleValue ();
  }

  /**
   * Round using the {@link RoundingMode#HALF_UP} mode and fix representation
   * 
   * @param dValue
   *        The value to be rounded
   * @param nScale
   *        The precision scale
   * @return the rounded value
   */
  public static double getRoundedUpFix (final double dValue, @Nonnegative final int nScale)
  {
    return getRounded (dValue, nScale, RoundingMode.HALF_UP, EDecimalType.FIX);
  }

  /**
   * Round using the {@link RoundingMode#HALF_UP} mode, fix representation and a
   * precision scale of 2.
   * 
   * @param dValue
   *        The value to be rounded
   * @return the rounded value
   */
  public static double getRoundedUpFix2 (final double dValue)
  {
    return getRoundedUpFix (dValue, 2);
  }

  /**
   * Round using the {@link RoundingMode#HALF_EVEN} mode and exponential
   * representation
   * 
   * @param dValue
   *        The value to be rounded
   * @param nScale
   *        The precision scale
   * @return the rounded value
   */
  public static double getRoundedEvenExp (final double dValue, @Nonnegative final int nScale)
  {
    return getRounded (dValue, nScale, RoundingMode.HALF_EVEN, EDecimalType.EXP);
  }

  /**
   * Round using the {@link RoundingMode#HALF_EVEN} mode, exponential
   * representation and a precision scale of 2.
   * 
   * @param dValue
   *        The value to be rounded
   * @return the rounded value
   */
  public static double getRoundedEvenExp2 (final double dValue)
  {
    return getRoundedEvenExp (dValue, 2);
  }

  /**
   * Source: http://www.luschny.de/java/doubleformat.html
   * 
   * @param dValue
   *        the value to be formatted
   * @param nScale
   *        The precision of the decimal scale. If type is
   *        {@link EDecimalType#FIX} the decimal scale, else the (carrying scale
   *        - 1). Should be &ge; 0.
   * @param eType
   *        The formatting type. May not be <code>null</code>.
   * @return the string representation of the double value. For NaN and infinite
   *         values, the return of {@link Double#toString()} is returned.
   */
  @Nonnull
  public static String getFormatted (final double dValue,
                                     @Nonnegative final int nScale,
                                     @Nonnull final EDecimalType eType)
  {
    if (nScale < 0)
      throw new IllegalArgumentException ("Scale is too small: " + nScale);
    if (eType == null)
      throw new NullPointerException ("type");

    if (Double.isNaN (dValue) || Double.isInfinite (dValue))
      return Double.toString (dValue);

    // Avoid negative scales
    final DecimalFormat aDF = new DecimalFormat ();
    aDF.setMaximumFractionDigits (nScale);
    aDF.setMinimumFractionDigits (nScale);

    if (eType == EDecimalType.EXP)
    {
      final StringBuilder sb = new StringBuilder ("0E0");
      if (nScale > 0)
        sb.append (".000000000000000000", 0, nScale + 1);
      aDF.applyPattern (sb.toString ());
    }
    else
    {
      aDF.setGroupingUsed (false);
      aDF.setMinimumIntegerDigits (1);
    }
    return aDF.format (dValue);
  }

  @Nonnull
  public static String getFormattedFix (final double dValue, @Nonnegative final int nScale)
  {
    return getFormatted (dValue, nScale, EDecimalType.FIX);
  }

  @Nonnull
  public static String getFormattedFix2 (final double dValue)
  {
    return getFormattedFix (dValue, 2);
  }

  @Nonnull
  public static String getFormattedExp (final double dValue, @Nonnegative final int nScale)
  {
    return getFormatted (dValue, nScale, EDecimalType.EXP);
  }

  @Nonnull
  public static String getFormattedExp2 (final double dValue)
  {
    return getFormattedExp (dValue, 2);
  }
}
