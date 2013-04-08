/**
 * Copyright (C) 2006-2013 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import com.phloc.commons.error.EErrorLevel;

/**
 * java.xml error handler that ignores all errors.
 * 
 * @author philip
 */
@Immutable
public class DoNothingSAXErrorHandler extends AbstractSAXErrorHandler
{
  private static final DoNothingSAXErrorHandler s_aInstance = new DoNothingSAXErrorHandler ();

  public DoNothingSAXErrorHandler ()
  {}

  public DoNothingSAXErrorHandler (@Nullable final ErrorHandler aWrappedErrorHandler)
  {
    super (aWrappedErrorHandler);
  }

  @Nonnull
  public static DoNothingSAXErrorHandler getInstance ()
  {
    return s_aInstance;
  }

  @Override
  protected void internalLog (@Nonnull final EErrorLevel eErrorLevel, final SAXParseException aException)
  {}
}
