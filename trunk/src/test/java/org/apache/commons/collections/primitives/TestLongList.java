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
import org.apache.commons.collections.primitives.adapters.ListLongList;
import org.apache.commons.collections.primitives.adapters.LongListList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestLongList extends BaseTestList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestLongList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: long list
  // ------------------------------------------------------------------------

  protected abstract LongList makeEmptyLongList ();

  protected LongList makeFullLongList ()
  {
    final LongList list = makeEmptyLongList ();
    final long [] values = getFullLongs ();
    for (final long value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected long [] getFullLongs ()
  {
    final long [] result = new long [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = i + ((long) Integer.MAX_VALUE - (long) 10);
    }
    return result;
  }

  protected long [] getOtherLongs ()
  {
    final long [] result = new long [16];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = (long) i + (long) 43;
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Long> makeEmptyList ()
  {
    return new LongListList (makeEmptyLongList ());
  }

  @Override
  public Long [] getFullElements ()
  {
    return wrapArray (getFullLongs ());
  }

  @Override
  public Long [] getOtherElements ()
  {
    return wrapArray (getOtherLongs ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Long [] wrapArray (final long [] primitives)
  {
    final Long [] result = new Long [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Long (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final LongList list = makeFullLongList ();
    final LongIterator iter = list.iterator ();
    iter.next ();
    list.add (3);
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

  public void testAddAllLongListAtIndex ()
  {
    final LongList source = makeFullLongList ();
    final LongList dest = makeFullLongList ();
    dest.addAll (1, source);

    final LongIterator iter = dest.iterator ();
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

  public void testToJustBigEnoughLongArray ()
  {
    final LongList list = makeFullLongList ();
    final long [] dest = new long [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final LongIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
  }

  public void testToLargerThanNeededLongArray ()
  {
    final LongList list = makeFullLongList ();
    final long [] dest = new long [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = Long.MAX_VALUE;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final LongIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (Long.MAX_VALUE, dest[i]);
    }
  }

  public void testToSmallerThanNeededLongArray ()
  {
    final LongList list = makeFullLongList ();
    final long [] dest = new long [list.size () / 2];
    final long [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final LongIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i]);
    }
  }

  public void testEqualsWithTwoLongLists ()
  {
    final LongList one = makeEmptyLongList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final LongList two = makeEmptyLongList ();
    assertEquals ("Empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on empty lists", two, one);

    one.add (1);
    assertEquals ("Equals is reflexive on non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add (1);
    assertEquals ("Non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on non empty list", one, two);

    one.add (1);
    one.add (2);
    one.add (3);
    one.add (5);
    one.add (8);
    assertEquals ("Equals is reflexive on larger non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add (1);
    two.add (2);
    two.add (3);
    two.add (5);
    two.add (8);
    assertEquals ("Larger non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on larger non empty list", two, one);

    one.add (9);
    two.add (10);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

  }

  public void testLongSubListEquals ()
  {
    final LongList one = makeEmptyLongList ();
    assertEquals (one, one.subList (0, 0));
    assertEquals (one.subList (0, 0), one);

    one.add (1);
    assertEquals (one, one.subList (0, 1));
    assertEquals (one.subList (0, 1), one);

    one.add (1);
    one.add (2);
    one.add (3);
    one.add (5);
    one.add (8);
    assertEquals (one.subList (0, 4), one.subList (0, 4));
    assertEquals (one.subList (3, 5), one.subList (3, 5));
  }

  public void testEqualsWithLongListAndList ()
  {
    final LongList ilist = makeEmptyLongList ();
    final List <Long> list = new ArrayList <Long> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty LongList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty LongList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListLongList (list), ilist);
    assertEquals (ilist, new ListLongList (list));
    assertEquals (new LongListList (ilist), list);
    assertEquals (list, new LongListList (ilist));

    ilist.add (1);
    list.add (new Long (1));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty LongList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty LongList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListLongList (list), ilist);
    assertEquals (ilist, new ListLongList (list));
    assertEquals (new LongListList (ilist), list);
    assertEquals (list, new LongListList (ilist));

    ilist.add (1);
    ilist.add (2);
    ilist.add (3);
    ilist.add (5);
    ilist.add (8);
    list.add (new Long (1));
    list.add (new Long (2));
    list.add (new Long (3));
    list.add (new Long (5));
    list.add (new Long (8));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty LongList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty LongList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListLongList (list), ilist);
    assertEquals (ilist, new ListLongList (list));
    assertEquals (new LongListList (ilist), list);
    assertEquals (list, new LongListList (ilist));

  }

  public void testClearAndSize ()
  {
    final LongList list = makeEmptyLongList ();
    assertEquals (0, list.size ());
    for (int i = 0; i < 100; i++)
    {
      list.add (i);
    }
    assertEquals (100, list.size ());
    list.clear ();
    assertEquals (0, list.size ());
  }

  public void testRemoveViaSubList ()
  {
    final LongList list = makeEmptyLongList ();
    for (int i = 0; i < 100; i++)
    {
      list.add (i);
    }
    final LongList sub = list.subList (25, 75);
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
    final LongList list = makeEmptyLongList ();
    for (int i = 0; i < 1000; i++)
    {
      list.add (i);
    }
    for (int i = 0; i < 1000; i++)
    {
      assertEquals (i, list.get (i));
    }
  }

  public void testAddAndShift ()
  {
    final LongList list = makeEmptyLongList ();
    list.add (0, 1);
    assertEquals ("Should have one entry", 1, list.size ());
    list.add (3);
    list.add (4);
    list.add (1, 2);
    for (int i = 0; i < 4; i++)
    {
      assertEquals ("Should get entry back", i + 1, list.get (i));
    }
    list.add (0, 0);
    for (int i = 0; i < 5; i++)
    {
      assertEquals ("Should get entry back", i, list.get (i));
    }
  }

  public void testIsSerializable () throws Exception
  {
    final LongList list = makeFullLongList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final LongList deser = (LongList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testLongListSerializeDeserializeThenCompare () throws Exception
  {
    final LongList list = makeFullLongList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final LongList deser = (LongList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final LongList list = makeFullLongList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyLongList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullLongList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullLongList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullLongList ().subList (2, makeFullLongList ().size () + 2);
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
      makeEmptyLongList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullLongList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullLongList ().listIterator (makeFullLongList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final LongListIterator iter = makeFullLongList ().listIterator ();
    try
    {
      iter.set (3);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

  public void testListIteratorSetAfterRemove () throws Exception
  {
    final LongListIterator iter = makeFullLongList ().listIterator ();
    iter.next ();
    iter.remove ();
    try
    {
      iter.set (3);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

}
