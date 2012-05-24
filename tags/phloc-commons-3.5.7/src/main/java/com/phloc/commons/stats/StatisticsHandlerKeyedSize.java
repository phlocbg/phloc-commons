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
package com.phloc.commons.stats;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link IStatisticsHandlerKeyedSize}
 * 
 * @author philip
 */
@ThreadSafe
final class StatisticsHandlerKeyedSize extends AbstractStatisticsHandlerKeyedNumeric implements
                                                                                    IStatisticsHandlerKeyedSize
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (StatisticsHandlerKeyedSize.class);

  public void addSize (@Nullable final String sKey, @Nonnegative final long nSize)
  {
    if (nSize < 0)
      s_aLogger.warn ("A negative value (" + nSize + ") for key '" + sKey + "' is added to " + getClass ().getName ());
    addValue (sKey, nSize);
  }
}
