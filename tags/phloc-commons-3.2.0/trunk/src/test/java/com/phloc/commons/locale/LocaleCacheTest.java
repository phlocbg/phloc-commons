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
 * @author philip
 */
public final class LocaleCacheTest extends AbstractPhlocTestCase
{
  @Test
  public void testGet ()
  {
    assertNotNull (LocaleCache.get ("de"));
    assertNotNull (LocaleCache.get ("de_at"));
    assertNotNull (LocaleCache.get ("de_surely_not_known"));
    assertNull (LocaleCache.get (null));
    assertNull (LocaleCache.get (""));

    assertNotNull (LocaleCache.get ("de", "AT"));
    assertEquals ("de_AT", LocaleCache.get ("de", "AT").toString ());
    assertEquals ("de_AT", LocaleCache.get ("de", "at").toString ());
    assertEquals ("de", LocaleCache.get ("de", null).toString ());
    assertEquals ("_AT", LocaleCache.get (null, "AT").toString ());
    assertNull (LocaleCache.get (null, null));

    assertNotNull (LocaleCache.get ("de", "AT", "Vienna"));
    assertEquals ("de__Vienna", new Locale ("de", "", "Vienna").toString ());
    assertEquals ("de__Vienna", LocaleCache.get ("de", null, "Vienna").toString ());
    assertEquals ("de_AT", LocaleCache.get ("de", "AT", null).toString ());
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
    assertEquals (aLocales.size (), ContainerHelper.getSorted (aLocales, new ComparatorLocaleDisplayName (L_DE))
                                                   .size ());
    assertEquals (aLocales.size (), ContainerHelper.getSorted (aLocales, new ComparatorLocaleDisplayNameNative (L_DE))
                                                   .size ());
    assertEquals (aLocales.size (),
                  ContainerHelper.getSorted (aLocales, new ComparatorLocaleDisplayNameInLocale (L_DE, L_EN)).size ());
  }

  @Test
  public void testContainsLocale ()
  {
    assertFalse (LocaleCache.containsLocale (null));
    assertTrue (LocaleCache.containsLocale ("de"));
    assertTrue (LocaleCache.containsLocale ("de_at"));
    assertFalse (LocaleCache.containsLocale ("de_at_var"));
    assertFalse (LocaleCache.containsLocale ("de_xx"));

    assertTrue (LocaleCache.containsLocale ("de", "at"));
    assertFalse (LocaleCache.containsLocale ("de", "xx"));

    assertFalse (LocaleCache.containsLocale (null, null, null));
    assertTrue (LocaleCache.containsLocale ("de", null, null));
    assertTrue (LocaleCache.containsLocale ("de", "at", null));
    assertFalse (LocaleCache.containsLocale ("de", "xx", null));
    assertFalse (LocaleCache.containsLocale ("de", "at", "var"));
  }
}
