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
package com.phloc.event.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.event.IEvent;
import com.phloc.event.IEventType;

/**
 * Default implementation of the {@link IEvent} interface that only takes an
 * event type and has no additional parameters.
 *
 * @author philip
 */
@Immutable
public class BaseEvent implements IEvent
{
  private final IEventType m_aEventType;

  public BaseEvent (@Nonnull final IEventType aEventType)
  {
    if (aEventType == null)
      throw new NullPointerException ("eventType");
    m_aEventType = aEventType;
  }

  @Nonnull
  public final IEventType getEventType ()
  {
    return m_aEventType;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final BaseEvent rhs = (BaseEvent) o;
    return m_aEventType.equals (rhs.getEventType ());
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aEventType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("eventType", m_aEventType).toString ();
  }
}
