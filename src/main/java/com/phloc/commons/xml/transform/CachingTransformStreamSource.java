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
package com.phloc.commons.xml.transform;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.transform.stream.StreamSource;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.ToStringGenerator;

/**
 * {@link javax.xml.transform.Source} that ensures that the passed
 * {@link InputStream} is copied. This is achieved by copying the content in a
 * {@link NonBlockingByteArrayInputStream}.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class CachingTransformStreamSource extends StreamSource
{
  public CachingTransformStreamSource (@Nonnull final IReadableResource aResource)
  {
    this (aResource.getInputStream (), aResource.getResourceID ());
  }

  public CachingTransformStreamSource (@Nonnull final IInputStreamProvider aIIS)
  {
    this (aIIS, null);
  }

  public CachingTransformStreamSource (@Nonnull final IInputStreamProvider aIIS, @Nullable final String sSystemID)
  {
    this (aIIS.getInputStream (), sSystemID);
  }

  public CachingTransformStreamSource (@Nonnull @WillClose final InputStream aIS)
  {
    this (aIS, null);
  }

  public CachingTransformStreamSource (@Nonnull @WillClose final InputStream aIS, @Nullable final String sSystemID)
  {
    super (new NonBlockingByteArrayInputStream (StreamUtils.getAllBytes (aIS)), sSystemID);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("systemID", getSystemId ()).toString ();
  }
}
