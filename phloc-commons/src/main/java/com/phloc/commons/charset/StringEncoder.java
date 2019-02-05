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
package com.phloc.commons.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.state.EFinish;

/**
 * A special string encoder that can be used to convert a set of
 * characters/string to a byte sequence in a certain charset. This class is not
 * thread-safe!
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class StringEncoder
{
  public static final int CHAR_BUFFER_SIZE = 1024;
  public static final int BYTE_BUFFER_SIZE = CHAR_BUFFER_SIZE * 2;
  // Extra "slop" when allocating a new byte buffer: permits the string to
  // contain some extra long UTF-8 characters without needing a new buffer.
  private static final int BUFFER_EXTRA_BYTES = 64;

  // The JDK's java.nio.charset.Charset.maxBytesPerChar() returns 4.0 for
  // UTF-8. This is wrong: the max is 4 bytes per CODEPOINT, but to represent
  // a 4 byte UTF-8 code point, you need 2 Java chars (UTF-16). Thus, the max
  // is 3 bytes per char, and there is a unit test to verify this.
  public static final int UTF8_MAX_BYTES_PER_CHAR = 3;

  private final CharBuffer m_aInChar = CharBuffer.allocate (CHAR_BUFFER_SIZE);
  private final ByteBuffer m_aArrayBuffer = ByteBuffer.allocate (BYTE_BUFFER_SIZE);

  private final CharsetEncoder m_aEncoder;
  private int m_nReadOffset = 0;

  public StringEncoder (@Nonnull final Charset aCharset)
  {
    ValueEnforcer.notNull (aCharset, "Charset");

    this.m_aEncoder = aCharset.newEncoder ();
    // set the buffer to "filled" so it gets filled by encode()
    this.m_aInChar.position (this.m_aInChar.limit ());
    // Needed for U+D800 - U+DBFF = High Surrogate; U+DC00 - U+DFFF = Low
    // Surrogates
    // Maybe others in the future? This is what the JDK does for
    // String.getBytes().
    this.m_aEncoder.onMalformedInput (CodingErrorAction.REPLACE);
    // Not actually needed for UTF-8, but can't hurt
    this.m_aEncoder.onUnmappableCharacter (CodingErrorAction.REPLACE);
  }

  private void _readInputChunk (@Nonnull final String sSource)
  {
    assert this.m_aInChar.remaining () <= 1;
    assert this.m_nReadOffset < sSource.length ();

    final char [] aInChars = this.m_aInChar.array ();

    // We need to get a chunk from the string: Compute the chunk length
    int nReadLength = sSource.length () - this.m_nReadOffset;
    if (nReadLength > aInChars.length)
      nReadLength = aInChars.length;

    // Copy the chunk from the string into our temporary buffer
    sSource.getChars (this.m_nReadOffset, this.m_nReadOffset + nReadLength, aInChars, 0);
    this.m_aInChar.position (0);
    this.m_aInChar.limit (nReadLength);
    this.m_nReadOffset += nReadLength;
  }

  /**
   * Encodes string into destination. This must be called multiple times with
   * the same string until it returns true. When this returns false, it must be
   * called again with larger destination buffer space. It is possible that
   * there are a few bytes of space remaining in the destination buffer, even
   * though it must be refreshed. For example, if a UTF-8 3 byte sequence needs
   * to be written, but there is only 1 or 2 bytes of space, this will leave the
   * last couple bytes unused.
   * 
   * @param sSource
   *        The source string which shall be encoded
   * @param aDestBuffer
   *        a ByteBuffer that will be filled with data.
   * @return {@link EFinish}
   */
  @Nonnull
  public EFinish encode (@Nonnull final String sSource, @Nonnull final ByteBuffer aDestBuffer)
  {
    ValueEnforcer.notNull (sSource, "Source");
    ValueEnforcer.notNull (aDestBuffer, "DestBuffer");

    // We need to special case the empty string
    if (sSource.length () == 0)
      return EFinish.FINISHED;

    // read data in, if needed
    if (!this.m_aInChar.hasRemaining () && this.m_nReadOffset < sSource.length ())
      _readInputChunk (sSource);

    // if flush() overflows the destination, skip the encode loop and re-try the
    // flush()
    if (this.m_aInChar.hasRemaining ())
    {
      while (true)
      {
        assert this.m_aInChar.hasRemaining ();
        final boolean bEndOfInput = this.m_nReadOffset == sSource.length ();
        final CoderResult aResult = this.m_aEncoder.encode (this.m_aInChar, aDestBuffer, bEndOfInput);
        if (aResult == CoderResult.OVERFLOW)
        {
          // NOTE: destination could space remaining, in case of a multi-byte
          // sequence
          assert aDestBuffer.remaining () < this.m_aEncoder.maxBytesPerChar ();
          return EFinish.UNFINISHED;
        }
        assert aResult == CoderResult.UNDERFLOW;

        // If we split a surrogate char (inBuffer.remaining() == 1), back up and
        // re-copy
        // from the source. avoid a branch by always subtracting
        assert this.m_aInChar.remaining () <= 1;
        this.m_nReadOffset -= this.m_aInChar.remaining ();
        assert this.m_nReadOffset > 0;

        // If we are done, break. Otherwise, read the next chunk
        if (this.m_nReadOffset == sSource.length ())
          break;
        _readInputChunk (sSource);
      }
    }
    assert !this.m_aInChar.hasRemaining ();
    assert this.m_nReadOffset == sSource.length ();

    final CoderResult aResult = this.m_aEncoder.flush (aDestBuffer);
    if (aResult == CoderResult.OVERFLOW)
    {
      // I don't think this can happen. If it does, assert so we can figure it
      // out
      assert false;
      // We attempt to handle it anyway
      return EFinish.UNFINISHED;
    }
    assert aResult == CoderResult.UNDERFLOW;

    // done!
    reset ();
    return EFinish.FINISHED;
  }

  @Nonnegative
  private int _getCharsConverted ()
  {
    final int nCharsConverted = this.m_nReadOffset - this.m_aInChar.remaining ();
    assert 0 <= nCharsConverted && nCharsConverted <= this.m_nReadOffset;
    return nCharsConverted;
  }

  /**
   * Returns a ByteBuffer containing the encoded version of source. The position
   * of the ByteBuffer will be 0, the limit is the length of the string. The
   * capacity of the ByteBuffer may be larger than the string.
   * 
   * @param sSource
   *        The source string which shall be encoded
   * @return The new byte buffer
   */
  @Nonnull
  public ByteBuffer getAsNewByteBuffer (@Nonnull final String sSource)
  {
    // Optimized for 1 byte per character strings (ASCII)
    ByteBuffer ret = ByteBuffer.allocate (sSource.length () + BUFFER_EXTRA_BYTES);

    while (encode (sSource, ret).isUnfinished ())
    {
      // need a larger buffer
      // estimate the average bytes per character from the current sample
      final int nCharsConverted = _getCharsConverted ();
      double dBytesPerChar;
      if (nCharsConverted > 0)
      {
        dBytesPerChar = ret.position () / (double) nCharsConverted;
      }
      else
      {
        // charsConverted can be 0 if the initial buffer is smaller than one
        // character
        dBytesPerChar = this.m_aEncoder.averageBytesPerChar ();
      }

      final int nCharsRemaining = sSource.length () - nCharsConverted;
      assert nCharsRemaining > 0;
      final int nBytesRemaining = (int) (nCharsRemaining * dBytesPerChar + 0.5);

      final int nPos = ret.position ();
      final ByteBuffer aNewBuffer = ByteBuffer.allocate (ret.position () + nBytesRemaining + BUFFER_EXTRA_BYTES);
      ret.flip ();
      aNewBuffer.put (ret);
      aNewBuffer.position (nPos);
      ret = aNewBuffer;
    }

    // Set the buffer for reading and finish
    ret.flip ();
    return ret;
  }

  /**
   * Returns a new byte array containing the UTF-8 version of source. The array
   * will be exactly the correct size for the string.
   * 
   * @param sSource
   *        The source string which shall be encoded
   * @return the new byte array
   */
  @Nonnull
  public byte [] getAsNewArray (@Nonnull final String sSource)
  {
    // Optimized for short strings
    assert this.m_aArrayBuffer.remaining () == this.m_aArrayBuffer.capacity ();
    if (encode (sSource, this.m_aArrayBuffer).isFinished ())
    {
      // copy the exact correct bytes out
      final byte [] ret = new byte [this.m_aArrayBuffer.position ()];
      System.arraycopy (this.m_aArrayBuffer.array (), 0, ret, 0, this.m_aArrayBuffer.position ());
      this.m_aArrayBuffer.clear ();
      // ~ good += 1;
      return ret;
    }

    // Worst case: assume max bytes per remaining character.
    final int charsRemaining = sSource.length () - _getCharsConverted ();
    final ByteBuffer aRestBuffer = ByteBuffer.allocate (charsRemaining * UTF8_MAX_BYTES_PER_CHAR);
    final EFinish eDone = encode (sSource, aRestBuffer);
    assert eDone.isFinished ();

    // Combine everything and return it
    final byte [] ret = new byte [this.m_aArrayBuffer.position () + aRestBuffer.position ()];
    System.arraycopy (this.m_aArrayBuffer.array (), 0, ret, 0, this.m_aArrayBuffer.position ());
    System.arraycopy (aRestBuffer.array (), 0, ret, this.m_aArrayBuffer.position (), aRestBuffer.position ());
    this.m_aArrayBuffer.clear ();
    // ~ worst += 1;
    return ret;
  }

  public void reset ()
  {
    this.m_nReadOffset = 0;
    // reset inBuffer in case we are in the middle of an operation
    this.m_aInChar.position (0);
    this.m_aInChar.limit (0);
    this.m_aEncoder.reset ();
  }
}
