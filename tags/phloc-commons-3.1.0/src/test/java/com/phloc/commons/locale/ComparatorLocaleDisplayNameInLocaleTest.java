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
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ComparatorLocaleDisplayNameInLocale}.
 * 
 * @author philip
 */
public final class ComparatorLocaleDisplayNameInLocaleTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final Set <Locale> aAll = LocaleCache.getAllLocales ();
    assertEquals (aAll.size (), ContainerHelper.getSorted (aAll, new ComparatorLocaleDisplayNameInLocale (L_DE, L_DE))
                                               .size ());

    try
    {
      new ComparatorLocaleDisplayNameInLocale (L_DE, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}