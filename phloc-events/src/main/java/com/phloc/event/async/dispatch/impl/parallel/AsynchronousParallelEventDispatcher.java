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
package com.phloc.event.async.dispatch.impl.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.aggregate.IAggregatorFactory;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.collections.pair.IReadonlyPair;
import com.phloc.commons.concurrent.IExecutorServiceFactory;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.event.EEventObserverHandlerType;
import com.phloc.event.IEvent;
import com.phloc.event.IEventObserver;
import com.phloc.event.IEventObservingExceptionHandler;
import com.phloc.event.async.dispatch.IAsynchronousEventDispatcher;
import com.phloc.event.async.dispatch.impl.AsynchronousEventResultCollector;
import com.phloc.event.impl.AbstractEventDispatcher;
import com.phloc.event.observerqueue.IEventObserverQueue;

/**
 * Event dispatcher that spawns a thread for each event and each observer. So if
 * you have 2 events with 7 observers a total of 2 * 7 threads are spawned.
 * 
 * @author philip
 */
public class AsynchronousParallelEventDispatcher extends AbstractEventDispatcher implements IAsynchronousEventDispatcher
{
  private final Lock m_aLock = new ReentrantLock ();
  private final IExecutorServiceFactory m_aExecutorServiceFactory;
  private final IEventObservingExceptionHandler m_aExceptionHandler;

  public AsynchronousParallelEventDispatcher (@Nonnull final IAggregatorFactory <Object, Object> aResultAggregatorFactory,
                                              @Nonnull final IExecutorServiceFactory aExecutorServiceFactory,
                                              @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    super (aResultAggregatorFactory);

    if (aExecutorServiceFactory == null)
      throw new NullPointerException ("The passed executor service factory is invalid");
    m_aExecutorServiceFactory = aExecutorServiceFactory;
    m_aExceptionHandler = aExceptionHandler;
  }

  public void dispatch (@Nonnull final IEvent aEvent,
                        @Nonnull final IEventObserverQueue aObservers,
                        final INonThrowingRunnableWithParameter <Object> aOverallResultCallback)
  {
    if (aEvent == null)
      throw new NullPointerException ("event");
    if (aObservers == null)
      throw new NullPointerException ("observerQueue");

    // find all observers that can handle the passed event
    final IReadonlyPair <Integer, Map <IEventObserver, EEventObserverHandlerType>> aHandlingInfo = getListOfObserversThatCanHandleTheEvent (aEvent,
                                                                                                                                            aObservers);

    final int nHandlingObserverCountWithReturnValue = aHandlingInfo.getFirst ().intValue ();
    final Map <IEventObserver, EEventObserverHandlerType> aHandlingObservers = aHandlingInfo.getSecond ();

    if (!aHandlingObservers.isEmpty ())
    {
      m_aLock.lock ();
      try
      {
        // At least one handler was found
        AsynchronousEventResultCollector aLocalResultCallback = null;
        if (nHandlingObserverCountWithReturnValue > 0)
        {
          // If we have handling observers, we need an overall result callback!
          if (aOverallResultCallback == null)
            throw new IllegalStateException ("Are you possibly using a unicast event manager and sending an event that has a return value?");

          // Create collector and start thread only if we expect a result
          aLocalResultCallback = new AsynchronousEventResultCollector (nHandlingObserverCountWithReturnValue,
                                                                       m_aResultAggregator,
                                                                       aOverallResultCallback);
          aLocalResultCallback.start ();
        }

        // Iterate all handling observers
        final List <Callable <Object>> aCallables = new ArrayList <Callable <Object>> ();
        for (final Map.Entry <IEventObserver, EEventObserverHandlerType> aEntry : aHandlingObservers.entrySet ())
        {
          aCallables.add (Executors.callable (new AsyncParallelDispatcherRunner (aEvent,
                                                                                 aEntry.getKey (),
                                                                                 aEntry.getValue ().hasReturnValue () ? aLocalResultCallback
                                                                                                                     : null,
                                                                                 m_aExceptionHandler)));
        }

        // Create a thread pool with at maximum the number of observers
        final ExecutorService aExecutor = m_aExecutorServiceFactory.getExecutorService (aHandlingObservers.size ());
        try
        {
          aExecutor.invokeAll (aCallables);
        }
        catch (final InterruptedException ex)
        {
          throw new RuntimeException (ex);
        }
        finally
        {
          aExecutor.shutdown ();
        }
      }
      finally
      {
        m_aLock.unlock ();
      }
    }
  }

  public EChange stop ()
  {
    // Nothing to do in here
    return EChange.UNCHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AsynchronousParallelEventDispatcher))
      return false;
    final AsynchronousParallelEventDispatcher rhs = (AsynchronousParallelEventDispatcher) o;
    return m_aResultAggregator.equals (rhs.m_aResultAggregator) &&
           m_aExecutorServiceFactory.equals (rhs.m_aExecutorServiceFactory);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aResultAggregator).append (m_aExecutorServiceFactory).getHashCode ();
  }
}
