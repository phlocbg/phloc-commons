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
package com.phloc.event.async.dispatch.impl.serial;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.aggregate.IAggregatorFactory;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.collections.pair.IReadonlyPair;
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
 * Event dispatcher that spawns a thread for each triggered event and notifies
 * all observers in a serial way.
 * 
 * @author philip
 */
public class AsynchronousSerialEventDispatcher extends AbstractEventDispatcher implements IAsynchronousEventDispatcher
{
  private final IEventObservingExceptionHandler m_aExceptionHandler;

  public AsynchronousSerialEventDispatcher (@Nonnull final IAggregatorFactory <Object, Object> aResultAggregatorFactory,
                                            @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    super (aResultAggregatorFactory);
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

      // Spawn a separate thread for each event that is triggered
      new AsyncSerialDispatcherThread (aEvent, aHandlingObservers, aLocalResultCallback, m_aExceptionHandler).start ();
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
    if (!(o instanceof AsynchronousSerialEventDispatcher))
      return false;
    final AsynchronousSerialEventDispatcher rhs = (AsynchronousSerialEventDispatcher) o;
    return m_aResultAggregator.equals (rhs.m_aResultAggregator);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aResultAggregator).getHashCode ();
  }
}
