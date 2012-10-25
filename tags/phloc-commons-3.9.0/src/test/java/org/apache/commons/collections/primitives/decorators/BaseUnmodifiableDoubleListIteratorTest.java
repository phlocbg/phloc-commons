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
package org.apache.commons.collections.primitives.decorators;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableDoubleListIteratorTest extends BaseUnmodifiableDoubleIteratorTest
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableDoubleListIteratorTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------

  protected abstract DoubleListIterator makeUnmodifiableDoubleListIterator ();

  @Override
  protected DoubleIterator makeUnmodifiableDoubleIterator ()
  {
    return makeUnmodifiableDoubleListIterator ();
  }

  @Override
  protected DoubleIterator makeDoubleIterator ()
  {
    return makeDoubleListIterator ();
  }

  protected DoubleListIterator makeDoubleListIterator ()
  {
    final DoubleList list = new ArrayDoubleList ();
    for (double i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list.listIterator ();
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testDoubleListIteratorNotModifiable ()
  {
    final DoubleListIterator iter = makeUnmodifiableDoubleListIterator ();
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
    try
    {
      iter.add (1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
    try
    {
      iter.set (3);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public final void testIterateDoubleListIterator ()
  {
    final DoubleListIterator iter = makeUnmodifiableDoubleListIterator ();
    final DoubleListIterator expected = makeDoubleListIterator ();

    assertTrue (!iter.hasPrevious ());

    while (expected.hasNext ())
    {
      assertTrue (iter.hasNext ());
      assertEquals (expected.nextIndex (), iter.nextIndex ());
      assertEquals (expected.previousIndex (), iter.previousIndex ());
      assertEquals (expected.next (), iter.next (), 0);
      assertTrue (iter.hasPrevious ());
      assertEquals (expected.nextIndex (), iter.nextIndex ());
      assertEquals (expected.previousIndex (), iter.previousIndex ());
    }

    assertTrue (!iter.hasNext ());

    while (expected.hasPrevious ())
    {
      assertTrue (iter.hasPrevious ());
      assertEquals (expected.nextIndex (), iter.nextIndex ());
      assertEquals (expected.previousIndex (), iter.previousIndex ());
      assertEquals (expected.previous (), iter.previous (), 0);
      assertTrue (iter.hasNext ());
      assertEquals (expected.nextIndex (), iter.nextIndex ());
      assertEquals (expected.previousIndex (), iter.previousIndex ());
    }
    assertTrue (!iter.hasPrevious ());
  }

}
