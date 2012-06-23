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
package com.phloc.commons.equals;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import com.phloc.commons.collections.ArrayHelper;

/**
 * A special equals helper class that handles collections and container classes.
 * The different to the reqular {@link EqualsUtils} and
 * {@link EqualsImplementationRegistry} methods is, that the methods contained
 * in here are not strictly obeying to the equals contract, as the
 * implementation classes must not match - only the content is relevant.
 * 
 * @author philip
 */
public final class ContainerEqualsUtils
{
  /**
   * This enum differentiates the different meta container types.
   * 
   * @author philip
   */
  public static enum EContainerType
  {
    /**
     * The list type applies to List and Queue objects, as the can be compared
     * in the regular order.
     */
    LIST,
    /** Sets represent unordered container */
    SET,
    /** Maps are key-value-containers */
    MAP,
    /** Arrays */
    ARRAY;
  }

  private ContainerEqualsUtils ()
  {}

  @Nullable
  public static EContainerType getContainerType (@Nullable final Object aObj)
  {
    if (aObj != null)
    {
      if (aObj instanceof List <?>)
        return EContainerType.LIST;
      if (aObj instanceof Queue <?>)
        return EContainerType.LIST;
      if (aObj instanceof Set <?>)
        return EContainerType.SET;
      if (aObj instanceof Map <?, ?>)
        return EContainerType.MAP;
      if (ArrayHelper.isArray (aObj))
        return EContainerType.ARRAY;
    }
    return null;
  }

  public static boolean isContainerObject (@Nullable final Object aObj)
  {
    return getContainerType (aObj) != null;
  }

  private static boolean _areChildrenEqual (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    if (isContainerObject (aObj1) && isContainerObject (aObj2))
    {
      // It's a nested collection!
      // -> indirect recursive call
      return equals (aObj1, aObj2);
    }

    // Not collections
    return EqualsImplementationRegistry.areEqual (aObj1, aObj2);
  }

  /**
   * Check if the content of the passed containers is equal. If the container
   * itself contains nested containers, this method is invoked recursively. For
   * non-container elements, the
   * {@link EqualsImplementationRegistry#areEqual(Object, Object)} method is
   * invoked to test for equality!
   * 
   * @param aObj1
   *        The first container
   * @param aObj2
   *        The second container
   * @return <code>true</code> if both objects are the same, or if they have the
   *         same meta type and have the same content.
   * @throws IllegalArgumentException
   *         if one of the arguments is not a container!
   */
  public static boolean equals (@Nullable final Object aObj1, @Nullable final Object aObj2)
  {
    // Same object - check first
    if (aObj1 == aObj2)
      return true;

    // Is only one value null?
    if (aObj1 == null || aObj2 == null)
      return false;

    final EContainerType eType1 = getContainerType (aObj1);
    final EContainerType eType2 = getContainerType (aObj2);
    if (eType1 == null)
      throw new IllegalArgumentException ("The first parameter is not a container type: " + aObj1);
    if (eType2 == null)
      throw new IllegalArgumentException ("The second parameter is not a container type: " + aObj2);

    if (!eType1.equals (eType2))
    {
      // Different container types!
      return false;
    }

    switch (eType1)
    {
      case LIST:
      {
        // Valid for List and Queue
        final Collection <?> aCont1 = (Collection <?>) aObj1;
        final Collection <?> aCont2 = (Collection <?>) aObj2;
        if (aCont1.isEmpty () && aCont2.isEmpty ())
          return true;
        if (aCont1.size () != aCont2.size ())
          return false;
        final Iterator <?> aIter1 = aCont1.iterator ();
        final Iterator <?> aIter2 = aCont2.iterator ();
        while (aIter1.hasNext ())
        {
          final Object aChildObj1 = aIter1.next ();
          final Object aChildObj2 = aIter2.next ();
          if (!_areChildrenEqual (aChildObj1, aChildObj2))
            return false;
        }
        return true;
      }
      case SET:
      {
        // Valid for Set
        final Set <?> aCont1 = (Set <?>) aObj1;
        final Set <?> aCont2 = (Set <?>) aObj2;
        if (aCont1.isEmpty () && aCont2.isEmpty ())
          return true;
        if (aCont1.size () != aCont2.size ())
          return false;
        for (final Object aChildObj1 : aCont1)
          if (!aCont2.contains (aChildObj1))
            return false;
        return true;
      }
      case MAP:
      {
        // Valid for Map
        final Map <?, ?> aCont1 = (Map <?, ?>) aObj1;
        final Map <?, ?> aCont2 = (Map <?, ?>) aObj2;
        if (aCont1.isEmpty () && aCont2.isEmpty ())
          return true;
        if (aCont1.size () != aCont2.size ())
          return false;
        for (final Map.Entry <?, ?> aEntry : aCont1.entrySet ())
        {
          final Object aChildObj1 = aEntry.getValue ();
          final Object aChildObj2 = aCont2.get (aEntry.getKey ());
          if (!_areChildrenEqual (aChildObj1, aChildObj2))
            return false;
        }
        return true;
      }
      case ARRAY:
      {
        // No different types possible -> use EqualsImplementationRegistry
        // directly
        return EqualsImplementationRegistry.areEqual (aObj1, aObj2);
      }
      default:
        throw new IllegalStateException ("Unhandled container type " + eType1 + "!");
    }
  }
}
