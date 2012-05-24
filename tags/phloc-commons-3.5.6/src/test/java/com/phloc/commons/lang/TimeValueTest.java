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
package com.phloc.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link TimeValue}.
 * 
 * @author philip
 */
public final class TimeValueTest
{
  @Test
  public void testAll ()
  {
    final TimeValue t = new TimeValue (TimeUnit.SECONDS, 5);
    assertEquals (TimeUnit.SECONDS, t.getTimeUnit ());
    assertEquals (5, t.getValue ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new TimeValue (TimeUnit.SECONDS, 5),
                                                                    new TimeValue (TimeUnit.SECONDS, 5));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new TimeValue (TimeUnit.SECONDS, 5),
                                                                        new TimeValue (TimeUnit.SECONDS, 4));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new TimeValue (TimeUnit.SECONDS, 5),
                                                                        new TimeValue (TimeUnit.NANOSECONDS, 5));
    try
    {
      new TimeValue (null, 5);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
