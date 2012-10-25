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
import org.apache.commons.collections.primitives.adapters.ListShortList;
import org.apache.commons.collections.primitives.adapters.ShortListList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestShortList extends BaseTestList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestShortList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: short list
  // ------------------------------------------------------------------------

  protected abstract ShortList makeEmptyShortList ();

  protected ShortList makeFullShortList ()
  {
    final ShortList list = makeEmptyShortList ();
    final short [] values = getFullShorts ();
    for (final short value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected short [] getFullShorts ()
  {
    final short [] result = new short [19];
    for (short i = 0; i < result.length; i++)
    {
      result[i] = (short) (Short.MAX_VALUE - i);
    }
    return result;
  }

  protected short [] getOtherShorts ()
  {
    final short [] result = new short [16];
    for (short i = 0; i < result.length; i++)
    {
      result[i] = (short) (i + 43);
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Short> makeEmptyList ()
  {
    return new ShortListList (makeEmptyShortList ());
  }

  @Override
  public Short [] getFullElements ()
  {
    return wrapArray (getFullShorts ());
  }

  @Override
  public Short [] getOtherElements ()
  {
    return wrapArray (getOtherShorts ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Short [] wrapArray (final short [] primitives)
  {
    final Short [] result = new Short [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Short (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final ShortList list = makeFullShortList ();
    final ShortIterator iter = list.iterator ();
    iter.next ();
    list.add ((short) 3);
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

  public void testAddAllShortListAtIndex ()
  {
    final ShortList source = makeFullShortList ();
    final ShortList dest = makeFullShortList ();
    dest.addAll (1, source);

    final ShortIterator iter = dest.iterator ();
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

  public void testToJustBigEnoughShortArray ()
  {
    final ShortList list = makeFullShortList ();
    final short [] dest = new short [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final ShortIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
  }

  public void testToLargerThanNeededShortArray ()
  {
    final ShortList list = makeFullShortList ();
    final short [] dest = new short [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = Short.MAX_VALUE;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final ShortIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (Short.MAX_VALUE, dest[i]);
    }
  }

  public void testToSmallerThanNeededShortArray ()
  {
    final ShortList list = makeFullShortList ();
    final short [] dest = new short [list.size () / 2];
    final short [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final ShortIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i]);
    }
  }

  public void testEqualsWithTwoShortLists ()
  {
    final ShortList one = makeEmptyShortList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final ShortList two = makeEmptyShortList ();
    assertEquals ("Empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on empty lists", two, one);

    one.add ((short) 1);
    assertEquals ("Equals is reflexive on non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add ((short) 1);
    assertEquals ("Non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on non empty list", one, two);

    one.add ((short) 1);
    one.add ((short) 2);
    one.add ((short) 3);
    one.add ((short) 5);
    one.add ((short) 8);
    assertEquals ("Equals is reflexive on larger non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add ((short) 1);
    two.add ((short) 2);
    two.add ((short) 3);
    two.add ((short) 5);
    two.add ((short) 8);
    assertEquals ("Larger non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on larger non empty list", two, one);

    one.add ((short) 9);
    two.add ((short) 10);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

  }

  public void testShortSubListEquals ()
  {
    final ShortList one = makeEmptyShortList ();
    assertEquals (one, one.subList (0, 0));
    assertEquals (one.subList (0, 0), one);

    one.add ((short) 1);
    assertEquals (one, one.subList (0, 1));
    assertEquals (one.subList (0, 1), one);

    one.add ((short) 1);
    one.add ((short) 2);
    one.add ((short) 3);
    one.add ((short) 5);
    one.add ((short) 8);
    assertEquals (one.subList (0, 4), one.subList (0, 4));
    assertEquals (one.subList (3, 5), one.subList (3, 5));
  }

  public void testEqualsWithShortListAndList ()
  {
    final ShortList ilist = makeEmptyShortList ();
    final List <Short> list = new ArrayList <Short> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty ShortList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty ShortList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListShortList (list), ilist);
    assertEquals (ilist, new ListShortList (list));
    assertEquals (new ShortListList (ilist), list);
    assertEquals (list, new ShortListList (ilist));

    ilist.add ((short) 1);
    list.add (new Short ((short) 1));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty ShortList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty ShortList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListShortList (list), ilist);
    assertEquals (ilist, new ListShortList (list));
    assertEquals (new ShortListList (ilist), list);
    assertEquals (list, new ShortListList (ilist));

    ilist.add ((short) 1);
    ilist.add ((short) 2);
    ilist.add ((short) 3);
    ilist.add ((short) 5);
    ilist.add ((short) 8);
    list.add (new Short ((short) 1));
    list.add (new Short ((short) 2));
    list.add (new Short ((short) 3));
    list.add (new Short ((short) 5));
    list.add (new Short ((short) 8));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty ShortList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty ShortList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListShortList (list), ilist);
    assertEquals (ilist, new ListShortList (list));
    assertEquals (new ShortListList (ilist), list);
    assertEquals (list, new ShortListList (ilist));

  }

  public void testClearAndSize ()
  {
    final ShortList list = makeEmptyShortList ();
    assertEquals (0, list.size ());
    for (int i = 0; i < 100; i++)
    {
      list.add ((short) i);
    }
    assertEquals (100, list.size ());
    list.clear ();
    assertEquals (0, list.size ());
  }

  public void testRemoveViaSubList ()
  {
    final ShortList list = makeEmptyShortList ();
    for (int i = 0; i < 100; i++)
    {
      list.add ((short) i);
    }
    final ShortList sub = list.subList (25, 75);
    assertEquals (50, sub.size ());
    for (int i = 0; i < 50; i++)
    {
      assertEquals (100 - i, list.size ());
      assertEquals (50 - i, sub.size ());
      assertEquals (25 + i, sub.removeElementAt (0));
      assertEquals (50 - i - 1, sub.size ());
      assertEquals (100 - i - 1, list.size ());
    }
    assertEquals (0, sub.size ());
    assertEquals (50, list.size ());
  }

  public void testAddGet ()
  {
    final ShortList list = makeEmptyShortList ();
    for (int i = 0; i < 255; i++)
    {
      list.add ((short) i);
    }
    for (int i = 0; i < 255; i++)
    {
      assertEquals ((short) i, list.get (i));
    }
  }

  public void testAddAndShift ()
  {
    final ShortList list = makeEmptyShortList ();
    list.add (0, (short) 1);
    assertEquals ("Should have one entry", 1, list.size ());
    list.add ((short) 3);
    list.add ((short) 4);
    list.add (1, (short) 2);
    for (int i = 0; i < 4; i++)
    {
      assertEquals ("Should get entry back", (short) (i + 1), list.get (i));
    }
    list.add (0, (short) 0);
    for (int i = 0; i < 5; i++)
    {
      assertEquals ("Should get entry back", (short) i, list.get (i));
    }
  }

  public void testIsSerializable () throws Exception
  {
    final ShortList list = makeFullShortList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final ShortList deser = (ShortList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testShortListSerializeDeserializeThenCompare () throws Exception
  {
    final ShortList list = makeFullShortList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final ShortList deser = (ShortList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final ShortList list = makeFullShortList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyShortList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullShortList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullShortList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullShortList ().subList (2, makeFullShortList ().size () + 2);
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
      makeEmptyShortList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullShortList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullShortList ().listIterator (makeFullShortList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final ShortListIterator iter = makeFullShortList ().listIterator ();
    try
    {
      iter.set ((short) 3);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

  public void testListIteratorSetAfterRemove () throws Exception
  {
    final ShortListIterator iter = makeFullShortList ().listIterator ();
    iter.next ();
    iter.remove ();
    try
    {
      iter.set ((short) 3);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

}
