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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import junit.framework.TestCase;

import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public abstract class BaseTestList <T> extends TestCase
{
  private List <T> m_aList;

  public BaseTestList (final String testName)
  {
    super (testName);
  }

  public abstract List <T> makeEmptyList ();

  public List <T> makeFullList ()
  {
    final List <T> list = makeEmptyList ();
    final T [] elts = getFullElements ();
    for (final T elt : elts)
      list.add (elt);
    return list;
  }

  public List <T> getList ()
  {
    if (m_aList == null)
      m_aList = makeFullList ();
    return m_aList;
  }

  public abstract T [] getFullElements ();

  public abstract T [] getOtherElements ();

  public boolean isAddSupported ()
  {
    return true;
  }

  public boolean isSetSupported ()
  {
    return true;
  }

  public boolean isRemoveSupported ()
  {
    return true;
  }

  public void resetFull ()
  {
    m_aList = null;
  }

  // tests
  // ------------------------------------------------------------------------

  @SuppressWarnings ("unchecked")
  public final void testAddAllAtIndex ()
  {
    final List <T> source = makeFullList ();
    final List <T> dest = makeFullList ();

    dest.addAll (1, source);

    final Iterator <T> iter = dest.iterator ();
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
    final ListIterator <?> it = getList ().listIterator ();
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

  protected final byte [] writeExternalFormToBytes (final Serializable x)
  {
    try
    {
      final NonBlockingByteArrayOutputStream aOS = new NonBlockingByteArrayOutputStream ();
      final ObjectOutputStream aOOS = new ObjectOutputStream (aOS);
      aOOS.writeObject (x);
      aOOS.close ();
      return aOS.toByteArray ();
    }
    catch (final IOException ex)
    {
      throw new RuntimeException (ex);
    }
  }

  protected final Object readExternalFormFromBytes (final byte [] ser)
  {
    try
    {
      final ObjectInputStream aOIS = new ObjectInputStream (new NonBlockingByteArrayInputStream (ser));
      final Object ret = aOIS.readObject ();
      aOIS.close ();
      return ret;
    }
    catch (final Exception ex)
    {
      throw new RuntimeException (ex);
    }
  }
}
