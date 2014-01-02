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
package com.phloc.types.dyntypes.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.phloc.commons.CGlobal;
import com.phloc.commons.factory.FactoryNull;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;
import com.phloc.commons.xml.XMLFactory;
import com.phloc.commons.xml.serialize.XMLWriter;

/**
 * Test class for class {@link DynamicValueDOMNode}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueDOMNodeTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final Document aDoc = XMLFactory.newDocument ();
    final Node eRoot = aDoc.appendChild (aDoc.createElement ("root"));
    eRoot.appendChild (aDoc.createTextNode ("text"));

    DynamicValueDOMNode aDTD = new DynamicValueDOMNode ();
    assertEquals (Node.class, aDTD.getNativeClass ());
    assertNull (aDTD.getValue ());
    assertNull (aDTD.getCastedValue (Integer.class));
    aDTD = new DynamicValueDOMNode (XMLFactory.newDocument ());
    assertTrue (XMLFactory.newDocument ().isEqualNode (aDTD.getValue ()));
    aDTD.setValue (aDoc);
    assertTrue (aDoc.isEqualNode (aDTD.getValue ()));
    assertEquals ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                  CGlobal.LINE_SEPARATOR +
                  "<root>text</root>" +
                  CGlobal.LINE_SEPARATOR, aDTD.getAsSerializationText ());
    assertEquals (ESuccess.SUCCESS,
                  aDTD.setAsSerializationText ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<root>text2</root>"));
    assertEquals (ESuccess.FAILURE, aDTD.setAsSerializationText ("bla no XML"));
    try
    {
      aDTD.setValue (FactoryNull.getInstance ());
      fail ();
    }
    catch (final TypeConverterException ex)
    {}

    final Document aDoc2 = XMLFactory.newDocument ();
    final Node eRoot2 = aDoc2.appendChild (aDoc2.createElement ("root"));
    eRoot2.appendChild (aDoc2.createTextNode ("text2"));
    assertEquals (XMLWriter.getXMLString (aDoc2), XMLWriter.getXMLString (aDTD.getValue ()));

    // Check display text
    assertEquals ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                  CGlobal.LINE_SEPARATOR +
                  "<root>text2</root>" +
                  CGlobal.LINE_SEPARATOR, aDTD.getAsDisplayText (L_DE));
    // Check null
    assertTrue (aDTD.setValue (null).isChanged ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));
    assertTrue (aDTD.setAsSerializationText (null).isSuccess ());
    assertNull (aDTD.getAsSerializationText ());
    assertNull (aDTD.getAsDisplayText (L_DE));

    PhlocTestUtils.testGetClone (aDTD);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueDOMNode (aDoc),
                                                                    new DynamicValueDOMNode (aDoc));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueDOMNode (aDoc),
                                                                        new DynamicValueDOMNode (XMLFactory.newDocument ()));
  }
}
