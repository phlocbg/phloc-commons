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
package org.apache.commons.collections.primitives.decorators;

import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableDoubleListTest extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableDoubleListTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------

  protected abstract DoubleList makeUnmodifiableDoubleList ();

  protected DoubleList makeDoubleList ()
  {
    final DoubleList list = new ArrayDoubleList ();
    for (double i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list;
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testNotModifiable () throws Exception
  {
    assertListNotModifiable (makeUnmodifiableDoubleList ());
  }

  public final void testSublistNotModifiable () throws Exception
  {
    final DoubleList list = makeUnmodifiableDoubleList ();
    assertListNotModifiable (list.subList (0, list.size () - 2));
  }

  public final void testIteratorNotModifiable () throws Exception
  {
    final DoubleList list = makeUnmodifiableDoubleList ();
    assertIteratorNotModifiable (list.iterator ());
    assertIteratorNotModifiable (list.subList (0, list.size () - 2).iterator ());
  }

  public final void testListIteratorNotModifiable () throws Exception
  {
    final DoubleList list = makeUnmodifiableDoubleList ();
    assertListIteratorNotModifiable (list.listIterator ());
    assertListIteratorNotModifiable (list.subList (0, list.size () - 2).listIterator ());
    assertListIteratorNotModifiable (list.listIterator (1));
    assertListIteratorNotModifiable (list.subList (0, list.size () - 2).listIterator (1));
  }

  // util
  // ------------------------------------------------------------------------

  private void assertListIteratorNotModifiable (final DoubleListIterator iter) throws Exception
  {
    assertIteratorNotModifiable (iter);

    assertTrue (iter.hasPrevious ());

    try
    {
      iter.set (2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      iter.add (2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  private void assertIteratorNotModifiable (final DoubleIterator iter) throws Exception
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

  private void assertListNotModifiable (final DoubleList list) throws Exception
  {
    try
    {
      list.add (1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.add (1, 2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.addAll (makeDoubleList ());
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.addAll (1, makeDoubleList ());
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
      list.removeElement (1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.removeAll (makeDoubleList ());
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }

    try
    {
      list.retainAll (makeDoubleList ());
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
      list.set (1, 2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
