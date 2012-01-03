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
 * Small enum for setter method to identify whether a value is mandatory or not.
 * 
 * @author philip
 */
public enum EMandatory implements IMandatoryIndicator
{
  MANDATORY,
  OPTIONAL;

  public boolean isMandatory ()
  {
    return this == MANDATORY;
  }

  public boolean isOptional ()
  {
    return this == OPTIONAL;
  }

  @Nonnull
  public EMandatory or (@Nonnull final IMandatoryIndicator aMandatory)
  {
    return valueOf (isMandatory () || aMandatory.isMandatory ());
  }

  @Nonnull
  public EMandatory and (@Nonnull final IMandatoryIndicator aMandatory)
  {
    return valueOf (isMandatory () && aMandatory.isMandatory ());
  }

  @Nonnull
  public static EMandatory valueOf (final boolean bMandatory)
  {
    return bMandatory ? MANDATORY : OPTIONAL;
  }

  @Nonnull
  public static EMandatory valueOf (@Nonnull final IMandatoryIndicator aMandatoryIndicator)
  {
    return valueOf (aMandatoryIndicator.isMandatory ());
  }
}
