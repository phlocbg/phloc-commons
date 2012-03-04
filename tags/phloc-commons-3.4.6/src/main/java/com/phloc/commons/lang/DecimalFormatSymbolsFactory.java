/**
 * Copyright (C) 2006-2012 phloc systems
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
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.system.EJavaVersion;

/**
 * A small helper class, that constructs {@link DecimalFormatSymbols} objects in
 * the best suitable way. For Java < 1.6 it is to use
 * "new DecimalFormatSymbols (Locale)". For Java >= 1.6
 * "DecimalFormatSymbols.getInstance (Locale)" is more suitable!
 * 
 * @author philip
 */
public final class DecimalFormatSymbolsFactory
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final DecimalFormatSymbolsFactory s_aInstance = new DecimalFormatSymbolsFactory ();

  private DecimalFormatSymbolsFactory ()
  {}

  @Nonnull
  public static DecimalFormatSymbols getInstance (final Locale aLocale)
  {
    if (EJavaVersion.JDK_16.isSupportedVersion ())
    {
      try
      {
        return GenericReflection.invokeStaticMethod (DecimalFormatSymbols.class, "getInstance", aLocale);
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
    return new DecimalFormatSymbols (aLocale);
  }
}
