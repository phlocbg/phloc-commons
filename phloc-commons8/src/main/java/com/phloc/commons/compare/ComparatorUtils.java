/**
 * Copyright (C) 2006-2014 phloc systems
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

import java.util.Comparator;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Helper class to easily create commonly used {@link Comparator} objects.
 *
 * @author Philip Helger
 */
@Immutable
public final class ComparatorUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ComparatorUtils s_aInstance = new ComparatorUtils ();

  private ComparatorUtils ()
  {}

  @Nonnull
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> Comparator <? super Map.Entry <KEYTYPE, VALUETYPE>> getComparatorMapEntryKey ()
  {
    return (p1, p2) -> CompareUtils.nullSafeCompare (p1.getKey (), p2.getKey ());
  }

  @Nonnull
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> Comparator <? super Map.Entry <KEYTYPE, VALUETYPE>> getComparatorMapEntryKey (@Nonnull final ESortOrder eSortOrder)
  {
    return new AbstractComparator <Map.Entry <KEYTYPE, VALUETYPE>> (eSortOrder)
    {
      @Override
      protected int mainCompare (final Map.Entry <KEYTYPE, VALUETYPE> aEntry1,
                                 final Map.Entry <KEYTYPE, VALUETYPE> aEntry2)
      {
        return CompareUtils.nullSafeCompare (aEntry1.getKey (), aEntry2.getKey ());
      }
    };
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> Comparator <? super Map.Entry <KEYTYPE, VALUETYPE>> getComparatorMapEntryKey (final Comparator <? super KEYTYPE> aKeyComparator)
  {
    return (p1, p2) -> aKeyComparator.compare (p1.getKey (), p2.getKey ());
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE extends Comparable <? super VALUETYPE>> Comparator <? super Map.Entry <KEYTYPE, VALUETYPE>> getComparatorMapEntryValue ()
  {
    return (p1, p2) -> CompareUtils.nullSafeCompare (p1.getValue (), p2.getValue ());
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE extends Comparable <? super VALUETYPE>> Comparator <? super Map.Entry <KEYTYPE, VALUETYPE>> getComparatorMapEntryValue (@Nonnull final ESortOrder eSortOrder)
  {
    return new AbstractComparator <Map.Entry <KEYTYPE, VALUETYPE>> (eSortOrder)
    {
      @Override
      protected int mainCompare (final Map.Entry <KEYTYPE, VALUETYPE> aEntry1,
                                 final Map.Entry <KEYTYPE, VALUETYPE> aEntry2)
      {
        return CompareUtils.nullSafeCompare (aEntry1.getValue (), aEntry2.getValue ());
      }
    };
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> Comparator <? super Map.Entry <KEYTYPE, VALUETYPE>> getComparatorMapEntryValue (@Nonnull final Comparator <? super VALUETYPE> aValueComparator)
  {
    return (p1, p2) -> aValueComparator.compare (p1.getValue (), p2.getValue ());
  }
}
