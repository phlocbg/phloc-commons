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
package org.apache.commons.collections.primitives.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.primitives.ByteCollection;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.ByteListIterator;
import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;
import org.apache.commons.collections.primitives.DoubleCollection;
import org.apache.commons.collections.primitives.DoubleIterator;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.collections.primitives.DoubleListIterator;
import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatIterator;
import org.apache.commons.collections.primitives.FloatList;
import org.apache.commons.collections.primitives.FloatListIterator;
import org.apache.commons.collections.primitives.IntCollection;
import org.apache.commons.collections.primitives.IntIterator;
import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.collections.primitives.IntListIterator;
import org.apache.commons.collections.primitives.LongCollection;
import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.collections.primitives.LongListIterator;
import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortIterator;
import org.apache.commons.collections.primitives.ShortList;
import org.apache.commons.collections.primitives.ShortListIterator;

/**
 * Convenience methods for constructing adapters.
 * 
 * @since Commons Primitives 1.1
 * @version $Revision: 480462 $ $Date: 2006-11-29 09:15:00 +0100 (Mi, 29 Nov
 *          2006) $
 * @author Rodney Waldhoff
 */
public final class Adapt
{
  public Adapt ()
  {}

  // to object based
  // ---------------------------------------------------------------

  public static final Collection <Byte> toCollection (final ByteCollection c)
  {
    return ByteCollectionCollection.wrap (c);
  }

  public static final Collection <Character> toCollection (final CharCollection c)
  {
    return CharCollectionCollection.wrap (c);
  }

  public static final Collection <Double> toCollection (final DoubleCollection c)
  {
    return DoubleCollectionCollection.wrap (c);
  }

  public static final Collection <Float> toCollection (final FloatCollection c)
  {
    return FloatCollectionCollection.wrap (c);
  }

  public static final Collection <Integer> toCollection (final IntCollection c)
  {
    return IntCollectionCollection.wrap (c);
  }

  public static final Collection <Long> toCollection (final LongCollection c)
  {
    return LongCollectionCollection.wrap (c);
  }

  public static final Collection <Short> toCollection (final ShortCollection c)
  {
    return ShortCollectionCollection.wrap (c);
  }

  public static final List <Byte> toList (final ByteList c)
  {
    return ByteListList.wrap (c);
  }

  public static final List <Character> toList (final CharList c)
  {
    return CharListList.wrap (c);
  }

  public static final List <Double> toList (final DoubleList c)
  {
    return DoubleListList.wrap (c);
  }

  public static final List <Float> toList (final FloatList c)
  {
    return FloatListList.wrap (c);
  }

  public static final List <Integer> toList (final IntList c)
  {
    return IntListList.wrap (c);
  }

  public static final List <Long> toList (final LongList c)
  {
    return LongListList.wrap (c);
  }

  public static final List <Short> toList (final ShortList c)
  {
    return ShortListList.wrap (c);
  }

  public static final Iterator <Byte> toIterator (final ByteIterator c)
  {
    return ByteIteratorIterator.wrap (c);
  }

  public static final Iterator <Character> toIterator (final CharIterator c)
  {
    return CharIteratorIterator.wrap (c);
  }

  public static final Iterator <Double> toIterator (final DoubleIterator c)
  {
    return DoubleIteratorIterator.wrap (c);
  }

  public static final Iterator <Float> toIterator (final FloatIterator c)
  {
    return FloatIteratorIterator.wrap (c);
  }

  public static final Iterator <Integer> toIterator (final IntIterator c)
  {
    return IntIteratorIterator.wrap (c);
  }

  public static final Iterator <Long> toIterator (final LongIterator c)
  {
    return LongIteratorIterator.wrap (c);
  }

  public static final Iterator <Short> toIterator (final ShortIterator c)
  {
    return ShortIteratorIterator.wrap (c);
  }

  public static final ListIterator <Byte> toListIterator (final ByteListIterator c)
  {
    return ByteListIteratorListIterator.wrap (c);
  }

  public static final ListIterator <Character> toListIterator (final CharListIterator c)
  {
    return CharListIteratorListIterator.wrap (c);
  }

  public static final ListIterator <Double> toListIterator (final DoubleListIterator c)
  {
    return DoubleListIteratorListIterator.wrap (c);
  }

  public static final ListIterator <Float> toListIterator (final FloatListIterator c)
  {
    return FloatListIteratorListIterator.wrap (c);
  }

  public static final ListIterator <Integer> toListIterator (final IntListIterator c)
  {
    return IntListIteratorListIterator.wrap (c);
  }

  public static final ListIterator <Long> toListIterator (final LongListIterator c)
  {
    return LongListIteratorListIterator.wrap (c);
  }

  public static final ListIterator <Short> toListIterator (final ShortListIterator c)
  {
    return ShortListIteratorListIterator.wrap (c);
  }

  // to byte based
  // ---------------------------------------------------------------

  public static final ByteCollection toByteCollection (final Collection <Byte> c)
  {
    return CollectionByteCollection.wrap (c);
  }

  public static final ByteList toByteList (final List <Byte> c)
  {
    return ListByteList.wrap (c);
  }

  public static final ByteIterator toByteIterator (final Iterator <Byte> c)
  {
    return IteratorByteIterator.wrap (c);
  }

  public static final ByteListIterator toByteListIterator (final ListIterator <Byte> c)
  {
    return ListIteratorByteListIterator.wrap (c);
  }

  // to char based
  // ---------------------------------------------------------------

  public static final CharCollection toCharCollection (final Collection <Character> c)
  {
    return CollectionCharCollection.wrap (c);
  }

  public static final CharList toCharList (final List <Character> c)
  {
    return ListCharList.wrap (c);
  }

  public static final CharIterator toCharIterator (final Iterator <Character> c)
  {
    return IteratorCharIterator.wrap (c);
  }

  public static final CharListIterator toCharListIterator (final ListIterator <Character> c)
  {
    return ListIteratorCharListIterator.wrap (c);
  }

  // to double based
  // ---------------------------------------------------------------

  public static final DoubleCollection toDoubleCollection (final Collection <Double> c)
  {
    return CollectionDoubleCollection.wrap (c);
  }

  public static final DoubleList toDoubleList (final List <Double> c)
  {
    return ListDoubleList.wrap (c);
  }

  public static final DoubleIterator toDoubleIterator (final Iterator <Double> c)
  {
    return IteratorDoubleIterator.wrap (c);
  }

  public static final DoubleListIterator toDoubleListIterator (final ListIterator <Double> c)
  {
    return ListIteratorDoubleListIterator.wrap (c);
  }

  // to float based
  // ---------------------------------------------------------------

  public static final FloatCollection toFloatCollection (final Collection <Float> c)
  {
    return CollectionFloatCollection.wrap (c);
  }

  public static final FloatList toFloatList (final List <Float> c)
  {
    return ListFloatList.wrap (c);
  }

  public static final FloatIterator toFloatIterator (final Iterator <Float> c)
  {
    return IteratorFloatIterator.wrap (c);
  }

  public static final FloatListIterator toFloatListIterator (final ListIterator <Float> c)
  {
    return ListIteratorFloatListIterator.wrap (c);
  }

  // to int based
  // ---------------------------------------------------------------

  public static final IntCollection toIntCollection (final Collection <Integer> c)
  {
    return CollectionIntCollection.wrap (c);
  }

  public static final IntList toIntList (final List <Integer> c)
  {
    return ListIntList.wrap (c);
  }

  public static final IntIterator toIntIterator (final Iterator <Integer> c)
  {
    return IteratorIntIterator.wrap (c);
  }

  public static final IntListIterator toIntListIterator (final ListIterator <Integer> c)
  {
    return ListIteratorIntListIterator.wrap (c);
  }

  // to long based
  // ---------------------------------------------------------------

  public static final LongCollection toLongCollection (final Collection <Long> c)
  {
    return CollectionLongCollection.wrap (c);
  }

  public static final LongList toLongList (final List <Long> c)
  {
    return ListLongList.wrap (c);
  }

  public static final LongIterator toLongIterator (final Iterator <Long> c)
  {
    return IteratorLongIterator.wrap (c);
  }

  public static final LongListIterator toLongListIterator (final ListIterator <Long> c)
  {
    return ListIteratorLongListIterator.wrap (c);
  }

  // to short based
  // ---------------------------------------------------------------

  public static final ShortCollection toShortCollection (final Collection <Short> c)
  {
    return CollectionShortCollection.wrap (c);
  }

  public static final ShortList toShortList (final List <Short> c)
  {
    return ListShortList.wrap (c);
  }

  public static final ShortIterator toShortIterator (final Iterator <Short> c)
  {
    return IteratorShortIterator.wrap (c);
  }

  public static final ShortListIterator toShortListIterator (final ListIterator <Short> c)
  {
    return ListIteratorShortListIterator.wrap (c);
  }
}
