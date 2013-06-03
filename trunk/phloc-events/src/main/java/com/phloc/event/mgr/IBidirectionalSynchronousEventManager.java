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
import javax.annotation.Nullable;

import com.phloc.event.IEvent;

/**
 * Interface for a bidirectional, synchronous event manager. Bidirectional means
 * that the event observer can provide a result of the event handling.
 * Synchronous means that the results are collected synchronously.
 * 
 * @author philip
 */
public interface IBidirectionalSynchronousEventManager
{
  @Nullable
  Object trigger (@Nonnull IEvent aEvent);
}
