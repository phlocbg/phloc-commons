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
package com.phloc.commons.xml.transform;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.URLResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;

/**
 * Factory class to create the correct {@link Source} objects for different
 * input types.
 * 
 * @author philip
 */
@Immutable
public final class TransformSourceFactory
{
  private TransformSourceFactory ()
  {}

  @Nonnull
  public static StreamSource create (@Nonnull final File aFile)
  {
    return new StreamSource (aFile);
  }

  @Nonnull
  public static ResourceStreamSource create (@Nonnull final URL aURL)
  {
    return create (new URLResource (aURL));
  }

  @Nonnull
  public static StreamSource create (@Nonnull final IInputStreamProvider aISP)
  {
    if (aISP instanceof IReadableResource)
      return create ((IReadableResource) aISP);
    return create (aISP.getInputStream ());
  }

  @Nonnull
  public static ResourceStreamSource create (@Nonnull final IReadableResource aResource)
  {
    return new ResourceStreamSource (aResource);
  }

  @Nonnull
  public static StringStreamSource create (@Nonnull final String sXML)
  {
    return new StringStreamSource (sXML);
  }

  @Nonnull
  public static StringStreamSource create (@Nonnull final char [] aXML)
  {
    return create (new String (aXML));
  }

  @Nonnull
  public static StringStreamSource create (@Nonnull final char [] aXML,
                                           @Nonnegative final int nOfs,
                                           @Nonnegative final int nLength)
  {
    return create (new String (aXML, nOfs, nLength));
  }

  @Nonnull
  public static StreamSource create (@Nonnull final byte [] aXML)
  {
    return create (new NonBlockingByteArrayInputStream (aXML));
  }

  @Nonnull
  public static StreamSource create (@Nonnull final byte [] aXML,
                                     @Nonnegative final int nOfs,
                                     @Nonnegative final int nLength)
  {
    return create (new NonBlockingByteArrayInputStream (aXML, nOfs, nLength));
  }

  @Nonnull
  public static StringStreamSource create (@Nonnull final CharSequence aXML)
  {
    if (aXML instanceof String)
      return create ((String) aXML);
    return create (aXML.toString ());
  }

  @Nonnull
  public static StreamSource create (@Nullable final InputStream aIS)
  {
    return new StreamSource (aIS);
  }

  @Nonnull
  public static StreamSource create (@Nullable final Reader aReader)
  {
    return new StreamSource (aReader);
  }

  @Nonnull
  public static DOMSource create (@Nullable final Node aNode)
  {
    return new DOMSource (aNode);
  }
}
