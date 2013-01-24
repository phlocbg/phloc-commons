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

import com.phloc.commons.CGlobal;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;

public final class ASCIIHexCodec implements IByteArrayDecoder
{
  public ASCIIHexCodec ()
  {}

  @Nullable
  public byte [] decode (@Nullable final byte [] aEncodedBuffer)
  {
    if (aEncodedBuffer == null)
      return null;

    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    try
    {
      boolean bFirstByte = true;
      int nFirstByte = 0;
      for (final byte nEncByte : aEncodedBuffer)
      {
        if (nEncByte == '>')
          break;

        // Ignore whitespaces
        if (Character.isWhitespace (nEncByte))
          continue;

        final byte nDecByte = (byte) StringHelper.getHexValue ((char) nEncByte);
        if (nDecByte == CGlobal.ILLEGAL_UINT)
          throw new DecoderException ("Failed to convert byte '" +
                                      nEncByte +
                                      "/" +
                                      ((char) nEncByte) +
                                      "' to hex value in ASCIIHexDecode");
        if (bFirstByte)
          nFirstByte = nDecByte;
        else
          aBAOS.write ((byte) (nFirstByte << 4 | nDecByte));
        bFirstByte = !bFirstByte;
      }

      // Write trailing byte
      if (!bFirstByte)
        aBAOS.write ((byte) (nFirstByte << 4));
      return aBAOS.toByteArray ();
    }
    finally
    {
      StreamUtils.close (aBAOS);
    }
  }
}
