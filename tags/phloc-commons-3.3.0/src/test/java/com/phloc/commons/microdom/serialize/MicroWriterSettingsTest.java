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
package com.phloc.commons.microdom.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.Charset;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.serialize.EXMLSerializeComments;
import com.phloc.commons.xml.serialize.EXMLSerializeDocType;
import com.phloc.commons.xml.serialize.EXMLSerializeFormat;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;

/**
 * Test class for class {@link MicroWriterSettings}.
 * 
 * @author philip
 */
public final class MicroWriterSettingsTest
{
  @Test
  public void testDefault ()
  {
    IMicroWriterSettings mws = MicroWriterSettings.DEFAULT_XML_SETTINGS;
    assertEquals (EXMLVersion.DEFAULT, mws.getXMLVersion ());
    assertEquals (EXMLSerializeDocType.EMIT, mws.getSerializeDocType ());
    assertEquals (EXMLSerializeComments.EMIT, mws.getSerializeComments ());
    assertEquals (MicroWriterSettings.DEFAULT_XML_CHARSET, mws.getCharset ());
    assertEquals (EXMLSerializeFormat.XML, mws.getFormat ());
    assertEquals (EXMLSerializeIndent.INDENT_AND_ALIGN, mws.getIndent ());

    mws = new MicroWriterSettings ();
    assertEquals (EXMLVersion.DEFAULT, mws.getXMLVersion ());
    assertEquals (EXMLSerializeDocType.EMIT, mws.getSerializeDocType ());
    assertEquals (EXMLSerializeComments.EMIT, mws.getSerializeComments ());
    assertEquals (MicroWriterSettings.DEFAULT_XML_CHARSET, mws.getCharset ());
    assertEquals (EXMLSerializeFormat.XML, mws.getFormat ());
    assertEquals (EXMLSerializeIndent.INDENT_AND_ALIGN, mws.getIndent ());

    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new MicroWriterSettings ().setXMLVersion (EXMLVersion.XML_11));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new MicroWriterSettings ().setFormat (EXMLSerializeFormat.HTML));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new MicroWriterSettings ().setSerializeDocType (EXMLSerializeDocType.IGNORE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new MicroWriterSettings ().setSerializeComments (EXMLSerializeComments.IGNORE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new MicroWriterSettings ().setIndent (EXMLSerializeIndent.NONE));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (mws,
                                                                        new MicroWriterSettings ().setCharset (CCharset.CHARSET_US_ASCII));

    // Now try all permutations
    final MicroWriterSettings mmws = new MicroWriterSettings ();
    for (final EXMLVersion eVersion : EXMLVersion.values ())
    {
      mmws.setXMLVersion (eVersion);
      assertEquals (eVersion, mmws.getXMLVersion ());
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
              for (final Charset aCS : CharsetManager.getAllCharsets ().values ())
              {
                mmws.setCharset (aCS.name ());
                assertEquals (aCS.name (), mmws.getCharset ());
                PhlocTestUtils.testDefaultImplementationWithEqualContentObject (mmws,
                                                                                new MicroWriterSettings ().setXMLVersion (eVersion)
                                                                                                          .setSerializeDocType (eDocType)
                                                                                                          .setSerializeComments (eComments)
                                                                                                          .setFormat (eFormat)
                                                                                                          .setIndent (eIndent)
                                                                                                          .setCharset (aCS.name ()));
              }
              assertEquals (eIndent, mmws.getIndent ());
            }
            assertEquals (eFormat, mmws.getFormat ());
          }
          assertEquals (eComments, mmws.getSerializeComments ());
        }
        assertEquals (eDocType, mmws.getSerializeDocType ());
      }
      assertEquals (eVersion, mmws.getXMLVersion ());
    }

    try
    {
      new MicroWriterSettings ().setCharset ("");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
    try
    {
      new MicroWriterSettings ().setFormat (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new MicroWriterSettings ().setIndent (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new MicroWriterSettings ().setSerializeDocType (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new MicroWriterSettings ().setSerializeComments (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      new MicroWriterSettings ().setXMLVersion (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
