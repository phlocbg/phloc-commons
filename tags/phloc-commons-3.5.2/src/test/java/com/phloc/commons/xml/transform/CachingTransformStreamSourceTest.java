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
package com.phloc.commons.xml.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Test;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link CachingTransformStreamSource}.
 * 
 * @author philip
 */
public final class CachingTransformStreamSourceTest
{
  @Test
  public void testAll ()
  {
    final IReadableResource aRes = new ClassPathResource ("xml/test1.xslt");
    assertTrue (aRes.exists ());
    CachingTransformStreamSource src = new CachingTransformStreamSource (aRes);
    InputStream is = src.getInputStream ();
    assertNotNull (is);
    StreamUtils.close (is);
    is = src.getInputStream ();
    assertNotNull (is);
    StreamUtils.close (is);
    assertEquals (aRes.getResourceID (), src.getSystemId ());
    assertNull (src.getPublicId ());

    src = new CachingTransformStreamSource ((IInputStreamProvider) aRes);
    is = src.getInputStream ();
    assertNotNull (is);
    StreamUtils.close (is);
    is = src.getInputStream ();
    assertNotNull (is);
    StreamUtils.close (is);
    assertNull (src.getSystemId ());
    assertNull (src.getPublicId ());

    src = new CachingTransformStreamSource (aRes.getInputStream ());
    is = src.getInputStream ();
    assertNotNull (is);
    StreamUtils.close (is);
    is = src.getInputStream ();
    assertNotNull (is);
    StreamUtils.close (is);
    assertNull (src.getSystemId ());
    assertNull (src.getPublicId ());

    PhlocTestUtils.testToStringImplementation (src);

    try
    {
      new CachingTransformStreamSource ((IReadableResource) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new CachingTransformStreamSource ((InputStream) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new CachingTransformStreamSource ((IInputStreamProvider) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      // Input stream provider
      new CachingTransformStreamSource (new IInputStreamProvider ()
      {
        public InputStream getInputStream ()
        {
          return null;
        }
      }, "systid").getInputStream ();
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
