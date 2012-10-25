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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.collections.iterators.AbstractTestIterator;
import org.apache.commons.collections.primitives.adapters.LongIteratorIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov 2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestLongIterator extends AbstractTestIterator {

    // conventional
    // ------------------------------------------------------------------------

    public TestLongIterator(String testName) {
        super(testName);
    }

    // collections testing framework
    // ------------------------------------------------------------------------

    public Iterator makeEmptyIterator() {
        return LongIteratorIterator.wrap(makeEmptyLongIterator());
    }

    public Iterator makeFullIterator() {
        return LongIteratorIterator.wrap(makeFullLongIterator());
    }


    protected abstract LongIterator makeEmptyLongIterator();
    protected abstract LongIterator makeFullLongIterator();
    protected abstract long[] getFullElements();

    // tests
    // ------------------------------------------------------------------------
    
    public void testNextHasNextRemove() {
        long[] elements = getFullElements();
        LongIterator iter = makeFullLongIterator();
        for(int i=0;i<elements.length;i++) {
            assertTrue(iter.hasNext());
            assertEquals(elements[i],iter.next());
            if(supportsRemove()) {
                iter.remove();
            }
        }        
        assertTrue(! iter.hasNext() );
    }

    public void testEmptyLongIterator() {
        assertTrue( ! makeEmptyLongIterator().hasNext() );
        try {
            makeEmptyLongIterator().next();
            fail("Expected NoSuchElementException");
        } catch(NoSuchElementException e) {
            // expected
        }
        if(supportsRemove()) {
            try {
                makeEmptyLongIterator().remove();
                fail("Expected IllegalStateException");
            } catch(IllegalStateException e) {
                // expected
            }
        }        
    }

    public void testRemoveBeforeNext() {
        if(supportsRemove()) {
            try {
                makeFullLongIterator().remove();
                fail("Expected IllegalStateException");
            } catch(IllegalStateException e) {
                // expected
            }
        }        
    }

    public void testRemoveAfterRemove() {
        if(supportsRemove()) {
            LongIterator iter = makeFullLongIterator();
            iter.next();
            iter.remove();
            try {
                iter.remove();
                fail("Expected IllegalStateException");
            } catch(IllegalStateException e) {
                // expected
            }
        }        
    }
}
