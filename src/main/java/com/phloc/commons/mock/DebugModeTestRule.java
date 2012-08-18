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
package com.phloc.commons.mock;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.junit.rules.ExternalResource;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.OverrideOnDemand;

public class DebugModeTestRule extends ExternalResource
{
  /** The global debug flags to use. */
  public static final boolean ENABLE_GLOBAL_DEBUG = true;
  public static final boolean ENABLE_GLOBAL_TRACE = false;

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  public void before () throws Throwable
  {
    // Init global stuff
    GlobalDebug.setDebugModeDirect (ENABLE_GLOBAL_DEBUG);
    GlobalDebug.setTraceModeDirect (ENABLE_GLOBAL_TRACE);

    // Enable testing with a security manager here :)
    if (false && System.getSecurityManager () == null)
      System.setSecurityManager (new SecurityManager ());
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  public void after ()
  {
    // Reset global stuff
    GlobalDebug.setDebugModeDirect (GlobalDebug.DEFAULT_DEBUG_MODE);
    GlobalDebug.setTraceModeDirect (GlobalDebug.DEFAULT_TRACE_MODE);
  }
}
