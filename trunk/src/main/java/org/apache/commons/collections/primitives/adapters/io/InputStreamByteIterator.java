/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.primitives.adapters.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.ByteIterator;

/**
 * Adapts an {@link InputStream} to the {@link ByteIterator} interface.
 * 
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class InputStreamByteIterator implements ByteIterator
{
  private final InputStream m_aStream;
  private boolean m_bNextAvailable = false;
  private int m_nNext;

  public InputStreamByteIterator (@Nonnull final InputStream in)
  {
    m_aStream = in;
  }

  public boolean hasNext ()
  {
    _ensureNextAvailable ();
    return -1 != m_nNext;
  }

  public byte next ()
  {
    if (!hasNext ())
      throw new NoSuchElementException ("No next element");
    m_bNextAvailable = false;
    return (byte) m_nNext;
  }

  /**
   * Not supported.
   * 
   * @throws UnsupportedOperationException
   */
  public void remove () throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException ("remove() is not supported here");
  }

  private void _ensureNextAvailable ()
  {
    if (!m_bNextAvailable)
      _readNext ();
  }

  private void _readNext ()
  {
    try
    {
      m_nNext = m_aStream.read ();
      m_bNextAvailable = true;
    }
    catch (final IOException e)
    {
      throw new RuntimeException (e);
    }
  }

  @Nullable
  public static ByteIterator adapt (@Nullable final InputStream in)
  {
    return null == in ? null : new InputStreamByteIterator (in);
  }
}
