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
package org.apache.commons.collections.primitives.adapters;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ArrayCharList;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.ArrayFloatList;
import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.ArrayShortList;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteListIterator;
import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.CharListIterator;
import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleListIterator;
import org.apache.commons.collections.primitives.FloatIterator;
import org.apache.commons.collections.primitives.FloatListIterator;
import org.apache.commons.collections.primitives.IntIterator;
import org.apache.commons.collections.primitives.IntListIterator;
import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.LongListIterator;
import org.apache.commons.collections.primitives.ShortIterator;
import org.apache.commons.collections.primitives.ShortListIterator;

/**
 * @version $Revision: 480451 $ $Date: 2006-11-29 08:45:08 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public class TestAdapt extends TestCase
{

  // conventional
  // ------------------------------------------------------------------------

  public TestAdapt (final String testName)
  {
    super (testName);
  }

  public static Test suite ()
  {
    return new TestSuite (TestAdapt.class);
  }

  // tests
  // ------------------------------------------------------------------------

  public void testHasPublicConstructorForReflectionBasedAPIs ()
  {
    assertNotNull (new Adapt ());
  }

  // to object based
  // ---------------------------------------------------------------

  public void testToCollection ()
  {
    assertNull (Adapt.toCollection ((ArrayByteList) null));
    assertNotNull (Adapt.toCollection (new ArrayByteList ()));
    assertNull (Adapt.toCollection ((ArrayCharList) null));
    assertNotNull (Adapt.toCollection (new ArrayCharList ()));
    assertNull (Adapt.toCollection ((ArrayDoubleList) null));
    assertNotNull (Adapt.toCollection (new ArrayDoubleList ()));
    assertNull (Adapt.toCollection ((ArrayFloatList) null));
    assertNotNull (Adapt.toCollection (new ArrayFloatList ()));
    assertNull (Adapt.toCollection ((ArrayIntList) null));
    assertNotNull (Adapt.toCollection (new ArrayIntList ()));
    assertNull (Adapt.toCollection ((ArrayLongList) null));
    assertNotNull (Adapt.toCollection (new ArrayLongList ()));
    assertNull (Adapt.toCollection ((ArrayShortList) null));
    assertNotNull (Adapt.toCollection (new ArrayShortList ()));
  }

  public void testToList ()
  {
    assertNull (Adapt.toList ((ArrayByteList) null));
    assertNotNull (Adapt.toList (new ArrayByteList ()));
    assertNull (Adapt.toList ((ArrayCharList) null));
    assertNotNull (Adapt.toList (new ArrayCharList ()));
    assertNull (Adapt.toList ((ArrayDoubleList) null));
    assertNotNull (Adapt.toList (new ArrayDoubleList ()));
    assertNull (Adapt.toList ((ArrayFloatList) null));
    assertNotNull (Adapt.toList (new ArrayFloatList ()));
    assertNull (Adapt.toList ((ArrayIntList) null));
    assertNotNull (Adapt.toList (new ArrayIntList ()));
    assertNull (Adapt.toList ((ArrayLongList) null));
    assertNotNull (Adapt.toList (new ArrayLongList ()));
    assertNull (Adapt.toList ((ArrayShortList) null));
    assertNotNull (Adapt.toList (new ArrayShortList ()));
  }

  public void testToIterator ()
  {
    assertNull (Adapt.toIterator ((ByteIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayByteList ().iterator ()));
    assertNull (Adapt.toIterator ((CharIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayCharList ().iterator ()));
    assertNull (Adapt.toIterator ((DoubleIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayDoubleList ().iterator ()));
    assertNull (Adapt.toIterator ((FloatIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayFloatList ().iterator ()));
    assertNull (Adapt.toIterator ((IntIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayIntList ().iterator ()));
    assertNull (Adapt.toIterator ((LongIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayLongList ().iterator ()));
    assertNull (Adapt.toIterator ((ShortIterator) null));
    assertNotNull (Adapt.toIterator (new ArrayShortList ().iterator ()));
  }

  public void testToListIterator ()
  {
    assertNull (Adapt.toListIterator ((ByteListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayByteList ().listIterator ()));
    assertNull (Adapt.toListIterator ((CharListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayCharList ().listIterator ()));
    assertNull (Adapt.toListIterator ((DoubleListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayDoubleList ().listIterator ()));
    assertNull (Adapt.toListIterator ((FloatListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayFloatList ().listIterator ()));
    assertNull (Adapt.toListIterator ((IntListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayIntList ().listIterator ()));
    assertNull (Adapt.toListIterator ((LongListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayLongList ().listIterator ()));
    assertNull (Adapt.toListIterator ((ShortListIterator) null));
    assertNotNull (Adapt.toListIterator (new ArrayShortList ().listIterator ()));
  }

  // to byte based
  // ---------------------------------------------------------------

  public void testToByteType ()
  {
    assertNotNull (Adapt.toByteCollection (new ArrayList <Byte> ()));
    assertNotNull (Adapt.toByteList (new ArrayList <Byte> ()));
    assertNotNull (Adapt.toByteIterator (new ArrayList <Byte> ().iterator ()));
    assertNotNull (Adapt.toByteListIterator (new ArrayList <Byte> ().listIterator ()));
  }

  public void testToByteTypeFromNull ()
  {
    assertNull (Adapt.toByteCollection (null));
    assertNull (Adapt.toByteList (null));
    assertNull (Adapt.toByteIterator (null));
    assertNull (Adapt.toByteListIterator (null));
  }

  // to char based
  // ---------------------------------------------------------------

  public void testToCharType ()
  {
    assertNotNull (Adapt.toCharCollection (new ArrayList <Character> ()));
    assertNotNull (Adapt.toCharList (new ArrayList <Character> ()));
    assertNotNull (Adapt.toCharIterator (new ArrayList <Character> ().iterator ()));
    assertNotNull (Adapt.toCharListIterator (new ArrayList <Character> ().listIterator ()));
  }

  public void testToCharTypeFromNull ()
  {
    assertNull (Adapt.toCharCollection (null));
    assertNull (Adapt.toCharList (null));
    assertNull (Adapt.toCharIterator (null));
    assertNull (Adapt.toCharListIterator (null));
  }

  // to double based
  // ---------------------------------------------------------------

  public void testToDoubleType ()
  {
    assertNotNull (Adapt.toDoubleCollection (new ArrayList <Double> ()));
    assertNotNull (Adapt.toDoubleList (new ArrayList <Double> ()));
    assertNotNull (Adapt.toDoubleIterator (new ArrayList <Double> ().iterator ()));
    assertNotNull (Adapt.toDoubleListIterator (new ArrayList <Double> ().listIterator ()));
  }

  public void testToDoubleTypeFromNull ()
  {
    assertNull (Adapt.toDoubleCollection (null));
    assertNull (Adapt.toDoubleList (null));
    assertNull (Adapt.toDoubleIterator (null));
    assertNull (Adapt.toDoubleListIterator (null));
  }

  // to float based
  // ---------------------------------------------------------------

  public void testToFloatType ()
  {
    assertNotNull (Adapt.toFloatCollection (new ArrayList <Float> ()));
    assertNotNull (Adapt.toFloatList (new ArrayList <Float> ()));
    assertNotNull (Adapt.toFloatIterator (new ArrayList <Float> ().iterator ()));
    assertNotNull (Adapt.toFloatListIterator (new ArrayList <Float> ().listIterator ()));
  }

  public void testToFloatTypeFromNull ()
  {
    assertNull (Adapt.toFloatCollection (null));
    assertNull (Adapt.toFloatList (null));
    assertNull (Adapt.toFloatIterator (null));
    assertNull (Adapt.toFloatListIterator (null));
  }

  // to int based
  // ---------------------------------------------------------------

  public void testToIntType ()
  {
    assertNotNull (Adapt.toIntCollection (new ArrayList <Integer> ()));
    assertNotNull (Adapt.toIntList (new ArrayList <Integer> ()));
    assertNotNull (Adapt.toIntIterator (new ArrayList <Integer> ().iterator ()));
    assertNotNull (Adapt.toIntListIterator (new ArrayList <Integer> ().listIterator ()));
  }

  public void testToIntTypeFromNull ()
  {
    assertNull (Adapt.toIntCollection (null));
    assertNull (Adapt.toIntList (null));
    assertNull (Adapt.toIntIterator (null));
    assertNull (Adapt.toIntListIterator (null));
  }

  // to long based
  // ---------------------------------------------------------------

  public void testToLongType ()
  {
    assertNotNull (Adapt.toLongCollection (new ArrayList <Long> ()));
    assertNotNull (Adapt.toLongList (new ArrayList <Long> ()));
    assertNotNull (Adapt.toLongIterator (new ArrayList <Long> ().iterator ()));
    assertNotNull (Adapt.toLongListIterator (new ArrayList <Long> ().listIterator ()));
  }

  public void testToLongTypeFromNull ()
  {
    assertNull (Adapt.toLongCollection (null));
    assertNull (Adapt.toLongList (null));
    assertNull (Adapt.toLongIterator (null));
    assertNull (Adapt.toLongListIterator (null));
  }

  // to short based
  // ---------------------------------------------------------------

  public void testToShortType ()
  {
    assertNotNull (Adapt.toShortCollection (new ArrayList <Short> ()));
    assertNotNull (Adapt.toShortList (new ArrayList <Short> ()));
    assertNotNull (Adapt.toShortIterator (new ArrayList <Short> ().iterator ()));
    assertNotNull (Adapt.toShortListIterator (new ArrayList <Short> ().listIterator ()));
  }

  public void testToShortTypeFromNull ()
  {
    assertNull (Adapt.toShortCollection (null));
    assertNull (Adapt.toShortList (null));
    assertNull (Adapt.toShortIterator (null));
    assertNull (Adapt.toShortListIterator (null));
  }
}
