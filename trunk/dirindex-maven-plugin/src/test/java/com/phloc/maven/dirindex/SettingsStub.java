package com.phloc.maven.dirindex;

import java.util.Collections;
import java.util.List;

import org.apache.maven.settings.Settings;

public class SettingsStub extends Settings
{
  /** {@inheritDoc} */
  @SuppressWarnings ({ "unchecked", "rawtypes" })
  @Override
  public List getProxies ()
  {
    return Collections.EMPTY_LIST;
  }
}
