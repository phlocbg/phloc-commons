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
package com.phloc.commons.timing;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.CGlobal;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IStoppable;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Simple stop watch based on {@link System#nanoTime()}.
 * 
 * @author philip
 */
@NotThreadSafe
public final class StopWatch implements IStoppable
{
  public static final boolean DEFAULT_START = false;

  private long m_nStartDT;
  private long m_nDurationNanos;

  /**
   * Create a new stop watch that is not yet started.
   */
  public StopWatch ()
  {
    this (DEFAULT_START);
  }

  /**
   * Constructor.
   * 
   * @param bStart
   *        if <code>true</code> the stop watch is directly started!
   */
  public StopWatch (final boolean bStart)
  {
    if (bStart)
      start ();
  }

  /**
   * Reset all saved durations, in case this stop watch is to be used in a loop.
   * Does not change the start/stop state.
   * 
   * @return {@link EChange}.
   */
  @Nonnull
  public EChange reset ()
  {
    if (m_nDurationNanos == 0)
      return EChange.UNCHANGED;
    m_nDurationNanos = 0;
    return EChange.CHANGED;
  }

  /**
   * Start the stop watch.
   * 
   * @return {@link EChange}.
   */
  @Nonnull
  public EChange start ()
  {
    if (m_nStartDT > 0)
      return EChange.UNCHANGED;
    m_nStartDT = System.nanoTime ();
    return EChange.CHANGED;
  }

  /**
   * Stop the stop watch.
   * 
   * @return {@link EChange#CHANGED} if the stop watch was previously running
   *         and is now stopped, and {@link EChange#UNCHANGED} if the stop watch
   *         was already stopped.
   */
  @Nonnull
  public EChange stop ()
  {
    if (m_nStartDT == 0)
      return EChange.UNCHANGED;
    m_nDurationNanos += (System.nanoTime () - m_nStartDT);
    m_nStartDT = 0;
    return EChange.CHANGED;
  }

  public boolean isStarted ()
  {
    return m_nStartDT != 0;
  }

  /**
   * @return The elapsed nano seconds (1000 nano seconds = 1 milli second).
   */
  public long getNanos ()
  {
    return m_nDurationNanos;
  }

  /**
   * @return The elapsed milli seconds.
   */
  public long getMillis ()
  {
    return m_nDurationNanos / CGlobal.NANOSECONDS_PER_MILLISECOND;
  }

  /**
   * @return The elapsed seconds.
   */
  public long getSeconds ()
  {
    return m_nDurationNanos / CGlobal.NANOSECONDS_PER_SECOND;
  }

  /**
   * Stop the stop watch and get the elapsed seconds since the start. If the
   * stop watch was started and stopped multiple times, the duration is added.
   * 
   * @return The elapsed seconds or 0 if the stop watch was never started.
   */
  public long stopAndGetSeconds ()
  {
    stop ();
    return getSeconds ();
  }

  /**
   * Stop the stop watch and get the elapsed milliseconds since the start. If
   * the stop watch was started and stopped multiple times, the duration is
   * added.
   * 
   * @return The elapsed milli seconds or 0 if the stop watch was never started.
   */
  public long stopAndGetMillis ()
  {
    stop ();
    return getMillis ();
  }

  /**
   * Stop the stop watch and get the elapsed nanoseconds since the start. If the
   * stop watch was started and stopped multiple times, the duration is added.
   * 
   * @return The elapsed nano seconds or 0 if the stop watch was never started.
   */
  public long stopAndGetNanos ()
  {
    stop ();
    return getNanos ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("startDT", m_nStartDT)
                                       .append ("duration", m_nDurationNanos)
                                       .toString ();
  }
}
