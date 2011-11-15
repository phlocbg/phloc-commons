/**
 * Copyright (C) 2006-2011 phloc systems
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

import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.ReturnsImmutableObject;

/**
 * Contains a list of resource errors and some sanity access methods.
 * 
 * @author philip
 */
public interface IResourceErrorGroup extends IHasSize, Iterable <IResourceError>
{
  /**
   * Check if this group contains only success messages. If no item is present,
   * <code>false</code> is returned.
   * 
   * @return <code>true</code> if at least one item is present, and if all items
   *         have the error level success, <code>false</code> otherwise.
   */
  boolean containsOnlySuccess ();

  /**
   * Check if this group contains at least one success message.
   * 
   * @return <code>true</code> if at least one success item is present,
   *         <code>false</code> otherwise.
   */
  boolean containsAtLeastOneSuccess ();

  /**
   * Check if this group contains only failure messages. If no item is present,
   * <code>false</code> is returned.
   * 
   * @return <code>true</code> if at least one item is present, and if all items
   *         have an error level indicating failure, <code>false</code>
   *         otherwise.
   */
  boolean containsOnlyFailure ();

  /**
   * Check if this group contains at least one failure message.
   * 
   * @return <code>true</code> if at least one failure item is present,
   *         <code>false</code> otherwise.
   */
  boolean containsAtLeastOneFailure ();

  /**
   * Check if this group contains only error or fatal error messages. If no item
   * is present, <code>false</code> is returned.
   * 
   * @return <code>true</code> if at least one item is present, and if all items
   *         have an error level indicating error or fatal error,
   *         <code>false</code> otherwise.
   */
  boolean containsOnlyError ();

  /**
   * Check if this group contains at least one error or fatal error message.
   * 
   * @return <code>true</code> if at least one error or fatal error item is
   *         present, <code>false</code> otherwise.
   */
  boolean containsAtLeastOneError ();

  /**
   * @return A resource error group containing only the failures.
   */
  @Nonnull
  IResourceErrorGroup getAllFailures ();

  /**
   * @return A resource error group containing only the errors.
   */
  @Nonnull
  IResourceErrorGroup getAllErrors ();

  /**
   * Get the most severe error level within this group.
   * 
   * @return {@link EErrorLevel#SUCCESS} if no resource error is contained, the
   *         most severe error level otherwise.
   */
  @Nonnull
  EErrorLevel getMostSevereErrorLevel ();

  /**
   * Get all contained resource errors.
   * 
   * @return A non-<code>null</code> immutable list of all contained error
   *         objects
   */
  @Nonnull
  @ReturnsImmutableObject
  List <IResourceError> getAllResourceErrors ();
}
