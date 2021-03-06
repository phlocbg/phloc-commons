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
package com.phloc.event.async.mgr.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.event.IEvent;
import com.phloc.event.async.dispatch.IAsynchronousEventDispatcherFactory;
import com.phloc.event.mgr.IBidirectionalAsynchronousEventManager;
import com.phloc.event.observerqueue.IEventObserverQueueFactory;

public class BidirectionalAsynchronousMulticastEventManager extends AbstractAsynchronousMulticastEventManager implements
                                                                                                             IBidirectionalAsynchronousEventManager
{
  public BidirectionalAsynchronousMulticastEventManager (final IEventObserverQueueFactory aObserverQueueFactory,
                                                         final IAsynchronousEventDispatcherFactory aEventDispatcherFactory)
  {
    super (aObserverQueueFactory, aEventDispatcherFactory);
  }

  public void trigger (@Nonnull final IEvent aEvent,
                       @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
  {
    if (!m_aObserverQueue.isEmpty ())
    {
      m_aObserverQueue.beforeDispatch ();
      m_aEventDispatcher.dispatch (aEvent, m_aObserverQueue, aResultCallback);
      m_aObserverQueue.afterDispatch ();
    }
  }
}
