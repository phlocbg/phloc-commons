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
package org.apache.commons.collections.primitives.adapters.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.TestCharIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov 2006) $
 * @author Rodney Waldhoff
 */
public class TestReaderCharIterator extends TestCharIterator {

    // conventional
    // ------------------------------------------------------------------------

    public TestReaderCharIterator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestReaderCharIterator.class);
    }

    // ------------------------------------------------------------------------
    
    public boolean supportsRemove() {
        return false;
    }

    protected CharIterator makeEmptyCharIterator() {
        return new ReaderCharIterator(new StringReader(""));
    }

    protected CharIterator makeFullCharIterator() {
        return new ReaderCharIterator(new StringReader(new String(getFullElements())));
    }

    protected char[] getFullElements() {
        return "The quick brown fox jumped over the lazy dogs.".toCharArray();
    }


    // ------------------------------------------------------------------------
    
    public void testErrorThrowingReader() {
        Reader errReader = new Reader() {
            public int read(char[] buf, int off, int len) throws IOException {
                throw new IOException();
            }
            
            public void close() throws IOException {
            }
        };
        
        CharIterator iter = new ReaderCharIterator(errReader);
        try {
            iter.hasNext();
            fail("Expected RuntimeException");
        } catch(RuntimeException e) {
            // expected
        } 
        try {
            iter.next();
            fail("Expected RuntimeException");
        } catch(RuntimeException e) {
            // expected
        } 
    }
    
    public void testAdaptNull() {
        assertNull(ReaderCharIterator.adapt(null));
    }

    public void testAdaptNonNull() {
        assertNotNull(ReaderCharIterator.adapt(new StringReader("")));
    }
}
