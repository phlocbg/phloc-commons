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
package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.phloc.commons.SystemProperties;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.microdom.serialize.MicroWriterSettings;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.XMLFactory;

/**
 * Test whether reading of XML 1.1 documents is valid.<br>
 * Links:
 * <ul>
 * <li><a href=
 * "http://stackoverflow.com/questions/4988114/java-standard-lib-produce-wrong-xml-1-1"
 * >Link 1</a></li>
 * </ul>
 * 
 * @author philip
 */
public final class FuncTestReadWriteXML11
{
  private static final EXMLVersion XMLVERSION = EXMLVersion.XML_11;

  private static void _generateXmlFile (final String filename, final int total) throws Exception
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eMain = aDoc.appendElement ("main_tag");
    for (int i = 0; i < total; ++i)
      eMain.appendElement ("test").appendText (String.format ("%04d", Integer.valueOf (i)));
    MicroWriter.saveToStream (aDoc,
                              FileUtils.getOutputStream (new File (filename)),
                              new MicroWriterSettings ().setXMLVersion (XMLVERSION));
  }

  @Test
  public void testReadingXML11 () throws Exception
  {
    final String sFilename1 = "target/xml11test.xml";
    _generateXmlFile (sFilename1, 2500);
    final IMicroDocument aDoc = MicroReader.readMicroXML (FileUtils.getInputStream (sFilename1));
    assertNotNull (aDoc);

    final String sFilename2 = "target/xml11test2.xml";
    MicroWriter.saveToStream (aDoc,
                              FileUtils.getOutputStream (new File (sFilename2)),
                              new MicroWriterSettings ().setXMLVersion (XMLVERSION));
    final IMicroDocument aDoc2 = MicroReader.readMicroXML (FileUtils.getInputStream (sFilename2));
    assertNotNull (aDoc2);

    // When using JAXP with Java 1.6.0_22 or 1.6.0_29 (tested only with this
    // version) the following test fails. That's why xerces must be included!
    assertTrue ("Documents are different when written to XML 1.1!\nUsed document builder: " +
                XMLFactory.getDocumentBuilder ().getClass ().toString () +
                "\nJava version: " +
                SystemProperties.getJavaVersion (), aDoc.isEqualContent (aDoc2));
  }
}
