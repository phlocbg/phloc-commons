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
import org.apache.commons.collections.primitives.adapters.CharIteratorIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestCharIterator extends AbstractTestIterator
{
  public TestCharIterator (final String sTestName)
  {
    super (sTestName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public Iterator <Character> makeEmptyIterator ()
  {
    return CharIteratorIterator.wrap (makeEmptyCharIterator ());
  }

  @Override
  public Iterator <Character> makeFullIterator ()
  {
    return CharIteratorIterator.wrap (makeFullCharIterator ());
  }

  @Nonnull
  protected abstract CharIterator makeEmptyCharIterator ();

  @Nonnull
  protected abstract CharIterator makeFullCharIterator ();

  @Nonnull
  protected abstract char [] getFullElements ();

  // tests
  // ------------------------------------------------------------------------

  public void testNextHasNextRemove ()
  {
    final char [] elements = getFullElements ();
    final CharIterator iter = makeFullCharIterator ();
    for (final char element : elements)
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

  public void testEmptyCharIterator ()
  {
    assertTrue (!makeEmptyCharIterator ().hasNext ());
    try
    {
      makeEmptyCharIterator ().next ();
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
        makeEmptyCharIterator ().remove ();
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
        makeFullCharIterator ().remove ();
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
      final CharIterator iter = makeFullCharIterator ();
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
