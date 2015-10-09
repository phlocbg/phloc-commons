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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link HashingOutputStream}.
 * 
 * @author Philip Helger
 */
public final class HashingOutputStreamTest
{
  private static final Random s_aRandom = new Random ();

  @Test
  public void testAll ()
  {
    // For all algorithms
    for (final EMessageDigestAlgorithm eMDAlgo : EMessageDigestAlgorithm.values ())
    {
      HashingOutputStream aHIS2 = null;
      HashingOutputStream aHIS1 = null;
      try
      {
        final String sTestString = "test" + eMDAlgo.getAlgorithm () + "-xxx" + s_aRandom.nextDouble ();

        // First hash
        aHIS1 = new HashingOutputStream (new NonBlockingByteArrayOutputStream (), eMDAlgo);
        StreamUtils.copyInputStreamToOutputStreamAndCloseOS (new StringInputStream (sTestString,
                                                                                    CCharset.CHARSET_ISO_8859_1_OBJ),
                                                             new NonBlockingByteArrayOutputStream ());
        final byte [] aDigest1 = aHIS1.getDigest ();

        // Second hash
        aHIS2 = new HashingOutputStream (new NonBlockingByteArrayOutputStream (), eMDAlgo);
        StreamUtils.copyInputStreamToOutputStreamAndCloseOS (new StringInputStream (sTestString,
                                                                                    CCharset.CHARSET_ISO_8859_1_OBJ),
                                                             new NonBlockingByteArrayOutputStream ());
        final byte [] aDigest2 = aHIS2.getDigest ();

        // Must be equal
        assertArrayEquals (aDigest1, aDigest2);

        PhlocTestUtils.testToStringImplementation (aHIS1);
      }
      finally
      {
        try
        {
          if (aHIS1 != null)
            aHIS1.close ();
          if (aHIS2 != null)
            aHIS2.close ();
        }
        catch (final IOException ex)
        {}
      }
    }
  }
}
