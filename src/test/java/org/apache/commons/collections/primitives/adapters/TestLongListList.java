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
import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.RandomAccessLongList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestLongListList extends BaseTestList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestLongListList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = BulkTest.makeSuite (TestLongListList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public List makeEmptyList ()
  {
    return new LongListList (new ArrayLongList ());
  }

  @Override
  public Object [] getFullElements ()
  {
    final Long [] elts = new Long [10];
    for (int i = 0; i < elts.length; i++)
    {
      elts[i] = new Long (i);
    }
    return elts;
  }

  @Override
  public Object [] getOtherElements ()
  {
    final Long [] elts = new Long [10];
    for (int i = 0; i < elts.length; i++)
    {
      elts[i] = new Long (10 + i);
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
    assertNull (LongListList.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final List list = LongListList.wrap (new ArrayLongList ());
    assertNotNull (list);
    assertTrue (list instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final List list = LongListList.wrap (new RandomAccessLongList ()
    {
      @Override
      public long get (final int i)
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
