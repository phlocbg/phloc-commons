package com.phloc.fonts;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.resource.ClassPathResource;

public enum EFontResource
{
  TTF_ANAHEIM_REGULAR (EFontType.TTF, 400, "font/ttf/Anaheim-Regular.ttf");

  private final EFontType m_eFontType;
  private final int m_nWeight;
  private final String m_sPath;

  private EFontResource (@Nonnull final EFontType eFontType,
                         @Nonnegative final int nWeight,
                         @Nonnull @Nonempty final String sPath)
  {
    m_eFontType = eFontType;
    m_nWeight = nWeight;
    m_sPath = sPath;
  }

  @Nonnull
  public EFontType getFontType ()
  {
    return m_eFontType;
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
}
