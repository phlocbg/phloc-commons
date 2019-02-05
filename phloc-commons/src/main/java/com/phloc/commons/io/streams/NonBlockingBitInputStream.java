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

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.ToStringGenerator;

/**
 * The {@link NonBlockingBitInputStream} allows reading individual bits from a
 * general Java InputStream. Like the various Stream-classes from Java, the
 * BitInputStream has to be created based on another Input stream. It provides a
 * function to read the next bit from the stream, as well as to read multiple
 * bits at once and write the resulting data into an integer value.<br>
 * For a thread-safe version see {@link BitInputStream}
 * 
 * @author Andreas Jakl
 * @author Philip Helger
 */
public class NonBlockingBitInputStream implements Closeable
{
  /**
   * The Java InputStream this class is working on.
   */
  private InputStream m_aIS;

  private final boolean m_bHighOrderBitFirst;

  /**
   * Next bit of the current byte value that the user will get. If it's 8, the
   * next bit will be read from the next byte of the InputStream.
   */
  private int m_nNextBitIndex;

  /**
   * The buffer containing the currently processed byte of the input stream.
   */
  private int m_nBuffer;

  /**
   * Create a new bit input stream based on an existing Java InputStream.
   * 
   * @param aIS
   *        the input stream this class should read the bits from. May not be
   *        <code>null</code>.
   * @param aByteOrder
   *        The non-<code>null</code> byte order to use.
   */
  public NonBlockingBitInputStream (@Nonnull final InputStream aIS, @Nonnull final ByteOrder aByteOrder)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aByteOrder, "ByteOrder");

    this.m_aIS = StreamUtils.getBuffered (aIS);
    this.m_bHighOrderBitFirst = aByteOrder.equals (ByteOrder.LITTLE_ENDIAN);
    this.m_nNextBitIndex = CGlobal.BITS_PER_BYTE;
  }

  /**
   * @return The byte order used. Never <code>null</code>.
   */
  @Nonnull
  public ByteOrder getByteOrder ()
  {
    return this.m_bHighOrderBitFirst ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
  }

  /**
   * Read a specified number of bits and return them combined as an integer
   * value. The bits are written to the integer starting at the highest bit (
   * &lt;&lt; aNumberOfBits ), going down to the lowest bit ( &lt;&lt; 0 ), so
   * thge returned ByteOrder is always LITTLE_ENDIAN!
   * 
   * @param aNumberOfBits
   *        defines how many bits to read from the stream.
   * @return integer value containing the bits read from the stream.
   * @throws IOException
   *         In case EOF is reached
   */
  public int readBits (@Nonnegative final int aNumberOfBits) throws IOException
  {
    ValueEnforcer.isBetweenInclusive (aNumberOfBits, "NumberOfBits", 1, CGlobal.BITS_PER_INT);

    int ret = 0;
    for (int i = aNumberOfBits - 1; i >= 0; i--)
      ret |= (readBit () << i);
    return ret;
  }

  /**
   * Read the next bit from the stream.
   * 
   * @return 0 if the bit is 0, 1 if the bit is 1.
   * @throws IOException
   *         In case EOF is reached
   */
  public int readBit () throws IOException
  {
    if (this.m_aIS == null)
      throw new IOException ("Already closed");

    if (this.m_nNextBitIndex == CGlobal.BITS_PER_BYTE)
    {
      this.m_nBuffer = this.m_aIS.read ();
      if (this.m_nBuffer == -1)
        throw new EOFException ();

      this.m_nNextBitIndex = 0;
    }

    final int nSelectorBit = this.m_bHighOrderBitFirst ? (1 << (CGlobal.BITS_PER_BYTE - 1 - this.m_nNextBitIndex))
                                                       : (1 << this.m_nNextBitIndex);
    final int nBitValue = this.m_nBuffer & nSelectorBit;
    this.m_nNextBitIndex++;

    return nBitValue == 0 ? CGlobal.BIT_NOT_SET : CGlobal.BIT_SET;
  }

  /**
   * Close the underlying input stream.
   */
  @Override
  public void close ()
  {
    StreamUtils.close (this.m_aIS);
    this.m_aIS = null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("IS", this.m_aIS)
                                       .append ("highOrderBitFirst", this.m_bHighOrderBitFirst)
                                       .append ("nextBitIndex", this.m_nNextBitIndex)
                                       .append ("buffer", this.m_nBuffer)
                                       .toString ();
  }
}
