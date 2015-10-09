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

import com.phloc.commons.locale.LocaleFormatter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringParser;
import com.phloc.types.datatype.impl.SimpleDataTypeRegistry;
import com.phloc.types.dyntypes.impl.AbstractDynamicValue;

/**
 * Dynamic value for objects of class {@link Integer}.
 * 
 * @author Philip Helger
 */
public final class DynamicValueInteger extends AbstractDynamicValue <Integer>
{
  public DynamicValueInteger ()
  {
    this (null);
  }

  public DynamicValueInteger (final int nValue)
  {
    this (Integer.valueOf (nValue));
  }

  public DynamicValueInteger (@Nullable final Integer aValue)
  {
    super (SimpleDataTypeRegistry.DT_INT, aValue);
  }

  @Nonnull
  public EChange setValue (final int nValue)
  {
    return setValue (Integer.valueOf (nValue));
  }

  @Nullable
  public String getAsSerializationText ()
  {
    final Integer aValue = getValue ();
    return aValue == null ? null : aValue.toString ();
  }

  @Nonnull
  public ESuccess setAsSerializationText (@Nullable final String sText)
  {
    if (sText == null)
      setValue (null);
    else
    {
      final Integer aValue = StringParser.parseIntObj (sText);
      if (aValue == null)
        return ESuccess.FAILURE;
      setValue (aValue);
    }
    return ESuccess.SUCCESS;
  }

  @Nullable
  public String getAsDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    final Integer aValue = getValue ();
    return aValue == null ? null : LocaleFormatter.getFormatted (aValue.intValue (), aDisplayLocale);
  }

  @Nonnull
  public DynamicValueInteger getClone ()
  {
    return new DynamicValueInteger (getValue ());
  }
}
