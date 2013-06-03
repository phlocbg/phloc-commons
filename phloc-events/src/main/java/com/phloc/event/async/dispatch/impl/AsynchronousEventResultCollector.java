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
package com.phloc.event.async.dispatch.impl;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.aggregate.IAggregator;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;

public final class AsynchronousEventResultCollector extends Thread implements
                                                                  INonThrowingRunnableWithParameter <Object>
{
  private final CountDownLatch m_aCountDown;
  private final List <Object> m_aResults = new Vector <Object> ();
  private final int m_nExpectedResults;
  private final IAggregator <Object, ?> m_aResultAggregator;
  private final INonThrowingRunnableWithParameter <Object> m_aResultCallback;

  public AsynchronousEventResultCollector (@Nonnegative final int nObserverWithReturn,
                                           @Nonnull final IAggregator <Object, ?> aResultAggregator,
                                           @Nonnull final INonThrowingRunnableWithParameter <Object> aResultCallback)
  {
    super ("event-result-collector-thread");
    if (nObserverWithReturn < 1)
      throw new IllegalArgumentException ("too little observers");
    if (aResultAggregator == null)
      throw new NullPointerException ("resultAggregator");
    if (aResultCallback == null)
      throw new NullPointerException ("resultCallback");

    m_nExpectedResults = nObserverWithReturn;
    m_aResultAggregator = aResultAggregator;
    m_aResultCallback = aResultCallback;
    m_aCountDown = new CountDownLatch (m_nExpectedResults);
  }

  // Called from each observer upon completion
  public void run (final Object aObserverResult)
  {
    m_aResults.add (aObserverResult);
    m_aCountDown.countDown ();
  }

  @Override
  public void run ()
  {
    try
    {
      // Wait until all results are present
      m_aCountDown.await ();

      // We have all
      m_aResultCallback.run (m_aResultAggregator.aggregate (m_aResults));
    }
    catch (final InterruptedException ex)
    {
      ex.printStackTrace ();
    }
  }
}
