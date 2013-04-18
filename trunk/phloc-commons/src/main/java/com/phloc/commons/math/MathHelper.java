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
package com.phloc.commons.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.equals.EqualsUtils;

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

  @CheckReturnValue
  public static int getLongAsInt (final long nValue, final int nFallback)
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

  @Nonnull
  public static BigDecimal getMaxBigDecimal (@Nonnull final BigDecimal aValue, @Nonnull final BigDecimal... aValues)
  {
    BigDecimal ret = aValue;
    for (final BigDecimal a : aValues)
      if (a.compareTo (ret) > 0)
        ret = a;
    return ret;
  }

  @Nonnull
  public static BigInteger getMaxBigInteger (@Nonnull final BigInteger aValue, @Nonnull final BigInteger... aValues)
  {
    BigInteger ret = aValue;
    for (final BigInteger a : aValues)
      if (a.compareTo (ret) > 0)
        ret = a;
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

  @Nonnull
  public static BigDecimal getMinBigDecimal (@Nonnull final BigDecimal aValue, @Nonnull final BigDecimal... aValues)
  {
    BigDecimal ret = aValue;
    for (final BigDecimal a : aValues)
      if (a.compareTo (ret) < 0)
        ret = a;
    return ret;
  }

  @Nonnull
  public static BigInteger getMinBigInteger (@Nonnull final BigInteger aValue, @Nonnull final BigInteger... aValues)
  {
    BigInteger ret = aValue;
    for (final BigInteger a : aValues)
      if (a.compareTo (ret) < 0)
        ret = a;
    return ret;
  }

  /**
   * This is a fix for <code>Math.abs</code> as it would return
   * {@link Integer#MIN_VALUE} for {@link Integer#MIN_VALUE} which is very
   * unexpected. Instead an exception is thrown.
   * 
   * @param nValue
   *        Input value
   * @return the absolute value of the argument.
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
   * @return the absolute value of the argument.
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

  /**
   * This is a sanity method wrapping <code>Math.abs (float)</code>, so that you
   * don't have to think whether you need to invoke the abs method from this
   * class or the one from Math directly.
   * 
   * @param fValue
   *        Input value
   * @return the absolute value of the argument.
   */
  @Nonnegative
  public static float abs (final float fValue)
  {
    return Math.abs (fValue);
  }

  /**
   * This is a sanity method wrapping <code>Math.abs (double)</code>, so that
   * you don't have to think whether you need to invoke the abs method from this
   * class or the one from Math directly.
   * 
   * @param dValue
   *        Input value
   * @return the absolute value of the argument.
   */
  @Nonnegative
  public static double abs (final double dValue)
  {
    return Math.abs (dValue);
  }

  /**
   * This is a sanity method wrapping <code>BigDecimal.abs (double)</code>, so
   * that you don't have to think whether you need to invoke the abs method from
   * this class or the one from BigDecimal directly.
   * 
   * @param aValue
   *        Input value
   * @return the absolute value of the argument.
   */
  @Nonnull
  public static BigDecimal abs (@Nonnull final BigDecimal aValue)
  {
    return aValue.abs ();
  }

  /**
   * This is a sanity method wrapping <code>BigInteger.abs (double)</code>, so
   * that you don't have to think whether you need to invoke the abs method from
   * this class or the one from BigInteger directly.
   * 
   * @param aValue
   *        Input value
   * @return the absolute value of the argument.
   */
  @Nonnull
  public static BigInteger abs (@Nonnull final BigInteger aValue)
  {
    return aValue.abs ();
  }

  public static boolean isEqualToZero (@Nonnull final BigDecimal aValue)
  {
    return EqualsUtils.equals (aValue, BigDecimal.ZERO);
  }

  public static boolean isLowerThanZero (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (BigDecimal.ZERO) < 0;
  }

  @Deprecated
  public static boolean isLowerOrEqualZero (@Nonnull final BigDecimal aValue)
  {
    return isLowerOrEqualThanZero (aValue);
  }

  public static boolean isLowerOrEqualThanZero (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (BigDecimal.ZERO) <= 0;
  }

  public static boolean isGreaterThanZero (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (BigDecimal.ZERO) > 0;
  }

  @Deprecated
  public static boolean isGreaterOrEqualZero (@Nonnull final BigDecimal aValue)
  {
    return isGreaterOrEqualThanZero (aValue);
  }

  public static boolean isGreaterOrEqualThanZero (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (BigDecimal.ZERO) >= 0;
  }

  public static boolean isEqualTo100 (@Nonnull final BigDecimal aValue)
  {
    return EqualsUtils.equals (aValue, CGlobal.BIGDEC_100);
  }

  public static boolean isLowerThan100 (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (CGlobal.BIGDEC_100) < 0;
  }

  public static boolean isLowerOrEqualThan100 (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (CGlobal.BIGDEC_100) <= 0;
  }

  public static boolean isGreaterThan100 (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (CGlobal.BIGDEC_100) > 0;
  }

  public static boolean isGreaterOrEqualThan100 (@Nonnull final BigDecimal aValue)
  {
    return aValue.compareTo (CGlobal.BIGDEC_100) >= 0;
  }

  public static boolean isEqualToZero (@Nonnull final BigInteger aValue)
  {
    return EqualsUtils.equals (aValue, BigInteger.ZERO);
  }

  public static boolean isLowerThanZero (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (BigInteger.ZERO) < 0;
  }

  @Deprecated
  public static boolean isLowerOrEqualZero (@Nonnull final BigInteger aValue)
  {
    return isLowerOrEqualThanZero (aValue);
  }

  public static boolean isLowerOrEqualThanZero (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (BigInteger.ZERO) <= 0;
  }

  public static boolean isGreaterThanZero (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (BigInteger.ZERO) > 0;
  }

  @Deprecated
  public static boolean isGreaterOrEqualZero (@Nonnull final BigInteger aValue)
  {
    return isGreaterOrEqualThanZero (aValue);
  }

  public static boolean isGreaterOrEqualThanZero (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (BigInteger.ZERO) >= 0;
  }

  public static boolean isEqualTo100 (@Nonnull final BigInteger aValue)
  {
    return EqualsUtils.equals (aValue, CGlobal.BIGINT_100);
  }

  public static boolean isLowerThan100 (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (CGlobal.BIGINT_100) < 0;
  }

  public static boolean isLowerOrEqualThan100 (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (CGlobal.BIGINT_100) <= 0;
  }

  public static boolean isGreaterThan100 (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (CGlobal.BIGINT_100) > 0;
  }

  public static boolean isGreaterOrEqualThan100 (@Nonnull final BigInteger aValue)
  {
    return aValue.compareTo (CGlobal.BIGINT_100) >= 0;
  }

  /**
   * @param a
   *        a
   * @param b
   *        b
   * @return sqrt(a^2 + b^2) without under/overflow.
   */
  public static double hypot (final double a, final double b)
  {
    double r;
    final double dAbsA = MathHelper.abs (a);
    final double dAbsB = MathHelper.abs (b);
    if (dAbsA > dAbsB)
    {
      r = b / a;
      r = dAbsA * Math.sqrt (1 + r * r);
    }
    else
      if (b != 0)
      {
        r = a / b;
        r = dAbsB * Math.sqrt (1 + r * r);
      }
      else
        r = 0.0;
    return r;
  }

  /**
   * Converts the passed signed integer to an unsigned long
   * 
   * @param a
   * @return The unsigned long
   */
  public static long getUnsignedInt (final int a)
  {
    return a & 0x00000000ffffffffL;
  }
}
