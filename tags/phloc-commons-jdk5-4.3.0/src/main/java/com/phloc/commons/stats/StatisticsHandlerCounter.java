/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.stats;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;

/**
 * Default implementation of {@link IStatisticsHandlerCounter}
 * 
 * @author Philip Helger
 */
@ThreadSafe
final class StatisticsHandlerCounter implements IStatisticsHandlerCounter
{
  private final AtomicInteger m_aInvocationCount = new AtomicInteger ();
  private final AtomicLong m_aCount = new AtomicLong ();

  @Nonnegative
  public int getInvocationCount ()
  {
    return m_aInvocationCount.intValue ();
  }

  @CheckForSigned
  public long getCount ()
  {
    return m_aCount.longValue ();
  }

  public void increment ()
  {
    m_aInvocationCount.incrementAndGet ();
    m_aCount.incrementAndGet ();
  }

  public void increment (final long nByHowMany)
  {
    m_aInvocationCount.incrementAndGet ();
    m_aCount.addAndGet (nByHowMany);
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    return "invocations=" + getInvocationCount () + ";count=" + getCount ();
  }
}
