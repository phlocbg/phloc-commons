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
package com.phloc.commons.xml.serialize;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.validation.Schema;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.phloc.commons.callback.IThrowingRunnable;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.NonBlockingStringReader;
import com.phloc.commons.io.streams.StringInputStream;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.sax.CachingSAXInputSource;
import com.phloc.commons.xml.sax.CollectingSAXErrorHandler;
import com.phloc.commons.xml.sax.LoggingSAXErrorHandler;
import com.phloc.commons.xml.sax.ReadableResourceSAXInputSource;
import com.phloc.commons.xml.sax.StringSAXInputSource;
import com.phloc.commons.xml.schema.XMLSchemaCache;

/**
 * Test class for {@link XMLReader}
 * 
 * @author Philip Helger
 */
public final class XMLReaderTest
{
  @SuppressWarnings ("unused")
  private static final Logger s_aLogger = LoggerFactory.getLogger (XMLReaderTest.class);

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
    doc = XMLReader.readXMLDOM (new NonBlockingStringReader ("<root/>"));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource ("<root/>"));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource ("<?xml version=\"1.0\"?>\n" + "<root/>"));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringInputStream ("<?xml version=\"1.0\"?>\n<root/>",
                                                       CCharset.CHARSET_ISO_8859_1_OBJ));
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
      XMLReader.readXMLDOM (new StringSAXInputSource (""));
      fail ();
    }
    catch (final SAXException ex)
    {}

    try
    {
      // non-XML
      XMLReader.readXMLDOM (new StringSAXInputSource ("<bla>"));
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
    Document doc = XMLReader.readXMLDOM (new StringInputStream ("<root/>", CCharset.CHARSET_ISO_8859_1_OBJ));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringInputStream ("<?xml version=\"1.0\"?>\n<root/>",
                                                       CCharset.CHARSET_ISO_8859_1_OBJ));
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

    doc = XMLReader.readXMLDOM (new StringInputStream ("<?xml version=\"1.0\"?>\n<root/>",
                                                       CCharset.CHARSET_ISO_8859_1_OBJ));
    assertNotNull (doc);
  }

  @Test
  public void testReadWithSchema () throws SAXException
  {
    final Schema aSchema = XMLSchemaCache.getInstance ().getSchema (new ClassPathResource ("xml/schema1.xsd"));
    assertNotNull (aSchema);

    // read valid
    final String sValid = "<?xml version='1.0'?><root xmlns='http://www.example.org/schema1'><a>1</a><b>2</b></root>";
    Document doc = XMLReader.readXMLDOM (sValid, new DOMReaderSettings ().setSchema (aSchema));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (sValid,
                                new DOMReaderSettings ().setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (sValid,
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);

    XMLReader.readXMLDOM (new NonBlockingStringReader (sValid), new DOMReaderSettings ().setSchema (aSchema));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingStringReader (sValid),
                                new DOMReaderSettings ().setErrorHandler (new CollectingSAXErrorHandler ()));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingStringReader (sValid),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);

    doc = XMLReader.readXMLDOM (new StringInputStream (sValid, CCharset.CHARSET_ISO_8859_1_OBJ),
                                new DOMReaderSettings ().setSchema (aSchema));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringInputStream (sValid, CCharset.CHARSET_ISO_8859_1_OBJ),
                                new DOMReaderSettings ().setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringInputStream (sValid, CCharset.CHARSET_ISO_8859_1_OBJ),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);

    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sValid), new DOMReaderSettings ().setSchema (aSchema));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sValid),
                                new DOMReaderSettings ().setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sValid),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));

    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-valid.xml"),
                                new DOMReaderSettings ().setSchema (aSchema));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-valid.xml"),
                                new DOMReaderSettings ().setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-valid.xml"),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNotNull (doc);

    // Read invalid (<c> tag is unknown)
    final String sInvalid = "<?xml version='1.0'?><root xmlns='http://www.example.org/schema1'><a>1</a><b>2</b><c>3</c></root>";
    doc = XMLReader.readXMLDOM (sInvalid, new DOMReaderSettings ().setSchema (aSchema));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (sInvalid,
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingStringReader (sInvalid), new DOMReaderSettings ().setSchema (aSchema));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new NonBlockingStringReader (sInvalid),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringInputStream (sInvalid, CCharset.CHARSET_ISO_8859_1_OBJ),
                                new DOMReaderSettings ().setSchema (aSchema));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringInputStream (sInvalid, CCharset.CHARSET_ISO_8859_1_OBJ),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sInvalid), new DOMReaderSettings ().setSchema (aSchema));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new StringSAXInputSource (sInvalid),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-invalid.xml"),
                                new DOMReaderSettings ().setSchema (aSchema));
    assertNull (doc);
    doc = XMLReader.readXMLDOM (new ClassPathResource ("xml/schema1-invalid.xml"),
                                new DOMReaderSettings ().setSchema (aSchema)
                                                        .setErrorHandler (LoggingSAXErrorHandler.getInstance ()));
    assertNull (doc);

    try
    {
      XMLReader.readXMLDOM ((Reader) null, new DOMReaderSettings ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLReader.readXMLDOM ((InputStream) null, new DOMReaderSettings ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLReader.readXMLDOM ((InputSource) null, new DOMReaderSettings ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLReader.readXMLDOM ((IReadableResource) null, new DOMReaderSettings ());
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

  @Test
  public void testMultithreadedDOM ()
  {
    PhlocTestUtils.testInParallel (100, new IThrowingRunnable ()
    {
      public void run () throws Exception
      {
        assertNotNull (XMLReader.readXMLDOM (new ClassPathResource ("xml/buildinfo.xml")));
      }
    });
  }
}
