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
package com.phloc.commons.convert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.name.IHasName;

/**
 * A unidirectional converter that extracts the name from an object implementing
 * {@link IHasName}.
 * 
 * @author Philip Helger
 */
public final class UnidirectionalConverterHasNameString implements IUnidirectionalConverter <IHasName, String>
{
  private static final UnidirectionalConverterHasNameString s_aInstance = new UnidirectionalConverterHasNameString ();

  private UnidirectionalConverterHasNameString ()
  {}

  @Nullable
  public String convert (@Nullable final IHasName aInput)
  {
    return aInput == null ? null : aInput.getName ();
  }

  @Nonnull
  public static UnidirectionalConverterHasNameString getInstance ()
  {
    return s_aInstance;
  }
}
