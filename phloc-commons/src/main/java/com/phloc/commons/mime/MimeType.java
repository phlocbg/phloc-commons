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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents a single MIME type as the combination of the content type and the
 * sub-type and parameters.
 * 
 * @author Philip Helger
 */
@Immutable
public class MimeType implements IMimeType, ICloneable <MimeType>
{
  /** The content type (text, application etc.) */
  private final EMimeContentType m_eContentType;

  /** The sub type top be added to the content type. */
  private final String m_sContentSubType;

  /**
   * The MIME type string including content type and sub type - for performance
   * reasons only
   */
  private final String m_sMainTypeAsString;

  /**
   * Constructor. To construct the MIME type "text/xml" you need to pass
   * {@link EMimeContentType#TEXT} and the String "xml" to this construcor.
   * 
   * @param eContentType
   *        MIME content type. May not be <code>null</code>.
   * @param sContentSubType
   *        MIME content sub type. May neither be <code>null</code> nor empty.
   */
  public MimeType (@Nonnull final EMimeContentType eContentType, @Nonnull @Nonempty final String sContentSubType)
  {
    if (eContentType == null)
      throw new NullPointerException ("contentType");
    if (StringHelper.hasNoText (sContentSubType))
      throw new IllegalArgumentException ("contentSubType may not be empty");

    m_eContentType = eContentType;
    m_sContentSubType = sContentSubType;
    m_sMainTypeAsString = m_eContentType.getText () + CMimeType.CONTENTTYPE_SUBTYPE_SEPARATOR + m_sContentSubType;
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
    return m_sMainTypeAsString;
  }

  @Nonnull
  @Nonempty
  public String getAsStringWithoutParameters ()
  {
    return m_sMainTypeAsString;
  }

  @Nonnull
  @Deprecated
  public String getAsStringWithEncoding (@Nonnull final String sEncoding)
  {
    // Don't store in member because this is not used so often
    return m_sMainTypeAsString + CMimeType.CHARSET_PARAM + sEncoding;
  }

  @Nonnull
  public MimeType getClone ()
  {
    return new MimeType (m_eContentType, m_sContentSubType);
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
   * Check if the passed character is a special character according to RFC 2045
   * chapter 5.1
   * 
   * @param c
   *        The character to check
   * @return <code>true</code> if the character is a special character,
   *         <code>false</code> otherwise.
   */
  public static boolean isTSpecialChar (final char c)
  {
    return c == '(' ||
           c == ')' ||
           c == '<' ||
           c == '>' ||
           c == '@' ||
           c == ',' ||
           c == ';' ||
           c == ':' ||
           c == '\\' ||
           c == '"' ||
           c == '/' ||
           c == '[' ||
           c == ']' ||
           c == '?' ||
           c == '=';
  }

  /**
   * Check if the passed character is a valid token character. According to RFC
   * 2045 this can be
   * <em>any (US-ASCII) CHAR except SPACE, CTLs, or tspecials</em>
   * 
   * @param c
   *        The character to check.
   * @return <code>true</code> if the passed character is a valid token
   *         character, <code>false</code> otherwise
   */
  public static boolean isTokenChar (final char c)
  {
    // SPACE: 32
    // CTLs: 0-31, 127
    return c > 32 && c < 127 && !isTSpecialChar (c);
  }

  /**
   * Try to convert the string representation of a MIME type to an object.
   * 
   * @param sMimeType
   *        The string representation to be converted. May be <code>null</code>.
   * @return <code>null</code> if the parsed string could not be converted to a
   *         MIME type object.
   * @deprecated Use {@link #createFromString(String)} instead
   */
  @Deprecated
  @Nullable
  public static MimeType parseFromStringWithoutEncoding (@Nullable final String sMimeType)
  {
    return createFromString (sMimeType);
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
  public static MimeType createFromString (@Nullable final String sMimeType)
  {
    if (StringHelper.hasText (sMimeType))
    {
      // Find the separator between content type and sub type ("/")
      final int nSlashIndex = sMimeType.indexOf (CMimeType.CONTENTTYPE_SUBTYPE_SEPARATOR);
      if (nSlashIndex >= 0)
      {
        // Use the main content type
        final String sContentType = sMimeType.substring (0, nSlashIndex);
        final EMimeContentType eContentType = EMimeContentType.getFromIDOrNull (sContentType);
        if (eContentType != null)
        {
          // Extract the rest (sub type + parameters)
          final String sRest = sMimeType.substring (nSlashIndex + 1);
          final int nSemicolonIndex = sRest.indexOf (CMimeType.PARAMETER_SEPARATOR);
          String sContentSubType;
          String sParameters;
          if (nSemicolonIndex >= 0)
          {
            sContentSubType = sRest.substring (0, nSemicolonIndex);
            sParameters = sRest.substring (nSemicolonIndex + 1);
          }
          else
          {
            sContentSubType = sRest;
            sParameters = null;
          }

          final MimeType ret = new MimeType (eContentType, sContentSubType);
          if (StringHelper.hasText (sParameters))
          {
            // We have parameters to extract
          }
          return ret;
        }
      }
    }
    return null;
  }
}
