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
public class TestArrayUnsignedByteList extends TestShortList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestArrayUnsignedByteList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestArrayUnsignedByteList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  protected ShortList makeEmptyShortList ()
  {
    return new ArrayUnsignedByteList ();
  }

  @Override
  protected short [] getFullShorts ()
  {
    final short [] result = new short [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = (short) (ArrayUnsignedByteList.MAX_VALUE - i);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testArrayConstructor ()
  {
    final short [] data = new short [] { 1, 2, 3 };
    final ShortList list = new ArrayUnsignedByteList (data);
    for (int i = 0; i < data.length; i++)
    {
      assertEquals (data[i], list.get (i));
    }
    data[0] = 17;
    assertEquals (1, list.get (0));
  }

  public void testZeroInitialCapacityIsValid ()
  {
    assertNotNull (new ArrayUnsignedByteList (0));
  }

  public void testIllegalArgumentExceptionWhenElementOutOfRange ()
  {
    final ArrayUnsignedByteList list = new ArrayUnsignedByteList ();
    list.add (ArrayUnsignedByteList.MIN_VALUE);
    list.add (ArrayUnsignedByteList.MAX_VALUE);
    try
    {
      list.add ((short) -1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
    try
    {
      list.add ((short) (ArrayUnsignedByteList.MAX_VALUE + 1));
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
      new ArrayUnsignedByteList (-1);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }
  }

  public void testCopyConstructor ()
  {
    final ArrayUnsignedByteList expected = new ArrayUnsignedByteList ();
    for (int i = 0; i < 10; i++)
    {
      expected.add ((short) i);
    }
    final ArrayUnsignedByteList list = new ArrayUnsignedByteList (expected);
    assertEquals (10, list.size ());
    assertEquals (expected, list);
  }

  public void testCopyConstructorWithNull ()
  {
    try
    {
      new ArrayUnsignedByteList ((ShortList) null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testTrimToSize ()
  {
    final ArrayUnsignedByteList list = new ArrayUnsignedByteList ();
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
        assertEquals (i, list.get (i));
      }

      list.trimToSize ();

      for (int i = 0; i < 10; i++)
      {
        assertEquals (i, list.get (i));
      }

      for (int i = 0; i < 10; i += 2)
      {
        list.removeElement ((short) i);
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
