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
package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.Charset;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;

/**
 * Test class for class {@link XMLWriterSettings}.
 * 
 * @author philip
 */
public final class XMLWriterSettingsTest
{
  @Test
  public void testDefault ()
  {
    IXMLWriterSettings mws = XMLWriterSettings.DEFAULT_XML_SETTINGS;
    assertEquals (EXMLSerializeDocType.EMIT, mws.getSerializeDocType ());
    assertEquals (EXMLSerializeComments.EMIT, mws.getSerializeComments ());
    assertEquals (XMLWriterSettings.DEFAULT_XML_CHARSET, mws.getCharset ());
    assertEquals (EXMLSerializeFormat.XML, mws.getFormat ());
    assertEquals (EXMLSerializeIndent.INDENT_AND_ALIGN, mws.getIndent ());

    mws = new XMLWriterSettings ();
    assertEquals (EXMLSerializeDocType.EMIT, mws.getSerializeDocType ());
    assertEquals (EXMLSerializeComments.EMIT, mws.getSerializeComments ());
    assertEquals (XMLWriterSettings.DEFAULT_XML_CHARSET, mws.getCharset ());
    assertEquals (EXMLSerializeFormat.XML, mws.getFormat ());
    assertEquals (EXMLSerializeIndent.INDENT_AND_ALIGN, mws.getIndent ());

    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setFormat (EXMLSerializeFormat.HTML));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setSerializeDocType (EXMLSerializeDocType.IGNORE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setSerializeComments (EXMLSerializeComments.IGNORE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setIndent (EXMLSerializeIndent.NONE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new XMLWriterSettings ().setCharset (CCharset.CHARSET_US_ASCII));

    // Now try all permutations
    final XMLWriterSettings mmws = new XMLWriterSettings ();
    for (final EXMLSerializeDocType eDocType : EXMLSerializeDocType.values ())
    {
      mmws.setSerializeDocType (eDocType);
      assertEquals (eDocType, mmws.getSerializeDocType ());
      for (final EXMLSerializeComments eComments : EXMLSerializeComments.values ())
      {
        mmws.setSerializeComments (eComments);
        assertEquals (eComments, mmws.getSerializeComments ());
        for (final EXMLSerializeFormat eFormat : EXMLSerializeFormat.values ())
        {
          mmws.setFormat (eFormat);
          assertEquals (eFormat, mmws.getFormat ());
          for (final EXMLSerializeIndent eIndent : EXMLSerializeIndent.values ())
          {
            mmws.setIndent (eIndent);
            assertEquals (eIndent, mmws.getIndent ());
            for (final EXMLIncorrectCharacterHandling eIncorrectCharHandling : EXMLIncorrectCharacterHandling.values ())
            {
              mmws.setIncorrectCharacterHandling (eIncorrectCharHandling);
              assertEquals (eIncorrectCharHandling, mmws.getIncorrectCharacterHandling ());
              for (final Charset aCS : CharsetManager.getAllCharsets ().values ())
              {
                mmws.setCharset (aCS.name ());
                assertEquals (aCS.name (), mmws.getCharset ());
                PhlocTestUtils.testDefaultImplementationWithEqualContentObject (mmws,
                                                                                new XMLWriterSettings ().setSerializeDocType (eDocType)
                                                                                                        .setSerializeComments (eComments)
                                                                                                        .setFormat (eFormat)
                                                                                                        .setIndent (eIndent)
                                                                                                        .setIncorrectCharacterHandling (eIncorrectCharHandling)
                                                                                                        .setCharset (aCS.name ()));
              }
              assertEquals (eIncorrectCharHandling, mmws.getIncorrectCharacterHandling ());
            }
            assertEquals (eIndent, mmws.getIndent ());
          }
          assertEquals (eFormat, mmws.getFormat ());
        }
        assertEquals (eComments, mmws.getSerializeComments ());
      }
      assertEquals (eDocType, mmws.getSerializeDocType ());
    }

    try
    {
      new XMLWriterSettings ().setCharset ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new XMLWriterSettings ().setFormat (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setIndent (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setSerializeDocType (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new XMLWriterSettings ().setSerializeComments (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
