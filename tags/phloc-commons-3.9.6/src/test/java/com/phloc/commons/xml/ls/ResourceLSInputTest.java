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
package com.phloc.commons.xml.ls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ResourceLSInput}.
 * 
 * @author philip
 */
public final class ResourceLSInputTest
{
  @Test
  public void testDefault ()
  {
    final ResourceLSInput lsi = new ResourceLSInput (new ClassPathResource ("xml/buildinfo.xml"));
    assertNull (lsi.getBaseURI ());
    final InputStream aIS = lsi.getByteStream ();
    assertNotNull (aIS);
    StreamUtils.close (aIS);
    assertFalse (lsi.getCertifiedText ());
    assertNull (lsi.getCharacterStream ());
    assertNull (lsi.getEncoding ());
    assertNull (lsi.getPublicId ());
    assertNull (lsi.getStringData ());
    assertNotNull (lsi.getSystemId ());
    assertTrue (lsi.getSystemId ().endsWith ("buildinfo.xml"));

    lsi.setBaseURI ("any");
    assertEquals ("any", lsi.getBaseURI ());
    try
    {
      lsi.setByteStream (new NonBlockingByteArrayInputStream (new byte [1]));
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}
    lsi.setCertifiedText (true);
    assertTrue (lsi.getCertifiedText ());
    try
    {
      lsi.setCharacterStream (null);
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}
    lsi.setEncoding (CCharset.CHARSET_ISO_8859_1);
    assertEquals (CCharset.CHARSET_ISO_8859_1, lsi.getEncoding ());
    lsi.setPublicId ("pubid");
    assertEquals ("pubid", lsi.getPublicId ());
    lsi.setStringData ("str");
    assertEquals ("str", lsi.getStringData ());

    PhlocTestUtils.testToStringImplementation (lsi);

    try
    {
      new ResourceLSInput (null, "sysid");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
