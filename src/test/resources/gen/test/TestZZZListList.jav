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

import java.io.Serializable;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ArrayZZZList;
import org.apache.commons.collections.primitives.RandomAccessZZZList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestZZZListList extends BaseTestList <XXX>
{
  public TestZZZListList (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    final TestSuite suite = new TestSuite (TestZZZListList.class);
    return suite;
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public List <XXX> makeEmptyList ()
  {
    return new ZZZListList (new ArrayZZZList ());
  }

  @Override
  public XXX [] getFullElements ()
  {
    final XXX [] elts = new XXX [10];
    for (int i = 0; i < elts.length; i++)
    {
      elts[i] = XXX.valueOf ($CASTINT$i);
    }
    return elts;
  }

  @Override
  public XXX [] getOtherElements ()
  {
    final XXX [] elts = new XXX [10];
    for (int i = 0; i < elts.length; i++)
    {
      elts[i] = XXX.valueOf ($CASTINT$(10 + i));
    }
    return elts;
  }

  public void testWrapNull ()
  {
    assertNull (ZZZListList.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final List <XXX> list = ZZZListList.wrap (new ArrayZZZList ());
    assertNotNull (list);
    assertTrue (list instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final List <XXX> list = ZZZListList.wrap (new RandomAccessZZZList ()
    {
      @Override
      public YYY get (final int i)
      {
        throw new IndexOutOfBoundsException ();
      }

      @Override
      public int size ()
      {
        return 0;
      }
    });
    assertNotNull (list);
    assertTrue (!(list instanceof Serializable));
  }
}
