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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.transform.stream.StreamResult;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Special {@link StreamResult} implementation that writes to {@link String}
 * objects.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class StringStreamResult extends StreamResult implements IHasStringRepresentation
{
  private final NonBlockingStringWriter m_aSW;

  public StringStreamResult ()
  {
    this (null);
  }

  public StringStreamResult (@Nullable final String sSystemID)
  {
    super (new NonBlockingStringWriter ());
    m_aSW = (NonBlockingStringWriter) getWriter ();
    setSystemId (sSystemID);
  }

  @Nonnull
  public String getAsString ()
  {
    return m_aSW.getAsString ();
  }

  @Nonnull
  public char [] getAsCharArray ()
  {
    return m_aSW.getAsCharArray ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("stringWriter", m_aSW).append ("systemID", getSystemId ()).toString ();
  }
}
