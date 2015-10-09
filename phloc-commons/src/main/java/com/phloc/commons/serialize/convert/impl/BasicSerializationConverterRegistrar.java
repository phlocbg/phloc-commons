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
package com.phloc.commons.serialize.convert.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.serialize.convert.ISerializationConverter;
import com.phloc.commons.serialize.convert.ISerializationConverterRegistrarSPI;
import com.phloc.commons.serialize.convert.ISerializationConverterRegistry;

/**
 * Implementation of {@link ISerializationConverterRegistrarSPI} for basic types
 * like Charset etc.
 * 
 * @author Philip Helger
 */
@Immutable
@IsSPIImplementation
public final class BasicSerializationConverterRegistrar implements ISerializationConverterRegistrarSPI
{
  private static final class SerializationConverterCharset implements ISerializationConverter
  {
    public void writeConvertedObject (@Nonnull final Object aSourceObject, @Nonnull final ObjectOutputStream aOOS) throws IOException
    {
      aOOS.writeUTF (((Charset) aSourceObject).name ());
    }

    public Object readConvertedObject (@Nonnull final ObjectInputStream aOIS) throws IOException
    {
      final String sCharsetName = aOIS.readUTF ();
      return CharsetManager.getCharsetFromName (sCharsetName);
    }
  }

  public void registerSerializationConverter (@Nonnull final ISerializationConverterRegistry aRegistry)
  {
    aRegistry.registerSerializationConverter (Charset.class, new SerializationConverterCharset ());
  }
}
