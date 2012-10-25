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
public class TestRandomAccessDoubleList extends TestCase {

    // conventional
    // ------------------------------------------------------------------------

    public TestRandomAccessDoubleList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestRandomAccessDoubleList.class);
    }

    // tests
    // ------------------------------------------------------------------------
    
    public void testAddIsUnsupportedByDefault() {
        RandomAccessDoubleList list = new AbstractRandomAccessDoubleListImpl();
        try {
            list.add((double)1);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }        
        try {
            list.set(0,(double)1);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }

    public void testAddAllIsUnsupportedByDefault() {
        RandomAccessDoubleList list = new AbstractRandomAccessDoubleListImpl();
        DoubleList list2 = new ArrayDoubleList();
        list2.add((double)3);
        try {
            list.addAll(list2);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }
    
    public void testSetIsUnsupportedByDefault() {
        RandomAccessDoubleList list = new AbstractRandomAccessDoubleListImpl();
        try {
            list.set(0,(double)1);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }
    
    public void testRemoveElementIsUnsupportedByDefault() {
        RandomAccessDoubleList list = new AbstractRandomAccessDoubleListImpl();
        try {
            list.removeElementAt(0);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // expected
        }               
    }
    
    // inner classes
    // ------------------------------------------------------------------------


    static class AbstractRandomAccessDoubleListImpl extends RandomAccessDoubleList {
        public AbstractRandomAccessDoubleListImpl() {
        }

        /**
         * @see org.apache.commons.collections.primitives.DoubleList#get(int)
         */
        public double get(int index) {
            throw new IndexOutOfBoundsException();
        }

        /**
         * @see org.apache.commons.collections.primitives.DoubleCollection#size()
         */
        public int size() {
            return 0;
        }

    }
}
