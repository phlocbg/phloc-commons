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
package com.phloc.commons.stats;

import java.util.Set;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;

/**
 * Base interface for keyed statistic handlers
 * 
 * @author philip
 */
public interface IStatisticsHandlerKeyed extends IStatisticsHandler
{
  /**
   * @return A collection of all keys that have a value assigned. Never
   *         <code>null</code>.
   */
  @Nonnull
  Set <String> getAllKeys ();

  /**
   * Get the invocation count for a single key.
   * 
   * @param sKey
   *        The key to be queried. May be <code>null</code>.
   * @return The invocation count for a single key or
   *         {@link CGlobal#ILLEGAL_UINT} if no such key exists
   */
  @CheckForSigned
  int getInvocationCount (@Nullable String sKey);
}
