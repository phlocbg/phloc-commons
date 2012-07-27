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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
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
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, Boolean.class, new ITypeConverter ()
    {
      public Boolean convert (@Nonnull final Object aSource)
      {
        return Boolean.valueOf (((Number) aSource).intValue () != 0);
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Boolean.class,
                                     new ITypeConverter ()
                                     {
                                       public Boolean convert (@Nonnull final Object aSource)
                                       {
                                         return Boolean.valueOf (aSource.toString ());
                                       }
                                     });

    // to Byte
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, Byte.class, new ITypeConverter ()
    {
      public Byte convert (@Nonnull final Object aSource)
      {
        return Byte.valueOf (((Number) aSource).byteValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Byte.class,
                                     new ITypeConverter ()
                                     {
                                       public Byte convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseByteObj (aSource);
                                       }
                                     });

    // to Character
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, Character.class, new ITypeConverter ()
    {
      public Character convert (@Nonnull final Object aSource)
      {
        return Character.valueOf ((char) ((Number) aSource).intValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Character.class,
                                     new ITypeConverter ()
                                     {
                                       public Character convert (@Nonnull final Object aSource)
                                       {
                                         final String sSource = aSource.toString ();
                                         return sSource.length () == 1 ? Character.valueOf (sSource.charAt (0)) : null;
                                       }
                                     });

    // to Double
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, Double.class, new ITypeConverter ()
    {
      public Double convert (@Nonnull final Object aSource)
      {
        return Double.valueOf (((Number) aSource).doubleValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Double.class,
                                     new ITypeConverter ()
                                     {
                                       public Double convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseDoubleObj (aSource.toString (), null);
                                       }
                                     });

    // to Float
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, Float.class, new ITypeConverter ()
    {
      public Float convert (@Nonnull final Object aSource)
      {
        return Float.valueOf (((Number) aSource).floatValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Float.class,
                                     new ITypeConverter ()
                                     {
                                       public Float convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseFloatObj (aSource.toString (), null);
                                       }
                                     });

    // to Integer
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Long.class,
                                                       Short.class }, Integer.class, new ITypeConverter ()
    {
      public Integer convert (@Nonnull final Object aSource)
      {
        return Integer.valueOf (((Number) aSource).intValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Integer.class,
                                     new ITypeConverter ()
                                     {
                                       public Integer convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseIntObj (aSource.toString (), null);
                                       }
                                     });

    // to Long
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Short.class }, Long.class, new ITypeConverter ()
    {
      public Long convert (@Nonnull final Object aSource)
      {
        return Long.valueOf (((Number) aSource).longValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Long.class,
                                     new ITypeConverter ()
                                     {
                                       public Long convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseLongObj (aSource.toString (), null);
                                       }
                                     });

    // to Short
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class }, Short.class, new ITypeConverter ()
    {
      public final Short convert (@Nonnull final Object aSource)
      {
        return Short.valueOf (((Number) aSource).shortValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     Short.class,
                                     new ITypeConverter ()
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
                                     });

    // to String
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicBoolean.class,
                                                       AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Boolean.class,
                                                       Byte.class,
                                                       Character.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class,
                                                       StringBuilder.class,
                                                       StringBuffer.class }, String.class, new ITypeConverter ()
    {
      public String convert (@Nonnull final Object aSource)
      {
        return aSource.toString ();
      }
    });

    // to BigDecimal
    aRegistry.registerTypeConverter (BigInteger.class, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return new BigDecimal ((BigInteger) aSource);
      }
    });
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, BigDecimal.class, new ITypeConverter ()
    {
      public BigDecimal convert (@Nonnull final Object aSource)
      {
        return BigDecimal.valueOf (((Number) aSource).doubleValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     BigDecimal.class,
                                     new ITypeConverter ()
                                     {
                                       public BigDecimal convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseBigDecimal (aSource.toString ());
                                       }
                                     });

    // to BigInteger
    aRegistry.registerTypeConverter (BigDecimal.class, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return ((BigDecimal) aSource).toBigInteger ();
      }
    });
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, BigInteger.class, new ITypeConverter ()
    {
      public BigInteger convert (@Nonnull final Object aSource)
      {
        return BigInteger.valueOf (((Number) aSource).longValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     BigInteger.class,
                                     new ITypeConverter ()
                                     {
                                       public BigInteger convert (@Nonnull final Object aSource)
                                       {
                                         return StringHelper.parseBigInteger (aSource.toString ());
                                       }
                                     });

    // to AtomicBoolean
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Boolean.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class,
                                                       Character.class,
                                                       String.class,
                                                       StringBuilder.class,
                                                       StringBuffer.class }, AtomicBoolean.class, new ITypeConverter ()
    {
      public AtomicBoolean convert (@Nonnull final Object aSource)
      {
        return new AtomicBoolean (TypeConverter.convertIfNecessary (aSource, Boolean.class).booleanValue ());
      }
    });

    // to AtomicInteger
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, AtomicInteger.class, new ITypeConverter ()
    {
      public AtomicInteger convert (@Nonnull final Object aSource)
      {
        return new AtomicInteger (((Number) aSource).intValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     AtomicInteger.class,
                                     new ITypeConverter ()
                                     {
                                       public AtomicInteger convert (@Nonnull final Object aSource)
                                       {
                                         final Integer aInt = StringHelper.parseIntObj (aSource.toString (), null);
                                         return aInt == null ? null : new AtomicInteger (aInt.intValue ());
                                       }
                                     });

    // to AtomicLong
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicInteger.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Byte.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, AtomicLong.class, new ITypeConverter ()
    {
      public AtomicLong convert (@Nonnull final Object aSource)
      {
        return new AtomicLong (((Number) aSource).longValue ());
      }
    });
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
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class, StringBuffer.class },
                                     AtomicLong.class,
                                     new ITypeConverter ()
                                     {
                                       public AtomicLong convert (@Nonnull final Object aSource)
                                       {
                                         final Long aLong = StringHelper.parseLongObj (aSource.toString (), null);
                                         return aLong == null ? null : new AtomicLong (aLong.longValue ());
                                       }
                                     });

    // StringBuilder
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicBoolean.class,
                                                       AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Boolean.class,
                                                       Byte.class,
                                                       Character.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, StringBuilder.class, new ITypeConverter ()
    {
      public StringBuilder convert (@Nonnull final Object aSource)
      {
        return new StringBuilder (TypeConverter.convertIfNecessary (aSource, String.class));
      }
    });
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuffer.class },
                                     StringBuilder.class,
                                     new ITypeConverter ()
                                     {
                                       public StringBuilder convert (@Nonnull final Object aSource)
                                       {
                                         return new StringBuilder ((CharSequence) aSource);
                                       }
                                     });

    // StringBuffer
    aRegistry.registerTypeConverter (new Class <?> [] { AtomicBoolean.class,
                                                       AtomicInteger.class,
                                                       AtomicLong.class,
                                                       BigDecimal.class,
                                                       BigInteger.class,
                                                       Boolean.class,
                                                       Byte.class,
                                                       Character.class,
                                                       Double.class,
                                                       Float.class,
                                                       Integer.class,
                                                       Long.class,
                                                       Short.class }, StringBuffer.class, new ITypeConverter ()
    {
      public StringBuffer convert (@Nonnull final Object aSource)
      {
        return new StringBuffer (TypeConverter.convertIfNecessary (aSource, String.class));
      }
    });
    aRegistry.registerTypeConverter (new Class <?> [] { String.class, StringBuilder.class },
                                     StringBuffer.class,
                                     new ITypeConverter ()
                                     {
                                       public StringBuffer convert (@Nonnull final Object aSource)
                                       {
                                         return new StringBuffer ((CharSequence) aSource);
                                       }
                                     });

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
  }
}
