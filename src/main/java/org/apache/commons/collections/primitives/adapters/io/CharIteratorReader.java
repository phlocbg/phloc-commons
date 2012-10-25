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

import java.io.Reader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.CharIterator;

/**
 * Adapts a {@link CharIterator} to the {@link Reader} interface.
 * 
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class CharIteratorReader extends Reader
{
  private final CharIterator m_aIterator;

  public CharIteratorReader (@Nonnull final CharIterator in)
  {
    m_aIterator = in;
  }

  @Override
  public int read (final char [] buf, final int off, final int len)
  {
    if (m_aIterator.hasNext ())
    {
      int count = 0;
      while (m_aIterator.hasNext () && count < len)
      {
        buf[off + count] = m_aIterator.next ();
        count++;
      }
      return count;
    }
    return -1;
  }

  @Override
  public void close ()
  {}

  @Nullable
  public static Reader adapt (@Nullable final CharIterator in)
  {
    return null == in ? null : new CharIteratorReader (in);
  }
}
