/**
 * Copyright (C) 2006-2013 phloc systems
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
import org.apache.commons.collections.primitives.adapters.DoubleListList;
import org.apache.commons.collections.primitives.adapters.ListDoubleList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestDoubleList extends BaseTestList <Double>
{

  // conventional
  // ------------------------------------------------------------------------

  public TestDoubleList (final String testName)
  {
    super (testName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  // collections testing framework: double list
  // ------------------------------------------------------------------------

  protected abstract DoubleList makeEmptyDoubleList ();

  protected DoubleList makeFullDoubleList ()
  {
    final DoubleList list = makeEmptyDoubleList ();
    final double [] values = getFullDoubles ();
    for (final double value : values)
    {
      list.add (value);
    }
    return list;
  }

  protected double [] getFullDoubles ()
  {
    final double [] result = new double [19];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = (i);
    }
    return result;
  }

  protected double [] getOtherDoubles ()
  {
    final double [] result = new double [16];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = i + 43;
    }
    return result;
  }

  // collections testing framework: inherited
  // ------------------------------------------------------------------------

  @Override
  public List <Double> makeEmptyList ()
  {
    return new DoubleListList (makeEmptyDoubleList ());
  }

  @Override
  public Double [] getFullElements ()
  {
    return wrapArray (getFullDoubles ());
  }

  @Override
  public Double [] getOtherElements ()
  {
    return wrapArray (getOtherDoubles ());
  }

  // private utils
  // ------------------------------------------------------------------------

  private Double [] wrapArray (final double [] primitives)
  {
    final Double [] result = new Double [primitives.length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = new Double (primitives[i]);
    }
    return result;
  }

  // tests
  // ------------------------------------------------------------------------

  public void testExceptionOnConcurrentModification ()
  {
    final DoubleList list = makeFullDoubleList ();
    final DoubleIterator iter = list.iterator ();
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

  public void testAddAllDoubleListAtIndex ()
  {
    final DoubleList source = makeFullDoubleList ();
    final DoubleList dest = makeFullDoubleList ();
    dest.addAll (1, source);

    final DoubleIterator iter = dest.iterator ();
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

  public void testToJustBigEnoughDoubleArray ()
  {
    final DoubleList list = makeFullDoubleList ();
    final double [] dest = new double [list.size ()];
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final DoubleIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i], 0f);
    }
  }

  public void testToLargerThanNeededDoubleArray ()
  {
    final DoubleList list = makeFullDoubleList ();
    final double [] dest = new double [list.size () * 2];
    for (int i = 0; i < dest.length; i++)
    {
      dest[i] = Double.MAX_VALUE;
    }
    assertSame (dest, list.toArray (dest));
    int i = 0;
    for (final DoubleIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest[i], 0f);
    }
    for (; i < dest.length; i++)
    {
      assertEquals (Double.MAX_VALUE, dest[i], 0f);
    }
  }

  public void testToSmallerThanNeededDoubleArray ()
  {
    final DoubleList list = makeFullDoubleList ();
    final double [] dest = new double [list.size () / 2];
    final double [] dest2 = list.toArray (dest);
    assertTrue (dest != dest2);
    int i = 0;
    for (final DoubleIterator iter = list.iterator (); iter.hasNext (); i++)
    {
      assertEquals (iter.next (), dest2[i], 0f);
    }
  }

  public void testEqualsWithTwoDoubleLists ()
  {
    final DoubleList one = makeEmptyDoubleList ();
    assertEquals ("Equals is reflexive on empty list", one, one);
    final DoubleList two = makeEmptyDoubleList ();
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

  public void testDoubleSubListEquals ()
  {
    final DoubleList one = makeEmptyDoubleList ();
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
  public void testEqualsWithDoubleListAndList ()
  {
    final DoubleList ilist = makeEmptyDoubleList ();
    final List <Double> list = new ArrayList <Double> ();

    assertTrue ("Unwrapped, empty List should not be equal to empty DoubleList.", !ilist.equals (list));
    assertTrue ("Unwrapped, empty DoubleList should not be equal to empty List.", !list.equals (ilist));

    assertEquals (new ListDoubleList (list), ilist);
    assertEquals (ilist, new ListDoubleList (list));
    assertEquals (new DoubleListList (ilist), list);
    assertEquals (list, new DoubleListList (ilist));

    ilist.add (1);
    list.add (new Double (1));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty DoubleList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty DoubleList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListDoubleList (list), ilist);
    assertEquals (ilist, new ListDoubleList (list));
    assertEquals (new DoubleListList (ilist), list);
    assertEquals (list, new DoubleListList (ilist));

    ilist.add (1);
    ilist.add (2);
    ilist.add (3);
    ilist.add (5);
    ilist.add (8);
    list.add (new Double (1));
    list.add (new Double (2));
    list.add (new Double (3));
    list.add (new Double (5));
    list.add (new Double (8));

    assertTrue ("Unwrapped, non-empty List is not equal to non-empty DoubleList.", !ilist.equals (list));
    assertTrue ("Unwrapped, non-empty DoubleList is not equal to non-empty List.", !list.equals (ilist));

    assertEquals (new ListDoubleList (list), ilist);
    assertEquals (ilist, new ListDoubleList (list));
    assertEquals (new DoubleListList (ilist), list);
    assertEquals (list, new DoubleListList (ilist));

  }

  public void testClearAndSize ()
  {
    final DoubleList list = makeEmptyDoubleList ();
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
    final DoubleList list = makeEmptyDoubleList ();
    for (int i = 0; i < 100; i++)
    {
      list.add (i);
    }
    final DoubleList sub = list.subList (25, 75);
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
    final DoubleList list = makeEmptyDoubleList ();
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
    final DoubleList list = makeEmptyDoubleList ();
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
    final DoubleList list = makeFullDoubleList ();
    assertTrue (list instanceof Serializable);
    final byte [] ser = writeExternalFormToBytes ((Serializable) list);
    final DoubleList deser = (DoubleList) (readExternalFormFromBytes (ser));
    assertEquals (list, deser);
    assertEquals (deser, list);
  }

  public void testDoubleListSerializeDeserializeThenCompare () throws Exception
  {
    final DoubleList list = makeFullDoubleList ();
    if (list instanceof Serializable)
    {
      final byte [] ser = writeExternalFormToBytes ((Serializable) list);
      final DoubleList deser = (DoubleList) (readExternalFormFromBytes (ser));
      assertEquals ("obj != deserialize(serialize(obj))", list, deser);
    }
  }

  public void testSubListsAreNotSerializable () throws Exception
  {
    final DoubleList list = makeFullDoubleList ().subList (2, 3);
    assertTrue (!(list instanceof Serializable));
  }

  public void testSubListOutOfBounds () throws Exception
  {
    try
    {
      makeEmptyDoubleList ().subList (2, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullDoubleList ().subList (-1, 3);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullDoubleList ().subList (5, 2);
      fail ("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      makeFullDoubleList ().subList (2, makeFullDoubleList ().size () + 2);
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
      makeEmptyDoubleList ().listIterator (2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullDoubleList ().listIterator (-1);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }

    try
    {
      makeFullDoubleList ().listIterator (makeFullDoubleList ().size () + 2);
      fail ("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e)
    {
      // expected
    }
  }

  public void testListIteratorSetWithoutNext () throws Exception
  {
    final DoubleListIterator iter = makeFullDoubleList ().listIterator ();
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
    final DoubleListIterator iter = makeFullDoubleList ().listIterator ();
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
