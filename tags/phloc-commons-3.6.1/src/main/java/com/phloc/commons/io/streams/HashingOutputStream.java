/**
 * Copyright (C) 2006-2012 phloc systems
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
import java.io.OutputStream;

import javax.annotation.Nonnull;

import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.messagedigest.IMessageDigestGenerator;
import com.phloc.commons.messagedigest.NonBlockingMessageDigestGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A wrapper around an {@link OutputStream} that performs a hashing while
 * writing.
 * 
 * @see IMessageDigestGenerator
 * @author philip
 */
public class HashingOutputStream extends WrappedOutputStream
{
  private final IMessageDigestGenerator m_aMDGen;

  public HashingOutputStream (@Nonnull final OutputStream aSourceOS, @Nonnull final EMessageDigestAlgorithm eMDAlgorithm)
  {
    super (aSourceOS);
    m_aMDGen = new NonBlockingMessageDigestGenerator (eMDAlgorithm);
  }

  @Override
  public void write (final int n) throws IOException
  {
    super.write (n);
    m_aMDGen.update ((byte) n);
  }

  @Override
  public void write (final byte [] aBuf, final int nOfs, final int nLength) throws IOException
  {
    super.write (aBuf, nOfs, nLength);
    m_aMDGen.update (aBuf, nOfs, nLength);
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
