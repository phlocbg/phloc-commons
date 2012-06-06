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
package com.phloc.commons.text.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.locale.LocaleUtils;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.ISimpleMultiLingualText;
import com.phloc.commons.text.ITextProvider;

/**
 * An in-memory implementation of the {@link ITextProvider} interface.
 * 
 * @author philip
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings ("SE_NO_SERIALVERSIONID")
public class TextProvider extends AbstractTextProvider implements ISimpleMultiLingualText, Serializable
{
  public static final Locale DE = LocaleCache.get ("de");
  public static final Locale EN = LocaleCache.get ("en");

  private final Map <Locale, String> m_aTexts = new HashMap <Locale, String> ();

  protected TextProvider ()
  {}

  private static void _performDebugOnlyConsistencyChecks (@Nonnull final String sValue)
  {
    // String contains masked newline?
    if (sValue.indexOf ("\\n") >= 0)
      throw new IllegalArgumentException ("Passed string contains a masked newline - replace with an inline one!");

    if (sValue.indexOf ("{0}") >= 0)
    {
      // When formatting is used, 2 single quotes are required!
      if (RegExHelper.stringMatchesPattern ("^'[^'].*", sValue))
        throw new IllegalArgumentException ("The passed string seems to start with unclosed single quotes: " + sValue);
      if (RegExHelper.stringMatchesPattern (".*[^']'[^'].*", sValue))
        throw new IllegalArgumentException ("The passed string seems to contain unclosed single quotes: " + sValue);
    }
    else
    {
      // When no formatting is used, single quotes are required!
      if (RegExHelper.stringMatchesPattern (".*''.*", sValue))
        throw new IllegalArgumentException ("The passed string seems to contain 2 single quotes: " + sValue);
    }
  }

  @Nonnull
  protected final TextProvider internalAddText (@Nonnull final Locale aContentLocale, @Nonnull final String sValue)
  {
    if (m_aTexts.containsKey (aContentLocale))
      throw new IllegalArgumentException ("Locale '" +
                                          aContentLocale +
                                          "' already contained in TextProvider: " +
                                          toString ());

    return internalSetText (aContentLocale, sValue);
  }

  @Nonnull
  protected final TextProvider internalSetText (@Nonnull final Locale aContentLocale, @Nonnull final String sValue)
  {
    if (GlobalDebug.isDebugMode () && sValue != null)
      _performDebugOnlyConsistencyChecks (sValue);

    m_aTexts.put (aContentLocale, sValue);
    return this;
  }

  @Nonnull
  protected final EChange internalRemoveText (@Nullable final Locale aLocale)
  {
    return EChange.valueOf (m_aTexts.remove (aLocale) != null);
  }

  @Nonnull
  protected final EChange internalClear ()
  {
    if (m_aTexts.isEmpty ())
      return EChange.UNCHANGED;
    m_aTexts.clear ();
    return EChange.CHANGED;
  }

  @Nonnull
  public final TextProvider addTextDE (@Nonnull final String sDE)
  {
    return internalAddText (DE, sDE);
  }

  @Nonnull
  public final TextProvider addTextEN (@Nonnull final String sEN)
  {
    return internalAddText (EN, sEN);
  }

  @Nonnull
  @ReturnsMutableObject (reason = "Internal use only")
  protected final Map <Locale, String> internalGetMap ()
  {
    return m_aTexts;
  }

  @Override
  @Nullable
  protected final Locale internalGetLocaleToUseWithFallback (@Nonnull final Locale aContentLocale)
  {
    return LocaleUtils.getLocaleToUseOrNull (aContentLocale, m_aTexts.keySet ());
  }

  @Override
  @OverrideOnDemand
  @Nullable
  protected String internalGetText (@Nonnull final Locale aContentLocale)
  {
    return m_aTexts.get (aContentLocale);
  }

  @Nonnull
  @ReturnsMutableObject (reason = "Internal use only")
  public final Set <Locale> internalGetAllLocales ()
  {
    return m_aTexts.keySet ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <Locale> getAllLocales ()
  {
    return ContainerHelper.newSet (m_aTexts.keySet ());
  }

  public final boolean containsLocale (@Nullable final Locale aLocale)
  {
    return m_aTexts.containsKey (aLocale);
  }

  public final boolean containsLocaleWithFallback (@Nullable final Locale aContentLocale)
  {
    if (aContentLocale != null)
      for (final Locale aCurrentLocale : LocaleUtils.getCalculatedLocaleListForResolving (aContentLocale))
        if (containsLocale (aCurrentLocale))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <Locale, String> getMap ()
  {
    return ContainerHelper.newMap (m_aTexts);
  }

  @Nonnegative
  public final int size ()
  {
    return m_aTexts.size ();
  }

  public final boolean isEmpty ()
  {
    return m_aTexts.isEmpty ();
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final TextProvider rhs = (TextProvider) o;
    return m_aTexts.equals (rhs.m_aTexts);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aTexts).getHashCode ();
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public String toString ()
  {
    return new ToStringGenerator (this).append ("texts", m_aTexts).toString ();
  }

  @Nonnull
  public static ISimpleMultiLingualText create_DE (@Nonnull final String sDE)
  {
    return new TextProvider ().addTextDE (sDE);
  }

  @Nonnull
  public static ISimpleMultiLingualText create_EN (@Nonnull final String sEN)
  {
    return new TextProvider ().addTextEN (sEN);
  }

  @Nonnull
  public static ISimpleMultiLingualText create_DE_EN (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    return new TextProvider ().addTextDE (sDE).addTextEN (sEN);
  }
}
