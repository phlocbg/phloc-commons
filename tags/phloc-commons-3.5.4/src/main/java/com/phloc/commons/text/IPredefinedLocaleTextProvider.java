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

/**
 * Specialized {@link com.phloc.commons.text.ITextProvider}-like interface that
 * gives support for resolving a text without manually passing in a locale.
 * 
 * @author philip
 */
public interface IPredefinedLocaleTextProvider
{
  /**
   * @return The same as
   *         {@link com.phloc.commons.text.ITextProvider#getText(java.util.Locale)}
   *         in the predefined locale.
   */
  String getText ();

  /**
   * @return The same as
   *         {@link com.phloc.commons.text.ITextProvider#getTextWithArgs(java.util.Locale, Object...)}
   *         in the predefined locale.
   */
  String getTextWithArgs (Object... aArgs);
}
