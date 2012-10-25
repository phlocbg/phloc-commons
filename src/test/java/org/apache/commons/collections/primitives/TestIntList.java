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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.apache.commons.collections.primitives.adapters.BaseTestList;
import org.apache.commons.collections.primitives.adapters.IntListList;
import org.apache.commons.collections.primitives.adapters.ListIntList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestIntList extends BaseTestList <Integer>
{
  // conventional
  // ------------------------------------------------------------------------

  public TestIntList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: int list
  // ------------------------------------------------------------------------

  protected abstract IntList makeEmptyIntList ();

  protected IntList makeFullIntList ()
  {
    final IntList list = makeEmptyIntList ();
    final int [] values = getFullIntegers ();
    for (final int value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected int [] getFullIntegers ()
  {
    final int [] result = new int [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = i + 19;
    }
    return result;
  }

  protected int [] getOtherIntegers ()
  {
    final int [] result = new int [16];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = i + 43;
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Integer> makeEmptyList ()
  {
    return new IntListList (makeEmptyIntList ());
  }

  @Override
  public Integer [] getFullElements ()
  {
    return wrapArray (getFullIntegers ());
  }

  @Override
  public Integer [] getOtherElements ()
  {
    return wrapArray (getOtherIntegers ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Integer [] wrapArray (final int [] primitives)
  {
    final Integer [] result = new Integer [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Integer (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final IntList list = makeFullIntList ();
    final IntIterator iter = list.iterator ();
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

  public void testAddAllIntListAtIndex ()
  {
    final IntList source = makeFullIntList ();
    final IntList dest = makeFullIntList ();
    dest.addAll (1, source);

    final IntIterator iter = dest.iterator ();
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

  public void testToJustBigEnoughIntArray ()
  {
    final IntList list = makeFullIntList ();
    final int [] dest = new int [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final IntIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
  }

  public void testToLargerThanNeededIntArray ()
  {
    final IntList list = makeFullIntList ();
    final int [] dest = new int [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = Integer.MAX_VALUE;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final IntIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (Integer.MAX_VALUE, dest[i]);
    }
  }

  public void testToSmallerThanNeededIntArray ()
  {
    final IntList list = makeFullIntList ();
    final int [] dest = new int [list.size () / 2];
    final int [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final IntIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i]);
    }
  }

  public void testEqualsWithTwoIntLists ()
  {
    final IntList one = makeEmptyIntList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final IntList two = makeEmptyIntList ();
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

  public void testIntSubListEquals ()
  {
    final IntList one = makeEmptyIntList ();
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

  @SuppressFBWarnings ("EC_UNRELATED_TYPES")
  public void testEqualsWithIntListAndList ()
  {
    final IntList ilist = makeEmptyIntList ();
    final List <Integer> list = new ArrayList <Integer> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty IntList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty IntList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListIntList (list), ilist);
    assertEquals (ilist, new ListIntList (list));
    assertEquals (new IntListList (ilist), list);
    assertEquals (list, new IntListList (ilist));

    ilist.add (1);
    list.add (new Integer (1));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty IntList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty IntList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListIntList (list), ilist);
    assertEquals (ilist, new ListIntList (list));
    assertEquals (new IntListList (ilist), list);
    assertEquals (list, new IntListList (ilist));

    ilist.add (1);
    ilist.add (2);
    ilist.add (3);
    ilist.add (5);
    ilist.add (8);
    list.add (new Integer (1));
    list.add (new Integer (2));
    list.add (new Integer (3));
    list.add (new Integer (5));
    list.add (new Integer (8));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty IntList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty IntList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListIntList (list), ilist);
    assertEquals (ilist, new ListIntList (list));
    assertEquals (new IntListList (ilist), list);
    assertEquals (list, new IntListList (ilist));

  }

  public void testClearAndSize ()
  {
    final IntList list = makeEmptyIntList ();
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
    final IntList list = makeEmptyIntList ();
    for (int i = 0; i < 100; i++)
    {
      list.add (i);
    }
    final IntList sub = list.subList (25, 75);
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
    final IntList list = makeEmptyIntList ();
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
    final IntList list = makeEmptyIntList ();
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
    final IntList list = makeFullIntList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final IntList deser = (IntList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testIntListSerializeDeserializeThenCompare () throws Exception
  {
    final IntList list = makeFullIntList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final IntList deser = (IntList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final IntList list = makeFullIntList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyIntList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullIntList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullIntList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullIntList ().subList (2, makeFullIntList ().size () + 2);
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
      makeEmptyIntList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullIntList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullIntList ().listIterator (makeFullIntList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final IntListIterator iter = makeFullIntList ().listIterator ();
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
    final IntListIterator iter = makeFullIntList ().listIterator ();
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
