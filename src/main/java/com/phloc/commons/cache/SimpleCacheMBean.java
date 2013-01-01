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
package com.phloc.commons.cache;

import javax.annotation.Nonnull;

import com.phloc.commons.IHasSize;
import com.phloc.commons.state.EChange;

/**
 * MBean interface for a simple cache
 * 
 * @author philip
 */
public interface SimpleCacheMBean extends IHasSize
{
  /**
   * Remove all cached elements.
   * 
   * @return {@link EChange}.
   */
  @Nonnull
  EChange clearCache ();
}
