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
import org.apache.commons.collections.primitives.adapters.FloatListList;
import org.apache.commons.collections.primitives.adapters.ListFloatList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestFloatList extends BaseTestList <Float>
{

  // conventional
  // ------------------------------------------------------------------------

  public TestFloatList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: float list
  // ------------------------------------------------------------------------

  protected abstract FloatList makeEmptyFloatList ();

  protected FloatList makeFullFloatList ()
  {
    final FloatList list = makeEmptyFloatList ();
    final float [] values = getFullFloats ();
    for (final float value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected float [] getFullFloats ()
  {
    final float [] result = new float [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = (i);
    }
    return result;
  }

  protected float [] getOtherFloats ()
  {
    final float [] result = new float [16];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = i + 43;
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Float> makeEmptyList ()
  {
    return new FloatListList (makeEmptyFloatList ());
  }

  @Override
  public Float [] getFullElements ()
  {
    return wrapArray (getFullFloats ());
  }

  @Override
  public Float [] getOtherElements ()
  {
    return wrapArray (getOtherFloats ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Float [] wrapArray (final float [] primitives)
  {
    final Float [] result = new Float [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Float (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final FloatList list = makeFullFloatList ();
    final FloatIterator iter = list.iterator ();
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

  public void testAddAllFloatListAtIndex ()
  {
    final FloatList source = makeFullFloatList ();
    final FloatList dest = makeFullFloatList ();
    dest.addAll (1, source);

    final FloatIterator iter = dest.iterator ();
    assertTrue (iter.hasNext ());
    assertEquals (source.get (0), iter.next (), 0d);
    for (int i = 0; i < source.size (); i++)
    {
      assertTrue (iter.hasNext ());
      assertEquals (source.get (i), iter.next (), 0d);
    }
    for (int i = 1; i < source.size (); i++)
    {
      assertTrue (iter.hasNext ());
      assertEquals (source.get (i), iter.next (), 0d);
    }
    assertFalse (iter.hasNext ());
  }

  public void testToJustBigEnoughFloatArray ()
  {
    final FloatList list = makeFullFloatList ();
    final float [] dest = new float [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final FloatIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i], 0f);
    }
  }

  public void testToLargerThanNeededFloatArray ()
  {
    final FloatList list = makeFullFloatList ();
    final float [] dest = new float [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = Float.MAX_VALUE;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final FloatIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i], 0f);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (Float.MAX_VALUE, dest[i], 0f);
    }
  }

  public void testToSmallerThanNeededFloatArray ()
  {
    final FloatList list = makeFullFloatList ();
    final float [] dest = new float [list.size () / 2];
    final float [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final FloatIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i], 0f);
    }
  }

  public void testEqualsWithTwoFloatLists ()
  {
    final FloatList one = makeEmptyFloatList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final FloatList two = makeEmptyFloatList ();
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

  public void testFloatSubListEquals ()
  {
    final FloatList one = makeEmptyFloatList ();
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

  public void testEqualsWithFloatListAndList ()
  {
    final FloatList ilist = makeEmptyFloatList ();
    final List <Float> list = new ArrayList <Float> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty FloatList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty FloatList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListFloatList (list), ilist);
    assertEquals (ilist, new ListFloatList (list));
    assertEquals (new FloatListList (ilist), list);
    assertEquals (list, new FloatListList (ilist));

    ilist.add (1);
    list.add (new Float (1));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty FloatList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty FloatList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListFloatList (list), ilist);
    assertEquals (ilist, new ListFloatList (list));
    assertEquals (new FloatListList (ilist), list);
    assertEquals (list, new FloatListList (ilist));

    ilist.add (1);
    ilist.add (2);
    ilist.add (3);
    ilist.add (5);
    ilist.add (8);
    list.add (new Float (1));
    list.add (new Float (2));
    list.add (new Float (3));
    list.add (new Float (5));
    list.add (new Float (8));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty FloatList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty FloatList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListFloatList (list), ilist);
    assertEquals (ilist, new ListFloatList (list));
    assertEquals (new FloatListList (ilist), list);
    assertEquals (list, new FloatListList (ilist));

  }

  public void testClearAndSize ()
  {
    final FloatList list = makeEmptyFloatList ();
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
    final FloatList list = makeEmptyFloatList ();
    for (int i = 0; i < 100; i++)
    {
      list.add (i);
    }
    final FloatList sub = list.subList (25, 75);
    assertEquals (50, sub.size ());
    for (int i = 0; i < 50; i++)
    {
      assertEquals (100 - i, list.size ());
      assertEquals (50 - i, sub.size ());
      assertEquals (25 + i, sub.removeElementAt (0), 0f);
      assertEquals (50 - i - 1, sub.size ());
      assertEquals (100 - i - 1, list.size ());
    }
    assertEquals (0, sub.size ());
    assertEquals (50, list.size ());
  }

  public void testAddGet ()
  {
    final FloatList list = makeEmptyFloatList ();
    for (int i = 0; i < 255; i++)
    {
      list.add (i);
    }
    for (int i = 0; i < 255; i++)
    {
      assertEquals (i, list.get (i), 0f);
    }
  }

  public void testAddAndShift ()
  {
    final FloatList list = makeEmptyFloatList ();
    list.add (0, 1);
    assertEquals ("Should have one entry", 1, list.size ());
    list.add (3);
    list.add (4);
    list.add (1, 2);
    for (int i = 0; i < 4; i++)
    {
      assertEquals ("Should get entry back", i + 1, list.get (i), 0f);
    }
    list.add (0, 0);
    for (int i = 0; i < 5; i++)
    {
      assertEquals ("Should get entry back", i, list.get (i), 0f);
    }
  }

  public void testIsSerializable () throws Exception
  {
    final FloatList list = makeFullFloatList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final FloatList deser = (FloatList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testFloatListSerializeDeserializeThenCompare () throws Exception
  {
    final FloatList list = makeFullFloatList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final FloatList deser = (FloatList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final FloatList list = makeFullFloatList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyFloatList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullFloatList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullFloatList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullFloatList ().subList (2, makeFullFloatList ().size () + 2);
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
      makeEmptyFloatList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullFloatList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullFloatList ().listIterator (makeFullFloatList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final FloatListIterator iter = makeFullFloatList ().listIterator ();
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
    final FloatListIterator iter = makeFullFloatList ().listIterator ();
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
