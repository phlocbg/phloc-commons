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
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.cache.AbstractNotifyingCache;

/**
 * Helper class to easily create commonly used {@link Collator} objects.
 * 
 * @author philip
 */
@ThreadSafe
public final class CollatorUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CollatorUtils.class);

  private static final class Cache extends AbstractNotifyingCache <Locale, Collator>
  {
    public Cache ()
    {
      super (CollatorUtils.class.getName ());
    }

    @Override
    @Nonnull
    protected Collator getValueToCache (final Locale aLocale)
    {
      final Locale aCollatorLocale = aLocale != null ? aLocale : Locale.getDefault ();
      final Collator c = Collator.getInstance (aCollatorLocale);
      if (!(c instanceof RuleBasedCollator))
        throw new IllegalStateException ("Collator.getInstance did not return a RulleBasedCollator!");

      try
      {
        final String sRules = ((RuleBasedCollator) c).getRules ();
        if (sRules.indexOf ("<'.'<") < 0)
        {
          // Nothing to replace - use collator as it is
          s_aLogger.warn ("Failed to identify the Collator rule part to be replaced. Locale used = " + aCollatorLocale);
          return c;
        }

        final String sNewRules = sRules.replace ("<'.'<", "<' '<'.'<");
        final RuleBasedCollator ret = new RuleBasedCollator (sNewRules);
        ret.setStrength (Collator.TERTIARY);
        ret.setDecomposition (Collator.FULL_DECOMPOSITION);
        return ret;
      }
      catch (final ParseException ex)
      {
        throw new IllegalStateException ("Failed to parse collator rule set", ex);
      }
    }
  }

  private static final Cache s_aCache = new Cache ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CollatorUtils s_aInstance = new CollatorUtils ();

  private CollatorUtils ()
  {}

  /**
   * Create a collator that is based on the standard collator but sorts spaces
   * before dots, because spaces are more important word separators than dots.
   * Another example is the correct sorting of things like "1.1 a" vs.
   * "1.1.1 b". This is the default collator used for sorting by default!
   * 
   * @param aLocale
   *        The locale for which the collator is to be retrieved. May be
   *        <code>null</code> to indicate the usage of the default locale.
   * @return The created {@link RuleBasedCollator} and never <code>null</code>.
   */
  @Nonnull
  public static Collator getCollatorSpaceBeforeDot (@Nullable final Locale aLocale)
  {
    return (Collator) s_aCache.getFromCache (aLocale).clone ();
  }
}
