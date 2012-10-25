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
public class TestFloatCollections extends TestCase
{
  public TestFloatCollections (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestFloatCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonFloatListIterator ()
  {
    final FloatListIterator iter = FloatCollections.singletonFloatListIterator (47.11f);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals (47.11f, iter.next (), 0d);
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals (47.11f, iter.previous (), 0d);
    try
    {
      iter.set (47.11f);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonFloatIterator ()
  {
    final FloatIterator iter = FloatCollections.singletonFloatIterator (47.11f);
    assertTrue (iter.hasNext ());
    assertEquals (47.11f, iter.next (), 0d);
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

  public void testSingletonFloatList ()
  {
    final FloatList list = FloatCollections.singletonFloatList (47.11f);
    assertEquals (1, list.size ());
    assertEquals (47.11f, list.get (0), 0d);
    try
    {
      list.add (47.11f);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableFloatListNull ()
  {
    try
    {
      FloatCollections.unmodifiableFloatList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyFloatList ()
  {
    assertSame (FloatCollections.EMPTY_FLOAT_LIST, FloatCollections.getEmptyFloatList ());
    assertTrue (FloatCollections.EMPTY_FLOAT_LIST.isEmpty ());
    try
    {
      FloatCollections.EMPTY_FLOAT_LIST.add (47.11f);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableFloatIteratorNull ()
  {
    try
    {
      FloatCollections.unmodifiableFloatIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyFloatIterator ()
  {
    assertSame (FloatCollections.EMPTY_FLOAT_ITERATOR, FloatCollections.getEmptyFloatIterator ());
    assertTrue (!FloatCollections.EMPTY_FLOAT_ITERATOR.hasNext ());
    try
    {
      FloatCollections.EMPTY_FLOAT_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableFloatListIteratorNull ()
  {
    try
    {
      FloatCollections.unmodifiableFloatListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyFloatListIterator ()
  {
    assertSame (FloatCollections.EMPTY_FLOAT_LIST_ITERATOR, FloatCollections.getEmptyFloatListIterator ());
    assertTrue (!FloatCollections.EMPTY_FLOAT_LIST_ITERATOR.hasNext ());
    assertTrue (!FloatCollections.EMPTY_FLOAT_LIST_ITERATOR.hasPrevious ());
    try
    {
      FloatCollections.EMPTY_FLOAT_LIST_ITERATOR.add (47.11f);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
