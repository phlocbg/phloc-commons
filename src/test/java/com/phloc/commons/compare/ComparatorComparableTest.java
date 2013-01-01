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
package com.phloc.commons.compare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link ComparatorComparable}.
 * 
 * @author philip
 */
public final class ComparatorComparableTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final List <String> l = ContainerHelper.newList ("a", "b", "c");
    assertEquals (3, ContainerHelper.getSorted (l, new ComparatorComparable <String> ()).size ());
    assertEquals (3, ContainerHelper.getSorted (l, new ComparatorComparable <String> (ESortOrder.DESCENDING)).size ());

    try
    {
      new ComparatorComparable <String> ((ESortOrder) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new ComparatorComparable <String> ().setSortOrder ((ESortOrder) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}
  }
}
