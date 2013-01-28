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

import org.apache.commons.collections.primitives.ArrayShortList;
import org.apache.commons.collections.primitives.ShortIterator;
import org.apache.commons.collections.primitives.ShortList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableShortIteratorTest extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableShortIteratorTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------
  protected abstract ShortIterator makeUnmodifiableShortIterator ();

  protected ShortIterator makeShortIterator ()
  {
    final ShortList list = new ArrayShortList ();
    for (short i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list.iterator ();
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testShortIteratorNotModifiable ()
  {
    final ShortIterator iter = makeUnmodifiableShortIterator ();
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

  public final void testIterateShortIterator ()
  {
    final ShortIterator iter = makeUnmodifiableShortIterator ();
    for (final ShortIterator expected = makeShortIterator (); expected.hasNext ();)
    {
      assertTrue (iter.hasNext ());
      assertEquals (expected.next (), iter.next ());
    }
    assertTrue (!iter.hasNext ());
  }

}