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
package com.phloc.commons.compare;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * A small helper class that provides comparison methods that are
 * <code>null</code> safe. Also provides secure ways to compare double and float
 * values.
 * 
 * @author philip
 */
@Immutable
public final class CompareUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CompareUtils s_aInstance = new CompareUtils ();

  private CompareUtils ()
  {}

  /**
   * Compare the passed items and handle <code>null</code> values correctly. A
   * <code>null</code> value is always smaller than a non-<code>null</code>
   * value.
   * 
   * @param <DATATYPE>
   *        Any comparable object to be used. Both need to be of the same type.
   * @param aObj1
   *        First object to compare. May be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May be <code>null</code>.
   * @return 0 if they are equal (or both <code>null</code>), -1 or +1.
   */
  public static <DATATYPE extends Comparable <? super DATATYPE>> int nullSafeCompare (@Nullable final DATATYPE aObj1,
                                                                                      @Nullable final DATATYPE aObj2)
  {
    return aObj1 == aObj2 ? 0 : aObj1 == null ? -1 : aObj2 == null ? +1 : aObj1.compareTo (aObj2);
  }

  /**
   * Compare the passed items and handle <code>null</code> values correctly. A
   * <code>null</code> value is always smaller than a non-<code>null</code>
   * value.
   * 
   * @param <DATATYPE>
   *        Any object to be used. Both need to be of the same type.
   * @param aObj1
   *        First object to compare. May be <code>null</code>.
   * @param aObj2
   *        Second object to compare. May be <code>null</code>.
   * @param aComp
   *        The comparator to be used if both parameters are not
   *        <code>null</code>. The comparator itself may not be
   *        <code>null</code>.
   * @return 0 if they are equal (or both <code>null</code>), -1 or +1.
   */
  public static <DATATYPE> int nullSafeCompare (@Nullable final DATATYPE aObj1,
                                                @Nullable final DATATYPE aObj2,
                                                @Nonnull final Comparator <DATATYPE> aComp)
  {
    if (aObj1 == aObj2)
      return 0;
    return aObj1 == null ? -1 : aObj2 == null ? +1 : aComp.compare (aObj1, aObj2);
  }

  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Locale aSortLocale)
  {
    return nullSafeCompare (sStr1, sStr2, CollatorUtils.getCollatorSpaceBeforeDot (aSortLocale));
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings ({ "ES_COMPARING_PARAMETER_STRING_WITH_EQ" })
  public static int nullSafeCompare (@Nullable final String sStr1,
                                     @Nullable final String sStr2,
                                     @Nonnull final Collator aCollator)
  {
    if (sStr1 == sStr2)
      return 0;
    return sStr1 == null ? -1 : sStr2 == null ? +1 : aCollator.compare (sStr1, sStr2);
  }
}
