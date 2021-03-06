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

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link IStatisticsHandlerTimer}
 * 
 * @author Philip Helger
 */
@ThreadSafe
final class StatisticsHandlerTimer extends AbstractStatisticsHandlerNumeric implements IStatisticsHandlerTimer
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (StatisticsHandlerTimer.class);

  public void addTime (@Nonnegative final long nMillis)
  {
    if (nMillis < 0)
      s_aLogger.warn ("A negative value (" + nMillis + ") is added to " + getClass ().getName ());
    addValue (nMillis);
  }
}
