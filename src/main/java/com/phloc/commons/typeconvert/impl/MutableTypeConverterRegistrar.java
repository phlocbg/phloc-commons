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
package com.phloc.commons.typeconvert.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.mutable.MutableBoolean;
import com.phloc.commons.mutable.MutableByte;
import com.phloc.commons.mutable.MutableChar;
import com.phloc.commons.mutable.MutableDouble;
import com.phloc.commons.mutable.MutableFloat;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.mutable.MutableLong;
import com.phloc.commons.mutable.MutableShort;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;

/**
 * Register the mutable* specific type converter
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class MutableTypeConverterRegistrar implements ITypeConverterRegistrarSPI
{
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    aRegistry.registerTypeConverter (MutableBoolean.class, Boolean.class, new ITypeConverter ()
    {
      public Boolean convert (@Nonnull final Object aSource)
      {
        return ((MutableBoolean) aSource).getAsBoolean ();
      }
    });
    aRegistry.registerTypeConverter (Boolean.class, MutableBoolean.class, new ITypeConverter ()
    {
      public MutableBoolean convert (@Nonnull final Object aSource)
      {
        return new MutableBoolean ((Boolean) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableByte.class, Byte.class, new ITypeConverter ()
    {
      public Byte convert (@Nonnull final Object aSource)
      {
        return ((MutableByte) aSource).getAsByte ();
      }
    });
    aRegistry.registerTypeConverter (Byte.class, MutableByte.class, new ITypeConverter ()
    {
      public MutableByte convert (@Nonnull final Object aSource)
      {
        return new MutableByte ((Byte) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableChar.class, Character.class, new ITypeConverter ()
    {
      public Character convert (@Nonnull final Object aSource)
      {
        return ((MutableChar) aSource).getAsCharacter ();
      }
    });
    aRegistry.registerTypeConverter (Character.class, MutableChar.class, new ITypeConverter ()
    {
      public MutableChar convert (@Nonnull final Object aSource)
      {
        return new MutableChar ((Character) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableDouble.class, Double.class, new ITypeConverter ()
    {
      public Double convert (@Nonnull final Object aSource)
      {
        return ((MutableDouble) aSource).getAsDouble ();
      }
    });
    aRegistry.registerTypeConverter (Double.class, MutableDouble.class, new ITypeConverter ()
    {
      public MutableDouble convert (@Nonnull final Object aSource)
      {
        return new MutableDouble ((Double) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableFloat.class, Float.class, new ITypeConverter ()
    {
      public Float convert (@Nonnull final Object aSource)
      {
        return ((MutableFloat) aSource).getAsFloat ();
      }
    });
    aRegistry.registerTypeConverter (Float.class, MutableFloat.class, new ITypeConverter ()
    {
      public MutableFloat convert (@Nonnull final Object aSource)
      {
        return new MutableFloat ((Float) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableInt.class, Integer.class, new ITypeConverter ()
    {
      public Integer convert (@Nonnull final Object aSource)
      {
        return ((MutableInt) aSource).getAsInteger ();
      }
    });
    aRegistry.registerTypeConverter (Integer.class, MutableInt.class, new ITypeConverter ()
    {
      public MutableInt convert (@Nonnull final Object aSource)
      {
        return new MutableInt ((Integer) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableLong.class, Long.class, new ITypeConverter ()
    {
      public Long convert (@Nonnull final Object aSource)
      {
        return ((MutableLong) aSource).getAsLong ();
      }
    });
    aRegistry.registerTypeConverter (Long.class, MutableLong.class, new ITypeConverter ()
    {
      public MutableLong convert (@Nonnull final Object aSource)
      {
        return new MutableLong ((Long) aSource);
      }
    });
    aRegistry.registerTypeConverter (MutableShort.class, Short.class, new ITypeConverter ()
    {
      public Short convert (@Nonnull final Object aSource)
      {
        return ((MutableShort) aSource).getAsShort ();
      }
    });
    aRegistry.registerTypeConverter (Short.class, MutableShort.class, new ITypeConverter ()
    {
      public MutableShort convert (@Nonnull final Object aSource)
      {
        return new MutableShort ((Short) aSource);
      }
    });
  }
}
