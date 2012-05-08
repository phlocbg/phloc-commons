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

import com.phloc.commons.collections.ContainerHelper;

/**
 * Test class for class {@link CombinedIterator}.
 * 
 * @author philip
 */
public final class CombinedIteratorTest
{
  @Test
  public void testAll ()
  {
    // both null
    CombinedIterator <String> es = CombinedIterator.create (null, null);
    assertFalse (es.hasNext ());
    try
    {
      es.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    // both empty
    es = CombinedIterator.create (EmptyIterator.<String> getInstance (), EmptyIterator.<String> getInstance ());
    assertFalse (es.hasNext ());
    try
    {
      es.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    // one null
    es = CombinedIterator.create (ContainerHelper.getIterator ("a", "b", "c"), null);
    assertTrue (es.hasNext ());
    assertEquals ("a", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("b", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("c", es.next ());
    assertFalse (es.hasNext ());
    try
    {
      es.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    // other one null
    es = CombinedIterator.create (null, ContainerHelper.getIterator ("a", "b", "c"));
    assertTrue (es.hasNext ());
    assertEquals ("a", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("b", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("c", es.next ());
    assertFalse (es.hasNext ());
    try
    {
      es.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    // both not null
    es = CombinedIterator.create (ContainerHelper.getIterator ("a", "b"), ContainerHelper.getIterator ("c", "d"));
    assertTrue (es.hasNext ());
    assertEquals ("a", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("b", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("c", es.next ());
    assertTrue (es.hasNext ());
    assertEquals ("d", es.next ());
    assertFalse (es.hasNext ());
    try
    {
      es.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}
  }
}
