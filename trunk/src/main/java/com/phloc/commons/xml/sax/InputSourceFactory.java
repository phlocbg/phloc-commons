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
package com.phloc.commons.xml.sax;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.xml.sax.InputSource;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.resource.URLResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;

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
    if (aISP instanceof IReadableResource)
      return create ((IReadableResource) aISP);
    return create (aISP.getInputStream ());
  }

  @Nonnull
  public static InputSource create (@Nonnull final IReadableResource aResource)
  {
    if (aResource instanceof FileSystemResource)
    {
      final File aFile = aResource.getAsFile ();
      if (aFile != null)
      {
        // Potentially use memory mapped files
        return create (FileUtils.getInputStream (aFile));
      }
    }
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
  public static InputSource create (@Nonnull final char [] aXML,
                                    @Nonnegative final int nOfs,
                                    @Nonnegative final int nLength)
  {
    return create (new String (aXML, nOfs, nLength));
  }

  @Nonnull
  public static InputSource create (@Nonnull final byte [] aXML)
  {
    return create (new NonBlockingByteArrayInputStream (aXML));
  }

  @Nonnull
  public static InputSource create (@Nonnull final byte [] aXML,
                                    @Nonnegative final int nOfs,
                                    @Nonnegative final int nLength)
  {
    return create (new NonBlockingByteArrayInputStream (aXML, nOfs, nLength));
  }

  @Nonnull
  public static InputSource create (@Nonnull final CharSequence aXML)
  {
    if (aXML instanceof String)
      return create ((String) aXML);
    return create (aXML.toString ());
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
