/**
 * Copyright (C) 2006-2011 phloc systems
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.cache.AbstractNotifyingCache;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.system.SystemHelper;

/**
 * Misc locale utility methods.
 * 
 * @author philip
 */
@Immutable
public final class LocaleUtils
{
  /**
   * Small cache for the resolved locales.
   * 
   * @author philip
   */
  private static final class LocaleListCache extends AbstractNotifyingCache <Locale, List <Locale>>
  {
    public LocaleListCache ()
    {
      super (LocaleListCache.class.getName ());
    }

    @Override
    @Nonnull
    protected List <Locale> getValueToCache (@Nonnull final Locale aBaseLocale)
    {
      // List has a maximum of 3 entries
      final List <Locale> ret = new ArrayList <Locale> (3);
      final String sLanguage = aBaseLocale.getLanguage ();
      if (sLanguage.length () > 0)
      {
        ret.add (0, LocaleCache.get (sLanguage));
        final String sCountry = aBaseLocale.getCountry ();
        if (sCountry.length () > 0)
        {
          ret.add (0, LocaleCache.get (sLanguage, sCountry));
          final String sVariant = aBaseLocale.getVariant ();
          if (sVariant.length () > 0)
            ret.add (0, LocaleCache.get (sLanguage, sCountry, sVariant));
        }
      }
      return ret;
    }
  }

  private static final LocaleListCache s_aLocaleListResolver = new LocaleListCache ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final LocaleUtils s_aInstance = new LocaleUtils ();

  private LocaleUtils ()
  {}

  /**
   * Get the display name of the passed language in the currently selected UI
   * language.
   * 
   * @param aLocale
   *        The locale from which the display name is required. May be
   *        <code>null</code>.
   * @param aContentLocale
   *        The locale in which the name of the locale is required. If aLocale
   *        is "de" and display locale is "de" the result would be "Deutsch"
   *        whereas if display locale is "en" the result would be "German".
   * @return the display name of the language or a fixed text if the passed
   *         Locale is <code>null</code>, "all" or "independent"
   * @see CGlobal#LOCALE_ALL
   * @see CGlobal#LOCALE_INDEPENDENT
   */
  @Nonnull
  public static String getLocaleDisplayName (@Nullable final Locale aLocale, @Nonnull final Locale aContentLocale)
  {
    if (aLocale == null || aLocale.equals (CGlobal.LOCALE_INDEPENDENT))
      return ELocaleName.ID_LANGUAGE_INDEPENDENT.getDisplayText (aContentLocale);
    if (aLocale.equals (CGlobal.LOCALE_ALL))
      return ELocaleName.ID_LANGUAGE_ALL.getDisplayText (aContentLocale);
    return aLocale.getDisplayName (aContentLocale);
  }

  /**
   * Get the display name of the passed locale <em>in</em> the passed locale.
   * 
   * @param aLocale
   *        The locale to use. May not be <code>null</code>.
   * @return The native display name of the passed locale.
   */
  @Nonnull
  public static String getLocaleNativeDisplayName (@Nonnull final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("locale");
    return getLocaleDisplayName (aLocale, aLocale);
  }

  /**
   * Get all possible locale names in the passed locale
   * 
   * @param aContentLocale
   *        the locale ID in which the language list is required
   * @return The mapping from the input locale to the display text. The result
   *         map is not ordered.
   */
  @Nonnull
  public static Map <Locale, String> getAllLocaleDisplayNames (@Nonnull final Locale aContentLocale)
  {
    final Map <Locale, String> ret = new HashMap <Locale, String> ();
    for (final Locale aCurLocale : LocaleCache.getAllLocales ())
      ret.put (aCurLocale, getLocaleDisplayName (aCurLocale, aContentLocale));
    return ret;
  }

  @Nonnull
  public static List <Locale> getCalculatedLocaleListForResolving (@Nonnull final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("baseLocale");

    return s_aLocaleListResolver.getFromCache (aLocale);
  }

  /**
   * Convert a String in the form "language-country-variant" to a Locale object.
   * Language needs to have exactly 2 characters. Country is optional but if
   * present needs to have exactly 2 characters. Variant is optional.
   * 
   * @param sLocaleAsString
   *        The string representation to be converted to a Locale.
   * @return Never <code>null</code>. If the passed parameter is
   *         <code>null</code> or empty, {@link SystemHelper#getSystemLocale()}
   *         is returned.
   */
  @Nonnull
  public static Locale getLocaleFromString (@Nullable final String sLocaleAsString)
  {
    if (StringHelper.hasNoText (sLocaleAsString))
    {
      // not specified => getDefault
      return SystemHelper.getSystemLocale ();
    }

    String sLanguage;
    String sCountry;
    String sVariant;

    int i1 = sLocaleAsString.indexOf (CGlobal.LOCALE_SEPARATOR);
    if (i1 < 0)
    {
      // No separator present -> use as is
      sLanguage = sLocaleAsString;
      sCountry = "";
      sVariant = "";
    }
    else
    {
      // Language found
      sLanguage = sLocaleAsString.substring (0, i1);
      ++i1;

      // Find next separator
      final int i2 = sLocaleAsString.indexOf (CGlobal.LOCALE_SEPARATOR, i1);
      if (i2 < 0)
      {
        // No other separator -> country is the rest
        sCountry = sLocaleAsString.substring (i1);
        sVariant = "";
      }
      else
      {
        // We have country and variant
        sCountry = sLocaleAsString.substring (i1, i2);
        sVariant = sLocaleAsString.substring (i2 + 1);
      }
    }

    // Unify elements
    if (sLanguage.length () == 2)
      sLanguage = sLanguage.toLowerCase ();// NOPMD
    else
      sLanguage = "";

    if (sCountry.length () == 2)
      sCountry = sCountry.toUpperCase ();// NOPMD
    else
      sCountry = "";

    if (sVariant.length () > 0 && (sLanguage.length () == 2 || sCountry.length () == 2))
      sVariant = sVariant.toUpperCase ();// NOPMD
    else
      sVariant = "";

    // And now resolve using the locale cache
    return LocaleCache.get (sLanguage, sCountry, sVariant);
  }

  @Nullable
  public static Locale getLocaleToUseOrNull (@Nonnull final Locale aRequestLocale,
                                             @Nonnull final Collection <Locale> aAvailableLocales)
  {
    return getLocaleToUseOrFallback (aRequestLocale, aAvailableLocales, null);
  }

  @Nullable
  public static Locale getLocaleToUseOrFallback (@Nonnull final Locale aRequestLocale,
                                                 @Nonnull final Collection <Locale> aAvailableLocales,
                                                 @Nullable final Locale aFallback)
  {
    if (aRequestLocale == null)
      throw new NullPointerException ("requestLocale");
    if (aAvailableLocales == null)
      throw new NullPointerException ("providedLocales");

    // first check direct match
    if (aAvailableLocales.contains (aRequestLocale))
      return aRequestLocale;

    // try to resolve a more general locale than asked for
    for (final Locale aCurrentLocale : getCalculatedLocaleListForResolving (aRequestLocale))
      if (aAvailableLocales.contains (aCurrentLocale))
        return aCurrentLocale;

    // try to resolve a more specific locale than asked for (only check
    // language)
    final String sRequestLanguage = aRequestLocale.getLanguage ();
    if (sRequestLanguage != null)
      for (final Locale aCurrentAvailableLocale : aAvailableLocales)
        if (sRequestLanguage.equals (aCurrentAvailableLocale.getLanguage ()))
          return aCurrentAvailableLocale;

    // If none matched, check if "all" is provided
    if (aAvailableLocales.contains (CGlobal.LOCALE_ALL))
      return CGlobal.LOCALE_ALL;

    // If none matched, check if "independent" is provided
    if (aAvailableLocales.contains (CGlobal.LOCALE_INDEPENDENT))
      return CGlobal.LOCALE_INDEPENDENT;

    // No matching found -> fallback locale
    return aFallback;
  }

  /**
   * Check if the passed locale is one of the special locales "all" or
   * "independent"
   * 
   * @param aLocale
   *        The locale to check. May be <code>null</code>.
   * @return if the passed locale is not <code>null</code> and a special locale.
   * @see CGlobal#LOCALE_ALL
   * @see CGlobal#LOCALE_INDEPENDENT
   */
  public static boolean isSpecialLocale (@Nullable final Locale aLocale)
  {
    return CGlobal.LOCALE_ALL.equals (aLocale) || CGlobal.LOCALE_INDEPENDENT.equals (aLocale);
  }
}
