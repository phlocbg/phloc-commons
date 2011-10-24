/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.typeconvert;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.locale.ELocaleName;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.state.EEnabled;
import com.phloc.commons.state.EFinish;
import com.phloc.commons.state.EInterrupt;
import com.phloc.commons.state.ELeftRight;
import com.phloc.commons.state.EMandatory;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ETopBottom;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.state.EValidity;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.commons.typeconvert.TypeConverterException.EReason;

/**
 * Test class for class {@link TypeConverter}.
 * 
 * @author philip
 */
public final class TypeConverterTest extends AbstractPhlocTestCase
{
  // All classes for which type converters from and to each other are defined
  private static final Class <?> [] CONVERTIBLE_CLASSES = new Class <?> [] { Boolean.class,
                                                                            Byte.class,
                                                                            Character.class,
                                                                            Double.class,
                                                                            Float.class,
                                                                            Integer.class,
                                                                            Long.class,
                                                                            Short.class,
                                                                            String.class,
                                                                            StringBuilder.class,
                                                                            StringBuffer.class,
                                                                            BigDecimal.class,
                                                                            BigInteger.class,
                                                                            AtomicBoolean.class,
                                                                            AtomicInteger.class,
                                                                            AtomicLong.class };

  @Nonnull
  private static Object _instantiate (@Nonnull final Class <?> aClass) throws Exception
  {
    if (aClass == AtomicBoolean.class)
      return new AtomicBoolean ((aClass.hashCode () % 2) == 0);
    if (aClass == AtomicInteger.class)
      return new AtomicInteger (aClass.hashCode ());
    if (aClass == AtomicLong.class)
      return new AtomicLong (aClass.hashCode () * 4711L);
    if (aClass == Boolean.class)
      return Boolean.valueOf ((aClass.getName ().hashCode () % 3) == 1);
    if (aClass == Character.class)
      return Character.valueOf (StringHelper.getLastChar (aClass.getName ()));
    // One character numeric string!
    return aClass.getConstructor (String.class).newInstance (Integer.toString (aClass.getName ().length () % 10));
  }

  @Test
  public void testConvertAllToAll () throws Exception
  {
    // Try convert all types into each other
    for (final Class <?> aSrcClass : CONVERTIBLE_CLASSES)
    {
      final Object aSrcValue = _instantiate (aSrcClass);

      // Convert into all other types
      for (final Class <?> aDstClass : CONVERTIBLE_CLASSES)
        assertNotNull (TypeConverter.convertIfNecessary (aSrcValue, aDstClass));

      // Convert to primitive types
      for (final Class <?> aDstClass : ClassHelper.getAllPrimitiveClasses ())
        assertNotNull (TypeConverter.convertIfNecessary (aSrcValue, aDstClass));

      assertNotNull (TypeConverter.convertIfNecessary (aSrcValue, List.class));
      assertNotNull (TypeConverter.convertIfNecessary (aSrcValue, Set.class));
    }
  }

  @Test
  public void testFromBoolean ()
  {
    for (int i = 0; i < 2; ++i)
    {
      final Boolean aSrcValue = Boolean.valueOf (i == 0);

      for (final Class <?> aDstClass : CONVERTIBLE_CLASSES)
        assertNotNull (TypeConverter.convertIfNecessary (aSrcValue, aDstClass));

      // Convert to primitive types
      for (final Class <?> aDstClass : ClassHelper.getAllPrimitiveClasses ())
        assertNotNull (TypeConverter.convertIfNecessary (aSrcValue, aDstClass));
    }
  }

  @Test
  public void testFromNonNumericString ()
  {
    // Test non numeric string
    for (final Class <?> aDstClass : ArrayHelper.getConcatenated (CONVERTIBLE_CLASSES, new Class <?> [] { Enum.class }))
      try
      {
        TypeConverter.convertIfNecessary ("not a number", aDstClass);
      }
      catch (final TypeConverterException ex)
      {
        assertEquals (EReason.CONVERSION_FAILED, ex.getReason ());
      }
  }

  @Test
  public void testToString ()
  {
    // Check conversion with explicit converters defined
    final Object [] aDefinedObjs = new Object [] { BigDecimal.ONE,
                                                  new BigDecimal (Double.MAX_VALUE),
                                                  BigInteger.ZERO,
                                                  new BigInteger ("123446712345678765456547865789762131"),
                                                  Byte.valueOf ((byte) 5),
                                                  Boolean.TRUE,
                                                  Character.valueOf ('c'),
                                                  Double.valueOf (1245.3433),
                                                  Float.valueOf (31.451f),
                                                  Integer.valueOf (17),
                                                  Long.valueOf (Long.MAX_VALUE),
                                                  Short.valueOf (Short.MIN_VALUE),
                                                  EChange.CHANGED,
                                                  EContinue.BREAK,
                                                  EEnabled.DISABLED,
                                                  EFinish.FINISHED,
                                                  EInterrupt.INTERRUPTED,
                                                  ELeftRight.RIGHT,
                                                  EMandatory.MANDATORY,
                                                  ESuccess.FAILURE,
                                                  ETopBottom.BOTTOM,
                                                  ETriState.UNDEFINED,
                                                  EValidity.VALID };
    for (final Object aSrcValue : aDefinedObjs)
    {
      final String sValue = TypeConverter.convertIfNecessary (aSrcValue, String.class);
      final Object aObj2 = TypeConverter.convertIfNecessary (sValue, aSrcValue.getClass ());
      assertEquals (aSrcValue, aObj2);
    }

    // Test conversion if no explicit converter available for source class, but
    // for super class
    final Object [] aUpCastObjs = new Object [] { ELocaleName.ID_LANGUAGE_ALL };
    for (final Object aSrcValue : aUpCastObjs)
    {
      final String sValue = TypeConverter.convertIfNecessary (aSrcValue, String.class);
      final Object aObj2 = TypeConverter.convertIfNecessary (sValue, aSrcValue.getClass ());
      assertEquals (aSrcValue, aObj2);
    }

    // Check if conversion works for special objects not overwriting
    // equals/hashcode
    for (final Object aSrcValue : new Object [] { new AtomicBoolean (true),
                                                 new AtomicInteger (177),
                                                 new AtomicLong (12374893127489L),
                                                 new StringBuilder (),
                                                 new StringBuilder ("Das StringBuilder kein equals implementiert ist doof"),
                                                 new StringBuffer (),
                                                 new StringBuffer ("Das gilt auch für StringBuffer") })
    {
      String sValue = TypeConverter.convertIfNecessary (aSrcValue, String.class);
      Object aObj2 = TypeConverter.convertIfNecessary (sValue, aSrcValue.getClass ());
      assertEquals (aSrcValue.toString (), aObj2.toString ());

      sValue = TypeConverter.convertIfNecessary (TypeConverterProviderExact.getInstance (), aSrcValue, String.class);
      aObj2 = TypeConverter.convertIfNecessary (TypeConverterProviderExact.getInstance (),
                                                sValue,
                                                aSrcValue.getClass ());
      assertEquals (aSrcValue.toString (), aObj2.toString ());

      sValue = TypeConverter.convertIfNecessary (TypeConverterProviderFuzzy.getInstance (), aSrcValue, String.class);
      aObj2 = TypeConverter.convertIfNecessary (TypeConverterProviderFuzzy.getInstance (),
                                                sValue,
                                                aSrcValue.getClass ());
      assertEquals (aSrcValue.toString (), aObj2.toString ());
    }
  }

  @Test
  public void testLocale ()
  {
    // Check if conversion works for all locales
    for (final Locale aLocale : LocaleCache.getAllLocales ())
    {
      final String sLocale = TypeConverter.convertIfNecessary (aLocale, String.class);
      final Locale aLocale2 = TypeConverter.convertIfNecessary (sLocale, Locale.class);
      assertEquals (aLocale, aLocale2);
    }
  }

  @Test
  public void testArray ()
  {
    // Check if conversion works for char[]
    final char [] aValue1 = "xyz".toCharArray ();
    String sValue = TypeConverter.convertIfNecessary (aValue1, String.class);
    final char [] aValue2 = TypeConverter.convertIfNecessary (sValue, char [].class);
    assertArrayEquals (aValue1, aValue2);

    // Check if conversion works for byte[]
    final byte [] aValue3 = new byte [256];
    for (int i = 0; i < aValue3.length; ++i)
      aValue3[i] = (byte) i;
    sValue = TypeConverter.convertIfNecessary (aValue3, String.class);
    final byte [] aValue4 = TypeConverter.convertIfNecessary (sValue, byte [].class);
    assertArrayEquals (aValue3, aValue4);
  }

  @Test
  public void testBigX ()
  {
    BigInteger aBI = new BigInteger ("1234");
    BigDecimal aBD = TypeConverter.convertIfNecessary (aBI, BigDecimal.class);
    assertEquals (1234, aBD.intValue ());
    assertEquals (aBI, aBD.toBigInteger ());

    aBD = new BigDecimal ("4567.891");
    aBI = TypeConverter.convertIfNecessary (aBD, BigInteger.class);
    assertEquals (4567, aBI.intValue ());
    assertEquals ("4567", aBI.toString ());
  }

  @Test
  public void testConvertToObject ()
  {
    final Object aObj = TypeConverter.convertIfNecessary (this, Object.class);
    assertNotNull (aObj);
    assertSame (this, aObj);
  }

  @Test
  public void testNoConverterButAssignable ()
  {
    final MockImplementation aImpl = new MockImplementation ();
    final IMockInterface aObj = TypeConverter.convertIfNecessary (aImpl, IMockInterface.class);
    assertNotNull (aObj);
    assertSame (aImpl, aObj);
  }

  @Test
  public void testNoConverterNoConversion ()
  {
    try
    {
      final MultiLingualText m = TypeConverter.convertIfNecessary ("1234", MultiLingualText.class);
      assertNotNull (m);
      fail ();
    }
    catch (final TypeConverterException ex)
    {}
  }

  @Test
  public void testBooleanArray ()
  {
    final boolean [] aBooleans = new boolean [] { true, false, true };
    assertFalse (TypeConverter.convertIfNecessary (aBooleans, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBooleans, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBooleans, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBooleans, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBooleans, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testByteArray ()
  {
    final byte [] aBytes = new byte [] { 5, 6, 7 };
    assertFalse (TypeConverter.convertIfNecessary (aBytes, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBytes, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBytes, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBytes, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aBytes, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testCharArray ()
  {
    final char [] aChars = new char [] { 'a', 'b', 'c' };
    assertFalse (TypeConverter.convertIfNecessary (aChars, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aChars, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aChars, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aChars, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aChars, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testDoubleArray ()
  {
    final double [] aDoubles = new double [] { 7, 3.14, 47.11 };
    assertFalse (TypeConverter.convertIfNecessary (aDoubles, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aDoubles, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aDoubles, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aDoubles, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aDoubles, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testFloatArray ()
  {
    final float [] aFloats = new float [] { 5, 1.1f, 12234.5f };
    assertFalse (TypeConverter.convertIfNecessary (aFloats, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aFloats, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aFloats, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aFloats, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aFloats, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testIntArray ()
  {
    final int [] aInts = new int [] { 6, 8, 110 };
    assertFalse (TypeConverter.convertIfNecessary (aInts, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aInts, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aInts, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aInts, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aInts, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testLongArray ()
  {
    final long [] aLongs = new long [] { 10, 111, 1212 };
    assertFalse (TypeConverter.convertIfNecessary (aLongs, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aLongs, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aLongs, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aLongs, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aLongs, LinkedHashSet.class).isEmpty ());
  }

  @Test
  public void testShortArray ()
  {
    final short [] aShorts = new short [] { 4, 5, 4 };
    assertFalse (TypeConverter.convertIfNecessary (aShorts, List.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aShorts, ArrayList.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aShorts, Set.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aShorts, HashSet.class).isEmpty ());
    assertFalse (TypeConverter.convertIfNecessary (aShorts, LinkedHashSet.class).isEmpty ());
  }
}
