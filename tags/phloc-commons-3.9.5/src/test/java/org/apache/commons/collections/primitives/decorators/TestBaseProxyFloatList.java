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
package org.apache.commons.collections.primitives.decorators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatList;
import org.apache.commons.collections.primitives.FloatListIterator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
@SuppressFBWarnings ({ "NP_NONNULL_PARAM_VIOLATION", "NP_NONNULL_RETURN_VIOLATION", "NP_TOSTRING_COULD_RETURN_NULL" })
public class TestBaseProxyFloatList extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public TestBaseProxyFloatList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestBaseProxyFloatList.class);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testListCallsAreProxied ()
  {
    final InvocationCounter proxied = new InvocationCounter ();
    final BaseProxyFloatList list = new BaseProxyFloatList ()
    {
      @Override
      protected FloatList getProxiedList ()
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

  static class InvocationCounter extends TestBaseProxyFloatCollection.InvocationCounter implements FloatList
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

    public void add (final int index, final float element)
    {
      addCount++;
    }

    public boolean addAll (final int index, final FloatCollection collection)
    {
      addAllCount++;
      return false;
    }

    public float get (final int index)
    {
      getCount++;
      return 0;
    }

    public int indexOf (final float element)
    {
      indexOfCount++;
      return 0;
    }

    public int lastIndexOf (final float element)
    {
      lastIndexOfCount++;
      return 0;
    }

    public FloatListIterator listIterator ()
    {
      listIteratorCount++;
      return null;
    }

    public FloatListIterator listIterator (final int index)
    {
      listIteratorFromCount++;
      return null;
    }

    public float removeElementAt (final int index)
    {
      removeElementAtCount++;
      return 0;
    }

    public float set (final int index, final float element)
    {
      setCount++;
      return 0;
    }

    public FloatList subList (final int fromIndex, final int toIndex)
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