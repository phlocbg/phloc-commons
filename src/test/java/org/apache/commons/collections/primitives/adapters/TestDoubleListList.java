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
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.RandomAccessDoubleList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestDoubleListList extends BaseTestList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestDoubleListList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = BulkTest.makeSuite (TestDoubleListList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public List makeEmptyList ()
  {
    return new DoubleListList (new ArrayDoubleList ());
  }

  @Override
  public Object [] getFullElements ()
  {
    final Double [] elts = new Double [10];
    for (int i = 0; i < elts.length; i++)
    {
      elts[i] = new Double (i);
    }
    return elts;
  }

  @Override
  public Object [] getOtherElements ()
  {
    final Double [] elts = new Double [10];
    for (int i = 0; i < elts.length; i++)
    {
      elts[i] = new Double (10 + i);
    }
    return elts;
  }

  // tests
  // ------------------------------------------------------------------------

  /** @TODO need to add serialized form to cvs */

  @Override
  public void testCanonicalEmptyCollectionExists ()
  {
    // XXX FIX ME XXX
    // need to add a serialized form to cvs
  }

  @Override
  public void testCanonicalFullCollectionExists ()
  {
    // XXX FIX ME XXX
    // need to add a serialized form to cvs
  }

  @Override
  public void testEmptyListCompatibility ()
  {
    // XXX FIX ME XXX
    // need to add a serialized form to cvs
  }

  @Override
  public void testFullListCompatibility ()
  {
    // XXX FIX ME XXX
    // need to add a serialized form to cvs
  }

  public void testWrapNull ()
  {
    assertNull (DoubleListList.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final List list = DoubleListList.wrap (new ArrayDoubleList ());
    assertNotNull (list);
    assertTrue (list instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final List list = DoubleListList.wrap (new RandomAccessDoubleList ()
    {
      @Override
      public double get (final int i)
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
