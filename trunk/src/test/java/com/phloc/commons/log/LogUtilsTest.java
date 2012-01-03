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
package com.phloc.commons.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.exceptions.LoggedException;
import com.phloc.commons.exceptions.LoggedRuntimeException;

/**
 * Test class for class {@link LogUtils}.
 * 
 * @author philip
 */
public final class LogUtilsTest
{
  @Test
  public void testAll ()
  {
    final Logger aLogger = LoggerFactory.getLogger (LogUtilsTest.class);
    for (final EErrorLevel eLevel : EErrorLevel.values ())
    {
      LogUtils.log (aLogger, eLevel, "my message");
      LogUtils.log (aLogger, eLevel, "my message with exception", new LoggedException ());
      LogUtils.log (aLogger, eLevel, "my message with exception", new LoggedRuntimeException ());
    }
  }
}
