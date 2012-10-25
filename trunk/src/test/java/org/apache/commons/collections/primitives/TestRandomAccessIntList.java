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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov 2006) $
 * @author Rodney Waldhoff
 */
public class TestRandomAccessIntList extends TestCase {

    // conventional
    // ------------------------------------------------------------------------

    public TestRandomAccessIntList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestRandomAccessIntList.class);
    }

    // tests
    // ------------------------------------------------------------------------
    
    public void testAddIsUnsupportedByDefault() {
        RandomAccessIntList list = new AbstractRandomAccessIntListImpl();
        try {
            list.add(1);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }        
        try {
            list.set(0,1);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }

    public void testAddAllIsUnsupportedByDefault() {
        RandomAccessIntList list = new AbstractRandomAccessIntListImpl();
        IntList list2 = new ArrayIntList();
        list2.add(3);
        try {
            list.addAll(list2);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }
    
    public void testSetIsUnsupportedByDefault() {
        RandomAccessIntList list = new AbstractRandomAccessIntListImpl();
        try {
            list.set(0,1);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }
    
    public void testRemoveElementIsUnsupportedByDefault() {
        RandomAccessIntList list = new AbstractRandomAccessIntListImpl();
        try {
            list.removeElementAt(0);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }
    
    // inner classes
    // ------------------------------------------------------------------------


    static class AbstractRandomAccessIntListImpl extends RandomAccessIntList {
        public AbstractRandomAccessIntListImpl() {
        }

        /**
         * @see org.apache.commons.collections.primitives.IntList#get(int)
         */
        public int get(int index) {
            throw new IndexOutOfBoundsException();
        }

        /**
         * @see org.apache.commons.collections.primitives.IntCollection#size()
         */
        public int size() {
            return 0;
        }

    }
}
