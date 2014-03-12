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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import org.junit.Before;
import org.junit.Test;

public final class AcceptanceTest
{
  private CharsetProvider provider;
  private Charset charset;
  private CharsetDecoder decoder;
  private CharsetEncoder encoder;

  @Before
  public void setUp () throws Exception
  {
    provider = new CharsetProvider ();
  }

  private void _init (final String init)
  {
    charset = provider.charsetForName (init);
    decoder = charset.newDecoder ();
    encoder = charset.newEncoder ();
  }

  @Test
  public void testUTF7 () throws Exception
  {
    _init ("UTF-7");
    assertEquals ("A+ImIDkQ.", _encodeGetBytes ("A\u2262\u0391."));
    assertEquals ("A+ImIDkQ.", _encodeCharsetEncode ("A\u2262\u0391."));
    assertEquals ("+ACEAIgAj-", _encodeGetBytes ("!\"#"));
    _verifyAll ();
  }

  @Test
  public void testUTF7o () throws Exception
  {
    _init ("X-UTF-7-OPTIONAL");
    assertEquals ("A+ImIDkQ.", _encodeGetBytes ("A\u2262\u0391."));
    assertEquals ("A+ImIDkQ.", _encodeCharsetEncode ("A\u2262\u0391."));
    assertEquals ("!\"#", _encodeGetBytes ("!\"#"));
    _verifyAll ();
  }

  @Test
  public void testModifiedUTF7 () throws Exception
  {
    _init ("x-IMAP4-MODIFIED-UTF7");
    assertEquals ("A&ImIDkQ-.", _encodeGetBytes ("A\u2262\u0391."));
    assertEquals ("A&ImIDkQ-.", _encodeCharsetEncode ("A\u2262\u0391."));
    _verifyAll ();
  }

  private void _verifyAll () throws Exception
  {
    verifySymmetrical ("������������������������");
    verifySymmetrical ("a�b�c�d�e�f�g�h�i�j�k�l�m�n�o�p�q�r�s�t�u�v�w�x�y�z");
    verifySymmetrical ("abc���def���ghi���jkl���mno���pqr���stu���vwx���yz�");
    verifySymmetrical ("abcdefghijklmnopqrstuvwyxz������������������������abcdefghijklmnopqrstuvwyxz");
    verifySymmetrical ("a�b+�c�+-d�e-�f�-+g�h+�i�+-j�k-�l�-+m�n+�o�+-p�q-�r�-+s�t+�u�+-v�w�-x�y-+�z+");
    verifySymmetrical ("�+��+���+���++�++��++���+++�+++��+++���+++���");
    verifySymmetrical ("�+-��+-���+-���++-�++-��++-���+++-�+++-��+++-���+++-���");
    verifySymmetrical ("++++++++");
    verifySymmetrical ("+-++--+++---++");
    verifySymmetrical ("+���+");
    verifySymmetrical ("`~!@#$%^&*()_+-=[]\\{}|;':\",./<>?\u0000\r\n\t\b\f�");
    verifySymmetrical ("#a�a#�#��#���#");
  }

  protected void verifySymmetrical (final String s) throws Exception
  {
    final String encoded = _encodeGetBytes (s);
    assertEquals (encoded, _encodeCharsetEncode (s));
    assertEquals ("problem decoding " + encoded, s, decode (encoded));
    for (int i = 4; i < encoded.length (); i++)
    {
      final ByteBuffer in = AbstractCharsetTestUtil.wrap (encoded);
      decoder.reset ();
      verifyChunkedOutDecode (i, in, s);
    }
    for (int i = 10; i < encoded.length (); i++)
    {
      final CharBuffer in = CharBuffer.wrap (s);
      encoder.reset ();
      verifyChunkedOutEncode (i, in, encoded);
    }
    for (int i = 10; i < encoded.length (); i++)
    {
      decoder.reset ();
      verifyChunkedInDecode (i, encoded, s);
    }
    for (int i = 4; i < encoded.length (); i++)
    {
      encoder.reset ();
      verifyChunkedInEncode (i, s, encoded);
    }
  }

  private String _encodeCharsetEncode (final String string) throws UnsupportedEncodingException
  {
    final String charsetEncode = AbstractCharsetTestUtil.asString (charset.encode (string));
    return charsetEncode;
  }

  /*
   * simulate what is done in String.getBytes (cannot be used directly since
   * Charset is not installed while testing)
   */
  private String _encodeGetBytes (final String string) throws CharacterCodingException, UnsupportedEncodingException
  {
    final ByteBuffer bb = ByteBuffer.allocate ((int) (encoder.maxBytesPerChar () * string.length ()));
    final CharBuffer cb = CharBuffer.wrap (string);
    encoder.reset ();
    CoderResult cr = encoder.encode (cb, bb, true);
    if (!cr.isUnderflow ())
      cr.throwException ();
    cr = encoder.flush (bb);
    if (!cr.isUnderflow ())
      cr.throwException ();
    bb.flip ();
    final String stringGetBytes = AbstractCharsetTestUtil.asString (bb);
    return stringGetBytes;
  }

  protected String decode (final String string) throws UnsupportedEncodingException
  {
    final ByteBuffer buffer = AbstractCharsetTestUtil.wrap (string);
    final CharBuffer decoded = charset.decode (buffer);
    return decoded.toString ();
  }

  protected void verifyChunkedInDecode (final int i, final String encoded, final String decoded) throws UnsupportedEncodingException
  {
    final ByteBuffer in = ByteBuffer.allocate (i);
    final CharBuffer out = CharBuffer.allocate (decoded.length () + 5);
    int pos = 0;
    CoderResult result = CoderResult.UNDERFLOW;
    while (pos < encoded.length ())
    {
      final int end = Math.min (encoded.length (), pos + i);
      in.put (AbstractCharsetTestUtil.wrap (encoded.substring (pos + in.position (), end)));
      in.flip ();
      result = decoder.decode (in, out, false);
      assertEquals ("at position: " + pos, CoderResult.UNDERFLOW, result);
      assertTrue ("no progress after " + pos + " of " + encoded.length (), in.position () > 0);
      pos += in.position ();
      in.compact ();
    }
    in.limit (0);
    result = decoder.decode (in, out, true);
    assertEquals (CoderResult.UNDERFLOW, result);
    result = decoder.flush (out);
    assertEquals (CoderResult.UNDERFLOW, result);
    assertEquals (encoded.length (), pos);
    assertEquals (decoded.length (), out.position ());
    out.flip ();
    assertEquals ("for length: " + i, decoded, out.toString ());
  }

  protected void verifyChunkedInEncode (final int i, final String decoded, final String encoded) throws UnsupportedEncodingException
  {
    final CharBuffer in = CharBuffer.allocate (i);
    final ByteBuffer out = ByteBuffer.allocate (encoded.length () + 40);
    int pos = 0;
    CoderResult result = CoderResult.UNDERFLOW;
    while (pos < decoded.length ())
    {
      final int end = Math.min (decoded.length (), pos + i);
      in.put (decoded.substring (pos + in.position (), end));
      in.flip ();
      assertTrue ("unexpected end at " + pos, in.limit () > 0);
      result = encoder.encode (in, out, false);
      if (result.isUnderflow ())
        assertTrue ("no progress after " + pos + " of " + decoded.length () + " in " + decoded, in.position () > 0);
      pos += in.position ();
      in.compact ();
    }
    pos += in.position ();
    in.limit (0);
    result = encoder.encode (in, out, true);
    result = encoder.flush (out);
    assertEquals (CoderResult.UNDERFLOW, result);
    out.flip ();
    assertEquals ("for length: " + i, encoded, AbstractCharsetTestUtil.asString (out));
  }

  protected void verifyChunkedOutEncode (final int i, final CharBuffer in, final String encoded) throws UnsupportedEncodingException
  {
    final ByteBuffer out = ByteBuffer.allocate (i);
    int encodeCount = 0;
    final StringBuffer sb = new StringBuffer ();
    CoderResult result;
    while (in.hasRemaining ())
    {
      result = encoder.encode (in, out, false);
      encodeCount += out.position ();
      if (in.hasRemaining ())
      {
        assertEquals ("at position: " + encodeCount, CoderResult.OVERFLOW, result);
        assertTrue ("at position: " + encodeCount, out.position () > 0);
      }
      AbstractCharsetTestUtil.outToSB (out, sb);
    }
    result = encoder.encode (in, out, true);
    assertFalse (!result.isOverflow () && in.hasRemaining ());
    AbstractCharsetTestUtil.outToSB (out, sb);
    result = encoder.flush (out);
    AbstractCharsetTestUtil.outToSB (out, sb);
    assertEquals (encoded, sb.toString ());
    in.rewind ();
  }

  protected void verifyChunkedOutDecode (final int i, final ByteBuffer in, final String decoded)
  {
    final CharBuffer out = CharBuffer.allocate (i);
    int decodeCount = 0;
    final StringBuffer sb = new StringBuffer ();
    CoderResult result = CoderResult.OVERFLOW;
    while (decodeCount < decoded.length ())
    {
      assertEquals ("at position: " + decodeCount, CoderResult.OVERFLOW, result);
      result = decoder.decode (in, out, true);
      assertTrue (out.position () > 0);
      decodeCount += out.position ();
      out.flip ();
      sb.append (out.toString ());
      out.clear ();
    }
    assertEquals (decoded, sb.toString ());
    in.rewind ();
  }
}
