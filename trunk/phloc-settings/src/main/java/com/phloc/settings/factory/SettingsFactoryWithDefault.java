package com.phloc.settings.factory;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.impl.Settings;
import com.phloc.settings.impl.SettingsWithDefault;

/**
 * The default implementation of {@link ISettingsFactory} creating a new
 * instance of {@link SettingsWithDefault} with a custom default.
 * 
 * @author Philip Helger
 */
public class SettingsFactoryWithDefault implements ISettingsFactory
{
  private final IReadonlySettings m_aDefaultSettings;

  public SettingsFactoryWithDefault (@Nonnull final IReadonlySettings aDefaultSettings)
  {
    if (aDefaultSettings == null)
      throw new NullPointerException ("DefaultSettings");
    m_aDefaultSettings = aDefaultSettings;
  }

  @Nonnull
  public Settings create (@Nonnull @Nonempty final String sName)
  {
    return new SettingsWithDefault (sName, m_aDefaultSettings);
  }
}
