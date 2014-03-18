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
package com.phloc.commons.idfactory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link StringIDFromGlobalPersistentIntIDFactory}.
 * 
 * @author Philip Helger
 */
public final class StringIDFromGlobalPersistentIntIDFactoryTest
{
  @Test
  public void testAll ()
  {
    GlobalIDFactory.setPersistentIntIDFactory (new MemoryStaticIntIDFactory ());
    final StringIDFromGlobalPersistentIntIDFactory x = new StringIDFromGlobalPersistentIntIDFactory ("idd");
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (x,
                                                                    new StringIDFromGlobalPersistentIntIDFactory ("idd"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (x,
                                                                        new StringIDFromGlobalPersistentIntIDFactory ("prefix"));
    assertTrue (x.getNewID ().startsWith ("idd"));

    try
    {
      new StringIDFromGlobalPersistentIntIDFactory (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
