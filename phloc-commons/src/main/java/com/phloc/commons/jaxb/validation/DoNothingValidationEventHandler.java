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
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.error.IResourceError;

/**
 * An implementation of the JAXB {@link jakarta.xml.bind.ValidationEventHandler}
 * interface that does nothing an swallows all errors.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class DoNothingValidationEventHandler extends AbstractValidationEventHandler
{
  private static final DoNothingValidationEventHandler s_aInstance = new DoNothingValidationEventHandler ();

  public DoNothingValidationEventHandler ()
  {
    super ();
  }

  @Nonnull
  public static DoNothingValidationEventHandler getInstance ()
  {
    return s_aInstance;
  }

  @Override
  protected void onEvent (@Nonnull final IResourceError aEvent)
  {
    // Do nothing
  }
}
