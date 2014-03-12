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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * The CharsetDecoder used to decode both variants of the UTF-7 charset and the
 * modified-UTF-7 charset.
 * </p>
 * 
 * @author Jaap Beetstra
 */
final class UTF7StyleCharsetDecoder extends CharsetDecoder
{
  private final Base64Util m_aBase64;
  private final byte m_nShift;
  private final byte m_nUnshift;
  private final boolean m_bStrict;
  private boolean m_bBase64mode;
  private int m_nBitsRead;
  private int m_nTempChar;
  private boolean m_bJustShifted;
  private boolean m_bJustUnshifted;

  UTF7StyleCharsetDecoder (final UTF7StyleCharset aCharset, final Base64Util aBase64, final boolean bStrict)
  {
    super (aCharset, 0.6f, 1.0f);
    m_aBase64 = aBase64;
    m_bStrict = bStrict;
    m_nShift = aCharset.shift ();
    m_nUnshift = aCharset.unshift ();
  }

  @Override
  protected CoderResult decodeLoop (final ByteBuffer in, final CharBuffer out)
  {
    while (in.hasRemaining ())
    {
      final byte b = in.get ();
      if (m_bBase64mode)
      {
        if (b == m_nUnshift)
        {
          if (_base64bitsWaiting ())
            return _malformed (in);
          if (m_bJustShifted)
          {
            if (!out.hasRemaining ())
              return _overflow (in);
            out.put ((char) m_nShift);
          }
          else
            m_bJustUnshifted = true;
          _setUnshifted ();
        }
        else
        {
          if (!out.hasRemaining ())
            return _overflow (in);
          final CoderResult result = _handleBase64 (in, out, b);
          if (result != null)
            return result;
        }
        m_bJustShifted = false;
      }
      else
      {
        if (b == m_nShift)
        {
          m_bBase64mode = true;
          if (m_bJustUnshifted && m_bStrict)
            return _malformed (in);
          m_bJustShifted = true;
          continue;
        }
        if (!out.hasRemaining ())
          return _overflow (in);
        out.put ((char) b);
        m_bJustUnshifted = false;
      }
    }
    return CoderResult.UNDERFLOW;
  }

  @Nonnull
  private CoderResult _overflow (@Nonnull final ByteBuffer in)
  {
    in.position (in.position () - 1);
    return CoderResult.OVERFLOW;
  }

  /**
   * <p>
   * Decodes a byte in <i>base 64 mode</i>. Will directly write a character to
   * the output buffer if completed.
   * </p>
   * 
   * @param in
   *        The input buffer
   * @param out
   *        The output buffer
   * @param lastRead
   *        Last byte read from the input buffer
   * @return CoderResult.malformed if a non-base 64 character was encountered in
   *         strict mode, null otherwise
   */
  @Nullable
  private CoderResult _handleBase64 (final ByteBuffer in, final CharBuffer out, final byte lastRead)
  {
    CoderResult result = null;
    final int sextet = m_aBase64.getSextet (lastRead);
    if (sextet >= 0)
    {
      m_nBitsRead += 6;
      if (m_nBitsRead < 16)
      {
        m_nTempChar += sextet << (16 - m_nBitsRead);
      }
      else
      {
        m_nBitsRead -= 16;
        m_nTempChar += sextet >> (m_nBitsRead);
        out.put ((char) m_nTempChar);
        m_nTempChar = (sextet << (16 - m_nBitsRead)) & 0xFFFF;
      }
    }
    else
    {
      if (m_bStrict)
        return _malformed (in);
      out.put ((char) lastRead);
      if (_base64bitsWaiting ())
        result = _malformed (in);
      _setUnshifted ();
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.nio.charset.CharsetDecoder#implFlush(java.nio.CharBuffer)
   */
  @Override
  protected CoderResult implFlush (final CharBuffer out)
  {
    if ((m_bBase64mode && m_bStrict) || _base64bitsWaiting ())
      return CoderResult.malformedForLength (1);
    return CoderResult.UNDERFLOW;
  }

  /*
   * (non-Javadoc)
   * @see java.nio.charset.CharsetDecoder#implReset()
   */
  @Override
  protected void implReset ()
  {
    _setUnshifted ();
    m_bJustUnshifted = false;
  }

  /**
   * <p>
   * Resets the input buffer position to just before the last byte read, and
   * returns a result indicating to skip the last byte.
   * </p>
   * 
   * @param in
   *        The input buffer
   * @return CoderResult.malformedForLength(1);
   */
  private CoderResult _malformed (final ByteBuffer in)
  {
    in.position (in.position () - 1);
    return CoderResult.malformedForLength (1);
  }

  /**
   * @return True if there are base64 encoded characters waiting to be written
   */
  private boolean _base64bitsWaiting ()
  {
    return m_nTempChar != 0 || m_nBitsRead >= 6;
  }

  /**
   * <p>
   * Updates internal state to reflect the decoder is no longer in <i>base 64
   * mode</i>
   * </p>
   */
  private void _setUnshifted ()
  {
    m_bBase64mode = false;
    m_nBitsRead = 0;
    m_nTempChar = 0;
  }
}
