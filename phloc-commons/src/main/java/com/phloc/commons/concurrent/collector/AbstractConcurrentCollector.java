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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.callback.INonThrowingRunnable;
import com.phloc.commons.state.ESuccess;

/**
 * Abstract concurrent collector.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of the objects in the queue.
 */
public abstract class AbstractConcurrentCollector <DATATYPE> implements INonThrowingRunnable, IConcurrentCollector <DATATYPE>
{
  /**
   * Default maximum queue size
   */
  public static final int DEFAULT_MAX_QUEUE_SIZE = 100;

  /**
   * Stop Queue
   */
  protected static final Object STOP_QUEUE_OBJECT = new Object ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractConcurrentCollector.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  // It's a list of Object because otherwise we could not use a static
  // STOP_OBJECT that works for every type. But it is ensured that the queue
  // contains only objects of type T
  /**
   * Queue
   */
  protected final BlockingQueue <Object> m_aQueue;

  // Is the queue stopped?
  private boolean m_bStopTakingNewObjects = false;

  /**
   * Constructor.
   * 
   * @param nMaxQueueSize
   *        The maximum number of items that can be in the queue. Must be &gt;
   *        0.
   */
  public AbstractConcurrentCollector (@Nonnegative final int nMaxQueueSize)
  {
    ValueEnforcer.isGT0 (nMaxQueueSize, "MaxQueueSize");
    this.m_aQueue = new ArrayBlockingQueue <Object> (nMaxQueueSize);
  }

  @Override
  @Nonnull
  public final ESuccess queueObject (@Nonnull final DATATYPE aObject)
  {
    ValueEnforcer.notNull (aObject, "Object");

    if (isStopped ())
      throw new IllegalStateException ("The queue is already stopped and does not take any more elements");

    this.m_aRWLock.writeLock ().lock ();
    try
    {
      this.m_aQueue.put (aObject);
      return ESuccess.SUCCESS;
    }
    catch (final InterruptedException ex)
    {
      s_aLogger.error ("Failed to submit object to queue", ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnegative
  public final int getQueueLength ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_aQueue.size ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public final ESuccess stopQueuingNewObjects ()
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      // put specific stop queue
      this.m_aQueue.put (STOP_QUEUE_OBJECT);
      this.m_bStopTakingNewObjects = true;
      return ESuccess.SUCCESS;
    }
    catch (final InterruptedException ex)
    {
      s_aLogger.error ("Error stopping queue", ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public final boolean isStopped ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_bStopTakingNewObjects;
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }
}
