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

import javax.annotation.Nullable;

import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * Decoder for run length encoding
 *
 * @author Philip Helger
 */
public class RunLengthCodec implements IByteArrayDecoder
{
  private static final int RUN_LENGTH_EOD = 128;

  public RunLengthCodec ()
  {}

  @Nullable
  public byte [] decode (@Nullable final byte [] aEncodedBuffer)
  {
    return decodeRunLength (aEncodedBuffer);
  }

  @Nullable
  public static byte [] decodeRunLength (@Nullable final byte [] aEncodedBuffer)
  {
    if (aEncodedBuffer == null)
      return null;

    int nDupAmount;
    final byte [] aReadBuffer = new byte [128];
    final NonBlockingByteArrayInputStream aBAIS = new NonBlockingByteArrayInputStream (aEncodedBuffer);
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    try
    {
      while ((nDupAmount = aBAIS.read ()) != -1 && nDupAmount != RUN_LENGTH_EOD)
      {
        if (nDupAmount <= 127)
        {
          // no duplicates present
          int nAmountToCopy = nDupAmount + 1;
          while (nAmountToCopy > 0)
          {
            final int nCompressedRead = aBAIS.read (aReadBuffer, 0, nAmountToCopy);
            aBAOS.write (aReadBuffer, 0, nCompressedRead);
            nAmountToCopy -= nCompressedRead;
          }
        }
        else
        {
          // we have something duplicated
          final int aDupByte = aBAIS.read ();
          if (aDupByte == -1)
            throw new DecoderException ("Unexpected EOF");

          // The char is repeated for 257-nDupAmount types
          for (int i = 0; i < 257 - nDupAmount; i++)
            aBAOS.write (aDupByte);
        }
      }
      return aBAOS.toByteArray ();
    }
    finally
    {
      StreamUtils.close (aBAOS);
      StreamUtils.close (aBAIS);
    }
  }
}
