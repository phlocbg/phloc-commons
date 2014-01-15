/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.commons.serialize.convert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.typeconvert.TypeConverterException;
import com.phloc.commons.typeconvert.TypeConverterException.EReason;

/**
 * A utility class for converting objects from and to serializable format.<br>
 * All converters are registered in the {@link SerializationConverterRegistry}.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class SerializationConverter
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final SerializationConverter s_aInstance = new SerializationConverter ();

  private SerializationConverter ()
  {}

  public static void writeConvertedObject (@Nullable final Object aObject, @Nonnull final ObjectOutputStream aOOS) throws TypeConverterException,
                                                                                                                  IOException
  {
    if (aOOS == null)
      throw new NullPointerException ("ObjectOutputStream");

    // Write boolean flag indicating null or not
    aOOS.writeBoolean (aObject == null);
    if (aObject != null)
    {
      // Lookup converter
      final Class <?> aSrcClass = aObject.getClass ();
      final ISerializationConverter aConverter = SerializationConverterRegistry.getConverter (aSrcClass);
      if (aConverter == null)
        throw new TypeConverterException (aSrcClass, EReason.NO_CONVERTER_FOUND_SINGLE);

      // Perform conversion
      aConverter.writeConvertedObject (aObject, aOOS);
    }
  }

  @SuppressWarnings ("unchecked")
  @Nullable
  public static <DSTTYPE> DSTTYPE readConvertedObject (@Nonnull final ObjectInputStream aOIS,
                                                       @Nonnull final Class <DSTTYPE> aDstClass) throws TypeConverterException,
                                                                                                IOException
  {
    if (aOIS == null)
      throw new NullPointerException ("ObjectInputStream");
    if (aDstClass == null)
      throw new NullPointerException ("destinationClass");

    // Was the object null?
    final boolean bIsNull = aOIS.readBoolean ();
    if (bIsNull)
    {
      // Don't read anything
      return null;
    }

    // Lookup converter
    final ISerializationConverter aConverter = SerializationConverterRegistry.getConverter (aDstClass);
    if (aConverter == null)
      throw new TypeConverterException (aDstClass, EReason.NO_CONVERTER_FOUND_SINGLE);

    // Convert
    return (DSTTYPE) aConverter.readConvertedObject (aOIS);
  }
}
