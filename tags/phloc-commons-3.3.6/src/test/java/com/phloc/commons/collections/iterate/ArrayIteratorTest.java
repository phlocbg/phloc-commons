/**
 * Copyright (C) 2006-2011 phloc systems
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

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ArrayIterator}.
 * 
 * @author philip
 */
public final class ArrayIteratorTest
{
  @Test
  public void testAll ()
  {
    final ArrayIterator <String> ae = ArrayIterator.create (ArrayHelper.newArray ("Hallo", "Welt", "from", "Copenhagen"));
    for (int i = 0; i < 10; ++i)
      assertTrue (ae.hasNext ());
    assertEquals ("Hallo", ae.next ());
    for (int i = 0; i < 10; ++i)
      assertTrue (ae.hasNext ());
    assertEquals ("Welt", ae.next ());
    for (int i = 0; i < 10; ++i)
      assertTrue (ae.hasNext ());
    assertEquals ("from", ae.next ());
    for (int i = 0; i < 10; ++i)
      assertTrue (ae.hasNext ());
    assertEquals ("Copenhagen", ae.next ());
    for (int i = 0; i < 10; ++i)
      assertFalse (ae.hasNext ());
    try
    {
      ae.remove ();
      fail ();
    }
    catch (final UnsupportedOperationException ex)
    {}

    try
    {
      ae.next ();
      fail ();
    }
    catch (final NoSuchElementException ex)
    {}

    try
    {
      new ArrayIterator <String> ((String []) null);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new ArrayIterator <String> (null, 5, 2);
      fail ();
    }
    catch (final NullPointerException ex)
    {}

    try
    {
      new ArrayIterator <String> (new String [] { "x", "y" }, -1, 5);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}

    try
    {
      new ArrayIterator <String> (new String [] { "x", "y" }, 5, 2);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testStdMethods ()
  {
    final ArrayIterator <String> ae = ArrayIterator.create (ArrayHelper.newArray ("Hallo", "Welt", "from", "Copenhagen"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (ae,
                                                                    ArrayIterator.create (ArrayHelper.newArray ("Hallo",
                                                                                                                "Welt",
                                                                                                                "from",
                                                                                                                "Copenhagen")));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (ae,
                                                                        ArrayIterator.create (ArrayHelper.newArray ("Hallo",
                                                                                                                    "Welt",
                                                                                                                    "from")));
    ae.next ();
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (ae,
                                                                        ArrayIterator.create (ArrayHelper.newArray ("Hallo",
                                                                                                                    "Welt",
                                                                                                                    "from",
                                                                                                                    "Copenhagen")));
  }
}
