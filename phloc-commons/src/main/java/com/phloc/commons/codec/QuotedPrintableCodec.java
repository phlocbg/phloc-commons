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

import java.nio.charset.Charset;
import java.util.BitSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.string.StringHelper;

/**
 * Encoder and decoder for quoted printable stuff
 * 
 * @author Philip Helger
 */
public class QuotedPrintableCodec extends AbstractCodec implements IStringCodec
{
  private static final byte ESCAPE_CHAR = '=';
  private static final byte TAB = 9;
  private static final byte SPACE = 32;

  /**
   * BitSet of printable characters as defined in RFC 1521.
   */
  private static final BitSet PRINTABLE_CHARS = new BitSet (256);

  static
  {
    PRINTABLE_CHARS.set (TAB);
    PRINTABLE_CHARS.set (SPACE);
    for (int i = 33; i <= 126; i++)
      if (i != ESCAPE_CHAR)
        PRINTABLE_CHARS.set (i);
  }

  /**
   * The default charset used for string decoding and encoding.
   */
  private final Charset m_aCharset;

  /**
   * Default constructor with the UTF-8 charset.
   */
  public QuotedPrintableCodec ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  /**
   * Constructor which allows for the selection of a default charset
   * 
   * @param aCharset
   *        the default string charset to use.
   */
  public QuotedPrintableCodec (@Nonnull final Charset aCharset)
  {
    if (aCharset == null)
      throw new NullPointerException ("Charset");
    m_aCharset = aCharset;
  }

  /**
   * Encodes byte into its quoted-printable representation.
   * 
   * @param b
   *        byte to encode
   * @param aBAOS
   *        the buffer to write to
   */
  public static final void encodeQuotedPrintable (final int b, final NonBlockingByteArrayOutputStream aBAOS)
  {
    final char hex1 = Character.toUpperCase (Character.forDigit ((b >> 4) & 0xF, 16));
    final char hex2 = Character.toUpperCase (Character.forDigit (b & 0xF, 16));
    aBAOS.write (ESCAPE_CHAR);
    aBAOS.write (hex1);
    aBAOS.write (hex2);
  }

  public static byte [] encodeQuotedPrintable (@Nonnull final BitSet aBitSet, @Nonnull final byte [] aDecodedBuffer)
  {
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream (aDecodedBuffer.length * 2);
    for (final byte nByte : aDecodedBuffer)
    {
      int b = nByte;
      if (b < 0)
        b = 256 + b;
      if (aBitSet.get (b))
        aBAOS.write (b);
      else
        encodeQuotedPrintable (b, aBAOS);
    }
    return aBAOS.toByteArray ();
  }

  @Nullable
  public byte [] encode (@Nullable final byte [] aDecodedBuffer)
  {
    if (aDecodedBuffer == null)
      return null;

    return encodeQuotedPrintable (PRINTABLE_CHARS, aDecodedBuffer);
  }

  public int getHexValue (final byte b)
  {
    final int ret = StringHelper.getHexValue ((char) b);
    if (ret < 0)
      throw new DecoderException ("Failed to convert " + b + " to a valid hex value");
    return ret;
  }

  @Nullable
  public static byte [] decodeQuotedPrintable (@Nullable final byte [] aEncodedBuffer)
  {
    if (aEncodedBuffer == null)
      return null;

    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    for (int i = 0; i < aEncodedBuffer.length; i++)
    {
      final int b = aEncodedBuffer[i];
      if (b == ESCAPE_CHAR)
      {
        final char cHigh = (char) aEncodedBuffer[++i];
        final char cLow = (char) aEncodedBuffer[++i];
        final int nDecodedValue = StringHelper.getHexByte (cHigh, cLow);
        if (nDecodedValue < 0)
          throw new DecoderException ("Invalid quoted-printable encoding for " + cHigh + cLow);

        aBAOS.write (nDecodedValue);
      }
      else
      {
        aBAOS.write (b);
      }
    }
    return aBAOS.toByteArray ();
  }

  @Nullable
  public byte [] decode (@Nullable final byte [] aEncodedBuffer)
  {
    return decodeQuotedPrintable (aEncodedBuffer);
  }

  @Nullable
  public String encodeText (@Nullable final String sDecoded)
  {
    return encodeText (PRINTABLE_CHARS, sDecoded, m_aCharset);
  }

  @Nullable
  public String encodeText (@Nonnull final BitSet aBitSet, @Nullable final String sDecoded)
  {
    return encodeText (aBitSet, sDecoded, m_aCharset);
  }

  @Nullable
  public static String encodeText (@Nonnull final BitSet aBitSet,
                                   @Nullable final String sDecoded,
                                   @Nonnull final Charset aCharset)
  {
    if (sDecoded == null)
      return null;

    final byte [] aEncodedData = encodeQuotedPrintable (aBitSet, CharsetManager.getAsBytes (sDecoded, aCharset));
    return CharsetManager.getAsString (aEncodedData, CCharset.CHARSET_US_ASCII_OBJ);
  }

  @Nullable
  public String decodeText (@Nullable final String sEncoded)
  {
    return decodeText (sEncoded, m_aCharset);
  }

  @Nullable
  public static String decodeText (@Nullable final String sEncoded, @Nonnull final Charset aCharset)
  {
    if (sEncoded == null)
      return null;
    byte [] aData = CharsetManager.getAsBytes (sEncoded, CCharset.CHARSET_US_ASCII_OBJ);
    aData = decodeQuotedPrintable (aData);
    return CharsetManager.getAsString (aData, aCharset);
  }
}
