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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.aggregate.IAggregator;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.mock.MockRuntimeException;
import com.phloc.event.IEvent;
import com.phloc.event.IEventType;
import com.phloc.event.impl.AbstractEventObserver;
import com.phloc.event.impl.BaseEvent;
import com.phloc.event.impl.EventTypeRegistry;
import com.phloc.event.resultaggregator.impl.DispatchResultAggregatorUseAll;
import com.phloc.event.sync.mgr.impl.BidirectionalSynchronousMulticastEventManager;
import com.phloc.event.sync.mgr.impl.UnidirectionalSynchronousMulticastEventManager;
import com.phloc.event.sync.mgr.impl.UnidirectionalSynchronousUnicastEventManager;

public final class SynchronousEventHelperTest
{
  private static Class <? extends IAggregator <Object, ?>> RES_AGG_CLASS = DispatchResultAggregatorUseAll.class;
  private static final IEventType EV_TYPE = EventTypeRegistry.createEventType (SynchronousEventHelperTest.class.getName ());
  private static final Logger s_aLogger = LoggerFactory.getLogger (SynchronousEventHelperTest.class);

  @Test
  public void testUnidirectionalUnicastEventManager ()
  {
    final UnidirectionalSynchronousUnicastEventManager mgr = SynchronousEventHelper.createUnidirectionalUnicastEventManager ();
    mgr.setObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        s_aLogger.info ("onEvent uni sync");
      }
    });
    mgr.trigger (new BaseEvent (EV_TYPE));
  }

  @Test
  public void testUnidirectionalMulticastEventManager ()
  {
    final UnidirectionalSynchronousMulticastEventManager mgr = SynchronousEventHelper.createUnidirectionalMulticastEventManager ();
    mgr.registerObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        s_aLogger.info ("onEvent multi sync 1");
      }
    });
    mgr.registerObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        s_aLogger.info ("onEvent multi sync 2");
      }
    });
    mgr.registerObserver (new AbstractEventObserver (true, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNotNull (aResultCallback);
        throw new MockRuntimeException ();
      }
    });
    mgr.trigger (new BaseEvent (EV_TYPE));
  }

  @Test
  public void testBidirectionalMulticastEventManager ()
  {
    final BidirectionalSynchronousMulticastEventManager mgr = SynchronousEventHelper.createBidirectionalMulticastEventManager (RES_AGG_CLASS);
    mgr.registerObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        s_aLogger.info ("onEvent multi sync 1");
      }
    });
    mgr.registerObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        s_aLogger.info ("onEvent multi sync 2");
      }
    });
    mgr.registerObserver (new AbstractEventObserver (true, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNotNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        s_aLogger.info ("onEvent multi sync 3");
        aResultCallback.run ("My return value");
      }
    });
    mgr.registerObserver (new AbstractEventObserver (true, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNotNull (aResultCallback);
        throw new MockRuntimeException ();
      }
    });
    s_aLogger.info ("Trigger result = " + mgr.trigger (new BaseEvent (EV_TYPE)));
  }
}
