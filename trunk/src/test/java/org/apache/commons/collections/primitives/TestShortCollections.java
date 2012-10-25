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
public class TestShortCollections extends TestCase
{

  // ------------------------------------------------------------ Conventional

  public TestShortCollections (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestShortCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonShortListIterator ()
  {
    final ShortListIterator iter = ShortCollections.singletonShortListIterator ((short) 17);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals (17, iter.next ());
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals (17, iter.previous ());
    try
    {
      iter.set ((short) 18);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonShortIterator ()
  {
    final ShortIterator iter = ShortCollections.singletonShortIterator ((short) 17);
    assertTrue (iter.hasNext ());
    assertEquals (17, iter.next ());
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

  public void testSingletonShortList ()
  {
    final ShortList list = ShortCollections.singletonShortList ((short) 17);
    assertEquals (1, list.size ());
    assertEquals (17, list.get (0));
    try
    {
      list.add ((short) 18);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableShortListNull ()
  {
    try
    {
      ShortCollections.unmodifiableShortList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyShortList ()
  {
    assertSame (ShortCollections.EMPTY_SHORT_LIST, ShortCollections.getEmptyShortList ());
    assertTrue (ShortCollections.EMPTY_SHORT_LIST.isEmpty ());
    try
    {
      ShortCollections.EMPTY_SHORT_LIST.add ((short) 1);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableShortIteratorNull ()
  {
    try
    {
      ShortCollections.unmodifiableShortIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyShortIterator ()
  {
    assertSame (ShortCollections.EMPTY_SHORT_ITERATOR, ShortCollections.getEmptyShortIterator ());
    assertTrue (!ShortCollections.EMPTY_SHORT_ITERATOR.hasNext ());
    try
    {
      ShortCollections.EMPTY_SHORT_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableShortListIteratorNull ()
  {
    try
    {
      ShortCollections.unmodifiableShortListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyShortListIterator ()
  {
    assertSame (ShortCollections.EMPTY_SHORT_LIST_ITERATOR, ShortCollections.getEmptyShortListIterator ());
    assertTrue (!ShortCollections.EMPTY_SHORT_LIST_ITERATOR.hasNext ());
    assertTrue (!ShortCollections.EMPTY_SHORT_LIST_ITERATOR.hasPrevious ());
    try
    {
      ShortCollections.EMPTY_SHORT_LIST_ITERATOR.add ((short) 1);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
