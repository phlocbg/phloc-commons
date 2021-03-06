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
package com.phloc.commons.id;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;

/**
 * Test class for class {@link ComparatorHasIDInteger}.
 * 
 * @author Philip Helger
 */
public final class ComparatorHasIDIntegerTest
{
  @Test
  public void testAll ()
  {
    final List <? extends IHasID <Integer>> aList = ContainerHelper.newList (new MockHasIDInteger (5),
                                                                             new MockHasIDInteger (3),
                                                                             new MockHasIDInteger (7));
    ContainerHelper.getSortedInline (aList, new ComparatorHasIDInteger <IHasID <Integer>> ());
    assertEquals (3, aList.get (0).getID ().intValue ());
    assertEquals (5, aList.get (1).getID ().intValue ());
    assertEquals (7, aList.get (2).getID ().intValue ());

    ContainerHelper.getSortedInline (aList, new ComparatorHasIDInteger <IHasID <Integer>> (ESortOrder.ASCENDING));
    assertEquals (3, aList.get (0).getID ().intValue ());
    assertEquals (5, aList.get (1).getID ().intValue ());
    assertEquals (7, aList.get (2).getID ().intValue ());

    ContainerHelper.getSortedInline (aList, new ComparatorHasIDInteger <IHasID <Integer>> (ESortOrder.DESCENDING));
    assertEquals (7, aList.get (0).getID ().intValue ());
    assertEquals (5, aList.get (1).getID ().intValue ());
    assertEquals (3, aList.get (2).getID ().intValue ());
  }
}
