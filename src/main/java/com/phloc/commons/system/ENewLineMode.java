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
package com.phloc.commons.system;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;

/**
 * Determines the different newline modes for th different operating systems.
 * 
 * @author Philip Helger
 */
public enum ENewLineMode
{
  UNIX ("\n"),
  MAC ("\r"),
  WINDOWS ("\r\n");

  public static final ENewLineMode DEFAULT = getFromTextOrDefault (CGlobal.LINE_SEPARATOR,
                                                                   EOperatingSystem.getCurrentOS ().getNewLineMode ());

  private final String m_sText;

  private ENewLineMode (@Nonnull @Nonempty final String sText)
  {
    m_sText = sText;
  }

  /**
   * @return The textual representation of the new line in this mode.
   */
  @Nonnull
  @Nonempty
  public String getText ()
  {
    return m_sText;
  }

  /**
   * @return <code>true</code> if this a Linux/Unix newline mode
   */
  public boolean isUnix ()
  {
    return this == UNIX;
  }

  /**
   * @return <code>true</code> if this a Mac newline mode
   */
  public boolean isMac ()
  {
    return this == MAC;
  }

  /**
   * @return <code>true</code> if this a Windows newline mode
   */
  public boolean isWindows ()
  {
    return this == WINDOWS;
  }

  @Nullable
  public static ENewLineMode getFromTextOrDefault (@Nullable final String sText, @Nullable final ENewLineMode eDefault)
  {
    if (sText != null && sText.length () > 0)
      for (final ENewLineMode e : values ())
        if (e.m_sText.equals (sText))
          return e;
    return eDefault;
  }

  @Nullable
  public static ENewLineMode getFromTextOrNull (@Nullable final String sText)
  {
    return getFromTextOrDefault (sText, null);
  }
}
