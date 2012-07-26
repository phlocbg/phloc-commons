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
package com.phloc.commons.typeconvert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.base64.Base64;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;
import com.phloc.commons.typeconvert.ITypeConverterUpCast;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * Register the base type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class BaseTypeConverterRegistrar implements ITypeConverterRegistrarSPI
{
  static final class NumberToShortTypeConverter implements ITypeConverter
  {
    public Short convert (@Nonnull final Object aSource)
    {
      return Short.valueOf (((Number) aSource).shortValue ());
    }
  }

  static final class NumberToLongTypeConverter implements ITypeConverter
  {
    public Long convert (@Nonnull final Object aSource)
    {
      return Long.valueOf (((Number) aSource).longValue ());
    }
  }

  static final class NumberToIntegerTypeConverter implements ITypeConverter
  {
    public Integer convert (@Nonnull final Object aSource)
    {
      return Integer.valueOf (((Number) aSource).intValue ());
    }
  }

  static final class NumberToFloatTypeConverter implements ITypeConverter
  {
    public Float convert (@Nonnull final Object aSource)
    {
      return Float.valueOf (((Number) aSource).floatValue ());
    }
  }

  static final class NumberToDoubleTypeConverter implements ITypeConverter
  {
    public Double convert (@Nonnull final Object aSource)
    {
      return Double.valueOf (((Number) aSource).doubleValue ());
    }
  }

  static final class NumberToCharacterTypeConverter implements ITypeConverter
  {
    public Character convert (@Nonnull final Object aSource)
    {
      return Character.valueOf ((char) ((Number) aSource).intValue ());
    }
  }

  static final class NumberToByteTypeConverter implements ITypeConverter
  {
    public Byte convert (@Nonnull final Object aSource)
    {
      return Byte.valueOf (((Number) aSource).byteValue ());
    }
  }

  static final class NumberToBooleanTypeConverter implements ITypeConverter
  {
    public Boolean convert (@Nonnull final Object aSource)
    {
      return Boolean.valueOf (((Number) aSource).intValue () != 0);
    }
  }

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
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    // to Boolean
    final ITypeConverter aConverterNumberToBoolean = new NumberToBooleanTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (AtomicLong.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (BigDecimal.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (BigInteger.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (Byte.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (Double.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (Float.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (Integer.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (Long.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (Short.class, Boolean.class, aConverterNumberToBoolean);
    aRegistry.registerTypeConverter (AtomicBoolean.class, Boolean.class, new ITypeConverter ()
    {
      public Boolean convert (@Nonnull final Object aSource)
      {
        return Boolean.valueOf (((AtomicBoolean) aSource).get ());
      }
    });
    aRegistry.registerTypeConverter (Character.class, Boolean.class, new ITypeConverter ()
    {
      public Boolean convert (@Nonnull final Object aSource)
      {
        return Boolean.valueOf (((Character) aSource).charValue () != 0);
      }
    });
    final ITypeConverter aConverterStringToBoolean = new ITypeConverter ()
    {
      public Boolean convert (@Nonnull final Object aSource)
      {
        return Boolean.valueOf (aSource.toString ());
      }
    };
    aRegistry.registerTypeConverter (String.class, Boolean.class, aConverterStringToBoolean);
    aRegistry.registerTypeConverter (StringBuilder.class, Boolean.class, aConverterStringToBoolean);
    aRegistry.registerTypeConverter (StringBuffer.class, Boolean.class, aConverterStringToBoolean);

    // to Byte
    final ITypeConverter aConverterNumberToByte = new NumberToByteTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (AtomicLong.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (BigDecimal.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (BigInteger.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (Double.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (Float.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (Integer.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (Long.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (Short.class, Byte.class, aConverterNumberToByte);
    aRegistry.registerTypeConverter (Boolean.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (@Nonnull final Object aSource)
      {
        return Byte.valueOf (((Boolean) aSource).booleanValue () ? (byte) 1 : (byte) 0);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (@Nonnull final Object aSource)
      {
        return Byte.valueOf (((AtomicBoolean) aSource).get () ? (byte) 1 : (byte) 0);
      }
    });
    aRegistry.registerTypeConverter (Character.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (@Nonnull final Object aSource)
      {
        return Byte.valueOf ((byte) ((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToByte = new ITypeConverter ()
    {
      public Byte convert (@Nonnull final Object aSource)
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
    aRegistry.registerTypeConverter (String.class, Byte.class, aConverterStringToByte);
    aRegistry.registerTypeConverter (StringBuilder.class, Byte.class, aConverterStringToByte);
    aRegistry.registerTypeConverter (StringBuffer.class, Byte.class, aConverterStringToByte);

    // to Character
    final ITypeConverter aConverterNumberToCharacter = new NumberToCharacterTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (AtomicLong.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (BigDecimal.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (BigInteger.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Byte.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Double.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Float.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Integer.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Long.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Short.class, Character.class, aConverterNumberToCharacter);
    aRegistry.registerTypeConverter (Boolean.class, Character.class, new ITypeConverter ()
    {
      public Character convert (@Nonnull final Object aSource)
      {
        return Character.valueOf (((Boolean) aSource).booleanValue () ? (char) 1 : (char) 0);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Character.class, new ITypeConverter ()
    {
      public Character convert (@Nonnull final Object aSource)
      {
        return Character.valueOf (((AtomicBoolean) aSource).get () ? (char) 1 : (char) 0);
      }
    });
    final ITypeConverter aConverterStringToCharacter = new ITypeConverter ()
    {
      public Character convert (@Nonnull final Object aSource)
      {
        final String sSource = aSource.toString ();
        return sSource.length () == 1 ? Character.valueOf (sSource.charAt (0)) : null;
      }
    };
    aRegistry.registerTypeConverter (String.class, Character.class, aConverterStringToCharacter);
    aRegistry.registerTypeConverter (StringBuilder.class, Character.class, aConverterStringToCharacter);
    aRegistry.registerTypeConverter (StringBuffer.class, Character.class, aConverterStringToCharacter);

    // to Double
    final ITypeConverter aConverterNumberToDouble = new NumberToDoubleTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (AtomicLong.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (BigDecimal.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (BigInteger.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (Byte.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (Float.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (Integer.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (Long.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (Short.class, Double.class, aConverterNumberToDouble);
    aRegistry.registerTypeConverter (Boolean.class, Double.class, new ITypeConverter ()
    {
      public Double convert (@Nonnull final Object aSource)
      {
        return Double.valueOf (((Boolean) aSource).booleanValue () ? 1d : 0d);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Double.class, new ITypeConverter ()
    {
      public Double convert (@Nonnull final Object aSource)
      {
        return Double.valueOf (((AtomicBoolean) aSource).get () ? 1d : 0d);
      }
    });
    aRegistry.registerTypeConverter (Character.class, Double.class, new ITypeConverter ()
    {
      public Double convert (@Nonnull final Object aSource)
      {
        return Double.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToDouble = new ITypeConverter ()
    {
      public Double convert (@Nonnull final Object aSource)
      {
        return StringHelper.parseDoubleObj (aSource.toString (), null);
      }
    };
    aRegistry.registerTypeConverter (String.class, Double.class, aConverterStringToDouble);
    aRegistry.registerTypeConverter (StringBuilder.class, Double.class, aConverterStringToDouble);
    aRegistry.registerTypeConverter (StringBuffer.class, Double.class, aConverterStringToDouble);

    // to Float
    final ITypeConverter aConverterNumberToFloat = new NumberToFloatTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (AtomicLong.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (BigDecimal.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (BigInteger.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (Byte.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (Double.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (Integer.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (Long.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (Short.class, Float.class, aConverterNumberToFloat);
    aRegistry.registerTypeConverter (Boolean.class, Float.class, new ITypeConverter ()
    {
      public Float convert (@Nonnull final Object aSource)
      {
        return Float.valueOf (((Boolean) aSource).booleanValue () ? 1f : 0f);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Float.class, new ITypeConverter ()
    {
      public Float convert (@Nonnull final Object aSource)
      {
        return Float.valueOf (((AtomicBoolean) aSource).get () ? 1f : 0f);
      }
    });
    aRegistry.registerTypeConverter (Character.class, Float.class, new ITypeConverter ()
    {
      public Float convert (@Nonnull final Object aSource)
      {
        return Float.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToFloat = new ITypeConverter ()
    {
      public Float convert (@Nonnull final Object aSource)
      {
        return StringHelper.parseFloatObj (aSource.toString (), null);
      }
    };
    aRegistry.registerTypeConverter (String.class, Float.class, aConverterStringToFloat);
    aRegistry.registerTypeConverter (StringBuilder.class, Float.class, aConverterStringToFloat);
    aRegistry.registerTypeConverter (StringBuffer.class, Float.class, aConverterStringToFloat);

    // to Integer
    final ITypeConverter aConverterNumberToInteger = new NumberToIntegerTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (AtomicLong.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (BigDecimal.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (BigInteger.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (Byte.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (Double.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (Float.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (Long.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (Short.class, Integer.class, aConverterNumberToInteger);
    aRegistry.registerTypeConverter (Boolean.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (@Nonnull final Object aSource)
      {
        return Integer.valueOf (((Boolean) aSource).booleanValue () ? 1 : 0);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (@Nonnull final Object aSource)
      {
        return Integer.valueOf (((AtomicBoolean) aSource).get () ? 1 : 0);
      }
    });
    aRegistry.registerTypeConverter (Character.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (@Nonnull final Object aSource)
      {
        return Integer.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToInteger = new ITypeConverter ()
    {
      public Integer convert (@Nonnull final Object aSource)
      {
        return StringHelper.parseIntObj (aSource.toString (), null);
      }
    };
    aRegistry.registerTypeConverter (String.class, Integer.class, aConverterStringToInteger);
    aRegistry.registerTypeConverter (StringBuilder.class, Integer.class, aConverterStringToInteger);
    aRegistry.registerTypeConverter (StringBuffer.class, Integer.class, aConverterStringToInteger);

    // to Long
    final ITypeConverter aConverterNumberToLong = new NumberToLongTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (AtomicLong.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (BigDecimal.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (BigInteger.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (Byte.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (Double.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (Float.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (Integer.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (Short.class, Long.class, aConverterNumberToLong);
    aRegistry.registerTypeConverter (Boolean.class, Long.class, new ITypeConverter ()
    {
      public Long convert (@Nonnull final Object aSource)
      {
        return Long.valueOf (((Boolean) aSource).booleanValue () ? 1L : 0L);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Long.class, new ITypeConverter ()
    {
      public Long convert (@Nonnull final Object aSource)
      {
        return Long.valueOf (((AtomicBoolean) aSource).get () ? 1L : 0L);
      }
    });
    aRegistry.registerTypeConverter (Character.class, Long.class, new ITypeConverter ()
    {
      public Long convert (@Nonnull final Object aSource)
      {
        return Long.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToLong = new ITypeConverter ()
    {
      public Long convert (@Nonnull final Object aSource)
      {
        return StringHelper.parseLongObj (aSource.toString (), null);
      }
    };
    aRegistry.registerTypeConverter (String.class, Long.class, aConverterStringToLong);
    aRegistry.registerTypeConverter (StringBuilder.class, Long.class, aConverterStringToLong);
    aRegistry.registerTypeConverter (StringBuffer.class, Long.class, aConverterStringToLong);

    // to Short
    final ITypeConverter aConverterNumberToShort = new NumberToShortTypeConverter ();
    aRegistry.registerTypeConverter (AtomicInteger.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (AtomicLong.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (BigDecimal.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (BigInteger.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (Byte.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (Double.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (Float.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (Integer.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (Long.class, Short.class, aConverterNumberToShort);
    aRegistry.registerTypeConverter (Boolean.class, Short.class, new ITypeConverter ()
    {
      public Short convert (@Nonnull final Object aSource)
      {
        return Short.valueOf (((Boolean) aSource).booleanValue () ? (short) 1 : (short) 0);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, Short.class, new ITypeConverter ()
    {
      public Short convert (@Nonnull final Object aSource)
      {
        return Short.valueOf (((AtomicBoolean) aSource).get () ? (short) 1 : (short) 0);
      }
    });
    aRegistry.registerTypeConverter (Character.class, Short.class, new ITypeConverter ()
    {
      public Short convert (@Nonnull final Object aSource)
      {
        return Short.valueOf ((short) ((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToShort = new ITypeConverter ()
    {
      public Short convert (@Nonnull final Object aSource)
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
    aRegistry.registerTypeConverter (String.class, Short.class, aConverterStringToShort);
    aRegistry.registerTypeConverter (StringBuilder.class, Short.class, aConverterStringToShort);
    aRegistry.registerTypeConverter (StringBuffer.class, Short.class, aConverterStringToShort);

    // to String
    final ITypeConverter aConverterToString = new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        return aSource.toString ();
      }
    };
    aRegistry.registerTypeConverter (AtomicBoolean.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (AtomicInteger.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (AtomicLong.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (BigDecimal.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (BigInteger.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Boolean.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Byte.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Character.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Double.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Float.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Integer.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Long.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (Short.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (StringBuilder.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (StringBuffer.class, String.class, aConverterToString);

    // to BigDecimal
    aRegistry.registerTypeConverter (BigInteger.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return new BigDecimal ((BigInteger) aSource);
      }
    });
    final ITypeConverter aConverterNumberToBigDecimal = new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return BigDecimal.valueOf (((Number) aSource).doubleValue ());
      }
    };
    aRegistry.registerTypeConverter (AtomicInteger.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (AtomicLong.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Byte.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Double.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Float.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Integer.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Long.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Short.class, BigDecimal.class, aConverterNumberToBigDecimal);
    aRegistry.registerTypeConverter (Boolean.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return ((Boolean) aSource).booleanValue () ? BigDecimal.ONE : BigDecimal.ZERO;
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return ((AtomicBoolean) aSource).get () ? BigDecimal.ONE : BigDecimal.ZERO;
      }
    });
    aRegistry.registerTypeConverter (Character.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return BigDecimal.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToBigDecimal = new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return StringHelper.parseBigDecimal (aSource.toString ());
      }
    };
    aRegistry.registerTypeConverter (String.class, BigDecimal.class, aConverterStringToBigDecimal);
    aRegistry.registerTypeConverter (StringBuilder.class, BigDecimal.class, aConverterStringToBigDecimal);
    aRegistry.registerTypeConverter (StringBuffer.class, BigDecimal.class, aConverterStringToBigDecimal);

    // to BigInteger
    aRegistry.registerTypeConverter (BigDecimal.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return ((BigDecimal) aSource).toBigInteger ();
      }
    });
    final ITypeConverter aConverterNumberToBigInteger = new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return BigInteger.valueOf (((Number) aSource).longValue ());
      }
    };
    aRegistry.registerTypeConverter (AtomicInteger.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (AtomicLong.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Byte.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Double.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Float.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Integer.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Long.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Short.class, BigInteger.class, aConverterNumberToBigInteger);
    aRegistry.registerTypeConverter (Boolean.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return ((Boolean) aSource).booleanValue () ? BigInteger.ONE : BigInteger.ZERO;
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return ((AtomicBoolean) aSource).get () ? BigInteger.ONE : BigInteger.ZERO;
      }
    });
    aRegistry.registerTypeConverter (Character.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return BigInteger.valueOf (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToBigInteger = new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return StringHelper.parseBigInteger (aSource.toString ());
      }
    };
    aRegistry.registerTypeConverter (String.class, BigInteger.class, aConverterStringToBigInteger);
    aRegistry.registerTypeConverter (StringBuilder.class, BigInteger.class, aConverterStringToBigInteger);
    aRegistry.registerTypeConverter (StringBuffer.class, BigInteger.class, aConverterStringToBigInteger);

    // to AtomicBoolean
    final ITypeConverter aConverterToAtomicBoolean = new ITypeConverter ()
    {
      public AtomicBoolean convert (@Nonnull final Object aSource)
      {
        return new AtomicBoolean (TypeConverter.convertIfNecessary (aSource, Boolean.class).booleanValue ());
      }
    };
    aRegistry.registerTypeConverter (AtomicInteger.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (AtomicLong.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (BigDecimal.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (BigInteger.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Boolean.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Byte.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Double.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Float.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Integer.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Long.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Short.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (Character.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (String.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (StringBuilder.class, AtomicBoolean.class, aConverterToAtomicBoolean);
    aRegistry.registerTypeConverter (StringBuffer.class, AtomicBoolean.class, aConverterToAtomicBoolean);

    // to AtomicInteger
    final ITypeConverter aConverterNumberToAtomicInteger = new ITypeConverter ()
    {
      public AtomicInteger convert (@Nonnull final Object aSource)
      {
        return new AtomicInteger (((Number) aSource).intValue ());
      }
    };
    aRegistry.registerTypeConverter (AtomicLong.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (BigDecimal.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (BigInteger.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Byte.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Double.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Float.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Integer.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Long.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Short.class, AtomicInteger.class, aConverterNumberToAtomicInteger);
    aRegistry.registerTypeConverter (Boolean.class, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (@Nonnull final Object aSource)
      {
        return new AtomicInteger (((Boolean) aSource).booleanValue () ? 1 : 0);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (@Nonnull final Object aSource)
      {
        return new AtomicInteger (((AtomicBoolean) aSource).get () ? 1 : 0);
      }
    });
    aRegistry.registerTypeConverter (Character.class, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (@Nonnull final Object aSource)
      {
        return new AtomicInteger (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToAtomicInteger = new ITypeConverter ()
    {
      public AtomicInteger convert (@Nonnull final Object aSource)
      {
        final Integer aInt = StringHelper.parseIntObj (aSource.toString (), null);
        return aInt == null ? null : new AtomicInteger (aInt.intValue ());
      }
    };
    aRegistry.registerTypeConverter (String.class, AtomicInteger.class, aConverterStringToAtomicInteger);
    aRegistry.registerTypeConverter (StringBuilder.class, AtomicInteger.class, aConverterStringToAtomicInteger);
    aRegistry.registerTypeConverter (StringBuffer.class, AtomicInteger.class, aConverterStringToAtomicInteger);

    // to AtomicLong
    final ITypeConverter aConverterNumberToAtomicLong = new ITypeConverter ()
    {
      public AtomicLong convert (@Nonnull final Object aSource)
      {
        return new AtomicLong (((Number) aSource).longValue ());
      }
    };
    aRegistry.registerTypeConverter (AtomicInteger.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (BigDecimal.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (BigInteger.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Byte.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Double.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Float.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Integer.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Long.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Short.class, AtomicLong.class, aConverterNumberToAtomicLong);
    aRegistry.registerTypeConverter (Boolean.class, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (@Nonnull final Object aSource)
      {
        return new AtomicLong (((Boolean) aSource).booleanValue () ? 1L : 0L);
      }
    });
    aRegistry.registerTypeConverter (AtomicBoolean.class, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (@Nonnull final Object aSource)
      {
        return new AtomicLong (((AtomicBoolean) aSource).get () ? 1L : 0L);
      }
    });
    aRegistry.registerTypeConverter (Character.class, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (@Nonnull final Object aSource)
      {
        return new AtomicLong (((Character) aSource).charValue ());
      }
    });
    final ITypeConverter aConverterStringToAtomicLong = new ITypeConverter ()
    {
      public AtomicLong convert (@Nonnull final Object aSource)
      {
        final Long aLong = StringHelper.parseLongObj (aSource.toString (), null);
        return aLong == null ? null : new AtomicLong (aLong.longValue ());
      }
    };
    aRegistry.registerTypeConverter (String.class, AtomicLong.class, aConverterStringToAtomicLong);
    aRegistry.registerTypeConverter (StringBuilder.class, AtomicLong.class, aConverterStringToAtomicLong);
    aRegistry.registerTypeConverter (StringBuffer.class, AtomicLong.class, aConverterStringToAtomicLong);

    // toList<?>
    final ITypeConverter aConverterObjectToList = new ITypeConverter ()
    {
      public List <?> convert (@Nonnull final Object aSource)
      {
        final List <Object> ret = new ArrayList <Object> ();
        ret.add (aSource);
        return ret;
      }
    };
    aRegistry.registerTypeConverter (AtomicBoolean.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (AtomicInteger.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (AtomicLong.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (BigDecimal.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (BigInteger.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Boolean.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Byte.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Character.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Double.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Float.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Integer.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Long.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (Short.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (String.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (StringBuilder.class, List.class, aConverterObjectToList);
    aRegistry.registerTypeConverter (StringBuffer.class, List.class, aConverterObjectToList);

    // to Set<?>
    final ITypeConverter aConverterObjectToSet = new ITypeConverter ()
    {
      public Set <?> convert (@Nonnull final Object aSource)
      {
        final Set <Object> ret = new HashSet <Object> ();
        ret.add (aSource);
        return ret;
      }
    };
    aRegistry.registerTypeConverter (AtomicBoolean.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (AtomicInteger.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (AtomicLong.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (BigDecimal.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (BigInteger.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Boolean.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Byte.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Character.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Double.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Float.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Integer.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Long.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (Short.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (String.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (StringBuilder.class, Set.class, aConverterObjectToSet);
    aRegistry.registerTypeConverter (StringBuffer.class, Set.class, aConverterObjectToSet);

    // StringBuilder
    final ITypeConverter aConverterObjectToStringBuilder = new ITypeConverter ()
    {
      public StringBuilder convert (@Nonnull final Object aSource)
      {
        return new StringBuilder (TypeConverter.convertIfNecessary (aSource, String.class));
      }
    };
    aRegistry.registerTypeConverter (AtomicBoolean.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (AtomicInteger.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (AtomicLong.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (BigDecimal.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (BigInteger.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Boolean.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Byte.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Character.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Double.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Float.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Integer.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Long.class, StringBuilder.class, aConverterObjectToStringBuilder);
    aRegistry.registerTypeConverter (Short.class, StringBuilder.class, aConverterObjectToStringBuilder);
    final ITypeConverter aConverterStringToStringBuilder = new ITypeConverter ()
    {
      public StringBuilder convert (@Nonnull final Object aSource)
      {
        return new StringBuilder (aSource.toString ());
      }
    };
    aRegistry.registerTypeConverter (String.class, StringBuilder.class, aConverterStringToStringBuilder);
    aRegistry.registerTypeConverter (StringBuffer.class, StringBuilder.class, aConverterStringToStringBuilder);

    // StringBuffer
    final ITypeConverter aConverterObjectToStringBuffer = new ITypeConverter ()
    {
      public StringBuffer convert (@Nonnull final Object aSource)
      {
        return new StringBuffer (TypeConverter.convertIfNecessary (aSource, String.class));
      }
    };
    aRegistry.registerTypeConverter (AtomicBoolean.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (AtomicInteger.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (AtomicLong.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (BigDecimal.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (BigInteger.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Boolean.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Byte.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Character.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Double.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Float.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Integer.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Long.class, StringBuffer.class, aConverterObjectToStringBuffer);
    aRegistry.registerTypeConverter (Short.class, StringBuffer.class, aConverterObjectToStringBuffer);
    final ITypeConverter aConverterStringToStringBuffer = new ITypeConverter ()
    {
      public StringBuffer convert (@Nonnull final Object aSource)
      {
        return new StringBuffer (aSource.toString ());
      }
    };
    aRegistry.registerTypeConverter (String.class, StringBuffer.class, aConverterStringToStringBuffer);
    aRegistry.registerTypeConverter (StringBuilder.class, StringBuffer.class, aConverterStringToStringBuffer);

    // Enum
    aRegistry.registerTypeConverter (Enum.class, String.class, new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        // We need to append the Enum class name, otherwise we cannot resolve
        // it! Use the colon as it is not allowed in class names.
        return aSource.getClass ().getName () + ':' + ((Enum <?>) aSource).name ();
      }
    });
    aRegistry.registerTypeConverter (String.class, Enum.class, new ITypeConverterUpCast ()
    {
      public Enum <?> convert (@Nonnull final Object aSource)
      {
        // Split class name and enum value name
        final String [] aParts = RegExHelper.getSplitToArray ((String) aSource, ":", 2);
        try
        {
          // Resolve any enum class
          // Note: The explicit EChange is just here, because an explicit enum
          // type is needed. It must of course not only be EChange :)
          final Class <EChange> aClass = GenericReflection.getClassFromName (aParts[0]);

          // And look up the element by name
          return Enum.valueOf (aClass, aParts[1]);
        }
        catch (final ClassNotFoundException ex)
        {
          return null;
        }
      }
    });

    // boolean[]
    aRegistry.registerTypeConverter (boolean [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Boolean> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newBooleanList ((boolean []) aSource);
      }
    });
    aRegistry.registerTypeConverter (boolean [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Boolean> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newBooleanSet ((boolean []) aSource);
      }
    });
    aRegistry.registerTypeConverter (boolean [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Boolean> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newBooleanOrderedSet ((boolean []) aSource);
      }
    });

    // byte[]
    aRegistry.registerTypeConverter (byte [].class, String.class, new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        return Base64.encodeBytes ((byte []) aSource);
      }
    });
    aRegistry.registerTypeConverter (String.class, byte [].class, new ITypeConverter ()
    {
      public byte [] convert (@Nonnull final Object aSource)
      {
        return Base64Helper.safeDecode ((String) aSource);
      }
    });
    aRegistry.registerTypeConverter (byte [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Byte> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newByteList ((byte []) aSource);
      }
    });
    aRegistry.registerTypeConverter (byte [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Byte> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newByteSet ((byte []) aSource);
      }
    });
    aRegistry.registerTypeConverter (byte [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Byte> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newByteOrderedSet ((byte []) aSource);
      }
    });

    // char[]
    aRegistry.registerTypeConverter (char [].class, String.class, new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        return new String ((char []) aSource);
      }
    });
    aRegistry.registerTypeConverter (String.class, char [].class, new ITypeConverter ()
    {
      public char [] convert (@Nonnull final Object aSource)
      {
        return ((String) aSource).toCharArray ();
      }
    });
    aRegistry.registerTypeConverter (char [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Character> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newCharList ((char []) aSource);
      }
    });
    aRegistry.registerTypeConverter (char [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Character> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newCharSet ((char []) aSource);
      }
    });
    aRegistry.registerTypeConverter (char [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Character> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newCharOrderedSet ((char []) aSource);
      }
    });

    // double[]
    aRegistry.registerTypeConverter (double [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Double> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newDoubleList ((double []) aSource);
      }
    });
    aRegistry.registerTypeConverter (double [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Double> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newDoubleSet ((double []) aSource);
      }
    });
    aRegistry.registerTypeConverter (double [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Double> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newDoubleOrderedSet ((double []) aSource);
      }
    });

    // float[]
    aRegistry.registerTypeConverter (float [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Float> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newFloatList ((float []) aSource);
      }
    });
    aRegistry.registerTypeConverter (float [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Float> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newFloatSet ((float []) aSource);
      }
    });
    aRegistry.registerTypeConverter (float [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Float> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newFloatOrderedSet ((float []) aSource);
      }
    });

    // int[]
    aRegistry.registerTypeConverter (int [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Integer> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newIntList ((int []) aSource);
      }
    });
    aRegistry.registerTypeConverter (int [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Integer> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newIntSet ((int []) aSource);
      }
    });
    aRegistry.registerTypeConverter (int [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Integer> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newIntOrderedSet ((int []) aSource);
      }
    });

    // long[]
    aRegistry.registerTypeConverter (long [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Long> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newLongList ((long []) aSource);
      }
    });
    aRegistry.registerTypeConverter (long [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Long> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newLongSet ((long []) aSource);
      }
    });
    aRegistry.registerTypeConverter (long [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Long> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newLongOrderedSet ((long []) aSource);
      }
    });

    // short[]
    aRegistry.registerTypeConverter (short [].class, ArrayList.class, new ITypeConverter ()
    {
      public List <Short> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newShortList ((short []) aSource);
      }
    });
    aRegistry.registerTypeConverter (short [].class, HashSet.class, new ITypeConverter ()
    {
      public Set <Short> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newShortSet ((short []) aSource);
      }
    });
    aRegistry.registerTypeConverter (short [].class, LinkedHashSet.class, new ITypeConverter ()
    {
      public Set <Short> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newShortOrderedSet ((short []) aSource);
      }
    });
  }
}
