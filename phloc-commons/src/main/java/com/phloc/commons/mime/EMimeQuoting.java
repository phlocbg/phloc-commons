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

import java.util.BitSet;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.codec.QuotedPrintableCodec;
import com.phloc.commons.codec.URLCodec;

/**
 * Defines the possible MIME type parameter value quotings
 * 
 * @author Philip Helger
 */
public enum EMimeQuoting
{
  /**
   * Create a quoted string according to RFC 822 (surrounding everything in
   * double-quotes and masking using backslash)
   */
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

  /**
   * Create a quoted printable String. Replace all non-printable characters with
   * =XY where XY is the hex encoding of the char.
   */
  QUOTED_PRINTABLE
  {
    @Override
    @Nonnull
    @Nonempty
    public String getQuotedString (@Nonnull @Nonempty final String sUnquotedString)
    {
      // Use a special BitSet
      return QuotedPrintableCodec.encodeText (BS_QUOTED_PRINTABLE, sUnquotedString, CCharset.CHARSET_UTF_8_OBJ);
    }
  },

  URL_ESCAPE
  {
    @Override
    @Nonnull
    @Nonempty
    public String getQuotedString (@Nonnull @Nonempty final String sUnquotedString)
    {
      // Use a special BitSet
      return URLCodec.encodeText (BS_URL_PRINTABLE, sUnquotedString, CCharset.CHARSET_UTF_8_OBJ);
    }
  };

  private static final BitSet BS_QUOTED_PRINTABLE = QuotedPrintableCodec.getDefaultBitSet ();
  private static final BitSet BS_URL_PRINTABLE = URLCodec.getDefaultBitSet ();

  static
  {
    // Modify BitSets
    BS_QUOTED_PRINTABLE.set ('\t', false);
    BS_QUOTED_PRINTABLE.set (' ', false);
    BS_URL_PRINTABLE.set (' ', false);

    for (final char c : MimeTypeParser.getAllTSpecialChars ())
    {
      BS_QUOTED_PRINTABLE.set (c, false);
      BS_URL_PRINTABLE.set (c, false);
    }
  }

  @Nonnull
  @Nonempty
  public abstract String getQuotedString (@Nonnull @Nonempty String sUnquotedString);
}
