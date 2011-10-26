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
package com.phloc.commons.mock;

import org.junit.Test;

/**
 * Test class for class {@link MockRuntimeException}.
 * 
 * @author philip
 */
public final class MockRuntimeExceptionTest
{
  @Test (expected = MockRuntimeException.class)
  public void testEmpty ()
  {
    throw new MockRuntimeException ();
  }

  @Test (expected = MockRuntimeException.class)
  public void testWithMessage ()
  {
    throw new MockRuntimeException ("msg");
  }

  @Test (expected = MockRuntimeException.class)
  public void testWithException ()
  {
    throw new MockRuntimeException (new Exception ());
  }

  @Test (expected = MockRuntimeException.class)
  public void testWithExceptionAndMessage ()
  {
    throw new MockRuntimeException ("msg", new Exception ());
  }
}
