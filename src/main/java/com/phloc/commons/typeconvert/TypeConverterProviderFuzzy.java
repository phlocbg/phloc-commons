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
package com.phloc.commons.typeconvert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A fuzzy type converter provider. Implemented as a singleton.
 * 
 * @author philip
 */
public final class TypeConverterProviderFuzzy implements ITypeConverterProvider
{
  private static final TypeConverterProviderFuzzy s_aInstance = new TypeConverterProviderFuzzy ();

  private TypeConverterProviderFuzzy ()
  {}

  @Nonnull
  public static final TypeConverterProviderFuzzy getInstance ()
  {
    return s_aInstance;
  }

  @Nullable
  public ITypeConverter getTypeConverter (@Nonnull final Class <?> aSrcClass, @Nonnull final Class <?> aDstClass)
  {
    return TypeConverterRegistry.getFuzzyConverter (aSrcClass, aDstClass);
  }
}
