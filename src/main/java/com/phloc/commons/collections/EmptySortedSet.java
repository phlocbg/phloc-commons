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
package com.phloc.commons.collections;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.iterate.EmptyIterator;
import com.phloc.commons.lang.GenericReflection;

/**
 * Default implementation of an empty sorted set.
 * 
 * @author philip
 */
public final class EmptySortedSet extends AbstractSet <Object> implements SortedSet <Object>
{
  private static final EmptySortedSet INSTANCE = new EmptySortedSet ();

  private EmptySortedSet ()
  {}

  @Override
  public Iterator <Object> iterator ()
  {
    return EmptyIterator.<Object> getInstance ();
  }

  @Override
  public int size ()
  {
    return 0;
  }

  @Override
  public boolean contains (final Object obj)
  {
    return false;
  }

  @Override
  @Nullable
  public Comparator <Object> comparator ()
  {
    return null;
  }

  @Override
  public SortedSet <Object> subSet (final Object fromElement, final Object toElement)
  {
    return this;
  }

  @Override
  public SortedSet <Object> headSet (final Object toElement)
  {
    return this;
  }

  @Override
  public SortedSet <Object> tailSet (final Object fromElement)
  {
    return this;
  }

  @Override
  public Object first ()
  {
    return null;
  }

  @Override
  public Object last ()
  {
    return null;
  }

  // ESCA-JAVA0029:
  // Preserves singleton property
  private Object readResolve ()
  {
    return INSTANCE;
  }

  @Nonnull
  public static <ELEMENTTYPE> SortedSet <ELEMENTTYPE> getInstance ()
  {
    return GenericReflection.<EmptySortedSet, SortedSet <ELEMENTTYPE>> uncheckedCast (INSTANCE);
  }
}
