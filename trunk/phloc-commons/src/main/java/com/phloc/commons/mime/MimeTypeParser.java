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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;

/**
 * This class handles the String parsing of MIME types.
 * 
 * @author Philip Helger
 */
@Immutable
public final class MimeTypeParser
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final MimeTypeParser s_aInstance = new MimeTypeParser ();

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
   * Check if the passed string is a valid MIME token by checking that the
   * length is at least 1 and all chars match the {@link #isTokenChar(char)}
   * condition.
   * 
   * @param sToken
   *        The token to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed string is valid token,
   *         <code>false</code> otherwise.
   */
  public static boolean isToken (@Nullable final String sToken)
  {
    // Check length
    if (StringHelper.hasNoText (sToken))
      return false;

    // Check that all chars are token chars
    final char [] aChars = sToken.toCharArray ();
    for (final char c : aChars)
      if (!isTokenChar (c))
        return false;
    return true;
  }

  private static void _parseAndAddParameters (@Nonnull final MimeType aMimeType,
                                              @Nonnull @Nonempty final String sParameters,
                                              @Nonnull final EMimeQuoting eQuotingAlgorithm)
  {
    // Split all parameters
    final String [] aParams = StringHelper.getExplodedArray (CMimeType.SEPARATOR_PARAMETER, sParameters);
    for (final String sParameter : aParams)
    {
      // Split each parameter into name and value
      final String [] aParamItems = StringHelper.getExplodedArray (CMimeType.SEPARATOR_PARAMETER_NAME_VALUE,
                                                                   sParameter,
                                                                   2);
      if (aParamItems.length != 2)
        throw new IllegalArgumentException ("MimeType Parameter without name/value separator found: '" +
                                            sParameter +
                                            "'");

      final String sParameterName = aParamItems[0].trim ();
      final String sParameterValue = aParamItems[1].trim ();
      aMimeType.addParameter (sParameterName, sParameterValue);
    }
  }

  /**
   * Try to convert the string representation of a MIME type to an object. The
   * default quoting algorithm {@link CMimeType#DEFAULT_QUOTING} is used to
   * unquote strings.
   * 
   * @param sMimeType
   *        The string representation to be converted. May be <code>null</code>.
   * @return <code>null</code> if the parsed string could not be converted to a
   *         MIME type object.
   */
  @Nullable
  public static MimeType createFromString (@Nullable final String sMimeType)
  {
    return createFromString (sMimeType, CMimeType.DEFAULT_QUOTING);
  }

  /**
   * Try to convert the string representation of a MIME type to an object. The
   * default quoting algorithm {@link CMimeType#DEFAULT_QUOTING} is used to
   * un-quote strings.
   * 
   * @param sMimeType
   *        The string representation to be converted. May be <code>null</code>.
   * @param eQuotingAlgorithm
   *        The quoting algorithm to be used to un-quote parameter values. May
   *        not be <code>null</code>. * @return <code>null</code> if the parsed
   *        string could not be converted to a MIME type object.
   */
  @Nullable
  public static MimeType createFromString (@Nullable final String sMimeType,
                                           @Nonnull final EMimeQuoting eQuotingAlgorithm)
  {
    if (eQuotingAlgorithm == null)
      throw new NullPointerException ("quotingAlgorithm");

    if (StringHelper.hasText (sMimeType))
    {
      // Find the separator between content type and sub type ("/")
      final int nSlashIndex = sMimeType.indexOf (CMimeType.SEPARATOR_CONTENTTYPE_SUBTYPE);
      if (nSlashIndex >= 0)
      {
        // Use the main content type
        final String sContentType = sMimeType.substring (0, nSlashIndex).trim ();
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
            sContentSubType = sRest.substring (0, nSemicolonIndex).trim ();
            // everything after the first ';' as parameters
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
            _parseAndAddParameters (ret, sParameters, eQuotingAlgorithm);
          }
          return ret;
        }
      }
    }
    return null;
  }
}
