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
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.primitives;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Abstract base class for {@link IntCollection}s.
 * <p/>
 * Read-only subclasses must override {@link #iterator} and {@link #size}.
 * Mutable subclasses should also override {@link #add} and
 * {@link IntIterator#remove IntIterator.remove}. All other methods have at
 * least some base implementation derived from these. Subclasses may choose to
 * override these methods to provide a more efficient implementation.
 *
 * @since Commons Primitives 1.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 */
public abstract class AbstractIntCollection implements IntCollection
{
  protected AbstractIntCollection ()
  {}

  @Nonnull
  public abstract IntIterator iterator ();

  @Nonnegative
  public abstract int size ();

  /** Unsupported in this base implementation. */
  public boolean add (final int aElement)
  {
    throw new UnsupportedOperationException ("add(int) is not supported.");
  }

  public boolean addAll (@Nonnull final IntCollection aCont)
  {
    boolean bModified = false;
    for (final IntIterator aIter = aCont.iterator (); aIter.hasNext ();)
      bModified |= add (aIter.next ());
    return bModified;
  }

  public void clear ()
  {
    for (final IntIterator aIter = iterator (); aIter.hasNext ();)
    {
      aIter.next ();
      aIter.remove ();
    }
  }

  public boolean contains (final int aElement)
  {
    for (final IntIterator aIter = iterator (); aIter.hasNext ();)
      if (aIter.next () == aElement)
        return true;
    return false;
  }

  public boolean containsAll (@Nonnull final IntCollection aCont)
  {
    for (final IntIterator aIter = aCont.iterator (); aIter.hasNext ();)
      if (!contains (aIter.next ()))
        return false;
    return true;
  }

  public boolean isEmpty ()
  {
    return 0 == size ();
  }

  public boolean removeElement (final int aElement)
  {
    for (final IntIterator aIter = iterator (); aIter.hasNext ();)
      if (aIter.next () == aElement)
      {
        aIter.remove ();
        return true;
      }
    return false;
  }

  public boolean removeAll (@Nonnull final IntCollection aCont)
  {
    boolean bModified = false;
    for (final IntIterator aIter = aCont.iterator (); aIter.hasNext ();)
      bModified |= removeElement (aIter.next ());
    return bModified;
  }

  public boolean retainAll (@Nonnull final IntCollection aCont)
  {
    boolean bModified = false;
    for (final IntIterator aIter = iterator (); aIter.hasNext ();)
      if (!aCont.contains (aIter.next ()))
      {
        aIter.remove ();
        bModified = true;
      }
    return bModified;
  }

  @Nonnull
  @ReturnsMutableCopy
  public int [] toArray ()
  {
    final int [] ret = new int [size ()];
    int i = 0;
    for (final IntIterator aIter = iterator (); aIter.hasNext ();)
      ret[i++] = aIter.next ();
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public int [] toArray (@Nonnull final int [] aTarget)
  {
    if (aTarget.length < size ())
      return toArray ();
    int i = 0;
    for (final IntIterator aIter = iterator (); aIter.hasNext ();)
      aTarget[i++] = aIter.next ();
    return aTarget;
  }
}
