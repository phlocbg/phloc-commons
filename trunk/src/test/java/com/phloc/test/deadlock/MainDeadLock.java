package com.phloc.test.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.test.deadlock.ThreadDeadlockDetector.Listener;

public final class MainDeadLock
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainDeadLock.class);

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
    final ThreadDeadlockDetector tdc = new ThreadDeadlockDetector ();
    tdc.addListener (new Listener ()
    {
      public void deadlockDetected (final Thread [] deadlockedThreads)
      {
        System.err.println ("Deadlocked Threads:");
        System.err.println ("-------------------");
        for (final Thread thread : deadlockedThreads)
        {
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
