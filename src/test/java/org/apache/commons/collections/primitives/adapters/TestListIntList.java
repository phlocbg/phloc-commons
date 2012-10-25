/**
 * Copyright (C) 2006-2012 phloc systems
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
package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.collections.primitives.TestIntList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestListIntList extends TestIntList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestListIntList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestListIntList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  /**
   * @see org.apache.commons.collections.primitives.TestIntList#makeEmptyIntList()
   */
  @Override
  protected IntList makeEmptyIntList ()
  {
    return new ListIntList (new ArrayList <Integer> ());
  }

  public void testWrapNull ()
  {
    assertNull (ListIntList.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final IntList list = ListIntList.wrap (new ArrayList <Integer> ());
    assertNotNull (list);
    assertTrue (list instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final IntList list = ListIntList.wrap (new AbstractList <Integer> ()
    {
      @Override
      public Integer get (final int i)
      {
        throw new IndexOutOfBoundsException ();
      }

      @Override
      public int size ()
      {
        return 0;
      }
    });
    assertNotNull (list);
    assertTrue (!(list instanceof Serializable));
  }

}
