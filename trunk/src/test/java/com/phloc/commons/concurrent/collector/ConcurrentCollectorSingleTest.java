/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.concurrent.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.callback.IThrowingRunnableWithParameter;

/**
 * Test class for class {@link ConcurrentCollectorSingle}.
 * 
 * @author philip
 */
public final class ConcurrentCollectorSingleTest
{
  @Test
  public void testCtor ()
  {
    try
    {
      new ConcurrentCollectorSingle <String> (-1, null);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    ConcurrentCollectorSingle <String> ccm = new ConcurrentCollectorSingle <String> (5, null);
    assertEquals (0, ccm.getQueueLength ());

    try
    {
      // null not allowed
      ccm.setPerformer (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // no performer
      ccm.run ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}

    ccm = new ConcurrentCollectorSingle <String> (5, new IThrowingRunnableWithParameter <String> ()
    {
      public void run (final String aCurrentObject) throws Exception
      {}
    });
    assertNotNull (ccm);
  }

  @Test
  public void testAny () throws InterruptedException
  {
    final int nThreads = 20;
    final int nPerThreadQueueAdd = 7230;

    final MockConcurrentCollectorSingle aQueue = new MockConcurrentCollectorSingle ();
    final Thread aQueueThread = new Thread (aQueue, "MockConcurrentQueue");
    aQueueThread.start ();
    assertEquals (0, aQueue.getPerformCount ());
    // create and run producers
    {
      final Thread [] aThreads = new Thread [nThreads];
      for (int i = 0; i < nThreads; ++i)
        aThreads[i] = new Thread ("Thread" + i)
        {
          @Override
          public void run ()
          {
            for (int j = 0; j < nPerThreadQueueAdd; ++j)
              aQueue.queueObject (j + " " + toString ());
          }
        };

      for (int i = 0; i < nThreads; ++i)
        aThreads[i].start ();
      for (int i = 0; i < nThreads; ++i)
        aThreads[i].join ();
    }
    aQueue.stopQueuingNewObjects ();
    aQueueThread.join ();
    assertEquals (nThreads * nPerThreadQueueAdd, aQueue.getPerformCount ());
  }
}
