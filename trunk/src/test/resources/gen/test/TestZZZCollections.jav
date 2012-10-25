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
public class TestZZZCollections extends TestCase
{
  public TestZZZCollections (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestZZZCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonZZZListIterator ()
  {
    final ZZZListIterator iter = ZZZCollections.singletonZZZListIterator ($DUMMY$);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals ($DUMMY$, iter.next (), 0d);
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals ($DUMMY$, iter.previous (), 0d);
    try
    {
      iter.set ($DUMMY$);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonZZZIterator ()
  {
    final ZZZIterator iter = ZZZCollections.singletonZZZIterator ($DUMMY$);
    assertTrue (iter.hasNext ());
    assertEquals ($DUMMY$, iter.next (), 0d);
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

  public void testSingletonZZZList ()
  {
    final ZZZList list = ZZZCollections.singletonZZZList ($DUMMY$);
    assertEquals (1, list.size ());
    assertEquals ($DUMMY$, list.get (0), 0d);
    try
    {
      list.add ($DUMMY$);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableZZZListNull ()
  {
    try
    {
      ZZZCollections.unmodifiableZZZList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyZZZList ()
  {
    assertSame (ZZZCollections.EMPTY_YYY$UC$_LIST, ZZZCollections.getEmptyZZZList ());
    assertTrue (ZZZCollections.EMPTY_YYY$UC$_LIST.isEmpty ());
    try
    {
      ZZZCollections.EMPTY_YYY$UC$_LIST.add ($DUMMY$);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableZZZIteratorNull ()
  {
    try
    {
      ZZZCollections.unmodifiableZZZIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyZZZIterator ()
  {
    assertSame (ZZZCollections.EMPTY_YYY$UC$_ITERATOR, ZZZCollections.getEmptyZZZIterator ());
    assertTrue (!ZZZCollections.EMPTY_YYY$UC$_ITERATOR.hasNext ());
    try
    {
      ZZZCollections.EMPTY_YYY$UC$_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableZZZListIteratorNull ()
  {
    try
    {
      ZZZCollections.unmodifiableZZZListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyZZZListIterator ()
  {
    assertSame (ZZZCollections.EMPTY_YYY$UC$_LIST_ITERATOR, ZZZCollections.getEmptyZZZListIterator ());
    assertTrue (!ZZZCollections.EMPTY_YYY$UC$_LIST_ITERATOR.hasNext ());
    assertTrue (!ZZZCollections.EMPTY_YYY$UC$_LIST_ITERATOR.hasPrevious ());
    try
    {
      ZZZCollections.EMPTY_YYY$UC$_LIST_ITERATOR.add ($DUMMY$);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
