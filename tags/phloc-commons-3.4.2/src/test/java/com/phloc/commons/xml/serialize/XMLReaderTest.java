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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.validation.Schema;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.xml.sax.CachingSAXInputSource;
import com.phloc.commons.xml.sax.CollectingSAXErrorHandler;
import com.phloc.commons.xml.sax.LoggingSAXErrorHandler;
import com.phloc.commons.xml.sax.ReadableResourceSAXInputSource;
import com.phloc.commons.xml.sax.StringSAXInputSource;
import com.phloc.commons.xml.schema.XMLSchemaCache;

/**
 * Test class for {@link XMLReader}
 * 
 * @author philip
 */
public final class XMLReaderTest
{
  @Test
  public void testReadString ()
  {}

  /**
   * Test method readXMLDOM
   * 
   * @throws SAXException
   */
  @Test
  public void testReadXMLDOMInputSource () throws SAXException
  {
    Document doc = XMLReader.readXMLDOM ("<root/>");
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringReader ("<root/>"));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new InputSource (new StringReader ("<root/>")));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new InputSource (new StringReader ("<?xml version=\"1.0\"?>\n" + "<root/>")));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream ("<?xml version=\"1.0\"?>\n<root/>".getBytes ()));
    assertNotNull (doc);

    try
    {
      // null reader not allowed
      XMLReader.readXMLDOM ((InputSource) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // non-XML
      XMLReader.readXMLDOM (new InputSource (new StringReader ("")));
      fail ();
    }
    catch (final SAXException ex)
    {}

    try
    {
      // non-XML
      XMLReader.readXMLDOM (new InputSource (new StringReader ("<bla>")));
      fail ();
    }
    catch (final SAXException ex)
    {}
  }

  /**
   * Test method readXMLDOM
   * 
   * @throws SAXException
   */
  @Test
  public void testReadXMLDOMString () throws SAXException
  {
    Document doc = XMLReader.readXMLDOM ("<root/>");
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM ("<?xml version=\"1.0\"?>\n" + "<root/>");
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM ("<?xml version=\"1.0\"?>\n" + "<root></root>");
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM ("<?xml version=\"1.0\"?>\n" + "<root><![CDATA[x<>]]></root>");
    assertNotNull (doc);

    try
    {
      // null reader not allowed
      XMLReader.readXMLDOM ((Reader) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // null string not allowed
      XMLReader.readXMLDOM ((String) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // non-XML
      XMLReader.readXMLDOM ("");
      fail ();
    }
    catch (final SAXException ex)
    {}
  }

  /**
   * Test method readXMLDOM
   * 
   * @throws SAXException
   */
  @Test
  public void testReadXMLDOMInputStream () throws SAXException
  {
    Document doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream ("<root/>".getBytes ()));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream ("<?xml version=\"1.0\"?>\n<root/>".getBytes ()));
    assertNotNull (doc);

    try
    {
      // null reader not allowed
      XMLReader.readXMLDOM ((InputStream) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // non-XML
      XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream (new byte [0]));
      fail ();
    }
    catch (final SAXException ex)
    {}

    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream ("<?xml version=\"1.0\"?>\n<root/>".getBytes ()));
    assertNotNull (doc);
  }

  @Test
  public void testReadWithSchema () throws SAXException
  {
    final Schema aSchema = XMLSchemaCache.getInstance ().getSchema (new ClassPathResource ("xml/schema1.xsd"));
    assertNotNull (aSchema);

    // read valid
    final String sValid = "<?xml version='1.0'?><root xmlns='http://www.example.org/schema1'><a>1</a><b>2</b></root>";
    Document doc = XMLReader.readXMLDOM (sValid, aSchema);
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (sValid, LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (sValid, aSchema, LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    XMLReader.readXMLDOM (new StringReader (sValid), aSchema);
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringReader (sValid), new CollectingSAXErrorHandler ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringReader (sValid), aSchema, LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream (sValid.getBytes ()), aSchema);
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream (sValid.getBytes ()),
                                LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream (sValid.getBytes ()),
                                aSchema,
                                LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sValid), aSchema);
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sValid), LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sValid), aSchema, LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-valid.xml"), aSchema);
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-valid.xml"), LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-valid.xml"),
                                aSchema,
                                LoggingSAXErrorHandler.getInstance ());
    assertNotNull (doc);

    // Read invalid (<c> tag is unknown)
    final String sInvalid = "<?xml version='1.0'?><root xmlns='http://www.example.org/schema1'><a>1</a><b>2</b><c>3</c></root>";
    doc = XMLReader.readXMLDOM (sInvalid, aSchema);
    assertNull (doc);
    doc = XMLReader.readXMLDOM (sInvalid, aSchema, LoggingSAXErrorHandler.getInstance ());
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringReader (sInvalid), aSchema);
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringReader (sInvalid), aSchema, LoggingSAXErrorHandler.getInstance ());
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream (sInvalid.getBytes ()), aSchema);
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingByteArrayInputStream (sInvalid.getBytes ()),
                                aSchema,
                                LoggingSAXErrorHandler.getInstance ());
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sInvalid), aSchema);
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sInvalid), aSchema, LoggingSAXErrorHandler.getInstance ());
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-invalid.xml"), aSchema);
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-invalid.xml"),
                                aSchema,
                                LoggingSAXErrorHandler.getInstance ());
    assertNull (doc);

    try
    {
      XMLReader.readXMLDOM ((Reader) null, aSchema);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLReader.readXMLDOM ((InputStream) null, aSchema);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLReader.readXMLDOM ((InputSource) null, aSchema);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLReader.readXMLDOM ((IReadableResource) null, aSchema);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testReadProcessingInstruction () throws SAXException
  {
    // Read file with processing instruction
    final Document doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/xml-processing-instruction.xml"));
    assertNotNull (doc);

    // Write again
    assertNotNull (XMLWriter.getXMLString (doc));
  }

  @Test
  public void testReadNotation () throws SAXException
  {
    // Read file with processing instruction
    final Document doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/xml-notation.xml"));
    assertNotNull (doc);

    // Write again
    assertNotNull (XMLWriter.getXMLString (doc));
  }

  @Test
  public void testOtherSources () throws SAXException
  {
    assertNotNull (XMLReader.readXMLDOM (new CachingSAXInputSource (new ClassPathResource ("xml/buildinfo.xml"))));
    assertNotNull (XMLReader.readXMLDOM (new ReadableResourceSAXInputSource (new ClassPathResource ("xml/buildinfo.xml"))));
  }
}
