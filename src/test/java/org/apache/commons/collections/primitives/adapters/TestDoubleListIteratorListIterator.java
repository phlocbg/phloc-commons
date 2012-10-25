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
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestDoubleListIteratorListIterator extends AbstractTestListIterator
{
  public TestDoubleListIteratorListIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestDoubleListIteratorListIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public ListIterator <Double> makeEmptyListIterator ()
  {
    return DoubleListIteratorListIterator.wrap (makeEmptyDoubleList ().listIterator ());
  }

  @Override
  public ListIterator <Double> makeFullListIterator ()
  {
    return DoubleListIteratorListIterator.wrap (makeFullDoubleList ().listIterator ());
  }

  protected DoubleList makeEmptyDoubleList ()
  {
    return new ArrayDoubleList ();
  }

  protected DoubleList makeFullDoubleList ()
  {
    final DoubleList list = makeEmptyDoubleList ();
    final double [] elts = getFullElements ();
    for (final double elt : elts)
      list.add (elt);
    return list;
  }

  public double [] getFullElements ()
  {
    final double [] ret = new double [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] = i;
    return ret;
  }

  @Override
  public Object addSetValue ()
  {
    return Double.valueOf (1);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testNextHasNextRemove ()
  {
    final double [] elements = getFullElements ();
    final Iterator <?> iter = makeFullIterator ();
    for (final double element : elements)
    {
      assertTrue (iter.hasNext ());
      assertEquals (Double.valueOf (element), iter.next ());
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
