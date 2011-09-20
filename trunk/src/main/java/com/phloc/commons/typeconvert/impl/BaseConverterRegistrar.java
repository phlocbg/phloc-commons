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
package com.phloc.commons.typeconvert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterUpCast;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.commons.typeconvert.TypeConverterRegistry;

/**
 * Register the base type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class BaseConverterRegistrar implements ITypeConverterRegistrarSPI
{
  /**
   * Register all type converters for the 15 base types:<br>
   * <ul>
   * <li>Boolean</li>
   * <li>Byte</li>
   * <li>Character</li>
   * <li>Double</li>
   * <li>Float</li>
   * <li>Integer</li>
   * <li>Long</li>
   * <li>Short</li>
   * <li>String</li>
   * <li>BigDecimal</li>
   * <li>BigInteger</li>
   * <li>AtomicBoolean</li>
   * <li>AtomicInteger</li>
   * <li>AtomicLong</li>
   * <li>StringBuffer</li>
   * <li>StringBuilder</li>
   * </ul>
   */
  public void registerTypeConverter ()
  {
    // to Boolean
    final ITypeConverter aConverterNumberToBoolean = new ITypeConverter ()
    {
      public Boolean convert (final Object aSource)
      {
        return Boolean.valueOf (((Number) aSource).intValue () != 0);
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (Double.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (Float.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (Long.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (Short.class, Boolean.class, aConverterNumberToBoolean);
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Boolean.class, new ITypeConverter ()
    {
      public Boolean convert (final Object aSource)
      {
        return Boolean.valueOf (((AtomicBoolean) aSource).get ());
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Boolean.class, new ITypeConverter ()
    {
      public Boolean convert (final Object aSource)
      {
        return Boolean.valueOf (((Character) aSource).charValue () != 0);
      }
    });
    final ITypeConverter aConverterStringToBoolean = new ITypeConverter ()
    {
      public Boolean convert (final Object aSource)
      {
        return Boolean.valueOf (aSource.toString ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Boolean.class, aConverterStringToBoolean);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Boolean.class, aConverterStringToBoolean);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Boolean.class, aConverterStringToBoolean);

    // to Byte
    final ITypeConverter aConverterNumberToByte = new ITypeConverter ()
    {
      public Byte convert (final Object aSource)
      {
        return Byte.valueOf (((Number) aSource).byteValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (Double.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (Float.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (Long.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (Short.class, Byte.class, aConverterNumberToByte);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (final Object aSource)
      {
        return Byte.valueOf (((Boolean) aSource).booleanValue () ? (byte) 1 : (byte) 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (final Object aSource)
      {
        return Byte.valueOf (((AtomicBoolean) aSource).get () ? (byte) 1 : (byte) 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (final Object aSource)
      {
        return Byte.valueOf ((byte) ((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToByte = new ITypeConverter ()
    {
      public Byte convert (final Object aSource)
      {
        try
        {
          return Byte.valueOf (Byte.parseByte (aSource.toString ()));
        }
        catch (final NumberFormatException ex)
        {
          return null;
        }
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Byte.class, aConverterStringToByte);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Byte.class, aConverterStringToByte);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Byte.class, aConverterStringToByte);

    // to Character
    final ITypeConverter aConverterNumberToCharacter = new ITypeConverter ()
    {
      public Character convert (final Object aSource)
      {
        return Character.valueOf ((char) ((Number) aSource).intValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Double.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Float.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Long.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Short.class, Character.class, aConverterNumberToCharacter);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Character.class, new ITypeConverter ()
    {
      public Character convert (final Object aSource)
      {
        return Character.valueOf (((Boolean) aSource).booleanValue () ? (char) 1 : (char) 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Character.class, new ITypeConverter ()
    {
      public Character convert (final Object aSource)
      {
        return Character.valueOf (((AtomicBoolean) aSource).get () ? (char) 1 : (char) 0);
      }
    });
    final ITypeConverter aConverterStringToCharacter = new ITypeConverter ()
    {
      public Character convert (final Object aSource)
      {
        final String sSource = aSource.toString ();
        return sSource.length () == 1 ? Character.valueOf (sSource.charAt (0)) : null;
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Character.class, aConverterStringToCharacter);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Character.class, aConverterStringToCharacter);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Character.class, aConverterStringToCharacter);

    // to Double
    final ITypeConverter aConverterNumberToDouble = new ITypeConverter ()
    {
      public Double convert (final Object aSource)
      {
        return Double.valueOf (((Number) aSource).doubleValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (Float.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (Long.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (Short.class, Double.class, aConverterNumberToDouble);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Double.class, new ITypeConverter ()
    {
      public Double convert (final Object aSource)
      {
        return Double.valueOf (((Boolean) aSource).booleanValue () ? 1d : 0d);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Double.class, new ITypeConverter ()
    {
      public Double convert (final Object aSource)
      {
        return Double.valueOf (((AtomicBoolean) aSource).get () ? 1d : 0d);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Double.class, new ITypeConverter ()
    {
      public Double convert (final Object aSource)
      {
        return Double.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToDouble = new ITypeConverter ()
    {
      public Double convert (final Object aSource)
      {
        return StringHelper.parseDoubleObj (aSource.toString (), null);
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Double.class, aConverterStringToDouble);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Double.class, aConverterStringToDouble);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Double.class, aConverterStringToDouble);

    // to Float
    final ITypeConverter aConverterNumberToFloat = new ITypeConverter ()
    {
      public Float convert (final Object aSource)
      {
        return Float.valueOf (((Number) aSource).floatValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (Double.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (Long.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (Short.class, Float.class, aConverterNumberToFloat);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Float.class, new ITypeConverter ()
    {
      public Float convert (final Object aSource)
      {
        return Float.valueOf (((Boolean) aSource).booleanValue () ? 1f : 0f);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Float.class, new ITypeConverter ()
    {
      public Float convert (final Object aSource)
      {
        return Float.valueOf (((AtomicBoolean) aSource).get () ? 1f : 0f);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Float.class, new ITypeConverter ()
    {
      public Float convert (final Object aSource)
      {
        return Float.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToFloat = new ITypeConverter ()
    {
      public Float convert (final Object aSource)
      {
        return StringHelper.parseFloatObj (aSource.toString (), null);
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Float.class, aConverterStringToFloat);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Float.class, aConverterStringToFloat);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Float.class, aConverterStringToFloat);

    // to Integer
    final ITypeConverter aConverterNumberToInteger = new ITypeConverter ()
    {
      public Integer convert (final Object aSource)
      {
        return Integer.valueOf (((Number) aSource).intValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (Double.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (Float.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (Long.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (Short.class, Integer.class, aConverterNumberToInteger);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (final Object aSource)
      {
        return Integer.valueOf (((Boolean) aSource).booleanValue () ? 1 : 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (final Object aSource)
      {
        return Integer.valueOf (((AtomicBoolean) aSource).get () ? 1 : 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (final Object aSource)
      {
        return Integer.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToInteger = new ITypeConverter ()
    {
      public Integer convert (final Object aSource)
      {
        return StringHelper.parseIntObj (aSource.toString (), null);
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Integer.class, aConverterStringToInteger);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Integer.class, aConverterStringToInteger);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Integer.class, aConverterStringToInteger);

    // to Long
    final ITypeConverter aConverterNumberToLong = new ITypeConverter ()
    {
      public Long convert (final Object aSource)
      {
        return Long.valueOf (((Number) aSource).longValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (Double.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (Float.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (Short.class, Long.class, aConverterNumberToLong);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Long.class, new ITypeConverter ()
    {
      public Long convert (final Object aSource)
      {
        return Long.valueOf (((Boolean) aSource).booleanValue () ? 1L : 0L);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Long.class, new ITypeConverter ()
    {
      public Long convert (final Object aSource)
      {
        return Long.valueOf (((AtomicBoolean) aSource).get () ? 1L : 0L);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Long.class, new ITypeConverter ()
    {
      public Long convert (final Object aSource)
      {
        return Long.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToLong = new ITypeConverter ()
    {
      public Long convert (final Object aSource)
      {
        return StringHelper.parseLongObj (aSource.toString (), null);
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Long.class, aConverterStringToLong);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Long.class, aConverterStringToLong);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Long.class, aConverterStringToLong);

    // to Short
    final ITypeConverter aConverterNumberToShort = new ITypeConverter ()
    {
      public Short convert (final Object aSource)
      {
        return Short.valueOf (((Number) aSource).shortValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (Double.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (Float.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (Long.class, Short.class, aConverterNumberToShort);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Short.class, new ITypeConverter ()
    {
      public Short convert (final Object aSource)
      {
        return Short.valueOf (((Boolean) aSource).booleanValue () ? (short) 1 : (short) 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Short.class, new ITypeConverter ()
    {
      public Short convert (final Object aSource)
      {
        return Short.valueOf (((AtomicBoolean) aSource).get () ? (short) 1 : (short) 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, Short.class, new ITypeConverter ()
    {
      public Short convert (final Object aSource)
      {
        return Short.valueOf ((short) ((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToShort = new ITypeConverter ()
    {
      public Short convert (final Object aSource)
      {
        try
        {
          return Short.valueOf (Short.parseShort (aSource.toString ()));
        }
        catch (final NumberFormatException ex)
        {
          return null;
        }
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, Short.class, aConverterStringToShort);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Short.class, aConverterStringToShort);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Short.class, aConverterStringToShort);

    // to String
    final ITypeConverter aConverterToString = new ITypeConverter ()
    {
      public String convert (final Object aSource)
      {
        return aSource.toString ();
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Byte.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Character.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Double.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Float.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Integer.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Long.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (Short.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, String.class, aConverterToString);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, String.class, aConverterToString);

    // to BigDecimal
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (final Object aSource)
      {
        return new BigDecimal ((BigInteger) aSource);
      }
    });
    final ITypeConverter aConverterNumberToBigDecimal = new ITypeConverter ()
    {
      public BigDecimal convert (final Object aSource)
      {
        return BigDecimal.valueOf (((Number) aSource).doubleValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Byte.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Double.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Float.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Integer.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Long.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Short.class, BigDecimal.class, aConverterNumberToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (final Object aSource)
      {
        return ((Boolean) aSource).booleanValue () ? BigDecimal.ONE : BigDecimal.ZERO;
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (final Object aSource)
      {
        return ((AtomicBoolean) aSource).get () ? BigDecimal.ONE : BigDecimal.ZERO;
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (final Object aSource)
      {
        return BigDecimal.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToBigDecimal = new ITypeConverter ()
    {
      public BigDecimal convert (final Object aSource)
      {
        return StringHelper.parseBigDecimal (aSource.toString ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, BigDecimal.class, aConverterStringToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, BigDecimal.class, aConverterStringToBigDecimal);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, BigDecimal.class, aConverterStringToBigDecimal);

    // to BigInteger
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (final Object aSource)
      {
        return ((BigDecimal) aSource).toBigInteger ();
      }
    });
    final ITypeConverter aConverterNumberToBigInteger = new ITypeConverter ()
    {
      public BigInteger convert (final Object aSource)
      {
        return BigInteger.valueOf (((Number) aSource).longValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Byte.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Double.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Float.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Integer.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Long.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Short.class, BigInteger.class, aConverterNumberToBigInteger);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (final Object aSource)
      {
        return ((Boolean) aSource).booleanValue () ? BigInteger.ONE : BigInteger.ZERO;
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (final Object aSource)
      {
        return ((AtomicBoolean) aSource).get () ? BigInteger.ONE : BigInteger.ZERO;
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (final Object aSource)
      {
        return BigInteger.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToBigInteger = new ITypeConverter ()
    {
      public BigInteger convert (final Object aSource)
      {
        return StringHelper.parseBigInteger (aSource.toString ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, BigInteger.class, aConverterStringToBigInteger);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, BigInteger.class, aConverterStringToBigInteger);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, BigInteger.class, aConverterStringToBigInteger);

    // to AtomicBoolean
    final ITypeConverter aConverterToAtomicBoolean = new ITypeConverter ()
    {
      public AtomicBoolean convert (final Object aSource)
      {
        return new AtomicBoolean (TypeConverter.convertIfNecessary (aSource, Boolean.class).booleanValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Byte.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Double.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Float.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Integer.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Long.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Short.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (Character.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (String.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, AtomicBoolean.class, aConverterToAtomicBoolean);

    // to AtomicInteger
    final ITypeConverter aConverterNumberToAtomicInteger = new ITypeConverter ()
    {
      public AtomicInteger convert (final Object aSource)
      {
        return new AtomicInteger (((Number) aSource).intValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Byte.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Double.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Float.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Integer.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Long.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Short.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (final Object aSource)
      {
        return new AtomicInteger (((Boolean) aSource).booleanValue () ? 1 : 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (final Object aSource)
      {
        return new AtomicInteger (((AtomicBoolean) aSource).get () ? 1 : 0);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (final Object aSource)
      {
        return new AtomicInteger (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToAtomicInteger = new ITypeConverter ()
    {
      public AtomicInteger convert (final Object aSource)
      {
        final Integer aInt = StringHelper.parseIntObj (aSource.toString (), null);
        return aInt == null ? null : new AtomicInteger (aInt.intValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, AtomicInteger.class, aConverterStringToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class,
                                                 AtomicInteger.class,
                                                 aConverterStringToAtomicInteger);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class,
                                                 AtomicInteger.class,
                                                 aConverterStringToAtomicInteger);

    // to AtomicLong
    final ITypeConverter aConverterNumberToAtomicLong = new ITypeConverter ()
    {
      public AtomicLong convert (final Object aSource)
      {
        return new AtomicLong (((Number) aSource).longValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Byte.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Double.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Float.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Integer.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Long.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Short.class, AtomicLong.class, aConverterNumberToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (final Object aSource)
      {
        return new AtomicLong (((Boolean) aSource).booleanValue () ? 1L : 0L);
      }
    });
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (final Object aSource)
      {
        return new AtomicLong (((AtomicBoolean) aSource).get () ? 1L : 0L);
      }
    });
    TypeConverterRegistry.registerTypeConverter (Character.class, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (final Object aSource)
      {
        return new AtomicLong (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToAtomicLong = new ITypeConverter ()
    {
      public AtomicLong convert (final Object aSource)
      {
        final Long aLong = StringHelper.parseLongObj (aSource.toString (), null);
        return aLong == null ? null : new AtomicLong (aLong.longValue ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, AtomicLong.class, aConverterStringToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, AtomicLong.class, aConverterStringToAtomicLong);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, AtomicLong.class, aConverterStringToAtomicLong);

    // toList<?>
    final ITypeConverter aConverterObjectToList = new ITypeConverter ()
    {
      public List <?> convert (final Object aSource)
      {
        final List <Object> ret = new ArrayList <Object> ();
        ret.add (aSource);
        return ret;
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Byte.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Character.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Double.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Float.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Integer.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Long.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (Short.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (String.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, List.class, aConverterObjectToList);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, List.class, aConverterObjectToList);

    // to Set<?>
    final ITypeConverter aConverterObjectToSet = new ITypeConverter ()
    {
      public Set <?> convert (final Object aSource)
      {
        final Set <Object> ret = new HashSet <Object> ();
        ret.add (aSource);
        return ret;
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Byte.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Character.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Double.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Float.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Integer.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Long.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (Short.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (String.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class, Set.class, aConverterObjectToSet);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class, Set.class, aConverterObjectToSet);

    // StringBuilder
    final ITypeConverter aConverterObjectToStringBuilder = new ITypeConverter ()
    {
      public StringBuilder convert (final Object aSource)
      {
        return new StringBuilder (TypeConverter.convertIfNecessary (aSource, String.class));
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class,
                                                 StringBuilder.class,
                                                 aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class,
                                                 StringBuilder.class,
                                                 aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Byte.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Character.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Double.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Float.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Integer.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Long.class, StringBuilder.class, aConverterObjectToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (Short.class, StringBuilder.class, aConverterObjectToStringBuilder);
    final ITypeConverter aConverterStringToStringBuilder = new ITypeConverter ()
    {
      public StringBuilder convert (final Object aSource)
      {
        return new StringBuilder (aSource.toString ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, StringBuilder.class, aConverterStringToStringBuilder);
    TypeConverterRegistry.registerTypeConverter (StringBuffer.class,
                                                 StringBuilder.class,
                                                 aConverterStringToStringBuilder);

    // StringBuffer
    final ITypeConverter aConverterObjectToStringBuffer = new ITypeConverter ()
    {
      public StringBuffer convert (final Object aSource)
      {
        return new StringBuffer (TypeConverter.convertIfNecessary (aSource, String.class));
      }
    };
    TypeConverterRegistry.registerTypeConverter (AtomicBoolean.class,
                                                 StringBuffer.class,
                                                 aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (AtomicInteger.class,
                                                 StringBuffer.class,
                                                 aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (AtomicLong.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (BigDecimal.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (BigInteger.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Boolean.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Byte.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Character.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Double.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Float.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Integer.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Long.class, StringBuffer.class, aConverterObjectToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (Short.class, StringBuffer.class, aConverterObjectToStringBuffer);
    final ITypeConverter aConverterStringToStringBuffer = new ITypeConverter ()
    {
      public StringBuffer convert (final Object aSource)
      {
        return new StringBuffer (aSource.toString ());
      }
    };
    TypeConverterRegistry.registerTypeConverter (String.class, StringBuffer.class, aConverterStringToStringBuffer);
    TypeConverterRegistry.registerTypeConverter (StringBuilder.class,
                                                 StringBuffer.class,
                                                 aConverterStringToStringBuffer);

    // Enum
    TypeConverterRegistry.registerTypeConverter (Enum.class, String.class, new ITypeConverter ()
    {
      public String convert (final Object aSource)
      {
        // We need to append the Enum class name, otherwise we cannot resolve
        // it! Use the colon as it is not allowed in class names.
        return aSource.getClass ().getName () + ':' + ((Enum <?>) aSource).name ();
      }
    });
    TypeConverterRegistry.registerTypeConverter (String.class, Enum.class, new ITypeConverterUpCast ()
    {
      public Enum <?> convert (final Object aSource)
      {
        // Split class name and enum value name
        final String [] aParts = RegExHelper.split ((String) aSource, ":", 2);
        try
        {
          // Resolve any enum class
          // Note: The explicit EChange is just here, because an explicit enum
          // type is needed. It must of course not only be EChange :)
          final Class <EChange> aClass = GenericReflection.forName (aParts[0]);

          // And look up the element by name
          return Enum.valueOf (aClass, aParts[1]);
        }
        catch (final ClassNotFoundException ex)
        {
          return null;
        }
      }
    });

    // byte[]
    TypeConverterRegistry.registerTypeConverter (byte [].class, String.class, new ITypeConverter ()
    {
      public String convert (final Object aSource)
      {
        return CharsetManager.getAsString ((byte []) aSource, CCharset.CHARSET_ISO_8859_1);
      }
    });
    TypeConverterRegistry.registerTypeConverter (String.class, byte [].class, new ITypeConverter ()
    {
      public byte [] convert (final Object aSource)
      {
        return CharsetManager.getAsBytes ((String) aSource, CCharset.CHARSET_ISO_8859_1);
      }
    });

    // char[]
    TypeConverterRegistry.registerTypeConverter (char [].class, String.class, new ITypeConverter ()
    {
      public String convert (final Object aSource)
      {
        return new String ((char []) aSource);
      }
    });
    TypeConverterRegistry.registerTypeConverter (String.class, char [].class, new ITypeConverter ()
    {
      public char [] convert (final Object aSource)
      {
        return ((String) aSource).toCharArray ();
      }
    });
  }
}
