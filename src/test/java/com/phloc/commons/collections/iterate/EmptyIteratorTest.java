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
package com.phloc.commons.collections.iterate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Test class for class {@link EmptyIterator}
 * 
 * @author philip
 */
public final class EmptyIteratorTest
{
  @Test
  public void testAll ()
  {
    final EmptyIterator <String> eit = EmptyIterator.getInstance ();
    assertFalse (eit.hasNext ());

    try
    {
      eit.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}
    try
    {
      eit.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}

    assertTrue (eit.equals (EmptyIterator.getInstance ()));
    assertFalse (eit.equals ("sthg else"));
    assertTrue (eit.hashCode () == EmptyIterator.getInstance ().hashCode ());
    assertTrue (eit.toString ().equals (EmptyIterator.getInstance ().toString ()));
  }
}
