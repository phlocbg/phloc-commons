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
package com.phloc.commons.concurrent.collector;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.state.ESuccess;

/**
 * Base interface for a concurrent queue worker. It asynchronously collects
 * objects to handle (via the {@link #queueObject(Object)} method).
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of objects to be queued
 */
public interface IConcurrentCollector <DATATYPE>
{
  /**
   * Submit an object to the queue.
   * 
   * @param aObject
   *        The object to submit. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess queueObject (@Nonnull DATATYPE aObject);

  /**
   * @return The number of objects currently in the queue.
   */
  @Nonnegative
  int getQueueLength ();

  /**
   * Stop taking new objects in the collector. Returns directly and does not
   * wait until the processing finished.
   * 
   * @return {@link ESuccess}
   */
  @Nonnull
  ESuccess stopQueuingNewObjects ();

  /**
   * Check if this collector is already stopped.
   * 
   * @return <code>true</code> if the collector is stopped, <code>false</code>
   *         otherwise.
   */
  boolean isStopped ();
}
