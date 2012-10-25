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

import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.TestByteIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestIteratorByteIterator extends TestByteIterator
{

  // conventional
  // ------------------------------------------------------------------------

  public TestIteratorByteIterator (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestIteratorByteIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public ByteIterator makeEmptyByteIterator ()
  {
    return IteratorByteIterator.wrap (makeEmptyList ().iterator ());
  }

  @Override
  public ByteIterator makeFullByteIterator ()
  {
    return IteratorByteIterator.wrap (makeFullList ().iterator ());
  }

  public List makeEmptyList ()
  {
    return new ArrayList ();
  }

  protected List makeFullList ()
  {
    final List list = makeEmptyList ();
    final byte [] elts = getFullElements ();
    for (final byte elt : elts)
    {
      list.add (new Byte (elt));
    }
    return list;
  }

  @Override
  public byte [] getFullElements ()
  {
    return new byte [] { (byte) 0,
                        (byte) 1,
                        (byte) 2,
                        (byte) 3,
                        (byte) 4,
                        (byte) 5,
                        (byte) 6,
                        (byte) 7,
                        (byte) 8,
                        (byte) 9 };
  }

  // tests
  // ------------------------------------------------------------------------

}
