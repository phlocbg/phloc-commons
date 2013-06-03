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
package com.phloc.event.async.mgr.impl;

import javax.annotation.Nonnull;

import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IStoppable;
import com.phloc.event.IEventObserver;
import com.phloc.event.async.dispatch.IAsynchronousEventDispatcher;
import com.phloc.event.async.dispatch.IAsynchronousEventDispatcherFactory;
import com.phloc.event.mgr.IMulticastEventManager;
import com.phloc.event.observerqueue.IEventObserverQueue;
import com.phloc.event.observerqueue.IEventObserverQueueFactory;

/**
 * Abstract base class for asynchronous multicast event managers.
 * 
 * @author philip
 */
public abstract class AbstractAsynchronousMulticastEventManager implements IMulticastEventManager, IStoppable
{
  protected final IEventObserverQueue m_aObserverQueue;
  protected final IAsynchronousEventDispatcher m_aEventDispatcher;

  public AbstractAsynchronousMulticastEventManager (@Nonnull final IEventObserverQueueFactory aObserverQueueFactory,
                                                    @Nonnull final IAsynchronousEventDispatcherFactory aEventDispatcherFactory)
  {
    if (aObserverQueueFactory == null)
      throw new NullPointerException ("observerQueueFactory");
    if (aEventDispatcherFactory == null)
      throw new NullPointerException ("eventDispatcherFactory");

    m_aObserverQueue = aObserverQueueFactory.create ();
    if (m_aObserverQueue == null)
      throw new IllegalStateException ("No observer queue was created!");
    m_aEventDispatcher = aEventDispatcherFactory.create ();
    if (m_aEventDispatcher == null)
      throw new IllegalStateException ("No event dispatcher was created!");
  }

  public final EChange registerObserver (final IEventObserver aObserver)
  {
    return m_aObserverQueue.addObserver (aObserver);
  }

  public final EChange unregisterObserver (final IEventObserver aObserver)
  {
    return m_aObserverQueue.removeObserver (aObserver);
  }

  @Nonnull
  public final EChange stop ()
  {
    return m_aEventDispatcher.stop ();
  }
}
