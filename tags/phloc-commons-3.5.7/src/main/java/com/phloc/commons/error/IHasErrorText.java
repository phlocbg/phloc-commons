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
package com.phloc.commons.error;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface representing an object having an error text.
 * 
 * @author philip
 */
public interface IHasErrorText
{
  /**
   * Get the error text of an operation.
   * 
   * @param aContentLocale
   *        The locale to use. May not be <code>null</code>.
   * @return The error text in the given locale. May be <code>null</code> if the
   *         operation succeeded.
   */
  @Nullable
  String getErrorText (@Nonnull Locale aContentLocale);
}
