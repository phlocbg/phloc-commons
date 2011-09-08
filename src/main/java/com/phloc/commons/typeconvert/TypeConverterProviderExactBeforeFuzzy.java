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
 * An type converter provider that tries to provide an exact match before trying
 * fuzzy matches. This should be the preferred type converter provider.
 * Implemented as a singleton.
 * 
 * @author philip
 */
public final class TypeConverterProviderExactBeforeFuzzy implements ITypeConverterProvider
{
  private static final TypeConverterProviderExactBeforeFuzzy s_aInstance = new TypeConverterProviderExactBeforeFuzzy ();

  private TypeConverterProviderExactBeforeFuzzy ()
  {}

  @Nonnull
  public static TypeConverterProviderExactBeforeFuzzy getInstance ()
  {
    return s_aInstance;
  }

  @Nullable
  public ITypeConverter getTypeConverter (@Nonnull final Class <?> aSrcClass, @Nonnull final Class <?> aDstClass)
  {
    ITypeConverter ret = TypeConverterRegistry.getExactConverter (aSrcClass, aDstClass);
    if (ret == null)
    {
      // No exact match was found -> try fuzzy converter
      ret = TypeConverterRegistry.getFuzzyConverter (aSrcClass, aDstClass);
    }
    return ret;
  }
}
