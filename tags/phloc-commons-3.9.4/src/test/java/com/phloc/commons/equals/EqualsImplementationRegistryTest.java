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
package com.phloc.commons.equals;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.xml.XMLFactory;

/**
 * Test class for class {@link EqualsImplementationRegistry}.
 * 
 * @author philip
 */
public final class EqualsImplementationRegistryTest extends AbstractPhlocTestCase
{
  @Test
  public void testEquals ()
  {
    final Document d1 = XMLFactory.newDocument ();
    final Node aRoot1 = d1.appendChild (d1.createElementNS ("urn:my:phloc:test-namespace", "root"));
    ((Element) aRoot1.appendChild (d1.createElement ("child"))).setAttribute ("any", "works");

    final Document d2 = XMLFactory.newDocument ();
    final Node aRoot2 = d2.appendChild (d2.createElementNS ("urn:my:phloc:test-namespace", "root"));
    ((Element) aRoot2.appendChild (d2.createElement ("child"))).setAttribute ("any", "works");

    // Regular
    assertTrue (EqualsUtils.equals (d1, d2));

    // 1 level array
    assertTrue (EqualsUtils.equals (ArrayHelper.newArray (d1), ArrayHelper.newArray (d2)));

    // 2 level array
    assertTrue (EqualsUtils.equals (ArrayHelper.newArray (ArrayHelper.newArray (d1)),
                                    ArrayHelper.newArray (ArrayHelper.newArray (d2))));
  }
}
