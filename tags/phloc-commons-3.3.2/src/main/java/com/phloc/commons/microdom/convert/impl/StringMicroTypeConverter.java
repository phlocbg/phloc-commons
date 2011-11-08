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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroFactory;

/**
 * Default {@link IMicroTypeConverter} implementation for {@link String}
 * objects.
 * 
 * @author philip
 */
@Immutable
public final class StringMicroTypeConverter implements IMicroTypeConverter
{
  private static final StringMicroTypeConverter s_aInstance = new StringMicroTypeConverter ();

  private StringMicroTypeConverter ()
  {}

  @Nonnull
  public static StringMicroTypeConverter getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  public IMicroElement convertToMicroElement (final Object aObject, final String sNamespaceURI, final String sTagName)
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
}
