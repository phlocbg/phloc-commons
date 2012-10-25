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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.list.AbstractTestList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseTestList extends AbstractTestList
{

  // conventional
  // ------------------------------------------------------------------------

  public BaseTestList (final String testName)
  {
    super (testName);
  }

  // tests
  // ------------------------------------------------------------------------

  public final void testAddAllAtIndex ()
  {
    final List source = makeFullList ();
    final List dest = makeFullList ();

    dest.addAll (1, source);

    final Iterator iter = dest.iterator ();
    assertTrue (iter.hasNext ());
    assertEquals (source.get (0), iter.next ());
    for (int i = 0; i < source.size (); i++)
    {
      assertTrue (iter.hasNext ());
      assertEquals (source.get (i), iter.next ());
    }
    for (int i = 1; i < source.size (); i++)
    {
      assertTrue (iter.hasNext ());
      assertEquals (source.get (i), iter.next ());
    }
    assertFalse (iter.hasNext ());
  }

  /**
   * Override to change assertSame to assertEquals.
   */
  public void testListListIteratorPreviousRemove ()
  {
    if (isRemoveSupported () == false)
      return;
    resetFull ();
    final ListIterator it = getList ().listIterator ();
    final Object zero = it.next ();
    final Object one = it.next ();
    final Object two = it.next ();
    final Object two2 = it.previous ();
    final Object one2 = it.previous ();
    assertEquals (one, one2);
    assertEquals (two, two2);
    assertEquals (zero, getList ().get (0));
    assertEquals (one, getList ().get (1));
    assertEquals (two, getList ().get (2));
    it.remove ();
    assertEquals (zero, getList ().get (0));
    assertEquals (two, getList ().get (1));
  }

  /**
   * Override to change assertSame to assertEquals.
   */
  @Override
  public BulkTest bulkTestSubList ()
  {
    if (getFullElements ().length - 6 < 10)
      return null;
    return new PrimitiveBulkTestSubList (this);
  }

  /**
   * Whole class copied as sub list constructor was package scoped in 3.1.
   */
  public static class PrimitiveBulkTestSubList extends BaseTestList
  {
    private final BaseTestList outer;

    PrimitiveBulkTestSubList (final BaseTestList outer)
    {
      super ("");
      this.outer = outer;
    }

    @Override
    public Object [] getFullElements ()
    {
      final List l = Arrays.asList (outer.getFullElements ());
      return l.subList (3, l.size () - 3).toArray ();
    }

    @Override
    public Object [] getOtherElements ()
    {
      return outer.getOtherElements ();
    }

    @Override
    public boolean isAddSupported ()
    {
      return outer.isAddSupported ();
    }

    @Override
    public boolean isSetSupported ()
    {
      return outer.isSetSupported ();
    }

    @Override
    public boolean isRemoveSupported ()
    {
      return outer.isRemoveSupported ();
    }

    @Override
    public List makeEmptyList ()
    {
      return outer.makeFullList ().subList (4, 4);
    }

    @Override
    public List makeFullList ()
    {
      final int size = getFullElements ().length;
      return outer.makeFullList ().subList (3, size - 3);
    }

    @Override
    public void resetEmpty ()
    {
      outer.resetFull ();
      this.collection = outer.getList ().subList (4, 4);
      this.confirmed = outer.getConfirmedList ().subList (4, 4);
    }

    @Override
    public void resetFull ()
    {
      outer.resetFull ();
      final int size = outer.confirmed.size ();
      this.collection = outer.getList ().subList (3, size - 3);
      this.confirmed = outer.getConfirmedList ().subList (3, size - 3);
    }

    @Override
    public void verify ()
    {
      super.verify ();
      outer.verify ();
    }

    @Override
    public boolean isTestSerialization ()
    {
      return false;
    }

    /**
     * Override to change assertSame to assertEquals.
     */
    @Override
    public void testListListIteratorPreviousRemove ()
    {
      if (isRemoveSupported () == false)
        return;
      resetFull ();
      final ListIterator it = getList ().listIterator ();
      final Object zero = it.next ();
      final Object one = it.next ();
      final Object two = it.next ();
      final Object two2 = it.previous ();
      final Object one2 = it.previous ();
      assertEquals (one, one2);
      assertEquals (two, two2);
      assertEquals (zero, getList ().get (0));
      assertEquals (one, getList ().get (1));
      assertEquals (two, getList ().get (2));
      it.remove ();
      assertEquals (zero, getList ().get (0));
      assertEquals (two, getList ().get (1));
    }
  }
}
