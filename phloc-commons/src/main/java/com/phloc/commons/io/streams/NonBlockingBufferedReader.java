/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.io.IOException;
import java.io.Reader;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;

/**
 * A non-synchronized copy of the class {@link java.io.BufferedReader}.
 * 
 * @author Philip Helger
 * @see java.io.BufferedReader
 */
public class NonBlockingBufferedReader extends Reader
{
  private static final int INVALIDATED = -2;
  private static final int UNMARKED = -1;

  private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
  private static final int DEFAULT_EXPECTED_LINE_LENGTH = 80;

  private Reader m_aReader;
  private char [] m_aBuf;
  private int m_nChars;
  private int m_nNextCharIndex;

  private int m_nMarkedChar = UNMARKED;
  /** Valid only when markedChar > 0 */
  private int m_nReadAheadLimit = 0;

  /** If the next character is a line feed, skip it */
  private boolean m_bSkipLF = false;

  /** The skipLF flag when the mark was set */
  private boolean m_bMarkedSkipLF = false;

  /**
   * Creates a buffering character-input stream that uses a default-sized input
   * buffer.
   * 
   * @param aReader
   *        A Reader
   */
  public NonBlockingBufferedReader (@Nonnull final Reader aReader)
  {
    this (aReader, DEFAULT_CHAR_BUFFER_SIZE);
  }

  /**
   * Creates a buffering character-input stream that uses an input buffer of the
   * specified size.
   * 
   * @param aReader
   *        A Reader
   * @param nSize
   *        Input-buffer size
   * @exception IllegalArgumentException
   *            If size is &lt;= 0
   */
  public NonBlockingBufferedReader (@Nonnull final Reader aReader, @Nonnegative final int nSize)
  {
    super (aReader);
    ValueEnforcer.isGT0 (nSize, "Size");
    this.m_aReader = aReader;
    this.m_aBuf = new char [nSize];
  }

  /**
   * Checks to make sure that the stream has not been closed
   * 
   * @throws IOException
   *         If the reader is not open
   */
  private void _ensureOpen () throws IOException
  {
    if (this.m_aReader == null)
      throw new IOException ("Stream closed");
  }

  /**
   * Fills the input buffer, taking the mark into account if it is valid.
   * 
   * @throws IOException
   *         In case reading fails
   */
  private void _fill () throws IOException
  {
    int nDstOfs = 0;
    if (this.m_nMarkedChar > UNMARKED)
    {
      // Marked
      final int nDelta = this.m_nNextCharIndex - this.m_nMarkedChar;
      if (nDelta >= this.m_nReadAheadLimit)
      {
        // Gone past read-ahead limit: Invalidate mark
        this.m_nMarkedChar = INVALIDATED;
        this.m_nReadAheadLimit = 0;
      }
      else
      {
        if (this.m_nReadAheadLimit <= this.m_aBuf.length)
        {
          // Shuffle in the current buffer
          System.arraycopy (this.m_aBuf, this.m_nMarkedChar, this.m_aBuf, 0, nDelta);
          this.m_nMarkedChar = 0;
          nDstOfs = nDelta;
        }
        else
        {
          // Reallocate buffer to accommodate read-ahead limit
          final char [] aNewBuf = new char [this.m_nReadAheadLimit];
          System.arraycopy (this.m_aBuf, this.m_nMarkedChar, aNewBuf, 0, nDelta);
          this.m_aBuf = aNewBuf;
          this.m_nMarkedChar = 0;
          nDstOfs = nDelta;
        }
        this.m_nNextCharIndex = nDelta;
        this.m_nChars = nDelta;
      }
    }

    int nBytesRead;
    do
    {
      nBytesRead = this.m_aReader.read (this.m_aBuf, nDstOfs, this.m_aBuf.length - nDstOfs);
    } while (nBytesRead == 0);
    if (nBytesRead > 0)
    {
      this.m_nChars = nDstOfs + nBytesRead;
      this.m_nNextCharIndex = nDstOfs;
    }
  }

  /**
   * Reads a single character.
   * 
   * @return The character read, as an integer in the range 0 to 65535 (
   *         <tt>0x00-0xffff</tt>), or -1 if the end of the stream has been
   *         reached
   * @exception IOException
   *            If an I/O error occurs
   */
  @Override
  public int read () throws IOException
  {
    _ensureOpen ();
    while (true)
    {
      if (this.m_nNextCharIndex >= this.m_nChars)
      {
        _fill ();
        if (this.m_nNextCharIndex >= this.m_nChars)
          return -1;
      }
      if (this.m_bSkipLF)
      {
        this.m_bSkipLF = false;
        if (this.m_aBuf[this.m_nNextCharIndex] == '\n')
        {
          this.m_nNextCharIndex++;
          continue;
        }
      }
      return this.m_aBuf[this.m_nNextCharIndex++];
    }
  }

  /**
   * Reads characters into a portion of an array, reading from the underlying
   * stream if necessary.
   * 
   * @param aBuf
   *        The buffer to be filled
   * @param nOfs
   *        The offset to start reading
   * @param nLen
   *        The number of bytes to read
   * @return The number of bytes read
   * @throws IOException
   *         in case reading fails
   */
  private int _internalRead (final char [] aBuf, final int nOfs, final int nLen) throws IOException
  {
    if (this.m_nNextCharIndex >= this.m_nChars)
    {
      /*
       * If the requested length is at least as large as the buffer, and if
       * there is no mark/reset activity, and if line feeds are not being
       * skipped, do not bother to copy the characters into the local buffer. In
       * this way buffered streams will cascade harmlessly.
       */
      if (nLen >= this.m_aBuf.length && this.m_nMarkedChar <= UNMARKED && !this.m_bSkipLF)
        return this.m_aReader.read (aBuf, nOfs, nLen);
      _fill ();
    }
    if (this.m_nNextCharIndex >= this.m_nChars)
      return -1;
    if (this.m_bSkipLF)
    {
      this.m_bSkipLF = false;
      if (this.m_aBuf[this.m_nNextCharIndex] == '\n')
      {
        this.m_nNextCharIndex++;
        if (this.m_nNextCharIndex >= this.m_nChars)
          _fill ();
        if (this.m_nNextCharIndex >= this.m_nChars)
          return -1;
      }
    }
    final int nBytesRead = Math.min (nLen, this.m_nChars - this.m_nNextCharIndex);
    System.arraycopy (this.m_aBuf, this.m_nNextCharIndex, aBuf, nOfs, nBytesRead);
    this.m_nNextCharIndex += nBytesRead;
    return nBytesRead;
  }

  /**
   * Reads characters into a portion of an array.
   * <p>
   * This method implements the general contract of the corresponding
   * <code>{@link Reader#read(char[], int, int) read}</code> method of the
   * <code>{@link Reader}</code> class. As an additional convenience, it
   * attempts to read as many characters as possible by repeatedly invoking the
   * <code>read</code> method of the underlying stream. This iterated
   * <code>read</code> continues until one of the following conditions becomes
   * true:
   * <ul>
   * <li>The specified number of characters have been read,
   * <li>The <code>read</code> method of the underlying stream returns
   * <code>-1</code>, indicating end-of-file, or
   * <li>The <code>ready</code> method of the underlying stream returns
   * <code>false</code>, indicating that further input requests would block.
   * </ul>
   * If the first <code>read</code> on the underlying stream returns
   * <code>-1</code> to indicate end-of-file then this method returns
   * <code>-1</code>. Otherwise this method returns the number of characters
   * actually read.
   * <p>
   * Subclasses of this class are encouraged, but not required, to attempt to
   * read as many characters as possible in the same fashion.
   * <p>
   * Ordinarily this method takes characters from this stream's character
   * buffer, filling it from the underlying stream as necessary. If, however,
   * the buffer is empty, the mark is not valid, and the requested length is at
   * least as large as the buffer, then this method will read characters
   * directly from the underlying stream into the given array. Thus redundant
   * <code>NonBlockingBufferedReader</code>s will not copy data unnecessarily.
   * 
   * @param cbuf
   *        Destination buffer
   * @param nOfs
   *        Offset at which to start storing characters
   * @param nLen
   *        Maximum number of characters to read
   * @return The number of characters read, or -1 if the end of the stream has
   *         been reached
   * @exception IOException
   *            If an I/O error occurs
   */
  @Override
  public int read (final char [] cbuf, final int nOfs, final int nLen) throws IOException
  {
    _ensureOpen ();
    ValueEnforcer.isArrayOfsLen (cbuf, nOfs, nLen);
    if (nLen == 0)
      return 0;

    // Main read
    int n = _internalRead (cbuf, nOfs, nLen);
    if (n <= 0)
      return n;
    while (n < nLen && this.m_aReader.ready ())
    {
      final int n1 = _internalRead (cbuf, nOfs + n, nLen - n);
      if (n1 <= 0)
        break;
      n += n1;
    }
    return n;
  }

  /**
   * Reads a line of text. A line is considered to be terminated by any one of a
   * line feed ('\n'), a carriage return ('\r'), or a carriage return followed
   * immediately by a linefeed.
   * 
   * @return A String containing the contents of the line, not including any
   *         line-termination characters, or null if the end of the stream has
   *         been reached
   * @exception IOException
   *            If an I/O error occurs
   */
  @Nullable
  public String readLine () throws IOException
  {
    StringBuilder aSB = null;
    int nStartChar;

    _ensureOpen ();
    boolean bOmitLF = this.m_bSkipLF;

    while (true)
    {
      if (this.m_nNextCharIndex >= this.m_nChars)
        _fill ();
      if (this.m_nNextCharIndex >= this.m_nChars)
      {
        /* EOF */
        if (aSB != null && aSB.length () > 0)
          return aSB.toString ();
        return null;
      }
      boolean bEOL = false;
      char cLast = 0;
      int nIndex;

      /* Skip a leftover '\n', if necessary */
      if (bOmitLF && this.m_aBuf[this.m_nNextCharIndex] == '\n')
        this.m_nNextCharIndex++;
      this.m_bSkipLF = false;
      bOmitLF = false;

      for (nIndex = this.m_nNextCharIndex; nIndex < this.m_nChars; nIndex++)
      {
        cLast = this.m_aBuf[nIndex];
        if (cLast == '\n' || cLast == '\r')
        {
          bEOL = true;
          break;
        }
      }

      nStartChar = this.m_nNextCharIndex;
      this.m_nNextCharIndex = nIndex;

      if (bEOL)
      {
        String sStr;
        if (aSB == null)
          sStr = new String (this.m_aBuf, nStartChar, nIndex - nStartChar);
        else
        {
          aSB.append (this.m_aBuf, nStartChar, nIndex - nStartChar);
          sStr = aSB.toString ();
        }
        this.m_nNextCharIndex++;
        if (cLast == '\r')
          this.m_bSkipLF = true;
        return sStr;
      }

      if (aSB == null)
        aSB = new StringBuilder (DEFAULT_EXPECTED_LINE_LENGTH);
      aSB.append (this.m_aBuf, nStartChar, nIndex - nStartChar);
    }
  }

  /**
   * Skips characters.
   * 
   * @param nBytes
   *        The number of characters to skip
   * @return The number of characters actually skipped
   * @exception IllegalArgumentException
   *            If <code>n</code> is negative.
   * @exception IOException
   *            If an I/O error occurs
   */
  @Override
  public long skip (final long nBytes) throws IOException
  {
    ValueEnforcer.isGE0 (nBytes, "Bytes");

    _ensureOpen ();
    long nRest = nBytes;
    while (nRest > 0)
    {
      if (this.m_nNextCharIndex >= this.m_nChars)
        _fill ();
      if (this.m_nNextCharIndex >= this.m_nChars)
      {
        // EOF
        break;
      }
      if (this.m_bSkipLF)
      {
        this.m_bSkipLF = false;
        if (this.m_aBuf[this.m_nNextCharIndex] == '\n')
          this.m_nNextCharIndex++;
      }
      final int d = this.m_nChars - this.m_nNextCharIndex;
      if (nRest <= d)
      {
        this.m_nNextCharIndex += nRest;
        nRest = 0;
        break;
      }
      nRest -= d;
      this.m_nNextCharIndex = this.m_nChars;
    }
    return nBytes - nRest;
  }

  /**
   * Tells whether this stream is ready to be read. A buffered character stream
   * is ready if the buffer is not empty, or if the underlying character stream
   * is ready.
   * 
   * @return <code>true</code> if the reader is ready
   * @exception IOException
   *            If an I/O error occurs
   */
  @Override
  public boolean ready () throws IOException
  {
    _ensureOpen ();

    /*
     * If newline needs to be skipped and the next char to be read is a newline
     * character, then just skip it right away.
     */
    if (this.m_bSkipLF)
    {
      /*
       * Note that in.ready() will return true if and only if the next read on
       * the stream will not block.
       */
      if (this.m_nNextCharIndex >= this.m_nChars && this.m_aReader.ready ())
        _fill ();
      if (this.m_nNextCharIndex < this.m_nChars)
      {
        if (this.m_aBuf[this.m_nNextCharIndex] == '\n')
          this.m_nNextCharIndex++;
        this.m_bSkipLF = false;
      }
    }
    return this.m_nNextCharIndex < this.m_nChars || this.m_aReader.ready ();
  }

  /**
   * Tells whether this stream supports the mark() operation, which it does.
   * 
   * @return Always <code>true</code>
   */
  @Override
  public boolean markSupported ()
  {
    return true;
  }

  /**
   * Marks the present position in the stream. Subsequent calls to reset() will
   * attempt to reposition the stream to this point.
   * 
   * @param nReadAheadLimit
   *        Limit on the number of characters that may be read while still
   *        preserving the mark. An attempt to reset the stream after reading
   *        characters up to this limit or beyond may fail. A limit value larger
   *        than the size of the input buffer will cause a new buffer to be
   *        allocated whose size is no smaller than limit. Therefore large
   *        values should be used with care.
   * @exception IllegalArgumentException
   *            If readAheadLimit is &lt; 0
   * @exception IOException
   *            If an I/O error occurs
   */
  @Override
  public void mark (@Nonnegative final int nReadAheadLimit) throws IOException
  {
    ValueEnforcer.isGE0 (nReadAheadLimit, "ReadAheadLimit");
    _ensureOpen ();
    this.m_nReadAheadLimit = nReadAheadLimit;
    this.m_nMarkedChar = this.m_nNextCharIndex;
    this.m_bMarkedSkipLF = this.m_bSkipLF;
  }

  /**
   * Resets the stream to the most recent mark.
   * 
   * @exception IOException
   *            If the stream has never been marked, or if the mark has been
   *            invalidated
   */
  @Override
  public void reset () throws IOException
  {
    _ensureOpen ();
    if (this.m_nMarkedChar < 0)
      throw new IOException (this.m_nMarkedChar == INVALIDATED ? "Mark invalid" : "Stream not marked");
    this.m_nNextCharIndex = this.m_nMarkedChar;
    this.m_bSkipLF = this.m_bMarkedSkipLF;
  }

  @Override
  public void close () throws IOException
  {
    if (this.m_aReader != null)
    {
      this.m_aReader.close ();
      this.m_aReader = null;
      this.m_aBuf = null;
    }
  }
}
