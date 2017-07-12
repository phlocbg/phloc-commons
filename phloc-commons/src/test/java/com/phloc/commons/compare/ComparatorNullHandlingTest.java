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
package com.phloc.commons.compare;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class testing the null handling of various comparators
 * 
 * @author Boris Gregorcic
 */
public final class ComparatorNullHandlingTest extends AbstractPhlocTestCase
{
  @Test
  public void testStringInstanceBased ()
  {
    final ComparatorAsString aCmp = new ComparatorAsString ();
    final List <String> l = ContainerHelper.newList ("a", "b", null, "c", null);
    assertEquals (ContainerHelper.newList (null, null, "a", "b", "c"), ContainerHelper.getSorted (l, aCmp));
    aCmp.setNullValuesComeFirst (false);
    assertEquals (ContainerHelper.newList ("a", "b", "c", null, null), ContainerHelper.getSorted (l, aCmp));
  }

  @Test
  public void testGlobal ()
  {
    final List <String> l1 = ContainerHelper.newList ("a", "c", null, "b", null);
    final List <Integer> l2 = ContainerHelper.newList (Integer.valueOf (1),
                                                       Integer.valueOf (7),
                                                       null,
                                                       Integer.valueOf (3),
                                                       null);
    {
      final ComparatorAsString aCmp = new ComparatorAsString ();
      final AbstractNumericComparator <Integer> aCmpNum = getNumericCmp ();
      assertEquals (ContainerHelper.newList (null, null, "a", "b", "c"), ContainerHelper.getSorted (l1, aCmp));
      assertEquals (ContainerHelper.newList (null, null, Integer.valueOf (1), Integer.valueOf (3), Integer.valueOf (7)),
                    ContainerHelper.getSorted (l2, aCmpNum));
    }
    GlobalCompareSettings.getInstance ().setSortNullValuesFirst (false);
    try
    {
      final ComparatorAsString aCmp = new ComparatorAsString ();
      final AbstractNumericComparator <Integer> aCmpNum = getNumericCmp ();
      assertEquals (ContainerHelper.newList ("a", "b", "c", null, null), ContainerHelper.getSorted (l1, aCmp));
      assertEquals (ContainerHelper.newList (Integer.valueOf (1), Integer.valueOf (3), Integer.valueOf (7), null, null),
                    ContainerHelper.getSorted (l2, aCmpNum));

    }
    finally
    {
      GlobalCompareSettings.getInstance ()
                           .setSortNullValuesFirst (GlobalCompareSettings.DEFAULT_NULL_VALUES_COME_FIRST);
    }
  }

  private AbstractNumericComparator <Integer> getNumericCmp ()
  {
    return new AbstractNumericComparator <Integer> ()
    {
      private static final long serialVersionUID = -4647101399639900684L;

      @Override
      protected double asDouble (final Integer aObject)
      {
        return aObject == null ? (isNullValuesComeFirst () ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY)
                              : aObject.doubleValue ();
      }
    };
  }
}
