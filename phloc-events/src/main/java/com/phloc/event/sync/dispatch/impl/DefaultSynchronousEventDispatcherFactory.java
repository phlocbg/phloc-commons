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
package com.phloc.event.sync.dispatch.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.aggregate.IAggregatorFactory;
import com.phloc.event.IEventObservingExceptionHandler;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcher;
import com.phloc.event.sync.dispatch.ISynchronousEventDispatcherFactory;

public class DefaultSynchronousEventDispatcherFactory implements ISynchronousEventDispatcherFactory
{
  private final IAggregatorFactory <Object, Object> m_aResultAggregateFactory;
  private final IEventObservingExceptionHandler m_aExceptionHandler;

  public DefaultSynchronousEventDispatcherFactory (@Nonnull final IAggregatorFactory <Object, Object> aResultAggregateFactory,
                                                   @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    if (aResultAggregateFactory == null)
      throw new NullPointerException ("resultAggregatorFactory");
    m_aResultAggregateFactory = aResultAggregateFactory;
    m_aExceptionHandler = aExceptionHandler;
  }

  @Nonnull
  public ISynchronousEventDispatcher create ()
  {
    return new SynchronousEventDispatcher (m_aResultAggregateFactory, m_aExceptionHandler);
  }
}
