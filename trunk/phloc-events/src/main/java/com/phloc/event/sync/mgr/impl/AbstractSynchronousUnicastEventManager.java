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
package com.phloc.event.sync.mgr.impl;

import javax.annotation.Nonnull;

import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IStoppable;
import com.phloc.event.IEventObserver;
import com.phloc.event.mgr.IUnicastEventManager;
import com.phloc.event.observerqueue.impl.EventObserverQueueSingleElement;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcher;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcherFactory;

/**
 * Abstract base class for synchronous unicast event managers. Unicast means
 * having only a single event observer.
 * 
 * @author philip
 */
public abstract class AbstractSynchronousUnicastEventManager implements IUnicastEventManager, IStoppable
{
  protected final ISynchronousEventDispatcher m_aEventDispatcher;
  protected final EventObserverQueueSingleElement m_aObserver;

  public AbstractSynchronousUnicastEventManager (@Nonnull final ISynchronousEventDispatcherFactory aEventDispatcherFactory)
  {
    if (aEventDispatcherFactory == null)
      throw new NullPointerException ("eventDispatcherFactory");

    m_aEventDispatcher = aEventDispatcherFactory.create ();
    if (m_aEventDispatcher == null)
      throw new IllegalStateException ("An illegal event dispatcher was created");
    m_aObserver = new EventObserverQueueSingleElement ();
  }

  public final void setObserver (@Nonnull final IEventObserver aObserver)
  {
    m_aObserver.addObserver (aObserver);
  }

  @Nonnull
  public final EChange stop ()
  {
    return m_aEventDispatcher.stop ();
  }
}
