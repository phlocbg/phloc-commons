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
package com.phloc.commons.mock;

import static org.junit.Assert.assertNotNull;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.streamprovider.ByteArrayInputStreamProvider;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class contains default test methods to test the correctness of
 * implementations of standard methods.
 * 
 * @author philip
 */
@Immutable
public final class PhlocTestUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final PhlocTestUtils s_aInstance = new PhlocTestUtils ();

  private PhlocTestUtils ()
  {}

  private static void _assertTrue (@Nonnull final String sMsg, final boolean bTrue)
  {
    if (!bTrue)
      throw new IllegalStateException (sMsg);
  }

  private static void _assertFalse (@Nonnull final String sMsg, final boolean bTrue)
  {
    if (bTrue)
      throw new IllegalStateException (sMsg);
  }

  private static void _assertNotNull (@Nonnull final String sMsg, final Object aObj)
  {
    if (aObj == null)
      throw new IllegalStateException (sMsg);
  }

  @SuppressFBWarnings ({ "EC_NULL_ARG" })
  private static <DATATYPE> void _testEqualsImplementation (@Nonnull final DATATYPE aObject)// NOPMD
  {
    _assertNotNull ("Passed object may not be null!", aObject);
    _assertTrue ("Passed objects are not equal", aObject.equals (aObject));
    _assertFalse ("Object may no be equal to String", aObject.equals ("any string"));
    _assertFalse ("Object may no be equal to null", aObject.equals (null));// NOPMD
  }

  private static <DATATYPE> void _testEqualsImplementationWithEqualContentObject (@Nonnull final DATATYPE aObject,
                                                                                  @Nonnull final DATATYPE aObject2)// NOPMD
  {
    _testEqualsImplementation (aObject);
    _testEqualsImplementation (aObject2);
    _assertFalse ("This test may not be used with the same object!", aObject == aObject2);// NOPMD
    _assertTrue ("Passed objects are not identical!", aObject.equals (aObject2));
    _assertTrue ("Passed objects are not identical!", aObject2.equals (aObject));
  }

  private static <DATATYPE> void _testEqualsImplementationWithDifferentContentObject (@Nonnull final DATATYPE aObject,
                                                                                      @Nonnull final DATATYPE aObject2)// NOPMD
  {
    _testEqualsImplementation (aObject);
    _testEqualsImplementation (aObject2);
    _assertFalse ("This test may not be used with the same object!", aObject == aObject2);// NOPMD
    _assertFalse ("Passed objects are identical!", aObject.equals (aObject2));
    _assertFalse ("Passed objects are identical!", aObject2.equals (aObject));
  }

  private static <DATATYPE> void _testHashcodeImplementation (@Nonnull final DATATYPE aObject)// NOPMD
  {
    _assertNotNull ("Passed object may not be null!", aObject);
    _assertTrue ("hashCode() invocations must be consistent", aObject.hashCode () == aObject.hashCode ());
    _assertFalse ("hashCode() may not be 0", aObject.hashCode () == 0);
  }

  private static <DATATYPE> void _testHashcodeImplementationWithEqualContentObject (@Nonnull final DATATYPE aObject,
                                                                                    @Nonnull final DATATYPE aObject2)// NOPMD
  {
    _testHashcodeImplementation (aObject);
    _testHashcodeImplementation (aObject2);
    _assertTrue ("Passed objects are not identical!", aObject.equals (aObject2));
    _assertFalse ("This test may not be used with the same object!", aObject == aObject2);// NOPMD
    _assertTrue ("hashCode() invocations must be consistent", aObject.hashCode () == aObject2.hashCode ());
    _assertTrue ("hashCode() invocations must be consistent", aObject2.hashCode () == aObject.hashCode ());
  }

  private static <DATATYPE> void _testHashcodeImplementationWithDifferentContentObject (@Nonnull final DATATYPE aObject,
                                                                                        @Nonnull final DATATYPE aObject2)// NOPMD
  {
    _testHashcodeImplementation (aObject);
    _testHashcodeImplementation (aObject2);
    _assertFalse ("Passed objects are identical!", aObject.equals (aObject2));
    _assertFalse ("This test may not be used with the same object!", aObject == aObject2);// NOPMD
    _assertFalse ("hashCode() may not be the same for both objects", aObject.hashCode () == aObject2.hashCode ());
  }

  /**
   * Test the toString implementation of the passed object. It may not be empty,
   * and consistent.
   * 
   * @param aObject
   *        The object to be tested.
   */
  public static <DATATYPE> void testToStringImplementation (@Nonnull final DATATYPE aObject)// NOPMD
  {
    _assertNotNull ("Passed object may not be null!", aObject);
    _assertNotNull ("toString() may not return null!", aObject.toString ());
    _assertTrue ("toString() may not return an empty string!", aObject.toString ().length () > 0);
    _assertTrue ("toString() invocations must be consistent", aObject.toString ().equals (aObject.toString ()));
  }

  private static <DATATYPE> void _testToStringImplementationWithEqualContentObject (@Nonnull final DATATYPE aObject,
                                                                                    @Nonnull final DATATYPE aObject2)// NOPMD
  {
    testToStringImplementation (aObject);
    testToStringImplementation (aObject2);
    _assertTrue ("Passed objects are not identical!", aObject.equals (aObject2));
    _assertFalse ("This test may not be used with the same object!", aObject == aObject2);// NOPMD
  }

  private static <DATATYPE> void _testToStringImplementationWithDifferentContentObject (@Nonnull final DATATYPE aObject,
                                                                                        @Nonnull final DATATYPE aObject2)// NOPMD
  {
    testToStringImplementation (aObject);
    testToStringImplementation (aObject2);
    _assertFalse ("Passed objects are identical!", aObject.equals (aObject2));
    _assertFalse ("This test may not be used with the same object!", aObject == aObject2);// NOPMD
  }

  /**
   * Check if two different objects (who may not be the same) are equal to each
   * other. Checks toString, equals and hashCode.
   * 
   * @param aObject1
   *        First object. May not be <code>null</code>.
   * @param aObject2
   *        Second object. May not be <code>null</code>.
   */
  public static <DATATYPE> void testDefaultImplementationWithEqualContentObject (@Nonnull final DATATYPE aObject1,
                                                                                 @Nonnull final DATATYPE aObject2)// NOPMD
  {
    _testEqualsImplementationWithEqualContentObject (aObject1, aObject2);
    _testHashcodeImplementationWithEqualContentObject (aObject1, aObject2);
    _testToStringImplementationWithEqualContentObject (aObject1, aObject2);
  }

  /**
   * Check if two different objects are different to each other. Checks
   * toString, equals and hashCode.
   * 
   * @param aObject1
   *        First object. May not be <code>null</code>.
   * @param aObject2
   *        Second object. May not be <code>null</code>.
   */
  public static <DATATYPE> void testDefaultImplementationWithDifferentContentObject (@Nonnull final DATATYPE aObject1,
                                                                                     @Nonnull final DATATYPE aObject2)// NOPMD
  {
    _testEqualsImplementationWithDifferentContentObject (aObject1, aObject2);
    _testHashcodeImplementationWithDifferentContentObject (aObject1, aObject2);
    _testToStringImplementationWithDifferentContentObject (aObject1, aObject2);
  }

  /**
   * Test the serializability of objects. First writes the object to a byte
   * array stream, and then tries to rebuild it from there. After reading it
   * performs an equals check using
   * {@link #testDefaultImplementationWithEqualContentObject(Object, Object)}
   * 
   * @param <DATATYPE>
   *        The type of object to be serialized.
   * @param aSerializable
   *        The object to be written and read
   * @return The newly read object
   */
  @Nonnull
  public static <DATATYPE extends Serializable> DATATYPE testDefaultSerialization (@Nonnull final DATATYPE aSerializable)
  {
    try
    {
      // Serialize to byte array
      final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
      final ObjectOutputStream aOOS = new ObjectOutputStream (aBAOS);
      aOOS.writeObject (aSerializable);
      aOOS.close ();

      // Read new object from byte array
      final ObjectInputStream aOIS = new ObjectInputStream (new ByteArrayInputStreamProvider (aBAOS.toByteArray ()).getInputStream ());
      final DATATYPE aReadObject = GenericReflection.<Object, DATATYPE> uncheckedCast (aOIS.readObject ());
      aOIS.close ();

      // Now check them for equality
      testDefaultImplementationWithEqualContentObject (aSerializable, aReadObject);
      return aReadObject;
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("testDefaultSerialization failed", ex);
    }
  }

  /**
   * Test if the implementation {@link ICloneable} is OK. It creates a clone and
   * than uses
   * {@link #testDefaultImplementationWithEqualContentObject(Object, Object)} to
   * check for equality.
   * 
   * @param aCloneable
   *        The cloneable object to test
   */
  public static void testGetClone (@Nonnull final ICloneable <?> aCloneable)
  {
    final Object aClone = aCloneable.getClone ();
    _assertNotNull ("Clone returned a null object", aClone);
    _assertTrue ("Clone returned a different class than the original one",
                 aClone.getClass ().equals (aCloneable.getClass ()));
    testDefaultImplementationWithEqualContentObject (aCloneable, aClone);
  }

  /**
   * Test if the {@link MicroTypeConverter} is OK. It converts it to XML and
   * back and than uses
   * {@link #testDefaultImplementationWithEqualContentObject(Object, Object)} to
   * check for equality.
   * 
   * @param aObj
   *        The object to test
   */
  public static void testMicroTypeConversion (@Nonnull final Object aObj)
  {
    assertNotNull (aObj);

    // Write to XML
    final IMicroElement e = MicroTypeConverter.convertToMicroElement (aObj, "test");
    assertNotNull (e);

    // Read from XML
    final Object o2 = MicroTypeConverter.convertToNative (e, aObj.getClass ());
    assertNotNull (o2);

    // Ensure they are equals
    testDefaultImplementationWithEqualContentObject (aObj, o2);
  }
}
