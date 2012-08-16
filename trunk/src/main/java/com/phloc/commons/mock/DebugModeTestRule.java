package com.phloc.commons.mock;

import org.junit.rules.ExternalResource;

import com.phloc.commons.GlobalDebug;

public class DebugModeTestRule extends ExternalResource
{
  /** The global debug flags to use. */
  public static final boolean ENABLE_GLOBAL_DEBUG = true;
  public static final boolean ENABLE_GLOBAL_TRACE = false;

  @Override
  protected void before ()
  {
    // Init global stuff
    GlobalDebug.setDebugModeDirect (ENABLE_GLOBAL_DEBUG);
    GlobalDebug.setTraceModeDirect (ENABLE_GLOBAL_TRACE);

    // Enable testing with a security manager here :)
    if (false && System.getSecurityManager () == null)
      System.setSecurityManager (new SecurityManager ());
  }

  @Override
  protected void after ()
  {
    // Reset global stuff
    GlobalDebug.setDebugModeDirect (GlobalDebug.DEFAULT_DEBUG_MODE);
    GlobalDebug.setTraceModeDirect (GlobalDebug.DEFAULT_TRACE_MODE);
  }
}
