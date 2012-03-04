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

import java.math.BigInteger;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Small class for calculating factorials.
 * 
 * @author philip
 */
@Immutable
public final class FactorialHelper
{
  private static final long [] PREDEFINED_FACTORIALS_LONG = { 1,
                                                             1,
                                                             2,
                                                             6,
                                                             24,
                                                             120,
                                                             720,
                                                             5040,
                                                             40320,
                                                             362880,
                                                             3628800,
                                                             39916800,
                                                             479001600,
                                                             6227020800L,
                                                             87178291200L,
                                                             1307674368000L,
                                                             20922789888000L,
                                                             355687428096000L,
                                                             6402373705728000L,
                                                             121645100408832000L,
                                                             2432902008176640000L };
  public static final int PREDEFINED_MIN_INDEX = 0;
  public static final int PREDEFINED_MAX_INDEX = PREDEFINED_FACTORIALS_LONG.length - 1;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final FactorialHelper s_aInstance = new FactorialHelper ();

  private FactorialHelper ()
  {}

  /**
   * Calculate n! whereas n must be in the range of
   * {@value #PREDEFINED_MIN_INDEX} to {@value #PREDEFINED_MAX_INDEX}.
   * 
   * @param n
   *        Input value
   * @return The factorial value.
   */
  @Nonnegative
  public static long getSmallFactorial (@Nonnegative final int n)
  {
    if (n < PREDEFINED_MIN_INDEX || n > PREDEFINED_MAX_INDEX)
      throw new IllegalArgumentException ("Passed index " + n + " is out of bounds");
    return PREDEFINED_FACTORIALS_LONG[n];
  }

  /**
   * Split algorithm for factorials.<br>
   * Based on http://www.luschny.de/math/factorial/java/FactorialSplit.java.html
   * 
   * @author philip
   */
  private static final class FactorialSplit
  {
    private long m_nCurrentN;

    private BigInteger _getProduct (final int n)
    {
      final int m = n / 2;
      if (m == 0)
        return new BigInteger (Long.toString (m_nCurrentN += 2));
      if (n == 2)
        return new BigInteger (Long.toString ((m_nCurrentN += 2) * (m_nCurrentN += 2)));
      return _getProduct (n - m).multiply (_getProduct (m));
    }

    @Nonnull
    public BigInteger getFactorial (final int n)
    {
      if (n < 0)
        throw new IllegalArgumentException ("n >= 0 required, but was " + n);
      if (n < 2)
        return BigInteger.ONE;
      BigInteger p = BigInteger.ONE;
      BigInteger r = BigInteger.ONE;
      m_nCurrentN = 1;
      int h = 0, shift = 0, high = 1;
      int log2n = true ? 31 - Integer.numberOfLeadingZeros (n) : (int) Math.floor (Math.log (n) / Math.log (2));
      while (h != n)
      {
        shift += h;
        h = n >> log2n--;
        int len = high;
        high = (h - 1) | 1;
        len = (high - len) / 2;
        if (len > 0)
        {
          p = p.multiply (_getProduct (len));
          r = r.multiply (p);
        }
      }
      return r.shiftLeft (shift);
    }
  }

  @Nonnull
  public static BigInteger getAnyFactorialLinear (@Nonnegative final int n)
  {
    return new FactorialSplit ().getFactorial (n);
  }
}
