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

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.iterators.AbstractTestIterator;
import org.apache.commons.collections.primitives.ArrayFloatList;
import org.apache.commons.collections.primitives.FloatList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestFloatIteratorIterator extends AbstractTestIterator
{

  // conventional
  // ------------------------------------------------------------------------

  public TestFloatIteratorIterator (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestFloatIteratorIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public Iterator makeEmptyIterator ()
  {
    return FloatIteratorIterator.wrap (makeEmptyFloatList ().iterator ());
  }

  @Override
  public Iterator makeFullIterator ()
  {
    return FloatIteratorIterator.wrap (makeFullFloatList ().iterator ());
  }

  protected FloatList makeEmptyFloatList ()
  {
    return new ArrayFloatList ();
  }

  protected FloatList makeFullFloatList ()
  {
    final FloatList list = makeEmptyFloatList ();
    final float [] elts = getFullElements ();
    for (final float elt : elts)
    {
      list.add (elt);
    }
    return list;
  }

  public float [] getFullElements ()
  {
    return new float [] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  }

  // tests
  // ------------------------------------------------------------------------

}
