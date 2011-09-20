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
package com.phloc.test.java;

import java.util.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.locale.ComparatorLocale;
import com.phloc.commons.locale.country.ComparatorLocaleCountry;

public final class FuncTestJavaListAllLocales
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FuncTestJavaListAllLocales.class);

  @Test
  public void testListAllCountries ()
  {
    for (final Locale aLocale : ContainerHelper.getSorted (Locale.getAvailableLocales (),
                                                           new ComparatorLocaleCountry (null)))
      if (aLocale.getCountry ().length () > 0)
        s_aLogger.info (aLocale.getCountry () + " " + aLocale.getDisplayCountry () + " (" + aLocale.toString () + ")");
  }

  @Test
  public void testListAllSerbianCountries ()
  {
    for (final Locale aLocale : ContainerHelper.getSorted (Locale.getAvailableLocales (), new ComparatorLocale (null)))
      if (aLocale.getLanguage ().equals ("sr") ||
          aLocale.getLanguage ().equals ("sh") ||
          aLocale.getLanguage ().equals ("bs"))
        s_aLogger.info (aLocale.toString () + ": " + aLocale.getDisplayName ());
  }
}
