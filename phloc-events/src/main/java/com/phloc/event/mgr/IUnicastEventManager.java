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
package com.phloc.event.mgr;

import javax.annotation.Nonnull;

import com.phloc.event.IEventObserver;

/**
 * Base interface for an unicast event manager. Unicast means having only a
 * single event observer.
 * 
 * @author philip
 */
public interface IUnicastEventManager
{
  /**
   * Set the observer to be used.
   * 
   * @param aObserver
   *        The observer to be registered. May not be <code>null</code>.
   * @throws IllegalArgumentException
   *         If the event manager already has an observer defined.
   * @see com.phloc.event.observerqueue.IEventObserverQueue
   */
  void setObserver (@Nonnull IEventObserver aObserver);
}
