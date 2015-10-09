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
package com.phloc.commons.microdom.convert.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.typeconvert.TypeConverter;

/**
 * An implementation if {@link IMicroTypeConverter} that uses a regular type
 * converter conversion from and to string for conversion.
 * 
 * @author Philip Helger
 */
public final class StringBasedMicroTypeConverter implements IMicroTypeConverter
{
  private final Class <?> m_aNativeClass;

  public StringBasedMicroTypeConverter (@Nonnull final Class <?> aNativeClass)
  {
    ValueEnforcer.notNull (aNativeClass, "NativeClass");

    m_aNativeClass = aNativeClass;
  }

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    // Convert object to string
    final String sValue = TypeConverter.convertIfNecessary (aObject, String.class);

    // Convert string to micro element
    return StringMicroTypeConverter.getInstance ().convertToMicroElement (sValue, sNamespaceURI, sTagName);
  }

  @Nonnull
  public Object convertToNative (@Nonnull final IMicroElement aElement)
  {
    // Convert micro element to string
    final String sValue = StringMicroTypeConverter.getInstance ().convertToNative (aElement);

    // Convert string to native type
    return TypeConverter.convertIfNecessary (sValue, m_aNativeClass);
  }
}
