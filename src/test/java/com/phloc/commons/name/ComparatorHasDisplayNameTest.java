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
package com.phloc.commons.name;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ComparatorHasDisplayName}.
 * 
 * @author philip
 */
public final class ComparatorHasDisplayNameTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final List <MockHasDisplayName> aList = ContainerHelper.newList (new MockHasDisplayName (10),
                                                                      new MockHasDisplayName (2),
                                                                      new MockHasDisplayName (5));
    List <MockHasDisplayName> l2 = ContainerHelper.getSorted (aList,
                                                               new ComparatorHasDisplayName <IHasDisplayName> (L_DE));
    assertEquals (3, l2.size ());
    assertEquals ("10", l2.get (0).getDisplayName ());
    assertEquals ("2", l2.get (1).getDisplayName ());
    assertEquals ("5", l2.get (2).getDisplayName ());

    l2 = ContainerHelper.getSorted (aList,
                                     new ComparatorHasDisplayName <IHasDisplayName> (L_DE, ESortOrder.DESCENDING));
    assertEquals (3, l2.size ());
    assertEquals ("5", l2.get (0).getDisplayName ());
    assertEquals ("2", l2.get (1).getDisplayName ());
    assertEquals ("10", l2.get (2).getDisplayName ());
  }
}
