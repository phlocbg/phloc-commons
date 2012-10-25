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
package org.apache.commons.collections.primitives.decorators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.DoubleCollection;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestBaseProxyDoubleList extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public TestBaseProxyDoubleList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestBaseProxyDoubleList.class);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testListCallsAreProxied ()
  {
    final InvocationCounter proxied = new InvocationCounter ();
    final BaseProxyDoubleList list = new BaseProxyDoubleList ()
    {
      @Override
      protected DoubleList getProxiedList ()
      {
        return proxied;
      }
    };

    assertSame (list.getProxiedList (), list.getProxiedCollection ());

    assertEquals (0, proxied.getAddCount ());
    list.add (1, 1);
    assertEquals (1, proxied.getAddCount ());

    assertEquals (0, proxied.getAddAllCount ());
    list.addAll (1, null);
    assertEquals (1, proxied.getAddAllCount ());

    assertEquals (0, proxied.getGetCount ());
    list.get (1);
    assertEquals (1, proxied.getGetCount ());

    assertEquals (0, proxied.getIndexOfCount ());
    list.indexOf (1);
    assertEquals (1, proxied.getIndexOfCount ());

    assertEquals (0, proxied.getLastIndexOfCount ());
    list.lastIndexOf (1);
    assertEquals (1, proxied.getLastIndexOfCount ());

    assertEquals (0, proxied.getListIteratorCount ());
    list.listIterator ();
    assertEquals (1, proxied.getListIteratorCount ());

    assertEquals (0, proxied.getListIteratorFromCount ());
    list.listIterator (1);
    assertEquals (1, proxied.getListIteratorFromCount ());

    assertEquals (0, proxied.getRemoveElementAtCount ());
    list.removeElementAt (1);
    assertEquals (1, proxied.getRemoveElementAtCount ());

    assertEquals (0, proxied.getSetCount ());
    list.set (1, 1);
    assertEquals (1, proxied.getSetCount ());

    assertEquals (0, proxied.getSubListCount ());
    list.subList (1, 2);
    assertEquals (1, proxied.getSubListCount ());
  }

  // inner classes
  // ------------------------------------------------------------------------

  static class InvocationCounter extends TestBaseProxyDoubleCollection.InvocationCounter implements DoubleList
  {
    private int addCount;
    private int addAllCount;
    private int getCount;
    private int indexOfCount;
    private int lastIndexOfCount;
    private int listIteratorCount;
    private int listIteratorFromCount;
    private int removeElementAtCount;
    private int setCount;
    private int subListCount;

    public void add (final int index, final double element)
    {
      addCount++;
    }

    public boolean addAll (final int index, final DoubleCollection collection)
    {
      addAllCount++;
      return false;
    }

    public double get (final int index)
    {
      getCount++;
      return 0;
    }

    public int indexOf (final double element)
    {
      indexOfCount++;
      return 0;
    }

    public int lastIndexOf (final double element)
    {
      lastIndexOfCount++;
      return 0;
    }

    public DoubleListIterator listIterator ()
    {
      listIteratorCount++;
      return null;
    }

    public DoubleListIterator listIterator (final int index)
    {
      listIteratorFromCount++;
      return null;
    }

    public double removeElementAt (final int index)
    {
      removeElementAtCount++;
      return 0;
    }

    public double set (final int index, final double element)
    {
      setCount++;
      return 0;
    }

    public DoubleList subList (final int fromIndex, final int toIndex)
    {
      subListCount++;
      return null;
    }

    @Override
    public int getAddAllCount ()
    {
      return addAllCount;
    }

    @Override
    public int getAddCount ()
    {
      return addCount;
    }

    public int getGetCount ()
    {
      return getCount;
    }

    public int getIndexOfCount ()
    {
      return indexOfCount;
    }

    public int getLastIndexOfCount ()
    {
      return lastIndexOfCount;
    }

    public int getListIteratorCount ()
    {
      return listIteratorCount;
    }

    public int getListIteratorFromCount ()
    {
      return listIteratorFromCount;
    }

    public int getRemoveElementAtCount ()
    {
      return removeElementAtCount;
    }

    public int getSetCount ()
    {
      return setCount;
    }

    public int getSubListCount ()
    {
      return subListCount;
    }

  }
}
