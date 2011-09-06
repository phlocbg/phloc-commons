/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.concurrent.collector;

import com.phloc.commons.callback.IThrowingCallback;

final class MockConcurrentCollectorSingle extends ConcurrentCollectorSingle <String> implements
                                                                                    IThrowingCallback <String>
{
  private int m_nPerformCount = 0;

  public MockConcurrentCollectorSingle ()
  {
    setPerformer (this);
  }

  public void execute (final String aObject)
  {
    m_nPerformCount++;
  }

  public int getPerformCount ()
  {
    return m_nPerformCount;
  }
}
