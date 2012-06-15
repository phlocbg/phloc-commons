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
package com.phloc.commons.idfactory;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An implementation of {@link IIntIDFactory} using a single static
 * {@link AtomicInteger}.
 * 
 * @author philip
 */
@ThreadSafe
public final class MemoryStaticIntIDFactory implements IIntIDFactory
{
  public static final int INITIAL_ID = 10000;
  private static final AtomicInteger s_aID = new AtomicInteger (INITIAL_ID);

  public MemoryStaticIntIDFactory ()
  {}

  @Nonnegative
  public int getNewID ()
  {
    return s_aID.getAndIncrement ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MemoryStaticIntIDFactory))
      return false;
    return true;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
