/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.multimap.AbstractMultiHashMapSetBased;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasName;

/**
 * This is a helper class for accessing containers with explicit locking
 *
 * @author Boris Gregorcic
 */
public final class LockedContainerHelper
{
  private LockedContainerHelper ()
  {
    // private
  }

  /**
   * Looks for the map entry with the passed key and return the corresponding
   * object
   *
   * @param <K>
   *        map key type
   * @param <T>
   *        map value type
   * @param aKey
   *        The key of the object (e.g. ID or name)
   * @param aObjectsByKey
   *        A map containing all available objects mapped to their key
   * @param aLock
   *        The monitor to use
   * @return The Object or <code>null</code> if not found
   */
  @Nullable
  public static <K, T> T getByKey (final K aKey,
                                   @Nonnull final Map <K, T> aObjectsByKey,
                                   @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return aObjectsByKey.get (aKey);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Performs a lookup using
   * {@link #getByKey(Object, Map, ReentrantReadWriteLock)} returning the result
   * casted according to the requested type
   *
   * @param <K>
   *        map key type
   * @param <T>
   *        map value type
   * @param <R>
   *        desired return type
   * @param aKey
   *        The key of the object (e.g. ID or name)
   * @param aObjectsByKey
   *        A map containing all available objects mapped to their key
   * @param aLock
   *        The monitor to use
   * @return The Object or <code>null</code> if not found
   */
  @SuppressWarnings ("unchecked")
  public static <K, T, R> R getByKeyCasted (final K aKey,
                                            @Nonnull final Map <K, T> aObjectsByKey,
                                            @Nonnull final ReentrantReadWriteLock aLock)
  {
    return (R) getByKey (aKey, aObjectsByKey, aLock);
  }

  /**
   * Returns a copy of the passed container performed in a read locked context
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to copy
   * @param aLock
   *        The lock to use
   * @return An copy of the container
   */
  @ReturnsMutableCopy
  public static <T> Collection <T> getCollection (@Nonnull final Collection <T> aObjects,
                                                  @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the passed container as a list performed in a read locked
   * context
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to copy
   * @param aLock
   *        The lock to use
   * @return A list copy of the container
   */
  @ReturnsMutableCopy
  public static <T> List <T> getList (@Nonnull final Collection <T> aObjects,
                                      @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the passed map in a read locked context
   *
   * @param <K>
   *        map key type
   * @param <V>
   *        map value type
   * @param aObjects
   *        The map to copy
   * @param aLock
   *        The lock to use
   * @return A map copy
   */
  @ReturnsMutableCopy
  public static <K, V> Map <K, V> getMap (@Nonnull final Map <K, V> aObjects,
                                          @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newMap (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns the element with the specified index from the passed list in a read
   * locked context
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The list to access
   * @param nIndex
   *        The index of the element to retrieve
   * @param aLock
   *        The lock to use
   * @return The element with the specified index or null
   */
  public static <T> T get (@Nonnull final List <T> aObjects,
                           final int nIndex,
                           @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      try
      {
        return aObjects.get (nIndex);
      }
      catch (final IndexOutOfBoundsException aEx)
      {
        return null;
      }
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the values in the passed map performed in a read locked
   * context
   *
   * @param <K>
   *        map key type
   * @param <V>
   *        map value type
   * @param aObjects
   *        The map containing the values to retrieve
   * @param aLock
   *        The lock to use
   * @return A copy of the map values
   */
  @ReturnsMutableCopy
  public static <K, V> Set <V> getValues (@Nonnull final Map <K, V> aObjects,
                                          @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (aObjects.values ());
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the keys in the passed map performed in a read locked
   * context
   *
   * @param <K>
   *        map key type
   * @param <V>
   *        map value type
   * @param aObjects
   *        The map containing the values to retrieve
   * @param aLock
   *        The lock to use
   * @return The keys in the passed map
   */
  @ReturnsMutableCopy
  public static <K, V> Collection <K> getKeys (@Nonnull final Map <K, V> aObjects,
                                               @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      if (aObjects instanceof LinkedHashMap)
      {
        return ContainerHelper.newList (aObjects.keySet ());
      }
      return ContainerHelper.newSet (aObjects.keySet ());
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the entry in the passed multi-map corresponding to the
   * passed key, performing the access in a read locked context
   *
   * @param <K>
   *        map key type
   * @param <T>
   *        map value type
   * @param aMap
   *        The container to copy
   * @param aKey
   *        The map entry key
   * @param aLock
   *        The lock to use
   * @return A copy of the entry
   */
  @ReturnsMutableCopy
  @Nonnull
  public static <K, T> Set <T> getMultiMapEntry (@Nonnull final AbstractMultiHashMapSetBased <K, T> aMap,
                                                 @Nonnull final K aKey,
                                                 @Nonnull final ReentrantReadWriteLock aLock)
  {
    if (aMap == null)
    {
      throw new NullPointerException ("aMap"); //$NON-NLS-1$
    }
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (aMap.get (aKey));
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the passed container performed in a read locked context
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to copy
   * @param aLock
   *        The lock to use
   * @return An copy of the container
   */
  @ReturnsMutableCopy
  public static <T> Set <T> getSet (@Nonnull final Set <T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a copy of the passed ordered container performed in a read locked
   * context
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to copy
   * @param aLock
   *        The lock to use
   * @return An copy of the container
   */
  @ReturnsMutableCopy
  public static <T> Set <T> getOrderedSet (@Nonnull final Set <T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newOrderedSet (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Returns a mutable copy of the passed stack performed in a read locked
   * context
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        the stack to copy
   * @param aLock
   *        the lock to use
   * @return A mutable copied representation of the passed stack
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <T> Stack <T> getStack (@Nonnull final Stack <T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    final Stack <T> aCopyStack = new Stack <T> ();
    aLock.readLock ().lock ();
    try
    {
      aCopyStack.addAll (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
    return aCopyStack;
  }

  /**
   * Performs a simple remove operation of the passed collection using the
   * passed object, locking the operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container from which to remove
   * @param aObject
   *        The element to remove
   * @param aLock
   *        The lock to use
   * @return the boolean result value of the remove operation
   */
  public static <T> boolean remove (@Nonnull final Collection <T> aObjects,
                                    @Nonnull final T aObject,
                                    @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      if (aObject != null)
      {
        return aObjects.remove (aObject);
      }
      return false;
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Checks if the passed item is contained in the passed collection in a thread
   * safe manner
   *
   * @param <T>
   *        value type
   * @param aCollection
   *        The container to check
   * @param aItem
   *        The element to check
   * @param aLock
   *        The lock to use
   * @return whether or not the element is contained
   */
  public static <T> boolean contains (@Nonnull final Collection <T> aCollection,
                                      @Nonnull final T aItem,
                                      @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      if (aItem != null)
      {
        return aCollection.contains (aItem);
      }
      return false;
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Checks if all the passed items is contained in the passed collection in a
   * thread safe manner
   *
   * @param <T>
   *        value type
   * @param aCollection
   *        The container to check
   * @param aItems
   *        The elements to check
   * @param aLock
   *        The lock to use
   * @return whether or not all elements are contained
   */
  public static <T> boolean containsAll (@Nullable final Collection <T> aCollection,
                                         @Nonnull final Collection <T> aItems,
                                         @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      if (aCollection != null)
      {
        return aCollection.containsAll (aItems);
      }
      return false;
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Performs a simple clear operation on the passed collection, locking the
   * operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to clear
   * @param aLock
   *        The lock to use
   */
  public static <T> void clear (@Nonnull final Collection <T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      aObjects.clear ();
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Performs a simple add operation of the passed collection using the passed
   * object, locking the operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to which to add
   * @param aObject
   *        The element to add
   * @param aLock
   *        The lock to use
   * @return the boolean result value of the add operation
   */
  public static <T> boolean add (@Nonnull final Collection <T> aObjects,
                                 @Nonnull final T aObject,
                                 @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      if (aObject != null)
      {
        return aObjects.add (aObject);
      }
      return false;
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Performs a simple addAll operation of the passed collection using the
   * passed objects, locking the operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container to which to add
   * @param aObjectsToAdd
   *        The element to add
   * @param aLock
   *        The lock to use
   * @return the boolean result value of the add operation
   */
  public static <T> boolean addAll (@Nonnull final Collection <T> aObjects,
                                    @Nullable final Collection <T> aObjectsToAdd,
                                    @Nonnull final ReentrantReadWriteLock aLock)
  {
    if (aObjects != null && aObjectsToAdd != null)
    {
      aLock.writeLock ().lock ();
      try
      {
        return aObjects.addAll (aObjectsToAdd);
      }
      finally
      {
        aLock.writeLock ().unlock ();
      }
    }
    return false;
  }

  /**
   * Performs a simple removeAll operation of the passed collection using the
   * passed objects, locking the operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The container from which to remove
   * @param aObjectsToRemove
   *        The elements to remove
   * @param aLock
   *        The lock to use
   * @return the boolean result value of the remove operation
   */
  public static <T> boolean removeAll (@Nonnull final Collection <T> aObjects,
                                       @Nullable final Collection <T> aObjectsToRemove,
                                       @Nonnull final ReentrantReadWriteLock aLock)
  {
    if (aObjects != null && aObjectsToRemove != null)
    {
      aLock.writeLock ().lock ();
      try
      {
        return aObjects.removeAll (aObjectsToRemove);
      }
      finally
      {
        aLock.writeLock ().unlock ();
      }
    }
    return false;
  }

  /**
   * Performs a simple put action on a map, locking the operation using the
   * passed lock
   *
   * @param <K>
   *        key type
   * @param <V>
   *        value type
   * @param aMap
   *        The map in which to put
   * @param aKey
   *        The key
   * @param aValue
   *        The value to put
   * @param aLock
   *        The lock to use
   * @return the usual return value of the underlying map
   */
  public static <K, V> V put (@Nonnull final Map <K, V> aMap,
                              @Nonnull final K aKey,
                              @Nonnull final V aValue,
                              @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      if (aMap != null)
      {
        return aMap.put (aKey, aValue);
      }
      return null;
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Tries to put all entries in the seconds map into the first map
   *
   * @param <K>
   *        key type
   * @param <V>
   *        value type
   * @param aMap
   *        The main map that should be modified
   * @param aEntries
   *        A map containing the entries that should be put to the first map,
   *        may be <code>null</code>
   * @param aLock
   *        The lock to use
   */
  public static <K, V> void putAll (@Nonnull final Map <K, V> aMap,
                                    @Nullable final Map <K, V> aEntries,
                                    @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      if (aMap != null && aEntries != null)
      {
        aMap.putAll (aEntries);
      }
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Performs a simple remove action on a map, locking the operation using the
   * passed lock
   *
   * @param <K>
   *        key type
   * @param <V>
   *        value type
   * @param aMap
   *        The map from which to remove
   * @param aKey
   *        The key
   * @param aLock
   *        The lock to use
   * @return the usual return value of the underlying map
   */
  public static <K, V> V remove (@Nonnull final Map <K, V> aMap,
                                 @Nonnull final K aKey,
                                 @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      if (aMap != null)
      {
        return aMap.remove (aKey);
      }
      return null;
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Performs a simple clear operation on the passed map, locking the operation
   * using the passed lock
   *
   * @param <K>
   *        key type
   * @param <V>
   *        value type
   * @param aObjects
   *        The map to clear
   * @param aLock
   *        The lock to use
   */
  public static <K, V> void clear (@Nonnull final Map <K, V> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.writeLock ().lock ();
    try
    {
      aObjects.clear ();
    }
    finally
    {
      aLock.writeLock ().unlock ();
    }
  }

  /**
   * Performs a simple check on the passed collection to see if it is empty,
   * locking the operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The map to check
   * @param aLock
   *        The lock to use
   * @return the boolean result value of the add operation
   */
  public static <T> boolean isEmpty (@Nonnull final Collection <T> aObjects,
                                     @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.isEmpty (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Performs a simple check on the passed map to see if it is empty, locking
   * the operation using the passed lock
   *
   * @param <K>
   *        key type
   * @param <V>
   *        value type
   * @param aObjects
   *        The map to check
   * @param aLock
   *        The lock to use
   * @return the boolean result value of the add operation
   */
  public static <K, V> boolean isEmpty (@Nonnull final Map <K, V> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.isEmpty (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * returns the size of the passed map, locking the operation using the passed
   * lock
   *
   * @param <K>
   *        key type
   * @param <V>
   *        value type
   * @param aObjects
   *        The Map to check
   * @param aLock
   *        The lock to use
   * @return the size of the passed map
   */
  public static <K, V> int getSize (@Nonnull final Map <K, V> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getSize (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * returns the size of the passed collection, locking the operation using the
   * passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The collection to check
   * @param aLock
   *        The lock to use
   * @return the size of the passed map
   */
  public static <T> int getSize (@Nonnull final Collection <T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getSize (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * returns the first element of the passed collection, locking the operation
   * using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The collection
   * @param aLock
   *        The lock to use
   * @return the first element of the passed collection, or <code>null</code> if
   *         empty
   */
  public static <T> T getFirst (@Nonnull final Collection <T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getFirstElement (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * returns the value of the first entry in the passed map, locking the
   * operation using the passed lock
   *
   * @param <T>
   *        value type
   * @param aObjects
   *        The map
   * @param aLock
   *        The lock to use
   * @return the first value of the passed map, or <code>null</code> if empty
   */
  public static <T> T getFirstValue (@Nonnull final Map <?, T> aObjects, @Nonnull final ReentrantReadWriteLock aLock)
  {
    aLock.readLock ().lock ();
    try
    {
      return ContainerHelper.getFirstValue (aObjects);
    }
    finally
    {
      aLock.readLock ().unlock ();
    }
  }

  /**
   * Iterates all passed objects and collect their IDs
   *
   * @param aSourceCollection
   *        Objects for which to collect the IDs, must not be <code>null</code>
   * @param aLock
   *        The lock to use
   * @return All IDs (no duplicates)
   */
  @Nonnull
  public static final Set <String> getIDs (@Nonnull final Collection <? extends IHasID <String>> aSourceCollection,
                                           @Nullable final ReentrantReadWriteLock aLock)
  {
    final Set <String> aNames = ContainerHelper.newSet ();
    extractIDs (aSourceCollection, aNames, aLock);
    return aNames;
  }

  /**
   * Iterates all passed objects and collect their IDs
   *
   * @param aSourceCollection
   *        Objects for which to collect the IDs, must not be <code>null</code>
   * @param aLock
   *        The lock to use
   * @return All IDs (no duplicates)
   */
  @Nonnull
  public static final Collection <String> getOrderedIDs (@Nonnull final Collection <? extends IHasID <String>> aSourceCollection,
                                                         @Nullable final ReentrantReadWriteLock aLock)
  {
    final List <String> aNames = ContainerHelper.newList ();
    extractIDs (aSourceCollection, aNames, aLock);
    return aNames;
  }

  @Nonnull
  public static final Collection <String> getOrderedIDs (@Nonnull final Map <?, ? extends IHasID <String>> aSourceMap,
                                                         @Nullable final ReentrantReadWriteLock aLock)
  {
    return getOrderedIDs (aSourceMap == null ? null : aSourceMap.values (), aLock);
  }

  /**
   * Iterates all passed objects and collect their IDs
   *
   * @param aSourceList
   *        Objects for which to collect the IDs, must not be <code>null</code>
   * @param aLock
   *        The lock to use
   * @return All IDs in stable order (may contain duplicates)
   */
  @Nonnull
  public static final List <String> getIDs (@Nonnull final List <? extends IHasID <String>> aSourceList,
                                            @Nullable final ReentrantReadWriteLock aLock)
  {
    final List <String> aNames = ContainerHelper.newList ();
    extractIDs (aSourceList, aNames, aLock);
    return aNames;
  }

  /**
   * Iterates all passed objects and collect their names
   *
   * @param aSourceCollection
   *        Objects for which to collect the names, must not be
   *        <code>null</code>
   * @param aLock
   *        The lock to use
   * @return All names (no duplicates)
   */
  @Nonnull
  public static final Set <String> getNames (@Nonnull final Collection <? extends IHasName> aSourceCollection,
                                             @Nullable final ReentrantReadWriteLock aLock)
  {
    final Set <String> aNames = ContainerHelper.newSet ();
    extractNames (aSourceCollection, aNames, aLock);
    return aNames;
  }

  /**
   * Iterates all passed objects and collect their names
   *
   * @param aSourceList
   *        Objects for which to collect the names, must not be
   *        <code>null</code>
   * @param aLock
   *        The lock to use
   * @return All names in stable order (may contain duplicates)
   */
  @Nonnull
  public static final List <String> getNames (@Nonnull final List <? extends IHasName> aSourceList,
                                              @Nullable final ReentrantReadWriteLock aLock)
  {
    final List <String> aNames = ContainerHelper.newList ();
    extractNames (aSourceList, aNames, aLock);
    return aNames;
  }

  private static final void extractNames (@Nonnull final Collection <? extends IHasName> aSource,
                                          @Nonnull final Collection <String> aTarget,
                                          @Nullable final ReentrantReadWriteLock aLock)
  {
    if (aSource == null)
    {
      throw new NullPointerException ("aSource"); //$NON-NLS-1$
    }
    if (aLock != null)
    {
      aLock.readLock ().lock ();
    }
    try
    {
      for (final IHasName aElement : aSource)
      {
        aTarget.add (aElement.getName ());
      }
    }
    finally
    {
      if (aLock != null)
      {
        aLock.readLock ().unlock ();
      }
    }
  }

  private static final void extractIDs (@Nonnull final Collection <? extends IHasID <String>> aSource,
                                        @Nonnull final Collection <String> aTarget,
                                        @Nullable final ReentrantReadWriteLock aLock)
  {
    if (aSource == null)
    {
      throw new NullPointerException ("aSource"); //$NON-NLS-1$
    }
    if (aLock != null)
    {
      aLock.readLock ().lock ();
    }
    try
    {
      for (final IHasID <String> aElement : aSource)
      {
        aTarget.add (aElement.getID ());
      }
    }
    finally
    {
      if (aLock != null)
      {
        aLock.readLock ().unlock ();
      }
    }
  }
}
