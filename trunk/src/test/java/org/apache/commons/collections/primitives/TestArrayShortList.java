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
package org.apache.commons.collections.primitives;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.BulkTest;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestArrayShortList extends TestShortList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestArrayShortList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = BulkTest.makeSuite (TestArrayShortList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  protected ShortList makeEmptyShortList ()
  {
    return new ArrayShortList ();
  }

  @Override
  public String [] ignoredTests ()
  {
    // sublists are not serializable
    return new String [] { "TestArrayShortList.bulkTestSubList.testFullListSerialization",
                          "TestArrayShortList.bulkTestSubList.testEmptyListSerialization",
                          "TestArrayShortList.bulkTestSubList.testCanonicalEmptyCollectionExists",
                          "TestArrayShortList.bulkTestSubList.testCanonicalFullCollectionExists",
                          "TestArrayShortList.bulkTestSubList.testEmptyListCompatibility",
                          "TestArrayShortList.bulkTestSubList.testFullListCompatibility",
                          "TestArrayShortList.bulkTestSubList.testSerializeDeserializeThenCompare",
                          "TestArrayShortList.bulkTestSubList.testSimpleSerialization" };
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

  public void testAddGetLargeValues ()
  {
    final ShortList list = new ArrayShortList ();
    for (int i = 0; i < 1000; i++)
    {
      short value = ((short) (Integer.MAX_VALUE));
      value += i;
      list.add (value);
    }
    for (int i = 0; i < 1000; i++)
    {
      short value = ((short) (Integer.MAX_VALUE));
      value += i;
      assertEquals (value, list.get (i));
    }
  }

  public void testZeroInitialCapacityIsValid ()
  {
    assertNotNull (new ArrayShortList (0));
  }

  public void testNegativeInitialCapacityIsInvalid ()
  {
    try
    {
      new ArrayShortList (-1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
  }

  public void testCopyConstructor ()
  {
    final ArrayShortList expected = new ArrayShortList ();
    for (int i = 0; i < 10; i++)
    {
      expected.add ((short) i);
    }
    final ArrayShortList list = new ArrayShortList (expected);
    assertEquals (10, list.size ());
    assertEquals (expected, list);
  }

  public void testCopyConstructorWithNull ()
  {
    try
    {
      new ArrayShortList ((ShortCollection) null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testArrayConstructor ()
  {
    final ArrayShortList expected = new ArrayShortList ();
    for (int i = 0; i < 10; i++)
    {
      expected.add ((short) i);
    }
    final ArrayShortList list = new ArrayShortList (expected.toArray ());
    assertEquals (10, list.size ());
    assertEquals (expected, list);
  }

  public void testArrayConstructorWithNull ()
  {
    try
    {
      new ArrayShortList ((short []) null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testTrimToSize ()
  {
    final ArrayShortList list = new ArrayShortList ();
    for (int j = 0; j < 3; j++)
    {
      assertTrue (list.isEmpty ());

      list.trimToSize ();

      assertTrue (list.isEmpty ());

      for (int i = 0; i < 10; i++)
      {
        list.add ((short) i);
      }

      for (int i = 0; i < 10; i++)
      {
        assertEquals ((short) i, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 10; i++)
      {
        assertEquals ((short) i, list.get (i));
      }

      for (int i = 0; i < 10; i += 2)
      {
        list.removeElement ((short) i);
      }

      for (int i = 0; i < 5; i++)
      {
        assertEquals ((short) (2 * i) + 1, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 5; i++)
      {
        assertEquals ((short) (2 * i) + 1, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 5; i++)
      {
        assertEquals ((short) (2 * i) + 1, list.get (i));
      }

      list.clear ();
    }
  }

}
