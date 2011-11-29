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
package com.phloc.commons.callback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for class {@link AdapterThrowingRunnableToCallable}
 * 
 * @author philip
 */
public final class AdapterThrowingRunnableToCallableTest
{
  @Test
  public void testAll () throws Exception
  {
    final IThrowingRunnable r = new IThrowingRunnable ()
    {
      public void run () throws Exception
      {
        // empty
      }
    };
    final AdapterThrowingRunnableToCallable <Object> rc = AdapterThrowingRunnableToCallable.createAdapter (r);
    assertNull (rc.call ());
    final AdapterThrowingRunnableToCallable <String> rcs = AdapterThrowingRunnableToCallable.createAdapter (r, "abc");
    assertEquals ("abc", rcs.call ());

    try
    {
      AdapterThrowingRunnableToCallable.createAdapter (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      AdapterThrowingRunnableToCallable.createAdapter (null, "retval");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
