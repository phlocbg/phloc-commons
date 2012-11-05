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
public class TestLongCollections extends TestCase
{
  public TestLongCollections (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestLongCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonLongListIterator ()
  {
    final LongListIterator iter = LongCollections.singletonLongListIterator (424242L);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals (424242L, iter.next (), 0d);
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals (424242L, iter.previous (), 0d);
    try
    {
      iter.set (424242L);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonLongIterator ()
  {
    final LongIterator iter = LongCollections.singletonLongIterator (424242L);
    assertTrue (iter.hasNext ());
    assertEquals (424242L, iter.next (), 0d);
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

  public void testSingletonLongList ()
  {
    final LongList list = LongCollections.singletonLongList (424242L);
    assertEquals (1, list.size ());
    assertEquals (424242L, list.get (0), 0d);
    try
    {
      list.add (424242L);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableLongListNull ()
  {
    try
    {
      LongCollections.unmodifiableLongList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyLongList ()
  {
    assertSame (LongCollections.EMPTY_LONG_LIST, LongCollections.getEmptyLongList ());
    assertTrue (LongCollections.EMPTY_LONG_LIST.isEmpty ());
    try
    {
      LongCollections.EMPTY_LONG_LIST.add (424242L);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableLongIteratorNull ()
  {
    try
    {
      LongCollections.unmodifiableLongIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyLongIterator ()
  {
    assertSame (LongCollections.EMPTY_LONG_ITERATOR, LongCollections.getEmptyLongIterator ());
    assertTrue (!LongCollections.EMPTY_LONG_ITERATOR.hasNext ());
    try
    {
      LongCollections.EMPTY_LONG_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableLongListIteratorNull ()
  {
    try
    {
      LongCollections.unmodifiableLongListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyLongListIterator ()
  {
    assertSame (LongCollections.EMPTY_LONG_LIST_ITERATOR, LongCollections.getEmptyLongListIterator ());
    assertTrue (!LongCollections.EMPTY_LONG_LIST_ITERATOR.hasNext ());
    assertTrue (!LongCollections.EMPTY_LONG_LIST_ITERATOR.hasPrevious ());
    try
    {
      LongCollections.EMPTY_LONG_LIST_ITERATOR.add (424242L);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
