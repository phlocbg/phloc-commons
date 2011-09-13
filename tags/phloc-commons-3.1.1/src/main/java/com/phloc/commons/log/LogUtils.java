/**
 * Copyright (C) 2006-2011 phloc systems
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
package com.phloc.commons.log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.error.EErrorLevel;

/**
 * Some utility functions to help integrating the {@link EErrorLevel} enum in
 * this package with SLF4J logger.
 * 
 * @author philip
 */
@Immutable
public final class LogUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final LogUtils s_aInstance = new LogUtils ();

  private LogUtils ()
  {}

  public static void log (@Nonnull final Logger aLogger,
                          @Nonnull final EErrorLevel eErrorLevel,
                          @Nonnull final String sMsg)
  {
    log (aLogger, eErrorLevel, sMsg, null);
  }

  public static void log (@Nonnull final Logger aLogger,
                          @Nonnull final EErrorLevel eErrorLevel,
                          @Nonnull final String sMsg,
                          @Nullable final Throwable t)
  {
    if (eErrorLevel.isMoreOrEqualSevereThan (EErrorLevel.ERROR))
      aLogger.error (sMsg, t);
    else
      if (eErrorLevel.isMoreOrEqualSevereThan (EErrorLevel.WARN))
        aLogger.warn (sMsg, t);
      else
        if (eErrorLevel.isMoreOrEqualSevereThan (EErrorLevel.INFO))
          aLogger.info (sMsg, t);
        else
          aLogger.debug (sMsg, t);
  }
}
