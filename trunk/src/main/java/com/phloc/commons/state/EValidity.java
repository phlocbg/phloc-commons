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
package com.phloc.commons.state;

import javax.annotation.Nonnull;

/**
 * Small enum for checks to indicate valid or invalid.
 * 
 * @author philip
 */
public enum EValidity implements IValidityIndicator
{
  VALID,
  INVALID;

  public boolean isValid ()
  {
    return this == VALID;
  }

  public boolean isInvalid ()
  {
    return this == INVALID;
  }

  @Nonnull
  public EValidity or (@Nonnull final IValidityIndicator aValidity)
  {
    return valueOf (isValid () || aValidity.isValid ());
  }

  @Nonnull
  public EValidity and (@Nonnull final IValidityIndicator aValidity)
  {
    return valueOf (isValid () && aValidity.isValid ());
  }

  @Nonnull
  public static EValidity valueOf (final boolean bValidity)
  {
    return bValidity ? VALID : INVALID;
  }

  @Nonnull
  public static EValidity valueOf (@Nonnull final IValidityIndicator aValidityIndicator)
  {
    return valueOf (aValidityIndicator.isValid ());
  }
}
