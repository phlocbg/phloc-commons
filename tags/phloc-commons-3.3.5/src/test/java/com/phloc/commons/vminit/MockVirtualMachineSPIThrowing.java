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
package com.phloc.commons.vminit;

import com.phloc.commons.annotations.IsSPIImplementation;

@IsSPIImplementation
public final class MockVirtualMachineSPIThrowing implements IVirtualMachineEventSPI
{
  private static int s_nInstanceCount = 0;

  public MockVirtualMachineSPIThrowing ()
  {
    s_nInstanceCount++;
  }

  public void onVirtualMachineStart () throws Exception
  {
    throw new Exception ("onVirtualMachineStart");
  }

  public void onVirtualMachineStop () throws Exception
  {
    throw new Exception ("onVirtualMachineStop");
  }

  public static int getInstanceCount ()
  {
    return s_nInstanceCount;
  }
}
