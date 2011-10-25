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
package com.phloc.commons.concurrent;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;

/**
 * Test class for class {@link ComparatorThreadID}.
 * 
 * @author philip
 */
public final class ComparatorThreadIDTest
{
  @Test
  public void testBasic ()
  {
    final List <Thread> aList = ContainerHelper.newList (new Thread ("name1"), new Thread ("name2"));
    ContainerHelper.getSortedInline (aList, new ComparatorThreadID ());
    ContainerHelper.getSortedInline (aList, new ComparatorThreadID (ESortOrder.DESCENDING));
  }
}
