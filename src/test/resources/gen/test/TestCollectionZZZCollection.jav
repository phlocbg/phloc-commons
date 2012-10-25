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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.AbstractTestObject;
import org.apache.commons.collections.primitives.ZZZCollection;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestCollectionZZZCollection extends AbstractTestObject
{

  // conventional
  // ------------------------------------------------------------------------

  public TestCollectionZZZCollection (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestCollectionZZZCollection.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public Object makeObject ()
  {
    final List <XXX> list = new ArrayList <XXX> ();
    for (int i = 0; i < 10; i++)
    {
      list.add (XXX.valueOf ($CASTINT$i));
    }
    return new CollectionZZZCollection (list);
  }

  @Override
  public void testSerializeDeserializeThenCompare ()
  {
    // Collection.equal contract doesn't work that way
  }

  /** @TODO need to add serialized form to cvs */
  @Override
  public void testCanonicalEmptyCollectionExists ()
  {
    // XXX FIX ME XXX
    // need to add a serialized form to cvs
  }

  @Override
  public void testCanonicalFullCollectionExists ()
  {
    // XXX FIX ME XXX
    // need to add a serialized form to cvs
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (CollectionZZZCollection.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final ZZZCollection collection = CollectionZZZCollection.wrap (new ArrayList <XXX> ());
    assertNotNull (collection);
    assertTrue (collection instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final ZZZCollection collection = CollectionZZZCollection.wrap (new AbstractList <XXX> ()
    {
      @Override
      public XXX get (final int i)
      {
        throw new IndexOutOfBoundsException ();
      }

      @Override
      public int size ()
      {
        return 0;
      }
    });
    assertNotNull (collection);
    assertTrue (!(collection instanceof Serializable));
  }
}
