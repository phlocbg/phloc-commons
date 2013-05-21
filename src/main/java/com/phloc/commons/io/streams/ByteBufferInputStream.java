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
package com.phloc.commons.io.streams;

import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.lang.ByteBufferUtils;

/**
 * {@link InputStream} wrapped around a single {@link ByteBuffer}.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class ByteBufferInputStream extends InputStream
{
  private ByteBuffer m_aBuffer;

  /**
   * Constructor
   * 
   * @param aBuffer
   *        {@link ByteBuffer} to use. May not be <code>null</code>.
   */
  public ByteBufferInputStream (@Nonnull final ByteBuffer aBuffer)
  {
    if (aBuffer == null)
      throw new NullPointerException ("buffer");

    m_aBuffer = aBuffer;
  }

  private void _checkClosed ()
  {
    if (m_aBuffer == null)
      throw new IllegalStateException ("InputStream is already closed");
  }

  /**
   * @return The contained {@link ByteBuffer}. Handle with care! May be
   *         <code>null</code> if the stream is closed!
   */
  @Nullable
  public ByteBuffer getBuffer ()
  {
    _checkClosed ();
    return m_aBuffer;
  }

  public boolean isAnythingAvailable ()
  {
    _checkClosed ();
    return m_aBuffer.hasRemaining ();
  }

  @Override
  @Nonnegative
  public int available ()
  {
    _checkClosed ();
    return m_aBuffer.remaining ();
  }

  @Override
  public void close ()
  {
    m_aBuffer = null;
  }

  @SuppressWarnings ("sync-override")
  @Override
  public void mark (final int nReadlimit)
  {
    _checkClosed ();
    m_aBuffer.mark ();
  }

  @SuppressWarnings ("sync-override")
  @Override
  public void reset ()
  {
    _checkClosed ();
    m_aBuffer.reset ();
  }

  @Override
  public boolean markSupported ()
  {
    return true;
  }

  @Override
  public int read ()
  {
    if (m_aBuffer.hasRemaining ())
      return m_aBuffer.get () & 0xff;
    return -1;
  }

  @Override
  public int read (@Nonnull final byte [] aBuf)
  {
    return read (aBuf, 0, aBuf.length);
  }

  @Override
  public int read (@Nonnull final byte [] aBuf, @Nonnegative final int nOfs, @Nonnegative final int nLen)
  {
    if (aBuf == null)
      throw new NullPointerException ("buffer");
    if (nOfs < 0 || nLen < 0 || (nOfs + nLen) > aBuf.length)
      throw new IllegalArgumentException ("ofs:" + nOfs + ";len=" + nLen + ";bufLen=" + aBuf.length);

    if (!m_aBuffer.hasRemaining ())
      return -1;
    if (nLen == 0 || aBuf.length == 0)
      return isAnythingAvailable () ? 0 : -1;

    final int nMaxRead = Math.min (m_aBuffer.remaining (), nLen);
    m_aBuffer.get (aBuf, nOfs, nMaxRead);
    return nMaxRead;
  }

  @Override
  @Nonnegative
  public long skip (final long nBytesToSkip)
  {
    final long nSkip = Math.min (m_aBuffer.remaining (), nBytesToSkip);
    m_aBuffer.position (m_aBuffer.position () + (int) nSkip);
    return nSkip;
  }

  /**
   * Reads as much as possible into the destination buffer.
   * 
   * @param aDestByteBuffer
   *        The destination byte buffer to use. May not be <code>null</code>.
   * @return The number of bytes read. Always &ge; 0.
   */
  @Nonnegative
  public long read (@Nonnull final ByteBuffer aDestByteBuffer)
  {
    if (aDestByteBuffer == null)
      throw new NullPointerException ("destByteBuffer");

    long nBytesRead = 0;
    if (m_aBuffer.hasRemaining ())
      nBytesRead += ByteBufferUtils.transfer (m_aBuffer, aDestByteBuffer, false);
    return nBytesRead;
  }
}
