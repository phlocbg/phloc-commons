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
package com.phloc.commons.mime;

import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represent a single mapping from content bytes to an {@link IMimeType}.
 * 
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public class MimeTypeContent
{
  private final byte [] m_aContentBytes;
  private final IMimeType m_aMimeType;

  /**
   * Constructor
   * 
   * @param aContentBytes
   *        The beginning bytes. May neither be <code>null</code> nor empty.
   * @param aMimeType
   *        The corresponding mime type. May not be <code>null</code>.
   */
  public MimeTypeContent (@Nonnull @Nonempty final byte [] aContentBytes, @Nonnull final IMimeType aMimeType)
  {
    if (ArrayHelper.isEmpty (aContentBytes))
      throw new IllegalArgumentException ("no ContentBytes provided");
    if (aMimeType == null)
      throw new NullPointerException ("mimeType");
    m_aContentBytes = ArrayHelper.getCopy (aContentBytes);
    m_aMimeType = aMimeType;
  }

  /**
   * @return A copy of the content bytes to use. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public byte [] getContentBytes ()
  {
    return ArrayHelper.getCopy (m_aContentBytes);
  }

  /**
   * @return The number of content bytes available. Always &gt; 0.
   */
  @Nonnegative
  public int getContentByteCount ()
  {
    return m_aContentBytes.length;
  }

  /**
   * @return The matching mime type. Never <code>null</code>.
   */
  @Nonnull
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  /**
   * Main match method
   * 
   * @param aBytes
   *        The bytes to compare to this type.
   * @param nOffset
   *        The offset within aBytes to start searching
   * @param aCmpBytes
   *        The compare bytes of this object.
   * @return <code>true</code> if the bytes match, <code>false</code> otherwise.
   */
  private static boolean _match (@Nonnull final byte [] aBytes,
                                 @Nonnegative final int nOffset,
                                 @Nonnull final byte [] aCmpBytes)
  {
    final int nEnd = aCmpBytes.length;
    for (int i = 0; i < nEnd; ++i)
      if (aBytes[nOffset + i] != aCmpBytes[i])
        return false;
    return true;
  }

  /**
   * Check if the passed byte array starts with the bytes of this object.
   * 
   * @param aBytes
   *        The bytes to compare to. May not be <code>null</code>.
   * @return <code>true</code> if the passed bytes start with the bytes in this
   *         object.
   */
  public boolean matchesBeginning (@Nonnull final byte [] aBytes)
  {
    if (aBytes == null)
      throw new NullPointerException ("bytes");

    return aBytes.length >= m_aContentBytes.length && _match (aBytes, 0, m_aContentBytes);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final MimeTypeContent rhs = (MimeTypeContent) o;
    return Arrays.equals (m_aContentBytes, rhs.m_aContentBytes) && m_aMimeType.equals (rhs.m_aMimeType);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aContentBytes).append (m_aMimeType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("contentBytes", Arrays.toString (m_aContentBytes))
                                       .append ("mimeType", m_aMimeType)
                                       .toString ();
  }
}
