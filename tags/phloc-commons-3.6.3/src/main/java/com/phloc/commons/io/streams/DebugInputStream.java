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
package com.phloc.commons.io.streams;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.string.ToStringGenerator;

/**
 * A wrapper around an {@link InputStream} that debugs read and skip actions.
 * 
 * @author philip
 */
public class DebugInputStream extends WrappedInputStream
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DebugInputStream.class);

  private long m_nPosition = 0;

  public DebugInputStream (@Nonnull final InputStream aSourceIS)
  {
    super (aSourceIS);
  }

  @Override
  public int read () throws IOException
  {
    final int ret = super.read ();
    if (ret != -1)
    {
      m_nPosition++;
      s_aLogger.info ("Read 1 byte; now at " + m_nPosition);
    }
    return ret;
  }

  @Override
  public int read (final byte b[], final int nOffset, final int nLength) throws IOException
  {
    final int ret = super.read (b, nOffset, nLength);
    if (ret != -1)
    {
      m_nPosition += ret;
      s_aLogger.info ("Read " + ret + " byte(s); now at " + m_nPosition);
    }
    return ret;
  }

  @Override
  public long skip (@Nonnegative final long n) throws IOException
  {
    final long nSkipped = super.skip (n);
    if (nSkipped > 0)
    {
      m_nPosition += nSkipped;
      s_aLogger.info ("Skipped " + nSkipped + " byte(s); now at " + m_nPosition);
    }
    return nSkipped;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("position", m_nPosition).toString ();
  }
}
