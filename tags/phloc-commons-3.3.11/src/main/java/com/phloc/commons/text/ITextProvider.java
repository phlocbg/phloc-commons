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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Basic interface for object providing multilingual texts.
 * 
 * @author philip
 */
public interface ITextProvider
{
  /**
   * Get the text specific for the passed locale. The implementation class MUST
   * NOT add locale-generalization when resolving the text ("de_DE" => "de" =>
   * <i>default</i>).
   * 
   * @param aContentLocale
   *        The locale to use. May not be <code>null</code>.
   * @return <code>null</code> if no text for the given locale was found.
   */
  @Nullable
  String getText (@Nonnull Locale aContentLocale);

  /**
   * Get the text specific for the passed locale. The implementation class MUST
   * add locale-generalization when resolving the text ("de_DE" => "de" =>
   * <i>default</i>).
   * 
   * @param aContentLocale
   *        The locale to use. May not be <code>null</code>.
   * @return <code>null</code> if no text for the given locale was found.
   */
  @Nullable
  String getTextWithLocaleFallback (@Nonnull Locale aContentLocale);

  /**
   * Get the text specific for the passed locale. The implementation class MUST
   * NOT add locale-generalization when resolving the text ("de_DE" => "de" =>
   * <i>default</i>). The placeholders will be resolved with the
   * {@link java.text.MessageFormat#format(Object)} method.
   * 
   * @param aContentLocale
   *        The locale to use. May not be <code>null</code>.
   * @param aArgs
   *        The arguments to be added into the string.
   * @return <code>null</code> if no text for the given locale was found.
   */
  @Nullable
  String getTextWithArgs (@Nonnull Locale aContentLocale, Object... aArgs);

  /**
   * Get the text specific for the passed locale. The implementation class MUST
   * add locale-generalization when resolving the text ("de_DE" => "de" =>
   * <i>default</i>). The placeholders will be resolved with the
   * {@link java.text.MessageFormat#format(Object)} method.
   * 
   * @param aContentLocale
   *        The locale to use. May not be <code>null</code>.
   * @param aArgs
   *        The arguments to be added into the string.
   * @return <code>null</code> if no text for the given locale was found.
   */
  @Nullable
  String getTextWithLocaleFallbackAndArgs (@Nonnull Locale aContentLocale, Object... aArgs);
}
