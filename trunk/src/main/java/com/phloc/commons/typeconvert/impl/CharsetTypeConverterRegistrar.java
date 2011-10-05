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
package com.phloc.commons.typeconvert.impl;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;

/**
 * Register the locale specific type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class CharsetTypeConverterRegistrar implements ITypeConverterRegistrarSPI
{
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    final ITypeConverter aConverterToString = new ITypeConverter ()
    {
      @Nonnull
      public String convert (final Object aSource)
      {
        return aSource.toString ();
      }
    };

    // Charset
    aRegistry.registerTypeConverter (Charset.class, String.class, aConverterToString);
    aRegistry.registerTypeConverter (String.class, Charset.class, new ITypeConverter ()
    {
      public Charset convert (final Object aSource)
      {
        return CharsetManager.charsetFromName ((String) aSource);
      }
    });
  }
}
