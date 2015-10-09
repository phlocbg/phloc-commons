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
package com.phloc.event.sync.dispatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.IStoppable;
import com.phloc.event.IEvent;
import com.phloc.event.observerqueue.IEventObserverQueue;

/**
 * Dispatch events synchronously.
 * 
 * @author philip
 */
public interface ISynchronousEventDispatcher extends IStoppable
{
  /**
   * Dispatch an event to a number of observers in a synchronized way.
   * 
   * @param aEvent
   *        The event to be dispatched. May not be <code>null</code>.
   * @param aObservers
   *        The list of available observers. They need to be queried whether
   *        they are interested in the event. May not be <code>null</code>.
   * @return The return values of the event based on the implemented result
   *         aggregator. May be <code>null</code>.
   */
  @Nullable
  Object dispatch (@Nonnull IEvent aEvent, @Nonnull IEventObserverQueue aObservers);
}
