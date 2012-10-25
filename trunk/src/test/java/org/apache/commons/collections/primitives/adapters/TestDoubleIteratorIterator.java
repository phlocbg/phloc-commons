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

import javax.annotation.Nonnull;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.iterators.AbstractTestIterator;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestDoubleIteratorIterator extends AbstractTestIterator
{
  public TestDoubleIteratorIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestDoubleIteratorIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  @Nonnull
  public Iterator <Double> makeEmptyIterator ()
  {
    return DoubleIteratorIterator.wrap (makeEmptyDoubleList ().iterator ());
  }

  @Override
  @Nonnull
  public Iterator <Double> makeFullIterator ()
  {
    return DoubleIteratorIterator.wrap (makeFullDoubleList ().iterator ());
  }

  @Nonnull
  protected DoubleList makeEmptyDoubleList ()
  {
    return new ArrayDoubleList ();
  }

  @Nonnull
  protected DoubleList makeFullDoubleList ()
  {
    final DoubleList list = makeEmptyDoubleList ();
    final double [] elts = getFullElements ();
    for (final double elt : elts)
      list.add (elt);
    return list;
  }

  @Nonnull
  public double [] getFullElements ()
  {
    final double [] ret = new double [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] =  i;
    return ret;
  }
}
