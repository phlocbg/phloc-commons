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

import org.apache.commons.collections.primitives.DoubleList;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestUnmodifiableDoubleList extends BaseUnmodifiableDoubleListTest
{

  // conventional
  // ------------------------------------------------------------------------

  public TestUnmodifiableDoubleList (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestUnmodifiableDoubleList.class);
  }

  // framework
  // ------------------------------------------------------------------------

  @Override
  protected DoubleList makeUnmodifiableDoubleList ()
  {
    return UnmodifiableDoubleList.wrap (makeDoubleList ());
  }

  // tests
  // ------------------------------------------------------------------------

  public void testWrapNull ()
  {
    assertNull (UnmodifiableDoubleList.wrap (null));
  }

  public void testWrapUnmodifiableDoubleList ()
  {
    final DoubleList list = makeUnmodifiableDoubleList ();
    assertSame (list, UnmodifiableDoubleList.wrap (list));
  }

  public void testWrapSerializableDoubleList ()
  {
    final DoubleList list = makeDoubleList ();
    assertTrue (list instanceof Serializable);
    assertTrue (UnmodifiableDoubleList.wrap (list) instanceof Serializable);
  }

  public void testWrapNonSerializableDoubleList ()
  {
    final DoubleList list = makeDoubleList ();
    final DoubleList ns = list.subList (0, list.size ());
    assertTrue (!(ns instanceof Serializable));
    assertTrue (!(UnmodifiableDoubleList.wrap (ns) instanceof Serializable));
  }
}
