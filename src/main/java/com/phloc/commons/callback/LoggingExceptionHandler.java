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
package com.phloc.commons.callback;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.mock.IMockException;

/**
 * A specific implementation of the {@link IExceptionHandler} interface, that
 * logs all exceptions to a standard logger.
 * 
 * @author philip
 */
public class LoggingExceptionHandler implements IExceptionHandler <Throwable>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingExceptionHandler.class);

  public void onException (@Nullable final Throwable t)
  {
    s_aLogger.warn ("Exception occurred", t instanceof IMockException ? null : t);
  }
}
