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
 * Abstract base class for {@link DoubleCollection}s.
 * <p/>
 * Read-only subclasses must override {@link #iterator} and {@link #size}.
 * Mutable subclasses should also override {@link #add} and
 * {@link DoubleIterator#remove DoubleIterator.remove}. All other methods have at
 * least some base implementation derived from these. Subclasses may choose to
 * override these methods to provide a more efficient implementation.
 *
 * @since Commons Primitives 1.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 */
public abstract class AbstractDoubleCollection implements DoubleCollection
{
  protected AbstractDoubleCollection ()
  {}

  @Nonnull
  public abstract DoubleIterator iterator ();

  @Nonnegative
  public abstract int size ();

  /** 
   * Unsupported in this base implementation.
   *
   * @return never 
   */
  public boolean add (final double aElement)
  {
    throw new UnsupportedOperationException ("add(double) is not supported.");
  }

  public boolean addAll (@Nonnull final DoubleCollection aCont)
  {
    boolean bModified = false;
    for (final DoubleIterator aIter = aCont.iterator (); aIter.hasNext ();)
      bModified |= add (aIter.next ());
    return bModified;
  }

  public void clear ()
  {
    for (final DoubleIterator aIter = iterator (); aIter.hasNext ();)
    {
      aIter.next ();
      aIter.remove ();
    }
  }

  public boolean contains (final double aElement)
  {
    for (final DoubleIterator aIter = iterator (); aIter.hasNext ();)
      if (aIter.next () == aElement)
        return true;
    return false;
  }

  public boolean containsAll (@Nonnull final DoubleCollection aCont)
  {
    for (final DoubleIterator aIter = aCont.iterator (); aIter.hasNext ();)
      if (!contains (aIter.next ()))
        return false;
    return true;
  }

  public boolean isEmpty ()
  {
    return 0 == size ();
  }

  public boolean removeElement (final double aElement)
  {
    for (final DoubleIterator aIter = iterator (); aIter.hasNext ();)
      if (aIter.next () == aElement)
      {
        aIter.remove ();
        return true;
      }
    return false;
  }

  public boolean removeAll (@Nonnull final DoubleCollection aCont)
  {
    boolean bModified = false;
    for (final DoubleIterator aIter = aCont.iterator (); aIter.hasNext ();)
      bModified |= removeElement (aIter.next ());
    return bModified;
  }

  public boolean retainAll (@Nonnull final DoubleCollection aCont)
  {
    boolean bModified = false;
    for (final DoubleIterator aIter = iterator (); aIter.hasNext ();)
      if (!aCont.contains (aIter.next ()))
      {
        aIter.remove ();
        bModified = true;
      }
    return bModified;
  }

  @Nonnull
  @ReturnsMutableCopy
  public double [] toArray ()
  {
    final double [] ret = new double [size ()];
    int i = 0;
    for (final DoubleIterator aIter = iterator (); aIter.hasNext ();)
      ret[i++] = aIter.next ();
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public double [] toArray (@Nonnull final double [] aTarget)
  {
    if (aTarget.length < size ())
      return toArray ();
    int i = 0;
    for (final DoubleIterator aIter = iterator (); aIter.hasNext ();)
      aTarget[i++] = aIter.next ();
    return aTarget;
  }
}
