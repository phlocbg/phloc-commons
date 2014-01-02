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
package com.phloc.commons.state.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;

/**
 * Test class for class {@link SuccessWithValue}.
 * 
 * @author Philip Helger
 */
public final class SuccessWithValueTest
{
  @Test
  public void testAll ()
  {
    SuccessWithValue <String> x = new SuccessWithValue <String> (ESuccess.SUCCESS, "bla");
    assertTrue (x.isSuccess ());
    assertFalse (x.isFailure ());
    assertEquals ("bla", x.get ());
    assertEquals ("bla", x.getIfSuccess ("other"));
    assertEquals ("bla", x.getIfSuccessOrNull ());
    assertEquals ("other", x.getIfFailure ("other"));
    assertNull (x.getIfFailureOrNull ());

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (x, SuccessWithValue.createSuccess ("bla"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (x, SuccessWithValue.createFailure ("bla"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (x, SuccessWithValue.createSuccess ("Other"));

    x = new SuccessWithValue <String> (ESuccess.SUCCESS, null);
    assertNull (x.get ());

    try
    {
      new SuccessWithValue <String> (null, "bla");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
