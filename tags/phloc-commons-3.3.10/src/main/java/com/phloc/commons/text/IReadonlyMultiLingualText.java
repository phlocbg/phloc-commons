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
package com.phloc.commons.text;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsImmutableObject;

public interface IReadonlyMultiLingualText extends ISimpleMultiLingualText, Serializable
{
  /**
   * Checks whether a text with the given locale is contained directly.
   * 
   * @param aContentLocale
   *        The locale to check. May not be <code>null</code>.
   * @return <code>true</code> if a text in the given locale is present,
   *         <code>false</code> otherwise.
   */
  boolean containsLocale (@Nonnull Locale aContentLocale);

  /**
   * Checks whether a text with the given locale is contained including fallback
   * resolution ("de_DE" => "de").
   * 
   * @param aContentLocale
   *        The locale to check. May not be <code>null</code>.
   * @return <code>true</code> if a text in the given locale (or one of its
   *         fallbacks) is present, <code>false</code> otherwise.
   */
  boolean containsLocaleWithFallback (@Nonnull Locale aContentLocale);

  /**
   * @return A read-only map over all contained locale/text pairs. Never
   *         <code>null</code> .
   */
  @Nonnull
  @ReturnsImmutableObject
  Map <Locale, String> getMap ();
}
