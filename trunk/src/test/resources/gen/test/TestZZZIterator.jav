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
import org.apache.commons.collections.primitives.adapters.ZZZIteratorIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestZZZIterator extends AbstractTestIterator
{
  public TestZZZIterator (final String sTestName)
  {
    super (sTestName);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public Iterator <XXX> makeEmptyIterator ()
  {
    return ZZZIteratorIterator.wrap (makeEmptyZZZIterator ());
  }

  @Override
  public Iterator <XXX> makeFullIterator ()
  {
    return ZZZIteratorIterator.wrap (makeFullZZZIterator ());
  }

  @Nonnull
  protected abstract ZZZIterator makeEmptyZZZIterator ();

  @Nonnull
  protected abstract ZZZIterator makeFullZZZIterator ();

  @Nonnull
  protected abstract YYY [] getFullElements ();

  // tests
  // ------------------------------------------------------------------------

  public void testNextHasNextRemove ()
  {
    final YYY [] elements = getFullElements ();
    final ZZZIterator iter = makeFullZZZIterator ();
    for (final YYY element : elements)
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

  public void testEmptyZZZIterator ()
  {
    assertTrue (!makeEmptyZZZIterator ().hasNext ());
    try
    {
      makeEmptyZZZIterator ().next ();
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
        makeEmptyZZZIterator ().remove ();
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
        makeFullZZZIterator ().remove ();
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
      final ZZZIterator iter = makeFullZZZIterator ();
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
