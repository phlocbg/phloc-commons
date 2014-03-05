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
package com.phloc.commons.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.state.EInterrupt;

/**
 * Wrapper around an {@link ExecutorService} with additional helper methods.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class ManagedExecutorService
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ManagedExecutorService.class);
  private final ExecutorService m_aES;

  public ManagedExecutorService (@Nonnull final ExecutorService aES)
  {
    if (aES == null)
      throw new NullPointerException ("executorService");
    m_aES = aES;
  }

  /**
   * Call shutdown on the {@link ExecutorService} and wait indefinitely until it
   * terminated.
   * 
   * @return {@link EInterrupt#INTERRUPTED} if the executor service was
   *         interrupted while awaiting termination. Never <code>null</code>.
   */
  @Nonnull
  public EInterrupt shutdownAndWaitUntilAllTasksAreFinished ()
  {
    if (!m_aES.isShutdown ())
    {
      // accept no further requests
      m_aES.shutdown ();

      try
      {
        while (!m_aES.awaitTermination (1, TimeUnit.SECONDS))
        {// NOPMD
         // wait until completion
        }
      }
      catch (final InterruptedException ex)
      {
        s_aLogger.error ("Error waiting for Executor service " + m_aES + " to end", ex);
        return EInterrupt.INTERRUPTED;
      }
    }
    return EInterrupt.NOT_INTERRUPTED;
  }

  @Nonnull
  public static EInterrupt shutdownAndWaitUntilAllTasksAreFinished (@Nonnull final ExecutorService aES)
  {
    return new ManagedExecutorService (aES).shutdownAndWaitUntilAllTasksAreFinished ();
  }
}
