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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroNode;

/**
 * Test class for class {@link MicroFactory}.
 * 
 * @author philip
 */
public final class MicroFactoryTest
{
  @Test
  public void testCreation ()
  {
    IMicroNode aNode = MicroFactory.newDocumentType ("qname", "pid", "sid");
    assertNotNull (aNode);
    assertTrue (aNode.isDocumentType ());

    aNode = MicroFactory.newDocument ();
    assertNotNull (aNode);
    assertTrue (aNode.isDocument ());

    aNode = MicroFactory.newDocument (MicroFactory.newDocumentType ("qname", "pid", "sid"));
    assertNotNull (aNode);
    assertTrue (aNode.isDocument ());

    aNode = MicroFactory.newComment ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isComment ());

    aNode = MicroFactory.newText ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isText ());

    aNode = MicroFactory.newCDATA ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isCDATA ());

    aNode = MicroFactory.newEntityReference ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isEntityReference ());

    aNode = MicroFactory.newElement ("bla");
    assertNotNull (aNode);
    assertTrue (aNode.isElement ());

    aNode = MicroFactory.newElement ("nsuri", "bla");
    assertNotNull (aNode);
    assertTrue (aNode.isElement ());

    aNode = MicroFactory.newContainer ();
    assertNotNull (aNode);
    assertTrue (aNode.isContainer ());

    aNode = MicroFactory.newContainer (MicroFactory.newElement ("nsuri", "bla"));
    assertNotNull (aNode);
    assertTrue (aNode.isContainer ());

    aNode = MicroFactory.newProcessingInstruction ("target");
    assertNotNull (aNode);
    assertTrue (aNode.isProcessingInstruction ());

    aNode = MicroFactory.newProcessingInstruction ("target", "data");
    assertNotNull (aNode);
    assertTrue (aNode.isProcessingInstruction ());
  }
}
