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
package com.phloc.commons.vminit;

import java.util.List;

import com.phloc.commons.annotations.CodingStyleguideUnaware;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.cleanup.CommonsCleanup;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.mock.IMockException;

//ESCA-JAVA0265:
//ESCA-JAVA0267:
/**
 * This class should be run upon VM initialization. This should be the very
 * first thing to run.
 * 
 * @author philip
 */
public final class VirtualMachineInitializer
{
  private static final List <IVirtualMachineEventSPI> s_aSPIs;
  private static volatile Thread s_aShutdownThread;

  static
  {
    // Get all SPI implementations
    s_aSPIs = ServiceLoaderUtils.getAllSPIImplementations (IVirtualMachineEventSPI.class);
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final VirtualMachineInitializer s_aInstance = new VirtualMachineInitializer ();

  private VirtualMachineInitializer ()
  {}

  private static void _init ()
  {
    for (final IVirtualMachineEventSPI aSPI : s_aSPIs)
      try
      {
        aSPI.onVirtualMachineStart ();
      }
      catch (final Throwable t)
      {
        // Do not use ILogger because this may interfere with the general
        // startup!
        System.err.println ("!!! Error running VM initializer SPI " + aSPI);// NOPMD
        if (!(t instanceof IMockException))
          t.printStackTrace (System.err);
      }
  }

  private static void _done ()
  {
    for (final IVirtualMachineEventSPI aSPI : s_aSPIs)
      try
      {
        aSPI.onVirtualMachineStop ();
      }
      catch (final Throwable t)
      {
        // Do not use ILogger because this may interfere with the general
        // shutdown!
        System.err.println ("!!! Error running VM shutdown SPI " + aSPI);// NOPMD
        if (!(t instanceof IMockException))
          t.printStackTrace (System.err);
      }

    // Cleanup everything
    CommonsCleanup.cleanup ();

    // Help the GC
    s_aShutdownThread = null;
    s_aSPIs.clear ();
  }

  // ESCA-JAVA0143:
  @CodingStyleguideUnaware ("FindBugs claims that we do need synchronized here!")
  public static synchronized void runInitialization ()// NOPMD
  {
    // Only if at least one implementing class is present!
    if (s_aSPIs != null && !s_aSPIs.isEmpty ())
    {
      if (s_aShutdownThread != null)
        throw new IllegalStateException ("Already initialized!");

      // Call the SPI implementors
      _init ();

      // Define what to do upon JVM exit
      s_aShutdownThread = new Thread ("VirtualMachineInitializer.shutdown")
      {
        @Override
        public void run ()
        {
          _done ();
        }
      };
      Runtime.getRuntime ().addShutdownHook (s_aShutdownThread);
    }
  }
}
