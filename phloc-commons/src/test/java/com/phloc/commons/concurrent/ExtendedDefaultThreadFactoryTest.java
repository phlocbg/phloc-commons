/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.concurrent;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.callback.INonThrowingRunnable;
import com.phloc.commons.mock.PhlocTestUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link ExtendedDefaultThreadFactory}.
 *
 * @author Philip Helger
 */
public final class ExtendedDefaultThreadFactoryTest
{
  @Test
  @SuppressFBWarnings (value = "DLS_DEAD_LOCAL_STORE")
  public void testAll ()
  {
    ExtendedDefaultThreadFactory x = new ExtendedDefaultThreadFactory ();
    x = new ExtendedDefaultThreadFactory ("pool");
    final Thread t = x.newThread (new INonThrowingRunnable ()
    {
      public void run ()
      {
        // nada
      }
    });
    assertNotNull (t);
    PhlocTestUtils.testToStringImplementation (x);
  }
}
