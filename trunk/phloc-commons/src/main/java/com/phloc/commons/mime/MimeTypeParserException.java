package com.phloc.commons.mime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Exception for errors that occur during MIME type parsing.
 * 
 * @author Philip Helger
 */
public class MimeTypeParserException extends RuntimeException
{
  public MimeTypeParserException (@Nonnull final String sMsg)
  {
    super (sMsg);
  }

  public MimeTypeParserException (@Nonnull final String sMsg, @Nullable final Throwable aCause)
  {
    super (sMsg, aCause);
  }
}
