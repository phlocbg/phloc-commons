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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestIntCollections extends TestCase
{
  public TestIntCollections (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestIntCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonIntListIterator ()
  {
    final IntListIterator iter = IntCollections.singletonIntListIterator (42);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals (42, iter.next (), 0d);
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals (42, iter.previous (), 0d);
    try
    {
      iter.set (42);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonIntIterator ()
  {
    final IntIterator iter = IntCollections.singletonIntIterator (42);
    assertTrue (iter.hasNext ());
    assertEquals (42, iter.next (), 0d);
    assertTrue (!iter.hasNext ());
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

  public void testSingletonIntList ()
  {
    final IntList list = IntCollections.singletonIntList (42);
    assertEquals (1, list.size ());
    assertEquals (42, list.get (0), 0d);
    try
    {
      list.add (42);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableIntListNull ()
  {
    try
    {
      IntCollections.unmodifiableIntList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyIntList ()
  {
    assertSame (IntCollections.EMPTY_INT_LIST, IntCollections.getEmptyIntList ());
    assertTrue (IntCollections.EMPTY_INT_LIST.isEmpty ());
    try
    {
      IntCollections.EMPTY_INT_LIST.add (42);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableIntIteratorNull ()
  {
    try
    {
      IntCollections.unmodifiableIntIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyIntIterator ()
  {
    assertSame (IntCollections.EMPTY_INT_ITERATOR, IntCollections.getEmptyIntIterator ());
    assertTrue (!IntCollections.EMPTY_INT_ITERATOR.hasNext ());
    try
    {
      IntCollections.EMPTY_INT_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableIntListIteratorNull ()
  {
    try
    {
      IntCollections.unmodifiableIntListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyIntListIterator ()
  {
    assertSame (IntCollections.EMPTY_INT_LIST_ITERATOR, IntCollections.getEmptyIntListIterator ());
    assertTrue (!IntCollections.EMPTY_INT_LIST_ITERATOR.hasNext ());
    assertTrue (!IntCollections.EMPTY_INT_LIST_ITERATOR.hasPrevious ());
    try
    {
      IntCollections.EMPTY_INT_LIST_ITERATOR.add (42);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
