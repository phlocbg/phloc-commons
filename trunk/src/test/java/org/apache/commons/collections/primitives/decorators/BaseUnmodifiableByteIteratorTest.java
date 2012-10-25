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

import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableByteIteratorTest extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseUnmodifiableByteIteratorTest (final String testName)
  {
    super (testName);
  }

  // framework
  // ------------------------------------------------------------------------
  protected abstract ByteIterator makeUnmodifiableByteIterator ();

  protected ByteIterator makeByteIterator ()
  {
    final ByteList list = new ArrayByteList ();
    for (byte i = 0; i < 10; i++)
    {
      list.add (i);
    }
    return list.iterator ();
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testByteIteratorNotModifiable ()
  {
    final ByteIterator iter = makeUnmodifiableByteIterator ();
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

  public final void testIterateByteIterator ()
  {
    final ByteIterator iter = makeUnmodifiableByteIterator ();
    for (final ByteIterator expected = makeByteIterator (); expected.hasNext ();)
    {
      assertTrue (iter.hasNext ());
      assertEquals (expected.next (), iter.next ());
    }
    assertTrue (!iter.hasNext ());
  }

}
