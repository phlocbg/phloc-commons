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

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;

/**
 * <p>
 * Charset service-provider class used for both variants of the UTF-7 charset
 * and the modified-UTF-7 charset.
 * </p>
 * 
 * @author Jaap Beetstra
 */
public class CharsetProvider extends java.nio.charset.spi.CharsetProvider
{
  private static final String UTF7_NAME = "UTF-7";
  private static final String UTF7_O_NAME = "X-UTF-7-OPTIONAL";
  private static final String UTF7_M_NAME = "X-MODIFIED-UTF-7";
  private static final String [] UTF7_ALIASES = new String [] { "UNICODE-1-1-UTF-7",
                                                               "CSUNICODE11UTF7",
                                                               "X-RFC2152",
                                                               "X-RFC-2152" };
  private static final String [] UTF7_O_ALIASES = new String [] { "X-RFC2152-OPTIONAL", "X-RFC-2152-OPTIONAL" };
  private static final String [] UTF7_M_ALIASES = new String [] { "X-IMAP-MODIFIED-UTF-7",
                                                                 "X-IMAP4-MODIFIED-UTF7",
                                                                 "X-IMAP4-MODIFIED-UTF-7",
                                                                 "X-RFC3501",
                                                                 "X-RFC-3501" };
  private static final Charset s_aUtf7charset = new UTF7Charset (UTF7_NAME, UTF7_ALIASES, false);
  private static final Charset s_aUtf7oCharset = new UTF7Charset (UTF7_O_NAME, UTF7_O_ALIASES, true);
  private static final Charset s_aImap4charset = new ModifiedUTF7Charset (UTF7_M_NAME, UTF7_M_ALIASES);

  private final List <Charset> m_aCharsets;

  public CharsetProvider ()
  {
    m_aCharsets = ContainerHelper.newList (s_aUtf7charset, s_aImap4charset, s_aUtf7oCharset);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public Charset charsetForName (@Nonnull final String sCharsetName)
  {
    final String sRealCharsetName = sCharsetName.toUpperCase (Locale.US);
    for (final Charset aCharset : m_aCharsets)
      if (aCharset.name ().equals (sRealCharsetName))
        return aCharset;
    for (final Charset aCharset : m_aCharsets)
      if (aCharset.aliases ().contains (sRealCharsetName))
        return aCharset;
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator <Charset> charsets ()
  {
    return m_aCharsets.iterator ();
  }
}
