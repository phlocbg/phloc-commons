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
package com.phloc.commons.io.streams;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link HashingInputStream}.
 * 
 * @author philip
 */
public final class HashingInputStreamTest
{
  private static final Random s_aRandom = new Random ();

  @Test
  public void testAll () throws IOException
  {
    // For all algorithms
    for (final EMessageDigestAlgorithm eMDAlgo : EMessageDigestAlgorithm.values ())
    {
      final String sTestString = "test" + eMDAlgo.getAlgorithm () + "-xxx" + s_aRandom.nextDouble ();

      // First hash
      final HashingInputStream aHIS1 = new HashingInputStream (new NonBlockingByteArrayInputStream (sTestString.getBytes ()),
                                                               eMDAlgo);
      assertTrue (aHIS1.read () != -1);
      StreamUtils.copyInputStreamToOutputStream (aHIS1, new NonBlockingByteArrayOutputStream ());
      final byte [] aDigest1 = aHIS1.getDigest ();

      // Second hash
      final HashingInputStream aHIS2 = new HashingInputStream (new NonBlockingByteArrayInputStream (sTestString.getBytes ()),
                                                               eMDAlgo);
      assertTrue (aHIS2.read () != -1);
      StreamUtils.copyInputStreamToOutputStream (aHIS2, new NonBlockingByteArrayOutputStream ());
      final byte [] aDigest2 = aHIS2.getDigest ();

      // Must be equal
      assertArrayEquals (aDigest1, aDigest2);

      PhlocTestUtils.testToStringImplementation (aHIS1);
    }
  }
}
