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

import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.IHasSize;

/**
 * Contains a list of resource errors and some sanity access methods.
 * 
 * @author philip
 */
public interface IResourceErrorGroup extends IHasSize, Iterable <IResourceError>, IHasErrorLevels
{
  /**
   * Get a resource error group containing only the failure elements. All error
   * levels except {@link EErrorLevel#SUCCESS} are considered to be a failure!
   * 
   * @return A resource error group containing only the failures.
   */
  @Nonnull
  IResourceErrorGroup getAllFailures ();

  /**
   * Get a resource error group containing only the error elements. All error
   * levels &ge; {@link EErrorLevel#ERROR} are considered to be an error!
   * 
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
   * Get a list of all contained resource errors.
   * 
   * @return A non-<code>null</code> list of all contained error objects
   */
  @Nonnull
  List <IResourceError> getAllResourceErrors ();
}
