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
import org.apache.commons.collections.primitives.adapters.BooleanListList;
import org.apache.commons.collections.primitives.adapters.ListBooleanList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestBooleanList extends BaseTestList
{

  // conventional
  // ------------------------------------------------------------------------

  public TestBooleanList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: boolean list
  // ------------------------------------------------------------------------

  protected abstract BooleanList makeEmptyBooleanList ();

  protected BooleanList makeFullBooleanList ()
  {
    final BooleanList list = makeEmptyBooleanList ();
    final boolean [] values = getFullBooleans ();
    for (final boolean value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected boolean [] getFullBooleans ()
  {
    final boolean [] result = new boolean [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = true;
    }
    return result;
  }

  protected boolean [] getOtherBooleans ()
  {
    final boolean [] result = new boolean [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = false;
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Boolean> makeEmptyList ()
  {
    return new BooleanListList (makeEmptyBooleanList ());
  }

  @Override
  public Boolean [] getFullElements ()
  {
    return wrapArray (getFullBooleans ());
  }

  @Override
  public Boolean [] getOtherElements ()
  {
    return wrapArray (getOtherBooleans ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Boolean [] wrapArray (final boolean [] primitives)
  {
    final Boolean [] result = new Boolean [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Boolean (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final BooleanList list = makeFullBooleanList ();
    final BooleanIterator iter = list.iterator ();
    iter.next ();
    list.add (true);
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

  public void testAddAllBooleanListAtIndex ()
  {
    final BooleanList source = makeFullBooleanList ();
    final BooleanList dest = makeFullBooleanList ();
    dest.addAll (1, source);

    final BooleanIterator iter = dest.iterator ();
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

  public void testToJustBigEnoughBooleanArray ()
  {
    final BooleanList list = makeFullBooleanList ();
    final boolean [] dest = new boolean [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final BooleanIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
  }

  public void testToLargerThanNeededBooleanArray ()
  {
    final BooleanList list = makeFullBooleanList ();
    final boolean [] dest = new boolean [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = true;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final BooleanIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i]);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (true, dest[i]);
    }
  }

  public void testToSmallerThanNeededBooleanArray ()
  {
    final BooleanList list = makeFullBooleanList ();
    final boolean [] dest = new boolean [list.size () / 2];
    final boolean [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final BooleanIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i]);
    }
  }

  public void testHashCodeSpecification ()
  {
    final BooleanList list = makeFullBooleanList ();
    int hash = 1;
    for (final BooleanIterator iter = list.iterator (); iter.hasNext ();)
    {
      hash = 31 * hash + new Boolean (iter.next ()).hashCode ();
    }
    assertEquals (hash, list.hashCode ());
  }

  public void testEqualsWithTwoBooleanLists ()
  {
    final BooleanList one = makeEmptyBooleanList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final BooleanList two = makeEmptyBooleanList ();
    assertEquals ("Empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on empty lists", two, one);

    one.add (true);
    assertEquals ("Equals is reflexive on non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add (true);
    assertEquals ("Non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on non empty list", one, two);

    one.add (true);
    one.add (false);
    one.add (true);
    one.add (false);
    one.add (true);
    one.add (false);
    assertEquals ("Equals is reflexive on larger non empty list", one, one);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

    two.add (true);
    two.add (false);
    two.add (true);
    two.add (false);
    two.add (true);
    two.add (false);
    assertEquals ("Larger non empty lists are equal", one, two);
    assertEquals ("Equals is symmetric on larger non empty list", two, one);

    one.add (true);
    two.add (false);
    assertTrue (!one.equals (two));
    assertTrue (!two.equals (one));

  }

  public void testBooleanSubListEquals ()
  {
    final BooleanList one = makeEmptyBooleanList ();
    assertEquals (one, one.subList (0, 0));
    assertEquals (one.subList (0, 0), one);

    one.add (true);
    assertEquals (one, one.subList (0, 1));
    assertEquals (one.subList (0, 1), one);

    one.add (true);
    one.add (false);
    one.add (true);
    one.add (true);
    one.add (false);
    one.add (false);
    assertEquals (one.subList (0, 4), one.subList (0, 4));
    assertEquals (one.subList (3, 5), one.subList (3, 5));
  }

  public void testEqualsWithBooleanListAndList ()
  {
    final BooleanList ilist = makeEmptyBooleanList ();
    final List <Boolean> list = new ArrayList <Boolean> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty BooleanList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty BooleanList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListBooleanList (list), ilist);
    assertEquals (ilist, new ListBooleanList (list));
    assertEquals (new BooleanListList (ilist), list);
    assertEquals (list, new BooleanListList (ilist));

    ilist.add (true);
    list.add (new Boolean (true));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty BooleanList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty BooleanList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListBooleanList (list), ilist);
    assertEquals (ilist, new ListBooleanList (list));
    assertEquals (new BooleanListList (ilist), list);
    assertEquals (list, new BooleanListList (ilist));

    ilist.add (true);
    ilist.add (false);
    ilist.add (true);
    ilist.add (true);
    ilist.add (false);
    list.add (new Boolean (true));
    list.add (Boolean.FALSE);
    list.add (Boolean.TRUE);
    list.add (Boolean.TRUE);
    list.add (new Boolean (false));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty BooleanList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty BooleanList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListBooleanList (list), ilist);
    assertEquals (ilist, new ListBooleanList (list));
    assertEquals (new BooleanListList (ilist), list);
    assertEquals (list, new BooleanListList (ilist));

  }

  public void testClearAndSize ()
  {
    final BooleanList list = makeEmptyBooleanList ();
    assertEquals (0, list.size ());
    for (int i = 0; i < 100; i++)
    {
      list.add (i % 2 == 0);
    }
    assertEquals (100, list.size ());
    list.clear ();
    assertEquals (0, list.size ());
  }

  public void testRemoveViaSubList ()
  {
    final BooleanList list = makeEmptyBooleanList ();
    for (int i = 0; i < 100; i++)
    {
      list.add (i % 2 == 0);
    }
    final BooleanList sub = list.subList (25, 75);
    assertEquals (50, sub.size ());
    for (int i = 0; i < 50; i++)
    {
      assertEquals (100 - i, list.size ());
      assertEquals (50 - i, sub.size ());
      assertEquals ((25 + i) % 2 == 0, sub.removeElementAt (0));
      assertEquals (50 - i - 1, sub.size ());
      assertEquals (100 - i - 1, list.size ());
    }
    assertEquals (0, sub.size ());
    assertEquals (50, list.size ());
  }

  public void testAddGet ()
  {
    final BooleanList list = makeEmptyBooleanList ();
    for (int i = 0; i < 255; i++)
    {
      list.add (i % 2 == 0);
    }
    for (int i = 0; i < 255; i++)
    {
      assertEquals (i % 2 == 0, list.get (i));
    }
  }

  public void testAddAndShift ()
  {
    final BooleanList list = makeEmptyBooleanList ();
    list.add (0, true);
    assertEquals ("Should have one entry", 1, list.size ());
    list.add (true);
    list.add (false);
    list.add (1, false);
    for (int i = 0; i < 4; i++)
    {
      assertEquals ("Should get entry back", (i % 2 == 0), list.get (i));
    }
  }

  public void testIsSerializable () throws Exception
  {
    final BooleanList list = makeFullBooleanList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final BooleanList deser = (BooleanList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testBooleanListSerializeDeserializeThenCompare () throws Exception
  {
    final BooleanList list = makeFullBooleanList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final BooleanList deser = (BooleanList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final BooleanList list = makeFullBooleanList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyBooleanList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullBooleanList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullBooleanList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullBooleanList ().subList (2, makeFullBooleanList ().size () + 2);
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
      makeEmptyBooleanList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullBooleanList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullBooleanList ().listIterator (makeFullBooleanList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final BooleanListIterator iter = makeFullBooleanList ().listIterator ();
    try
    {
      iter.set (true);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

  public void testListIteratorSetAfterRemove () throws Exception
  {
    final BooleanListIterator iter = makeFullBooleanList ().listIterator ();
    iter.next ();
    iter.remove ();
    try
    {
      iter.set (true);
      fail ("Expected IllegalStateException");
    }
    catch (final IllegalStateException e)
    {
      // expected
    }
  }

  @Override
  public void testCollectionRemoveAll ()
  {
    // Super's impl doesn't work because there are only two unique values in my
    // list.
    final BooleanList trueList = new ArrayBooleanList ();
    trueList.add (true);
    trueList.add (true);
    trueList.add (true);
    trueList.add (true);
    trueList.add (true);
    final BooleanList falseList = new ArrayBooleanList ();
    falseList.add (false);
    falseList.add (false);
    falseList.add (false);
    falseList.add (false);
    falseList.add (false);

    final BooleanList list = new ArrayBooleanList ();
    assertTrue (list.isEmpty ());
    assertFalse (list.removeAll (trueList));
    assertTrue (list.isEmpty ());

    list.add (false);
    list.add (false);
    assertEquals (2, list.size ());
    assertFalse (list.removeAll (trueList));
    assertEquals (2, list.size ());

    list.add (true);
    list.add (false);
    list.add (true);
    assertEquals (5, list.size ());
    assertTrue (list.removeAll (trueList));
    assertEquals (3, list.size ());

    for (final BooleanIterator iter = list.iterator (); iter.hasNext ();)
    {
      assertEquals (false, iter.next ());
    }

    assertFalse (list.removeAll (trueList));
    assertEquals (3, list.size ());

    for (final BooleanIterator iter = list.iterator (); iter.hasNext ();)
    {
      assertEquals (false, iter.next ());
    }

    assertTrue (list.removeAll (falseList));
    assertTrue (list.isEmpty ());
  }

  @Override
  public void testCollectionRetainAll ()
  {
    // Super's impl doesn't work because there are only two unique values in my
    // list.
    final BooleanList trueList = new ArrayBooleanList ();
    trueList.add (true);
    final BooleanList falseList = new ArrayBooleanList ();
    falseList.add (false);

    final BooleanList list = new ArrayBooleanList ();
    assertTrue (list.isEmpty ());
    assertFalse (list.retainAll (falseList));
    assertTrue (list.isEmpty ());

    list.add (false);
    list.add (false);
    assertEquals (2, list.size ());
    assertFalse (list.retainAll (falseList));
    assertEquals (2, list.size ());

    list.add (true);
    list.add (false);
    list.add (true);
    assertEquals (5, list.size ());
    assertTrue (list.retainAll (falseList));
    assertEquals (3, list.size ());

    for (final BooleanIterator iter = list.iterator (); iter.hasNext ();)
    {
      assertEquals (false, iter.next ());
    }

    assertFalse (list.retainAll (falseList));
    assertEquals (3, list.size ());

    for (final BooleanIterator iter = list.iterator (); iter.hasNext ();)
    {
      assertEquals (false, iter.next ());
    }

    assertTrue (list.retainAll (trueList));
    assertTrue (list.isEmpty ());
  }

  @Override
  public void testListEquals ()
  {
    // Super type doesn't work because there are only two unique values in my
    // list.
  }
}
