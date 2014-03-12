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
package com.phloc.commons.charset.utf7;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;

/**
 * <p>
 * Abstract base class for UTF-7 style encoding and decoding.
 * </p>
 * 
 * @author Jaap Beetstra
 */
abstract class UTF7StyleCharset extends Charset
{
  private static final List <String> CONTAINED = ContainerHelper.newList ("US-ASCII",
                                                                          "ISO-8859-1",
                                                                          "UTF-8",
                                                                          "UTF-16",
                                                                          "UTF-16LE",
                                                                          "UTF-16BE");
  private final boolean m_bStrict;
  private final Base64Util m_aBase64;

  /**
   * <p>
   * Besides the name and aliases, two additional parameters are required. First
   * the base 64 alphabet used; in modified UTF-7 a slightly different alphabet
   * is used. Additionally, it should be specified if encoders and decoders
   * should be strict about the interpretation of malformed encoded sequences.
   * This is used since modified UTF-7 specifically disallows some constructs
   * which are allowed (or not specifically disallowed) in UTF-7 (RFC 2152).
   * </p>
   * 
   * @param sCanonicalName
   *        The name as defined in java.nio.charset.Charset
   * @param aAliases
   *        The aliases as defined in java.nio.charset.Charset
   * @param sAlphabet
   *        The base 64 alphabet used
   * @param bStrict
   *        True if strict handling of sequences is requested
   */
  protected UTF7StyleCharset (@Nonnull @Nonempty final String sCanonicalName,
                              @Nullable final String [] aAliases,
                              @Nonnull @Nonempty final String sAlphabet,
                              final boolean bStrict)
  {
    super (sCanonicalName, aAliases);
    m_aBase64 = new Base64Util (sAlphabet);
    m_bStrict = bStrict;
  }

  @Override
  public boolean contains (final Charset cs)
  {
    return CONTAINED.contains (cs.name ());
  }

  @Override
  public CharsetDecoder newDecoder ()
  {
    return new UTF7StyleCharsetDecoder (this, m_aBase64, m_bStrict);
  }

  @Override
  public CharsetEncoder newEncoder ()
  {
    return new UTF7StyleCharsetEncoder (this, m_aBase64, m_bStrict);
  }

  /**
   * Tells if a character can be encoded using simple (US-ASCII) encoding or
   * requires base 64 encoding.
   * 
   * @param ch
   *        The character
   * @return True if the character can be encoded directly, false otherwise
   */
  abstract boolean canEncodeDirectly (char ch);

  /**
   * Returns character used to switch to base 64 encoding.
   * 
   * @return The shift character
   */
  abstract byte shift ();

  /**
   * Returns character used to switch from base 64 encoding to simple encoding.
   * 
   * @return The unshift character
   */
  abstract byte unshift ();
}
