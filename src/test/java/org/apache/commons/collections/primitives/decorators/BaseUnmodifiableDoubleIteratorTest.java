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
package org.apache.commons.collections.primitives.decorators;

import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableDoubleIteratorTest extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableDoubleIteratorTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------
  protected abstract DoubleIterator makeUnmodifiableDoubleIterator ();

  protected DoubleIterator makeDoubleIterator ()
  {
    final DoubleList list = new ArrayDoubleList ();
    for (double i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list.iterator ();
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testDoubleIteratorNotModifiable ()
  {
    final DoubleIterator iter = makeUnmodifiableDoubleIterator ();
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

  public final void testIterateDoubleIterator ()
  {
    final DoubleIterator iter = makeUnmodifiableDoubleIterator ();
    for (final DoubleIterator expected = makeDoubleIterator (); expected.hasNext ();)
    {
      assertTrue (iter.hasNext ());
      assertEquals (expected.next (), iter.next (), 0);
    }
    assertTrue (!iter.hasNext ());
  }

}
