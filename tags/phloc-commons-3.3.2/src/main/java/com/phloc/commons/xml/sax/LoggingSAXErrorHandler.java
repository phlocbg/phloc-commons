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
package com.phloc.commons.xml.sax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.log.LogUtils;

/**
 * java.xml error handler that simply logs data to a logger.
 * 
 * @author philip
 */
public final class LoggingSAXErrorHandler extends AbstractSAXErrorHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingSAXErrorHandler.class);
  private static final LoggingSAXErrorHandler s_aInstance = new LoggingSAXErrorHandler ();

  private LoggingSAXErrorHandler ()
  {}

  public static LoggingSAXErrorHandler getInstance ()
  {
    return s_aInstance;
  }

  @Override
  protected void log (final EErrorLevel eErrorLevel, final SAXParseException aException)
  {
    LogUtils.log (s_aLogger, eErrorLevel, "SAX " + getSaxParseError (eErrorLevel, aException).getAsString ());
  }
}
