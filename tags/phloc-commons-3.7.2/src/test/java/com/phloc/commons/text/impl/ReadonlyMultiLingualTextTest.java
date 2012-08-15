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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ISimpleMultiLingualText;

/**
 * Test class for class {@link ReadonlyMultiLingualText}.
 * 
 * @author philip
 */
public final class ReadonlyMultiLingualTextTest extends AbstractPhlocTestCase
{
  @Test
  public void testCtor ()
  {
    IReadonlyMultiLingualText aMLT = new ReadonlyMultiLingualText ();
    assertEquals (0, aMLT.size ());
    assertNotNull (aMLT.getAllLocales ());
    assertTrue (aMLT.getAllLocales ().isEmpty ());

    final Map <Locale, String> aMap = ContainerHelper.newMap (new Locale [] { L_DE, L_EN },
                                                              new String [] { "de", "en" });
    aMLT = new ReadonlyMultiLingualText (aMap);
    assertEquals (2, aMLT.size ());
    assertEquals (2, aMLT.getAllLocales ().size ());
    assertTrue (aMLT.containsLocale (L_DE));
    assertFalse (aMLT.containsLocale (L_FR));
    assertTrue (aMLT.containsLocaleWithFallback (L_DE_AT));
    assertTrue (aMLT.containsLocaleWithFallback (L_DE));
    assertFalse (aMLT.containsLocaleWithFallback (L_FR_FR));
    assertFalse (aMLT.containsLocaleWithFallback (L_FR));
    assertEquals (2, aMLT.getMap ().size ());

    final MultiLingualText t = new MultiLingualText ();
    for (final Map.Entry <Locale, String> aEntry : aMap.entrySet ())
      t.setText (aEntry.getKey (), aEntry.getValue ());
    aMLT = new ReadonlyMultiLingualText (t);
    assertEquals (2, aMLT.size ());
    assertEquals (2, aMLT.getAllLocales ().size ());
    assertTrue (aMLT.containsLocale (L_DE));
    assertFalse (aMLT.containsLocale (L_FR));
    assertTrue (aMLT.containsLocaleWithFallback (L_DE_AT));
    assertTrue (aMLT.containsLocaleWithFallback (L_DE));
    assertFalse (aMLT.containsLocaleWithFallback (L_FR_FR));
    assertFalse (aMLT.containsLocaleWithFallback (L_FR));
    assertEquals (2, aMLT.getMap ().size ());

    aMLT = new ReadonlyMultiLingualText ((ISimpleMultiLingualText) t);
    assertEquals (2, aMLT.size ());
    assertEquals (2, aMLT.getAllLocales ().size ());
    assertTrue (aMLT.containsLocale (L_DE));
    assertFalse (aMLT.containsLocale (L_FR));
    assertTrue (aMLT.containsLocaleWithFallback (L_DE_AT));
    assertTrue (aMLT.containsLocaleWithFallback (L_DE));
    assertFalse (aMLT.containsLocaleWithFallback (L_FR_FR));
    assertFalse (aMLT.containsLocaleWithFallback (L_FR));
    assertEquals (2, aMLT.getMap ().size ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aMLT,
                                                                    new ReadonlyMultiLingualText (ContainerHelper.newMap (new Locale [] { L_DE,
                                                                                                                                         L_EN },
                                                                                                                          new String [] { "de",
                                                                                                                                         "en" })));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new ReadonlyMultiLingualText (),
                                                                    new ReadonlyMultiLingualText ());
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aMLT, new ReadonlyMultiLingualText ());

    try
    {
      new ReadonlyMultiLingualText ((Map <Locale, String>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new ReadonlyMultiLingualText ((IReadonlyMultiLingualText) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new ReadonlyMultiLingualText ((ISimpleMultiLingualText) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
