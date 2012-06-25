/**
 * Copyright (C) 2006-2012 phloc systems
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
import javax.xml.transform.stream.StreamSource;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.resource.URLResource;

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
  public static Source create (@Nonnull final File aFile)
  {
    return create (new FileSystemResource (aFile));
  }

  @Nonnull
  public static Source create (@Nonnull final URL aURL)
  {
    return create (new URLResource (aURL));
  }

  @Nonnull
  public static Source create (@Nonnull final IInputStreamProvider aISP)
  {
    return create (aISP.getInputStream ());
  }

  @Nonnull
  public static Source create (@Nonnull final IReadableResource aResource)
  {
    return new ResourceStreamSource (aResource);
  }

  @Nonnull
  public static Source create (@Nonnull final String sXML)
  {
    return new StringStreamSource (sXML);
  }

  @Nonnull
  public static Source create (@Nonnull final char [] aXML)
  {
    return create (new String (aXML));
  }

  @Nonnull
  public static Source create (@Nonnull final char [] aXML, @Nonnegative final int nOfs, @Nonnegative final int nLength)
  {
    return create (new String (aXML, nOfs, nLength));
  }

  @Nonnull
  public static Source create (@Nonnull final CharSequence aXML)
  {
    return create (aXML.toString ());
  }

  @Nonnull
  public static Source create (@Nullable final InputStream aIS)
  {
    return new StreamSource (aIS);
  }

  @Nonnull
  public static Source create (@Nullable final Reader aReader)
  {
    return new StreamSource (aReader);
  }
}
