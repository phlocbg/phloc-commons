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
package com.phloc.settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.state.EChange;

/**
 * Writable settings interface.
 * 
 * @author philip
 */
public interface ISettings extends IReadonlySettings
{
  /**
   * {@inheritDoc}
   */
  @Nullable
  ISettings getSettingsValue (@Nullable String sFieldName);

  /**
   * Restore a value from serialization. Does not trigger an event!
   * 
   * @param sFieldName
   *        The name of the field.
   * @param aNewValue
   *        The value to be set. May be <code>null</code> .
   */
  void restoreValue (@Nonnull @Nonempty String sFieldName, @Nonnull Object aNewValue);

  /**
   * Copy all settings from another settings object. Existing values are not
   * erased, only additional values are added and existing values may be
   * changed.
   * 
   * @param aOtherSettings
   *        The settings from which we want to copy stuff. May not be
   *        <code>null</code>.
   * @return {@link EChange}. Is changed if at least one value has been changed.
   */
  @Nonnull
  EChange setValues (@Nonnull IReadonlySettings aOtherSettings);

  /**
   * Remove the value identified by the given field name. If the value was
   * successfully removed a change event is triggered, where the old value is
   * passed in and the new value is <code>null</code>.
   * 
   * @param sFieldName
   *        The field name to be removed. May be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  EChange removeValue (@Nullable String sFieldName);

  @Nonnull
  EChange clear ();

  @Nonnull
  EChange setValue (@Nonnull @Nonempty String sFieldName, @Nullable Object aNewValue);

  @Nonnull
  EChange setValue (@Nonnull @Nonempty String sFieldName, boolean bValue);

  @Nonnull
  EChange setValue (@Nonnull @Nonempty String sFieldName, int nValue);

  @Nonnull
  EChange setValue (@Nonnull @Nonempty String sFieldName, long nValue);

  @Nonnull
  EChange setValue (@Nonnull @Nonempty String sFieldName, float fValue);

  @Nonnull
  EChange setValue (@Nonnull @Nonempty String sFieldName, double dValue);
}
