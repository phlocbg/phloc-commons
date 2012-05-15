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
package com.phloc.commons.xml.transform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.xml.transform.ErrorListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.CodingStyleguideUnaware;
import com.phloc.commons.error.IResourceError;
import com.phloc.commons.log.LogUtils;

/**
 * java.xml.transform error listener that simply logs data to a logger.
 * 
 * @author philip
 */
@Immutable
@CodingStyleguideUnaware ("logger too visible by purpose")
public class LoggingTransformErrorListener extends AbstractTransformErrorListener
{
  protected static final Logger s_aLogger = LoggerFactory.getLogger (LoggingTransformErrorListener.class);
  private static final LoggingTransformErrorListener s_aInstance = new LoggingTransformErrorListener ();

  public LoggingTransformErrorListener ()
  {}

  public LoggingTransformErrorListener (@Nullable final ErrorListener aWrappedErrorListener)
  {
    super (aWrappedErrorListener);
  }

  @Nonnull
  public static LoggingTransformErrorListener getInstance ()
  {
    return s_aInstance;
  }

  @Override
  protected void log (@Nonnull final IResourceError aResError)
  {
    LogUtils.log (s_aLogger, aResError.getErrorLevel (), aResError.getAsString ());
  }
}
