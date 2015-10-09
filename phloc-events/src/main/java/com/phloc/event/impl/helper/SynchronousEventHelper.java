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
package com.phloc.event.impl.helper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.aggregate.IAggregator;
import com.phloc.event.IEventObservingExceptionHandler;
import com.phloc.event.resultaggregator.impl.DispatchResultAggregatorUseFirst;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcherFactory;
import com.phloc.event.sync.dispatch.impl.DefaultSynchronousEventDispatcherFactory;
import com.phloc.event.sync.mgr.impl.BidirectionalSynchronousMulticastEventManager;
import com.phloc.event.sync.mgr.impl.BidirectionalSynchronousUnicastEventManager;
import com.phloc.event.sync.mgr.impl.UnidirectionalSynchronousMulticastEventManager;
import com.phloc.event.sync.mgr.impl.UnidirectionalSynchronousUnicastEventManager;

@Immutable
public final class SynchronousEventHelper extends AbstractEventHelper
{
  private SynchronousEventHelper ()
  {}

  @Nonnull
  public static ISynchronousEventDispatcherFactory createSynchronousEventDispatcherFactory (@Nonnull final Class <? extends IAggregator <Object, ?>> aClass,
                                                                                            @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    return new DefaultSynchronousEventDispatcherFactory (createDispatchResultAggregatorFactory (aClass),
                                                         aExceptionHandler);
  }

  @Nonnull
  public static ISynchronousEventDispatcherFactory createSynchronousEventDispatcherFactory ()
  {
    return createSynchronousEventDispatcherFactory (DispatchResultAggregatorUseFirst.class, null);
  }

  @Nonnull
  public static UnidirectionalSynchronousUnicastEventManager createUnidirectionalUnicastEventManager ()
  {
    // No need for aggregation here
    return new UnidirectionalSynchronousUnicastEventManager (createSynchronousEventDispatcherFactory ());
  }

  @Nonnull
  public static BidirectionalSynchronousUnicastEventManager createBidirectionalUnicastEventManager ()
  {
    // No need for aggregation here
    return new BidirectionalSynchronousUnicastEventManager (createSynchronousEventDispatcherFactory ());
  }

  @Nonnull
  public static UnidirectionalSynchronousMulticastEventManager createUnidirectionalMulticastEventManager ()
  {
    return new UnidirectionalSynchronousMulticastEventManager (getObserverQueueFactory (),
                                                               createSynchronousEventDispatcherFactory ());
  }

  @Nonnull
  public static BidirectionalSynchronousMulticastEventManager createBidirectionalMulticastEventManager (@Nonnull final Class <? extends IAggregator <Object, ?>> aAggregatorClass)
  {
    return createBidirectionalMulticastEventManager (aAggregatorClass, null);
  }

  @Nonnull
  public static BidirectionalSynchronousMulticastEventManager createBidirectionalMulticastEventManager (@Nonnull final Class <? extends IAggregator <Object, ?>> aAggregatorClass,
                                                                                                        @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    return new BidirectionalSynchronousMulticastEventManager (getObserverQueueFactory (),
                                                              createSynchronousEventDispatcherFactory (aAggregatorClass,
                                                                                                       aExceptionHandler));
  }
}
