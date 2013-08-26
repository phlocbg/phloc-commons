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
package com.phloc.commons.supplementary.test.deadlock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.deadlock.IThreadDeadlockListener;
import com.phloc.commons.deadlock.ThreadDeadlockDetectionTimer;
import com.phloc.commons.deadlock.ThreadDeadlockInfo;

public final class MainDeadLock2
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainDeadLock2.class);

  private static final class A
  {
    private final ReadWriteLock lock1 = new ReentrantReadWriteLock ();
    private final ReadWriteLock lock2 = new ReentrantReadWriteLock ();

    public void f ()
    {
      lock1.writeLock ().lock ();
      try
      {
        lock2.writeLock ().lock ();
        try
        {}
        finally
        {
          lock2.writeLock ().unlock ();
        }
      }
      finally
      {
        lock1.writeLock ().unlock ();
      }
    }

    public void g ()
    {
      lock2.writeLock ().lock ();
      try
      {
        f ();
      }
      finally
      {
        lock2.writeLock ().unlock ();
      }
    }
  }

  public static void main (final String [] args) throws Exception
  {
    final ThreadDeadlockDetectionTimer tdc = new ThreadDeadlockDetectionTimer ();
    tdc.addListener (new IThreadDeadlockListener ()
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
    });

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
