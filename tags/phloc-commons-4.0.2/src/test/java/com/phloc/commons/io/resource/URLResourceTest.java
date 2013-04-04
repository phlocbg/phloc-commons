/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.io.resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link URLResource}.
 * 
 * @author philip
 */
public final class URLResourceTest extends AbstractPhlocTestCase
{
  @Test
  public void testCtor () throws MalformedURLException
  {
    assertNotNull (new URLResource ("http://www.phloc.com"));
    assertNotNull (new URLResource ("http://www.phloc.com/"));
    assertNotNull (new URLResource ("http://www.phloc.com#anchor"));
    assertNotNull (new URLResource ("http://www.phloc.com?param=value"));
    assertNotNull (new URLResource ("http://www.phloc.com?param=value#anchor"));
    assertNotNull (new URLResource ("http://www.phloc.com/path"));
    assertNotNull (new URLResource ("http://www.phloc.com/path#anchor"));
    assertNotNull (new URLResource ("http://www.phloc.com/path?param=value"));
    assertNotNull (new URLResource ("http://www.phloc.com/path?param=value#anchor"));
    assertNotNull (new URLResource ("file://test.txt"));
    new URLResource ("http://www.phloc.com").exists ();
    new URLResource ("http://dfgsdfdfgsdfgsdfghhh").exists ();

    try
    {
      // no protocol
      new URLResource ("test1.txt");
      fail ();
    }
    catch (final MalformedURLException ex)
    {}

    try
    {
      new URLResource ((URL) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testAccess () throws IOException
  {
    final URL aFileURL = new File ("pom.xml").toURI ().toURL ();
    final URLResource ur = new URLResource (aFileURL);
    assertTrue (ur.exists ());
    assertTrue (ur.getResourceID ().endsWith ("/pom.xml"));
    assertTrue (ur.getPath ().endsWith ("/pom.xml"));
    final byte [] aBytes = StreamUtils.getAllBytes (ur);
    assertTrue (aBytes.length > 0);
    assertNotNull (ur.getAsURL ());
    assertNotNull (ur.getAsFile ());
    ur.getReader (CCharset.CHARSET_ISO_8859_1_OBJ).close ();

    final URL aNoNExistingURL = new File ("pom2.xml").toURI ().toURL ();
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (ur, new URLResource (aFileURL));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (ur, ur.getReadableCloneForPath (aFileURL));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (ur,
                                                                    ur.getReadableCloneForPath (aFileURL.toExternalForm ()));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (ur, new URLResource (aNoNExistingURL));

    assertNotNull (URLResource.getAsFile (aFileURL));
    assertNotNull (URLResource.getAsFile (aNoNExistingURL));
    try
    {
      URLResource.getAsFile (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      URLResource.getAsFile (new URL ("http://www.google.com"));
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    assertNotNull (URLResource.getInputStream (aFileURL));
    assertNull (URLResource.getInputStream (aNoNExistingURL));
    assertNotNull (new URLResource (aFileURL).getReader (CCharset.CHARSET_ISO_8859_1_OBJ));
    assertNull (new URLResource (aNoNExistingURL).getReader (CCharset.CHARSET_ISO_8859_1_OBJ));
    try
    {
      URLResource.getInputStream ((URL) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      ur.getReadableCloneForPath ("bla fasel");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testSerialize () throws Exception
  {
    PhlocTestUtils.testDefaultSerialization (new URLResource ("http://www.phloc.com"));
  }
}
