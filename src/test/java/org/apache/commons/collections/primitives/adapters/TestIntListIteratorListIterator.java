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
package org.apache.commons.collections.primitives.adapters;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.iterators.AbstractTestListIterator;
import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestIntListIteratorListIterator extends AbstractTestListIterator
{
  public TestIntListIteratorListIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestIntListIteratorListIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public ListIterator <Integer> makeEmptyListIterator ()
  {
    return IntListIteratorListIterator.wrap (makeEmptyIntList ().listIterator ());
  }

  @Override
  public ListIterator <Integer> makeFullListIterator ()
  {
    return IntListIteratorListIterator.wrap (makeFullIntList ().listIterator ());
  }

  protected IntList makeEmptyIntList ()
  {
    return new ArrayIntList ();
  }

  protected IntList makeFullIntList ()
  {
    final IntList list = makeEmptyIntList ();
    final int [] elts = getFullElements ();
    for (final int elt : elts)
      list.add (elt);
    return list;
  }

  public int [] getFullElements ()
  {
    final int [] ret = new int [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] = i;
    return ret;
  }

  @Override
  public Object addSetValue ()
  {
    return Integer.valueOf (1);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testNextHasNextRemove ()
  {
    final int [] elements = getFullElements ();
    final Iterator <?> iter = makeFullIterator ();
    for (final int element : elements)
    {
      assertTrue (iter.hasNext ());
      assertEquals (Integer.valueOf (element), iter.next ());
      if (supportsRemove ())
      {
        iter.remove ();
      }
    }
    assertTrue (!iter.hasNext ());
  }

  @Override
  public void testEmptyIterator ()
  {
    assertTrue (!makeEmptyIterator ().hasNext ());
    try
    {
      makeEmptyIterator ().next ();
      fail ("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e)
    {
      // expected
    }
    if (supportsRemove ())
    {
      try
      {
        makeEmptyIterator ().remove ();
        fail ("Expected IllegalStateException");
      }
      catch (final IllegalStateException e)
      {
        // expected
      }
    }
  }

  public void testRemoveBeforeNext ()
  {
    if (supportsRemove ())
    {
      try
      {
        makeFullIterator ().remove ();
        fail ("Expected IllegalStateException");
      }
      catch (final IllegalStateException e)
      {
        // expected
      }
    }
  }

  public void testRemoveAfterRemove ()
  {
    if (supportsRemove ())
    {
      final Iterator <?> iter = makeFullIterator ();
      iter.next ();
      iter.remove ();
      try
      {
        iter.remove ();
        fail ("Expected IllegalStateException");
      }
      catch (final IllegalStateException e)
      {
        // expected
      }
    }
  }
}
