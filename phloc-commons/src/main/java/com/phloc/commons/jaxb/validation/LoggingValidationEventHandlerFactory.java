/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.jaxb.validation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.xml.bind.ValidationEventHandler;

/**
 * Implementation of {@link IValidationEventHandlerFactory} creating
 * {@link LoggingValidationEventHandler} objects.
 * 
 * @author Philip Helger
 */
public class LoggingValidationEventHandlerFactory implements IValidationEventHandlerFactory
{
  /**
   * By default the old event handler is encapsulated into the created
   * {@link LoggingValidationEventHandler}.
   */
  public static final boolean DEFAULT_ENCAPSULATE_HANDLER = true;

  private final boolean m_bEncapsulateHandler;

  public LoggingValidationEventHandlerFactory ()
  {
    this (DEFAULT_ENCAPSULATE_HANDLER);
  }

  public LoggingValidationEventHandlerFactory (final boolean bEncapsulateHandler)
  {
    m_bEncapsulateHandler = bEncapsulateHandler;
  }

  @Nonnull
  public LoggingValidationEventHandler create (@Nullable final ValidationEventHandler aOldEventHandler)
  {
    return new LoggingValidationEventHandler (m_bEncapsulateHandler ? aOldEventHandler : null);
  }

}
