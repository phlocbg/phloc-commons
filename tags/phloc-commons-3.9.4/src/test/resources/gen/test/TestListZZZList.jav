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
package org.apache.commons.collections.primitives.adapters;

import java.util.AbstractList;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ZZZList;
import org.apache.commons.collections.primitives.TestZZZList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestListZZZList extends TestZZZList
{
  public TestListZZZList (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestListZZZList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  /**
   * @see org.apache.commons.collections.primitives.TestZZZList#makeEmptyZZZList()
   */
  @Override
  protected ZZZList makeEmptyZZZList ()
  {
    return new ListZZZList (new ArrayList <XXX> ());
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (ListZZZList.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final ZZZList list = ListZZZList.wrap (new ArrayList <XXX> ());
    assertNotNull (list);
  }

  public void testWrapNonSerializable ()
  {
    final ZZZList list = ListZZZList.wrap (new AbstractList <XXX> ()
    {
      @Override
      public XXX get (final int i)
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
  }
}
