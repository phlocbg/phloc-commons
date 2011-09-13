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
package com.phloc.commons.tree.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.convert.UnidirectionalConverterIdentity;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.name.MockHasName;
import com.phloc.commons.tree.simple.ITreeItem;
import com.phloc.commons.tree.simple.Tree;
import com.phloc.commons.tree.sort.ComparatorTreeItemValueComparable;
import com.phloc.commons.tree.withid.unique.TreeWithGlobalUniqueID;

/**
 * Test class for class {@link TreeXMLConverter}.
 * 
 * @author philip
 */
public final class TreeXMLConverterTest extends AbstractPhlocTestCase
{
  private static final class MockHasNameConverter implements IConverterTreeXML <MockHasName>
  {
    public void appendDataValue (final IMicroElement eDataElement, final MockHasName aAnyName)
    {
      eDataElement.appendElement ("name").appendText (aAnyName.getName ());
    }

    public MockHasName getAsDataValue (final IMicroElement eDataElement)
    {
      return new MockHasName (MicroUtils.getChildTextContent (eDataElement, "name"));
    }
  }

  @Test
  public void testReadWrite ()
  {
    // read initial document
    final IMicroDocument aDoc1 = MicroReader.readMicroXML (new ClassPathResource ("tree/xmlconverter-valid1.xml"));
    assertNotNull (aDoc1);

    // convert document to tree
    final TreeWithGlobalUniqueID <String, MockHasName> t1 = TreeXMLConverter.getXMLAsTreeWithUniqueStringID (aDoc1,
                                                                                                             new MockHasNameConverter ());
    assertNotNull (t1);

    // convert tree again to document
    final IMicroDocument aDoc2 = TreeXMLConverter.getTreeWithStringIDAsXML (t1, new MockHasNameConverter ());
    assertNotNull (aDoc2);

    // and convert the document again to a tree
    TreeWithGlobalUniqueID <String, MockHasName> t2 = TreeXMLConverter.getXMLAsTreeWithUniqueStringID (aDoc2,
                                                                                                       new MockHasNameConverter ());
    assertNotNull (t2);
    assertEquals (t1, t2);

    // and convert the document again to a tree
    t2 = TreeXMLConverter.getXMLAsTreeWithUniqueID (aDoc2,
                                                    UnidirectionalConverterIdentity.<String> create (),
                                                    new MockHasNameConverter ());
    assertNotNull (t2);
    assertEquals (t1, t2);

    // and convert the document again to a tree
    assertNotNull (TreeXMLConverter.getXMLAsTreeWithID (aDoc2,
                                                        UnidirectionalConverterIdentity.<String> create (),
                                                        new MockHasNameConverter ()));
  }

  @Test
  public void testTree ()
  {
    final Tree <MockHasName> aTree = new Tree <MockHasName> ();
    aTree.getRootItem ().createChildItem (new MockHasName ("name2"));
    aTree.getRootItem ().createChildItem (new MockHasName ("name1"));

    TreeXMLConverter.getTreeAsXML (aTree,
                                   new ComparatorTreeItemValueComparable <MockHasName, ITreeItem <MockHasName>> (),
                                   new MockHasNameConverter ());
  }
}
