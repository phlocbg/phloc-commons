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
package com.phloc.commons.changelog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroDOMInputStreamProvider;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.version.Version;
import com.phloc.commons.xml.sax.ReadableResourceSAXInputSource;
import com.phloc.commons.xml.schema.XMLSchemaCache;
import com.phloc.commons.xml.serialize.XMLReader;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link ChangeLogSerializer}.
 *
 * @author philip
 */
public final class ChangeLogSerializerTest
{
  @Test
  @SuppressFBWarnings (value = "NP_NONNULL_PARAM_VIOLATION")
  public void testRead () throws SAXException
  {
    // Read valid
    final ChangeLog aCL = ChangeLogSerializer.readChangeLog (new ClassPathResource (CChangeLog.CHANGELOG_XML_FILENAME));
    assertNotNull (aCL);
    assertEquals (new Version (1, 0), aCL.getVersion ());
    assertEquals ("phloc-commons", aCL.getComponent ());
    assertTrue (aCL.getAllEntries ().size () > 0);
    assertTrue (aCL.getAllReleases ().size () > 0);
    assertNotNull (aCL.getLatestRelease ());
    for (final EChangeLogCategory eCat : EChangeLogCategory.values ())
      assertNotNull (aCL.getAllEntriesOfCategory (eCat));

    // Read with XML Schema
    final Document aW3CDoc = XMLReader.readXMLDOM (new ClassPathResource (CChangeLog.CHANGELOG_XML_FILENAME),
                                                   XMLSchemaCache.getInstance ()
                                                                 .getFromCache (CChangeLog.CHANGELOG_XSD_10));
    assertNotNull (aW3CDoc);

    // Read invalid
    assertNull (ChangeLogSerializer.readChangeLog (null));
    assertNull (ChangeLogSerializer.readChangeLog (new ClassPathResource ("does-not-exist.xml")));

    try
    {
      aCL.getAllEntriesOfCategory (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testReadAll ()
  {
    final Map <URI, ChangeLog> aChangeLogs = ChangeLogSerializer.readAllChangeLogs ();
    assertNotNull (aChangeLogs);
    assertEquals (1, aChangeLogs.size ());
    assertEquals ("phloc-commons", ContainerHelper.getFirstValue (aChangeLogs).getComponent ());
  }

  @Test
  public void testWrite () throws SAXException
  {
    // 1. read a valid one
    final ChangeLog aCL = ChangeLogSerializer.readChangeLog (new ClassPathResource (CChangeLog.CHANGELOG_XML_FILENAME));
    assertNotNull (aCL);

    // 2. write it
    final IMicroDocument aDoc = ChangeLogSerializer.writeChangeLog (aCL);
    assertNotNull (aDoc);

    // 3. read again -> must be equal
    final ChangeLog aCL2 = ChangeLogSerializer.readChangeLog (new MicroDOMInputStreamProvider (aDoc,
                                                                                               XMLWriterSettings.DEFAULT_XML_CHARSET));
    assertNotNull (aCL2);

    // 4. read again with XML Schema
    final Document aW3CDoc = XMLReader.readXMLDOM (new ReadableResourceSAXInputSource (new MicroDOMInputStreamProvider (aDoc,
                                                                                                                        XMLWriterSettings.DEFAULT_XML_CHARSET),
                                                                                       null),
                                                   XMLSchemaCache.getInstance ()
                                                                 .getFromCache (CChangeLog.CHANGELOG_XSD_10));
    assertNotNull (aW3CDoc);

    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aCL, aCL2);

    try
    {
      ChangeLogSerializer.writeChangeLog (null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
