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
package com.phloc.commons.microdom.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroNode;

/**
 * Test class for class MicroFactory
 * 
 * @author philip
 */
public final class MicroFactoryTest
{
  @Test
  public void testCreation ()
  {
    IMicroNode aNode = new MicroDocumentType ("qname", "pid", "sid");
    assertNotNull (aNode);
    assertTrue (aNode.isDocumentType ());

    aNode = new MicroDocument ();
    assertNotNull (aNode);
    assertTrue (aNode.isDocument ());

    aNode = new MicroDocument (new MicroDocumentType ("qname", "pid", "sid"));
    assertNotNull (aNode);
    assertTrue (aNode.isDocument ());

    aNode = new MicroComment ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isComment ());

    aNode = new MicroText ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isText ());

    aNode = new MicroCDATA ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isCDATA ());

    aNode = new MicroEntityReference ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isEntityReference ());

    aNode = new MicroElement ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isElement ());

    aNode = new MicroElement ("nsuri", "bla");
    assertNotNull (aNode);
    assertTrue (aNode.isElement ());

    aNode = new MicroContainer ();
    assertNotNull (aNode);
    assertTrue (aNode.isContainer ());
    final IMicroNode [] aChildNodes = { new MicroElement ("nsuri", "bla") };

    aNode = new MicroContainer (aChildNodes);
    assertNotNull (aNode);
    assertTrue (aNode.isContainer ());

    aNode = new MicroProcessingInstruction ("target");
    assertNotNull (aNode);
    assertTrue (aNode.isProcessingInstruction ());

    aNode = new MicroProcessingInstruction ("target", "data");
    assertNotNull (aNode);
    assertTrue (aNode.isProcessingInstruction ());
  }
}
