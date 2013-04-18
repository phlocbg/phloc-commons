/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.commons.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link TypedObject}.
 * 
 * @author philip
 */
public final class TypedObjectTest
{
  @Test
  public void testAll () throws Exception
  {
    final ObjectType ot1 = new ObjectType ("type1");
    final ObjectType ot2 = new ObjectType ("type2");
    final TypedObject <String> t1 = TypedObject.create (ot1, "id1");
    assertSame (ot1, t1.getTypeID ());
    assertEquals ("id1", t1.getID ());
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (t1, TypedObject.create (ot1, "id1"));
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (t1, TypedObject.create (t1));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (t1, TypedObject.create (ot1, "id2"));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (t1, TypedObject.create (ot2, "id1"));

    // Serialization
    PhlocTestUtils.testDefaultSerialization (t1);
  }
}
