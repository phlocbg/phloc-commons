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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link LocaleCache}.
 * 
 * @author Philip Helger
 */
public final class LocaleCacheTest extends AbstractPhlocTestCase
{
  @Test
  public void testGet ()
  {
    assertNotNull (LocaleCache.getLocale ("de")); //$NON-NLS-1$
    assertNotNull (LocaleCache.getLocale ("de_at")); //$NON-NLS-1$
    assertNotNull (LocaleCache.getLocale ("de_surely_not_known")); //$NON-NLS-1$
    assertNull (LocaleCache.getLocale (null));
    assertNull (LocaleCache.getLocale ("")); //$NON-NLS-1$

    assertNotNull (LocaleCache.getLocale ("de", "AT")); //$NON-NLS-1$ //$NON-NLS-2$
    assertEquals ("de_AT", LocaleCache.getLocale ("de", "AT").toString ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertEquals ("de_AT", LocaleCache.getLocale ("de", "at").toString ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertEquals ("de", LocaleCache.getLocale ("de", null).toString ()); //$NON-NLS-1$ //$NON-NLS-2$
    assertEquals ("_AT", LocaleCache.getLocale (null, "AT").toString ()); //$NON-NLS-1$ //$NON-NLS-2$
    assertNull (LocaleCache.getLocale (null, null));

    assertNotNull (LocaleCache.getLocale ("de", "AT", "Vienna")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertEquals ("de__Vienna", new Locale ("de", "", "Vienna").toString ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    assertEquals ("de__Vienna", LocaleCache.getLocale ("de", null, "Vienna").toString ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertEquals ("de_AT", LocaleCache.getLocale ("de", "AT", null).toString ()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    // test via language tag
    assertNotNull (LocaleCache.getLocale ("en-GB")); //$NON-NLS-1$
    assertTrue (LocaleCache.getLocale ("en-GB") == LocaleCache.getLocale ("en_GB")); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull (LocaleCache.getLocale ("de-AT-Vienna")); //$NON-NLS-1$

    final Locale aVienna1 = LocaleCache.getLocale ("de-AT-Vienna");
    System.out.println (aVienna1.toLanguageTag () + ":::" + aVienna1.toString ());
    final Locale aVienna2 = LocaleCache.getLocale ("de_AT_Vienna");
    System.out.println (aVienna2.toLanguageTag () + ":::" + aVienna2.toString ());

    assertTrue (LocaleCache.getLocale ("de-AT-Vienna") == LocaleCache.getLocale ("de_AT_Vienna")); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull (LocaleCache.getLocale ("gsw-u-sd-chzh")); //$NON-NLS-1$
    assertEquals (LocaleCache.getLocale ("gsw-u-sd-chzh").toLanguageTag (), "gsw-u-sd-chzh"); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull (LocaleCache.getLocale ("ar-u-nu-latn")); //$NON-NLS-1$
    assertEquals (LocaleCache.getLocale ("ar-u-nu-latn").toLanguageTag (), "ar-u-nu-latn"); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull (LocaleCache.getLocale ("he-IL-u-ca-hebrew-tz-jeruslm")); //$NON-NLS-1$
    assertEquals (LocaleCache.getLocale ("he-IL-u-ca-hebrew-tz-jeruslm").toLanguageTag (), //$NON-NLS-1$
                  "he-IL-u-ca-hebrew-tz-jeruslm"); //$NON-NLS-1$

  }

  @Test
  public void testGetInvalid ()
  {
    assertNull (LocaleCache.getLocale ("gb result: chosen nickname \"stevenwhitecotton063\"; success;")); //$NON-NLS-1$
    assertNull (LocaleCache.getLocale ("aa bb")); //$NON-NLS-1$
  }

  @Test
  public void testGetAllLocales ()
  {
    assertNotNull (LocaleCache.getAllLocales ());
    for (final Locale aLocale : LocaleCache.getAllLocales ())
      assertNotNull (aLocale);
  }

  @Test
  public void testGetAllLanguages ()
  {
    for (final Locale aLocale : LocaleCache.getAllLanguages ())
    {
      assertNotNull (aLocale);
      assertTrue (StringHelper.hasText (aLocale.getLanguage ()));
      assertTrue (StringHelper.hasNoText (aLocale.getCountry ()));
      assertTrue (StringHelper.hasNoText (aLocale.getVariant ()));
    }
  }

  @Test
  public void testCompare ()
  {
    final Set <Locale> aLocales = LocaleCache.getAllLocales ();
    assertEquals (aLocales.size (), ContainerHelper.getSorted (aLocales, new ComparatorLocale ()).size ());
    assertEquals (aLocales.size (),
                  ContainerHelper.getSorted (aLocales, new ComparatorLocaleDisplayName (L_DE)).size ());
    assertEquals (aLocales.size (),
                  ContainerHelper.getSorted (aLocales, new ComparatorLocaleDisplayNameNative (L_DE)).size ());
    assertEquals (aLocales.size (),
                  ContainerHelper.getSorted (aLocales, new ComparatorLocaleDisplayNameInLocale (L_DE, L_EN)).size ());
  }

  @Test
  public void testContainsLocale ()
  {
    assertFalse (LocaleCache.containsLocale (null));
    assertFalse (LocaleCache.containsLocale (null));
    assertTrue (LocaleCache.containsLocale ("de")); //$NON-NLS-1$
    assertTrue (LocaleCache.containsLocale ("de_at")); //$NON-NLS-1$
    assertFalse (LocaleCache.containsLocale ("de_at_var")); //$NON-NLS-1$
    assertFalse (LocaleCache.containsLocale ("de_xx")); //$NON-NLS-1$
    assertFalse (LocaleCache.containsLocale ("deh")); //$NON-NLS-1$

    assertTrue (LocaleCache.containsLocale ("de", "at")); //$NON-NLS-1$ //$NON-NLS-2$
    assertFalse (LocaleCache.containsLocale ("de", "xx")); //$NON-NLS-1$ //$NON-NLS-2$

    assertFalse (LocaleCache.containsLocale (null, null, null));
    assertTrue (LocaleCache.containsLocale ("de", null, null)); //$NON-NLS-1$
    assertTrue (LocaleCache.containsLocale ("de", "at", null)); //$NON-NLS-1$ //$NON-NLS-2$
    assertFalse (LocaleCache.containsLocale ("de", "xx", null)); //$NON-NLS-1$ //$NON-NLS-2$
    assertFalse (LocaleCache.containsLocale ("de", "at", "var")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  @Test
  public void testResetCache ()
  {
    LocaleCache.resetCache ();
    final int nCount = LocaleCache.getAllLanguages ().size ();
    LocaleCache.getLocale ("xy"); //$NON-NLS-1$
    assertEquals (nCount + 1, LocaleCache.getAllLanguages ().size ());
    LocaleCache.resetCache ();
    assertEquals (nCount, LocaleCache.getAllLanguages ().size ());
  }
}
