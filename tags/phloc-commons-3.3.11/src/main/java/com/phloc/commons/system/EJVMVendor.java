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
package com.phloc.commons.system;

import javax.annotation.Nonnull;

import com.phloc.commons.SystemProperties;

/**
 * Determine the current JVM (Java Virtual Machine) vendor.
 * 
 * @author philip
 */
public enum EJVMVendor
{
  SUN_CLIENT
  {
    @Override
    protected boolean determineIfItIsCurrentJVMVendor ()
    {
      final boolean bIsSunJVM = SUN_VENDOR_NAME.equals (SystemProperties.getJavaVmVendor ());
      return bIsSunJVM && !_isSunServerJVM ();
    }
  },

  SUN_SERVER
  {
    @Override
    protected boolean determineIfItIsCurrentJVMVendor ()
    {
      final boolean bIsSunJVM = SUN_VENDOR_NAME.equals (SystemProperties.getJavaVmVendor ());
      return bIsSunJVM && _isSunServerJVM ();
    }
  },

  UNKNOWN
  {
    @Override
    protected boolean determineIfItIsCurrentJVMVendor ()
    {
      // Never determined as such :)
      return false;
    }
  };

  private static final String SUN_VENDOR_NAME = "Sun Microsystems Inc.";
  private static final String SYSPROP_JAVA_VM_NAME = "java.vm.name";

  private static boolean _isSunServerJVM ()
  {
    final String sVM = SystemProperties.getPropertyValue (SYSPROP_JAVA_VM_NAME);
    // Dev machine, Windows Vista 32-bit:
    if ("Java HotSpot(TM) Server VM".equals (sVM))
      return true;

    // Server machine, CentOS 64-bit:
    if ("Java HotSpot(TM) 64-Bit Server VM".equals (sVM))
      return true;

    return false;
  }

  /** The current vendor. */
  private static volatile EJVMVendor s_aInstance = null;
  private boolean m_bIsIt = false;

  private EJVMVendor ()
  {
    m_bIsIt = determineIfItIsCurrentJVMVendor ();
  }

  protected abstract boolean determineIfItIsCurrentJVMVendor ();

  public final boolean isJVMVendor ()
  {
    return m_bIsIt;
  }

  /**
   * @return <code>true</code> if this is a Sun/Oracle JVM.
   */
  public final boolean isSun ()
  {
    return this == SUN_CLIENT || this == SUN_SERVER;
  }

  /**
   * @return The current JVM vendor. If the vendor could not be determined,
   *         {@link #UNKNOWN} is returned and never <code>null</code>.
   */
  @Nonnull
  public static EJVMVendor getCurrentVendor ()
  {
    EJVMVendor ret = s_aInstance;
    if (ret == null)
    {
      // Note: double initialization doesn't matter here

      // Check
      for (final EJVMVendor eVendor : values ())
        if (eVendor.isJVMVendor ())
        {
          ret = eVendor;
          break;
        }
      if (ret == null)
        ret = UNKNOWN;
      s_aInstance = ret;
    }
    return ret;
  }
}
