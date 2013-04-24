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
package com.phloc.types.datatype.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.w3c.dom.Node;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.types.datatype.ISimpleDataType;
import com.phloc.types.datatype.ISimpleDataTypeRegistrarSPI;

/**
 * Registry for all simple data type descriptors.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class SimpleDataTypeRegistry
{
  public static final ISimpleDataType <Boolean> DT_BOOLEAN = SimpleDataType.create ("boolean", Boolean.class);
  public static final ISimpleDataType <Byte> DT_BYTE = SimpleDataType.create ("byte", Byte.class);
  public static final ISimpleDataType <Character> DT_CHAR = SimpleDataType.create ("char", Character.class);
  public static final ISimpleDataType <Double> DT_DOUBLE = SimpleDataType.create ("double", Double.class);
  public static final ISimpleDataType <Float> DT_FLOAT = SimpleDataType.create ("float", Float.class);
  public static final ISimpleDataType <Integer> DT_INT = SimpleDataType.create ("int", Integer.class);
  public static final ISimpleDataType <Long> DT_LONG = SimpleDataType.create ("long", Long.class);
  public static final ISimpleDataType <Short> DT_SHORT = SimpleDataType.create ("short", Short.class);
  public static final ISimpleDataType <String> DT_STRING = SimpleDataType.create ("string", String.class);
  public static final ISimpleDataType <BigInteger> DT_BIGINTEGER = SimpleDataType.create ("bigint", BigInteger.class);
  public static final ISimpleDataType <BigDecimal> DT_BIGDECIMAL = SimpleDataType.create ("bigdec", BigDecimal.class);
  public static final ISimpleDataType <AtomicBoolean> DT_ATOMICBOOLEAN = SimpleDataType.create ("atomicboolean",
                                                                                                AtomicBoolean.class);
  public static final ISimpleDataType <AtomicInteger> DT_ATOMICINT = SimpleDataType.create ("atomicint",
                                                                                            AtomicInteger.class);
  public static final ISimpleDataType <AtomicLong> DT_ATOMICLONG = SimpleDataType.create ("atomiclong",
                                                                                          AtomicLong.class);
  public static final ISimpleDataType <LocalDate> DT_LOCALDATE = SimpleDataType.create ("localdate", LocalDate.class);
  public static final ISimpleDataType <LocalTime> DT_LOCALTIME = SimpleDataType.create ("localtime", LocalTime.class);
  public static final ISimpleDataType <DateTime> DT_DATETIME = SimpleDataType.create ("datetime", DateTime.class);
  public static final ISimpleDataType <LocalDateTime> DT_LOCALDATETIME = SimpleDataType.create ("localdatetime",
                                                                                                LocalDateTime.class);
  public static final ISimpleDataType <Period> DT_PERIOD = SimpleDataType.create ("period", Period.class);
  public static final ISimpleDataType <Locale> DT_LOCALE = SimpleDataType.create ("locale", Locale.class);
  public static final ISimpleDataType <Node> DT_DOMNODE = SimpleDataType.create ("domnode", Node.class);
  public static final ISimpleDataType <IMicroNode> DT_MICRONODE = SimpleDataType.create ("micronode", IMicroNode.class);
  public static final ISimpleDataType <boolean []> DT_BOOLEAN_ARRAY = SimpleDataType.create ("boolean_array",
                                                                                             boolean [].class);
  public static final ISimpleDataType <byte []> DT_BYTE_ARRAY = SimpleDataType.create ("byte_array", byte [].class);
  public static final ISimpleDataType <char []> DT_CHAR_ARRAY = SimpleDataType.create ("char_array", char [].class);
  public static final ISimpleDataType <double []> DT_DOUBLE_ARRAY = SimpleDataType.create ("double_array",
                                                                                           double [].class);
  public static final ISimpleDataType <float []> DT_FLOAT_ARRAY = SimpleDataType.create ("float_array", float [].class);
  public static final ISimpleDataType <int []> DT_INT_ARRAY = SimpleDataType.create ("int_array", int [].class);
  public static final ISimpleDataType <long []> DT_LONG_ARRAY = SimpleDataType.create ("long_array", long [].class);
  public static final ISimpleDataType <short []> DT_SHORT_ARRAY = SimpleDataType.create ("short_array", short [].class);

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <String, ISimpleDataType <?>> s_aMap = new HashMap <String, ISimpleDataType <?>> ();

  static
  {
    registerSimpleDataType (DT_BOOLEAN);
    registerSimpleDataType (DT_BYTE);
    registerSimpleDataType (DT_CHAR);
    registerSimpleDataType (DT_DOUBLE);
    registerSimpleDataType (DT_FLOAT);
    registerSimpleDataType (DT_INT);
    registerSimpleDataType (DT_LONG);
    registerSimpleDataType (DT_SHORT);
    registerSimpleDataType (DT_STRING);
    registerSimpleDataType (DT_BIGINTEGER);
    registerSimpleDataType (DT_BIGDECIMAL);
    registerSimpleDataType (DT_ATOMICBOOLEAN);
    registerSimpleDataType (DT_ATOMICINT);
    registerSimpleDataType (DT_ATOMICLONG);
    registerSimpleDataType (DT_LOCALDATE);
    registerSimpleDataType (DT_LOCALTIME);
    registerSimpleDataType (DT_DATETIME);
    registerSimpleDataType (DT_LOCALDATETIME);
    registerSimpleDataType (DT_PERIOD);
    registerSimpleDataType (DT_LOCALE);
    registerSimpleDataType (DT_DOMNODE);
    registerSimpleDataType (DT_MICRONODE);
    registerSimpleDataType (DT_BOOLEAN_ARRAY);
    registerSimpleDataType (DT_BYTE_ARRAY);
    registerSimpleDataType (DT_CHAR_ARRAY);
    registerSimpleDataType (DT_DOUBLE_ARRAY);
    registerSimpleDataType (DT_FLOAT_ARRAY);
    registerSimpleDataType (DT_INT_ARRAY);
    registerSimpleDataType (DT_LONG_ARRAY);
    registerSimpleDataType (DT_SHORT_ARRAY);

    // Register all custom data types
    for (final ISimpleDataTypeRegistrarSPI aSPI : ServiceLoaderUtils.getAllSPIImplementations (ISimpleDataTypeRegistrarSPI.class))
      aSPI.registerSimpleDataTypes ();
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final SimpleDataTypeRegistry s_aInstance = new SimpleDataTypeRegistry ();

  private SimpleDataTypeRegistry ()
  {}

  public static void registerSimpleDataType (@Nonnull final ISimpleDataType <?> aDataType)
  {
    if (aDataType == null)
      throw new NullPointerException ("dataType");

    s_aRWLock.writeLock ().lock ();
    try
    {
      final String sID = aDataType.getID ();
      if (s_aMap.containsKey (sID))
        throw new IllegalArgumentException ("A data type with ID '" + sID + "' is already contained!");
      s_aMap.put (sID, aDataType);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <String> getAllRegisteredSimpleDataTypeIDs ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (s_aMap.keySet ());
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public static ISimpleDataType <?> getSimpleDataTypeOfID (@Nullable final String sID)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aMap.get (sID);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
