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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Contains several math help routines.
 * 
 * @author philip
 */
@Immutable
public final class MathHelper
{
  private static final long LONG_HIGH_BITS = 0xFFFFFFFF80000000L;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MathHelper s_aInstance = new MathHelper ();

  private MathHelper ()
  {}

  /**
   * Divides the passed int dividend through the passed divisor (nDividend /
   * nDivisor)
   * 
   * @param nDividend
   *        the dividend
   * @param nDivisor
   *        the divisor
   * @return a double representing the exact quotient. Returns
   *         {@link Double#NaN} if the divisor is 0.
   */
  public static double getDividedDouble (final int nDividend, final int nDivisor)
  {
    final double dDividend = nDividend;
    final double dDivisor = nDivisor;
    return dDividend / dDivisor;
  }

  /**
   * Get the division result using {@link BigDecimal}.
   * 
   * @param nDividend
   *        the dividend
   * @param nDivisor
   *        the divisor
   * @return the result of the division
   * @throws ArithmeticException
   *         if the divisor is 0.
   */
  @Nonnull
  public static BigDecimal getDividedBigDecimal (final int nDividend, final int nDivisor) throws ArithmeticException
  {
    final BigDecimal aDividend = BigDecimal.valueOf (nDividend);
    final BigDecimal aDivisor = BigDecimal.valueOf (nDivisor);
    return aDividend.divide (aDivisor);
  }

  public static int getIntDividedCeil (final int nDividend, final int nDivisor)
  {
    return getIntDivided (nDividend, nDivisor, RoundingMode.CEILING);
  }

  public static int getIntDividedFloor (final int nDividend, final int nDivisor)
  {
    return getIntDivided (nDividend, nDivisor, RoundingMode.FLOOR);
  }

  public static int getIntDivided (final int nDividend, final int nDivisor, @Nonnull final RoundingMode eRoundingMode)
  {
    return new BigDecimal (nDividend).divide (new BigDecimal (nDivisor), eRoundingMode).intValue ();
  }

  public static long getLongDividedCeil (final long nDividend, final long nDivisor)
  {
    return getLongDivided (nDividend, nDivisor, RoundingMode.CEILING);
  }

  public static long getLongDividedFloor (final long nDividend, final long nDivisor)
  {
    return getLongDivided (nDividend, nDivisor, RoundingMode.FLOOR);
  }

  public static long getLongDivided (final long nDividend,
                                     final long nDivisor,
                                     @Nonnull final RoundingMode eRoundingMode)
  {
    return new BigDecimal (nDividend).divide (new BigDecimal (nDivisor), eRoundingMode).longValue ();
  }

  public static boolean canConvertLongToInt (final long nValue)
  {
    return (nValue & LONG_HIGH_BITS) == 0 || (nValue & LONG_HIGH_BITS) == LONG_HIGH_BITS;
  }

  public static int longToInt (final long nValue, final int nFallback)
  {
    return canConvertLongToInt (nValue) ? (int) nValue : nFallback;
  }

  public static int getMaxInt (final int nValue, @Nonnull final int... aValues)
  {
    int ret = nValue;
    for (final int n : aValues)
      ret = Math.max (ret, n);
    return ret;
  }

  public static long getMaxLong (final long nValue, @Nonnull final long... aValues)
  {
    long ret = nValue;
    for (final long n : aValues)
      ret = Math.max (ret, n);
    return ret;
  }

  public static double getMaxFloat (final float fValue, @Nonnull final float... aValues)
  {
    float ret = fValue;
    for (final float f : aValues)
      ret = Math.max (ret, f);
    return ret;
  }

  public static double getMaxDouble (final double dValue, @Nonnull final double... aValues)
  {
    double ret = dValue;
    for (final double d : aValues)
      ret = Math.max (ret, d);
    return ret;
  }

  public static int getMinInt (final int nValue, @Nonnull final int... aValues)
  {
    int ret = nValue;
    for (final int n : aValues)
      ret = Math.min (ret, n);
    return ret;
  }

  public static long getMinLong (final long nValue, @Nonnull final long... aValues)
  {
    long ret = nValue;
    for (final long n : aValues)
      ret = Math.min (ret, n);
    return ret;
  }

  public static double getMinFloat (final float fValue, @Nonnull final float... aValues)
  {
    float ret = fValue;
    for (final float f : aValues)
      ret = Math.min (ret, f);
    return ret;
  }

  public static double getMinDouble (final double dValue, @Nonnull final double... aValues)
  {
    double ret = dValue;
    for (final double d : aValues)
      ret = Math.min (ret, d);
    return ret;
  }

  /**
   * This is a fix for <code>Math.abs</code> as it would return
   * {@link Integer#MIN_VALUE} for {@link Integer#MIN_VALUE} which is very
   * unexpected. Instead an exception is thrown.
   * 
   * @param nValue
   *        Input value
   * @return The output value
   * @throws IllegalArgumentException
   *         if the input value is {@link Integer#MIN_VALUE}
   */
  @Nonnegative
  public static int abs (final int nValue)
  {
    // As Integer.MIN_VALUE is -2^31 and Integer.MAX_VALUE is 2^31-1 it means
    // that there is not integer value matching 2^31!!!
    if (nValue == Integer.MIN_VALUE)
      throw new IllegalArgumentException ("There is not absolute value for Integer.MIN_VALUE!");
    return Math.abs (nValue);
  }

  /**
   * This is a fix for <code>Math.abs</code> as it would return
   * {@link Long#MIN_VALUE} for {@link Long#MIN_VALUE} which is very unexpected.
   * Instead an exception is thrown.
   * 
   * @param nValue
   *        Input value
   * @return The output value
   * @throws IllegalArgumentException
   *         if the input value is {@link Long#MIN_VALUE}
   */
  @Nonnegative
  public static long abs (final long nValue)
  {
    // As Long.MIN_VALUE is -2^63 and Integer.MAX_VALUE is 2^63-1 it means
    // that there is not integer value matching 2^63!!!
    if (nValue == Long.MIN_VALUE)
      throw new IllegalArgumentException ("There is not absolute value for Long.MIN_VALUE!");
    return Math.abs (nValue);
  }
}
