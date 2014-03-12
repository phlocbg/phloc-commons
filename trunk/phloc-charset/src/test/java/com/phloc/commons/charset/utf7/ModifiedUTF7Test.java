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
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import org.junit.Before;
import org.junit.Test;

public class ModifiedUTF7Test extends AbstractCharsetTest
{
  @Before
  public void setUp () throws Exception
  {
    tested = new ModifiedUTF7Charset ("X-MODIFIED-UTF-7", new String [] {});
  }

  @Test
  public void testContains ()
  {
    assertTrue (tested.contains (Charset.forName ("US-ASCII")));
    assertTrue (tested.contains (Charset.forName ("ISO-8859-1")));
    assertTrue (tested.contains (Charset.forName ("UTF-8")));
    assertTrue (tested.contains (Charset.forName ("UTF-16")));
    assertTrue (tested.contains (Charset.forName ("UTF-16LE")));
    assertTrue (tested.contains (Charset.forName ("UTF-16BE")));
  }

  @Test
  public void testEmpty () throws Exception
  {
    final String string = "";
    assertEquals (string, decode (string));
    assertEquals (string, encode (string));
  }

  @Test
  public void testDecodeSimple () throws Exception
  {
    assertEquals ("abcdefghijklmnopqrstuvwxyz", decode ("abcdefghijklmnopqrstuvwxyz"));
    final String directly = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'(),-./:?\r\n";
    assertEquals (directly, decode (directly));
    final String optional = "!\"#$%*;<=>@[]^_'{|}";
    assertEquals (optional, decode (optional));
    final String nonUTF7 = "+\\~";
    assertEquals (nonUTF7, decode (nonUTF7));
  }

  @Test
  public void testDecodeComplex () throws Exception
  {
    assertEquals ("A\u2262\u0391.", decode ("A&ImIDkQ-."));
  }

  @Test
  public void testDecodeLong () throws Exception
  {
    assertEquals ("€áéúíóýäëïöüÿ", decode ("&IKwA4QDpAPoA7QDzAP0A5ADrAO8A9gD8AP8-"));
  }

  @Test
  public void testDecodeLimitedOutput () throws Exception
  {
    final CharsetDecoder decoder = tested.newDecoder ();
    final ByteBuffer in = AbstractCharsetTestUtil.wrap ("A&ImIDkQ-.");
    final CharBuffer out = CharBuffer.allocate (4);
    assertEquals (CoderResult.UNDERFLOW, decoder.decode (in, out, true));
    out.flip ();
    assertEquals ("A\u2262\u0391.", out.toString ());
  }

  @Test
  public void testDecodePerfectSized () throws Exception
  {
    assertEquals ("€áé", decode ("&IKwA4QDp-"));
  }

  @Test
  public void testDecodeShiftSequence () throws Exception
  {
    assertEquals ("&", decode ("&-"));
    assertEquals ("&-", decode ("&--"));
    assertEquals ("&&", decode ("&-&-"));
  }

  @Test
  public void testDecodeNoClosing () throws Exception
  {
    ByteBuffer in = AbstractCharsetTestUtil.wrap ("&");
    CharBuffer out = CharBuffer.allocate (1024);
    final CharsetDecoder decoder = tested.newDecoder ();
    CoderResult result = decoder.decode (in, out, true);
    assertEquals (CoderResult.UNDERFLOW, result);
    result = decoder.flush (out);
    assertEquals (CoderResult.malformedForLength (1), result);
    assertEquals (1, in.position ());
    assertEquals (0, out.position ());
    in = AbstractCharsetTestUtil.wrap ("&AO");
    out = CharBuffer.allocate (1024);
    decoder.reset ();
    result = decoder.decode (in, out, true);
    assertEquals (CoderResult.UNDERFLOW, result);
    result = decoder.flush (out);
    assertEquals (CoderResult.malformedForLength (1), result);
    assertEquals (3, in.position ());
    assertEquals (0, out.position ());
  }

  @Test
  public void testDecodeInvalidLength () throws Exception
  {
    assertMalformed ("&a-", "");
    assertMalformed ("ab&IKwD-", "ab€");
  }

  @Test
  public void testDecodeUnshiftShiftSequence () throws Exception
  {
    assertMalformed ("&ImIDkQ-&ImIDkQ-", "\u2262\u0391");
    assertEquals ("\u2262\u0391a\u2262\u0391", decode ("&ImIDkQ-a&ImIDkQ-"));
  }

  @Test
  public void testDecodeIllegalBase64char () throws Exception
  {
    assertMalformed ("&[-", "");
    assertMalformed ("&&ImIDkQ-", "");
  }

  @Test
  public void testLongBadDecode () throws Exception
  {
    assertMalformed ("&IKwA4QDpA-", "€áé");
    assertMalformed ("&IKwA4QDpA", "€áé");
    assertMalformed ("&IKwA4QDp", "€áé");
    assertMalformed ("&IKwA4QDpAP", "€áé");
    assertMalformed ("&IKwA4QDpAPoA7QDzAP0A5ADrAO8A9gD8AP-", "€áéúíóýäëïöü");
  }

  @Test
  public void testEncodeSimple () throws Exception
  {
    final String directly = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'(),-./:?";
    assertEquals (directly, encode (directly));
    final String optional = "!\"#$%*;<=>@[]^_'{|}";
    assertEquals (optional, encode (optional));
    final String nonUTF7 = "+\\~";
    assertEquals (nonUTF7, encode (nonUTF7));
  }

  @Test
  public void testEncodeComplex () throws Exception
  {
    assertEquals ("A&ImIDkQ-.", encode ("A\u2262\u0391."));
    assertEquals ("&AO0A4Q-", encode ("íá"));
  }

  @Test
  public void testEncodeLong () throws Exception
  {
    assertEquals ("&IKwA4QDpAPoA7QDzAP0A5ADrAO8A9gD8AP8-", encode ("€áéúíóýäëïöüÿ"));
  }

  @Test
  public void testEncodeAlphabet () throws Exception
  {
    assertEquals ("&AL4AvgC+-", encode ("¾¾¾"));
    assertEquals ("&AL8AvwC,-", encode ("¿¿¿"));
  }

  @Test
  public void testGetBytes () throws Exception
  {
    // simulate what is done in String.getBytes
    // (cannot be used directly since Charset is not installed while testing)
    final String string = "café";
    final CharsetEncoder encoder = tested.newEncoder ();
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
    assertEquals ("caf&AOk-", AbstractCharsetTestUtil.asString (bb));
  }

  protected void assertMalformed (final String s, final String stringOut) throws UnsupportedEncodingException
  {
    final ByteBuffer in = AbstractCharsetTestUtil.wrap (s);
    final CharsetDecoder testedDecoder = tested.newDecoder ();
    final CharBuffer out = CharBuffer.allocate (1024);
    CoderResult result = testedDecoder.decode (in, out, true);
    if (result.isUnderflow ())
      result = testedDecoder.flush (out);
    out.flip ();
    assertEquals (stringOut, out.toString ());
    assertTrue (result.isMalformed ());
  }
}
