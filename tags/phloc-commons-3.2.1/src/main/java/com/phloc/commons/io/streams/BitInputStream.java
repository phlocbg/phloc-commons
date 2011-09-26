/**
 * Copyright (C) 2006-2011 phloc systems
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

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.CGlobal;

/**
 * The BitInputStream allows reading individual bits from a general Java
 * InputStream. Like the various Stream-classes from Java, the BitInputStream
 * has to be created based on another Input stream. It provides a function to
 * read the next bit from the stream, as well as to read multiple bits at once
 * and write the resulting data into an integer value.
 * 
 * @author Andreas Jakl
 * @author philip
 */
public class BitInputStream implements Closeable
{
  private final Lock m_aLock = new ReentrantLock ();

  /**
   * The Java InputStream this class is working on.
   */
  private InputStream m_aIS;

  /**
   * The buffer containing the currently processed byte of the input stream.
   */
  private int m_nBuffer;

  /**
   * Next bit of the current byte value that the user will get. If it's 8, the
   * next bit will be read from the next byte of the InputStream.
   */
  private int m_nNextBitIndex;

  private final boolean m_bHighOrderBitFirst;

  /**
   * Create a new bit input stream based on an existing Java InputStream.
   * 
   * @param aIS
   *        the input stream this class should read the bits from. May not be
   *        <code>null</code>.
   */
  public BitInputStream (@Nonnull final InputStream aIS, final boolean bHighOrderBitFirst)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");
    m_aIS = new BufferedInputStream (aIS);
    m_nNextBitIndex = CGlobal.BITS_PER_BYTE;
    m_bHighOrderBitFirst = bHighOrderBitFirst;
  }

  /**
   * Read a specified number of bits and return them combined as an integer
   * value. The bits are written to the integer starting at the highest bit ( <<
   * aNumberOfBits ), going down to the lowest bit ( << 0 )
   * 
   * @param aNumberOfBits
   *        defines how many bits to read from the stream.
   * @return integer value containing the bits read from the stream.
   * @throws IOException
   */
  public int readBits (@Nonnegative final int aNumberOfBits) throws IOException
  {
    if (aNumberOfBits < 1 || aNumberOfBits > 32)
      throw new IllegalArgumentException ("Illegal number of bits passed!");

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
   */
  public int readBit () throws IOException
  {
    m_aLock.lock ();
    try
    {
      if (m_aIS == null)
        throw new IOException ("Already closed");

      if (m_nNextBitIndex == CGlobal.BITS_PER_BYTE)
      {
        m_nBuffer = m_aIS.read ();
        if (m_nBuffer == -1)
          throw new EOFException ();

        m_nNextBitIndex = 0;
      }

      final int nSelectorBit = m_bHighOrderBitFirst ? (1 << (CGlobal.BITS_PER_BYTE - 1 - m_nNextBitIndex))
                                                   : (1 << m_nNextBitIndex);
      final int nBitValue = m_nBuffer & nSelectorBit;
      m_nNextBitIndex++;

      return nBitValue == 0 ? CGlobal.BIT_NOT_SET : CGlobal.BIT_SET;
    }
    finally
    {
      m_aLock.unlock ();
    }
  }

  /**
   * Close the underlying input stream.
   */
  public void close ()
  {
    m_aLock.lock ();
    try
    {
      StreamUtils.close (m_aIS);
      m_aIS = null;
    }
    finally
    {
      m_aLock.unlock ();
    }
  }
}
