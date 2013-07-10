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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Nullable;

import org.junit.Test;

import com.phloc.commons.CGlobal;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mock.PhlocAssert;

/**
 * Test class for {@link AbstractNumericComparator}
 * 
 * @author Philip Helger
 */
public final class AbstractNumericComparatorTest
{
  private static final class ComparatorMockNumeric extends AbstractNumericComparator <Double>
  {
    ComparatorMockNumeric ()
    {}

    ComparatorMockNumeric (final ESortOrder eSortOrder)
    {
      super (eSortOrder);
    }

    @Override
    protected double asDouble (@Nullable final Double aValue)
    {
      return aValue == null ? CGlobal.ILLEGAL_DOUBLE : aValue.doubleValue ();
    }
  }

  @Test
  public void testAll ()
  {
    final Double [] x = new Double [] { Double.valueOf (3),
                                       Double.valueOf (3),
                                       Double.valueOf (-56),
                                       Double.valueOf (1) };

    // default: sort ascending
    List <Double> l = ContainerHelper.getSorted (x, new ComparatorMockNumeric ());
    assertNotNull (l);
    PhlocAssert.assertEquals (-56, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (3).doubleValue ());

    // Explicitly sort ascending
    l = ContainerHelper.getSorted (x, new ComparatorMockNumeric (ESortOrder.ASCENDING));
    assertNotNull (l);
    PhlocAssert.assertEquals (-56, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (3).doubleValue ());

    // Explicitly sort descending
    l = ContainerHelper.getSorted (x, new ComparatorMockNumeric (ESortOrder.DESCENDING));
    assertNotNull (l);
    PhlocAssert.assertEquals (3, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (-56, l.get (3).doubleValue ());

    // change dynamically
    final ComparatorMockNumeric c = new ComparatorMockNumeric (ESortOrder.ASCENDING);
    l = ContainerHelper.getSorted (x, c);
    PhlocAssert.assertEquals (-56, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (3).doubleValue ());

    // change to descending
    l = ContainerHelper.getSorted (x, c.setSortOrder (ESortOrder.DESCENDING));
    PhlocAssert.assertEquals (3, l.get (0).doubleValue ());
    PhlocAssert.assertEquals (3, l.get (1).doubleValue ());
    PhlocAssert.assertEquals (1, l.get (2).doubleValue ());
    PhlocAssert.assertEquals (-56, l.get (3).doubleValue ());
  }

  /**
   * Test for method isAscending
   */
  @Test
  public void testIsAscending ()
  {
    assertTrue (new ComparatorMockNumeric ().getSortOrder ().isAscending ());
    assertFalse (new ComparatorMockNumeric (ESortOrder.DESCENDING).getSortOrder ().isAscending ());
  }
}
