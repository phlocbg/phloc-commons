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

import org.apache.commons.collections.primitives.ShortIterator;
import org.apache.commons.collections.primitives.TestShortIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestIteratorShortIterator extends TestShortIterator
{

  // conventional
  // ------------------------------------------------------------------------

  public TestIteratorShortIterator (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestIteratorShortIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public ShortIterator makeEmptyShortIterator ()
  {
    return IteratorShortIterator.wrap (makeEmptyList ().iterator ());
  }

  @Override
  public ShortIterator makeFullShortIterator ()
  {
    return IteratorShortIterator.wrap (makeFullList ().iterator ());
  }

  public List makeEmptyList ()
  {
    return new ArrayList ();
  }

  protected List makeFullList ()
  {
    final List list = makeEmptyList ();
    final short [] elts = getFullElements ();
    for (final short elt : elts)
    {
      list.add (new Short (elt));
    }
    return list;
  }

  @Override
  public short [] getFullElements ()
  {
    return new short [] { (short) 0,
                         (short) 1,
                         (short) 2,
                         (short) 3,
                         (short) 4,
                         (short) 5,
                         (short) 6,
                         (short) 7,
                         (short) 8,
                         (short) 9 };
  }

  // tests
  // ------------------------------------------------------------------------

}
