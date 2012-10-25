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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.AbstractTestObject;
import org.apache.commons.collections.primitives.ByteCollection;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestCollectionByteCollection extends AbstractTestObject
{

  // conventional
  // ------------------------------------------------------------------------

  public TestCollectionByteCollection (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestCollectionByteCollection.class);
  }

  // collections testing framework
  // ------------------------------------------------------------------------

  @Override
  public Object makeObject ()
  {
    final List <Byte> list = new ArrayList <Byte> ();
    for (int i = 0; i < 10; i++)
    {
      list.add (Byte.valueOf ((byte) i));
    }
    return new CollectionByteCollection (list);
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
    // Byte FIX ME Byte
    // need to add a serialized form to cvs
  }

  @Override
  public void testCanonicalFullCollectionExists ()
  {
    // Byte FIX ME Byte
    // need to add a serialized form to cvs
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (CollectionByteCollection.wrap (null));
  }

  public void testWrapSerializable ()
  {
    final ByteCollection collection = CollectionByteCollection.wrap (new ArrayList <Byte> ());
    assertNotNull (collection);
    assertTrue (collection instanceof Serializable);
  }

  public void testWrapNonSerializable ()
  {
    final ByteCollection collection = CollectionByteCollection.wrap (new AbstractList <Byte> ()
    {
      @Override
      public Byte get (final int i)
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
