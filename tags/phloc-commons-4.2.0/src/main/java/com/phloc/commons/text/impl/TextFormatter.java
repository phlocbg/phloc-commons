/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.text.impl;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Utility methods for formatting text using {@link MessageFormat}.
 * 
 * @author Philip Helger
 */
@Immutable
public final class TextFormatter
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final TextFormatter s_aInstance = new TextFormatter ();

  private TextFormatter ()
  {}

  @Nullable
  public static String getFormattedText (@Nullable final String sText, @Nullable final Object... aArgs)
  {
    if (sText == null)
    {
      // Avoid NPE in MessageFormat
      return null;
    }

    if (aArgs == null || aArgs.length == 0)
    {
      // Return text unchanged
      return sText;
    }

    return MessageFormat.format (sText, aArgs);
  }

  @Nullable
  public static String getFormattedText (@Nonnull final Locale aDisplayLocale,
                                         @Nullable final String sText,
                                         @Nullable final Object... aArgs)
  {
    if (aDisplayLocale == null)
      throw new NullPointerException ("DisplayLocale");

    if (sText == null)
    {
      // Avoid NPE in MessageFormat
      return null;
    }

    if (aArgs == null || aArgs.length == 0)
    {
      // Return text unchanged
      return sText;
    }

    final MessageFormat aMF = new MessageFormat (sText, aDisplayLocale);
    return aMF.format (aArgs);
  }
}
