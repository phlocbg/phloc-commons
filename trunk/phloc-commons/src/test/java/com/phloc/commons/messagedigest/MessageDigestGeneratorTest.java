/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.messagedigest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MessageDigestGenerator}.
 * 
 * @author philip
 */
public final class MessageDigestGeneratorTest
{
  @SuppressWarnings ("deprecation")
  private void _testRun (final EMessageDigestAlgorithm... aAlgo)
  {
    final IMessageDigestGenerator x = new MessageDigestGenerator (aAlgo);
    assertEquals (aAlgo[0].getAlgorithm (), x.getAlgorithmName ());
    x.update ("Any string", CCharset.CHARSET_ISO_8859_1);
    x.update ("Any string", CCharset.CHARSET_ISO_8859_1_OBJ);
    final IMessageDigestGenerator y = new MessageDigestGenerator (aAlgo);
    assertEquals (aAlgo[0].getAlgorithm (), y.getAlgorithmName ());
    y.update ("Any ", CCharset.CHARSET_ISO_8859_1);
    y.update ("string", CCharset.CHARSET_ISO_8859_1);
    y.update ("Any ", CCharset.CHARSET_ISO_8859_1_OBJ);
    y.update ("string", CCharset.CHARSET_ISO_8859_1_OBJ);
    assertEquals (x.getDigestLong (), y.getDigestLong ());
    assertEquals (x.getDigestHexString (), y.getDigestHexString ());
    assertEquals (x.getDigestLong (), x.getDigestLong ());
    assertEquals (x.getDigestHexString (), x.getDigestHexString ());
    assertEquals (y.getDigestLong (), y.getDigestLong ());
    assertEquals (y.getDigestHexString (), y.getDigestHexString ());

    long nSaved = 0;
    for (int i = 0; i < 5; ++i)
    {
      final IMessageDigestGenerator z = new MessageDigestGenerator (aAlgo);
      z.update ((byte) 12);
      if (i == 0)
        nSaved = z.getDigestLong ();
      else
        assertEquals (z.getDigestLong (), nSaved);
    }
  }

  @Test
  public void testDifferentAlgorithms ()
  {
    for (final EMessageDigestAlgorithm eMD : EMessageDigestAlgorithm.values ())
      _testRun (eMD);
  }

  @SuppressWarnings ("deprecation")
  @Test
  public void testErrorCases ()
  {
    try
    {
      // null array not allowed
      _testRun ((EMessageDigestAlgorithm []) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // null item not allowed
      _testRun ((EMessageDigestAlgorithm) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // Empty array not allowed
      _testRun (new EMessageDigestAlgorithm [0]);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    final IMessageDigestGenerator md = new MessageDigestGenerator ();
    try
    {
      md.update ((byte []) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      md.update ((byte []) null, 0, 10);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      md.update ((String) null, CCharset.CHARSET_ISO_8859_1);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      md.update ((String) null, CCharset.CHARSET_ISO_8859_1_OBJ);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    md.update ((byte) 5);
    md.update (CharsetManager.getAsBytes ("abc", CCharset.CHARSET_ISO_8859_1_OBJ));
    md.update (CharsetManager.getAsBytes ("abc", CCharset.CHARSET_ISO_8859_1_OBJ), 1, 1);
    md.update ("abc", CCharset.CHARSET_UTF_8);
    md.update ("äöü", CCharset.CHARSET_UTF_8_OBJ);
    assertNotNull (md.getDigest ());
    assertNotNull (md.getDigest (2));
    assertEquals (2, md.getDigest (2).length);
    try
    {
      // Already digested
      md.update ((byte) 6);
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    try
    {
      // Already digested
      md.update (new byte [5]);
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    md.reset ();
    md.update ((byte) 6);

    PhlocTestUtils.testToStringImplementation (md);
  }
}
