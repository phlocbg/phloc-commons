/**
 * Copyright (C) 2014 phloc systems
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
package com.phloc.fonts;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.io.resource.ClassPathResource;

/**
 * Defines the available font resources available in this library.
 * 
 * @author Philip Helger
 */
public enum EFontResource
{
  ALGREYA_SANS_THIN (EFontType.TTF, EFontStyle.REGULAR, 100, "fonts/ttf/AlegreyaSans/AlegreyaSans-Thin.ttf"),
  ALGREYA_SANS_THIN_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 100, "fonts/ttf/AlegreyaSans/AlegreyaSans-ThinItalic.ttf"),
  ALGREYA_SANS_LIGHT (EFontType.TTF, EFontStyle.REGULAR, 300, "fonts/ttf/AlegreyaSans/AlegreyaSans-Light.ttf"),
  ALGREYA_SANS_LIGHT_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 300, "fonts/ttf/AlegreyaSans/AlegreyaSans-LightItalic.ttf"),
  ALGREYA_SANS_NORMAL (EFontType.TTF, EFontStyle.REGULAR, 400, "fonts/ttf/AlegreyaSans/AlegreyaSans-Regular.ttf"),
  ALGREYA_SANS_NORMAL_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 400, "fonts/ttf/AlegreyaSans/AlegreyaSans-Italic.ttf"),
  ALGREYA_SANS_MEDIUM (EFontType.TTF, EFontStyle.REGULAR, 500, "fonts/ttf/AlegreyaSans/AlegreyaSans-Medium.ttf"),
  ALGREYA_SANS_MEDIUM_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 500, "fonts/ttf/AlegreyaSans/AlegreyaSans-MediumItalic.ttf"),
  ALGREYA_SANS_BOLD (EFontType.TTF, EFontStyle.REGULAR, 700, "fonts/ttf/AlegreyaSans/AlegreyaSans-Bold.ttf"),
  ALGREYA_SANS_BOLD_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 700, "fonts/ttf/AlegreyaSans/AlegreyaSans-BoldItalic.ttf"),
  ALGREYA_SANS_EXTRA_BOLD (EFontType.TTF, EFontStyle.REGULAR, 800, "fonts/ttf/AlegreyaSans/AlegreyaSans-ExtraBold.ttf"),
  ALGREYA_SANS_EXTRA_BOLD_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 800, "fonts/ttf/AlegreyaSans/AlegreyaSans-ExtraBoldItalic.ttf"),
  ALGREYA_SANS_BLACK (EFontType.TTF, EFontStyle.REGULAR, 900, "fonts/ttf/AlegreyaSans/AlegreyaSans-Black.ttf"),
  ALGREYA_SANS_BLACK_ITALIC (EFontType.TTF, EFontStyle.ITALIC, 900, "fonts/ttf/AlegreyaSans/AlegreyaSans-BlackItalic.ttf"),
  ANAHEIM_REGULAR (EFontType.TTF, EFontStyle.REGULAR, 400, "fonts/ttf/Anaheim/Anaheim-Regular.ttf"),
  EXO2_THIN (EFontType.OTF, EFontStyle.REGULAR, 100, "fonts/ttf/Exo2/Exo2.0-Thin.otf"),
  EXO2_THIN_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 100, "fonts/ttf/Exo2/Exo2.0-ThinItalic.otf"),
  EXO2_EXTRA_LIGHT (EFontType.OTF, EFontStyle.REGULAR, 200, "fonts/ttf/Exo2/Exo2.0-ExtraLight.otf"),
  EXO2_EXTRA_LIGHT_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 200, "fonts/ttf/Exo2/Exo2.0-ExtraLightItalic.otf"),
  EXO2_LIGHT (EFontType.OTF, EFontStyle.REGULAR, 300, "fonts/ttf/Exo2/Exo2.0-Light.otf"),
  EXO2_LIGHT_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 300, "fonts/ttf/Exo2/Exo2.0-LightItalic.otf"),
  EXO2_NORMAL (EFontType.OTF, EFontStyle.REGULAR, 400, "fonts/ttf/Exo2/Exo2.0-Regular.otf"),
  EXO2_NORMAL_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 400, "fonts/ttf/Exo2/Exo2.0-Italic.otf"),
  EXO2_MEDIUM (EFontType.OTF, EFontStyle.REGULAR, 500, "fonts/ttf/Exo2/Exo2.0-Medium.otf"),
  EXO2_MEDIUM_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 500, "fonts/ttf/Exo2/Exo2.0-MediumItalic.otf"),
  EXO2_SEMI_BOLD (EFontType.OTF, EFontStyle.REGULAR, 600, "fonts/ttf/Exo2/Exo2.0-SemiBold.otf"),
  EXO2_SEMI_BOLD_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 600, "fonts/ttf/Exo2/Exo2.0-SemiBoldItalic.otf"),
  EXO2_BOLD (EFontType.OTF, EFontStyle.REGULAR, 700, "fonts/ttf/Exo2/Exo2.0-Bold.otf"),
  EXO2_BOLD_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 700, "fonts/ttf/Exo2/Exo2.0-BoldItalic.otf"),
  EXO2_EXTRA_BOLD (EFontType.OTF, EFontStyle.REGULAR, 800, "fonts/ttf/Exo2/Exo2.0-ExtraBold.otf"),
  EXO2_EXTRA_BOLD_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 800, "fonts/ttf/Exo2/Exo2.0-ExtraBoldItalic.otf"),
  EXO2_BLACK (EFontType.OTF, EFontStyle.REGULAR, 900, "fonts/ttf/Exo2/Exo2.0-Black.otf"),
  EXO2_BLACK_ITALIC (EFontType.OTF, EFontStyle.ITALIC, 900, "fonts/ttf/Exo2/Exo2.0-BlackItalic.otf");

  private final EFontType m_eFontType;
  private final EFontStyle m_eFontStyle;
  private final int m_nWeight;
  private final String m_sPath;

  private EFontResource (@Nonnull final EFontType eFontType,
                         @Nonnull final EFontStyle eFontStyle,
                         @Nonnegative final int nWeight,
                         @Nonnull @Nonempty final String sPath)
  {
    m_eFontType = eFontType;
    m_eFontStyle = eFontStyle;
    m_nWeight = nWeight;
    m_sPath = sPath;
  }

  @Nonnull
  public EFontType getFontType ()
  {
    return m_eFontType;
  }

  @Nonnull
  public EFontStyle getFontStyle ()
  {
    return m_eFontStyle;
  }

  @Nonnegative
  public int getFontWeight ()
  {
    return m_nWeight;
  }

  @Nonnull
  @Nonempty
  public String getPath ()
  {
    return m_sPath;
  }

  @Nonnull
  public ClassPathResource getResource ()
  {
    return new ClassPathResource (m_sPath);
  }

  @Nonnull
  public InputStream getInputStream ()
  {
    return ClassPathResource.getInputStream (m_sPath);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <EFontResource> getAllResourcesOfFontType (@Nullable final EFontType eFontType)
  {
    final Set <EFontResource> ret = EnumSet.noneOf (EFontResource.class);
    for (final EFontResource e : values ())
      if (e.getFontType ().equals (eFontType))
        ret.add (e);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Set <EFontResource> getAllResourcesOfFontWeight (final int nFontWeight)
  {
    final Set <EFontResource> ret = EnumSet.noneOf (EFontResource.class);
    for (final EFontResource e : values ())
      if (e.getFontWeight () == nFontWeight)
        ret.add (e);
    return ret;
  }
}
