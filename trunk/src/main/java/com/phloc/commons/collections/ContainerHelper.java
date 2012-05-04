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
package com.phloc.commons.collections;//NOPMD

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.iterate.CombinedEnumeration;
import com.phloc.commons.collections.iterate.EmptyEnumeration;
import com.phloc.commons.collections.iterate.EmptyIterator;
import com.phloc.commons.collections.iterate.EnumerationFromIterator;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.collections.iterate.IterableIteratorFromEnumeration;
import com.phloc.commons.collections.iterate.ReverseListIterator;
import com.phloc.commons.collections.multimap.IMultiMap;
import com.phloc.commons.collections.multimap.IMultiMapSetBased;
import com.phloc.commons.collections.multimap.MultiHashMapHashSetBased;
import com.phloc.commons.compare.ComparatorComparableNullAware;
import com.phloc.commons.compare.ComparatorUtils;

/**
 * Provides various helper methods to handle collections like {@link List},
 * {@link Set} and {@link Map}.
 * 
 * @author philip
 */
@Immutable
public final class ContainerHelper
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ContainerHelper s_aInstance = new ContainerHelper ();

  private ContainerHelper ()
  {}

  @Nonnull
  public static <ELEMENTTYPE> List <? extends ELEMENTTYPE> getNotNull (final List <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newList () : aCollection;
  }

  @Nonnull
  public static <ELEMENTTYPE> Set <? extends ELEMENTTYPE> getNotNull (final Set <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newSet () : aCollection;
  }

  @Nonnull
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <? extends ELEMENTTYPE> getNotNull (final SortedSet <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newSortedSet () : aCollection;
  }

  @Nonnull
  public static <KEYTYPE, VALUETYPE> Map <? extends KEYTYPE, ? extends VALUETYPE> getNotNull (final Map <? extends KEYTYPE, ? extends VALUETYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<KEYTYPE, VALUETYPE> newMap () : aCollection;
  }

  @Nonnull
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <? extends KEYTYPE, ? extends VALUETYPE> getNotNull (final SortedMap <? extends KEYTYPE, ? extends VALUETYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<KEYTYPE, VALUETYPE> newSortedMap () : aCollection;
  }

  @Nullable
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Collection <ELEMENTTYPE> makeUnmodifiable (@Nullable final Collection <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? null : Collections.unmodifiableCollection (aCollection);
  }

  @Nullable
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> makeUnmodifiable (@Nullable final List <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? null : Collections.unmodifiableList (aCollection);
  }

  @Nullable
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> makeUnmodifiable (@Nullable final Set <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? null : Collections.unmodifiableSet (aCollection);
  }

  @Nullable
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> makeUnmodifiable (@Nullable final Map <? extends KEYTYPE, ? extends VALUETYPE> aCollection)
  {
    return aCollection == null ? null : Collections.unmodifiableMap (aCollection);
  }

  @Nullable
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> makeUnmodifiable (@Nullable final SortedSet <ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? null : Collections.unmodifiableSortedSet (aCollection);
  }

  @Nullable
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> makeUnmodifiable (@Nullable final SortedMap <KEYTYPE, ? extends VALUETYPE> aCollection)
  {
    return aCollection == null ? null : Collections.unmodifiableSortedMap (aCollection);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Collection <ELEMENTTYPE> makeUnmodifiableNotNull (@Nonnull final Collection <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newUnmodifiableList ()
                              : Collections.unmodifiableCollection (aCollection);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> makeUnmodifiableNotNull (@Nonnull final List <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newUnmodifiableList ()
                              : Collections.unmodifiableList (aCollection);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> makeUnmodifiableNotNull (@Nonnull final Set <? extends ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newUnmodifiableSet ()
                              : Collections.unmodifiableSet (aCollection);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> makeUnmodifiableNotNull (@Nonnull final Map <? extends KEYTYPE, ? extends VALUETYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<KEYTYPE, VALUETYPE> newUnmodifiableMap ()
                              : Collections.unmodifiableMap (aCollection);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> makeUnmodifiableNotNull (@Nonnull final SortedSet <ELEMENTTYPE> aCollection)
  {
    return aCollection == null ? ContainerHelper.<ELEMENTTYPE> newUnmodifiableSortedSet ()
                              : Collections.unmodifiableSortedSet (aCollection);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> makeUnmodifiableNotNull (@Nonnull final SortedMap <KEYTYPE, ? extends VALUETYPE> aCollection)
  {
    return Collections.unmodifiableSortedMap (aCollection == null
                                                                 ? ContainerHelper.<KEYTYPE, VALUETYPE> newSortedMap ()
                                                                 : aCollection);
  }

  /**
   * Get all elements that are only contained in the first contained, and not in
   * the second. This method implements <code>aCont1 - aCont2</code>.
   * 
   * @param aCont1
   *        The first container. May be <code>null</code> or empty.
   * @param aCont2
   *        The second container. May be <code>null</code> or empty.
   * @return The difference and never <code>null</code>. Returns an empty set,
   *         if the first container is empty. Returns a copy of the first
   *         container, if the second container is empty. Returns
   *         <code>aCont1 - aCont2</code> if both containers are non-empty.
   */
  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> getDifference (@Nullable final Collection <? extends ELEMENTTYPE> aCont1,
                                                               @Nullable final Collection <? extends ELEMENTTYPE> aCont2)
  {
    if (isEmpty (aCont1))
      return newSet ();
    if (isEmpty (aCont2))
      return newSet (aCont1);

    final Set <ELEMENTTYPE> ret = newSet (aCont1);
    ret.removeAll (aCont2);
    return ret;
  }

  /**
   * Get all elements that are contained in the first AND in the second
   * container.
   * 
   * @param aCont1
   *        The first container. May be <code>null</code> or empty.
   * @param aCont2
   *        The second container. May be <code>null</code> or empty.
   * @return An empty set, if either the first or the second container are
   *         empty. Returns a set of elements that are contained in both
   *         containers, if both containers are non-empty. The return value is
   *         never <code>null</code>.
   */
  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> getIntersected (@Nullable final Collection <? extends ELEMENTTYPE> aCont1,
                                                                @Nullable final Collection <? extends ELEMENTTYPE> aCont2)
  {
    if (isEmpty (aCont1))
      return newSet ();
    if (isEmpty (aCont2))
      return newSet ();

    final Set <ELEMENTTYPE> ret = newSet (aCont1);
    ret.retainAll (aCont2);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newMap ()
  {
    return new HashMap <KEYTYPE, VALUETYPE> (0);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newMap (@Nullable final KEYTYPE aKey,
                                                                      @Nullable final VALUETYPE aValue)
  {
    final Map <KEYTYPE, VALUETYPE> ret = new HashMap <KEYTYPE, VALUETYPE> (1);
    ret.put (aKey, aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Map <ELEMENTTYPE, ELEMENTTYPE> newMap (@Nullable final ELEMENTTYPE... aValues)
  {
    if (ArrayHelper.isEmpty (aValues))
      return new HashMap <ELEMENTTYPE, ELEMENTTYPE> (0);

    if ((aValues.length % 2) != 0)
      throw new IllegalArgumentException ("The passed array needs an even number of elements!");

    final Map <ELEMENTTYPE, ELEMENTTYPE> ret = new HashMap <ELEMENTTYPE, ELEMENTTYPE> (aValues.length / 2);
    for (int i = 0; i < aValues.length; i += 2)
      ret.put (aValues[i], aValues[i + 1]);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newMap (@Nullable final KEYTYPE [] aKeys,
                                                                      @Nullable final VALUETYPE [] aValues)
  {
    // Are both empty?
    if (ArrayHelper.isEmpty (aKeys) && ArrayHelper.isEmpty (aValues))
      return new HashMap <KEYTYPE, VALUETYPE> (0);

    // keys OR values may be null here
    if (ArrayHelper.getSize (aKeys) != ArrayHelper.getSize (aValues))
      throw new IllegalArgumentException ("The passed arrays have different length!");

    final Map <KEYTYPE, VALUETYPE> ret = new HashMap <KEYTYPE, VALUETYPE> (aKeys.length);
    for (int i = 0; i < aKeys.length; ++i)
      ret.put (aKeys[i], aValues[i]);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newMap (@Nullable final Collection <? extends KEYTYPE> aKeys,
                                                                      @Nullable final Collection <? extends VALUETYPE> aValues)
  {
    // Are both empty?
    if (isEmpty (aKeys) && isEmpty (aValues))
      return new HashMap <KEYTYPE, VALUETYPE> (0);

    // keys OR values may be null here
    if (getSize (aKeys) != getSize (aValues))
      throw new IllegalArgumentException ("Number of keys is different from number of values");

    final Map <KEYTYPE, VALUETYPE> ret = new HashMap <KEYTYPE, VALUETYPE> (aKeys.size ());
    final Iterator <? extends KEYTYPE> itk = aKeys.iterator ();
    final Iterator <? extends VALUETYPE> itv = aValues.iterator ();
    while (itk.hasNext ())
      ret.put (itk.next (), itv.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newMap (@Nullable final Map <? extends KEYTYPE, ? extends VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return new HashMap <KEYTYPE, VALUETYPE> (0);

    return new HashMap <KEYTYPE, VALUETYPE> (aMap);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newMap (@Nullable final Collection <? extends Map.Entry <KEYTYPE, VALUETYPE>> aCollection)
  {
    if (isEmpty (aCollection))
      return new HashMap <KEYTYPE, VALUETYPE> (0);

    final Map <KEYTYPE, VALUETYPE> ret = new HashMap <KEYTYPE, VALUETYPE> (aCollection.size ());
    for (final Map.Entry <KEYTYPE, VALUETYPE> aEntry : aCollection)
      ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableMap ()
  {
    return Collections.<KEYTYPE, VALUETYPE> emptyMap ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableMap (@Nullable final KEYTYPE aKey,
                                                                                  @Nullable final VALUETYPE aValue)
  {
    return Collections.singletonMap (aKey, aValue);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Map <ELEMENTTYPE, ELEMENTTYPE> newUnmodifiableMap (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newMap (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableMap (@Nullable final KEYTYPE [] aKeys,
                                                                                  @Nullable final VALUETYPE [] aValues)
  {
    return makeUnmodifiable (newMap (aKeys, aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableMap (@Nullable final Collection <? extends KEYTYPE> aKeys,
                                                                                  @Nullable final Collection <? extends VALUETYPE> aValues)
  {
    return makeUnmodifiable (newMap (aKeys, aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableMap (@Nullable final Map <? extends KEYTYPE, ? extends VALUETYPE> aMap)
  {
    return makeUnmodifiable (aMap);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableMap (@Nullable final Collection <? extends Map.Entry <KEYTYPE, VALUETYPE>> aCollection)
  {
    return makeUnmodifiable (newMap (aCollection));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newOrderedMap ()
  {
    return new LinkedHashMap <KEYTYPE, VALUETYPE> (0);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newOrderedMap (@Nullable final KEYTYPE aKey,
                                                                             @Nullable final VALUETYPE aValue)
  {
    final Map <KEYTYPE, VALUETYPE> ret = new LinkedHashMap <KEYTYPE, VALUETYPE> (1);
    ret.put (aKey, aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Map <ELEMENTTYPE, ELEMENTTYPE> newOrderedMap (@Nullable final ELEMENTTYPE... aValues)
  {
    if (ArrayHelper.isEmpty (aValues))
      return new LinkedHashMap <ELEMENTTYPE, ELEMENTTYPE> (0);

    if ((aValues.length % 2) != 0)
      throw new IllegalArgumentException ("The passed array needs an even number of elements!");

    final Map <ELEMENTTYPE, ELEMENTTYPE> ret = new LinkedHashMap <ELEMENTTYPE, ELEMENTTYPE> (aValues.length / 2);
    for (int i = 0; i < aValues.length; i += 2)
      ret.put (aValues[i], aValues[i + 1]);
    return ret;
  }

  /**
   * Retrieve a map that is ordered in the way the parameter arrays are passed
   * in. Note that key and value arrays need to have the same length.
   * 
   * @param <KEYTYPE>
   *        The key type.
   * @param <VALUETYPE>
   *        The value type.
   * @param aKeys
   *        The key array to use. May not be <code>null</code>.
   * @param aValues
   *        The value array to use. May not be <code>null</code>.
   * @return A {@link java.util.LinkedHashMap} containing the passed key-value
   *         entries. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newOrderedMap (@Nullable final KEYTYPE [] aKeys,
                                                                             @Nullable final VALUETYPE [] aValues)
  {
    // Are both empty?
    if (ArrayHelper.isEmpty (aKeys) && ArrayHelper.isEmpty (aValues))
      return new LinkedHashMap <KEYTYPE, VALUETYPE> (0);

    // keys OR values may be null here
    if (ArrayHelper.getSize (aKeys) != ArrayHelper.getSize (aValues))
      throw new IllegalArgumentException ("The passed arrays have different length!");

    final Map <KEYTYPE, VALUETYPE> ret = new LinkedHashMap <KEYTYPE, VALUETYPE> (aKeys.length);
    for (int i = 0; i < aKeys.length; ++i)
      ret.put (aKeys[i], aValues[i]);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newOrderedMap (@Nullable final Collection <? extends KEYTYPE> aKeys,
                                                                             @Nullable final Collection <? extends VALUETYPE> aValues)
  {
    // Are both empty?
    if (isEmpty (aKeys) && isEmpty (aValues))
      return new LinkedHashMap <KEYTYPE, VALUETYPE> (0);

    // keys OR values may be null here
    if (getSize (aKeys) != getSize (aValues))
      throw new IllegalArgumentException ("Number of keys is different from number of values");

    final Map <KEYTYPE, VALUETYPE> ret = new LinkedHashMap <KEYTYPE, VALUETYPE> (aKeys.size ());
    final Iterator <? extends KEYTYPE> itk = aKeys.iterator ();
    final Iterator <? extends VALUETYPE> itv = aValues.iterator ();
    while (itk.hasNext ())
      ret.put (itk.next (), itv.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newOrderedMap (@Nullable final Map <? extends KEYTYPE, ? extends VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return new LinkedHashMap <KEYTYPE, VALUETYPE> (0);

    return new LinkedHashMap <KEYTYPE, VALUETYPE> (aMap);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newOrderedMap (@Nullable final Collection <? extends Map.Entry <KEYTYPE, VALUETYPE>> aCollection)
  {
    if (isEmpty (aCollection))
      return new LinkedHashMap <KEYTYPE, VALUETYPE> (0);

    final Map <KEYTYPE, VALUETYPE> ret = new LinkedHashMap <KEYTYPE, VALUETYPE> (aCollection.size ());
    for (final Map.Entry <KEYTYPE, VALUETYPE> aEntry : aCollection)
      ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableOrderedMap ()
  {
    return Collections.<KEYTYPE, VALUETYPE> emptyMap ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableOrderedMap (@Nullable final KEYTYPE aKey,
                                                                                         @Nullable final VALUETYPE aValue)
  {
    return Collections.singletonMap (aKey, aValue);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Map <ELEMENTTYPE, ELEMENTTYPE> newUnmodifiableOrderedMap (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newOrderedMap (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableOrderedMap (@Nullable final KEYTYPE [] aKeys,
                                                                                         @Nullable final VALUETYPE [] aValues)
  {
    return makeUnmodifiable (newOrderedMap (aKeys, aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableOrderedMap (@Nullable final Collection <? extends KEYTYPE> aKeys,
                                                                                         @Nullable final Collection <? extends VALUETYPE> aValues)
  {
    return makeUnmodifiable (newOrderedMap (aKeys, aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableOrderedMap (@Nullable final Map <? extends KEYTYPE, ? extends VALUETYPE> aOrderedMap)
  {
    return makeUnmodifiable (aOrderedMap);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> newUnmodifiableOrderedMap (@Nullable final Collection <? extends Map.Entry <KEYTYPE, VALUETYPE>> aCollection)
  {
    return makeUnmodifiable (newOrderedMap (aCollection));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> TreeMap <KEYTYPE, VALUETYPE> newSortedMap ()
  {
    return new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> TreeMap <KEYTYPE, VALUETYPE> newSortedMap (@Nullable final KEYTYPE aKey,
                                                                                                                     @Nullable final VALUETYPE aValue)
  {
    final TreeMap <KEYTYPE, VALUETYPE> ret = new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());
    ret.put (aKey, aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeMap <ELEMENTTYPE, ELEMENTTYPE> newSortedMap (@Nullable final ELEMENTTYPE... aValues)
  {
    if (ArrayHelper.isEmpty (aValues))
      return new TreeMap <ELEMENTTYPE, ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());

    if ((aValues.length % 2) != 0)
      throw new IllegalArgumentException ("The passed array needs an even number of elements!");

    final TreeMap <ELEMENTTYPE, ELEMENTTYPE> ret = new TreeMap <ELEMENTTYPE, ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    for (int i = 0; i < aValues.length; i += 2)
      ret.put (aValues[i], aValues[i + 1]);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> TreeMap <KEYTYPE, VALUETYPE> newSortedMap (@Nullable final KEYTYPE [] aKeys,
                                                                                                                     @Nullable final VALUETYPE [] aValues)
  {
    // Are both empty?
    if (ArrayHelper.isEmpty (aKeys) && ArrayHelper.isEmpty (aValues))
      return new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());

    // keys OR values may be null here
    if (ArrayHelper.getSize (aKeys) != ArrayHelper.getSize (aValues))
      throw new IllegalArgumentException ("The passed arrays have different length!");

    final TreeMap <KEYTYPE, VALUETYPE> ret = new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());
    for (int i = 0; i < aKeys.length; ++i)
      ret.put (aKeys[i], aValues[i]);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> TreeMap <KEYTYPE, VALUETYPE> newSortedMap (@Nullable final Collection <? extends KEYTYPE> aKeys,
                                                                                                                     @Nullable final Collection <? extends VALUETYPE> aValues)
  {
    // Are both empty?
    if (isEmpty (aKeys) && isEmpty (aValues))
      return new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());

    // keys OR values may be null here
    if (getSize (aKeys) != getSize (aValues))
      throw new IllegalArgumentException ("Number of keys is different from number of values");

    final TreeMap <KEYTYPE, VALUETYPE> ret = new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());
    final Iterator <? extends KEYTYPE> itk = aKeys.iterator ();
    final Iterator <? extends VALUETYPE> itv = aValues.iterator ();
    while (itk.hasNext ())
      ret.put (itk.next (), itv.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> TreeMap <KEYTYPE, VALUETYPE> newSortedMap (@Nullable final Map <? extends KEYTYPE, ? extends VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());

    final TreeMap <KEYTYPE, VALUETYPE> ret = new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());
    ret.putAll (aMap);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> TreeMap <KEYTYPE, VALUETYPE> newSortedMap (@Nullable final Collection <? extends Map.Entry <KEYTYPE, VALUETYPE>> aCollection)
  {
    if (isEmpty (aCollection))
      return new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());

    final TreeMap <KEYTYPE, VALUETYPE> ret = new TreeMap <KEYTYPE, VALUETYPE> (new ComparatorComparableNullAware <KEYTYPE> ());
    for (final Map.Entry <KEYTYPE, VALUETYPE> aEntry : aCollection)
      ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> newUnmodifiableSortedMap ()
  {
    return makeUnmodifiable (ContainerHelper.<KEYTYPE, VALUETYPE> newSortedMap ());
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> newUnmodifiableSortedMap (@Nullable final KEYTYPE aKey,
                                                                                                                                   @Nullable final VALUETYPE aValue)
  {
    return makeUnmodifiable (newSortedMap (aKey, aValue));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedMap <ELEMENTTYPE, ELEMENTTYPE> newUnmodifiableSortedMap (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newSortedMap (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> newUnmodifiableSortedMap (@Nullable final KEYTYPE [] aKeys,
                                                                                                                                   @Nullable final VALUETYPE [] aValues)
  {
    return makeUnmodifiable (newSortedMap (aKeys, aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> newUnmodifiableSortedMap (@Nullable final Collection <? extends KEYTYPE> aKeys,
                                                                                                                                   @Nullable final Collection <? extends VALUETYPE> aValues)
  {
    return makeUnmodifiable (newSortedMap (aKeys, aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> newUnmodifiableSortedMap (@Nullable final SortedMap <KEYTYPE, ? extends VALUETYPE> aMap)
  {
    return makeUnmodifiable (aMap);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> SortedMap <KEYTYPE, VALUETYPE> newUnmodifiableSortedMap (@Nullable final Collection <? extends Map.Entry <KEYTYPE, VALUETYPE>> aCollection)
  {
    return makeUnmodifiable (newSortedMap (aCollection));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet ()
  {
    return new HashSet <ELEMENTTYPE> (0);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final ELEMENTTYPE aValue)
  {
    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> (1);
    ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final ELEMENTTYPE... aValues)
  {
    if (ArrayHelper.isEmpty (aValues))
      return new HashSet <ELEMENTTYPE> (0);

    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> (aValues.length);
    for (final ELEMENTTYPE aValue : aValues)
      ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> ();
    if (aCont != null)
      for (final ELEMENTTYPE aValue : aCont)
        ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    if (isEmpty (aCont))
      return new HashSet <ELEMENTTYPE> (0);

    return new HashSet <ELEMENTTYPE> (aCont);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> ();
    if (aIter != null)
      while (aIter.hasNext ())
        ret.add (aIter.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    if (aIter == null)
      return new HashSet <ELEMENTTYPE> ();
    return newSet (aIter.iterator ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newSet (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> ();
    if (aEnum != null)
      while (aEnum.hasMoreElements ())
        ret.add (aEnum.nextElement ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Boolean> newBooleanSet (@Nullable final boolean... aValues)
  {
    final Set <Boolean> ret = new HashSet <Boolean> ();
    if (aValues != null)
      for (final boolean aValue : aValues)
        ret.add (Boolean.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Byte> newByteSet (@Nullable final byte... aValues)
  {
    final Set <Byte> ret = new HashSet <Byte> ();
    if (aValues != null)
      for (final byte aValue : aValues)
        ret.add (Byte.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Character> newCharSet (@Nullable final char... aValues)
  {
    final Set <Character> ret = new HashSet <Character> ();
    if (aValues != null)
      for (final char aValue : aValues)
        ret.add (Character.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Double> newDoubleSet (@Nullable final double... aValues)
  {
    final Set <Double> ret = new HashSet <Double> ();
    if (aValues != null)
      for (final double aValue : aValues)
        ret.add (Double.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Float> newFloatSet (@Nullable final float... aValues)
  {
    final Set <Float> ret = new HashSet <Float> ();
    if (aValues != null)
      for (final float aValue : aValues)
        ret.add (Float.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Integer> newIntSet (@Nullable final int... aValues)
  {
    final Set <Integer> ret = new HashSet <Integer> ();
    if (aValues != null)
      for (final int aValue : aValues)
        ret.add (Integer.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Long> newLongSet (@Nullable final long... aValues)
  {
    final Set <Long> ret = new HashSet <Long> ();
    if (aValues != null)
      for (final long aValue : aValues)
        ret.add (Long.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Short> newShortSet (@Nullable final short... aValues)
  {
    final Set <Short> ret = new HashSet <Short> ();
    if (aValues != null)
      for (final short aValue : aValues)
        ret.add (Short.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet ()
  {
    return Collections.<ELEMENTTYPE> emptySet ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final ELEMENTTYPE aValue)
  {
    return Collections.singleton (aValue);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newSet (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newSet (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newSet (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newSet (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableSet (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    return makeUnmodifiable (newSet (aEnum));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Boolean> newUnmodifiableBooleanSet (@Nullable final boolean... aValues)
  {
    return makeUnmodifiable (newBooleanSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Byte> newUnmodifiableByteSet (@Nullable final byte... aValues)
  {
    return makeUnmodifiable (newByteSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Character> newUnmodifiableCharSet (@Nullable final char... aValues)
  {
    return makeUnmodifiable (newCharSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Double> newUnmodifiableDoubleSet (@Nullable final double... aValues)
  {
    return makeUnmodifiable (newDoubleSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Float> newUnmodifiableFloatSet (@Nullable final float... aValues)
  {
    return makeUnmodifiable (newFloatSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Integer> newUnmodifiableIntSet (@Nullable final int... aValues)
  {
    return makeUnmodifiable (newIntSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Long> newUnmodifiableLongSet (@Nullable final long... aValues)
  {
    return makeUnmodifiable (newLongSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Short> newUnmodifiableShortSet (@Nullable final short... aValues)
  {
    return makeUnmodifiable (newShortSet (aValues));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet ()
  {
    return new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
  }

  @Nonnull
  @ReturnsMutableCopy
  @edu.umd.cs.findbugs.annotations.SuppressWarnings (value = { "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE" }, justification = "When using the constructor with the Comparator it works with null values!")
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final ELEMENTTYPE aValue)
  {
    final TreeSet <ELEMENTTYPE> ret = new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final ELEMENTTYPE... aValues)
  {
    final TreeSet <ELEMENTTYPE> ret = new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    if (!ArrayHelper.isEmpty (aValues))
      for (final ELEMENTTYPE aValue : aValues)
        ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    final TreeSet <ELEMENTTYPE> ret = new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    if (aCont != null)
      for (final ELEMENTTYPE aValue : aCont)
        ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    final TreeSet <ELEMENTTYPE> ret = new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    if (!isEmpty (aCont))
      ret.addAll (aCont);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    final TreeSet <ELEMENTTYPE> ret = new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    if (aIter != null)
      while (aIter.hasNext ())
        ret.add (aIter.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    if (aIter == null)
      return new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    return newSortedSet (aIter.iterator ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> TreeSet <ELEMENTTYPE> newSortedSet (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    final TreeSet <ELEMENTTYPE> ret = new TreeSet <ELEMENTTYPE> (new ComparatorComparableNullAware <ELEMENTTYPE> ());
    if (aEnum != null)
      while (aEnum.hasMoreElements ())
        ret.add (aEnum.nextElement ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Boolean> newBooleanSortedSet (@Nullable final boolean... aValues)
  {
    final TreeSet <Boolean> ret = new TreeSet <Boolean> (new ComparatorComparableNullAware <Boolean> ());
    if (aValues != null)
      for (final boolean aValue : aValues)
        ret.add (Boolean.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Byte> newByteSortedSet (@Nullable final byte... aValues)
  {
    final TreeSet <Byte> ret = new TreeSet <Byte> (new ComparatorComparableNullAware <Byte> ());
    if (aValues != null)
      for (final byte aValue : aValues)
        ret.add (Byte.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Character> newCharSortedSet (@Nullable final char... aValues)
  {
    final TreeSet <Character> ret = new TreeSet <Character> (new ComparatorComparableNullAware <Character> ());
    if (aValues != null)
      for (final char aValue : aValues)
        ret.add (Character.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Double> newDoubleSortedSet (@Nullable final double... aValues)
  {
    final TreeSet <Double> ret = new TreeSet <Double> (new ComparatorComparableNullAware <Double> ());
    if (aValues != null)
      for (final double aValue : aValues)
        ret.add (Double.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Float> newFloatSortedSet (@Nullable final float... aValues)
  {
    final TreeSet <Float> ret = new TreeSet <Float> (new ComparatorComparableNullAware <Float> ());
    if (aValues != null)
      for (final float aValue : aValues)
        ret.add (Float.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Integer> newIntSortedSet (@Nullable final int... aValues)
  {
    final TreeSet <Integer> ret = new TreeSet <Integer> (new ComparatorComparableNullAware <Integer> ());
    if (aValues != null)
      for (final int aValue : aValues)
        ret.add (Integer.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Long> newLongSortedSet (@Nullable final long... aValues)
  {
    final TreeSet <Long> ret = new TreeSet <Long> (new ComparatorComparableNullAware <Long> ());
    if (aValues != null)
      for (final long aValue : aValues)
        ret.add (Long.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static TreeSet <Short> newShortSortedSet (@Nullable final short... aValues)
  {
    final TreeSet <Short> ret = new TreeSet <Short> (new ComparatorComparableNullAware <Short> ());
    if (aValues != null)
      for (final short aValue : aValues)
        ret.add (Short.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet ()
  {
    return EmptySortedSet.<ELEMENTTYPE> getInstance ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final ELEMENTTYPE aValue)
  {
    return makeUnmodifiable (newSortedSet (aValue));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newSortedSet (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newSortedSet (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newSortedSet (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newSortedSet (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> SortedSet <ELEMENTTYPE> newUnmodifiableSortedSet (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    return makeUnmodifiable (newSortedSet (aEnum));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Boolean> newUnmodifiableBooleanSortedSet (@Nullable final boolean... aValues)
  {
    return makeUnmodifiable (newBooleanSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Byte> newUnmodifiableByteSortedSet (@Nullable final byte... aValues)
  {
    return makeUnmodifiable (newByteSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Character> newUnmodifiableCharSortedSet (@Nullable final char... aValues)
  {
    return makeUnmodifiable (newCharSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Double> newUnmodifiableDoubleSortedSet (@Nullable final double... aValues)
  {
    return makeUnmodifiable (newDoubleSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Float> newUnmodifiableFloatSortedSet (@Nullable final float... aValues)
  {
    return makeUnmodifiable (newFloatSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Integer> newUnmodifiableIntSortedSet (@Nullable final int... aValues)
  {
    return makeUnmodifiable (newIntSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Long> newUnmodifiableLongSortedSet (@Nullable final long... aValues)
  {
    return makeUnmodifiable (newLongSortedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static SortedSet <Short> newUnmodifiableShortSortedSet (@Nullable final short... aValues)
  {
    return makeUnmodifiable (newShortSortedSet (aValues));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet ()
  {
    return new LinkedHashSet <ELEMENTTYPE> (0);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nullable final ELEMENTTYPE aValue)
  {
    final Set <ELEMENTTYPE> ret = new LinkedHashSet <ELEMENTTYPE> (1);
    ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nullable final ELEMENTTYPE... aValues)
  {
    if (ArrayHelper.isEmpty (aValues))
      return new LinkedHashSet <ELEMENTTYPE> (0);

    final Set <ELEMENTTYPE> ret = new LinkedHashSet <ELEMENTTYPE> (aValues.length);
    for (final ELEMENTTYPE aValue : aValues)
      ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    final Set <ELEMENTTYPE> ret = new LinkedHashSet <ELEMENTTYPE> ();
    if (aCont != null)
      for (final ELEMENTTYPE aValue : aCont)
        ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    if (isEmpty (aCont))
      return new LinkedHashSet <ELEMENTTYPE> (0);

    return new LinkedHashSet <ELEMENTTYPE> (aCont);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nonnull final Iterator <? extends ELEMENTTYPE> aIter)
  {
    final Set <ELEMENTTYPE> ret = new LinkedHashSet <ELEMENTTYPE> ();
    if (aIter != null)
      while (aIter.hasNext ())
        ret.add (aIter.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    if (aIter == null)
      return new LinkedHashSet <ELEMENTTYPE> ();
    return newOrderedSet (aIter.iterator ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newOrderedSet (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    final Set <ELEMENTTYPE> ret = new LinkedHashSet <ELEMENTTYPE> ();
    if (aEnum != null)
      while (aEnum.hasMoreElements ())
        ret.add (aEnum.nextElement ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Boolean> newBooleanOrderedSet (@Nullable final boolean... aValues)
  {
    final Set <Boolean> ret = new LinkedHashSet <Boolean> ();
    if (aValues != null)
      for (final boolean aValue : aValues)
        ret.add (Boolean.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Byte> newByteOrderedSet (@Nullable final byte... aValues)
  {
    final Set <Byte> ret = new LinkedHashSet <Byte> ();
    if (aValues != null)
      for (final byte aValue : aValues)
        ret.add (Byte.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Character> newCharOrderedSet (@Nullable final char... aValues)
  {
    final Set <Character> ret = new LinkedHashSet <Character> ();
    if (aValues != null)
      for (final char aValue : aValues)
        ret.add (Character.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Double> newDoubleOrderedSet (@Nullable final double... aValues)
  {
    final Set <Double> ret = new LinkedHashSet <Double> ();
    if (aValues != null)
      for (final double aValue : aValues)
        ret.add (Double.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Float> newFloatOrderedSet (@Nullable final float... aValues)
  {
    final Set <Float> ret = new LinkedHashSet <Float> ();
    if (aValues != null)
      for (final float aValue : aValues)
        ret.add (Float.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Integer> newIntOrderedSet (@Nullable final int... aValues)
  {
    final Set <Integer> ret = new LinkedHashSet <Integer> ();
    if (aValues != null)
      for (final int aValue : aValues)
        ret.add (Integer.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Long> newLongOrderedSet (@Nullable final long... aValues)
  {
    final Set <Long> ret = new LinkedHashSet <Long> ();
    if (aValues != null)
      for (final long aValue : aValues)
        ret.add (Long.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <Short> newShortOrderedSet (@Nullable final short... aValues)
  {
    final Set <Short> ret = new LinkedHashSet <Short> ();
    if (aValues != null)
      for (final short aValue : aValues)
        ret.add (Short.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet ()
  {
    return Collections.<ELEMENTTYPE> emptySet ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nullable final ELEMENTTYPE aValue)
  {
    return Collections.singleton (aValue);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nonnull final Iterable <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newOrderedSet (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nonnull final Collection <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newOrderedSet (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nonnull final Iterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newOrderedSet (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nonnull final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newOrderedSet (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> newUnmodifiableOrderedSet (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    return makeUnmodifiable (newOrderedSet (aEnum));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Boolean> newUnmodifiableBooleanOrderedSet (@Nullable final boolean... aValues)
  {
    return makeUnmodifiable (newBooleanOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Byte> newUnmodifiableByteOrderedSet (@Nullable final byte... aValues)
  {
    return makeUnmodifiable (newByteOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Character> newUnmodifiableCharOrderedSet (@Nullable final char... aValues)
  {
    return makeUnmodifiable (newCharOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Double> newUnmodifiableDoubleOrderedSet (@Nullable final double... aValues)
  {
    return makeUnmodifiable (newDoubleOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Float> newUnmodifiableFloatOrderedSet (@Nullable final float... aValues)
  {
    return makeUnmodifiable (newFloatOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Integer> newUnmodifiableIntOrderedSet (@Nullable final int... aValues)
  {
    return makeUnmodifiable (newIntOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Long> newUnmodifiableLongOrderedSet (@Nullable final long... aValues)
  {
    return makeUnmodifiable (newLongOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Set <Short> newUnmodifiableShortOrderedSet (@Nullable final short... aValues)
  {
    return makeUnmodifiable (newShortOrderedSet (aValues));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newListPrefilled (@Nullable final ELEMENTTYPE aValue,
                                                                   @Nonnegative final int nElements)
  {
    if (nElements < 0)
      throw new IllegalArgumentException ("Element count must be >= 0!");

    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (nElements);
    for (int i = 0; i < nElements; ++i)
      ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList ()
  {
    return new ArrayList <ELEMENTTYPE> (0);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final ELEMENTTYPE aValue)
  {
    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (1);
    ret.add (aValue);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final ELEMENTTYPE... aValues)
  {
    // Don't user Arrays.asList since aIter returns an unmodifiable list!
    if (ArrayHelper.isEmpty (aValues))
      return new ArrayList <ELEMENTTYPE> (0);

    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (aValues.length);
    for (int i = 0; i < aValues.length; ++i)
      ret.add (aValues[i]);
    return ret;
  }

  /**
   * Compared to {@link Collections#list(Enumeration)} this method is more
   * flexible in Generics parameter.
   * 
   * @param <ELEMENTTYPE>
   *        Type of the elements
   * @param aEnum
   *        The enumeration to be converted
   * @return The non-<code>null</code> created {@link ArrayList}.
   * @see Collections#list(Enumeration)
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> ();
    if (aEnum != null)
      while (aEnum.hasMoreElements ())
        ret.add (aEnum.nextElement ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> ();
    if (aIter != null)
      while (aIter.hasNext ())
        ret.add (aIter.next ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final Iterable <? extends ELEMENTTYPE> aIter)
  {
    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> ();
    if (aIter != null)
      for (final ELEMENTTYPE aObj : aIter)
        ret.add (aObj);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    if (isEmpty (aCont))
      return new ArrayList <ELEMENTTYPE> (0);

    return new ArrayList <ELEMENTTYPE> (aCont);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newList (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    if (aIter == null)
      return new ArrayList <ELEMENTTYPE> ();
    return newList (aIter.iterator ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Boolean> newBooleanList (@Nullable final boolean... aValues)
  {
    final List <Boolean> ret = new ArrayList <Boolean> ();
    if (aValues != null)
      for (final boolean aValue : aValues)
        ret.add (Boolean.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Byte> newByteList (@Nullable final byte... aValues)
  {
    final List <Byte> ret = new ArrayList <Byte> ();
    if (aValues != null)
      for (final byte aValue : aValues)
        ret.add (Byte.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Character> newCharList (@Nullable final char... aValues)
  {
    final List <Character> ret = new ArrayList <Character> ();
    if (aValues != null)
      for (final char aValue : aValues)
        ret.add (Character.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Double> newDoubleList (@Nullable final double... aValues)
  {
    final List <Double> ret = new ArrayList <Double> ();
    if (aValues != null)
      for (final double aValue : aValues)
        ret.add (Double.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Float> newFloatList (@Nullable final float... aValues)
  {
    final List <Float> ret = new ArrayList <Float> ();
    if (aValues != null)
      for (final float aValue : aValues)
        ret.add (Float.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Integer> newIntList (@Nullable final int... aValues)
  {
    final List <Integer> ret = new ArrayList <Integer> ();
    if (aValues != null)
      for (final int aValue : aValues)
        ret.add (Integer.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Long> newLongList (@Nullable final long... aValues)
  {
    final List <Long> ret = new ArrayList <Long> ();
    if (aValues != null)
      for (final long aValue : aValues)
        ret.add (Long.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <Short> newShortList (@Nullable final short... aValues)
  {
    final List <Short> ret = new ArrayList <Short> ();
    if (aValues != null)
      for (final short aValue : aValues)
        ret.add (Short.valueOf (aValue));
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList ()
  {
    return Collections.<ELEMENTTYPE> emptyList ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final ELEMENTTYPE aValue)
  {
    return Collections.singletonList (aValue);
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final ELEMENTTYPE... aValues)
  {
    return makeUnmodifiable (newList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final Enumeration <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newList (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newList (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newList (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    return makeUnmodifiable (newList (aCont));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static <ELEMENTTYPE> List <ELEMENTTYPE> newUnmodifiableList (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    return makeUnmodifiable (newList (aIter));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Boolean> newUnmodifiableBooleanList (@Nullable final boolean... aValues)
  {
    return makeUnmodifiable (newBooleanList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Byte> newUnmodifiableByteList (@Nullable final byte... aValues)
  {
    return makeUnmodifiable (newByteList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Character> newUnmodifiableCharList (@Nullable final char... aValues)
  {
    return makeUnmodifiable (newCharList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Double> newUnmodifiableDoubleList (@Nullable final double... aValues)
  {
    return makeUnmodifiable (newDoubleList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Float> newUnmodifiableFloatList (@Nullable final float... aValues)
  {
    return makeUnmodifiable (newFloatList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Integer> newUnmodifiableIntList (@Nullable final int... aValues)
  {
    return makeUnmodifiable (newIntList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Long> newUnmodifiableLongList (@Nullable final long... aValues)
  {
    return makeUnmodifiable (newLongList (aValues));
  }

  @Nonnull
  @ReturnsImmutableObject
  public static List <Short> newUnmodifiableShortList (@Nullable final short... aValues)
  {
    return makeUnmodifiable (newShortList (aValues));
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> newStack ()
  {
    return new NonBlockingStack <ELEMENTTYPE> ();
  }

  /**
   * Create a new stack with a single element.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements contained in the stack.
   * @param aValue
   *        The value to push. Maybe <code>null</code>.
   * @return A non-<code>null</code> stack.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> newStack (@Nullable final ELEMENTTYPE aValue)
  {
    final NonBlockingStack <ELEMENTTYPE> ret = new NonBlockingStack <ELEMENTTYPE> ();
    ret.push (aValue);
    return ret;
  }

  /**
   * Create a new stack from the given array.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements contained in the stack.
   * @param aValues
   *        The values that are to be pushed on the stack. The last element will
   *        be the top element on the stack. May not be <code>null</code> .
   * @return A non-<code>null</code> stack object.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> newStack (@Nullable final ELEMENTTYPE... aValues)
  {
    return new NonBlockingStack <ELEMENTTYPE> (aValues);
  }

  /**
   * Create a new stack from the given collection.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements contained in the stack.
   * @param aValues
   *        The values that are to be pushed on the stack. The last element will
   *        be the top element on the stack. May not be <code>null</code> .
   * @return A non-<code>null</code> stack object.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> newStack (@Nullable final Collection <? extends ELEMENTTYPE> aValues)
  {
    return new NonBlockingStack <ELEMENTTYPE> (aValues);
  }

  /**
   * Convert the given iterator to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements to iterate. May not be <code>null</code>.
   * @param aIter
   *        Input iterator. May be <code>null</code>.
   * @return a non-null {@link ArrayList} based on the results of
   *         {@link Collections#sort(List)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSorted (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter)
  {
    return getSortedInline (newList (aIter));
  }

  /**
   * Convert the given iterator to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements to iterate. May not be <code>null</code>.
   * @param aIter
   *        Input iterator. May be <code>null</code>.
   * @param aComparator
   *        The comparator to use. May not be <code>null</code>.
   * @return a non-null {@link ArrayList} based on the results of
   *         {@link Collections#sort(List)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSorted (@Nullable final IIterableIterator <? extends ELEMENTTYPE> aIter,
                                                                                                     @Nonnull final Comparator <? super ELEMENTTYPE> aComparator)
  {
    return getSortedInline (newList (aIter), aComparator);
  }

  /**
   * Convert the given iterator to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements to iterate. May not be <code>null</code>.
   * @param aIter
   *        Input iterator. May not be <code>null</code>.
   * @return a non-null {@link ArrayList} based on the results of
   *         {@link Collections#sort(List)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSorted (@Nullable final Iterator <? extends ELEMENTTYPE> aIter)
  {
    return getSortedInline (newList (aIter));
  }

  /**
   * Convert the given iterator to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements to iterate.
   * @param aIter
   *        Input iterator. May be <code>null</code>.
   * @param aComparator
   *        The comparator to use. May not be <code>null</code>.
   * @return a non-null {@link ArrayList} based on the results of
   *         {@link Collections#sort(List, Comparator)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getSorted (@Nullable final Iterator <? extends ELEMENTTYPE> aIter,
                                                            @Nonnull final Comparator <? super ELEMENTTYPE> aComparator)
  {
    return getSortedInline (newList (aIter), aComparator);
  }

  /**
   * Convert the given iterable object to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of element to iterate.
   * @param aCont
   *        Iterable input object. May be <code>null</code>.
   * @return A {@link ArrayList} based on the results of
   *         {@link Collections#sort(List)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSorted (@Nullable final Iterable <? extends ELEMENTTYPE> aCont)
  {
    return getSortedInline (newList (aCont));
  }

  /**
   * Convert the given iterable object to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of element to iterate.
   * @param aCont
   *        Iterable input object. May be <code>null</code>.
   * @param aComparator
   *        The comparator to use. May not be <code>null</code>.
   * @return A {@link ArrayList} based on the results of
   *         {@link Collections#sort(List, Comparator)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getSorted (@Nullable final Iterable <? extends ELEMENTTYPE> aCont,
                                                            @Nonnull final Comparator <? super ELEMENTTYPE> aComparator)
  {
    return getSortedInline (newList (aCont), aComparator);
  }

  /**
   * Convert the given collection object to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of element to iterate.
   * @param aCont
   *        Collection input object. May be <code>null</code>.
   * @return A {@link ArrayList} based on the results of
   *         {@link Collections#sort(List)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSorted (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    return getSortedInline (newList (aCont));
  }

  /**
   * Convert the given collection object to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of element to iterate.
   * @param aCont
   *        Collection input object. May be <code>null</code>.
   * @param aComparator
   *        The comparator to use. May not be <code>null</code>.
   * @return A {@link ArrayList} based on the results of
   *         {@link Collections#sort(List, Comparator)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getSorted (@Nullable final Collection <? extends ELEMENTTYPE> aCont,
                                                            @Nonnull final Comparator <? super ELEMENTTYPE> aComparator)
  {
    return getSortedInline (newList (aCont), aComparator);
  }

  /**
   * Convert the given iterable object to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of element to iterate.
   * @param aCont
   *        Array input object. May be <code>null</code>.
   * @return A {@link ArrayList} based on the results of
   *         {@link Collections#sort(List)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSorted (@Nullable final ELEMENTTYPE... aCont)
  {
    return getSortedInline (newList (aCont));
  }

  /**
   * Convert the given iterable object to a sorted list.
   * 
   * @param <ELEMENTTYPE>
   *        The type of element to iterate.
   * @param aCont
   *        Iterable input object. May be <code>null</code>.
   * @param aComparator
   *        The comparator to use. May not be <code>null</code>.
   * @return A {@link ArrayList} based on the results of
   *         {@link Collections#sort(List, Comparator)}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getSorted (@Nullable final ELEMENTTYPE [] aCont,
                                                            @Nonnull final Comparator <? super ELEMENTTYPE> aComparator)
  {
    return getSortedInline (newList (aCont), aComparator);
  }

  @Nullable
  @ReturnsMutableObject (reason = "design")
  public static <ELEMENTTYPE extends Comparable <? super ELEMENTTYPE>> List <ELEMENTTYPE> getSortedInline (@Nullable final List <ELEMENTTYPE> aList)
  {
    if (!isEmpty (aList))
      Collections.sort (aList);
    return aList;
  }

  @Nullable
  @ReturnsMutableObject (reason = "design")
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getSortedInline (@Nullable final List <ELEMENTTYPE> aList,
                                                                  @Nonnull final Comparator <? super ELEMENTTYPE> aComparator)
  {
    if (aComparator == null)
      throw new NullPointerException ("comparator");

    if (!isEmpty (aList))
      Collections.sort (aList, aComparator);
    return aList;
  }

  /**
   * Get a map sorted by aIter's keys. Because no comparator is defined, the key
   * type needs to implement the {@link java.lang.Comparable} interface.
   * 
   * @param <KEYTYPE>
   *        map key type
   * @param <VALUETYPE>
   *        map value type
   * @param aMap
   *        the map to sort
   * @return the sorted map or the original map, if it was empty
   */
  @Nullable
  public static <KEYTYPE extends Comparable <? super KEYTYPE>, VALUETYPE> Map <KEYTYPE, VALUETYPE> getSortedByKey (@Nullable final Map <KEYTYPE, VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return aMap;

    // get sorted entry list
    final List <Map.Entry <KEYTYPE, VALUETYPE>> aList = newList (aMap.entrySet ());
    Collections.sort (aList, ComparatorUtils.<KEYTYPE, VALUETYPE> getComparatorMapEntryKey ());
    return newOrderedMap (aList);
  }

  /**
   * Get a map sorted by aIter's keys. The comparison order is defined by the
   * passed comparator object.
   * 
   * @param <KEYTYPE>
   *        map key type
   * @param <VALUETYPE>
   *        map value type
   * @param aMap
   *        The map to sort. May not be <code>null</code>.
   * @param aKeyComparator
   *        The comparator to be used. May not be <code>null</code>.
   * @return the sorted map or the original map, if it was empty
   */
  @Nullable
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> getSortedByKey (@Nullable final Map <KEYTYPE, VALUETYPE> aMap,
                                                                              @Nonnull final Comparator <? super KEYTYPE> aKeyComparator)
  {
    if (aKeyComparator == null)
      throw new NullPointerException ("keyComparator");

    if (isEmpty (aMap))
      return aMap;

    // get sorted Map.Entry list by Entry.getValue ()
    final List <Map.Entry <KEYTYPE, VALUETYPE>> aList = newList (aMap.entrySet ());
    Collections.sort (aList, ComparatorUtils.<KEYTYPE, VALUETYPE> getComparatorMapEntryKey (aKeyComparator));
    return newOrderedMap (aList);
  }

  /**
   * Get a map sorted by aIter's values. Because no comparator is defined, the
   * value type needs to implement the {@link java.lang.Comparable} interface.
   * 
   * @param <KEYTYPE>
   *        map key type
   * @param <VALUETYPE>
   *        map value type
   * @param aMap
   *        The map to sort. May not be <code>null</code>.
   * @return the sorted map or the original map, if it was empty
   */
  @Nullable
  public static <KEYTYPE, VALUETYPE extends Comparable <? super VALUETYPE>> Map <KEYTYPE, VALUETYPE> getSortedByValue (@Nullable final Map <KEYTYPE, VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return aMap;

    // get sorted entry list
    final List <Map.Entry <KEYTYPE, VALUETYPE>> aList = newList (aMap.entrySet ());
    Collections.sort (aList, ComparatorUtils.<KEYTYPE, VALUETYPE> getComparatorMapEntryValue ());
    return newOrderedMap (aList);
  }

  /**
   * Get a map sorted by aIter's values. The comparison order is defined by the
   * passed comparator object.
   * 
   * @param <KEYTYPE>
   *        map key type
   * @param <VALUETYPE>
   *        map value type
   * @param aMap
   *        The map to sort. May not be <code>null</code>.
   * @param aValueComparator
   *        The comparator to be used. May not be <code>null</code>.
   * @return the sorted map or the original map, if it was empty
   */
  @Nullable
  public static <KEYTYPE, VALUETYPE> Map <KEYTYPE, VALUETYPE> getSortedByValue (@Nullable final Map <KEYTYPE, VALUETYPE> aMap,
                                                                                @Nonnull final Comparator <? super VALUETYPE> aValueComparator)
  {
    if (aValueComparator == null)
      throw new NullPointerException ("valueComparator");

    if (isEmpty (aMap))
      return aMap;

    // get sorted Map.Entry list by Entry.getValue ()
    final List <Map.Entry <KEYTYPE, VALUETYPE>> aList = newList (aMap.entrySet ());
    Collections.sort (aList, ComparatorUtils.<KEYTYPE, VALUETYPE> getComparatorMapEntryValue (aValueComparator));
    return newOrderedMap (aList);
  }

  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getReverseList (@Nullable final Collection <? extends ELEMENTTYPE> aCollection)
  {
    if (isEmpty (aCollection))
      return new ArrayList <ELEMENTTYPE> (0);

    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (aCollection);
    Collections.reverse (ret);
    return ret;
  }

  @Nullable
  @ReturnsMutableObject (reason = "semantics of this method")
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getReverseInlineList (@Nullable final List <ELEMENTTYPE> aList)
  {
    if (aList == null)
      return null;

    Collections.reverse (aList);
    return aList;
  }

  @Nonnull
  public static <ELEMENTTYPE> IIterableIterator <ELEMENTTYPE> getIterator (@Nullable final Enumeration <? extends ELEMENTTYPE> aEnum)
  {
    return new IterableIteratorFromEnumeration <ELEMENTTYPE> (aEnum);
  }

  @Nonnull
  public static <ELEMENTTYPE> Iterator <ELEMENTTYPE> getIterator (@Nullable final Iterable <ELEMENTTYPE> aCont)
  {
    return aCont == null ? EmptyIterator.<ELEMENTTYPE> getInstance () : getIterator (aCont.iterator ());
  }

  @Nonnull
  public static <ELEMENTTYPE> Iterator <ELEMENTTYPE> getIterator (@Nullable final Iterator <ELEMENTTYPE> aIter)
  {
    return aIter == null ? EmptyIterator.<ELEMENTTYPE> getInstance () : aIter;
  }

  @Nonnull
  public static <ELEMENTTYPE> Iterator <ELEMENTTYPE> getIterator (@Nullable final ELEMENTTYPE... aArray)
  {
    return ArrayHelper.isEmpty (aArray) ? EmptyIterator.<ELEMENTTYPE> getInstance ()
                                       : getIterator (newList (aArray).iterator ());
  }

  @Nonnull
  public static <ELEMENTTYPE> Iterator <ELEMENTTYPE> getReverseIterator (@Nullable final List <ELEMENTTYPE> aCont)
  {
    if (isEmpty (aCont))
      return EmptyIterator.<ELEMENTTYPE> getInstance ();

    /**
     * Performance note: this implementation is much faster than building a
     * temporary list in reverse order and returning a forward iterator!
     */
    return new ReverseListIterator <ELEMENTTYPE> (aCont);
  }

  /**
   * Create an empty iterator.
   * 
   * @param <ELEMENTTYPE>
   *        The type the iterator's elements.
   * @return A non-<code>null</code> object.
   */
  @Nonnull
  public static <ELEMENTTYPE> Iterator <ELEMENTTYPE> getEmptyIterator ()
  {
    return EmptyIterator.<ELEMENTTYPE> getInstance ();
  }

  /**
   * Get an {@link Enumeration} object based on a {@link Collection} object.
   * 
   * @param <ELEMENTTYPE>
   *        the type of the elements in the container
   * @param aCont
   *        The container to enumerate.
   * @return an Enumeration object
   */
  @Nonnull
  public static <ELEMENTTYPE> Enumeration <ELEMENTTYPE> getEnumeration (@Nullable final Iterable <ELEMENTTYPE> aCont)
  {
    return isEmpty (aCont) ? EmptyEnumeration.<ELEMENTTYPE> getInstance () : getEnumeration (aCont.iterator ());
  }

  /**
   * Get an {@link Enumeration} object based on the passed array.
   * 
   * @param <ELEMENTTYPE>
   *        the type of the elements in the container
   * @param aArray
   *        The array to enumerate.
   * @return an Enumeration object
   */
  @Nonnull
  public static <ELEMENTTYPE> Enumeration <ELEMENTTYPE> getEnumeration (@Nullable final ELEMENTTYPE... aArray)
  {
    return getEnumeration (getIterator (aArray));
  }

  /**
   * Get an Enumeration object based on an Iterator object.
   * 
   * @param <ELEMENTTYPE>
   *        the type of the elements in the container
   * @param aIter
   *        iterator object to use
   * @return an Enumeration object
   */
  @Nonnull
  public static <ELEMENTTYPE> Enumeration <ELEMENTTYPE> getEnumeration (@Nullable final Iterator <ELEMENTTYPE> aIter)
  {
    if (aIter == null)
      return EmptyEnumeration.<ELEMENTTYPE> getInstance ();

    return new EnumerationFromIterator <ELEMENTTYPE> (aIter);
  }

  /**
   * Get a merged enumeration of both enumeration. The first enumeration is
   * enumerated first, the second one afterwards.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements to be enumerated.
   * @param aEnum1
   *        First enumeration. May be <code>null</code>.
   * @param aEnum2
   *        Second enumeration. May be <code>null</code>.
   * @return The merged enumeration. Never <code>null</code>.
   */
  @Nonnull
  public static <ELEMENTTYPE> Enumeration <ELEMENTTYPE> getCombinedEnumeration (@Nullable final Enumeration <ELEMENTTYPE> aEnum1,
                                                                                @Nullable final Enumeration <ELEMENTTYPE> aEnum2)
  {
    return new CombinedEnumeration <ELEMENTTYPE> (aEnum1, aEnum2);
  }

  /**
   * Create an empty enumeration.
   * 
   * @param <ELEMENTTYPE>
   *        The type the enumeration's elements.
   * @return A non-<code>null</code> object.
   */
  @Nonnull
  public static <ELEMENTTYPE> Enumeration <ELEMENTTYPE> getEmptyEnumeration ()
  {
    return EmptyEnumeration.<ELEMENTTYPE> getInstance ();
  }

  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> getStackCopyWithoutTop (@Nullable final NonBlockingStack <ELEMENTTYPE> aStack)
  {
    if (isEmpty (aStack))
      return null;

    final NonBlockingStack <ELEMENTTYPE> ret = new NonBlockingStack <ELEMENTTYPE> (aStack);
    ret.pop ();
    return ret;
  }

  /**
   * Get a map consisting only of a set of specified keys. If an element from
   * the key set is not contained in the original map, the key is ignored.
   * 
   * @param <KEY>
   *        Source map key type.
   * @param <VALUE>
   *        Source map value type.
   * @param aValues
   *        Source map to filter. May not be <code>null</code>.
   * @param aKeys
   *        The filter set to filter the entries from. May not be
   *        <code>null</code>.
   * @return A non-<code>null</code> map containing only the elements from the
   *         specified key set.
   */
  @Nullable
  @ReturnsMutableCopy
  public static <KEY, VALUE> Map <KEY, VALUE> getFilteredMap (@Nullable final Map <KEY, VALUE> aValues,
                                                              @Nullable final Collection <KEY> aKeys)
  {
    if (isEmpty (aValues) || isEmpty (aKeys))
      return null;

    final Map <KEY, VALUE> ret = new HashMap <KEY, VALUE> ();
    for (final KEY aKey : aKeys)
      if (aValues.containsKey (aKey))
        ret.put (aKey, aValues.get (aKey));
    return ret;
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getFirstElement (@Nullable final List <ELEMENTTYPE> aList)
  {
    return isEmpty (aList) ? null : aList.get (0);
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getFirstElement (@Nullable final Collection <ELEMENTTYPE> aCont)
  {
    return isEmpty (aCont) ? null : aCont.iterator ().next ();
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getFirstElement (@Nullable final Iterable <ELEMENTTYPE> aCont)
  {
    if (aCont == null)
      return null;
    final Iterator <ELEMENTTYPE> it = aCont.iterator ();
    return it.hasNext () ? it.next () : null;
  }

  @Nullable
  public static <KEYTYPE, VALUETYPE> Map.Entry <KEYTYPE, VALUETYPE> getFirstElement (@Nullable final Map <KEYTYPE, VALUETYPE> aMap)
  {
    return isEmpty (aMap) ? null : getFirstElement (aMap.entrySet ());
  }

  @Nullable
  public static <KEYTYPE, VALUETYPE> KEYTYPE getFirstKey (@Nullable final Map <KEYTYPE, VALUETYPE> aMap)
  {
    return isEmpty (aMap) ? null : getFirstElement (aMap.keySet ());
  }

  @Nullable
  public static <KEYTYPE, VALUETYPE> VALUETYPE getFirstValue (@Nullable final Map <KEYTYPE, VALUETYPE> aMap)
  {
    return isEmpty (aMap) ? null : getFirstElement (aMap.values ());
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE removeFirstElement (@Nullable final List <ELEMENTTYPE> aList)
  {
    return isEmpty (aList) ? null : aList.remove (0);
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getLastElement (@Nullable final List <ELEMENTTYPE> aList)
  {
    final int nSize = getSize (aList);
    return nSize == 0 ? null : aList.get (nSize - 1);
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getLastElement (@Nullable final Collection <ELEMENTTYPE> aCont)
  {
    if (isEmpty (aCont))
      return null;

    // Slow but shouldn't matter
    ELEMENTTYPE aLast = null;
    for (final Iterator <ELEMENTTYPE> it = aCont.iterator (); it.hasNext ();)
      aLast = it.next ();
    return aLast;
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getLastElement (@Nullable final Iterable <ELEMENTTYPE> aCont)
  {
    if (aCont == null)
      return null;

    // Slow but shouldn't matter
    ELEMENTTYPE aLast = null;
    for (final Iterator <ELEMENTTYPE> it = aCont.iterator (); it.hasNext ();)
      aLast = it.next ();
    return aLast;
  }

  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE removeLastElement (@Nullable final List <ELEMENTTYPE> aList)
  {
    final int nSize = getSize (aList);
    return nSize == 0 ? null : aList.remove (nSize - 1);
  }

  public static boolean isEmpty (@Nullable final Iterable <?> aCont)
  {
    return aCont == null || !aCont.iterator ().hasNext ();
  }

  public static boolean isEmpty (@Nullable final Iterator <?> aIter)
  {
    return aIter == null || !aIter.hasNext ();
  }

  public static boolean isEmpty (@Nullable final IIterableIterator <?> aIter)
  {
    return aIter == null || !aIter.hasNext ();
  }

  public static boolean isEmpty (@Nullable final Enumeration <?> aEnum)
  {
    return aEnum == null || !aEnum.hasMoreElements ();
  }

  public static boolean isEmpty (@Nullable final Collection <?> aCont)
  {
    return aCont == null || aCont.isEmpty ();
  }

  public static boolean isEmpty (@Nullable final Map <?, ?> aCont)
  {
    return aCont == null || aCont.isEmpty ();
  }

  /**
   * Retrieve the size of the passed {@link Collection}. This method handles
   * <code>null</code> containers.
   * 
   * @param aObj
   *        Object to check. May be <code>null</code>.
   * @return The size of the object or 0 if the passed parameter is
   *         <code>null</code>.
   */
  @Nonnegative
  public static int getSize (@Nullable final Collection <?> aObj)
  {
    return aObj == null ? 0 : aObj.size ();
  }

  /**
   * Retrieve the size of the passed {@link Map}. This method handles
   * <code>null</code> containers.
   * 
   * @param aObj
   *        Object to check. May be <code>null</code>.
   * @return The size of the object or 0 if the passed parameter is
   *         <code>null</code>.
   */
  @Nonnegative
  public static int getSize (@Nullable final Map <?, ?> aObj)
  {
    return aObj == null ? 0 : aObj.size ();
  }

  /**
   * Retrieve the size of the passed {@link Iterable}.
   * 
   * @param aCont
   *        Iterator to check. May be <code>null</code>.
   * @return The number objects or 0 if the passed parameter is
   *         <code>null</code>.
   */
  @Nonnegative
  public static int getSize (@Nullable final Iterable <?> aCont)
  {
    return aCont == null ? 0 : getSize (aCont.iterator ());
  }

  /**
   * Retrieve the size of the passed {@link Iterable}.
   * 
   * @param aCont
   *        Iterable iterator to check. May be <code>null</code>.
   * @return The number objects or 0 if the passed parameter is
   *         <code>null</code>.
   */
  @Nonnegative
  public static int getSize (@Nullable final IIterableIterator <?> aCont)
  {
    return aCont == null ? 0 : getSize (aCont.iterator ());
  }

  /**
   * Retrieve the size of the passed {@link Iterator}.
   * 
   * @param aIter
   *        Iterator to check. May be <code>null</code>.
   * @return The number objects or 0 if the passed parameter is
   *         <code>null</code>.
   */
  @Nonnegative
  public static int getSize (@Nullable final Iterator <?> aIter)
  {
    int ret = 0;
    if (aIter != null)
      while (aIter.hasNext ())
      {
        aIter.next ();
        ++ret;
      }
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getConcatenatedList (@Nullable final Collection <? extends ELEMENTTYPE> aCont1,
                                                                      @Nullable final Collection <? extends ELEMENTTYPE> aCont2)
  {
    final int nSize1 = getSize (aCont1);
    final int nSize2 = getSize (aCont2);
    if (nSize1 == 0)
      return newList (aCont2);
    if (nSize2 == 0)
      return newList (aCont1);

    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (nSize1 + nSize2);
    ret.addAll (aCont1);
    ret.addAll (aCont2);
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getConcatenatedList (@Nullable final Collection <? extends ELEMENTTYPE> aCont1,
                                                                      @Nullable final ELEMENTTYPE... aCont2)
  {
    final int nSize1 = getSize (aCont1);
    final int nSize2 = ArrayHelper.getSize (aCont2);
    if (nSize1 == 0)
      return newList (aCont2);
    if (nSize2 == 0)
      return newList (aCont1);

    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (nSize1 + nSize2);
    ret.addAll (aCont1);
    for (final ELEMENTTYPE aElement : aCont2)
      ret.add (aElement);
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getConcatenatedList (@Nullable final ELEMENTTYPE [] aCont1,
                                                                      @Nullable final Collection <? extends ELEMENTTYPE> aCont2)
  {
    final int nSize1 = ArrayHelper.getSize (aCont1);
    final int nSize2 = getSize (aCont2);
    if (nSize1 == 0)
      return newList (aCont2);
    if (nSize2 == 0)
      return newList (aCont1);

    final List <ELEMENTTYPE> ret = new ArrayList <ELEMENTTYPE> (nSize1 + nSize2);
    for (final ELEMENTTYPE aElement : aCont1)
      ret.add (aElement);
    ret.addAll (aCont2);
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> getConcatenatedSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont1,
                                                                    @Nullable final Collection <? extends ELEMENTTYPE> aCont2)
  {
    final int nSize1 = getSize (aCont1);
    final int nSize2 = getSize (aCont2);
    if (nSize1 == 0)
      return newSet (aCont2);
    if (nSize2 == 0)
      return newSet (aCont1);

    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> (nSize1 + nSize2);
    ret.addAll (aCont1);
    ret.addAll (aCont2);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> getConcatenatedSet (@Nullable final Collection <? extends ELEMENTTYPE> aCont1,
                                                                    @Nullable final ELEMENTTYPE... aCont2)
  {
    final int nSize1 = getSize (aCont1);
    final int nSize2 = ArrayHelper.getSize (aCont2);
    if (nSize1 == 0)
      return newSet (aCont2);
    if (nSize2 == 0)
      return newSet (aCont1);

    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> (nSize1 + nSize2);
    ret.addAll (aCont1);
    for (final ELEMENTTYPE aElement : aCont2)
      ret.add (aElement);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> Set <ELEMENTTYPE> getConcatenatedSet (@Nullable final ELEMENTTYPE [] aCont1,
                                                                    @Nullable final Collection <? extends ELEMENTTYPE> aCont2)
  {
    final int nSize1 = ArrayHelper.getSize (aCont1);
    final int nSize2 = getSize (aCont2);
    if (nSize1 == 0)
      return newSet (aCont2);
    if (nSize2 == 0)
      return newSet (aCont1);

    final Set <ELEMENTTYPE> ret = new HashSet <ELEMENTTYPE> (nSize1 + nSize2);
    for (final ELEMENTTYPE aElement : aCont1)
      ret.add (aElement);
    ret.addAll (aCont2);
    return ret;
  }

  @Nonnull
  @ReturnsMutableObject (reason = "design")
  public static <ELEMENTTYPE, COLLTYPE extends Collection <? super ELEMENTTYPE>> COLLTYPE getConcatenatedInline (@Nonnull final COLLTYPE aCont,
                                                                                                                 @Nullable final ELEMENTTYPE... aElementsToAdd)
  {
    if (aCont == null)
      throw new NullPointerException ("cont");

    if (aElementsToAdd != null)
      for (final ELEMENTTYPE aElement : aElementsToAdd)
        aCont.add (aElement);
    return aCont;
  }

  @Nonnull
  @ReturnsMutableObject (reason = "design")
  public static <ELEMENTTYPE, COLLTYPE extends Collection <? super ELEMENTTYPE>> COLLTYPE getConcatenatedInline (@Nonnull final COLLTYPE aCont,
                                                                                                                 @Nullable final Collection <? extends ELEMENTTYPE> aElementsToAdd)
  {
    if (aCont == null)
      throw new NullPointerException ("cont");

    if (aElementsToAdd != null)
      aCont.addAll (aElementsToAdd);
    return aCont;
  }

  /**
   * Create a map that contains the combination of the other 2 maps. Both maps
   * need to have the same key and value type.
   * 
   * @param <KEY>
   *        The map key type.
   * @param <VALUE>
   *        The map value type.
   * @param aMap1
   *        The first map. May be <code>null</code>.
   * @param aMap2
   *        The second map. May be <code>null</code>.
   * @return Never <code>null</code> and always a new object. If both parameters
   *         are not <code>null</code> a new map is created, initially
   *         containing the entries from the first parameter, afterwards
   *         extended by the parameters of the second map potentially
   *         overwriting elements from the first map.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <KEY, VALUE> Map <KEY, VALUE> getCombinedMap (@Nullable final Map <KEY, VALUE> aMap1,
                                                              @Nullable final Map <KEY, VALUE> aMap2)
  {
    if (isEmpty (aMap1))
      return newMap (aMap2);
    if (isEmpty (aMap2))
      return newMap (aMap1);

    // create and fill result map
    final Map <KEY, VALUE> ret = new HashMap <KEY, VALUE> (aMap1);
    ret.putAll (aMap2);
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Boolean> newObjectListFromArray (@Nullable final boolean [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Boolean> ret = new ArrayList <Boolean> (aArray.length);
    for (final boolean x : aArray)
      ret.add (Boolean.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Byte> newObjectListFromArray (@Nullable final byte [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Byte> ret = new ArrayList <Byte> (aArray.length);
    for (final byte x : aArray)
      ret.add (Byte.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Character> newObjectListFromArray (@Nullable final char [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Character> ret = new ArrayList <Character> (aArray.length);
    for (final char x : aArray)
      ret.add (Character.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Double> newObjectListFromArray (@Nullable final double [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Double> ret = new ArrayList <Double> (aArray.length);
    for (final double x : aArray)
      ret.add (Double.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Float> newObjectListFromArray (@Nullable final float [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Float> ret = new ArrayList <Float> (aArray.length);
    for (final float x : aArray)
      ret.add (Float.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Integer> newObjectListFromArray (@Nullable final int [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Integer> ret = new ArrayList <Integer> (aArray.length);
    for (final int x : aArray)
      ret.add (Integer.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Long> newObjectListFromArray (@Nullable final long [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Long> ret = new ArrayList <Long> (aArray.length);
    for (final long x : aArray)
      ret.add (Long.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <Short> newObjectListFromArray (@Nullable final short [] aArray)
  {
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Short> ret = new ArrayList <Short> (aArray.length);
    for (final short x : aArray)
      ret.add (Short.valueOf (x));
    return ret;
  }

  @Nullable
  @ReturnsMutableCopy
  public static List <?> newObjectListFromArray (@Nullable final Object aValue, @Nonnull final Class <?> aComponentType)
  {
    if (aComponentType == boolean.class)
    {
      // get as List<Boolean>
      return newObjectListFromArray ((boolean []) aValue);
    }
    if (aComponentType == byte.class)
    {
      // get as List<Byte>
      return newObjectListFromArray ((byte []) aValue);
    }
    if (aComponentType == char.class)
    {
      // get as List<Character>
      return newObjectListFromArray ((char []) aValue);
    }
    if (aComponentType == double.class)
    {
      // get as List<Double>
      return newObjectListFromArray ((double []) aValue);
    }
    if (aComponentType == float.class)
    {
      // get as List<Float>
      return newObjectListFromArray ((float []) aValue);
    }
    if (aComponentType == int.class)
    {
      // get as List<Integer>
      return newObjectListFromArray ((int []) aValue);
    }
    if (aComponentType == long.class)
    {
      // get as List<Long>
      return newObjectListFromArray ((long []) aValue);
    }
    if (aComponentType == short.class)
    {
      // get as List<Short>
      return newObjectListFromArray ((short []) aValue);
    }

    // the rest
    final Object [] aArray = (Object []) aValue;
    if (ArrayHelper.isEmpty (aArray))
      return null;

    final List <Object> ret = new ArrayList <Object> (aArray.length);
    for (final Object x : aArray)
      ret.add (x);
    return ret;
  }

  /**
   * Gets a sublist excerpt of the passed list.
   * 
   * @param <ELEMENTTYPE>
   *        Type of elements in list
   * @param aCont
   *        The backing list. May not be <code>null</code>.
   * @param nStartIndex
   *        The start index to use. Needs to be &ge; 0.
   * @param nSectionLength
   *        the length of the desired subset. If list is shorter than that,
   *        aIter will return a shorter section
   * @return The specified section of the passed list, or a shorter list if
   *         nStartIndex + nSectionLength is an invalid index. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static <ELEMENTTYPE> List <ELEMENTTYPE> getSubList (@Nullable final List <ELEMENTTYPE> aCont,
                                                             @Nonnegative final int nStartIndex,
                                                             @Nonnegative final int nSectionLength)
  {
    if (nStartIndex < 0)
      throw new IllegalArgumentException ("Start index must be >= 0: " + nStartIndex);
    if (nSectionLength < 0)
      throw new IllegalArgumentException ("Length must be >= 0: " + nSectionLength);

    final int nSize = getSize (aCont);
    if (nSize == 0)
      return new ArrayList <ELEMENTTYPE> (0);

    int nEndIndex = nStartIndex + nSectionLength;
    if (nEndIndex > nSize)
      nEndIndex = nSize;

    // Create a copy of the list because "subList" only returns a view of the
    // original list!
    return newList (aCont.subList (nStartIndex, nEndIndex));
  }

  /**
   * Get a map where keys and values are exchanged.
   * 
   * @param <KEYTYPE>
   *        Original key type.
   * @param <VALUETYPE>
   *        Original value type.
   * @param aMap
   *        The input map to convert. May not be <code>null</code>.
   * @return The swapped hash map (unsorted!)
   */
  @Nullable
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> Map <VALUETYPE, KEYTYPE> getSwappedKeyValues (@Nullable final Map <KEYTYPE, VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return null;

    final Map <VALUETYPE, KEYTYPE> ret = new HashMap <VALUETYPE, KEYTYPE> (aMap.size ());
    for (final Map.Entry <KEYTYPE, VALUETYPE> aEntry : aMap.entrySet ())
      ret.put (aEntry.getValue (), aEntry.getKey ());
    return ret;
  }

  /**
   * Get a map where the lookup (1K..nV) has been reversed to (1V..nK)
   * 
   * @param <KEYTYPE>
   *        Original key type
   * @param <VALUETYPE>
   *        Original value type
   * @param aMap
   *        The input map to convert. May not be <code>null</code>
   * @return A swapped {@link IMultiMapSetBased}
   */
  @Nullable
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> IMultiMapSetBased <VALUETYPE, KEYTYPE> getReverseLookupSet (@Nullable final IMultiMap <KEYTYPE, VALUETYPE, ? extends Collection <VALUETYPE>> aMap)
  {
    if (isEmpty (aMap))
      return null;

    final IMultiMapSetBased <VALUETYPE, KEYTYPE> ret = new MultiHashMapHashSetBased <VALUETYPE, KEYTYPE> ();
    for (final Map.Entry <KEYTYPE, ? extends Collection <VALUETYPE>> aEntry : aMap.entrySet ())
      for (final VALUETYPE aValue : aEntry.getValue ())
        ret.putSingle (aValue, aEntry.getKey ());
    return ret;
  }

  /**
   * Get a map where the lookup (1K..nV) has been reversed to (1V..nK)
   * 
   * @param <KEYTYPE>
   *        Original key type
   * @param <VALUETYPE>
   *        Original value type
   * @param aMap
   *        The input map to convert. May not be <code>null</code>
   * @return A swapped {@link HashMap}
   */
  @Nullable
  @ReturnsMutableCopy
  public static <KEYTYPE, VALUETYPE> IMultiMapSetBased <VALUETYPE, KEYTYPE> getReverseLookup (@Nullable final IMultiMapSetBased <KEYTYPE, VALUETYPE> aMap)
  {
    if (isEmpty (aMap))
      return null;

    final IMultiMapSetBased <VALUETYPE, KEYTYPE> aRet = new MultiHashMapHashSetBased <VALUETYPE, KEYTYPE> ();
    for (final Map.Entry <KEYTYPE, Set <VALUETYPE>> aEntry : aMap.entrySet ())
      for (final VALUETYPE aValue : aEntry.getValue ())
        aRet.putSingle (aValue, aEntry.getKey ());
    return aRet;
  }

  /**
   * Safe list element accessor method.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements on the list.
   * @param aList
   *        The list to extract from. May be <code>null</code>.
   * @param nIndex
   *        The index to access. Should be &ge; 0.
   * @return <code>null</code> if the element cannot be accessed.
   */
  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getSafe (@Nullable final List <ELEMENTTYPE> aList, final int nIndex)
  {
    return getSafe (aList, nIndex, null);
  }

  /**
   * Safe list element accessor method.
   * 
   * @param <ELEMENTTYPE>
   *        The type of elements on the list.
   * @param aList
   *        The list to extract from. May be <code>null</code>.
   * @param nIndex
   *        The index to access. Should be &ge; 0.
   * @param aDefault
   *        The value to be returned, if the index is out of bounds.
   * @return The default parameter if the element cannot be accessed.
   */
  @Nullable
  public static <ELEMENTTYPE> ELEMENTTYPE getSafe (@Nullable final List <ELEMENTTYPE> aList,
                                                   final int nIndex,
                                                   @Nullable final ELEMENTTYPE aDefault)
  {
    return aList != null && nIndex >= 0 && nIndex < aList.size () ? aList.get (nIndex) : aDefault;
  }

  /**
   * Check if the passed collection contains at least one <code>null</code>
   * element.
   * 
   * @param aCollection
   *        The collection to check. May be <code>null</code>.
   * @return <code>true</code> only if the passed collection is neither
   *         <code>null</code> nor empty and if at least one <code>null</code>
   *         element is contained.
   */
  public static boolean containsNullElement (@Nullable final Collection <?> aCollection)
  {
    if (aCollection != null)
      for (final Object aObj : aCollection)
        if (aObj == null)
          return true;
    return false;
  }
}
