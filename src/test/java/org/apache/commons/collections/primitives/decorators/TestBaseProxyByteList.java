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

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestBaseProxyByteList extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public TestBaseProxyByteList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestBaseProxyByteList.class);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testListCallsAreProxied ()
  {
    final InvocationCounter proxied = new InvocationCounter ();
    final BaseProxyByteList list = new BaseProxyByteList ()
    {
      @Override
      protected ByteList getProxiedList ()
      {
        return proxied;
      }
    };

    assertSame (list.getProxiedList (), list.getProxiedCollection ());

    assertEquals (0, proxied.getAddCount ());
    list.add (1, (byte) 1);
    assertEquals (1, proxied.getAddCount ());

    assertEquals (0, proxied.getAddAllCount ());
    list.addAll (1, null);
    assertEquals (1, proxied.getAddAllCount ());

    assertEquals (0, proxied.getGetCount ());
    list.get (1);
    assertEquals (1, proxied.getGetCount ());

    assertEquals (0, proxied.getIndexOfCount ());
    list.indexOf ((byte) 1);
    assertEquals (1, proxied.getIndexOfCount ());

    assertEquals (0, proxied.getLastIndexOfCount ());
    list.lastIndexOf ((byte) 1);
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
    list.set (1, (byte) 1);
    assertEquals (1, proxied.getSetCount ());

    assertEquals (0, proxied.getSubListCount ());
    list.subList (1, 2);
    assertEquals (1, proxied.getSubListCount ());
  }

  // inner classes
  // ------------------------------------------------------------------------

  static class InvocationCounter extends TestBaseProxyByteCollection.InvocationCounter implements ByteList
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

    public void add (final int index, final byte element)
    {
      addCount++;
    }

    public boolean addAll (final int index, final ByteCollection collection)
    {
      addAllCount++;
      return false;
    }

    public byte get (final int index)
    {
      getCount++;
      return 0;
    }

    public int indexOf (final byte element)
    {
      indexOfCount++;
      return 0;
    }

    public int lastIndexOf (final byte element)
    {
      lastIndexOfCount++;
      return 0;
    }

    public ByteListIterator listIterator ()
    {
      listIteratorCount++;
      return null;
    }

    public ByteListIterator listIterator (final int index)
    {
      listIteratorFromCount++;
      return null;
    }

    public byte removeElementAt (final int index)
    {
      removeElementAtCount++;
      return 0;
    }

    public byte set (final int index, final byte element)
    {
      setCount++;
      return 0;
    }

    public ByteList subList (final int fromIndex, final int toIndex)
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
