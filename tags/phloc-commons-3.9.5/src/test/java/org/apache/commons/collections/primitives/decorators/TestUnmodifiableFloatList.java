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
package org.apache.commons.collections.primitives.decorators;

import java.io.Serializable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.FloatList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestUnmodifiableFloatList extends BaseUnmodifiableFloatListTest
{

  // conventional
  // ------------------------------------------------------------------------

  public TestUnmodifiableFloatList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestUnmodifiableFloatList.class);
  }

  // framework
  // ------------------------------------------------------------------------

  @Override
  protected FloatList makeUnmodifiableFloatList ()
  {
    return UnmodifiableFloatList.wrap (makeFloatList ());
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (UnmodifiableFloatList.wrap (null));
  }

  public void testWrapUnmodifiableFloatList ()
  {
    final FloatList list = makeUnmodifiableFloatList ();
    assertSame (list, UnmodifiableFloatList.wrap (list));
  }

  public void testWrapSerializableFloatList ()
  {
    final FloatList list = makeFloatList ();
    assertTrue (list instanceof Serializable);
    assertTrue (UnmodifiableFloatList.wrap (list) instanceof Serializable);
  }

  public void testWrapNonSerializableFloatList ()
  {
    final FloatList list = makeFloatList ();
    final FloatList ns = list.subList (0, list.size ());
    assertTrue (!(ns instanceof Serializable));
    assertTrue (!(UnmodifiableFloatList.wrap (ns) instanceof Serializable));
  }
}
