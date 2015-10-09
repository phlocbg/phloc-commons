/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.event.sync.mgr.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.event.IEvent;
import com.phloc.event.mgr.IBidirectionalSynchronousEventManager;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcherFactory;

public class BidirectionalSynchronousUnicastEventManager extends AbstractSynchronousUnicastEventManager implements
                                                                                                       IBidirectionalSynchronousEventManager
{
  public BidirectionalSynchronousUnicastEventManager (final ISynchronousEventDispatcherFactory aEventDispatcherFactory)
  {
    super (aEventDispatcherFactory);
  }

  @Nullable
  public Object trigger (@Nonnull final IEvent aEvent)
  {
    if (m_aObserver.isEmpty ())
      return null;

    m_aObserver.beforeDispatch ();
    final Object ret = m_aEventDispatcher.dispatch (aEvent, m_aObserver);
    m_aObserver.afterDispatch ();

    return ret;
  }
}
