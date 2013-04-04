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
package com.phloc.commons.mime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MimeType}
 * 
 * @author philip
 */
public final class MimeTypeTest extends AbstractPhlocTestCase
{
  @Test
  public void testCtor ()
  {
    final MimeType mt = new MimeType (EMimeContentType.TEXT, "junit");
    assertSame (EMimeContentType.TEXT, mt.getContentType ());
    assertEquals ("junit", mt.getContentSubType ());
    assertEquals ("text/junit", mt.getAsString ());
    assertEquals ("text/junit; charset=UTF-8", mt.getAsStringWithEncoding (CCharset.CHARSET_UTF_8));

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (mt, new MimeType (EMimeContentType.TEXT, "junit"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mt, new MimeType (EMimeContentType.APPLICATION,
                                                                                          "junit"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mt, new MimeType (EMimeContentType.TEXT,
                                                                                          "testng"));

    try
    {
      new MimeType (null, "foo");
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new MimeType (EMimeContentType.APPLICATION, "");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testParseFromStringWithoutEncoding ()
  {
    IMimeType mt;
    assertNull (MimeType.parseFromStringWithoutEncoding (null));
    assertNull (MimeType.parseFromStringWithoutEncoding (""));
    assertNull (MimeType.parseFromStringWithoutEncoding ("text"));
    assertNull (MimeType.parseFromStringWithoutEncoding ("foo/bar"));
    mt = MimeType.parseFromStringWithoutEncoding ("text/x");
    assertNotNull (mt);
    assertSame (EMimeContentType.TEXT, mt.getContentType ());
    assertEquals ("x", mt.getContentSubType ());
  }
}
