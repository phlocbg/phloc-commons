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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.StringHelper;

/**
 * This class handles the String parsing of MIME types.
 * 
 * @author Philip Helger
 */
@Immutable
public final class MimeTypeParser
{
  private MimeTypeParser ()
  {}

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
   */
  @Nullable
  public static MimeType createFromString (@Nullable final String sMimeType)
  {
    if (StringHelper.hasText (sMimeType))
    {
      // Find the separator between content type and sub type ("/")
      final int nSlashIndex = sMimeType.indexOf (CMimeType.SEPARATOR_CONTENTTYPE_SUBTYPE);
      if (nSlashIndex >= 0)
      {
        // Use the main content type
        final String sContentType = sMimeType.substring (0, nSlashIndex);
        final EMimeContentType eContentType = EMimeContentType.getFromIDOrNull (sContentType);
        if (eContentType != null)
        {
          // Extract the rest (sub type + parameters)
          final String sRest = sMimeType.substring (nSlashIndex + 1);
          final int nSemicolonIndex = sRest.indexOf (CMimeType.SEPARATOR_PARAMETER);
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
