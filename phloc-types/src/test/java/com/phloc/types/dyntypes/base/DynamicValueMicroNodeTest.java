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
package com.phloc.types.dyntypes.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.CGlobal;
import com.phloc.commons.factory.FactoryNull;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Test class for class {@link DynamicValueMicroNode}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueMicroNodeTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    aDoc.appendElement ("root").appendText ("text");

    DynamicValueMicroNode aDTD = new DynamicValueMicroNode ();
    assertEquals (IMicroNode.class, aDTD.getNativeClass ());
    assertNull (aDTD.getValue ());
    assertNull (aDTD.getCastedValue (Integer.class));
    aDTD = new DynamicValueMicroNode (new MicroDocument ());
    assertTrue (new MicroDocument ().isEqualContent (aDTD.getValue ()));
    aDTD.setValue (aDoc);
    assertTrue (aDoc.isEqualContent (aDTD.getValue ()));
    assertEquals ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
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

    final IMicroDocument aDoc2 = new MicroDocument ();
    aDoc2.appendElement ("root").appendText ("text2");
    assertTrue (aDoc2.isEqualContent (aDTD.getValue ()));

    // Check display text
    assertEquals ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
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
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (new DynamicValueMicroNode (aDoc),
                                                                    new DynamicValueMicroNode (aDoc));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (new DynamicValueMicroNode (aDoc),
                                                                        new DynamicValueMicroNode (new MicroDocument ()));
  }
}
