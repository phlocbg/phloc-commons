/* ====================================================================
 * Copyright (c) 2006 J.T. Beetstra
 *
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to 
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY 
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ====================================================================
 */
package com.phloc.charset.utf7;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;

/**
 * <p>
 * The character set specified in RFC 2152. Two variants are supported using the
 * encodeOptional constructor flag
 * </p>
 * 
 * @see <a href="http://tools.ietf.org/html/rfc2152">RFC 2152</a>
 * @author Jaap Beetstra
 */
final class UTF7Charset extends UTF7StyleCharset
{
  private static final String BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                                + "abcdefghijklmnopqrstuvwxyz"
                                                + "0123456789+/";
  private static final String SET_D = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'(),-./:?";
  private static final String SET_O = "!\"#$%&*;<=>@[]^_`{|}";
  private static final String RULE_3 = " \t\r\n";
  final String m_sDirectlyEncoded;

  UTF7Charset (@Nonnull @Nonempty final String sName, @Nullable final String [] aAliases, final boolean bIncludeOptional)
  {
    super (sName, aAliases, BASE64_ALPHABET, false);
    if (bIncludeOptional)
      m_sDirectlyEncoded = SET_D + SET_O + RULE_3;
    else
      m_sDirectlyEncoded = SET_D + RULE_3;
  }

  @Override
  boolean canEncodeDirectly (final char ch)
  {
    return m_sDirectlyEncoded.indexOf (ch) >= 0;
  }

  @Override
  byte shift ()
  {
    return '+';
  }

  @Override
  byte unshift ()
  {
    return '-';
  }
}
