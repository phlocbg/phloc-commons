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
package com.phloc.event.async.dispatch.impl.parallel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.event.IEvent;
import com.phloc.event.IEventObserver;
import com.phloc.event.IEventObservingExceptionHandler;
import com.phloc.event.async.dispatch.impl.AsynchronousEventResultCollector;
import com.phloc.event.impl.EventObservingExceptionHandler;
import com.phloc.event.impl.EventObservingExceptionWrapper;

final class AsyncParallelDispatcherRunner implements Runnable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AsyncParallelDispatcherRunner.class);

  private final IEvent m_aEvent;
  private final IEventObserver m_aHandlingObserver;
  private final AsynchronousEventResultCollector m_aLocalResultCallback;
  private final IEventObservingExceptionHandler m_aExceptionHandler;

  AsyncParallelDispatcherRunner (@Nonnull final IEvent aEvent,
                                 @Nonnull final IEventObserver aObserver,
                                 final AsynchronousEventResultCollector aLocalResultCallback,
                                 @Nullable final IEventObservingExceptionHandler aExceptionHandler)
  {
    if (aEvent == null)
      throw new NullPointerException ("event");
    if (aObserver == null)
      throw new NullPointerException ("observer");
    m_aEvent = aEvent;
    m_aHandlingObserver = aObserver;
    m_aLocalResultCallback = aLocalResultCallback;
    m_aExceptionHandler = aExceptionHandler != null ? aExceptionHandler : EventObservingExceptionHandler.getInstance ();
  }

  public void run ()
  {
    try
    {
      m_aHandlingObserver.onEvent (m_aEvent, m_aLocalResultCallback);
    }
    catch (final Throwable t)
    {
      m_aExceptionHandler.handleObservingException (t);
      s_aLogger.error ("Failed to notify " + m_aHandlingObserver + " on " + m_aEvent, t);

      // Notify on exception
      if (m_aLocalResultCallback != null)
      {
        // Put exception in result list
        m_aLocalResultCallback.run (new EventObservingExceptionWrapper (m_aHandlingObserver, m_aEvent, t));
      }
    }
  }
}
