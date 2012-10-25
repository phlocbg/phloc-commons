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
package org.apache.commons.collections.primitives.decorators;

import java.io.Serializable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ByteList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestUnmodifiableByteList extends BaseUnmodifiableByteListTest
{

  // conventional
  // ------------------------------------------------------------------------

  public TestUnmodifiableByteList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestUnmodifiableByteList.class);
  }

  // framework
  // ------------------------------------------------------------------------

  @Override
  protected ByteList makeUnmodifiableByteList ()
  {
    return UnmodifiableByteList.wrap (makeByteList ());
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (UnmodifiableByteList.wrap (null));
  }

  public void testWrapUnmodifiableByteList ()
  {
    final ByteList list = makeUnmodifiableByteList ();
    assertSame (list, UnmodifiableByteList.wrap (list));
  }

  public void testWrapSerializableByteList ()
  {
    final ByteList list = makeByteList ();
    assertTrue (list instanceof Serializable);
    assertTrue (UnmodifiableByteList.wrap (list) instanceof Serializable);
  }

  public void testWrapNonSerializableByteList ()
  {
    final ByteList list = makeByteList ();
    final ByteList ns = list.subList (0, list.size ());
    assertTrue (!(ns instanceof Serializable));
    assertTrue (!(UnmodifiableByteList.wrap (ns) instanceof Serializable));
  }
}
