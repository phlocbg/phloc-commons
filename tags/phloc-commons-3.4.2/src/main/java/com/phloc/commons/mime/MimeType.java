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
package com.phloc.commons.mime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents a single MIME type as the combination of the content type and the
 * sub-type.
 * 
 * @author philip
 */
@Immutable
public final class MimeType implements IMimeType
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MimeType.class);

  /** The content type (text, application etc.) */
  private final EMimeContentType m_eContentType;

  /** The sub type top be added to the content type. */
  private final String m_sContentSubType;

  /** The final MIME type including content type and sub type */
  private final String m_sAsString;

  public MimeType (@Nonnull final EMimeContentType eContentType, @Nonnull @Nonempty final String sContentSubType)
  {
    if (eContentType == null)
      throw new NullPointerException ("contentType");
    if (StringHelper.hasNoText (sContentSubType))
      throw new IllegalArgumentException ("contentSubType may not be empty");
    m_eContentType = eContentType;
    m_sContentSubType = sContentSubType;
    m_sAsString = m_eContentType.getText () + CMimeType.CONTENTTYPE_SUBTYPE_SEPARATOR + m_sContentSubType;
  }

  @Nonnull
  public EMimeContentType getContentType ()
  {
    return m_eContentType;
  }

  @Nonnull
  @Nonempty
  public String getContentSubType ()
  {
    return m_sContentSubType;
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    return m_sAsString;
  }

  @Nonnull
  public String getAsStringWithEncoding (final String sEncoding)
  {
    // Leads to false positives (e.g. with application/atom+xml)
    if (false)
      if (m_eContentType != EMimeContentType.TEXT)
        s_aLogger.warn ("You are using non-text MIME type " + m_sAsString + " with encoding " + sEncoding);

    // Don't store in member because this is not used so often
    return m_sAsString + CMimeType.CHARSET_PARAM + sEncoding;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MimeType))
      return false;
    final MimeType rhs = (MimeType) o;
    return m_eContentType.equals (rhs.m_eContentType) && m_sContentSubType.equals (rhs.m_sContentSubType);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eContentType).append (m_sContentSubType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("contentType", m_eContentType)
                                       .append ("subType", m_sContentSubType)
                                       .toString ();
  }

  /**
   * Try to convert the string representation of a MIME type to an object.
   * 
   * @param sMimeType
   *        The string representation to be converted. May be <code>null</code>.
   * @return <code>null</code> if the parsed string could not be converted to a
   *         MIME type object.
   */
  @Nullable
  public static IMimeType parseFromStringWithoutEncoding (@Nullable final String sMimeType)
  {
    if (StringHelper.hasText (sMimeType))
    {
      final int nSlashIndex = sMimeType.indexOf (CMimeType.CONTENTTYPE_SUBTYPE_SEPARATOR);
      if (nSlashIndex != -1)
      {
        final String sContentType = sMimeType.substring (0, nSlashIndex);
        final EMimeContentType eContentType = EMimeContentType.getFromIDOrNull (sContentType);
        if (eContentType != null)
        {
          final String sContentSubType = sMimeType.substring (nSlashIndex + 1);
          return new MimeType (eContentType, sContentSubType);
        }
      }
    }
    return null;
  }
}
