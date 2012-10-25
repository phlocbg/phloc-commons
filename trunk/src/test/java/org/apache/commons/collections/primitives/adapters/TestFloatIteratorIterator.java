/**
 * Copyright (C) 2006-2012 phloc systems
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
import org.apache.commons.collections.primitives.ArrayFloatList;
import org.apache.commons.collections.primitives.FloatList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestFloatIteratorIterator extends AbstractTestIterator
{
  public TestFloatIteratorIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestFloatIteratorIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  @Nonnull
  public Iterator <Float> makeEmptyIterator ()
  {
    return FloatIteratorIterator.wrap (makeEmptyFloatList ().iterator ());
  }

  @Override
  @Nonnull
  public Iterator <Float> makeFullIterator ()
  {
    return FloatIteratorIterator.wrap (makeFullFloatList ().iterator ());
  }

  @Nonnull
  protected FloatList makeEmptyFloatList ()
  {
    return new ArrayFloatList ();
  }

  @Nonnull
  protected FloatList makeFullFloatList ()
  {
    final FloatList list = makeEmptyFloatList ();
    final float [] elts = getFullElements ();
    for (final float elt : elts)
      list.add (elt);
    return list;
  }

  @Nonnull
  public float [] getFullElements ()
  {
    final float [] ret = new float [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] =  i;
    return ret;
  }
}
