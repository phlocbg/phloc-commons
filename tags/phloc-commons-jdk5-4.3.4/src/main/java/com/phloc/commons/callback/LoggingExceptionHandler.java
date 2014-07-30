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
package com.phloc.commons.callback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IHasErrorLevel;
import com.phloc.commons.log.LogUtils;
import com.phloc.commons.mock.IMockException;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A specific implementation of the {@link IExceptionHandler} interface, that
 * logs all exceptions to a standard logger.
 * 
 * @author Philip Helger
 */
public class LoggingExceptionHandler implements IExceptionHandler <Throwable>, IHasErrorLevel
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingExceptionHandler.class);

  private final EErrorLevel m_eErrorLevel;

  public LoggingExceptionHandler ()
  {
    this (EErrorLevel.ERROR);
  }

  public LoggingExceptionHandler (@Nonnull final EErrorLevel eErrorLevel)
  {
    m_eErrorLevel = ValueEnforcer.notNull (eErrorLevel, "ErrorLevel");
  }

  /**
   * @return The configured error level. Never <code>null</code>.
   */
  @Nonnull
  public EErrorLevel getErrorLevel ()
  {
    return m_eErrorLevel;
  }

  /**
   * Get the text to be logged for a certain exception
   * 
   * @param t
   *        The exception to be logged. May theoretically be <code>null</code>.
   * @return The text to be logged. May neither be <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getLogMessage (@Nullable final Throwable t)
  {
    if (t == null)
      return "An error occurred";
    return "Exception occurred";
  }

  /**
   * Check if the passed exception should be part of the log entry.
   * 
   * @param t
   *        The exception to check. May theoretically be <code>null</code>.
   * @return <code>true</code> to log the exception, <code>false</code> to not
   *         log it
   */
  @OverrideOnDemand
  protected boolean isLogException (@Nullable final Throwable t)
  {
    return !(t instanceof IMockException);
  }

  public void onException (@Nullable final Throwable t)
  {
    final String sLogMessage = getLogMessage (t);
    final boolean bLogException = isLogException (t);
    LogUtils.log (s_aLogger, m_eErrorLevel, sLogMessage, bLogException ? t : null);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
