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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

abstract class AbstractCharsetTestUtil
{
  static void outToSB (final ByteBuffer out, final StringBuffer sb) throws UnsupportedEncodingException
  {
    out.flip ();
    sb.append (AbstractCharsetTestUtil.asString (out));
    out.clear ();
  }

  static String asString (final ByteBuffer buffer) throws UnsupportedEncodingException
  {
    final byte [] bytes = new byte [buffer.limit ()];
    buffer.get (bytes);
    return new String (bytes, "US-ASCII");
  }

  static ByteBuffer wrap (final String string) throws UnsupportedEncodingException
  {
    final byte [] bytes = string.getBytes ("US-ASCII");
    return ByteBuffer.wrap (bytes);
  }
}
