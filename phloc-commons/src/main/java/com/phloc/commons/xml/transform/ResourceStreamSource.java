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
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.transform.stream.StreamSource;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Special {@link StreamSource} implementation that reads from
 * {@link IReadableResource} or {@link IInputStreamProvider} objects. The system
 * ID of the stream source is automatically determined from the resource or can
 * be manually passed in.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class ResourceStreamSource extends StreamSource
{
  private final IInputStreamProvider m_aISP;

  public ResourceStreamSource (@Nonnull final IReadableResource aResource)
  {
    this (aResource, aResource.getResourceID ());
  }

  public ResourceStreamSource (@Nonnull final IInputStreamProvider aISP, @Nullable final String sSystemID)
  {
    m_aISP = ValueEnforcer.notNull (aISP, "InputStreamProvider");
    setSystemId (sSystemID);
  }

  @Override
  public InputStream getInputStream ()
  {
    final InputStream aIS = m_aISP.getInputStream ();
    if (aIS == null)
      throw new IllegalStateException ("Failed to open input stream for " + m_aISP);
    return aIS;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ISP", m_aISP).append ("systemID", getSystemId ()).toString ();
  }
}
