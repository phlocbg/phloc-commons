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
package com.phloc.commons.mock;

import org.junit.Test;

/**
 * Test class for class {@link MockException}.
 * 
 * @author Philip Helger
 */
public final class MockExceptionTest
{
  @Test (expected = MockException.class)
  public void testEmpty () throws MockException
  {
    throw new MockException ();
  }

  @Test (expected = MockException.class)
  public void testWithMessage () throws MockException
  {
    throw new MockException ("msg");
  }

  @Test (expected = MockException.class)
  public void testWithException () throws MockException
  {
    throw new MockException (new Exception ());
  }

  @Test (expected = MockException.class)
  public void testWithExceptionAndMessage () throws MockException
  {
    throw new MockException ("msg", new Exception ());
  }
}
