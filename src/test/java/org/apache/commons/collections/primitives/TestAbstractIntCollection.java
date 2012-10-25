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
package org.apache.commons.collections.primitives;

import java.util.Collections;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.adapters.IteratorIntIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestAbstractIntCollection extends TestCase
{
  public TestAbstractIntCollection (final String sTestName)
  {
    super (sTestName);
  }

  @Nonnull
  public static Test suite ()
  {
    return new TestSuite (TestAbstractIntCollection.class);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testAddIsUnsupportedByDefault ()
  {
    final IntCollection col = new IntCollectionImpl ();
    try
    {
      col.add (42);
      fail ("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e)
    {
      // expected
    }
  }

  // inner classes
  // ------------------------------------------------------------------------

  static final class IntCollectionImpl extends AbstractIntCollection
  {
    public IntCollectionImpl ()
    {}

    @Override
    public IntIterator iterator ()
    {
      return new IteratorIntIterator (Collections.<Integer> emptyList ().iterator ());
    }

    @Override
    @Nonnegative
    public int size ()
    {
      return 0;
    }
  }
}
