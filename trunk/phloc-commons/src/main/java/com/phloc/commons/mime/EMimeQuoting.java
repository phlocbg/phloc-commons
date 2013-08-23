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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

/**
 * Defines the possible MIME type parameter value quotings
 * 
 * @author Philip Helger
 */
public enum EMimeQuoting
{
  /** Create a quoted string according to RFC 822 */
  QUOTED_STRING
  {
    @Override
    @Nonnull
    @Nonempty
    public String getQuotedString (@Nonnull @Nonempty final String sUnquotedString)
    {
      final StringBuilder aSB = new StringBuilder ("\"");
      final char [] aChars = sUnquotedString.toCharArray ();
      for (final char c : aChars)
        if (c == '"' || c == '\\')
        {
          // Mask chars
          aSB.append ('\\').append (c);
        }
        else
          aSB.append (c);
      return aSB.append ("\"").toString ();
    }
  },

  /** Create a quoted printable String */
  QUOTED_PRINTABLE
  {
    @Override
    @Nonnull
    @Nonempty
    public String getQuotedString (@Nonnull @Nonempty final String sUnquotedString)
    {
      final StringBuilder aSB = new StringBuilder ();
      final char [] aChars = sUnquotedString.toCharArray ();
      for (final char c : aChars)
        if (c >= 33 && c <= 126 && c != '=')
          aSB.append (c);
        else
        {
          // Mask chars
          aSB.append ('=').append (StringHelper.getHexStringLeadingZero (c, 2));
        }
      return aSB.toString ();
    }
  },

  URL_ESCAPE
  {
    @Override
    @Nonnull
    @Nonempty
    public String getQuotedString (@Nonnull @Nonempty final String sUnquotedString)
    {
      final StringBuilder aSB = new StringBuilder ();
      final char [] aChars = sUnquotedString.toCharArray ();
      for (final char c : aChars)
        if (MimeTypeParser.isTokenChar (c) && c != '%')
          aSB.append (c);
        else
        {
          // Mask chars
          aSB.append ('%').append (StringHelper.getHexStringLeadingZero (c, 2));
        }
      return aSB.toString ();
    }
  };

  @Nonnull
  @Nonempty
  public abstract String getQuotedString (@Nonnull @Nonempty String sUnquotedString);
}
