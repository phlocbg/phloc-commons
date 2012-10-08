/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.test.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.deadlock.IThreadDeadlockListener;
import com.phloc.commons.deadlock.ThreadDeadlockDetectionTimer;
import com.phloc.commons.deadlock.ThreadDeadlockInfo;

public final class MainDeadLock
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainDeadLock.class);

  private static final class LoggingInfoListener implements IThreadDeadlockListener
  {
    public void onDeadlockDetected (final ThreadDeadlockInfo [] deadlockedThreads)
    {
      System.err.println ("Deadlocked Threads:");
      System.err.println ("-------------------");
      for (final ThreadDeadlockInfo threadi : deadlockedThreads)
      {
        final Thread thread = threadi.getThread ();
        System.err.println (thread);
        for (final StackTraceElement ste : thread.getStackTrace ())
        {
          System.err.println ("\t" + ste);
        }
      }
    }
  }

  private static final class A
  {
    private final Lock lock1 = new ReentrantLock ();
    private final Lock lock2 = new ReentrantLock ();

    public void f ()
    {
      lock1.lock ();
      try
      {
        lock2.lock ();
        try
        {}
        finally
        {
          lock2.unlock ();
        }
      }
      finally
      {
        lock1.unlock ();
      }
    }

    public void g ()
    {
      lock2.lock ();
      try
      {
        f ();
      }
      finally
      {
        lock2.unlock ();
      }
    }
  }

  public static void main (final String [] args) throws Exception
  {

    final LoggingInfoListener aListener = new LoggingInfoListener ();
    final ThreadDeadlockDetectionTimer tdc = new ThreadDeadlockDetectionTimer ();
    tdc.addListener (aListener);

    final A a = new A ();
    final Thread t1 = new Thread (new Runnable ()
    {
      public void run ()
      {
        while (true)
          a.f ();
      }
    }, "t1");
    final Thread t2 = new Thread (new Runnable ()
    {
      public void run ()
      {
        a.g ();
      }
    }, "t2");
    t1.start ();
    t2.start ();
    s_aLogger.info ("Waiting");
    t1.join ();
    t2.join ();
    s_aLogger.info ("Success");
  }
}
