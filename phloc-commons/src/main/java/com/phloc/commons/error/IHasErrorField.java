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
package com.phloc.commons.error;

import javax.annotation.Nullable;

import com.phloc.commons.error.IResourceLocation;

/**
 * Base interface for an object that has an error field.
 * 
 * @author Philip Helger
 */
public interface IHasErrorField
{
  /**
   * @return The field for which the error occurred. May be <code>null</code>.
   */
  @Nullable
  String getErrorFieldName ();

  /**
   * @return <code>true</code> if a field name is present, <code>false</code>
   *         otherwise
   */
  boolean hasErrorFieldName ();

  /**
   * @return The error field name of this object as an {@link IResourceLocation}
   *         . If no error field name is present, <code>null</code> is returned,
   *         else an {@link IResourceLocation} with the field name set is
   *         returned.
   */
  @Nullable
  IResourceLocation getAsResourceLocation ();
}
