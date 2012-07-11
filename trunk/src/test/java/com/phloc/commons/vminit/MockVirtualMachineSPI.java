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
package com.phloc.commons.vminit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.IsSPIImplementation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@IsSPIImplementation
public final class MockVirtualMachineSPI implements IVirtualMachineEventSPI
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockVirtualMachineSPI.class);
  private static int s_nInstanceCount = 0;

  @SuppressFBWarnings ("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  public MockVirtualMachineSPI ()
  {
    s_nInstanceCount++;
  }

  public void onVirtualMachineStart ()
  {
    s_aLogger.info ("onVirtualMachineStart");
  }

  public void onVirtualMachineStop ()
  {
    s_aLogger.info ("onVirtualMachineStop");
  }

  public static int getInstanceCount ()
  {
    return s_nInstanceCount;
  }
}
