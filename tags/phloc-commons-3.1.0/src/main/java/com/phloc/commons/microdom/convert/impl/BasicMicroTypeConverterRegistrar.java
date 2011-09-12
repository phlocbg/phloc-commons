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
package com.phloc.commons.microdom.convert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.MicroTypeConverterRegistry;
import com.phloc.commons.microdom.impl.MicroFactory;
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
import com.phloc.commons.typeconvert.TypeConverter;

@Immutable
@IsSPIImplementation
public final class BasicMicroTypeConverterRegistrar implements IMicroTypeConverterRegistrarSPI
{
  private static final class StringBasedMicroTypeConverter implements IMicroTypeConverter
  {
    private final IMicroTypeConverter m_aStringConverter;
    private final Class <?> m_aNativeClass;

    private StringBasedMicroTypeConverter (@Nonnull final IMicroTypeConverter aStringConverter,
                                           @Nonnull final Class <?> aNativeClass)
    {
      m_aStringConverter = aStringConverter;
      m_aNativeClass = aNativeClass;
    }

    @Nonnull
    public IMicroElement convertToMicroElement (final Object aObject, final String sNamespaceURI, final String sTagName)
    {
      final String sValue = TypeConverter.convertIfNecessary (aObject, String.class);
      return m_aStringConverter.convertToMicroElement (sValue, sNamespaceURI, sTagName);
    }

    @Nonnull
    public Object convertToNative (@Nonnull final IMicroElement aElement)
    {
      final String sValue = (String) m_aStringConverter.convertToNative (aElement);
      return TypeConverter.convertIfNecessary (sValue, m_aNativeClass);
    }
  }

  public void registerMicroTypeConverter ()
  {
    // String converter
    final IMicroTypeConverter aStringConverter = new IMicroTypeConverter ()
    {
      @Nonnull
      public IMicroElement convertToMicroElement (final Object aObject,
                                                  final String sNamespaceURI,
                                                  final String sTagName)
      {
        final IMicroElement e = MicroFactory.newElement (sNamespaceURI, sTagName);
        e.appendText ((String) aObject);
        return e;
      }

      @Nonnull
      public String convertToNative (@Nonnull final IMicroElement aElement)
      {
        return aElement.getTextContent ();
      }
    };
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (String.class, aStringConverter);

    // Other base type based on the String converter
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Boolean.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Boolean.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Byte.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Byte.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Character.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Character.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Double.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Double.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Float.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Float.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Integer.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Integer.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Long.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Long.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (Short.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     Short.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (BigDecimal.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     BigDecimal.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (BigInteger.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     BigInteger.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (StringBuffer.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     StringBuffer.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (StringBuilder.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     StringBuilder.class));

    // State enums
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EChange.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EChange.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EContinue.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EContinue.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EEnabled.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EEnabled.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EFinish.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EFinish.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EInterrupt.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EInterrupt.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ELeftRight.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     ELeftRight.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EMandatory.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EMandatory.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ESuccess.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     ESuccess.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ETopBottom.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     ETopBottom.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (ETriState.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     ETriState.class));
    MicroTypeConverterRegistry.registerMicroElementTypeConverter (EValidity.class,
                                                                  new StringBasedMicroTypeConverter (aStringConverter,
                                                                                                     EValidity.class));
  }
}
