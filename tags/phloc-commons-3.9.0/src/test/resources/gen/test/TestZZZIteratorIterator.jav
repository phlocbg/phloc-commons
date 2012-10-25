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
import org.apache.commons.collections.primitives.ArrayZZZList;
import org.apache.commons.collections.primitives.ZZZList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestZZZIteratorIterator extends AbstractTestIterator
{
  public TestZZZIteratorIterator (final String sTestName)
  {
    super (sTestName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestZZZIteratorIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  @Nonnull
  public Iterator <XXX> makeEmptyIterator ()
  {
    return ZZZIteratorIterator.wrap (makeEmptyZZZList ().iterator ());
  }

  @Override
  @Nonnull
  public Iterator <XXX> makeFullIterator ()
  {
    return ZZZIteratorIterator.wrap (makeFullZZZList ().iterator ());
  }

  @Nonnull
  protected ZZZList makeEmptyZZZList ()
  {
    return new ArrayZZZList ();
  }

  @Nonnull
  protected ZZZList makeFullZZZList ()
  {
    final ZZZList list = makeEmptyZZZList ();
    final YYY [] elts = getFullElements ();
    for (final YYY elt : elts)
      list.add (elt);
    return list;
  }

  @Nonnull
  public YYY [] getFullElements ()
  {
    final YYY [] ret = new YYY [10];
    for (int i = 0; i < ret.length; ++i)
      ret[i] = $CASTINT$ i;
    return ret;
  }
}
