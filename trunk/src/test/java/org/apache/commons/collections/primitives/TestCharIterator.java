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
import org.apache.commons.collections.primitives.adapters.CharIteratorIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov 2006) $
 * @author Rodney Waldhoff
 */
public abstract class TestCharIterator extends AbstractTestIterator {

    // conventional
    // ------------------------------------------------------------------------

    public TestCharIterator(String testName) {
        super(testName);
    }

    // collections testing framework
    // ------------------------------------------------------------------------

    public Iterator makeEmptyIterator() {
        return CharIteratorIterator.wrap(makeEmptyCharIterator());
    }

    public Iterator makeFullIterator() {
        return CharIteratorIterator.wrap(makeFullCharIterator());
    }


    protected abstract CharIterator makeEmptyCharIterator();
    protected abstract CharIterator makeFullCharIterator();
    protected abstract char[] getFullElements();

    // tests
    // ------------------------------------------------------------------------
    
    public void testNextHasNextRemove() {
        char[] elements = getFullElements();
        CharIterator iter = makeFullCharIterator();
        for(int i=0;i<elements.length;i++) {
            assertTrue(iter.hasNext());
            assertEquals(elements[i],iter.next(),0f);
            if(supportsRemove()) {
                iter.remove();
            }
        }        
        assertTrue(! iter.hasNext() );
    }

    public void testEmptyCharIterator() {
        assertTrue( ! makeEmptyCharIterator().hasNext() );
        try {
            makeEmptyCharIterator().next();
            fail("Expected NoSuchElementException");
        } catch(NoSuchElementException e) {
            // expected
        }
        if(supportsRemove()) {
            try {
                makeEmptyCharIterator().remove();
                fail("Expected IllegalStateException");
            } catch(IllegalStateException e) {
                // expected
            }
        }        
    }

    public void testRemoveBeforeNext() {
        if(supportsRemove()) {
            try {
                makeFullCharIterator().remove();
                fail("Expected IllegalStateException");
            } catch(IllegalStateException e) {
                // expected
            }
        }        
    }

    public void testRemoveAfterRemove() {
        if(supportsRemove()) {
            CharIterator iter = makeFullCharIterator();
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
