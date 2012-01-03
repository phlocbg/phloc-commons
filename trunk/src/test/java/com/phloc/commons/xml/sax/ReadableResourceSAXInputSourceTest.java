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
package com.phloc.commons.xml.sax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Test;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ReadableResourceSAXInputSource}.
 * 
 * @author philip
 */
public final class ReadableResourceSAXInputSourceTest
{
  @Test
  public void testAll ()
  {
    final IReadableResource aRes = new ClassPathResource ("xml/list.xml");
    final ReadableResourceSAXInputSource is = new ReadableResourceSAXInputSource (aRes);
    assertNotNull (StreamUtils.getAllBytes (is.getByteStream ()));
    PhlocTestUtils.testToStringImplementation (is);

    try
    {
      new ReadableResourceSAXInputSource (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new ReadableResourceSAXInputSource (null, "sysid");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new ReadableResourceSAXInputSource (new IInputStreamProvider ()
      {
        public InputStream getInputStream ()
        {
          return null;
        }
      }, "sysid").getByteStream ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
  }
}
