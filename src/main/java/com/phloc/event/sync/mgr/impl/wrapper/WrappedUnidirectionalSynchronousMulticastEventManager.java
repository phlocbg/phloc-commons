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
package com.phloc.event.sync.mgr.impl.wrapper;

import javax.annotation.Nonnull;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IStoppable;
import com.phloc.event.IEvent;
import com.phloc.event.IEventObserver;
import com.phloc.event.mgr.IMulticastEventManager;
import com.phloc.event.mgr.IUnidirectionalEventManager;
import com.phloc.event.sync.mgr.impl.UnidirectionalSynchronousMulticastEventManager;

public class WrappedUnidirectionalSynchronousMulticastEventManager implements
                                                                  IUnidirectionalEventManager,
                                                                  IMulticastEventManager,
                                                                  IStoppable
{
  private final UnidirectionalSynchronousMulticastEventManager m_aEvtMgr;

  public WrappedUnidirectionalSynchronousMulticastEventManager (@Nonnull final UnidirectionalSynchronousMulticastEventManager aEvtMgr)
  {
    if (aEvtMgr == null)
      throw new NullPointerException ("eventManager");
    m_aEvtMgr = aEvtMgr;
  }

  @Nonnull
  public EChange registerObserver (@Nonnull final IEventObserver aObserver)
  {
    return m_aEvtMgr.registerObserver (aObserver);
  }

  @Nonnull
  public EChange unregisterObserver (@Nonnull final IEventObserver aObserver)
  {
    return m_aEvtMgr.unregisterObserver (aObserver);
  }

  public void trigger (@Nonnull final IEvent aEvent)
  {
    m_aEvtMgr.trigger (aEvent);
  }

  @Nonnull
  public EChange stop ()
  {
    return m_aEvtMgr.stop ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof WrappedUnidirectionalSynchronousMulticastEventManager))
      return false;
    final WrappedUnidirectionalSynchronousMulticastEventManager rhs = (WrappedUnidirectionalSynchronousMulticastEventManager) o;
    return m_aEvtMgr.equals (rhs.m_aEvtMgr);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aEvtMgr).getHashCode ();
  }
}
