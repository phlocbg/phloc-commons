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
package com.phloc.commons.charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.random.VerySecureRandom;

/**
 * Test class for class {@link CharsetManager}.
 * 
 * @author philip
 */
public final class CharsetManagerTest
{
  @Test
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testCharsetFromName ()
  {
    assertNotNull (CharsetManager.charsetFromName ("UTF-8"));
    try
    {
      CharsetManager.charsetFromName (null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // Illegal name
      CharsetManager.charsetFromName ("does not exist");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      // Unsupported
      CharsetManager.charsetFromName ("bla");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testGetAsBytes ()
  {
    final String s = "äbc";
    assertEquals (3, CharsetManager.getAsBytes (s, CCharset.CHARSET_ISO_8859_1).length);
    assertEquals (4, CharsetManager.getAsBytes (s, CCharset.CHARSET_UTF_8).length);

    try
    {
      CharsetManager.getAsBytes (s, (String) null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      CharsetManager.getAsBytes (s, "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      CharsetManager.getAsBytes (s, "bla");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testGetAsBytesCharset ()
  {
    final String s = "äbc";
    assertEquals (3, CharsetManager.getAsBytes (s, CCharset.CHARSET_ISO_8859_1_OBJ).length);
    assertEquals (4, CharsetManager.getAsBytes (s, CCharset.CHARSET_UTF_8_OBJ).length);

    try
    {
      CharsetManager.getAsBytes (s, (Charset) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetAsStringInOtherCharset ()
  {
    final String s = "äbc";
    assertEquals (3, CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_ISO_8859_1, CCharset.CHARSET_UTF_8)
                                   .length ());
    assertEquals (4, CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_UTF_8, CCharset.CHARSET_ISO_8859_1)
                                   .length ());
    assertNull (CharsetManager.getAsStringInOtherCharset (null, CCharset.CHARSET_ISO_8859_1, CCharset.CHARSET_UTF_8));
    assertEquals (s,
                  CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_ISO_8859_1, CCharset.CHARSET_ISO_8859_1));
    assertEquals (s, CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_UTF_8, CCharset.CHARSET_UTF_8));

    try
    {
      CharsetManager.getAsStringInOtherCharset (s, null, CCharset.CHARSET_UTF_8);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_ISO_8859_1, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGetAsStringInOtherCharsetCharset ()
  {
    final String s = "äbc";
    assertEquals (3,
                  CharsetManager.getAsStringInOtherCharset (s,
                                                            CCharset.CHARSET_ISO_8859_1_OBJ,
                                                            CCharset.CHARSET_UTF_8_OBJ).length ());
    assertEquals (4,
                  CharsetManager.getAsStringInOtherCharset (s,
                                                            CCharset.CHARSET_UTF_8_OBJ,
                                                            CCharset.CHARSET_ISO_8859_1_OBJ).length ());
    assertNull (CharsetManager.getAsStringInOtherCharset (null,
                                                          CCharset.CHARSET_ISO_8859_1_OBJ,
                                                          CCharset.CHARSET_UTF_8_OBJ));
    assertEquals (s, CharsetManager.getAsStringInOtherCharset (s,
                                                               CCharset.CHARSET_ISO_8859_1_OBJ,
                                                               CCharset.CHARSET_ISO_8859_1_OBJ));
    assertEquals (s,
                  CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_UTF_8_OBJ, CCharset.CHARSET_UTF_8_OBJ));

    try
    {
      CharsetManager.getAsStringInOtherCharset (s, null, CCharset.CHARSET_UTF_8_OBJ);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      CharsetManager.getAsStringInOtherCharset (s, CCharset.CHARSET_ISO_8859_1_OBJ, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testGreek () throws Exception
  {
    final String sAlpha = "?\u03B1";
    byte [] b = CharsetManager.getAsBytes (sAlpha, CCharset.CHARSET_UTF_8);
    assertEquals (sAlpha, CharsetManager.getAsString (b, CCharset.CHARSET_UTF_8));
    assertEquals (sAlpha, CharsetManager.getAsString (b, CCharset.CHARSET_UTF_8_OBJ));
    try
    {
      CharsetManager.getAsString (b, "charset bla");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    b = CharsetManager.getAsBytes (sAlpha, CCharset.CHARSET_UTF_8_OBJ);
    assertEquals (sAlpha, CharsetManager.getAsString (b, CCharset.CHARSET_UTF_8));
    assertEquals (sAlpha, CharsetManager.getAsString (b, CCharset.CHARSET_UTF_8_OBJ));

    BufferedReader aReader = new BufferedReader (new InputStreamReader (new NonBlockingByteArrayInputStream (b),
                                                                        CCharset.CHARSET_UTF_8));
    assertEquals (sAlpha, aReader.readLine ());
    StreamUtils.close (aReader);

    aReader = new BufferedReader (new InputStreamReader (new NonBlockingByteArrayInputStream (b),
                                                         CCharset.CHARSET_UTF_8_OBJ));
    assertEquals (sAlpha, aReader.readLine ());
    StreamUtils.close (aReader);
  }

  @Test
  public void testJavaCompiledAsUTF8 ()
  {
    final String s = "ä";
    if (s.length () != 1)
      throw new IllegalStateException ("Seems like the Java Source files were not compiled with UTF-8 encoding!");
  }

  @Test
  public void testGetUTF8ByteCount ()
  {
    assertEquals (0, CharsetManager.getUTF8ByteCount ((String) null));
    assertEquals (0, CharsetManager.getUTF8ByteCount ((char []) null));

    assertEquals (2, CharsetManager.getUTF8ByteCount ("\0"));
    assertEquals (2, CharsetManager.getUTF8ByteCount ("ä"));
    assertEquals (2, CharsetManager.getUTF8ByteCount ('ä'));
    assertEquals (0, CharsetManager.getUTF8ByteCount (""));
    assertEquals (3, CharsetManager.getUTF8ByteCount ("abc"));
    assertEquals (9, CharsetManager.getUTF8ByteCount ("abcäöü"));
    assertEquals (3, CharsetManager.getUTF8ByteCount ("\ud7ff"));

    assertEquals (0, CharsetManager.getUTF8ByteCount ("\udfff"));
    assertEquals (0, CharsetManager.getUTF8ByteCount ("\ue000"));
    assertEquals (0, CharsetManager.getUTF8ByteCount ("\uffff"));
    try
    {
      CharsetManager.getUTF8ByteCount (0x110000);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      CharsetManager.getUTF8ByteCount (0x10000);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  @Ignore
  public void testGetUTF8ByteCountRandom ()
  {
    for (int i = 0; i < 1000; i++)
    {
      // Build random String with 20 chars
      final int nStringLen = 20;
      final StringBuilder aSB = new StringBuilder ();
      for (int x = 0; x < nStringLen; ++x)
      {
        final int c = VerySecureRandom.getInstance ().nextInt (Character.MIN_HIGH_SURROGATE);
        aSB.append ((char) c);
      }

      // Count
      final int nCounted = CharsetManager.getUTF8ByteCount (aSB.toString ());
      assertTrue (nCounted >= nStringLen);

      // Convert and count
      final byte [] b = CharsetManager.getAsBytes (aSB.toString (), CCharset.CHARSET_UTF_8);
      assertTrue (b.length >= nStringLen);

      // Must be equals
      assertEquals (Arrays.toString (b), nCounted, b.length);
    }
  }

  @Test
  public void testBasic ()
  {
    assertNotNull (CharsetManager.getAllCharsets ());
    assertTrue (CharsetManager.getAllCharsets ().size () > 0);
  }
}
