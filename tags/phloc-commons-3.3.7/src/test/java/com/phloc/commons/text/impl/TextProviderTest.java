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
package com.phloc.commons.text.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.text.ISimpleMultiLingualText;
import com.phloc.commons.text.ITextProvider;

/**
 * Test class for class {@link TextProvider}.
 * 
 * @author philip
 */
public final class TextProviderTest extends AbstractPhlocTestCase
{
  @Test
  public void testGetText ()
  {
    ISimpleMultiLingualText aTP = TextProvider.create_DE_EN ("Hallo", "Hello");
    assertEquals ("Hallo", aTP.getTextWithLocaleFallback (L_DE_DE));
    assertEquals ("Hallo", aTP.getTextWithLocaleFallback (L_DE));
    assertNull (aTP.getTextWithLocaleFallback (L_FR));

    aTP = TextProvider.create_DE ("Hallo");
    assertEquals ("Hallo", aTP.getTextWithLocaleFallback (L_DE_DE));
    assertEquals ("Hallo", aTP.getTextWithLocaleFallback (L_DE));
    assertNull (aTP.getTextWithLocaleFallback (L_FR));

    aTP = TextProvider.create_EN ("Hello");
    assertEquals ("Hello", aTP.getTextWithLocaleFallback (L_EN_US));
    assertEquals ("Hello", aTP.getTextWithLocaleFallback (L_EN_GB));
    assertEquals ("Hello", aTP.getTextWithLocaleFallback (L_EN));
    assertNull (aTP.getTextWithLocaleFallback (L_FR));
  }

  @Test
  public void testIsEmpty ()
  {
    ISimpleMultiLingualText aTP = TextProvider.create_DE_EN ("Hallo", "Hello");
    assertFalse (aTP.isEmpty ());

    aTP = new TextProvider ();
    assertTrue (aTP.isEmpty ());
  }

  @Test
  public void testGetTextWithArgs ()
  {
    final ITextProvider aTP = TextProvider.create_DE_EN ("Hallo {0}", "{0} Hello");
    assertEquals ("Hallo {0}", aTP.getTextWithLocaleFallback (L_DE_DE));
    assertEquals ("Hallo {0}", aTP.getTextWithLocaleFallback (L_DE));
    assertNull (aTP.getTextWithLocaleFallback (L_FR));

    assertEquals ("Hallo Hugo", aTP.getTextWithLocaleFallbackAndArgs (L_DE_DE, "Hugo"));
    assertEquals ("Hallo Hugo", aTP.getTextWithLocaleFallbackAndArgs (L_DE, "Hugo"));
    assertEquals ("Hugo Hello", aTP.getTextWithLocaleFallbackAndArgs (L_EN, "Hugo"));
    assertNull (aTP.getTextWithLocaleFallbackAndArgs (L_FR));
  }

  @Test
  public void testQuotes ()
  {
    assertTrue (GlobalDebug.isDebugMode ());

    assertNotNull (TextProvider.create_DE_EN ("Test 123!", ""));
    assertNotNull (TextProvider.create_DE_EN ("Test {0} 123!", ""));
    assertNotNull (TextProvider.create_DE_EN ("Test ''{0}'' 123!", ""));

    try
    {
      // Must use two single quotes
      TextProvider.create_DE_EN ("Test '{0}' 123!", "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      TextProvider.create_DE_EN ("'{0}' 123!", "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      TextProvider.create_DE_EN ("Test '{0}'", "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      TextProvider.create_DE_EN ("Test\\nmasked new line", "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      // Requires only a single quote
      TextProvider.create_DE ("Test '' no arguments");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
