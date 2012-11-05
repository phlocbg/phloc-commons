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
package org.apache.commons.collections.primitives.decorators;

import java.io.Serializable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.CharList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestUnmodifiableCharList extends BaseUnmodifiableCharListTest
{

  // conventional
  // ------------------------------------------------------------------------

  public TestUnmodifiableCharList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestUnmodifiableCharList.class);
  }

  // framework
  // ------------------------------------------------------------------------

  @Override
  protected CharList makeUnmodifiableCharList ()
  {
    return UnmodifiableCharList.wrap (makeCharList ());
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (UnmodifiableCharList.wrap (null));
  }

  public void testWrapUnmodifiableCharList ()
  {
    final CharList list = makeUnmodifiableCharList ();
    assertSame (list, UnmodifiableCharList.wrap (list));
  }

  public void testWrapSerializableCharList ()
  {
    final CharList list = makeCharList ();
    assertTrue (list instanceof Serializable);
    assertTrue (UnmodifiableCharList.wrap (list) instanceof Serializable);
  }

  public void testWrapNonSerializableCharList ()
  {
    final CharList list = makeCharList ();
    final CharList ns = list.subList (0, list.size ());
    assertTrue (!(ns instanceof Serializable));
    assertTrue (!(UnmodifiableCharList.wrap (ns) instanceof Serializable));
  }
}
