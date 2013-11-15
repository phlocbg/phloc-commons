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
package com.phloc.commons.xml.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.NonBlockingStringWriter;
import com.phloc.commons.xml.EXMLIncorrectCharacterHandling;
import com.phloc.commons.xml.EXMLVersion;
import com.phloc.commons.xml.XMLFactory;
import com.phloc.commons.xml.serialize.XMLReader;
import com.phloc.commons.xml.serialize.XMLWriter;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Test class for class {@link XMLTransformerFactory}.
 * 
 * @author Philip Helger
 */
public final class XMLTransformerFactoryTest
{
  @Test
  public void testGetDefaultTransformerFactory ()
  {
    final TransformerFactory fac = XMLTransformerFactory.getDefaultTransformerFactory ();
    assertNotNull (fac);
    // Must be the same!
    assertSame (fac, XMLTransformerFactory.getDefaultTransformerFactory ());
  }

  @Test
  public void testNewTransformer ()
  {
    Transformer t1 = XMLTransformerFactory.newTransformer ();
    assertNotNull (t1);
    assertTrue (t1 != XMLTransformerFactory.newTransformer ());

    // Read valid XSLT
    t1 = XMLTransformerFactory.newTransformer (new ClassPathResource ("xml/test1.xslt"));
    assertNotNull (t1);

    // Read valid XSLT (with import)
    t1 = XMLTransformerFactory.newTransformer (new ClassPathResource ("xml/test2.xslt"));
    assertNotNull (t1);

    // Read invalid XSLT
    assertNull (XMLTransformerFactory.newTransformer (new ClassPathResource ("test1.txt")));

    try
    {
      XMLTransformerFactory.newTransformer ((IReadableResource) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLTransformerFactory.newTransformer ((Source) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testNewTemplates ()
  {
    // Read valid XSLT
    Templates t1 = XMLTransformerFactory.newTemplates (new ClassPathResource ("xml/test1.xslt"));
    assertNotNull (t1);

    // Read valid XSLT (with import)
    t1 = XMLTransformerFactory.newTemplates (new ClassPathResource ("xml/test2.xslt"));
    assertNotNull (t1);
    t1 = XMLTransformerFactory.newTemplates (TransformSourceFactory.create (new ClassPathResource ("xml/test2.xslt")));
    assertNotNull (t1);

    // Read invalid XSLT
    assertNull (XMLTransformerFactory.newTemplates (new ClassPathResource ("test1.txt")));
    assertNull (XMLTransformerFactory.newTemplates (TransformSourceFactory.create (new ClassPathResource ("test1.txt"))));

    try
    {
      XMLTransformerFactory.newTemplates ((IReadableResource) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLTransformerFactory.newTemplates ((Source) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLTransformerFactory.newTemplates (null, (IReadableResource) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLTransformerFactory.newTemplates (null, (Source) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testCustomFactory ()
  {
    final TransformerFactory fac = XMLTransformerFactory.createTransformerFactory (new CollectingTransformErrorListener (),
                                                                                   new LoggingTransformURIResolver ());
    assertNotNull (fac);

    // Read valid XSLT
    Templates t1 = XMLTransformerFactory.newTemplates (fac, new ClassPathResource ("xml/test1.xslt"));
    assertNotNull (t1);

    // Read valid XSLT
    t1 = XMLTransformerFactory.newTemplates (fac,
                                             new CachingTransformStreamSource (new ClassPathResource ("xml/test1.xslt")));
    assertNotNull (t1);

    // Read valid XSLT (with import)
    t1 = XMLTransformerFactory.newTemplates (fac, new ClassPathResource ("xml/test2.xslt"));
    assertNotNull (t1);

    // Read invalid XSLT
    assertNull (XMLTransformerFactory.newTemplates (fac, new ClassPathResource ("test1.txt")));

    try
    {
      XMLTransformerFactory.newTemplates (fac, (IReadableResource) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      XMLTransformerFactory.newTemplates (fac, (Source) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testSpecialChars () throws Exception
  {
    final Document aDoc = XMLFactory.newDocument (EXMLVersion.XML_11);
    final Node aRoot = aDoc.appendChild (aDoc.createElement ("root"));
    final char [] aChars = new char [256];
    for (int i = 0; i < 256; ++i)
      if (i == 0 || (i >= 127 && i <= 159))
      {
        // Not in attrs
        aChars[i] = '.';
      }
      else
        aChars[i] = (char) i;
    final String sAll = new String (aChars);
    if (true)
      ((Element) aRoot).setAttribute ("test", sAll);
    aRoot.appendChild (aDoc.createTextNode (sAll));
    final NonBlockingStringWriter aSW = new NonBlockingStringWriter ();
    XMLTransformerFactory.newTransformer ().transform (new DOMSource (aDoc), new StreamResult (aSW));
    final String sTransform = aSW.getAsString ();
    final String sXML = XMLWriter.getNodeAsString (aDoc,
                                                   new XMLWriterSettings ().setIncorrectCharacterHandling (EXMLIncorrectCharacterHandling.WRITE_TO_FILE_NO_LOG));
    System.out.println (sTransform);
    System.out.println ();
    System.out.println (sXML);
    XMLReader.readXMLDOM (sTransform);
    XMLReader.readXMLDOM (sXML);
  }
}
