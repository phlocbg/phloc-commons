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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.apache.commons.collections.primitives.adapters.BaseTestList;
import org.apache.commons.collections.primitives.adapters.ByteListList;
import org.apache.commons.collections.primitives.adapters.ListByteList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestByteList extends BaseTestList <Byte>
{

  // conventional
  // ------------------------------------------------------------------------

  public TestByteList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: byte list
  // ------------------------------------------------------------------------

  protected abstract ByteList makeEmptyByteList ();

  protected ByteList makeFullByteList ()
  {
    final ByteList list = makeEmptyByteList ();
    final byte [] values = getFullBytes ();
    for (final byte value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected byte [] getFullBytes ()
  {
    final byte [] result = new byte [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = (byte) (i);
    }
    return result;
  }

  protected byte [] getOtherBytes ()
  {
    final byte [] result = new byte [16];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = (byte) (i + 43);
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Byte> makeEmptyList ()
  {
    return new ByteListList (makeEmptyByteList ());
  }

  @Override
  public Byte [] getFullElements ()
  {
    return wrapArray (getFullBytes ());
  }

  @Override
  public Byte [] getOtherElements ()
  {
    return wrapArray (getOtherBytes ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Byte [] wrapArray (final byte [] primitives)
  {
    final Byte [] result = new Byte [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Byte (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final ByteList list = makeFullByteList ();
    final ByteIterator iter = list.iterator ();
    iter.next ();
    list.add ((byte) 3);
    try
    {
      iter.next ();
      fail ("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e)
    {
      // expected
    }
  }

  public void testAddAllByteListAtIndex ()
  {
    final ByteList source = makeFullByteList ();
    final ByteList dest = makeFullByteList ();
    dest.addAll (1, source);

    final ByteIterator iter = dest.iterator ();
    assertTrue (iter.hasNext ());
    assertEquals (source.get (0), iter.next ());
    for (int i = 0; i < source.size (); i++)
    {
      assertTrue (iter.hasNext ());
      assertEquals (source.get (i), iter.next ());
    }
    for (int i = 1; i < source.size (); i++)
    {
      assertTrue (iter.hasNext ());
      assertEquals (source.get (i), iter.next ());
    }
    assertFalse (iter.hasNext ());
  }

  public void testToJustBigEnoughByteArray ()
  {
    final ByteList list = makeFullByteList ();
    final byte [] dest = new byte [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final ByteIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
  }

  public void testToLargerThanNeededByteArray ()
  {
    final ByteList list = makeFullByteList ();
    final byte [] dest = new byte [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = Byte.MAX_VALUE;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final ByteIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (Byte.MAX_VALUE, dest[i]);
    }
  }

  public void testToSmallerThanNeededByteArray ()
  {
    final ByteList list = makeFullByteList ();
    final byte [] dest = new byte [list.size () / 2];
    final byte [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final ByteIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i]);
    }
  }

  public void testEqualsWithTwoByteLists ()
  {
    final ByteList one = makeEmptyByteList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final ByteList two = makeEmptyByteList ();
    assertEquals ("Empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on empty lists", two, one);

    one.add ((byte) 1);
    assertEquals ("Equals is reflexive on non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add ((byte) 1);
    assertEquals ("Non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on non empty list", one, two);

    one.add ((byte) 1);
    one.add ((byte) 2);
    one.add ((byte) 3);
    one.add ((byte) 5);
    one.add ((byte) 8);
    assertEquals ("Equals is reflexive on larger non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add ((byte) 1);
    two.add ((byte) 2);
    two.add ((byte) 3);
    two.add ((byte) 5);
    two.add ((byte) 8);
    assertEquals ("Larger non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on larger non empty list", two, one);

    one.add ((byte) 9);
    two.add ((byte) 10);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

  }

  public void testByteSubListEquals ()
  {
    final ByteList one = makeEmptyByteList ();
    assertEquals (one, one.subList (0, 0));
    assertEquals (one.subList (0, 0), one);

    one.add ((byte) 1);
    assertEquals (one, one.subList (0, 1));
    assertEquals (one.subList (0, 1), one);

    one.add ((byte) 1);
    one.add ((byte) 2);
    one.add ((byte) 3);
    one.add ((byte) 5);
    one.add ((byte) 8);
    assertEquals (one.subList (0, 4), one.subList (0, 4));
    assertEquals (one.subList (3, 5), one.subList (3, 5));
  }

  public void testEqualsWithByteListAndList ()
  {
    final ByteList ilist = makeEmptyByteList ();
    final List <Byte> list = new ArrayList <Byte> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty ByteList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty ByteList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListByteList (list), ilist);
    assertEquals (ilist, new ListByteList (list));
    assertEquals (new ByteListList (ilist), list);
    assertEquals (list, new ByteListList (ilist));

    ilist.add ((byte) 1);
    list.add (new Byte ((byte) 1));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty ByteList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty ByteList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListByteList (list), ilist);
    assertEquals (ilist, new ListByteList (list));
    assertEquals (new ByteListList (ilist), list);
    assertEquals (list, new ByteListList (ilist));

    ilist.add ((byte) 1);
    ilist.add ((byte) 2);
    ilist.add ((byte) 3);
    ilist.add ((byte) 5);
    ilist.add ((byte) 8);
    list.add (new Byte ((byte) 1));
    list.add (new Byte ((byte) 2));
    list.add (new Byte ((byte) 3));
    list.add (new Byte ((byte) 5));
    list.add (new Byte ((byte) 8));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty ByteList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty ByteList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListByteList (list), ilist);
    assertEquals (ilist, new ListByteList (list));
    assertEquals (new ByteListList (ilist), list);
    assertEquals (list, new ByteListList (ilist));

  }

  public void testClearAndSize ()
  {
    final ByteList list = makeEmptyByteList ();
    assertEquals (0, list.size ());
    for (int i = 0; i < 100; i++)
    {
      list.add ((byte) i);
    }
    assertEquals (100, list.size ());
    list.clear ();
    assertEquals (0, list.size ());
  }

  public void testRemoveViaSubList ()
  {
    final ByteList list = makeEmptyByteList ();
    for (int i = 0; i < 100; i++)
    {
      list.add ((byte) i);
    }
    final ByteList sub = list.subList (25, 75);
    assertEquals (50, sub.size ());
    for (int i = 0; i < 50; i++)
    {
      assertEquals (100 - i, list.size ());
      assertEquals (50 - i, sub.size ());
      assertEquals ((byte) (25 + i), sub.removeElementAt (0));
      assertEquals (50 - i - 1, sub.size ());
      assertEquals (100 - i - 1, list.size ());
    }
    assertEquals (0, sub.size ());
    assertEquals (50, list.size ());
  }

  public void testAddGet ()
  {
    final ByteList list = makeEmptyByteList ();
    for (int i = 0; i < 255; i++)
    {
      list.add ((byte) i);
    }
    for (int i = 0; i < 255; i++)
    {
      assertEquals ((byte) i, list.get (i));
    }
  }

  public void testAddAndShift ()
  {
    final ByteList list = makeEmptyByteList ();
    list.add (0, (byte) 1);
    assertEquals ("Should have one entry", 1, list.size ());
    list.add ((byte) 3);
    list.add ((byte) 4);
    list.add (1, (byte) 2);
    for (int i = 0; i < 4; i++)
    {
      assertEquals ("Should get entry back", (byte) (i + 1), list.get (i));
    }
    list.add (0, (byte) 0);
    for (int i = 0; i < 5; i++)
    {
      assertEquals ("Should get entry back", (byte) i, list.get (i));
    }
  }

  public void testIsSerializable () throws Exception
  {
    final ByteList list = makeFullByteList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final ByteList deser = (ByteList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testByteListSerializeDeserializeThenCompare () throws Exception
  {
    final ByteList list = makeFullByteList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final ByteList deser = (ByteList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final ByteList list = makeFullByteList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyByteList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullByteList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullByteList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullByteList ().subList (2, makeFullByteList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyByteList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullByteList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullByteList ().listIterator (makeFullByteList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final ByteListIterator iter = makeFullByteList ().listIterator ();
    try
    {
      iter.set ((byte) 3);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

  public void testListIteratorSetAfterRemove () throws Exception
  {
    final ByteListIterator iter = makeFullByteList ().listIterator ();
    iter.next ();
    iter.remove ();
    try
    {
      iter.set ((byte) 3);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

}
