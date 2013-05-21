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
package com.phloc.commons.locale.country;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;

/**
 * This is a global cache for country objects to avoid too many object flowing
 * around.<br>
 * This cache is application independent.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class CountryCache
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CountryCache.class);

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  /** Contains all known countries (as ISO 3166 2-letter codes). */
  private static final Set <String> s_aCountries = new HashSet <String> ();

  static
  {
    _initialFillCache ();
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CountryCache s_aInstance = new CountryCache ();

  private CountryCache ()
  {}

  private static void _initialFillCache ()
  {
    for (final Locale aLocale : LocaleCache.getAllLocales ())
    {
      final String sCountry = aLocale.getCountry ();
      if (StringHelper.hasText (sCountry))
        addCountry (sCountry);
    }
  }

  @Nonnull
  private static String _getUnifiedCountry (@Nonnull final String sCountry)
  {
    // We can US locale, because the ISO 3166 codes don't contain non-ASCII
    // chars
    return sCountry.toUpperCase (Locale.US);
  }

  @Nonnull
  static EChange addCountry (@Nonnull final String sCountry)
  {
    if (sCountry == null)
      throw new NullPointerException ("country");
    if (!sCountry.equals (_getUnifiedCountry (sCountry)))
      throw new IllegalArgumentException ("illegal casing of '" + sCountry + "'");

    s_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (s_aCountries.add (sCountry));
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static Locale getCountry (@Nullable final Locale aCountry)
  {
    return aCountry == null ? null : getCountry (aCountry.getCountry ());
  }

  @Nullable
  public static Locale getCountry (@Nullable final String sCountry)
  {
    if (StringHelper.hasNoText (sCountry))
      return null;

    // Was something like "_AT" (e.g. the result of getCountry (...).toString
    // ()) passed in? -> indirect recursion
    if (sCountry.indexOf (CGlobal.LOCALE_SEPARATOR) >= 0)
      return getCountry (LocaleCache.getLocale (sCountry));

    final String sRealCountry = _getUnifiedCountry (sCountry);
    if (!containsCountry (sRealCountry))
      s_aLogger.warn ("Trying to retrieve unsupported country " + sRealCountry);
    return LocaleCache.getLocale ("", sRealCountry, "");
  }

  /**
   * @return a set with all contained countries
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Set <String> getAllCountries ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (s_aCountries);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Check if the passed country is known.
   *
   * @param aCountry
   *        The country to check. May be <code>null</code>.
   * @return <code>true</code> if the passed country is contained,
   *         <code>false</code> otherwise.
   */
  public static boolean containsCountry (@Nullable final Locale aCountry)
  {
    return aCountry != null && containsCountry (aCountry.getCountry ());
  }

  /**
   * Check if the passed country is known.
   *
   * @param sCountry
   *        The country to check. May be <code>null</code>.
   * @return <code>true</code> if the passed country is contained,
   *         <code>false</code> otherwise.
   */
  public static boolean containsCountry (@Nullable final String sCountry)
  {
    if (sCountry == null)
      return false;

    final String sUnifiedCountry = _getUnifiedCountry (sCountry);
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aCountries.contains (sUnifiedCountry);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Reset the cache to the initial state.
   */
  public static void resetCache ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aCountries.clear ();
      _initialFillCache ();
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Cache was reset: " + CountryCache.class.getName ());
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
