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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ByteListIterator;
import org.apache.commons.collections.primitives.TestByteListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestListIteratorByteListIterator extends TestByteListIterator
{
  public TestListIteratorByteListIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestListIteratorByteListIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public ByteListIterator makeEmptyByteListIterator ()
  {
    return ListIteratorByteListIterator.wrap (makeEmptyList ().listIterator ());
  }

  @Override
  public ByteListIterator makeFullByteListIterator ()
  {
    return ListIteratorByteListIterator.wrap (makeFullList ().listIterator ());
  }

  public List <Byte> makeEmptyList ()
  {
    return new ArrayList <Byte> ();
  }

  protected List <Byte> makeFullList ()
  {
    final List <Byte> list = makeEmptyList ();
    final byte [] elts = getFullElements ();
    for (final byte elt : elts)
      list.add (Byte.valueOf (elt));
    return list;
  }

  @Override
  public byte [] getFullElements ()
  {
    final byte [] ret = new byte [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] = (byte) i;
    return ret;
  }
}
