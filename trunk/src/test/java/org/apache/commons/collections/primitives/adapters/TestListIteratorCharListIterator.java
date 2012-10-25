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

import org.apache.commons.collections.primitives.CharListIterator;
import org.apache.commons.collections.primitives.TestCharListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestListIteratorCharListIterator extends TestCharListIterator
{

  // conventional
  // ------------------------------------------------------------------------

  public TestListIteratorCharListIterator (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestListIteratorCharListIterator.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public CharListIterator makeEmptyCharListIterator ()
  {
    return ListIteratorCharListIterator.wrap (makeEmptyList ().listIterator ());
  }

  @Override
  public CharListIterator makeFullCharListIterator ()
  {
    return ListIteratorCharListIterator.wrap (makeFullList ().listIterator ());
  }

  public List makeEmptyList ()
  {
    return new ArrayList ();
  }

  protected List makeFullList ()
  {
    final List list = makeEmptyList ();
    final char [] elts = getFullElements ();
    for (final char elt : elts)
    {
      list.add (new Character (elt));
    }
    return list;
  }

  @Override
  public char [] getFullElements ()
  {
    return new char [] { (char) 0,
                        (char) 1,
                        (char) 2,
                        (char) 3,
                        (char) 4,
                        (char) 5,
                        (char) 6,
                        (char) 7,
                        (char) 8,
                        (char) 9 };
  }

  // tests
  // ------------------------------------------------------------------------

}
