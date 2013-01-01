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
public class TestDoubleCollections extends TestCase
{
  public TestDoubleCollections (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestDoubleCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonDoubleListIterator ()
  {
    final DoubleListIterator iter = DoubleCollections.singletonDoubleListIterator (3.1415);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals (3.1415, iter.next (), 0d);
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals (3.1415, iter.previous (), 0d);
    try
    {
      iter.set (3.1415);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonDoubleIterator ()
  {
    final DoubleIterator iter = DoubleCollections.singletonDoubleIterator (3.1415);
    assertTrue (iter.hasNext ());
    assertEquals (3.1415, iter.next (), 0d);
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

  public void testSingletonDoubleList ()
  {
    final DoubleList list = DoubleCollections.singletonDoubleList (3.1415);
    assertEquals (1, list.size ());
    assertEquals (3.1415, list.get (0), 0d);
    try
    {
      list.add (3.1415);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableDoubleListNull ()
  {
    try
    {
      DoubleCollections.unmodifiableDoubleList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyDoubleList ()
  {
    assertSame (DoubleCollections.EMPTY_DOUBLE_LIST, DoubleCollections.getEmptyDoubleList ());
    assertTrue (DoubleCollections.EMPTY_DOUBLE_LIST.isEmpty ());
    try
    {
      DoubleCollections.EMPTY_DOUBLE_LIST.add (3.1415);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableDoubleIteratorNull ()
  {
    try
    {
      DoubleCollections.unmodifiableDoubleIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyDoubleIterator ()
  {
    assertSame (DoubleCollections.EMPTY_DOUBLE_ITERATOR, DoubleCollections.getEmptyDoubleIterator ());
    assertTrue (!DoubleCollections.EMPTY_DOUBLE_ITERATOR.hasNext ());
    try
    {
      DoubleCollections.EMPTY_DOUBLE_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableDoubleListIteratorNull ()
  {
    try
    {
      DoubleCollections.unmodifiableDoubleListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyDoubleListIterator ()
  {
    assertSame (DoubleCollections.EMPTY_DOUBLE_LIST_ITERATOR, DoubleCollections.getEmptyDoubleListIterator ());
    assertTrue (!DoubleCollections.EMPTY_DOUBLE_LIST_ITERATOR.hasNext ());
    assertTrue (!DoubleCollections.EMPTY_DOUBLE_LIST_ITERATOR.hasPrevious ());
    try
    {
      DoubleCollections.EMPTY_DOUBLE_LIST_ITERATOR.add (3.1415);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
