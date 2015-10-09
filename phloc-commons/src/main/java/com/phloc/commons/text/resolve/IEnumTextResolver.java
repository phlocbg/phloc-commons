/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.commons.text.resolve;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.text.ISimpleTextProvider;
import com.phloc.commons.text.ITextProvider;

/**
 * Base interface for an enum-based text resolver.
 * 
 * @author Philip Helger
 */
public interface IEnumTextResolver
{
  /**
   * Get the text of an enumeration item.
   * 
   * @param aEnum
   *        The enumeration item to get the unique ID of. May not be
   *        <code>null</code>.
   * @param aTP
   *        The text provider containing the text. May not be <code>null</code>.
   * @param aContentLocale
   *        The locale to be used. May not be <code>null</code>.
   * @return <code>null</code> if no text could be resolved.
   */
  @Nullable
  String getText (@Nonnull Enum <?> aEnum, @Nonnull ISimpleTextProvider aTP, @Nonnull Locale aContentLocale);

  /**
   * Get the text of an enumeration item with placeholder texts being replaced.
   * 
   * @param aEnum
   *        The enumeration item to get the unique ID of. May not be
   *        <code>null</code>.
   * @param aTP
   *        The text provider containing the text. May not be <code>null</code>.
   * @param aContentLocale
   *        The locale to be used. May not be <code>null</code>.
   * @param aArgs
   *        The arguments to be added into the string. May be <code>null</code>
   *        but this makes no sense.
   * @return <code>null</code> if no text could be resolved.
   */
  @Nullable
  String getTextWithArgs (@Nonnull Enum <?> aEnum,
                          @Nonnull ITextProvider aTP,
                          @Nonnull Locale aContentLocale,
                          @Nullable Object... aArgs);
}
