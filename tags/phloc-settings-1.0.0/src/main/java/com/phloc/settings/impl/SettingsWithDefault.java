/**
 * Copyright (C) 2013-2014 phloc systems
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
package com.phloc.settings.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.settings.IReadonlySettings;
import com.phloc.settings.ISettingsWithDefault;

/**
 * Default implementation of {@link ISettingsWithDefault} based on
 * {@link Settings}.
 * 
 * @author Philip Helger
 */
public class SettingsWithDefault extends Settings implements ISettingsWithDefault
{
  private final IReadonlySettings m_aDefaultSettings;

  public SettingsWithDefault (@Nonnull final IReadonlySettings aDefaultSettings)
  {
    this (aDefaultSettings.getName (), aDefaultSettings);
  }

  public SettingsWithDefault (@Nonnull @Nonempty final String sName, @Nonnull final IReadonlySettings aDefaultSettings)
  {
    super (sName);
    if (aDefaultSettings == null)
      throw new NullPointerException ("DefaultSettings");
    m_aDefaultSettings = aDefaultSettings;
  }

  @Override
  public boolean containsField (@Nullable final String sFieldName)
  {
    if (super.containsField (sFieldName))
      return true;
    return m_aDefaultSettings.containsField (sFieldName);
  }

  @Override
  @Nullable
  public Object getValue (@Nullable final String sFieldName)
  {
    Object aValue = super.getValue (sFieldName);
    if (aValue == null)
    {
      // Value not found - query default
      aValue = m_aDefaultSettings.getValue (sFieldName);
    }
    return aValue;
  }

  @Nonnull
  public IReadonlySettings getDefault ()
  {
    return m_aDefaultSettings;
  }

  @Nonnull
  public EChange setToDefault (@Nullable final String sFieldName)
  {
    final Object aDefaultValue = m_aDefaultSettings.getValue (sFieldName);
    if (aDefaultValue == null)
      return EChange.UNCHANGED;

    // set if field is present in the configuration
    return setValue (sFieldName, aDefaultValue);
  }

  public boolean isSetToDefault (@Nullable final String sFieldName)
  {
    return super.containsField (sFieldName) &&
           EqualsUtils.equals (super.getValue (sFieldName), m_aDefaultSettings.getValue (sFieldName));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final SettingsWithDefault rhs = (SettingsWithDefault) o;
    return m_aDefaultSettings.equals (rhs.m_aDefaultSettings);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aDefaultSettings).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("defaultsettings", m_aDefaultSettings).toString ();
  }
}
