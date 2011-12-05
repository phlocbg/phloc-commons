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
package com.phloc.commons;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for class {@link GlobalDebug}
 * 
 * @author philip
 */
public final class GlobalDebugTest
{
  @Test
  public void testInstance ()
  {
    final GlobalDebug g = new GlobalDebug ();
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    g.setProductionMode (true);
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertTrue (GlobalDebug.isProductionMode ());

    g.setProductionMode (false);
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    g.setDebugMode (true);
    assertFalse (GlobalDebug.isTraceMode ());
    assertTrue (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    g.setTraceMode (true);
    assertTrue (GlobalDebug.isTraceMode ());
    assertTrue (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    g.setDebugMode (false);
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    g.setTraceMode (true);
    assertTrue (GlobalDebug.isTraceMode ());
    assertTrue (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());
  }

  @Test
  public void testStatic ()
  {
    GlobalDebug.setTraceModeDirect (false);
    assertFalse (GlobalDebug.isTraceMode ());
    assertTrue (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    GlobalDebug.setTraceModeDirect (true);
    assertTrue (GlobalDebug.isTraceMode ());
    assertTrue (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    GlobalDebug.setDebugModeDirect (false);
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    GlobalDebug.setDebugModeDirect (true);
    assertFalse (GlobalDebug.isTraceMode ());
    assertTrue (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());

    GlobalDebug.setProductionModeDirect (true);
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertTrue (GlobalDebug.isProductionMode ());

    GlobalDebug.setProductionModeDirect (false);
    assertFalse (GlobalDebug.isTraceMode ());
    assertFalse (GlobalDebug.isDebugMode ());
    assertFalse (GlobalDebug.isProductionMode ());
  }

  @Test
  public void testSetToDefault ()
  {
    GlobalDebug.setProductionModeDirect (GlobalDebug.DEFAULT_PRODUCTION_MODE);
    GlobalDebug.setDebugModeDirect (GlobalDebug.DEFAULT_DEBUG_MODE);
    GlobalDebug.setTraceModeDirect (GlobalDebug.DEFAULT_TRACE_MODE);
  }
}
