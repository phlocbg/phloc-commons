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

import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.LongList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableLongIteratorTest extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableLongIteratorTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------
  protected abstract LongIterator makeUnmodifiableLongIterator ();

  protected LongIterator makeLongIterator ()
  {
    final LongList list = new ArrayLongList ();
    for (long i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list.iterator ();
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testLongIteratorNotModifiable ()
  {
    final LongIterator iter = makeUnmodifiableLongIterator ();
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

  public final void testIterateLongIterator ()
  {
    final LongIterator iter = makeUnmodifiableLongIterator ();
    for (final LongIterator expected = makeLongIterator (); expected.hasNext ();)
    {
      assertTrue (iter.hasNext ());
      assertEquals (expected.next (), iter.next ());
    }
    assertTrue (!iter.hasNext ());
  }

}
