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
package com.phloc.commons.mock;

import org.junit.rules.ExternalResource;

import com.phloc.commons.GlobalDebug;

/**
 * A JUnit test rule that sets global debug and trace flag for a test
 * 
 * @author philip
 */
public final class DebugModeTestRule extends ExternalResource
{
  /** The default debug flags to use. */
  public static final boolean ENABLE_GLOBAL_DEBUG = true;
  public static final boolean ENABLE_GLOBAL_TRACE = false;

  private final boolean m_bDebug;
  private final boolean m_bTrace;
  private boolean m_bOldDebug;
  private boolean m_bOldTrace;

  public DebugModeTestRule ()
  {
    this (ENABLE_GLOBAL_DEBUG, ENABLE_GLOBAL_TRACE);
  }

  public DebugModeTestRule (final boolean bDebug, final boolean bTrace)
  {
    m_bDebug = bDebug;
    m_bTrace = bTrace;
  }

  @Override
  public void before () throws Throwable
  {
    // Remember old states
    m_bOldDebug = GlobalDebug.isDebugMode ();
    m_bOldTrace = GlobalDebug.isTraceMode ();

    // Init debug stuff to state specified in ctor
    GlobalDebug.setDebugModeDirect (m_bDebug);
    GlobalDebug.setTraceModeDirect (m_bTrace);
  }

  @Override
  public void after ()
  {
    // Reset debug stuff to previous state
    GlobalDebug.setDebugModeDirect (m_bOldDebug);
    GlobalDebug.setTraceModeDirect (m_bOldTrace);
  }
}
