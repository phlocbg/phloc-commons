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
package org.apache.commons.collections.primitives.decorators;

import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableByteListTest extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableByteListTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------

  protected abstract ByteList makeUnmodifiableByteList ();

  protected ByteList makeByteList ()
  {
    final ByteList list = new ArrayByteList ();
    for (byte i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list;
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testNotModifiable () throws Exception
  {
    assertListNotModifiable (makeUnmodifiableByteList ());
  }

  public final void testSublistNotModifiable () throws Exception
  {
    final ByteList list = makeUnmodifiableByteList ();
    assertListNotModifiable (list.subList (0, list.size () - 2));
  }

  public final void testIteratorNotModifiable () throws Exception
  {
    final ByteList list = makeUnmodifiableByteList ();
    assertIteratorNotModifiable (list.iterator ());
    assertIteratorNotModifiable (list.subList (0, list.size () - 2).iterator ());
  }

  public final void testListIteratorNotModifiable () throws Exception
  {
    final ByteList list = makeUnmodifiableByteList ();
    assertListIteratorNotModifiable (list.listIterator ());
    assertListIteratorNotModifiable (list.subList (0, list.size () - 2).listIterator ());
    assertListIteratorNotModifiable (list.listIterator (1));
    assertListIteratorNotModifiable (list.subList (0, list.size () - 2).listIterator (1));
  }

  // util
  // ------------------------------------------------------------------------

  private void assertListIteratorNotModifiable (final ByteListIterator iter) throws Exception
  {
    assertIteratorNotModifiable (iter);

    assertTrue (iter.hasPrevious ());

    try
    {
      iter.set ((byte) 2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      iter.add ((byte) 2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  private void assertIteratorNotModifiable (final ByteIterator iter) throws Exception
  {
    assertTrue (iter.hasNext ());
    iter.next ();

    try
    {
      iter.remove ();
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  private void assertListNotModifiable (final ByteList list) throws Exception
  {
    try
    {
      list.add ((byte) 1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.add (1, (byte) 2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.addAll (makeByteList ());
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.addAll (1, makeByteList ());
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.removeElementAt (1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.removeElement ((byte) 1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.removeAll (makeByteList ());
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.retainAll (makeByteList ());
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.clear ();
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.set (1, (byte) 2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
