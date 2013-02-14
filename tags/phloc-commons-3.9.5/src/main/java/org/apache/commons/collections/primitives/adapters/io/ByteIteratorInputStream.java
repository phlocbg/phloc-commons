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
package org.apache.commons.collections.primitives.adapters.io;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.collections.primitives.ByteIterator;

/**
 * Adapts an {@link ByteIterator} to the {@link InputStream} interface.
 * 
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class ByteIteratorInputStream extends InputStream
{
  private final ByteIterator m_aIterator;

  public ByteIteratorInputStream (@Nonnull final ByteIterator in)
  {
    m_aIterator = in;
  }

  @Override
  public int read ()
  {
    if (m_aIterator.hasNext ())
      return (0xFF & m_aIterator.next ());
    return -1;
  }

  @Nullable
  public static InputStream adapt (@Nullable final ByteIterator in)
  {
    return null == in ? null : new ByteIteratorInputStream (in);
  }
}
