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
package com.phloc.commons.jaxb.validation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.bind.ValidationEventHandler;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IHasResourceErrorGroup;
import com.phloc.commons.error.IResourceError;
import com.phloc.commons.error.IResourceErrorGroup;
import com.phloc.commons.error.ResourceErrorGroup;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An implementation of the JAXB {@link ValidationEventHandler} interface. It
 * collects all events that occurred!
 * 
 * @author philip
 */
@NotThreadSafe
public class CollectingValidationEventHandler extends AbstractValidationEventHandler implements IHasResourceErrorGroup
{
  private final ResourceErrorGroup m_aErrors = new ResourceErrorGroup ();

  public CollectingValidationEventHandler ()
  {}

  public CollectingValidationEventHandler (@Nullable final ValidationEventHandler aOrigHandler)
  {
    super (aOrigHandler);
  }

  @Override
  protected void onEvent (@Nonnull final IResourceError aEvent)
  {
    m_aErrors.addResourceError (aEvent);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IResourceErrorGroup getResourceErrors ()
  {
    return m_aErrors.getClone ();
  }

  /**
   * Get the most severe error level within this group.
   * 
   * @return {@link EErrorLevel#SUCCESS} if no resource error is contained, the
   *         most severe error level otherwise.
   */
  @Nonnull
  public EErrorLevel getMostSevereErrorLevel ()
  {
    return m_aErrors.getMostSevereErrorLevel ();
  }

  /**
   * Get the number of resource errors on any error level.
   * 
   * @return A non-negative number of resource errors contained.
   */
  @Nonnegative
  public int getResourceErrorCount ()
  {
    return m_aErrors.size ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("errors", m_aErrors).toString ();
  }
}
