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
package com.phloc.commons.text.resolve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.NoTranslationRequired;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ISimpleMultiLingualText;
import com.phloc.commons.text.impl.TextProvider;

/**
 * Test class for class {@link DefaultTextResolver}.
 * 
 * @author philip
 */
public final class DefaultTextResolverTest
{
  @NoTranslationRequired
  public static enum EText implements IHasDisplayText
  {
    TEXT1 ("Text1de", "Text1en"),
    TEXT2 ("Text2de", "Text2en"),
    TEXT3 ("Text3{0}de", "Text3{0}en");
    private final ISimpleMultiLingualText m_aTP;

    private EText (final String sDE, final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    public String getDisplayText (final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }

    public String getDisplayTextWithArgs (final Locale aContentLocale, final Object... aArgs)
    {
      return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
    }
  }

  // DISABLE DEBUG MODE!

  private static boolean s_bDebugMode;

  @BeforeClass
  public static void before ()
  {
    s_bDebugMode = GlobalDebug.isDebugMode ();
    GlobalDebug.setDebugModeDirect (false);
  }

  @AfterClass
  public static void after ()
  {
    GlobalDebug.setDebugModeDirect (s_bDebugMode);
  }

  @Test
  public void testGetText ()
  {
    final Locale aDE = TextProvider.DE;
    final Locale aEN = TextProvider.EN;
    final Locale aHR = LocaleCache.get ("hr", "HR");
    final Locale aSR = LocaleCache.get ("sr", "RS");

    // Regular
    assertEquals ("Text1de", EText.TEXT1.getDisplayText (aDE));
    assertEquals ("Text1en", EText.TEXT1.getDisplayText (aEN));

    // German has override!
    assertEquals ("Text2de-override", EText.TEXT2.getDisplayText (aDE));
    assertEquals ("Text2en", EText.TEXT2.getDisplayText (aEN));

    // Use fallback
    assertEquals ("Text1hr", EText.TEXT1.getDisplayText (aHR));
    assertEquals (null, EText.TEXT2.getDisplayText (aHR));

    // No fallback properties file present
    assertNull (EText.TEXT1.getDisplayText (aSR));
    assertNull (EText.TEXT2.getDisplayText (aSR));

    // Check bundle names
    assertTrue (DefaultTextResolver.getAllUsedOverrideBundleNames ().contains ("properties/override-de"));
    assertFalse (DefaultTextResolver.getAllUsedOverrideBundleNames ().contains ("properties/override-en"));
    assertTrue (DefaultTextResolver.getAllUsedFallbackBundleNames ().contains ("properties/hr_HR"));
    assertFalse (DefaultTextResolver.getAllUsedFallbackBundleNames ().contains ("properties/sr_RS"));
  }

  @Test
  public void testGetTextWithArgs ()
  {
    final Locale aDE = TextProvider.DE;
    final Locale aEN = TextProvider.EN;

    // Regular
    assertEquals ("Text3abcde", EText.TEXT3.getDisplayTextWithArgs (aDE, "abc"));
    assertEquals ("Text3abcen", EText.TEXT3.getDisplayTextWithArgs (aEN, "abc"));

    // Clear cache and try again (should not make any difference)
    DefaultTextResolver.clearCache ();

    // Regular
    assertEquals ("Text3abcde", EText.TEXT3.getDisplayTextWithArgs (aDE, "abc"));
    assertEquals ("Text3abcen", EText.TEXT3.getDisplayTextWithArgs (aEN, "abc"));
  }
}
