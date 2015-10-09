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

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.event.IEvent;
import com.phloc.event.mgr.IUnidirectionalEventManager;
import com.phloc.event.observerqueue.IEventObserverQueueFactory;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcherFactory;

public class UnidirectionalSynchronousMulticastEventManager extends AbstractSynchronousMulticastEventManager implements
                                                                                                            IUnidirectionalEventManager
{
  public UnidirectionalSynchronousMulticastEventManager (final IEventObserverQueueFactory aObserverQueueFactory,
                                                         final ISynchronousEventDispatcherFactory aEventDispatcherFactory)
  {
    super (aObserverQueueFactory, aEventDispatcherFactory);
  }

  public void trigger (@Nonnull final IEvent aEvent)
  {
    if (!m_aObserverQueue.isEmpty ())
    {
      m_aObserverQueue.beforeDispatch ();
      m_aEventDispatcher.dispatch (aEvent, m_aObserverQueue);
      m_aObserverQueue.afterDispatch ();
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UnidirectionalSynchronousMulticastEventManager))
      return false;
    final UnidirectionalSynchronousMulticastEventManager rhs = (UnidirectionalSynchronousMulticastEventManager) o;
    return m_aObserverQueue.equals (rhs.m_aObserverQueue) && m_aEventDispatcher.equals (rhs.m_aEventDispatcher);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aObserverQueue).append (m_aEventDispatcher).getHashCode ();
  }
}
