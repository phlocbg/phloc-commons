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
package com.phloc.commons.text;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Readonly interface for a multilingual text
 * 
 * @author Philip Helger
 */
public interface IReadonlyMultiLingualText extends ISimpleMultiLingualText
{
  /**
   * @return A map over all contained locale/text pairs. Never <code>null</code>
   *         .
   */
  @Nonnull
  Map <Locale, String> getMap ();
}
