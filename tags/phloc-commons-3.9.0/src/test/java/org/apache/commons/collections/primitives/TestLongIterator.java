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

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

import org.apache.commons.collections.iterators.AbstractTestIterator;
import org.apache.commons.collections.primitives.adapters.LongIteratorIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestLongIterator extends AbstractTestIterator
{
  public TestLongIterator (final String sTestName)
  {
    super (sTestName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public Iterator <Long> makeEmptyIterator ()
  {
    return LongIteratorIterator.wrap (makeEmptyLongIterator ());
  }

  @Override
  public Iterator <Long> makeFullIterator ()
  {
    return LongIteratorIterator.wrap (makeFullLongIterator ());
  }

  @Nonnull
  protected abstract LongIterator makeEmptyLongIterator ();

  @Nonnull
  protected abstract LongIterator makeFullLongIterator ();

  @Nonnull
  protected abstract long [] getFullElements ();

  // tests
  // ------------------------------------------------------------------------

  public void testNextHasNextRemove ()
  {
    final long [] elements = getFullElements ();
    final LongIterator iter = makeFullLongIterator ();
    for (final long element : elements)
    {
      assertTrue (iter.hasNext ());
      assertEquals (element, iter.next (), 0d);
      if (supportsRemove ())
      {
        iter.remove ();
      }
    }
    assertTrue (!iter.hasNext ());
  }

  public void testEmptyLongIterator ()
  {
    assertTrue (!makeEmptyLongIterator ().hasNext ());
    try
    {
      makeEmptyLongIterator ().next ();
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
        makeEmptyLongIterator ().remove ();
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
        makeFullLongIterator ().remove ();
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
      final LongIterator iter = makeFullLongIterator ();
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
