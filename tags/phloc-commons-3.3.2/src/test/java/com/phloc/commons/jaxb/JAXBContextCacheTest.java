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
package com.phloc.commons.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.transform.ResourceStreamSource;

/**
 * Test class for class {@link JAXBContextCache}.
 * 
 * @author philip
 */
public final class JAXBContextCacheTest
{
  @Test
  public void testBasic ()
  {
    assertNotNull (JAXBContextCache.getInstance ());

    try
    {
      // null not allowed
      JAXBContextCache.getInstance ().getFromCache ((Class <?>) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // null not allowed
      JAXBContextCache.getInstance ().getFromCache ((Package) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      // Not a JAXB class
      JAXBContextCache.getInstance ().getFromCache (StringHelper.class);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testReadWrite () throws JAXBException
  {
    JAXBContext aCtx = JAXBContextCache.getInstance ().getFromCache (MockJAXBArchive.class);
    assertNotNull (aCtx);

    // retrieve again
    assertNotNull (JAXBContextCache.getInstance ().getFromCache (MockJAXBArchive.class));

    CollectingValidationEventHandler evh = new CollectingValidationEventHandler (new LoggingValidationEventHandler (null));
    final Unmarshaller um = aCtx.createUnmarshaller ();
    um.setEventHandler (evh);

    // read valid
    JAXBElement <MockJAXBArchive> o = um.unmarshal (new ResourceStreamSource (new ClassPathResource ("xml/test-archive-01.xml")),
                                                    MockJAXBArchive.class);
    assertNotNull (o);
    assertEquals (0, evh.getResourceErrors ().size ());

    // read invalid
    evh = new CollectingValidationEventHandler ();
    um.setEventHandler (evh);
    o = um.unmarshal (new ResourceStreamSource (new ClassPathResource ("xml/buildinfo.xml")), MockJAXBArchive.class);
    assertNotNull (o);
    assertTrue (evh.getResourceErrors ().size () > 0);

    // Read invalid (but close to valid)
    evh = new CollectingValidationEventHandler (new LoggingValidationEventHandler ());
    um.setEventHandler (evh);
    o = um.unmarshal (new ResourceStreamSource (new ClassPathResource ("xml/test-archive-03.xml")),
                      MockJAXBArchive.class);
    assertNotNull (o);
    assertEquals (1, evh.getResourceErrors ().size ());

    // For code coverage completion
    PhlocTestUtils.testToStringImplementation (evh);

    // Clear cache
    assertTrue (JAXBContextCache.getInstance ().clearCache ().isChanged ());
    assertFalse (JAXBContextCache.getInstance ().clearCache ().isChanged ());

    // Get context again
    aCtx = JAXBContextCache.getInstance ().getFromCache (MockJAXBArchive.class);
    assertNotNull (aCtx);

    // And remove manually
    assertTrue (JAXBContextCache.getInstance ().removeFromCache (MockJAXBArchive.class.getPackage ()).isChanged ());
    assertFalse (JAXBContextCache.getInstance ().removeFromCache (MockJAXBArchive.class.getPackage ()).isChanged ());
  }
}
