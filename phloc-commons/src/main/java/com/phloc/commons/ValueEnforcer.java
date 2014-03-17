package com.phloc.commons;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ValueEnforcer
{
  private ValueEnforcer ()
  {}

  @Nonnull
  public static <T> T notNull (final T aValue, final String sName)
  {
    if (aValue == null)
      throw new NullPointerException ("The value of '" + sName + "' may not be null!");
    return aValue;
  }

  @Nonnull
  public static <T extends CharSequence> T notEmpty (final T aValue, final String sName)
  {
    notNull (aValue, sName);
    if (aValue.length () == 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' may not be empty!");
    return aValue;
  }

  public static short isGE0 (final short nValue, final String sName)
  {
    if (nValue < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + nValue);
    return nValue;
  }

  public static int isGE0 (final int nValue, final String sName)
  {
    if (nValue < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + nValue);
    return nValue;
  }

  public static long isGE0 (final long nValue, final String sName)
  {
    if (nValue < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + nValue);
    return nValue;
  }

  public static double isGE0 (final double dValue, final String sName)
  {
    if (dValue < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + dValue);
    return dValue;
  }

  public static float isGE0 (final float fValue, final String sName)
  {
    if (fValue < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + fValue);
    return fValue;
  }

  @Nonnull
  public static BigDecimal isGE0 (@Nonnull final BigDecimal aValue, final String sName)
  {
    notNull (aValue, sName);
    if (aValue.compareTo (BigDecimal.ZERO) < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + aValue);
    return aValue;
  }

  @Nonnull
  public static BigInteger isGE0 (@Nonnull final BigInteger aValue, final String sName)
  {
    notNull (aValue, sName);
    if (aValue.compareTo (BigInteger.ZERO) < 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be >= 0! The current value is: " + aValue);
    return aValue;
  }

  public static short isGT0 (final short nValue, final String sName)
  {
    if (nValue <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + nValue);
    return nValue;
  }

  public static int isGT0 (final int nValue, final String sName)
  {
    if (nValue <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + nValue);
    return nValue;
  }

  public static long isGT0 (final long nValue, final String sName)
  {
    if (nValue <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + nValue);
    return nValue;
  }

  public static double isGT0 (final double dValue, final String sName)
  {
    if (dValue <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + dValue);
    return dValue;
  }

  public static float isGT0 (final float fValue, final String sName)
  {
    if (fValue <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + fValue);
    return fValue;
  }

  @Nonnull
  public static BigDecimal isGT0 (@Nonnull final BigDecimal aValue, final String sName)
  {
    notNull (aValue, sName);
    if (aValue.compareTo (BigDecimal.ZERO) <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + aValue);
    return aValue;
  }

  @Nonnull
  public static BigInteger isGT0 (@Nonnull final BigInteger aValue, final String sName)
  {
    notNull (aValue, sName);
    if (aValue.compareTo (BigInteger.ZERO) <= 0)
      throw new IllegalArgumentException ("The value of '" + sName + "' must be > 0! The current value is: " + aValue);
    return aValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param nValue
   *        Value
   * @param sName
   *        Name
   * @param nLowerBoundInclusive
   *        Lower bound
   * @param nUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static short isBetweenInclusive (final short nValue,
                                          final String sName,
                                          final short nLowerBoundInclusive,
                                          final short nUpperBoundInclusive)
  {
    if (nValue < nLowerBoundInclusive || nValue > nUpperBoundInclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          nLowerBoundInclusive +
                                          " and <= " +
                                          nUpperBoundInclusive +
                                          "! The current value is: " +
                                          nValue);
    return nValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param nValue
   *        Value
   * @param sName
   *        Name
   * @param nLowerBoundInclusive
   *        Lower bound
   * @param nUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static int isBetweenInclusive (final int nValue,
                                        final String sName,
                                        final int nLowerBoundInclusive,
                                        final int nUpperBoundInclusive)
  {
    if (nValue < nLowerBoundInclusive || nValue > nUpperBoundInclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          nLowerBoundInclusive +
                                          " and <= " +
                                          nUpperBoundInclusive +
                                          "! The current value is: " +
                                          nValue);
    return nValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param nValue
   *        Value
   * @param sName
   *        Name
   * @param nLowerBoundInclusive
   *        Lower bound
   * @param nUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static long isBetweenInclusive (final long nValue,
                                         final String sName,
                                         final long nLowerBoundInclusive,
                                         final long nUpperBoundInclusive)
  {
    if (nValue < nLowerBoundInclusive || nValue > nUpperBoundInclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          nLowerBoundInclusive +
                                          " and <= " +
                                          nUpperBoundInclusive +
                                          "! The current value is: " +
                                          nValue);
    return nValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param fValue
   *        Value
   * @param sName
   *        Name
   * @param fLowerBoundInclusive
   *        Lower bound
   * @param fUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static float isBetweenInclusive (final float fValue,
                                          final String sName,
                                          final float fLowerBoundInclusive,
                                          final float fUpperBoundInclusive)
  {
    if (fValue < fLowerBoundInclusive || fValue > fUpperBoundInclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          fLowerBoundInclusive +
                                          " and <= " +
                                          fUpperBoundInclusive +
                                          "! The current value is: " +
                                          fValue);
    return fValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param dValue
   *        Value
   * @param sName
   *        Name
   * @param dLowerBoundInclusive
   *        Lower bound
   * @param dUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static double isBetweenInclusive (final double dValue,
                                           final String sName,
                                           final double dLowerBoundInclusive,
                                           final double dUpperBoundInclusive)
  {
    if (dValue < dLowerBoundInclusive || dValue > dUpperBoundInclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          dLowerBoundInclusive +
                                          " and <= " +
                                          dUpperBoundInclusive +
                                          "! The current value is: " +
                                          dValue);
    return dValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param aValue
   *        Value
   * @param sName
   *        Name
   * @param aLowerBoundInclusive
   *        Lower bound
   * @param aUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static BigDecimal isBetweenInclusive (@Nonnull final BigDecimal aValue,
                                               final String sName,
                                               @Nonnull final BigDecimal aLowerBoundInclusive,
                                               @Nonnull final BigDecimal aUpperBoundInclusive)
  {
    notNull (aValue, sName);
    notNull (aLowerBoundInclusive, "LowerBoundInclusive");
    notNull (aUpperBoundInclusive, "UpperBoundInclusive");
    if (aValue.compareTo (aLowerBoundInclusive) < 0 || aValue.compareTo (aUpperBoundInclusive) > 0)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          aLowerBoundInclusive +
                                          " and <= " +
                                          aUpperBoundInclusive +
                                          "! The current value is: " +
                                          aValue);
    return aValue;
  }

  /**
   * Check if
   * <code>nValue &ge; nLowerBoundInclusive && nValue &le; nUpperBoundInclusive</code>
   * 
   * @param aValue
   *        Value
   * @param sName
   *        Name
   * @param aLowerBoundInclusive
   *        Lower bound
   * @param aUpperBoundInclusive
   *        Upper bound
   * @return The value
   */
  public static BigInteger isBetweenInclusive (@Nonnull final BigInteger aValue,
                                               final String sName,
                                               @Nonnull final BigInteger aLowerBoundInclusive,
                                               @Nonnull final BigInteger aUpperBoundInclusive)
  {
    notNull (aValue, sName);
    notNull (aLowerBoundInclusive, "LowerBoundInclusive");
    notNull (aUpperBoundInclusive, "UpperBoundInclusive");
    if (aValue.compareTo (aLowerBoundInclusive) < 0 || aValue.compareTo (aUpperBoundInclusive) > 0)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be >= " +
                                          aLowerBoundInclusive +
                                          " and <= " +
                                          aUpperBoundInclusive +
                                          "! The current value is: " +
                                          aValue);
    return aValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param nValue
   *        Value
   * @param sName
   *        Name
   * @param nLowerBoundExclusive
   *        Lower bound
   * @param nUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static short isBetweenExclusive (final short nValue,
                                          final String sName,
                                          final short nLowerBoundExclusive,
                                          final short nUpperBoundExclusive)
  {
    if (nValue <= nLowerBoundExclusive || nValue >= nUpperBoundExclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          nLowerBoundExclusive +
                                          " and < " +
                                          nUpperBoundExclusive +
                                          "! The current value is: " +
                                          nValue);
    return nValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param nValue
   *        Value
   * @param sName
   *        Name
   * @param nLowerBoundExclusive
   *        Lower bound
   * @param nUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static int isBetweenExclusive (final int nValue,
                                        final String sName,
                                        final int nLowerBoundExclusive,
                                        final int nUpperBoundExclusive)
  {
    if (nValue <= nLowerBoundExclusive || nValue >= nUpperBoundExclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          nLowerBoundExclusive +
                                          " and < " +
                                          nUpperBoundExclusive +
                                          "! The current value is: " +
                                          nValue);
    return nValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param nValue
   *        Value
   * @param sName
   *        Name
   * @param nLowerBoundExclusive
   *        Lower bound
   * @param nUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static long isBetweenExclusive (final long nValue,
                                         final String sName,
                                         final long nLowerBoundExclusive,
                                         final long nUpperBoundExclusive)
  {
    if (nValue <= nLowerBoundExclusive || nValue >= nUpperBoundExclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          nLowerBoundExclusive +
                                          " and < " +
                                          nUpperBoundExclusive +
                                          "! The current value is: " +
                                          nValue);
    return nValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param fValue
   *        Value
   * @param sName
   *        Name
   * @param fLowerBoundExclusive
   *        Lower bound
   * @param fUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static float isBetweenExclusive (final float fValue,
                                          final String sName,
                                          final float fLowerBoundExclusive,
                                          final float fUpperBoundExclusive)
  {
    if (fValue <= fLowerBoundExclusive || fValue >= fUpperBoundExclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          fLowerBoundExclusive +
                                          " and < " +
                                          fUpperBoundExclusive +
                                          "! The current value is: " +
                                          fValue);
    return fValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param dValue
   *        Value
   * @param sName
   *        Name
   * @param dLowerBoundExclusive
   *        Lower bound
   * @param dUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static double isBetweenExclusive (final double dValue,
                                           final String sName,
                                           final double dLowerBoundExclusive,
                                           final double dUpperBoundExclusive)
  {
    if (dValue <= dLowerBoundExclusive || dValue >= dUpperBoundExclusive)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          dLowerBoundExclusive +
                                          " and < " +
                                          dUpperBoundExclusive +
                                          "! The current value is: " +
                                          dValue);
    return dValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param aValue
   *        Value
   * @param sName
   *        Name
   * @param aLowerBoundExclusive
   *        Lower bound
   * @param aUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static BigDecimal isBetweenExclusive (@Nonnull final BigDecimal aValue,
                                               final String sName,
                                               @Nonnull final BigDecimal aLowerBoundExclusive,
                                               @Nonnull final BigDecimal aUpperBoundExclusive)
  {
    notNull (aValue, sName);
    notNull (aLowerBoundExclusive, "LowerBoundInclusive");
    notNull (aUpperBoundExclusive, "UpperBoundInclusive");
    if (aValue.compareTo (aLowerBoundExclusive) <= 0 || aValue.compareTo (aUpperBoundExclusive) >= 0)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          aLowerBoundExclusive +
                                          " and < " +
                                          aUpperBoundExclusive +
                                          "! The current value is: " +
                                          aValue);
    return aValue;
  }

  /**
   * Check if
   * <code>nValue &gt; nLowerBoundInclusive && nValue &lt; nUpperBoundInclusive</code>
   * 
   * @param aValue
   *        Value
   * @param sName
   *        Name
   * @param aLowerBoundExclusive
   *        Lower bound
   * @param aUpperBoundExclusive
   *        Upper bound
   * @return The value
   */
  public static BigInteger isBetweenExclusive (@Nonnull final BigInteger aValue,
                                               final String sName,
                                               @Nonnull final BigInteger aLowerBoundExclusive,
                                               @Nonnull final BigInteger aUpperBoundExclusive)
  {
    notNull (aValue, sName);
    notNull (aLowerBoundExclusive, "LowerBoundInclusive");
    notNull (aUpperBoundExclusive, "UpperBoundInclusive");
    if (aValue.compareTo (aLowerBoundExclusive) <= 0 || aValue.compareTo (aUpperBoundExclusive) >= 0)
      throw new IllegalArgumentException ("The value of '" +
                                          sName +
                                          "' must be > " +
                                          aLowerBoundExclusive +
                                          " and < " +
                                          aUpperBoundExclusive +
                                          "! The current value is: " +
                                          aValue);
    return aValue;
  }
}
