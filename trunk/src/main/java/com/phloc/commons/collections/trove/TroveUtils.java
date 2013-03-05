package com.phloc.commons.collections.trove;

import gnu.trove.impl.Constants;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

@Immutable
public final class TroveUtils
{
  @PresentForCodeCoverage
  private static final TroveUtils s_aInstance = new TroveUtils ();

  private TroveUtils ()
  {}

  public static boolean isContained (final byte n)
  {
    return n != Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final byte n)
  {
    return n == Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
  }

  public static boolean isContained (final char c)
  {
    return c != Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final char c)
  {
    return c == Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
  }

  public static boolean isContained (final double d)
  {
    return d != Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final double d)
  {
    return d == Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
  }

  public static boolean isContained (final float f)
  {
    return f != Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final float f)
  {
    return f == Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
  }

  public static boolean isContained (final int n)
  {
    return n != Constants.DEFAULT_INT_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final int n)
  {
    return n == Constants.DEFAULT_INT_NO_ENTRY_VALUE;
  }

  public static boolean isContained (final long n)
  {
    return n != Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final long n)
  {
    return n == Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
  }

  public static boolean isContained (final short n)
  {
    return n != Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
  }

  public static boolean isNotContained (final short n)
  {
    return n == Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
  }
}
