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
package com.phloc.commons.concurrent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.math.MathHelper;
import com.phloc.commons.state.ESuccess;

/**
 * Some thread utility methods.
 * 
 * @author philip
 */
@Immutable
public final class ThreadUtils
{
  private ThreadUtils ()
  {}

  /**
   * Sleep the current thread for a certain amount of time
   * 
   * @param nMinutes
   *        The minutes to sleep.
   * @return {@link ESuccess#SUCCESS} if sleeping was not interrupted,
   *         {@link ESuccess#FAILURE} if sleeping was interrupted
   */
  @Nonnull
  public static ESuccess sleepMinutes (@Nonnegative final int nMinutes)
  {
    if (nMinutes < 0)
      throw new IllegalArgumentException ("Negative minutes: " + nMinutes);
    return sleep (MathHelper.getLongAsInt (nMinutes * CGlobal.MILLISECONDS_PER_MINUTE, -1));
  }

  /**
   * Sleep the current thread for a certain amount of time
   * 
   * @param nSeconds
   *        The seconds to sleep.
   * @return {@link ESuccess#SUCCESS} if sleeping was not interrupted,
   *         {@link ESuccess#FAILURE} if sleeping was interrupted
   */
  @Nonnull
  public static ESuccess sleepSeconds (@Nonnegative final int nSeconds)
  {
    if (nSeconds < 0)
      throw new IllegalArgumentException ("Negative seconds: " + nSeconds);
    return sleep (MathHelper.getLongAsInt (nSeconds * CGlobal.MILLISECONDS_PER_SECOND, -1));
  }

  /**
   * Sleep the current thread for a certain amount of time
   * 
   * @param nMilliseconds
   *        The milliseconds to sleep.
   * @return {@link ESuccess#SUCCESS} if sleeping was not interrupted,
   *         {@link ESuccess#FAILURE} if sleeping was interrupted
   */
  @Nonnull
  public static ESuccess sleep (@Nonnegative final int nMilliseconds)
  {
    if (nMilliseconds < 0)
      throw new IllegalArgumentException ("Negative milliseconds: " + nMilliseconds);
    try
    {
      Thread.sleep (nMilliseconds);
      return ESuccess.SUCCESS;
    }
    catch (final InterruptedException ex)
    {
      return ESuccess.FAILURE;
    }
  }
}
