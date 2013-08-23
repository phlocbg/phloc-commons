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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link MimeTypeParser}
 * 
 * @author Philip Helger
 */
public final class MimeTypeParserTest extends AbstractPhlocTestCase
{
  @Test
  public void testIsToken ()
  {
    assertFalse (MimeTypeParser.isToken (null));
    assertFalse (MimeTypeParser.isToken (""));
    assertFalse (MimeTypeParser.isToken ("  "));
    assertFalse (MimeTypeParser.isToken (" any"));
    assertFalse (MimeTypeParser.isToken ("any "));
    assertFalse (MimeTypeParser.isToken ("charset="));

    assertTrue (MimeTypeParser.isToken ("a"));
    assertTrue (MimeTypeParser.isToken ("param"));
    assertTrue (MimeTypeParser.isToken ("param1"));
    assertTrue (MimeTypeParser.isToken ("charset"));
  }

  @Test
  public void testCreateFromString ()
  {
    IMimeType mt;
    assertNull (MimeTypeParser.createFromString (null));
    assertNull (MimeTypeParser.createFromString (""));
    assertNull (MimeTypeParser.createFromString ("text"));
    // Invalid content type
    assertNull (MimeTypeParser.createFromString ("foo/bar"));

    mt = MimeTypeParser.createFromString ("text/x");
    assertNotNull (mt);
    assertSame (EMimeContentType.TEXT, mt.getContentType ());
    assertEquals ("x", mt.getContentSubType ());

    mt = MimeTypeParser.createFromString ("text/x;");
    assertNotNull (mt);
    assertSame (EMimeContentType.TEXT, mt.getContentType ());
    assertEquals ("x", mt.getContentSubType ());

    mt = MimeTypeParser.createFromString ("text/x;param1=x;param2=y");
    assertNotNull (mt);
    assertSame (EMimeContentType.TEXT, mt.getContentType ());
    assertEquals ("x", mt.getContentSubType ());
  }
}
