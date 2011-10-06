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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.state.EEnabled;
import com.phloc.commons.state.EFinish;
import com.phloc.commons.state.EInterrupt;
import com.phloc.commons.state.ELeftRight;
import com.phloc.commons.state.EMandatory;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ETopBottom;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.state.EValidity;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;

/**
 * Register the state specific type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class StateEnumConverterRegistrar implements ITypeConverterRegistrarSPI
{
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    final ITypeConverter aEnumToStringConverter = new ITypeConverter ()
    {
      @Nonnull
      public String convert (final Object aSource)
      {
        return ((Enum <?>) aSource).name ();
      }
    };

    // EChange
    aRegistry.registerTypeConverter (EChange.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EChange.class, new ITypeConverter ()
    {
      @Nonnull
      public EChange convert (final Object aSource)
      {
        return EChange.valueOf ((String) aSource);
      }
    });

    // EContinue
    aRegistry.registerTypeConverter (EContinue.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EContinue.class, new ITypeConverter ()
    {
      @Nonnull
      public EContinue convert (final Object aSource)
      {
        return EContinue.valueOf ((String) aSource);
      }
    });

    // EEnabled
    aRegistry.registerTypeConverter (EEnabled.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EEnabled.class, new ITypeConverter ()
    {
      @Nonnull
      public EEnabled convert (final Object aSource)
      {
        return EEnabled.valueOf ((String) aSource);
      }
    });

    // EFinish
    aRegistry.registerTypeConverter (EFinish.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EFinish.class, new ITypeConverter ()
    {
      @Nonnull
      public EFinish convert (final Object aSource)
      {
        return EFinish.valueOf ((String) aSource);
      }
    });

    // EInterrupt
    aRegistry.registerTypeConverter (EInterrupt.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EInterrupt.class, new ITypeConverter ()
    {
      @Nonnull
      public EInterrupt convert (final Object aSource)
      {
        return EInterrupt.valueOf ((String) aSource);
      }
    });

    // ELeftRight
    aRegistry.registerTypeConverter (ELeftRight.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, ELeftRight.class, new ITypeConverter ()
    {
      @Nonnull
      public ELeftRight convert (final Object aSource)
      {
        return ELeftRight.valueOf ((String) aSource);
      }
    });

    // EMandatory
    aRegistry.registerTypeConverter (EMandatory.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EMandatory.class, new ITypeConverter ()
    {
      @Nonnull
      public EMandatory convert (final Object aSource)
      {
        return EMandatory.valueOf ((String) aSource);
      }
    });

    // ESuccess
    aRegistry.registerTypeConverter (ESuccess.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, ESuccess.class, new ITypeConverter ()
    {
      @Nonnull
      public ESuccess convert (final Object aSource)
      {
        return ESuccess.valueOf ((String) aSource);
      }
    });

    // ETopBottom
    aRegistry.registerTypeConverter (ETopBottom.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, ETopBottom.class, new ITypeConverter ()
    {
      @Nonnull
      public ETopBottom convert (final Object aSource)
      {
        return ETopBottom.valueOf ((String) aSource);
      }
    });

    // ETriState
    aRegistry.registerTypeConverter (ETriState.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, ETriState.class, new ITypeConverter ()
    {
      @Nonnull
      public ETriState convert (final Object aSource)
      {
        return ETriState.valueOf ((String) aSource);
      }
    });

    // EValidity
    aRegistry.registerTypeConverter (EValidity.class, String.class, aEnumToStringConverter);
    aRegistry.registerTypeConverter (String.class, EValidity.class, new ITypeConverter ()
    {
      @Nonnull
      public EValidity convert (final Object aSource)
      {
        return EValidity.valueOf ((String) aSource);
      }
    });
  }
}
