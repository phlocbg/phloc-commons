/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.types.dyntypes.base;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringParser;
import com.phloc.types.datatype.impl.SimpleDataTypeRegistry;
import com.phloc.types.dyntypes.impl.AbstractDynamicValue;

/**
 * Dynamic value for objects of class {@link Boolean}.
 * 
 * @author Philip Helger
 */
public class DynamicValueBoolean extends AbstractDynamicValue <Boolean>
{
  public DynamicValueBoolean ()
  {
    this (null);
  }

  public DynamicValueBoolean (final boolean bValue)
  {
    this (Boolean.valueOf (bValue));
  }

  public DynamicValueBoolean (@Nullable final Boolean aValue)
  {
    super (SimpleDataTypeRegistry.DT_BOOLEAN, aValue);
  }

  @Nonnull
  public EChange setValue (final boolean bValue)
  {
    return setValue (Boolean.valueOf (bValue));
  }

  @Nullable
  public final String getAsSerializationText ()
  {
    final Boolean aValue = getValue ();
    return aValue == null ? null : aValue.toString ();
  }

  @Nonnull
  public final ESuccess setAsSerializationText (@Nullable final String sText)
  {
    if (sText == null)
      setValue (null);
    else
      setValue (StringParser.parseBoolObj (sText));
    return ESuccess.SUCCESS;
  }

  @OverrideOnDemand
  @Nullable
  public String getAsDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    return getAsSerializationText ();
  }

  @Nonnull
  public DynamicValueBoolean getClone ()
  {
    return new DynamicValueBoolean (getValue ());
  }
}
