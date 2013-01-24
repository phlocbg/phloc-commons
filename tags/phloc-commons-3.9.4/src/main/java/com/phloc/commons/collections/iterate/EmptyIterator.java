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
package com.phloc.commons.collections.iterate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation of an empty enumerator.
 * 
 * @author philip
 * @param <ELEMENTTYPE>
 *        The pseudo element type to iterate
 */
@ThreadSafe
public final class EmptyIterator <ELEMENTTYPE> implements Iterator <ELEMENTTYPE>
{
  private static EmptyIterator <Object> s_aInstance = new EmptyIterator <Object> ();

  private EmptyIterator ()
  {}

  public boolean hasNext ()
  {
    return false;
  }

  public ELEMENTTYPE next ()
  {
    throw new NoSuchElementException ();
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  public boolean equals (final Object o)
  {
    // Singleton object!
    return o == this;
  }

  @Override
  public int hashCode ()
  {
    // Singleton object!
    return System.identityHashCode (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }

  @Nonnull
  public static <ELEMENTTYPE> EmptyIterator <ELEMENTTYPE> getInstance ()
  {
    return GenericReflection.<EmptyIterator <Object>, EmptyIterator <ELEMENTTYPE>> uncheckedCast (s_aInstance);
  }
}
