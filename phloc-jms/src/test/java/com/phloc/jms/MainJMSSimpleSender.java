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
package com.phloc.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.phloc.commons.concurrent.ExtendedDefaultThreadFactory;
import com.phloc.commons.concurrent.ManagedExecutorService;
import com.phloc.commons.timing.StopWatch;
import com.phloc.jms.simple.IJMSMessageCreator;
import com.phloc.jms.simple.IJMSMessageHandler;
import com.phloc.jms.simple.JMSSimpleReceiver;
import com.phloc.jms.simple.JMSSimpleSender;

/**
 * Hello world!
 */
public class MainJMSSimpleSender
{
  private static final MockActiveMQJMSFactory s_aFactory = new MockActiveMQJMSFactory ();

  public static void main (final String [] args) throws Exception
  {
    final StopWatch aOSW = new StopWatch (true);

    final int nMax = 1024 * 2;
    final StopWatch aSW = new StopWatch (true);
    final ExecutorService aESProduce = Executors.newFixedThreadPool (10, new ExtendedDefaultThreadFactory ("producers"));
    for (int i = 0; i < nMax; ++i)
      aESProduce.submit (new MockProducer ());
    final long nMSP = aSW.stopAndGetMillis ();
    System.out.println ("Production of " + nMax + " messages took " + nMSP + "ms");

    aSW.restart ();
    final ExecutorService aESConsume = Executors.newFixedThreadPool (30, new ExtendedDefaultThreadFactory ("consumers"));
    for (int i = 0; i < nMax; ++i)
      aESConsume.submit (new MockConsumer ());
    final long nMSC = aSW.stopAndGetMillis ();
    System.out.println ("Consumption of " + nMax + " messages took " + nMSC + "ms");

    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aESProduce);
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aESConsume);

    System.out.println ("Overall time " + aOSW.stopAndGetMillis () + "ms");

    s_aFactory.shutdown ();
  }

  public static class MockProducer implements Runnable
  {
    private static final AtomicInteger m_aCounter = new AtomicInteger (0);

    public void run ()
    {
      new JMSSimpleSender (s_aFactory, false).sendNonTransactional ("TEST.FOO", new IJMSMessageCreator ()
      {
        @Nonnull
        public Message createMessage (@Nonnull final Session aSession) throws JMSException
        {
          return aSession.createTextMessage ("Hello world " + m_aCounter.incrementAndGet ());
        }
      });
    }
  }

  public static class MockConsumer implements Runnable
  {
    public void run ()
    {
      new JMSSimpleReceiver (s_aFactory).receiveNonTransactional ("TEST.FOO", new IJMSMessageHandler ()
      {
        public void handleMessage (@Nonnull final Message aMessage) throws JMSException
        {
          assert aMessage instanceof TextMessage;
          ((TextMessage) aMessage).getText ();
        }
      });
    }
  }
}
