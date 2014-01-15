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
package com.phloc.commons.name;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base interface for objects that have a locale <b>dependent</b> display name.
 * 
 * @author Philip Helger
 */
public interface IHasDisplayText
{
  /**
   * @param aContentLocale
   *        The locale to be used for resolving. May not be <code>null</code>.
   * @return The display text of the object in the given locale. May be
   *         <code>null</code> if the text could not be resolved in the passed
   *         locale.
   */
  @Nullable
  String getDisplayText (@Nonnull Locale aContentLocale);
}
