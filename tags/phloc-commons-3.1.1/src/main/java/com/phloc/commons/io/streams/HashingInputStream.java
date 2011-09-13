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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.messagedigest.MessageDigestGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A wrapper around an {@link InputStream} that performs a hashing while
 * reading.
 * 
 * @see MessageDigestGenerator
 * @author philip
 */
public class HashingInputStream extends WrappedInputStream
{
  private final MessageDigestGenerator m_aMDGen;

  public HashingInputStream (@Nonnull final InputStream aSourceIS, @Nonnull final EMessageDigestAlgorithm eMDAlgorithm)
  {
    super (aSourceIS);
    m_aMDGen = new MessageDigestGenerator (eMDAlgorithm);
  }

  @Override
  public int read () throws IOException
  {
    final int ret = super.read ();
    if (ret != -1)
      m_aMDGen.update ((byte) ret);
    return ret;
  }

  @Override
  public int read (final byte b[], final int nOffset, final int nLength) throws IOException
  {
    final int ret = super.read (b, nOffset, nLength);
    if (ret != -1)
      m_aMDGen.update (b, nOffset, ret);
    return ret;
  }

  /**
   * Get the message digest of this stream. Call this only once the read has
   * been finished. Never call this in the middle of reading a stream, because
   * the digest cannot be updated afterwards.
   * 
   * @return The message digest of this stream.
   */
  @Nonnull
  public byte [] getDigest ()
  {
    return m_aMDGen.getDigest ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("mdgen", m_aMDGen).toString ();
  }
}
