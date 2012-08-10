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
package com.phloc.commons.xml.sax;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.NotThreadSafe;

import org.xml.sax.InputSource;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * {@link org.xml.sax.InputSource} that ensures that the passed
 * {@link InputStream} is closed. This is achieved by copying the content in a
 * {@link NonBlockingByteArrayInputStream}.
 * 
 * @author philip
 */
@NotThreadSafe
public final class CachingSAXInputSource extends InputSource
{
  @Nonnull
  private static NonBlockingByteArrayInputStream _getCachedInputStream (@Nonnull @WillClose final InputStream aIS)
  {
    return new NonBlockingByteArrayInputStream (StreamUtils.getAllBytes (aIS));
  }

  public CachingSAXInputSource (@Nonnull final IReadableResource aRes)
  {
    this (aRes.getInputStream (), aRes.getResourceID ());
  }

  public CachingSAXInputSource (@Nonnull final IInputStreamProvider aISP)
  {
    this (aISP.getInputStream (), null);
  }

  public CachingSAXInputSource (@Nonnull final IInputStreamProvider aISP, @Nullable final String sSystemID)
  {
    this (aISP.getInputStream (), sSystemID);
  }

  public CachingSAXInputSource (@Nonnull @WillClose final InputStream aIS)
  {
    this (aIS, null);
  }

  public CachingSAXInputSource (@Nonnull @WillClose final InputStream aIS, @Nullable final String sSystemID)
  {
    super (_getCachedInputStream (aIS));
    setSystemId (sSystemID);
  }
}
