/**
 * Copyright (C) 2006-2015 phloc systems
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
 * Test class for class {@link ComparatorHasSimpleLongID}.
 * 
 * @author Philip Helger
 */
public final class ComparatorHasSimpleLongIDTest
{
  @Test
  public void testAll ()
  {
    final List <? extends IHasSimpleLongID> aList = ContainerHelper.newList (new MockHasSimpleLongID (5),
                                                                             new MockHasSimpleLongID (3),
                                                                             new MockHasSimpleLongID (7));
    ContainerHelper.getSortedInline (aList, new ComparatorHasSimpleLongID <IHasSimpleLongID> ());
    assertEquals (3, aList.get (0).getID ());
    assertEquals (5, aList.get (1).getID ());
    assertEquals (7, aList.get (2).getID ());

    ContainerHelper.getSortedInline (aList, new ComparatorHasSimpleLongID <IHasSimpleLongID> (ESortOrder.ASCENDING));
    assertEquals (3, aList.get (0).getID ());
    assertEquals (5, aList.get (1).getID ());
    assertEquals (7, aList.get (2).getID ());

    ContainerHelper.getSortedInline (aList, new ComparatorHasSimpleLongID <IHasSimpleLongID> (ESortOrder.DESCENDING));
    assertEquals (7, aList.get (0).getID ());
    assertEquals (5, aList.get (1).getID ());
    assertEquals (3, aList.get (2).getID ());
  }
}
