/**
 * Copyright (C) 2006-2013 phloc systems
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
package org.apache.commons.collections.primitives;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestRandomAccessLongList extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public TestRandomAccessLongList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestRandomAccessLongList.class);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testAddIsUnsupportedByDefault ()
  {
    final RandomAccessLongList list = new AbstractRandomAccessLongListImpl ();
    try
    {
      list.add (1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
    try
    {
      list.set (0, 1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testAddAllIsUnsupportedByDefault ()
  {
    final RandomAccessLongList list = new AbstractRandomAccessLongListImpl ();
    final LongList list2 = new ArrayLongList ();
    list2.add (3);
    try
    {
      list.addAll (list2);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testSetIsUnsupportedByDefault ()
  {
    final RandomAccessLongList list = new AbstractRandomAccessLongListImpl ();
    try
    {
      list.set (0, 1);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  public void testRemoveElementIsUnsupportedByDefault ()
  {
    final RandomAccessLongList list = new AbstractRandomAccessLongListImpl ();
    try
    {
      list.removeElementAt (0);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  // inner classes
  // ------------------------------------------------------------------------

  static class AbstractRandomAccessLongListImpl extends RandomAccessLongList
  {
    public AbstractRandomAccessLongListImpl ()
    {}

    /**
     * @see org.apache.commons.collections.primitives.LongList#get(int)
     */
    @Override
    public long get (final int index)
    {
      throw new IndexOutOfBoundsException ();
    }

    /**
     * @see org.apache.commons.collections.primitives.LongCollection#size()
     */
    @Override
    public int size ()
    {
      return 0;
    }

  }
}
