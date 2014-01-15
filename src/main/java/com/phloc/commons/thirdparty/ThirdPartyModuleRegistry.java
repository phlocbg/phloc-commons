/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.thirdparty;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.state.EChange;

/**
 * This class manages all registered third party modules
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class ThirdPartyModuleRegistry
{
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Set <IThirdPartyModule> s_aModules = new LinkedHashSet <IThirdPartyModule> ();

  static
  {
    // Load all SPI implementations
    for (final IThirdPartyModuleProviderSPI aTPM : ServiceLoaderUtils.getAllSPIImplementations (IThirdPartyModuleProviderSPI.class))
    {
      final IThirdPartyModule [] aModules = aTPM.getAllThirdPartyModules ();
      if (aModules != null)
        for (final IThirdPartyModule aModule : aModules)
          registerThirdPartyModule (aModule);
    }
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ThirdPartyModuleRegistry s_aInstance = new ThirdPartyModuleRegistry ();

  private ThirdPartyModuleRegistry ()
  {}

  @Nonnull
  public static EChange registerThirdPartyModule (@Nonnull final IThirdPartyModule aModule)
  {
    if (aModule == null)
      throw new NullPointerException ("module");

    s_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (s_aModules.add (aModule));
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <IThirdPartyModule> getAllRegisteredThirdPartyModules ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (s_aModules);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
