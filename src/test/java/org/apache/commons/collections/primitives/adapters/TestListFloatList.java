/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.commons.collections.primitives.FloatList;
import org.apache.commons.collections.primitives.TestFloatList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestListFloatList extends TestFloatList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestListFloatList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestListFloatList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  /**
   * @see org.apache.commons.collections.primitives.TestFloatList#makeEmptyFloatList()
   */
  @Override
  protected FloatList makeEmptyFloatList ()
  {
    return new ListFloatList (new ArrayList <Float> ());
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (ListFloatList.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final FloatList list = ListFloatList.wrap (new ArrayList <Float> ());
    assertNotNull (list);
    assertTrue (list instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final FloatList list = ListFloatList.wrap (new AbstractList <Float> ()
    {
      @Override
      public Float get (final int i)
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
