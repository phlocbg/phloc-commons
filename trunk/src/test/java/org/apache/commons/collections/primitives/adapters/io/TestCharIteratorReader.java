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

import java.io.Reader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ArrayCharList;
import org.apache.commons.collections.primitives.CharList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestCharIteratorReader extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public TestCharIteratorReader (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestCharIteratorReader.class);
  }

  // ------------------------------------------------------------------------

  // ------------------------------------------------------------------------

  public void testReadNonEmpty () throws Exception
  {
    final String str = "The quick brown fox jumped over the lazy dogs.";
    final CharList list = new ArrayCharList ();
    for (int i = 0; i < str.length (); i++)
    {
      list.add (str.charAt (i));
    }

    final Reader in = new CharIteratorReader (list.iterator ());
    for (int i = 0; i < str.length (); i++)
    {
      assertEquals (str.charAt (i), in.read ());
    }
    assertEquals (-1, in.read ());
    assertEquals (-1, in.read ());
    in.close ();
  }

  public void testReadEmpty () throws Exception
  {
    final CharList list = new ArrayCharList ();
    final Reader in = new CharIteratorReader (list.iterator ());
    assertEquals (-1, in.read ());
    assertEquals (-1, in.read ());
    in.close ();
  }

  public void testAdaptNull ()
  {
    assertNull (CharIteratorReader.adapt (null));
  }

  public void testAdaptNonNull ()
  {
    assertNotNull (CharIteratorReader.adapt (new ArrayCharList ().iterator ()));
  }

}
