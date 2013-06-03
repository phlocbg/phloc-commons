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
package com.phloc.event.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.aggregate.IAggregator;
import com.phloc.commons.aggregate.IAggregatorFactory;
import com.phloc.commons.collections.pair.IReadonlyPair;
import com.phloc.commons.collections.pair.ReadonlyPair;
import com.phloc.event.EEventObserverHandlerType;
import com.phloc.event.IEvent;
import com.phloc.event.IEventObserver;
import com.phloc.event.IOnlyOnceEventObserver;
import com.phloc.event.observerqueue.IEventObserverQueue;

public abstract class AbstractEventDispatcher
{
  protected final IAggregator <Object, Object> m_aResultAggregator;

  public AbstractEventDispatcher (@Nonnull final IAggregatorFactory <Object, Object> aResultAggregatorFactory)
  {
    if (aResultAggregatorFactory == null)
      throw new NullPointerException ("resultAggregatorFactory");

    m_aResultAggregator = aResultAggregatorFactory.create ();
    if (m_aResultAggregator == null)
      throw new IllegalArgumentException ("No dispatch result aggregator was created");
  }

  @Nonnull
  protected static final IReadonlyPair <Integer, Map <IEventObserver, EEventObserverHandlerType>> getListOfObserversThatCanHandleTheEvent (@Nonnull final IEvent aEvent,
                                                                                                                                           @Nonnull final IEventObserverQueue aObservers)
  {
    // find all handling observers
    final Map <IEventObserver, EEventObserverHandlerType> aHandler = new LinkedHashMap <IEventObserver, EEventObserverHandlerType> ();
    int nHandlingObserverCountWithReturnValue = 0;
    for (final IEventObserver aObserver : aObservers.getAllObservers ())
    {
      final EEventObserverHandlerType eHandleType = aObserver.canHandleEvent (aEvent);
      if (eHandleType.isHandling ())
      {
        aHandler.put (aObserver, eHandleType);
        if (eHandleType.hasReturnValue ())
          nHandlingObserverCountWithReturnValue++;
      }
    }

    // remove all "only once" observers
    // Note: we're operating on a copy of the original container!
    // Note: iterate only the "handling" observers
    for (final IEventObserver aObserver : aHandler.keySet ())
      if (aObserver instanceof IOnlyOnceEventObserver)
        if (aObservers.removeObserver (aObserver).isUnchanged ())
          throw new IllegalStateException ("Failed to remove observer " + aObserver + " from " + aObservers);

    // return number of handling + handling observer map
    return ReadonlyPair.create (Integer.valueOf (nHandlingObserverCountWithReturnValue), aHandler);
  }
}
