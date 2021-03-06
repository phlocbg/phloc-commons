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
package com.phloc.commons.id;

import javax.annotation.Nonnull;

/**
 * Base interface for all objects having an ID.
 * 
 * @author Philip Helger
 * @param <IDTYPE>
 *        The type of the provided ID.
 */
public interface IHasID <IDTYPE>
{
  /**
   * Get the unique ID of this object. If the type is {@link String} than the
   * returned value must match an XML NMToken expression (so e.g. no ':' in the
   * ID)!
   * 
   * @return The ID of this object. May not be <code>null</code>.
   */
  @Nonnull
  IDTYPE getID ();
}
