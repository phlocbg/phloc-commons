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
package com.phloc.commons.microdom.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.mutable.MutableInt;

/**
 * Test class for class {@link MicroWalker}.
 * 
 * @author philip
 */
public final class MicroWalkerTest
{
  @Test
  public void testWalkNode ()
  {
    // Read file with processing instruction
    final IMicroDocument doc = MicroReader.readMicroXML (new ClassPathResource ("xml/xml-processing-instruction.xml"));
    assertNotNull (doc);

    // Count processing instruction
    final MutableInt aInt = new MutableInt (0);
    MicroWalker.walkNode (doc, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aItem)
      {
        if (aItem.isProcessingInstruction ())
          aInt.inc ();
      }
    });
    assertEquals (3, aInt.intValue ());

    // Start from the root document -> only 1 left
    aInt.set (0);
    MicroWalker.walkNode (doc.getDocumentElement (), new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (@Nonnull final IMicroNode aItem)
      {
        if (aItem.isProcessingInstruction ())
          aInt.inc ();
      }
    });
    assertEquals (1, aInt.intValue ());

    try
    {
      MicroWalker.walkNode (null, new DefaultHierarchyWalkerCallback <IMicroNode> ());
      fail ();
    }
    catch (final NullPointerException ex)
    {}
    try
    {
      MicroWalker.walkNode (doc, null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
