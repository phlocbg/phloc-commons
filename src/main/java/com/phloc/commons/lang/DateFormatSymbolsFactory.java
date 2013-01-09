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
package com.phloc.commons.lang;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.system.EJavaVersion;

/**
 * A small helper class, that constructs {@link DateFormatSymbols} objects in
 * the best suitable way. For Java < 1.6 it is to use
 * "new DateFormatSymbols (Locale)". For Java >= 1.6
 * "DateFormatSymbols.getInstance (Locale)" is more suitable!
 * 
 * @author philip
 */
@Immutable
public final class DateFormatSymbolsFactory
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final DateFormatSymbolsFactory s_aInstance = new DateFormatSymbolsFactory ();

  private DateFormatSymbolsFactory ()
  {}

  @Nonnull
  public static DateFormatSymbols getInstance (final Locale aLocale)
  {
    if (EJavaVersion.JDK_16.isSupportedVersion ())
    {
      try
      {
        return GenericReflection.invokeStaticMethod (DateFormatSymbols.class, "getInstance", aLocale);
      }
      catch (final NoSuchMethodException ex)
      {
        // Failed
      }
      catch (final IllegalAccessException e)
      {
        // Failed
      }
      catch (final InvocationTargetException e)
      {
        // Failed
      }
    }

    // For all Java < 1.6
    return new DateFormatSymbols (aLocale);
  }
}
