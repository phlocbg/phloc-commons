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
package com.phloc.event.impl.helper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.aggregate.IAggregator;
import com.phloc.commons.concurrent.IExecutorServiceFactory;
import com.phloc.event.IEventObservingExceptionHandler;
import com.phloc.event.async.dispatch.IAsynchronousEventDispatcherFactory;
import com.phloc.event.async.dispatch.impl.parallel.DefaultAsynchronousParallelEventDispatcherFactory;
import com.phloc.event.async.dispatch.impl.queue.DefaultAsynchronousQueueEventDispatcherFactory;
import com.phloc.event.async.dispatch.impl.serial.DefaultAsynchronousSerialEventDispatcherFactory;
import com.phloc.event.async.impl.NewThreadPoolExecutorServiceFactory;
import com.phloc.event.async.mgr.impl.BidirectionalAsynchronousMulticastEventManager;
import com.phloc.event.async.mgr.impl.BidirectionalAsynchronousUnicastEventManager;
import com.phloc.event.async.mgr.impl.UnidirectionalAsynchronousMulticastEventManager;
import com.phloc.event.async.mgr.impl.UnidirectionalAsynchronousUnicastEventManager;
import com.phloc.event.resultaggregator.impl.DispatchResultAggregatorUseFirst;

public final class AsynchronousEventHelper extends AbstractEventHelper
{
  private enum EAsyncDispatcher
  {
    QUEUE,
    SERIAL,
    PARALLEL;
  }

  private AsynchronousEventHelper ()
  {}

  @Nonnull
  public static IExecutorServiceFactory createExecutorServiceFactory ()
  {
    return new NewThreadPoolExecutorServiceFactory (10);
  }

  @Nonnull
  private static EAsyncDispatcher _getDefaultDispatcherType ()
  {
    return EAsyncDispatcher.QUEUE;
  }

  @Nonnull
  public static IAsynchronousEventDispatcherFactory createEventDispFactory (@Nonnull final Class <? extends IAggregator <Object, ?>> aClass,
                                                                            @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    // switch between parallel, serial and queue
    switch (_getDefaultDispatcherType ())
    {
      case QUEUE:
        return new DefaultAsynchronousQueueEventDispatcherFactory (createDispatchResultAggregatorFactory (aClass),
                                                                   aExceptionHandler);
      case SERIAL:
        return new DefaultAsynchronousSerialEventDispatcherFactory (createDispatchResultAggregatorFactory (aClass),
                                                                    aExceptionHandler);
      case PARALLEL:
        return new DefaultAsynchronousParallelEventDispatcherFactory (createDispatchResultAggregatorFactory (aClass),
                                                                      createExecutorServiceFactory (),
                                                                      aExceptionHandler);
      default:
        throw new IllegalStateException ("Illegal event dispatcher type!");
    }
  }

  @Nonnull
  public static UnidirectionalAsynchronousUnicastEventManager createUnidirectionalUnicastEventManager ()
  {
    // No need for aggregation here
    return new UnidirectionalAsynchronousUnicastEventManager (createEventDispFactory (DispatchResultAggregatorUseFirst.class,
                                                                                      null));
  }

  @Nonnull
  public static BidirectionalAsynchronousUnicastEventManager createBidirectionalUnicastEventManager ()
  {
    return new BidirectionalAsynchronousUnicastEventManager (createEventDispFactory (DispatchResultAggregatorUseFirst.class,
                                                                                     null));
  }

  @Nonnull
  public static UnidirectionalAsynchronousMulticastEventManager createUnidirectionalMulticastEventManager ()
  {
    // No need for aggregation here
    return new UnidirectionalAsynchronousMulticastEventManager (getObserverQueueFactory (),
                                                                createEventDispFactory (DispatchResultAggregatorUseFirst.class,
                                                                                        null));
  }

  @Nonnull
  public static BidirectionalAsynchronousMulticastEventManager createBidirectionalMulticastEventManager (@Nonnull final Class <? extends IAggregator <Object, ?>> aClass)
  {
    return createBidirectionalMulticastEventManager (aClass, null);
  }

  @Nonnull
  public static BidirectionalAsynchronousMulticastEventManager createBidirectionalMulticastEventManager (@Nonnull final Class <? extends IAggregator <Object, ?>> aClass,
                                                                                                         @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    return new BidirectionalAsynchronousMulticastEventManager (getObserverQueueFactory (),
                                                               createEventDispFactory (aClass, aExceptionHandler));
  }
}
