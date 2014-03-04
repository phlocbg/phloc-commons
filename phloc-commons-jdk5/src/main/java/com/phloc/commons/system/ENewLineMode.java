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
