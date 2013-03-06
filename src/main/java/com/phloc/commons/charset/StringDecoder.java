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
package com.phloc.commons.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A special string decoder that can be used to convert a byte source to a
 * String in a certain charset. This class is not thread-safe!
 * 
 * @author philip
 */
@NotThreadSafe
public final class StringDecoder
{
  public static final int INITIAL_BUFFER_SIZE = 1024;
  private static final int SIZE_ALIGNMENT_BITS = 10; // = 1024
  private static final int SIZE_ALIGNMENT = 1 << SIZE_ALIGNMENT_BITS;
  private static final int SIZE_ALIGNMENT_MASK = (1 << SIZE_ALIGNMENT_BITS) - 1;

  private final CharsetDecoder m_aDecoder;
  private CharBuffer m_aBuffer = CharBuffer.allocate (INITIAL_BUFFER_SIZE);

  public StringDecoder (@Nonnull final Charset aCharset)
  {
    if (aCharset == null)
      throw new NullPointerException ("charset");

    m_aDecoder = aCharset.newDecoder ();
    // This matches the default behaviour for String(byte[], "UTF-8");
    // TODO: Support throwing exceptions on invalid input?
    m_aDecoder.onMalformedInput (CodingErrorAction.REPLACE);
  }

  /**
   * Reserve space for the next string that will be <= expectedLength characters
   * long. Must only be called when the buffer is empty.
   * 
   * @param nExpectedLength
   *        The number of chars to reserve. Must be &ge; 0.
   */
  public void reserve (@Nonnegative final int nExpectedLength)
  {
    if (nExpectedLength < 0)
      throw new IllegalArgumentException ("expectedLength cannot be negative (= " + nExpectedLength + ")");
    if (m_aBuffer.position () != 0)
      throw new IllegalStateException ("cannot be called except after finish()");

    if (nExpectedLength > m_aBuffer.capacity ())
    {
      // Allocate a temporary buffer large enough for this string rounded up
      int nDesiredLength = nExpectedLength;
      if ((nDesiredLength & SIZE_ALIGNMENT_MASK) != 0)
      {
        // round up
        nDesiredLength = (nExpectedLength + SIZE_ALIGNMENT) & ~SIZE_ALIGNMENT_MASK;
      }
      assert nDesiredLength % SIZE_ALIGNMENT == 0;

      m_aBuffer = CharBuffer.allocate (nDesiredLength);
    }
    assert m_aBuffer.position () == 0;
    assert nExpectedLength <= m_aBuffer.capacity ();
  }

  private void _decode (@Nonnull final ByteBuffer aByteBuffer, final boolean bEndOfInput)
  {
    // Call decode at least once to pass the endOfInput signal through
    do
    {
      final CoderResult aResult = m_aDecoder.decode (aByteBuffer, m_aBuffer, bEndOfInput);
      if (aResult != CoderResult.UNDERFLOW)
      {
        // Error handling
        if (aResult == CoderResult.OVERFLOW)
        {
          // double the buffer size and retry
          final CharBuffer aNewBuffer = CharBuffer.allocate (m_aBuffer.capacity () * 2);
          System.arraycopy (m_aBuffer.array (), 0, aNewBuffer.array (), 0, m_aBuffer.position ());
          aNewBuffer.position (m_aBuffer.position ());
          assert aNewBuffer.remaining () >= m_aBuffer.capacity ();
          m_aBuffer = aNewBuffer;
        }
        else
        {
          // We disable errors in the constructor (replace instead)
          assert false;
          // TODO: Are there any unmappable sequences for UTF-8?
          assert aResult.isMalformed ();
        }
      }
    } while (aByteBuffer.hasRemaining ());
    assert !aByteBuffer.hasRemaining ();
  }

  public void decode (@Nonnull final byte [] aBuf)
  {
    if (aBuf == null)
      throw new NullPointerException ("buf");

    _decode (ByteBuffer.wrap (aBuf, 0, aBuf.length), false);
  }

  public void decode (@Nonnull final byte [] aBuf, @Nonnegative final int nOfs, @Nonnegative final int nLen)
  {
    if (aBuf == null)
      throw new NullPointerException ("buf");
    if (nOfs < 0 || nLen < 0 || (nOfs + nLen) > aBuf.length)
      throw new IllegalArgumentException ("ofs:" + nOfs + ";len=" + nLen + ";bufLen=" + aBuf.length);

    _decode (ByteBuffer.wrap (aBuf, nOfs, nLen), false);
  }

  public void decode (@Nonnull final ByteBuffer aByteBuffer)
  {
    if (aByteBuffer == null)
      throw new NullPointerException ("byteBuffer");

    _decode (aByteBuffer, false);
  }

  @Nonnull
  public String finish (@Nonnull final byte [] aBuf, @Nonnegative final int nOfs, @Nonnegative final int nLen)
  {
    return finish (ByteBuffer.wrap (aBuf, nOfs, nLen));
  }

  @Nonnull
  public String finish (@Nonnull final ByteBuffer aByteBuffer)
  {
    if (aByteBuffer == null)
      throw new NullPointerException ("byteBuffer");

    _decode (aByteBuffer, true);

    final CoderResult aResult = m_aDecoder.flush (m_aBuffer);
    if (aResult == CoderResult.OVERFLOW)
      throw new RuntimeException ("TODO: Handle overflow?");
    if (aResult != CoderResult.UNDERFLOW)
      throw new RuntimeException ("TODO: Handle errors?");

    // Copy out the string
    final String sRet = new String (m_aBuffer.array (), 0, m_aBuffer.position ());

    // Reset for the next string
    m_aBuffer.clear ();
    m_aDecoder.reset ();

    return sRet;
  }
}
