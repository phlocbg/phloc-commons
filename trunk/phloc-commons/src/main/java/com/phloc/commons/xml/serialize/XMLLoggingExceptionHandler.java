package com.phloc.commons.xml.serialize;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.callback.LoggingExceptionHandler;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.xml.sax.AbstractSAXErrorHandler;

/**
 * A special version of the {@link LoggingExceptionHandler} that handles the
 * most common XML exceptions in a nice way :)
 * 
 * @author Philip Helger
 */
public class XMLLoggingExceptionHandler extends LoggingExceptionHandler
{
  public XMLLoggingExceptionHandler ()
  {}

  public XMLLoggingExceptionHandler (@Nonnull final EErrorLevel eErrorLevel)
  {
    super (eErrorLevel);
  }

  @Override
  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getLogMessage (@Nullable final Throwable t)
  {
    if (t instanceof SAXParseException)
    {
      final SAXParseException ex = (SAXParseException) t;
      return AbstractSAXErrorHandler.getSaxParseError (EErrorLevel.ERROR, ex).getAsString (CGlobal.DEFAULT_LOCALE);
    }
    if (t instanceof SAXException)
    {
      return "Error parsing XML document";
    }
    if (t instanceof UnknownHostException)
    {
      // Must be checked before IOException because it is an IOException
      // Caught if entity resolver failed
      final UnknownHostException ex = (UnknownHostException) t;
      return "Failed to resolve entity host: " + ex.getMessage ();
    }
    if (t instanceof IOException)
    {
      return "Error reading XML document";
    }
    return super.getLogMessage (t);
  }

  @Override
  protected boolean isLogException (@Nullable final Throwable t)
  {
    if (t instanceof UnknownHostException)
      return false;
    return super.isLogException (t);
  }
}
