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
 * The character set specified in RFC 3501 to use for IMAP4rev1 mailbox name
 * encoding.
 * </p>
 * 
 * @see <a href="http://tools.ietf.org/html/rfc3501">RFC 3501</a>
 * @author Jaap Beetstra
 */
final class ModifiedUTF7Charset extends UTF7StyleCharset
{
  private static final String MODIFIED_BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                                         + "abcdefghijklmnopqrstuvwxyz"
                                                         + "0123456789+,";

  ModifiedUTF7Charset (@Nonnull @Nonempty final String sName, @Nullable final String [] aAliases)
  {
    super (sName, aAliases, MODIFIED_BASE64_ALPHABET, true);
  }

  @Override
  boolean canEncodeDirectly (final char ch)
  {
    if (ch == shift ())
      return false;
    return ch >= 0x20 && ch <= 0x7E;
  }

  @Override
  byte shift ()
  {
    return '&';
  }

  @Override
  byte unshift ()
  {
    return '-';
  }
}
