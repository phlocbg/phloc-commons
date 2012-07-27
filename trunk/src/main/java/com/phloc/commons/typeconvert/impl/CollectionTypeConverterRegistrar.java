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
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;

/**
 * Register the base type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class CollectionTypeConverterRegistrar implements ITypeConverterRegistrarSPI
{
  /**
   * Register all type converters for the base types:<br>
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

    // to List<?>
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
                                                       String.class,
                                                       StringBuilder.class,
                                                       StringBuffer.class }, List.class, new ITypeConverter ()
    {
      public List <?> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newList (aSource);
      }
    });

    // to Set<?>
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
                                                       String.class,
                                                       StringBuilder.class,
                                                       StringBuffer.class }, Set.class, new ITypeConverter ()
    {
      public Set <?> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newSet (aSource);
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
