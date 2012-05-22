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
package com.phloc.commons.messagedigest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;

/**
 * Test class for class {@link MessageDigestGeneratorHelper}.
 * 
 * @author philip
 */
public final class MessageDigestGeneratorHelperTest
{
  @Test
  public void testAll () throws UnsupportedEncodingException
  {
    // For all algorithms
    for (final EMessageDigestAlgorithm eAlgo : EMessageDigestAlgorithm.values ())
    {
      // Create 2 MDGens
      final IMessageDigestGenerator aMD1 = new MessageDigestGenerator (eAlgo);
      final IMessageDigestGenerator aMD2 = new NonBlockingMessageDigestGenerator (eAlgo);
      for (int i = 0; i < 255; ++i)
      {
        final byte [] aBytes = ("abc" + i + "def").getBytes (CCharset.CHARSET_ISO_8859_1);
        aMD1.update ((byte) i);
        aMD1.update (aBytes);
        aMD2.update ((byte) i);
        aMD2.update (aBytes);
      }

      // Results must be equal
      assertArrayEquals (aMD1.getDigest (), aMD2.getDigest ());
      assertEquals (MessageDigestGeneratorHelper.getLongFromDigest (aMD1.getDigest ()),
                    MessageDigestGeneratorHelper.getLongFromDigest (aMD2.getDigest ()));
      assertEquals (MessageDigestGeneratorHelper.getHexValueFromDigest (aMD1.getDigest ()),
                    MessageDigestGeneratorHelper.getHexValueFromDigest (aMD2.getDigest ()));

      final String s = "phloc commons is great";
      final byte [] aBytes = s.getBytes (CCharset.CHARSET_ISO_8859_1);
      assertArrayEquals (MessageDigestGeneratorHelper.getDigest (eAlgo, aBytes),
                         MessageDigestGeneratorHelper.getDigest (eAlgo, aBytes));
      assertArrayEquals (MessageDigestGeneratorHelper.getDigest (eAlgo, aBytes, 5, 10),
                         MessageDigestGeneratorHelper.getDigest (eAlgo, aBytes, 5, 10));
      assertArrayEquals (MessageDigestGeneratorHelper.getDigest (eAlgo, s, CCharset.CHARSET_UTF_8),
                         MessageDigestGeneratorHelper.getDigest (eAlgo, s, CCharset.CHARSET_UTF_8));
    }
  }
}
