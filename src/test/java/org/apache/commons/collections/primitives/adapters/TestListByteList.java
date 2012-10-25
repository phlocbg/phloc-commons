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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.TestByteList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov 2006) $
 * @author Rodney Waldhoff
 */
public class TestListByteList extends TestByteList {

    // conventional
    // ------------------------------------------------------------------------

    public TestListByteList(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = BulkTest.makeSuite(TestListByteList.class);
        return suite;
    }

    // collections testing framework
    // ------------------------------------------------------------------------

    /**
     * @see org.apache.commons.collections.primitives.TestByteList#makeEmptyByteList()
     */
    protected ByteList makeEmptyByteList() {
        return new ListByteList(new ArrayList());
    }
    
    public String[] ignoredTests() {
        // sublists are not serializable
        return new String[] { 
            "TestListByteList.bulkTestSubList.testFullListSerialization",
            "TestListByteList.bulkTestSubList.testEmptyListSerialization",
            "TestListByteList.bulkTestSubList.testCanonicalEmptyCollectionExists",
            "TestListByteList.bulkTestSubList.testCanonicalFullCollectionExists",
            "TestListByteList.bulkTestSubList.testEmptyListCompatibility",
            "TestListByteList.bulkTestSubList.testFullListCompatibility",
            "TestListByteList.bulkTestSubList.testSerializeDeserializeThenCompare",
            "TestListByteList.bulkTestSubList.testSimpleSerialization"
        };
    }

    // tests
    // ------------------------------------------------------------------------

    /** @TODO need to add serialized form to cvs */
    public void testCanonicalEmptyCollectionExists() {
        // XXX FIX ME XXX
        // need to add a serialized form to cvs
    }

    public void testCanonicalFullCollectionExists() {
        // XXX FIX ME XXX
        // need to add a serialized form to cvs
    }

    public void testEmptyListCompatibility() {
        // XXX FIX ME XXX
        // need to add a serialized form to cvs
    }

    public void testFullListCompatibility() {
        // XXX FIX ME XXX
        // need to add a serialized form to cvs
    }
    public void testWrapNull() {
        assertNull(ListByteList.wrap(null));
    }
    
    public void testWrapSerializable() {
        ByteList list = ListByteList.wrap(new ArrayList());
        assertNotNull(list);
        assertTrue(list instanceof Serializable);
    }
    
    public void testWrapNonSerializable() {
        ByteList list = ListByteList.wrap(new AbstractList() { 
            public Object get(int i) { throw new IndexOutOfBoundsException(); } 
            public int size() { return 0; } 
        });
        assertNotNull(list);
        assertTrue(!(list instanceof Serializable));
    }

}
