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
package com.phloc.commons.microdom.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.microdom.EMicroEvent;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link MicroEvent}.
 * 
 * @author philip
 */
public final class MicroEventTest
{
  @Test
  public void testBasic ()
  {
    final IMicroElement e1 = MicroFactory.newElement ("a1");
    final IMicroElement e2 = MicroFactory.newElement ("a1");
    MicroEvent e = new MicroEvent (EMicroEvent.NODE_INSERTED, e1, e2);
    assertEquals (EMicroEvent.NODE_INSERTED, e.getEventType ());
    assertSame (e1, e.getSourceNode ());
    assertSame (e2, e.getTargetNode ());
    PhlocTestUtils.testToStringImplementation (e);

    e = new MicroEvent (EMicroEvent.NODE_INSERTED, null, null);
    assertEquals (EMicroEvent.NODE_INSERTED, e.getEventType ());
    assertNull (e.getSourceNode ());
    assertNull (e.getTargetNode ());
    PhlocTestUtils.testToStringImplementation (e);

    try
    {
      new MicroEvent (null, e1, e2);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }

  @Test
  public void testInsertionEvent ()
  {
    final MockMicroEventListener aIEL = new MockMicroEventListener ();
    final IMicroDocument aDoc = MicroFactory.newDocument ();
    assertFalse (aDoc.unregisterEventTarget (EMicroEvent.NODE_INSERTED, aIEL).isChanged ());
    assertTrue (aDoc.registerEventTarget (EMicroEvent.NODE_INSERTED, aIEL).isChanged ());
    assertFalse (aDoc.registerEventTarget (EMicroEvent.NODE_INSERTED, aIEL).isChanged ());
    IMicroElement eRoot = null;
    try
    {
      assertEquals (0, aIEL.getInvocationCount ());
      // Direct invoke!
      eRoot = aDoc.appendElement ("root_element");
      assertEquals (1, aIEL.getInvocationCount ());
      // Recursive invoke!
      eRoot.appendElement ("root_element");
      assertEquals (2, aIEL.getInvocationCount ());
      // Simple create a text node
      eRoot.appendText ("My Text node");
      assertEquals (3, aIEL.getInvocationCount ());
    }
    finally
    {
      // unregister
      aDoc.unregisterEventTarget (EMicroEvent.NODE_INSERTED, aIEL);
    }

    // append another element to the root -> no change!
    eRoot.appendElement ("dummy");
    assertEquals (3, aIEL.getInvocationCount ());

    try
    {
      aDoc.registerEventTarget (null, aIEL);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      aDoc.registerEventTarget (EMicroEvent.NODE_INSERTED, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      aDoc.unregisterEventTarget (null, aIEL);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      aDoc.unregisterEventTarget (EMicroEvent.NODE_INSERTED, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
