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
package com.phloc.commons.codec;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.random.VerySecureRandom;

/**
 * Test class for class {@link FlateCodec}
 * 
 * @author Philip Helger
 */
public final class FlateCodecTest
{
  private static void _testEncodeDecode (final byte [] aBytes)
  {
    final FlateCodec aCodec = new FlateCodec ();
    final byte [] aEncoded = aCodec.encode (aBytes);
    assertNotNull (aEncoded);
    assertTrue (aEncoded.length > 0);

    final byte [] aDecoded = aCodec.decode (aEncoded);
    assertNotNull (aDecoded);
    assertArrayEquals (aBytes, aDecoded);
  }

  @Test
  public void testEncodeAndDecode ()
  {
    _testEncodeDecode (new byte [0]);
    _testEncodeDecode (CharsetManager.getAsBytes ("Hallo JUnit", CCharset.CHARSET_ISO_8859_1_OBJ));

    for (int i = 0; i < 256; ++i)
    {
      // build ascending identity field
      final byte [] aBuf = new byte [i];
      for (int j = 0; j < i; ++j)
        aBuf[j] = (byte) j;
      _testEncodeDecode (aBuf);

      // build constant field
      for (int j = 0; j < i; ++j)
        aBuf[j] = (byte) i;
      _testEncodeDecode (aBuf);
    }

    final byte [] aAny = new byte [1024];
    VerySecureRandom.getInstance ().nextBytes (aAny);
    _testEncodeDecode (aAny);
  }

  @Test
  public void testArbitrary ()
  {
    for (int i = 0; i < 500; ++i)
    {
      final byte [] buf = new byte [i];
      VerySecureRandom.getInstance ().nextBytes (buf);
      _testEncodeDecode (buf);
    }
  }
}
