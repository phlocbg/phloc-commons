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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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
import com.phloc.event.IOnlyOnceEventObserver;
import com.phloc.event.async.mgr.impl.BidirectionalAsynchronousMulticastEventManager;
import com.phloc.event.async.mgr.impl.BidirectionalAsynchronousUnicastEventManager;
import com.phloc.event.async.mgr.impl.UnidirectionalAsynchronousUnicastEventManager;
import com.phloc.event.impl.AbstractEventObserver;
import com.phloc.event.impl.BaseEvent;
import com.phloc.event.impl.EventTypeRegistry;
import com.phloc.event.resultaggregator.impl.DispatchResultAggregatorUseAll;

public final class AsynchronousEventHelperTest
{
  private static Class <? extends IAggregator <Object, ?>> RES_AGG_CLASS = DispatchResultAggregatorUseAll.class;
  private static final IEventType EV_TYPE = EventTypeRegistry.createEventType (AsynchronousEventHelperTest.class.getName ());
  private static final Logger s_aLogger = LoggerFactory.getLogger (AsynchronousEventHelperTest.class);

  @Test
  public void testUnidirectionalUnicastEventManager ()
  {
    final UnidirectionalAsynchronousUnicastEventManager mgr = AsynchronousEventHelper.createUnidirectionalUnicastEventManager ();
    mgr.setObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
      }
    });
    mgr.trigger (new BaseEvent (EV_TYPE));
  }

  @Test
  public void testBidirectionalUnicastEventManager () throws InterruptedException
  {
    final CountDownLatch aCountDown = new CountDownLatch (1);
    final BidirectionalAsynchronousUnicastEventManager mgr = AsynchronousEventHelper.createBidirectionalUnicastEventManager ();
    mgr.setObserver (new AbstractEventObserver (true, EV_TYPE)
    {
      public void onEvent (final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNotNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
        aResultCallback.run ("onEvent called!");
        aCountDown.countDown ();
      }
    });
    final INonThrowingRunnableWithParameter <Object> aOverallCB = new INonThrowingRunnableWithParameter <Object> ()
    {
      public void run (final Object currentObject)
      {
        s_aLogger.info ("Got: " + currentObject);
      }
    };
    mgr.trigger (new BaseEvent (EV_TYPE), aOverallCB);
    aCountDown.await ();

    // Try triggering the event that throws an exception
    final CountDownLatch aCountDown2 = new CountDownLatch (1);
    mgr.setObserver (new AbstractEventObserver (true, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        aCountDown2.countDown ();
        throw new MockRuntimeException ();
      }
    });
    mgr.trigger (new BaseEvent (EV_TYPE), aOverallCB);
    aCountDown2.await ();
  }

  @Test
  public void testBidirectionalMulticastEventManager () throws InterruptedException
  {
    final int EXECUTIONS = 100000;
    final CountDownLatch aCountDown = new CountDownLatch (EXECUTIONS);
    final BidirectionalAsynchronousMulticastEventManager mgr = AsynchronousEventHelper.createBidirectionalMulticastEventManager (RES_AGG_CLASS);
    for (int i = 0; i < EXECUTIONS; ++i)
      mgr.registerObserver (new AbstractEventObserver (true, EV_TYPE)
      {
        public void onEvent (@Nonnull final IEvent aEvent,
                             @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
        {
          // Ensure we're called for the correct event type
          assertNotNull (aEvent);
          assertEquals (EV_TYPE, aEvent.getEventType ());

          // Check that the callback for the result is present
          assertNotNull (aResultCallback);

          aResultCallback.run ("onEvent1 called!");
          aCountDown.countDown ();
        }
      });

    final INonThrowingRunnableWithParameter <Object> aOverallCB = new INonThrowingRunnableWithParameter <Object> ()
    {
      public void run (final Object currentObject)
      {
        s_aLogger.info ("Got: " + ((List <?>) currentObject).size () + " results");
      }
    };
    mgr.trigger (new BaseEvent (EV_TYPE), aOverallCB);
    aCountDown.await ();
  }

  @Test
  public void testUnidirectionalUnicastEventManagerMultiple ()
  {
    final UnidirectionalAsynchronousUnicastEventManager mgr = AsynchronousEventHelper.createUnidirectionalUnicastEventManager ();
    mgr.setObserver (new AbstractEventObserver (false, EV_TYPE)
    {
      public void onEvent (@Nonnull final IEvent aEvent,
                           @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
      {
        assertNull (aResultCallback);
        assertEquals (EV_TYPE, aEvent.getEventType ());
      }
    });
    for (int i = 0; i < 100; ++i)
      mgr.trigger (new BaseEvent (EV_TYPE));
  }

  private static class MockAsyncObserver extends AbstractEventObserver
  {
    private final String m_sText;

    public MockAsyncObserver (final String sText)
    {
      super (true, EV_TYPE);
      m_sText = sText;
    }

    public void onEvent (@Nonnull final IEvent aEvent,
                         @Nullable final INonThrowingRunnableWithParameter <Object> aResultCallback)
    {
      aResultCallback.run (m_sText);
    }
  }

  private static class MockAsyncObserverOnlyOnce extends MockAsyncObserver implements IOnlyOnceEventObserver
  {
    public MockAsyncObserverOnlyOnce (final String sText)
    {
      super (sText);
    }
  }

  @Test
  public void testBidirectionalMulticastEventManagerOnlyOnce ()
  {
    final BidirectionalAsynchronousMulticastEventManager mgr = AsynchronousEventHelper.createBidirectionalMulticastEventManager (RES_AGG_CLASS);
    mgr.registerObserver (new MockAsyncObserver ("Hallo"));
    mgr.registerObserver (new MockAsyncObserverOnlyOnce ("Welt"));
    // trigger for the first time
    mgr.trigger (new BaseEvent (EV_TYPE), new INonThrowingRunnableWithParameter <Object> ()
    {
      public void run (final Object currentObject)
      {
        assertTrue (currentObject instanceof List <?>);
        // -> expect 2 results
        assertEquals (2, ((List <?>) currentObject).size ());
        s_aLogger.info ("1. Got: " + currentObject);
      }
    });
    // trigger for the second time
    mgr.trigger (new BaseEvent (EV_TYPE), new INonThrowingRunnableWithParameter <Object> ()
    {
      public void run (final Object currentObject)
      {
        assertTrue (currentObject instanceof List <?>);
        // -> expect 1 result
        assertEquals (1, ((List <?>) currentObject).size ());
        s_aLogger.info ("2. Got: " + currentObject);
      }
    });
    // trigger for the third time
    mgr.trigger (new BaseEvent (EV_TYPE), new INonThrowingRunnableWithParameter <Object> ()
    {
      public void run (final Object currentObject)
      {
        assertTrue (currentObject instanceof List <?>);
        // -> expect 1 result
        assertEquals (1, ((List <?>) currentObject).size ());
        s_aLogger.info ("3. Got: " + currentObject);
      }
    });
  }
}
