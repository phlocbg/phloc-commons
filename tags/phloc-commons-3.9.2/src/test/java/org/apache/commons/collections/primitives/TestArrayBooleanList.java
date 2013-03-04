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
package org.apache.commons.collections.primitives;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestArrayBooleanList extends TestBooleanList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestArrayBooleanList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestArrayBooleanList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  protected BooleanList makeEmptyBooleanList ()
  {
    return new ArrayBooleanList ();
  }

  // tests
  // ------------------------------------------------------------------------

  public void testZeroInitialCapacityIsValid ()
  {
    assertNotNull (new ArrayBooleanList (0));
  }

  public void testNegativeInitialCapacityIsInvalid ()
  {
    try
    {
      new ArrayBooleanList (-1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
  }

  public void testCopyConstructor ()
  {
    final ArrayBooleanList expected = new ArrayBooleanList ();
    for (int i = 0; i < 10; i++)
    {
      expected.add (i % 2 == 0);
    }
    final ArrayBooleanList list = new ArrayBooleanList (expected);
    assertEquals (10, list.size ());
    assertEquals (expected, list);
  }

  public void testCopyConstructorWithNull ()
  {
    try
    {
      new ArrayBooleanList ((BooleanCollection) null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testArrayConstructor ()
  {
    final ArrayBooleanList expected = new ArrayBooleanList ();
    for (int i = 0; i < 10; i++)
    {
      expected.add (i % 2 == 0);
    }
    final ArrayBooleanList list = new ArrayBooleanList (expected.toArray ());
    assertEquals (10, list.size ());
    assertEquals (expected, list);
  }

  public void testArrayConstructorWithNull ()
  {
    try
    {
      new ArrayBooleanList ((boolean []) null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testTrimToSize ()
  {
    final ArrayBooleanList list = new ArrayBooleanList ();
    for (int j = 0; j < 3; j++)
    {
      assertTrue (list.isEmpty ());

      list.trimToSize ();

      assertTrue (list.isEmpty ());

      for (int i = 0; i < 10; i++)
      {
        list.add (i % 2 == 0);
      }

      for (int i = 0; i < 10; i++)
      {
        assertEquals (i % 2 == 0, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 10; i++)
      {
        assertEquals (i % 2 == 0, list.get (i));
      }

      for (int i = 0; i < 5; i++)
      {
        list.removeElement (true);
      }

      for (int i = 0; i < 5; i++)
      {
        assertEquals (false, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 5; i++)
      {
        assertEquals (false, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 5; i++)
      {
        assertEquals (false, list.get (i));
      }

      list.clear ();
    }
  }

}