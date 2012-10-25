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
public class TestCharCollections extends TestCase
{
  public TestCharCollections (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestCharCollections.class);
  }

  // ---------------------------------------------------------------- Tests

  public void testSingletonCharListIterator ()
  {
    final CharListIterator iter = CharCollections.singletonCharListIterator ((char) 17);
    assertTrue (!iter.hasPrevious ());
    assertTrue (iter.hasNext ());
    assertEquals (17, iter.next ());
    assertTrue (iter.hasPrevious ());
    assertTrue (!iter.hasNext ());
    assertEquals (17, iter.previous ());
    try
    {
      iter.set ((char) 18);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSingletonCharIterator ()
  {
    final CharIterator iter = CharCollections.singletonCharIterator ((char) 17);
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

  public void testSingletonCharList ()
  {
    final CharList list = CharCollections.singletonCharList ((char) 17);
    assertEquals (1, list.size ());
    assertEquals (17, list.get (0));
    try
    {
      list.add ((char) 18);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableCharListNull ()
  {
    try
    {
      CharCollections.unmodifiableCharList (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyCharList ()
  {
    assertSame (CharCollections.EMPTY_CHAR_LIST, CharCollections.getEmptyCharList ());
    assertTrue (CharCollections.EMPTY_CHAR_LIST.isEmpty ());
    try
    {
      CharCollections.EMPTY_CHAR_LIST.add ((char) 1);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableCharIteratorNull ()
  {
    try
    {
      CharCollections.unmodifiableCharIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyCharIterator ()
  {
    assertSame (CharCollections.EMPTY_CHAR_ITERATOR, CharCollections.getEmptyCharIterator ());
    assertTrue (!CharCollections.EMPTY_CHAR_ITERATOR.hasNext ());
    try
    {
      CharCollections.EMPTY_CHAR_ITERATOR.remove ();
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testUnmodifiableCharListIteratorNull ()
  {
    try
    {
      CharCollections.unmodifiableCharListIterator (null);
      fail ("Expected NullPointerException");
    }
    catch (final NullPointerException e)
    {
      // expected
    }
  }

  public void testEmptyCharListIterator ()
  {
    assertSame (CharCollections.EMPTY_CHAR_LIST_ITERATOR, CharCollections.getEmptyCharListIterator ());
    assertTrue (!CharCollections.EMPTY_CHAR_LIST_ITERATOR.hasNext ());
    assertTrue (!CharCollections.EMPTY_CHAR_LIST_ITERATOR.hasPrevious ());
    try
    {
      CharCollections.EMPTY_CHAR_LIST_ITERATOR.add ((char) 1);
      fail ("Expected UnsupportedOperationExcpetion");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }
}
