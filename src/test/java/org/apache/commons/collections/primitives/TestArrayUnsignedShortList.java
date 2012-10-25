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

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestArrayUnsignedShortList extends TestIntList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestArrayUnsignedShortList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestArrayUnsignedShortList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  protected IntList makeEmptyIntList ()
  {
    return new ArrayUnsignedShortList ();
  }

  // tests
  // ------------------------------------------------------------------------

  public void testArrayConstructor ()
  {
    final int [] data = new int [] { 1, 2, 3 };
    final IntList list = new ArrayUnsignedShortList (data);
    for (int i = 0; i < data.length; i++)
    {
      assertEquals (data[i], list.get (i));
    }
    data[0] = 17;
    assertEquals (1, list.get (0));
  }

  public void testZeroInitialCapacityIsValid ()
  {
    assertNotNull (new ArrayUnsignedShortList (0));
  }

  public void testIllegalArgumentExceptionWhenElementOutOfRange ()
  {
    final ArrayUnsignedShortList list = new ArrayUnsignedShortList ();
    list.add (ArrayUnsignedShortList.MIN_VALUE);
    list.add (ArrayUnsignedShortList.MAX_VALUE);
    try
    {
      list.add (-1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
    try
    {
      list.add (ArrayUnsignedShortList.MAX_VALUE + 1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
  }

  public void testNegativeInitialCapacityIsInvalid ()
  {
    try
    {
      new ArrayUnsignedShortList (-1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
  }

  public void testCopyConstructor ()
  {
    final ArrayUnsignedShortList expected = new ArrayUnsignedShortList ();
    for (int i = 0; i < 10; i++)
    {
      expected.add (i);
    }
    final ArrayUnsignedShortList list = new ArrayUnsignedShortList (expected);
    assertEquals (10, list.size ());
    assertEquals (expected, list);
  }

  public void testCopyConstructorWithNull ()
  {
    try
    {
      new ArrayUnsignedShortList ((IntList) null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testTrimToSize ()
  {
    final ArrayUnsignedShortList list = new ArrayUnsignedShortList ();
    for (int j = 0; j < 3; j++)
    {
      assertTrue (list.isEmpty ());

      list.trimToSize ();

      assertTrue (list.isEmpty ());

      for (int i = 0; i < 10; i++)
      {
        list.add (i);
      }

      for (int i = 0; i < 10; i++)
      {
        assertEquals (i, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 10; i++)
      {
        assertEquals (i, list.get (i));
      }

      for (int i = 0; i < 10; i += 2)
      {
        list.removeElement (i);
      }

      for (int i = 0; i < 5; i++)
      {
        assertEquals ((2 * i) + 1, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 5; i++)
      {
        assertEquals ((2 * i) + 1, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 5; i++)
      {
        assertEquals ((2 * i) + 1, list.get (i));
      }

      list.clear ();
    }
  }

}
