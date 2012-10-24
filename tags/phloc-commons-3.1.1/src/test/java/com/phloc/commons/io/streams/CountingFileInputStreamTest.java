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
package com.phloc.commons.io.streams;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link CountingFileInputStream}.
 * 
 * @author philip
 */
public final class CountingFileInputStreamTest
{
  @Test
  public void testAll () throws IOException
  {
    CountingFileInputStream aCIS = new CountingFileInputStream ("pom.xml");
    aCIS.read ();
    aCIS.read (new byte [5], 1, 1);
    StreamUtils.copyInputStreamToOutputStream (aCIS, new NonBlockingByteArrayOutputStream ());
    PhlocTestUtils.testToStringImplementation (aCIS);

    aCIS = new CountingFileInputStream (new File ("pom.xml"));
    aCIS.read ();
    aCIS.read (new byte [5], 1, 1);
    StreamUtils.copyInputStreamToOutputStream (aCIS, new NonBlockingByteArrayOutputStream ());
    PhlocTestUtils.testToStringImplementation (aCIS);
  }
}