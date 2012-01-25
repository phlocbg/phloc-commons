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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * Contains helper classes for handling message digests and there results.
 * 
 * @author philip
 */
@Immutable
public final class MessageDigestGeneratorHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MessageDigestGeneratorHelper s_aInstance = new MessageDigestGeneratorHelper ();

  private MessageDigestGeneratorHelper ()
  {}

  public static long getLongFromDigest (@Nonnull final byte [] aDigest)
  {
    long nDigest = 0;
    for (int i = 0; i < aDigest.length; i += 2)
    {
      // Since we have bytes, we can use the 8 bits constant
      nDigest += (byte) (aDigest[i] << CGlobal.BITS_PER_BYTE) | aDigest[i + 1];
    }
    return nDigest;
  }

  @Nonnull
  public static String getHexValueFromDigest (@Nonnull final byte [] aDigest)
  {
    return StringHelper.hexEncode (aDigest);
  }

  @Nonnull
  public static byte [] getDigest (@Nonnull final EMessageDigestAlgorithm eAlgorithm,
                                   @Nonnull final String sText,
                                   @Nonnull final String sCharset)
  {
    return new NonBlockingMessageDigestGenerator (eAlgorithm).update (sText, sCharset).getDigest ();
  }

  @Nonnull
  public static byte [] getDigest (@Nonnull final EMessageDigestAlgorithm eAlgorithm, @Nonnull final byte [] aBytes)
  {
    return new NonBlockingMessageDigestGenerator (eAlgorithm).update (aBytes).getDigest ();
  }

  @Nonnull
  public static byte [] getDigest (@Nonnull final EMessageDigestAlgorithm eAlgorithm,
                                   @Nonnull final byte [] aBytes,
                                   @Nonnegative final int nOfs,
                                   @Nonnegative final int nLength)
  {
    return new NonBlockingMessageDigestGenerator (eAlgorithm).update (aBytes, nOfs, nLength).getDigest ();
  }
}
