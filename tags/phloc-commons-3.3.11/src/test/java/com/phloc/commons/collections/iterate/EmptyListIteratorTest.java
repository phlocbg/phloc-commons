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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Test class for class {@link EmptyListIterator}
 * 
 * @author philip
 */
public final class EmptyListIteratorTest
{
  @Test
  public void testAll ()
  {
    final EmptyListIterator <String> eit = EmptyListIterator.getInstance ();
    assertFalse (eit.hasNext ());
    assertFalse (eit.hasPrevious ());
    assertEquals (-1, eit.previousIndex ());
    assertEquals (0, eit.nextIndex ());

    try
    {
      eit.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    try
    {
      eit.previous ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    try
    {
      eit.add ("any");
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}

    try
    {
      eit.set ("any");
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}

    try
    {
      eit.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}

    assertTrue (eit.equals (EmptyListIterator.getInstance ()));
    assertFalse (eit.equals ("other thinggy"));
    assertTrue (eit.hashCode () == EmptyListIterator.getInstance ().hashCode ());
    assertTrue (eit.toString ().equals (EmptyListIterator.getInstance ().toString ()));
  }
}
