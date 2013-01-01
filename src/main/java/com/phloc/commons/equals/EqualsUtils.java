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
package com.phloc.commons.equals;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A small helper class that provides helper methods for easy
 * <code>equals</code> method generation
 *
 * @author philip
 */
@Immutable
public final class EqualsUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final EqualsUtils s_aInstance = new EqualsUtils ();

  private EqualsUtils ()
  {}

  public static boolean equals (final boolean aObj1, final boolean aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final byte aObj1, final byte aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final char aObj1, final char aObj2)
  {
    return aObj1 == aObj2;
  }

  /**
   * Check if two double values are equal. This is necessary, because in some
   * cases, the "==" operator returns wrong results.
   *
   * @param aObj1
   *        First double
   * @param aObj2
   *        Second double
   * @return <code>true</code> if they are equal.
   */
  public static boolean equals (final double aObj1, final double aObj2)
  {
    // Special overload for "double" required!
    return (aObj1 == aObj2) || (Double.doubleToLongBits (aObj1) == Double.doubleToLongBits (aObj2));
  }

  /**
   * Check if two float values are equal. This is necessary, because in some
   * cases, the "==" operator returns wrong results.
   *
   * @param aObj1
   *        First float
   * @param aObj2
   *        Second float
   * @return <code>true</code> if they are equal.
   */
  public static boolean equals (final float aObj1, final float aObj2)
  {
    // Special overload for "float" required!
    return (aObj1 == aObj2) || (Float.floatToIntBits (aObj1) == Float.floatToIntBits (aObj2));
  }

  public static boolean equals (final int aObj1, final int aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final long aObj1, final long aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (final short aObj1, final short aObj2)
  {
    return aObj1 == aObj2;
  }

  public static boolean equals (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    return EqualsImplementationRegistry.areEqual (aObj1, aObj2);
  }

  @SuppressFBWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static boolean nullSafeEqualsIgnoreCase (@Nullable final String sObj1, @Nullable final String sObj2)
  {
    return sObj1 == null ? sObj2 == null : sObj1.equalsIgnoreCase (sObj2);
  }
}
