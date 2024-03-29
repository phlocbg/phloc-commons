/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.locale;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
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
import com.phloc.commons.string.StringHelper;

/**
 * This is a global cache for Locale objects to avoid too many object flowing
 * around.<br>
 * This cache is application independent.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class LocaleCache
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LocaleCache.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  /** maps a string to a locale. */
  private static final Map <String, Locale> s_aLocalesByString = new HashMap <String, Locale> ();
  private static final Map <String, Locale> s_aLocalesByLanguageTag = new HashMap <String, Locale> ();

  static
  {
    _initialFillCache ();
  }

  private static void _initialAdd (@Nonnull final Locale aLocale)
  {
    s_aLocalesByString.put (aLocale.toString (), aLocale);
    s_aLocalesByLanguageTag.put (_buildLanguageTagString (aLocale.toLanguageTag ()), aLocale);
  }

  private static void _initialFillCache ()
  {
    // add pseudo locales
    _initialAdd (CGlobal.LOCALE_ALL);
    _initialAdd (CGlobal.LOCALE_INDEPENDENT);

    // add all predefined languages
    for (final Locale aLocale : Locale.getAvailableLocales ())
      _initialAdd (aLocale);

    // http://forums.sun.com/thread.jspa?threadID=525482&tstart=1411
    for (final String sCountry : Locale.getISOCountries ())
      _initialAdd (new Locale ("", sCountry)); //$NON-NLS-1$
    for (final String sLanguage : Locale.getISOLanguages ())
      _initialAdd (new Locale (sLanguage, "")); //$NON-NLS-1$
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final LocaleCache s_aInstance = new LocaleCache ();

  private LocaleCache ()
  {}

  /**
   * Get the {@link Locale} object matching the given language.
   * 
   * @param sLanguage
   *        The language to use. Either using the String representation of the
   *        Locale object or the language tag syntax. May be <code>null</code>
   *        or empty.
   * @return <code>null</code> if the passed language string is
   *         <code>null</code> or empty
   */
  @Nullable
  public static Locale getLocale (@Nullable final String sLanguage)
  {
    if (sLanguage != null && sLanguage.length () > 2)
    {
      if (sLanguage.contains (CGlobal.LANGUAGE_TAG_SEPARATOR_STR) && !sLanguage.contains (CGlobal.LOCALE_SEPARATOR_STR))
      {
        return getLocaleByLanguageTag (sLanguage);
      }
      // parse
      final String [] aParts = StringHelper.getExplodedArray (CGlobal.LOCALE_SEPARATOR, sLanguage, 3);
      if (aParts.length == 3)
        return getLocale (aParts[0], aParts[1], aParts[2]);
      if (aParts.length == 2)
        return getLocale (aParts[0], aParts[1], ""); //$NON-NLS-1$
      // else fall through
    }
    return getLocale (sLanguage, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static Locale getLocaleByLanguageTag (@Nullable final String sLanguage)
  {
    if (StringHelper.hasNoText (sLanguage))
      return null;

    final String sLanguageTag = _buildLanguageTagString (sLanguage);

    // try to resolve locale
    Locale aLocale;
    s_aRWLock.readLock ().lock ();
    try
    {
      aLocale = s_aLocalesByLanguageTag.get (sLanguageTag);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    if (aLocale == null)
    {
      s_aRWLock.writeLock ().lock ();
      try
      {
        // Try fetching again in writeLock
        aLocale = s_aLocalesByLanguageTag.get (sLanguageTag);
        if (aLocale == null)
        {
          // not yet in cache, create a new one
          // -> may lead to illegal locales, but simpler than the error handling
          // for all the possible illegal values
          aLocale = Locale.forLanguageTag (sLanguageTag);
          s_aLocalesByString.put (aLocale.toString (), aLocale);
          s_aLocalesByLanguageTag.put (sLanguageTag, aLocale);
        }
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return aLocale;
  }

  /**
   * Get the {@link Locale} object matching the given language and country.
   * 
   * @param sLanguage
   *        The language to use. May be <code>null</code> or empty.
   * @param sCountry
   *        The country to use. May be <code>null</code>.
   * @return <code>null</code> if the passed language string is
   *         <code>null</code> or empty
   */
  @Nullable
  public static Locale getLocale (@Nullable final String sLanguage, @Nullable final String sCountry)
  {
    return getLocale (sLanguage, sCountry, ""); //$NON-NLS-1$
  }

  /**
   * Build the locale key internally used. Note: this is not the same string as
   * returned by {@link Locale#toString()}!!
   * 
   * @param sLanguage
   *        Language to use
   * @param sCountry
   *        Country to use
   * @param sVariant
   *        Variant to use
   * @return String
   */
  @Nonnull
  private static String _buildLocaleString (@Nonnull final String sLanguage,
                                            @Nonnull final String sCountry,
                                            @Nonnull final String sVariant)
  {
    final StringBuilder aLocaleSB = new StringBuilder ();
    if (sLanguage.length () > 0)
      aLocaleSB.append (sLanguage);
    if (sCountry.length () > 0)
      aLocaleSB.append (CGlobal.LOCALE_SEPARATOR).append (sCountry);
    if (sVariant.length () > 0)
      aLocaleSB.append (CGlobal.LOCALE_SEPARATOR).append (sVariant);
    return aLocaleSB.toString ();
  }

  private static String _buildLanguageTagString (@Nonnull final String sLanguageTag)
  {
    return sLanguageTag.toLowerCase ();
  }

  /**
   * Get the {@link Locale} object matching the given locale string
   * 
   * @param sLanguage
   *        The language to use. May be <code>null</code> or empty.
   * @param sCountry
   *        Optional country to use. May be <code>null</code>.
   * @param sVariant
   *        Optional variant. May be <code>null</code>.
   * @return <code>null</code> if all the passed parameters are
   *         <code>null</code> or empty
   */
  @Nullable
  public static Locale getLocale (@Nullable final String sLanguage,
                                  @Nullable final String sCountry,
                                  @Nullable final String sVariant)
  {
    final String sRealLanguage = StringHelper.getNotNull (LocaleUtils.getValidLanguageCode (sLanguage));
    final String sRealCountry = StringHelper.getNotNull (LocaleUtils.getValidCountryCode (sCountry));
    final String sRealVariant = StringHelper.getNotNull (sVariant);
    final String sLocaleKey = _buildLocaleString (sRealLanguage, sRealCountry, sRealVariant);
    if (sLocaleKey.length () == 0)
      return null;
    // try to resolve locale
    Locale aLocale;
    s_aRWLock.readLock ().lock ();
    try
    {
      aLocale = s_aLocalesByString.get (sLocaleKey);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }

    if (aLocale == null)
    {
      s_aRWLock.writeLock ().lock ();
      try
      {
        // Try fetching again in writeLock
        aLocale = s_aLocalesByString.get (sLocaleKey);
        if (aLocale == null)
        {
          // not yet in cache, create a new one
          // -> may lead to illegal locales, but simpler than the error handling
          // for all the possible illegal values
          aLocale = new Locale (sRealLanguage, sRealCountry, sRealVariant);
          s_aLocalesByString.put (sLocaleKey, aLocale);
          s_aLocalesByLanguageTag.put (_buildLanguageTagString (aLocale.toLanguageTag ()), aLocale);
        }
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return aLocale;
  }

  /**
   * Get all contained locales except the locales "all" and "independent"
   * 
   * @return a set with all contained locales, except "all" and "independent"
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Set <Locale> getAllLocales ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      final Set <Locale> ret = ContainerHelper.newSet (s_aLocalesByString.values ());
      ret.remove (CGlobal.LOCALE_ALL);
      ret.remove (CGlobal.LOCALE_INDEPENDENT);
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get all contained locales that consist only of a non-empty language.
   * 
   * @return a set with all contained languages, except "all" and "independent"
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Set <Locale> getAllLanguages ()
  {
    final Set <Locale> ret = new HashSet <Locale> ();
    for (final Locale aLocale : getAllLocales ())
    {
      final String sLanguage = aLocale.getLanguage ();
      if (StringHelper.hasText (sLanguage))
        ret.add (getLocale (sLanguage, null, null));
    }
    return ret;
  }

  /**
   * Check if the passed language is in the cache.
   * 
   * @param sLanguage
   *        The language to check.
   * @return <code>true</code> if it is in the cache, <code>false</code>
   *         otherwise.
   */
  public static boolean containsLocale (@Nullable final String sLanguage)
  {
    if (sLanguage != null && sLanguage.length () > 2)
    {
      // parse
      final String [] aParts = StringHelper.getExplodedArray (CGlobal.LOCALE_SEPARATOR, sLanguage, 3);
      if (aParts.length == 3)
        return containsLocale (aParts[0], aParts[1], aParts[2]);
      if (aParts.length == 2)
        return containsLocale (aParts[0], aParts[1], ""); //$NON-NLS-1$
      // else fall through
    }
    return containsLocale (sLanguage, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * Check if the passed language is in the cache.
   * 
   * @param sLanguage
   *        The language to check.
   * @param sCountry
   *        The country to check.
   * @return <code>true</code> if it is in the cache, <code>false</code>
   *         otherwise.
   */
  public static boolean containsLocale (@Nullable final String sLanguage, @Nullable final String sCountry)
  {
    return containsLocale (sLanguage, sCountry, ""); //$NON-NLS-1$
  }

  @Nonnull
  private static String _createLocaleKey (@Nullable final String sLanguage,
                                          @Nullable final String sCountry,
                                          @Nullable final String sVariant)
  {
    final String sRealLanguage = StringHelper.getNotNull (LocaleUtils.getValidLanguageCode (sLanguage));
    final String sRealCountry = StringHelper.getNotNull (LocaleUtils.getValidCountryCode (sCountry));
    final String sRealVariant = StringHelper.getNotNull (sVariant);
    return _buildLocaleString (sRealLanguage, sRealCountry, sRealVariant);
  }

  /**
   * Check if the passed language is in the cache.
   * 
   * @param sLanguage
   *        The language to check.
   * @param sCountry
   *        The country to check.
   * @param sVariant
   *        The variant to check.
   * @return <code>true</code> if it is in the cache, <code>false</code>
   *         otherwise.
   */
  public static boolean containsLocale (@Nullable final String sLanguage,
                                        @Nullable final String sCountry,
                                        @Nullable final String sVariant)
  {
    final String sLocaleKey = _createLocaleKey (sLanguage, sCountry, sVariant);
    if (sLocaleKey.length () == 0)
      return false;
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aLocalesByString.containsKey (sLocaleKey);
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
      s_aLocalesByString.clear ();
      _initialFillCache ();
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Cache was reset: " + LocaleCache.class.getName ()); //$NON-NLS-1$
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
