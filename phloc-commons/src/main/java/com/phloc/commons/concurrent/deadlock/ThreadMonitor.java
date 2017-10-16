package com.phloc.commons.concurrent.deadlock;

/*
 * @(#)Deadlock.java  1.5 05/11/17
 */
import static java.lang.management.ManagementFactory.THREAD_MXBEAN_NAME;
import static java.lang.management.ManagementFactory.getThreadMXBean;
import static java.lang.management.ManagementFactory.newPlatformMXBeanProxy;

import java.io.IOException;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @(#)ThreadMonitor.java 1.6 05/12/22
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)ThreadMonitor.java 1.6 05/12/22
 */

/**
 * Example of using the java.lang.management API to dump stack trace and to
 * perform deadlock detection.
 * 
 * @author Mandy Chung
 * @version %% 12/22/05
 */
public class ThreadMonitor
{
  private static final Logger LOG = LoggerFactory.getLogger (ThreadMonitor.class);
  private MBeanServerConnection server;

  private final ThreadMXBean tmbean;

  private ObjectName objname;

  // default - JDK 6+ VM
  private String findDeadlocksMethodName = "findDeadlockedThreads"; //$NON-NLS-1$

  private boolean canDumpLocks = true;

  /**
   * Constructs a ThreadMonitor object to get thread information in a remote
   * JVM.
   * 
   * @param aServer
   * @throws IOException
   */
  public ThreadMonitor (final MBeanServerConnection aServer) throws IOException
  {
    this.server = aServer;
    this.tmbean = newPlatformMXBeanProxy (aServer, THREAD_MXBEAN_NAME, ThreadMXBean.class);
    try
    {
      this.objname = new ObjectName (THREAD_MXBEAN_NAME);
    }
    catch (final MalformedObjectNameException e)
    {
      // should not reach here
      final InternalError aError = new InternalError (e.getMessage ());
      aError.initCause (e);
      // ESCA-JAVA0150:
      throw aError;
    }
    parseMBeanInfo ();
  }

  /**
   * Constructs a ThreadMonitor object to get thread information in the local
   * JVM.
   */
  public ThreadMonitor ()
  {
    this.tmbean = getThreadMXBean ();
  }

  /**
   * Prints the thread dump information to logger.
   */
  public void threadDump ()
  {
    if (this.canDumpLocks)
    {
      if (this.tmbean.isObjectMonitorUsageSupported () && this.tmbean.isSynchronizerUsageSupported ())
      {
        // Print lock info if both object monitor usage
        // and synchronizer usage are supported.
        // This sample code can be modified to handle if
        // either monitor usage or synchronizer usage is supported.
        dumpThreadInfoWithLocks ();
      }
    }
    else
    {
      dumpThreadInfo ();
    }
  }

  private void dumpThreadInfo ()
  {
    LOG.error ("Full Java thread dump"); //$NON-NLS-1$
    final long [] tids = this.tmbean.getAllThreadIds ();
    final ThreadInfo [] tinfos = this.tmbean.getThreadInfo (tids, Integer.MAX_VALUE);
    for (final ThreadInfo ti : tinfos)
    {
      printThreadInfo (ti);
    }
  }

  /**
   * Prints the thread dump information with locks info to logger.
   */
  private void dumpThreadInfoWithLocks ()
  {
    LOG.error ("Full Java thread dump with locks info"); //$NON-NLS-1$

    final ThreadInfo [] tinfos = this.tmbean.dumpAllThreads (true, true);
    for (final ThreadInfo ti : tinfos)
    {
      printThreadInfo (ti);
      final LockInfo [] syncs = ti.getLockedSynchronizers ();
      printLockInfo (syncs);
    }
    LOG.error (""); //$NON-NLS-1$
  }

  private static final String INDENT = "    "; //$NON-NLS-1$

  private static void printThreadInfo (final ThreadInfo ti)
  {
    // print thread information
    printThread (ti);

    // print stack trace with locks
    final StackTraceElement [] stacktrace = ti.getStackTrace ();
    final MonitorInfo [] monitors = ti.getLockedMonitors ();
    for (int i = 0; i < stacktrace.length; i++)
    {
      final StackTraceElement ste = stacktrace[i];
      LOG.error (INDENT + "at " + ste.toString ()); //$NON-NLS-1$
      for (final MonitorInfo mi : monitors)
      {
        if (mi.getLockedStackDepth () == i)
        {
          LOG.error (INDENT + "  - locked " + mi); //$NON-NLS-1$
        }
      }
    }
    LOG.error (""); //$NON-NLS-1$
  }

  private static void printThread (final ThreadInfo ti)
  {
    final StringBuilder sb = new StringBuilder ("\"" + //$NON-NLS-1$
                                                ti.getThreadName () +
                                                "\"" + //$NON-NLS-1$
                                                " Id=" //$NON-NLS-1$
                                                +
                                                ti.getThreadId () +
                                                " in " + //$NON-NLS-1$
                                                ti.getThreadState ());
    if (ti.getLockName () != null)
    {
      sb.append (" on lock=" + ti.getLockName ()); //$NON-NLS-1$
    }
    if (ti.isSuspended ())
    {
      sb.append (" (suspended)"); //$NON-NLS-1$
    }
    if (ti.isInNative ())
    {
      sb.append (" (running in native)"); //$NON-NLS-1$
    }
    LOG.error (sb.toString ());
    if (ti.getLockOwnerName () != null)
    {
      LOG.error (INDENT + " owned by " + ti.getLockOwnerName () + " Id=" + ti.getLockOwnerId ()); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  private static void printLockInfo (final LockInfo [] locks)
  {
    LOG.error (INDENT + "Locked synchronizers: count = " + locks.length); //$NON-NLS-1$
    for (final LockInfo li : locks)
    {
      LOG.error (INDENT + "  - " + li); //$NON-NLS-1$
    }
    LOG.error (""); //$NON-NLS-1$
  }

  /**
   * Checks if any threads are deadlocked. If any, print the thread dump
   * information.
   * 
   * @return whether or not a deadlock was found
   */
  public boolean findDeadlock ()
  {
    long [] tids = null;
    if (this.findDeadlocksMethodName.equals ("findDeadlockedThreads") && //$NON-NLS-1$
        this.tmbean.isSynchronizerUsageSupported ())
    {
      tids = this.tmbean.findDeadlockedThreads ();
      if (tids == null)
      {
        return false;
      }

      LOG.error ("Deadlock found :-"); //$NON-NLS-1$
      final ThreadInfo [] infos = this.tmbean.getThreadInfo (tids, true, true);
      for (final ThreadInfo ti : infos)
      {
        printThreadInfo (ti);
        printLockInfo (ti.getLockedSynchronizers ());
        LOG.error (""); //$NON-NLS-1$
      }
    }
    else
    {
      tids = this.tmbean.findMonitorDeadlockedThreads ();
      if (tids == null)
      {
        return false;
      }
      final ThreadInfo [] infos = this.tmbean.getThreadInfo (tids, Integer.MAX_VALUE);
      for (final ThreadInfo ti : infos)
      {
        // print thread information
        printThreadInfo (ti);
      }
    }

    return true;
  }

  private void parseMBeanInfo () throws IOException
  {
    try
    {
      final MBeanOperationInfo [] mopis = this.server.getMBeanInfo (this.objname).getOperations ();

      // look for findDeadlockedThreads operations;
      boolean found = false;
      for (final MBeanOperationInfo op : mopis)
      {
        if (op.getName ().equals (this.findDeadlocksMethodName))
        {
          found = true;
          break;
        }
      }
      if (!found)
      {
        // if findDeadlockedThreads operation doesn't exist,
        // the target VM is running on JDK 5 and details about
        // synchronizers and locks cannot be dumped.
        this.findDeadlocksMethodName = "findMonitorDeadlockedThreads"; //$NON-NLS-1$
        this.canDumpLocks = false;
      }
    }
    catch (final IntrospectionException e)
    {
      final InternalError ie = new InternalError (e.getMessage ());
      ie.initCause (e);
      throw ie;
    }
    catch (final InstanceNotFoundException e)
    {
      final InternalError ie = new InternalError (e.getMessage ());
      ie.initCause (e);
      throw ie;
    }
    catch (final ReflectionException e)
    {
      final InternalError ie = new InternalError (e.getMessage ());
      ie.initCause (e);
      throw ie;
    }
  }
}
