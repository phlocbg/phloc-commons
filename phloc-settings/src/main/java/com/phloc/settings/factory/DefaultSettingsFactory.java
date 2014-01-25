package com.phloc.settings.factory;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.settings.impl.Settings;

/**
 * The default implementation of {@link ISettingsFactory} simply creating a new
 * instance of Settings.
 * 
 * @author Philip Helger
 */
public class DefaultSettingsFactory implements ISettingsFactory
{
  private static final class SingletonHolder
  {
    static final DefaultSettingsFactory s_aInstance = new DefaultSettingsFactory ();
  }

  public DefaultSettingsFactory ()
  {}

  @Nonnull
  public static DefaultSettingsFactory getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }

  @Nonnull
  public Settings create (@Nonnull @Nonempty final String sName)
  {
    return new Settings (sName);
  }
}
