/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.locale.ComparatorLocale;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.text.ISimpleMultiLingualText;
import com.phloc.commons.text.impl.MultiLingualText;
import com.phloc.commons.text.impl.ReadonlyMultiLingualText;

@Immutable
@IsSPIImplementation
public final class MultiLingualTextMicroTypeConverterRegistrar implements IMicroTypeConverterRegistrarSPI
{
  private static final String ELEMENT_TEXT = "text";
  private static final String ATTR_LOCALE = "locale";

  private static abstract class AbstractMLTConverter implements IMicroTypeConverter
  {
    @Nonnull
    public final IMicroElement convertToMicroElement (@Nonnull final Object aSource,
                                                      @Nullable final String sNamespaceURI,
                                                      @Nonnull @Nonempty final String sTagName)
    {
      final ISimpleMultiLingualText aMLT = (ISimpleMultiLingualText) aSource;
      final IMicroElement eMText = new MicroElement (sNamespaceURI, sTagName);
      for (final Locale aLocale : ContainerHelper.getSorted (aMLT.getAllLocales (), new ComparatorLocale ()))
      {
        final IMicroElement eText = eMText.appendElement (sNamespaceURI, ELEMENT_TEXT);
        eText.setAttribute (ATTR_LOCALE, aLocale.toString ());
        eText.appendText (aMLT.getText (aLocale));
      }
      return eMText;
    }

    @Nonnull
    protected static final MultiLingualText convertToMLT (@Nonnull final IMicroElement aElement)
    {
      final MultiLingualText aMLT = new MultiLingualText ();
      for (final IMicroElement eText : aElement.getChildElements (ELEMENT_TEXT))
      {
        final Locale aLocale = LocaleCache.getLocale (eText.getAttribute (ATTR_LOCALE));
        aMLT.setText (aLocale, eText.getTextContent ());
      }
      return aMLT;
    }
  }

  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    // Register the read-only version first!
    aRegistry.registerMicroElementTypeConverter (ReadonlyMultiLingualText.class, new AbstractMLTConverter ()
    {
      @Nonnull
      public ReadonlyMultiLingualText convertToNative (@Nonnull final IMicroElement aElement)
      {
        return new ReadonlyMultiLingualText (convertToMLT (aElement));
      }
    });

    // Register the writable version afterwards!
    aRegistry.registerMicroElementTypeConverter (MultiLingualText.class, new AbstractMLTConverter ()
    {
      @Nonnull
      public MultiLingualText convertToNative (@Nonnull final IMicroElement aElement)
      {
        return convertToMLT (aElement);
      }
    });
  }
}
