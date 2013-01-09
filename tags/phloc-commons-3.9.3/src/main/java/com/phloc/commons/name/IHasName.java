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
package com.phloc.commons.name;

import javax.annotation.Nonnull;

/**
 * Base interface for all objects that have a mandatory <b>INTERNAL</b> name
 * that is not multilingual but should be human interpretable. If you need to
 * have a name that is displayed on the screen, please use the
 * {@link IHasDisplayName} interface.
 * 
 * @author philip
 */
public interface IHasName
{
  /**
   * @return The name of the object. May not be <code>null</code>.
   */
  @Nonnull
  String getName ();
}
