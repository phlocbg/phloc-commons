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
package com.phloc.commons.io.streams;

import java.io.Closeable;
import java.io.Flushable;

public final class MockCloseableWithState implements Closeable, Flushable
{
  private boolean m_bClosed = false;
  private boolean m_bFlushed = false;

  public MockCloseableWithState ()
  {}

  public void close ()
  {
    m_bClosed = true;
  }

  public void flush ()
  {
    m_bFlushed = true;
  }

  public boolean isClosed ()
  {
    return m_bClosed;
  }

  public boolean isFlushed ()
  {
    return m_bFlushed;
  }
}
