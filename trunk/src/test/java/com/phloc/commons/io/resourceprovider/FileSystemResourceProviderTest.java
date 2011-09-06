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
package com.phloc.commons.io.resourceprovider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link FileSystemResourceProvider}.
 * 
 * @author philip
 */
public final class FileSystemResourceProviderTest
{
  @Test
  public void testAll ()
  {
    final FileSystemResourceProvider aFSRP = new FileSystemResourceProvider ();
    assertNull (aFSRP.getBasePath ());
    assertFalse (aFSRP.supportsReading ("test1.txt"));
    assertFalse (aFSRP.supportsReading ("http://www.phloc.com"));
    assertTrue (aFSRP.supportsReading (new File ("test1.txt").getAbsolutePath ()));
    IReadableResource aRes = aFSRP.getReadableResource (new File ("src/test/resources/test1.txt").getAbsolutePath ());
    assertNotNull (aRes);
    assertTrue (aRes.exists ());

    final FileSystemResourceProvider aFSRP2 = new FileSystemResourceProvider ("src/test/resources");
    assertEquals (new File ("src/test/resources"), aFSRP2.getBasePath ());
    aRes = aFSRP2.getReadableResource ("test1.txt");
    assertNotNull (aRes);
    assertTrue (aRes.exists ());

    assertFalse (aFSRP.supportsReading (null));
    assertFalse (aFSRP.supportsReading (""));
    assertFalse (aFSRP.supportsWriting (null));
    assertFalse (aFSRP.supportsWriting (""));

    try
    {
      aFSRP.getReadableResource (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testEqualsAndHashcode ()
  {
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new FileSystemResourceProvider (),
                                                                    new FileSystemResourceProvider ());
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new FileSystemResourceProvider ("src/test/resources"),
                                                                    new FileSystemResourceProvider ("src/test/resources"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new FileSystemResourceProvider ("src/test/resources"),
                                                                    new FileSystemResourceProvider (new File ("src/test/resources")));
  }
}
