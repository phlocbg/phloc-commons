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

import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * Decoder for ASCII85 encoded values
 *
 * @author Philip Helger
 */
public final class ASCII85Codec implements IByteArrayDecoder
{
  private static final int BIT1 = 8;
  private static final int BIT2 = 16;
  private static final int BIT3 = 24;
  private static final int ENCODED_MAX = 117;
  private static final int ENCODED_MIN = 33;
  private static final int EIGHTY_FIVE = 85;

  public ASCII85Codec ()
  {}

  @Nullable
  public byte [] decode (@Nullable final byte [] aEncodedBuffer)
  {
    return decodeASCII85 (aEncodedBuffer);
  }

  @Nullable
  public static byte [] decodeASCII85 (@Nullable final byte [] aEncodedBuffer)
  {
    if (aEncodedBuffer == null)
      return null;
    if (aEncodedBuffer.length < 4)
      throw new IllegalArgumentException ("Buffer too small: " + aEncodedBuffer.length);

    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    try
    {
      int nEncodedCount = 0;
      final byte [] aBuffer = new byte [5];

      // Determine start index
      int nIndex = 0;

      // Special start sequence "<~" ??
      if (aEncodedBuffer[0] == '<' && aEncodedBuffer[1] == '~')
        nIndex = 2;

      for (; nIndex < aEncodedBuffer.length; ++nIndex)
      {
        final byte nEncByte = aEncodedBuffer[nIndex];

        // end of data with "~>"
        if (nEncByte == '~')
          break;

        // skip all whitespaces
        if (Character.isWhitespace (nEncByte))
          continue;

        if (nEncByte == 'z' && nEncodedCount == 0)
        {
          aBAOS.write (0);
          aBAOS.write (0);
          aBAOS.write (0);
          aBAOS.write (0);
        }
        else
        {
          if (nEncByte < ENCODED_MIN || nEncByte > ENCODED_MAX)
            throw new DecoderException ("Illegal character in ASCII85Decode: " + nEncByte);

          aBuffer[nEncodedCount] = (byte) (nEncByte - ENCODED_MIN);
          ++nEncodedCount;
          if (nEncodedCount == 5)
          {
            nEncodedCount = 0;
            int r = 0;
            for (int j = 0; j < 5; ++j)
              r = r * EIGHTY_FIVE + aBuffer[j];
            aBAOS.write ((byte) (r >> BIT3));
            aBAOS.write ((byte) (r >> BIT2));
            aBAOS.write ((byte) (r >> BIT1));
            aBAOS.write ((byte) r);
          }
        }
      }

      int nRest;
      switch (nEncodedCount)
      {
        case 1:
          throw new IllegalStateException ("Unexpected end of ASCII85 encoded data!");
        case 2:
          nRest = (aBuffer[0] * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (aBuffer[1] * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (EIGHTY_FIVE * EIGHTY_FIVE) +
                  EIGHTY_FIVE;
          aBAOS.write ((byte) (nRest >> BIT3));
          break;
        case 3:
          nRest = (aBuffer[0] * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (aBuffer[1] * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (aBuffer[2] * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (EIGHTY_FIVE * EIGHTY_FIVE) +
                  EIGHTY_FIVE;
          aBAOS.write ((byte) (nRest >> BIT3));
          aBAOS.write ((byte) (nRest >> BIT2));
          break;
        case 4:
          nRest = (aBuffer[0] * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (aBuffer[1] * EIGHTY_FIVE * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (aBuffer[2] * EIGHTY_FIVE * EIGHTY_FIVE) +
                  (aBuffer[3] * EIGHTY_FIVE) +
                  EIGHTY_FIVE;
          aBAOS.write ((byte) (nRest >> BIT3));
          aBAOS.write ((byte) (nRest >> BIT2));
          aBAOS.write ((byte) (nRest >> BIT1));
          break;
        default:
          break;
      }
      return aBAOS.toByteArray ();
    }
    finally
    {
      StreamUtils.close (aBAOS);
    }
  }
}
