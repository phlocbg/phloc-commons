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

import javax.annotation.Nonnull;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.TestLongIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestIteratorLongIterator extends TestLongIterator
{
  public TestIteratorLongIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestIteratorLongIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public LongIterator makeEmptyLongIterator ()
  {
    return IteratorLongIterator.wrap (makeEmptyList ().iterator ());
  }

  @Override
  public LongIterator makeFullLongIterator ()
  {
    return IteratorLongIterator.wrap (makeFullList ().iterator ());
  }

  @Nonnull
  public List <Long> makeEmptyList ()
  {
    return new ArrayList <Long> ();
  }

  protected List <Long> makeFullList ()
  {
    final List <Long> list = makeEmptyList ();
    final long [] elts = getFullElements ();
    for (final long elt : elts)
      list.add (Long.valueOf (elt));
    return list;
  }

  @Override
  public long [] getFullElements ()
  {
    final long [] ret = new long [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] = i;
    return ret;
  }
}
