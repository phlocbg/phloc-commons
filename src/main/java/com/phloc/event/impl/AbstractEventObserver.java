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
package com.phloc.event.impl;

import java.util.Set;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.event.EEventObserverHandlerType;
import com.phloc.event.IEvent;
import com.phloc.event.IEventObserver;
import com.phloc.event.IEventType;

/**
 * Abstract base class for a simple event observer.
 * 
 * @author philip
 */
public abstract class AbstractEventObserver implements IEventObserver
{
  private final EEventObserverHandlerType m_eHandlerType;
  private final Set <IEventType> m_aHandledEventTypes;

  /**
   * @param bWithReturnValue
   *        does not matter
   */
  @Deprecated
  @DevelopersNote ("Just to avoid the instantiation of the below constructor without an event type.")
  @UnsupportedOperation
  protected AbstractEventObserver (final boolean bWithReturnValue)
  {
    throw new UnsupportedOperationException ("No event type passed!");
  }

  public AbstractEventObserver (final boolean bWithReturnValue, @Nonnull final IEventType... aHandledEventTypes)
  {
    if (ArrayHelper.isEmpty (aHandledEventTypes))
      throw new IllegalArgumentException ("No event type is passed");
    if (ArrayHelper.containsAnyNullElement (aHandledEventTypes))
      throw new IllegalArgumentException ("Event types contain an illegal event type");

    m_eHandlerType = bWithReturnValue ? EEventObserverHandlerType.HANDLE_RETURN_VALUE
                                     : EEventObserverHandlerType.HANDLE_NO_RETURN;
    m_aHandledEventTypes = ContainerHelper.newSet (aHandledEventTypes);
  }

  public AbstractEventObserver (final boolean bWithReturnValue, @Nonnull final Iterable <IEventType> aHandledEventTypes)
  {
    if (ContainerHelper.isEmpty (aHandledEventTypes))
      throw new IllegalArgumentException ("No event type is passed");
    if (ContainerHelper.containsAnyNullElement (aHandledEventTypes))
      throw new IllegalArgumentException ("Event types contain an illegal event type");

    m_eHandlerType = bWithReturnValue ? EEventObserverHandlerType.HANDLE_RETURN_VALUE
                                     : EEventObserverHandlerType.HANDLE_NO_RETURN;
    m_aHandledEventTypes = ContainerHelper.newSet (aHandledEventTypes);
  }

  @Nonnull
  public final EEventObserverHandlerType canHandleEvent (@Nonnull final IEvent aEvent)
  {
    return m_aHandledEventTypes.contains (aEvent.getEventType ()) ? m_eHandlerType
                                                                 : EEventObserverHandlerType.CANNOT_HANDLE;
  }
}
