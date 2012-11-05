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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

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
  public TestIteratorShortIterator (final String sTestName)
  {
    super (sTestName);
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

  @Nonnull
  public List <Short> makeEmptyList ()
  {
    return new ArrayList <Short> ();
  }

  protected List <Short> makeFullList ()
  {
    final List <Short> list = makeEmptyList ();
    final short [] elts = getFullElements ();
    for (final short elt : elts)
      list.add (Short.valueOf (elt));
    return list;
  }

  @Override
  public short [] getFullElements ()
  {
    final short [] ret = new short [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] = (short) i;
    return ret;
  }
}
