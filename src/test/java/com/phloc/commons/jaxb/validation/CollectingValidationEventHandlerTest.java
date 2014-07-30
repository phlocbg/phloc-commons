/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.jaxb.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.jaxb.JAXBContextCache;
import com.phloc.commons.jaxb.MockJAXBArchive;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.xml.transform.TransformSourceFactory;

/**
 * Test class for class {@link CollectingValidationEventHandler}.
 * 
 * @author Philip Helger
 */
public final class CollectingValidationEventHandlerTest
{
  @Test
  public void testReadWrite () throws JAXBException
  {
    final JAXBContext aCtx = JAXBContextCache.getInstance ().getFromCache (MockJAXBArchive.class);
    CollectingValidationEventHandler evh = new CollectingValidationEventHandler (new LoggingValidationEventHandler (null));
    // is equal to:
    evh = new CollectingValidationEventHandlerFactory ().create (new LoggingValidationEventHandlerFactory ().create (null));
    final Unmarshaller um = aCtx.createUnmarshaller ();
    um.setEventHandler (evh);

    // read valid
    JAXBElement <MockJAXBArchive> o = um.unmarshal (TransformSourceFactory.create (new ClassPathResource ("xml/test-archive-01.xml")),
                                                    MockJAXBArchive.class);
    assertNotNull (o);
    assertTrue (evh.getResourceErrors ().isEmpty ());

    // read invalid
    evh = new CollectingValidationEventHandler ();
    um.setEventHandler (evh);
    o = um.unmarshal (TransformSourceFactory.create (new ClassPathResource ("xml/buildinfo.xml")),
                      MockJAXBArchive.class);
    assertNotNull (o);
    assertTrue (evh.getResourceErrors ().size () > 0);

    // Read invalid (but close to valid)
    evh = new CollectingValidationEventHandler (new LoggingValidationEventHandler ());
    um.setEventHandler (evh);
    o = um.unmarshal (TransformSourceFactory.create (new ClassPathResource ("xml/test-archive-03.xml")),
                      MockJAXBArchive.class);
    assertNotNull (o);
    assertEquals (1, evh.getResourceErrors ().size ());

    // For code coverage completion
    PhlocTestUtils.testToStringImplementation (evh);
  }
}
