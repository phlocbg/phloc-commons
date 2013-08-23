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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.base64.Base64;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;

/**
 * Identical to the Base64 encoding defined by <a
 * href="http://www.ietf.org/rfc/rfc1521.txt">RFC 1521</a> and allows a
 * character set to be specified.
 * <p>
 * <a href="http://www.ietf.org/rfc/rfc1522.txt">RFC 1522</a> describes
 * techniques to allow the encoding of non-ASCII text in various portions of a
 * RFC 822 [2] message header, in a manner which is unlikely to confuse existing
 * message handling software.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc1522.txt">MIME (Multipurpose
 *      Internet Mail Extensions) Part Two: Message Header Extensions for
 *      Non-ASCII Text</a>
 */
public class RFC1522BCodec extends AbstractRFC1522Codec
{
  /**
   * The default charset used for string decoding and encoding.
   */
  private final Charset m_aCharset;

  /**
   * Default constructor with the UTF-8 charset.
   */
  public RFC1522BCodec ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  /**
   * Constructor which allows for the selection of a default charset
   * 
   * @param aCharset
   *        the default string charset to use.
   */
  public RFC1522BCodec (@Nonnull final Charset aCharset)
  {
    if (aCharset == null)
      throw new NullPointerException ("Charset");
    m_aCharset = aCharset;
  }

  @Override
  protected String getRFC1522Encoding ()
  {
    return "B";
  }

  @Nonnull
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  @Override
  @Nonnull
  public byte [] encode (@Nonnull final byte [] aDecodedBuffer)
  {
    return aDecodedBuffer == null ? null : Base64.encodeBytesToBytes (aDecodedBuffer);
  }

  @Override
  @Nonnull
  public byte [] decode (@Nonnull final byte [] aEncodedBuffer)
  {
    return aEncodedBuffer == null ? null : Base64Helper.safeDecode (aEncodedBuffer);
  }

  /**
   * Encodes a string into its Base64 form using the default charset. Unsafe
   * characters are escaped.
   * 
   * @param sText
   *        string to convert to Base64 form
   * @return Base64 string
   * @throws EncoderException
   *         thrown if a failure condition is encountered during the encoding
   *         process.
   */
  @Nullable
  public String encodeText (@Nullable final String sText) throws EncoderException
  {
    return super.encodeText (sText, getCharset ());
  }

  /**
   * Decodes a Base64 string into its original form. Escaped characters are
   * converted back to their original representation.
   * 
   * @param sText
   *        Base64 string to convert into its original form
   * @return original string
   * @throws DecoderException
   *         A decoder exception is thrown if a failure condition is encountered
   *         during the decode process.
   */
  @Override
  @Nullable
  public String decodeText (@Nullable final String sText) throws DecoderException
  {
    return super.decodeText (sText);
  }
}
