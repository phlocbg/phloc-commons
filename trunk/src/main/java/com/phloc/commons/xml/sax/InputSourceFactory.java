package com.phloc.commons.xml.sax;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.xml.sax.InputSource;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.resource.URLResource;

/**
 * Factory class to create the correct {@link InputSource} objects for different
 * input types.
 * 
 * @author philip
 */
@Immutable
public final class InputSourceFactory
{
  private InputSourceFactory ()
  {}

  @Nonnull
  public static InputSource create (@Nonnull final File aFile)
  {
    return create (new FileSystemResource (aFile));
  }

  @Nonnull
  public static InputSource create (@Nonnull final URL aURL)
  {
    return create (new URLResource (aURL));
  }

  @Nonnull
  public static InputSource create (@Nonnull final IInputStreamProvider aISP)
  {
    return create (aISP.getInputStream ());
  }

  @Nonnull
  public static InputSource create (@Nonnull final IReadableResource aResource)
  {
    return new ReadableResourceSAXInputSource (aResource);
  }

  @Nonnull
  public static InputSource create (@Nonnull final String sXML)
  {
    return new StringSAXInputSource (sXML);
  }

  @Nonnull
  public static InputSource create (@Nonnull final char [] aXML)
  {
    return create (new String (aXML));
  }

  @Nonnull
  public static InputSource create (@Nullable final InputStream aIS)
  {
    return new InputSource (aIS);
  }

  @Nonnull
  public static InputSource create (@Nullable final Reader aReader)
  {
    return new InputSource (aReader);
  }
}
