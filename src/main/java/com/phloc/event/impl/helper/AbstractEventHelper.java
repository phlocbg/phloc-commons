/**
 * Copyright (C) 2006-2014 phloc systems
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
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.aggregate.AggregatorFactoryNewInstance;
import com.phloc.commons.aggregate.IAggregator;
import com.phloc.commons.aggregate.IAggregatorFactory;
import com.phloc.event.observerqueue.IEventObserverQueueFactory;
import com.phloc.event.observerqueue.impl.DefaultEventObserverQueueFactory;

@Immutable
public abstract class AbstractEventHelper
{
  protected AbstractEventHelper ()
  {}

  @Nonnull
  public static final IAggregatorFactory <Object, Object> createDispatchResultAggregatorFactory (@Nonnull final Class <? extends IAggregator <Object, ?>> aClass)
  {
    return new AggregatorFactoryNewInstance <Object, Object> (aClass);
  }

  /**
   * @return The event observer queue factory to use.
   */
  @Nonnull
  public static final IEventObserverQueueFactory getObserverQueueFactory ()
  {
    return DefaultEventObserverQueueFactory.getInstance ();
  }
}
