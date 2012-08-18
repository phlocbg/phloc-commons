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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.base64.Base64;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;
import com.phloc.commons.typeconvert.rule.AbstractTypeConverterRuleAnySourceFixedDestination;

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
   * Register type converters for the collection types:<br>
   * <ul>
   * <li>ArrayList</li>
   * <li>Vector</li>
   * <li>LinkedList</li>
   * <li>CopyOnWriteArrayList</li>
   * <li>List</li>
   * <li>HashSet</li>
   * <li>TreeSet</li>
   * <li>LinkedHashSet</li>
   * <li>CopyOnWriteArraySet</li>
   * <li>Set</li>
   * </ul>
   */
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    // to ArrayList<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (ArrayList.class)
    {
      public ArrayList <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new ArrayList <Object> ((Collection <?>) aSource);
        final ArrayList <Object> ret = new ArrayList <Object> (1);
        ret.add (aSource);
        return ret;
      }
    });

    // to Vector<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (Vector.class)
    {
      public Vector <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new Vector <Object> ((Collection <?>) aSource);
        final Vector <Object> ret = new Vector <Object> (1);
        ret.add (aSource);
        return ret;
      }
    });

    // to LinkedList<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (LinkedList.class)
    {
      public LinkedList <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new LinkedList <Object> ((Collection <?>) aSource);
        final LinkedList <Object> ret = new LinkedList <Object> ();
        ret.add (aSource);
        return ret;
      }
    });

    // to CopyOnWriteArrayList<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (CopyOnWriteArrayList.class)
    {
      public CopyOnWriteArrayList <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new CopyOnWriteArrayList <Object> ((Collection <?>) aSource);
        final CopyOnWriteArrayList <Object> ret = new CopyOnWriteArrayList <Object> ();
        ret.add (aSource);
        return ret;
      }
    });

    // to List<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (List.class)
    {
      public List <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return ContainerHelper.newList ((Collection <?>) aSource);
        return ContainerHelper.newList (aSource);
      }
    });

    // to TreeSet<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (TreeSet.class)
    {
      public TreeSet <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new TreeSet <Object> ((Collection <?>) aSource);
        final TreeSet <Object> ret = new TreeSet <Object> ();
        ret.add (aSource);
        return ret;
      }
    });

    // to LinkedHashSet<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (LinkedHashSet.class)
    {
      public LinkedHashSet <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new LinkedHashSet <Object> ((Collection <?>) aSource);
        final LinkedHashSet <Object> ret = new LinkedHashSet <Object> (1);
        ret.add (aSource);
        return ret;
      }
    });

    // to CopyOnWriteArraySet<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (CopyOnWriteArraySet.class)
    {
      public CopyOnWriteArraySet <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return new CopyOnWriteArraySet <Object> ((Collection <?>) aSource);
        final CopyOnWriteArraySet <Object> ret = new CopyOnWriteArraySet <Object> ();
        ret.add (aSource);
        return ret;
      }
    });

    // to Set<?>
    aRegistry.registerTypeConverterRule (new AbstractTypeConverterRuleAnySourceFixedDestination (Set.class)
    {
      public Set <?> convert (@Nonnull final Object aSource)
      {
        if (aSource instanceof Collection <?>)
          return ContainerHelper.newSet ((Collection <?>) aSource);
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
    aRegistry.registerTypeConverter (boolean [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Boolean> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newBooleanVector ((boolean []) aSource);
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
    aRegistry.registerTypeConverter (boolean [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Boolean> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newBooleanSortedSet ((boolean []) aSource);
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
    aRegistry.registerTypeConverter (byte [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Byte> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newByteVector ((byte []) aSource);
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
    aRegistry.registerTypeConverter (byte [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Byte> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newByteSortedSet ((byte []) aSource);
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
    aRegistry.registerTypeConverter (char [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Character> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newCharVector ((char []) aSource);
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
    aRegistry.registerTypeConverter (char [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Character> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newCharSortedSet ((char []) aSource);
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
    aRegistry.registerTypeConverter (double [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Double> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newDoubleVector ((double []) aSource);
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
    aRegistry.registerTypeConverter (double [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Double> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newDoubleSortedSet ((double []) aSource);
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
    aRegistry.registerTypeConverter (float [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Float> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newFloatVector ((float []) aSource);
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
    aRegistry.registerTypeConverter (float [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Float> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newFloatSortedSet ((float []) aSource);
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
    aRegistry.registerTypeConverter (int [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Integer> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newIntVector ((int []) aSource);
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
    aRegistry.registerTypeConverter (int [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Integer> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newIntSortedSet ((int []) aSource);
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
    aRegistry.registerTypeConverter (long [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Long> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newLongVector ((long []) aSource);
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
    aRegistry.registerTypeConverter (long [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Long> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newLongSortedSet ((long []) aSource);
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
    aRegistry.registerTypeConverter (short [].class, Vector.class, new ITypeConverter ()
    {
      public Vector <Short> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newShortVector ((short []) aSource);
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
    aRegistry.registerTypeConverter (short [].class, TreeSet.class, new ITypeConverter ()
    {
      public Set <Short> convert (@Nonnull final Object aSource)
      {
        return ContainerHelper.newShortSortedSet ((short []) aSource);
      }
    });
  }
}
