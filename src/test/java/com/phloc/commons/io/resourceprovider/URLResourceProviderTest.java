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
package com.phloc.commons.io.resourceprovider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link URLResourceProvider}.
 * 
 * @author Philip Helger
 */
public final class URLResourceProviderTest
{
  @Test
  public void testAll ()
  {
    final URLResourceProvider aURLRP = new URLResourceProvider ();
    assertFalse (aURLRP.supportsReading ("test1.txt"));
    assertTrue (aURLRP.supportsReading ("http://www.phloc.com"));
    final IReadableResource aRes = aURLRP.getReadableResource ("http://www.phloc.com");
    assertNotNull (aRes);

    assertFalse (aURLRP.supportsReading (null));

    try
    {
      aURLRP.getReadableResource (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testEqualsAndHashcode ()
  {
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new URLResourceProvider (),
                                                                    new URLResourceProvider ());
  }
}
