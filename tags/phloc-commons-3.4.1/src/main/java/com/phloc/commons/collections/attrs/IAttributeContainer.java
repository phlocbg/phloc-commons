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
package com.phloc.commons.collections.attrs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;
import com.phloc.commons.state.IClearable;

/**
 * This is the writable extension of the {@link IReadonlyAttributeContainer}.
 * 
 * @author philip
 */
public interface IAttributeContainer extends IReadonlyAttributeContainer, IClearable
{
  /**
   * Set/overwrite an attribute value.
   * 
   * @param sName
   *        name of the attribute
   * @param aValue
   *        value of the attribute
   * @return {@link EChange#CHANGED} if something changed,
   *         {@link EChange#UNCHANGED} otherwise.
   */
  @Nonnull
  EChange setAttribute (@Nullable String sName, @Nullable Object aValue);

  /**
   * Remove the specified attribute from the container.
   * 
   * @param sName
   *        the attribute name
   * @return {@link EChange#CHANGED} if something changed,
   *         {@link EChange#UNCHANGED} otherwise.
   */
  @Nonnull
  EChange removeAttribute (@Nullable String sName);
}
