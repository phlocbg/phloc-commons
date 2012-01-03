/**
 * Copyright (C) 2006-2012 phloc systems
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
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.callback.IThrowingRunnableWithParameter;
import com.phloc.commons.lang.GenericReflection;

/**
 * Concurrent collector that performs action on each object separately
 * 
 * @author philip
 * @param <DATATYPE>
 *        The type of the objects in the queue.
 */
public class ConcurrentCollectorSingle <DATATYPE> extends AbstractConcurrentCollector <DATATYPE>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ConcurrentCollectorSingle.class);

  private IThrowingRunnableWithParameter <DATATYPE> m_aPerformer;

  /**
   * Constructor that uses {@link #DEFAULT_MAX_QUEUE_SIZE} elements as the
   * maximum queue length.
   */
  public ConcurrentCollectorSingle ()
  {
    this (null);
  }

  /**
   * Constructor that uses {@link #DEFAULT_MAX_QUEUE_SIZE} elements as the
   * maximum queue length.
   */
  public ConcurrentCollectorSingle (@Nullable final IThrowingRunnableWithParameter <DATATYPE> aPerformer)
  {
    this (DEFAULT_MAX_QUEUE_SIZE, aPerformer);
  }

  /**
   * Constructor.
   * 
   * @param nMaxQueueSize
   *        The maximum number of items that can be in the queue. Must be &gt;
   *        0.
   */
  public ConcurrentCollectorSingle (@Nonnegative final int nMaxQueueSize,
                                    @Nullable final IThrowingRunnableWithParameter <DATATYPE> aPerformer)
  {
    super (nMaxQueueSize);
    if (aPerformer != null)
      setPerformer (aPerformer);
  }

  protected final void setPerformer (@Nonnull final IThrowingRunnableWithParameter <DATATYPE> aPerformer)
  {
    if (aPerformer == null)
      throw new NullPointerException ("performer");
    m_aPerformer = aPerformer;
  }

  private void _executeCallback (final DATATYPE aObject)
  {
    try
    {
      // Perform the action on the objects, regardless of whether a
      // "stop queue message" was received or not
      m_aPerformer.run (aObject);
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to perform actions on object - object has been lost!", t);
    }
  }

  public final void run ()
  {
    if (m_aPerformer == null)
      throw new IllegalStateException ("No performer set!");

    try
    {
      // The temporary list that contains all objects to be delivered
      while (true)
      {
        // Block until the first object is in the queue
        final Object aCurrentObject = m_aQueue.take ();
        if (aCurrentObject == STOP_QUEUE_OBJECT)
          break;
        _executeCallback (GenericReflection.<Object, DATATYPE> uncheckedCast (aCurrentObject));
      }
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Error taking elements from queue - queue has been interrupted!!!", t);
    }
  }
}
