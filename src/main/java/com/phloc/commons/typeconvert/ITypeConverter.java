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
package com.phloc.commons.typeconvert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.convert.IUnidirectionalConverter;

/**
 * Special interface that is used to convert between values of different types.
 * 
 * @author philip
 */
public interface ITypeConverter extends IUnidirectionalConverter <Object, Object>
{
  /**
   * {@inheritDoc} Here the parameter cannot be <code>null</code> because the
   * type converter already filters <code>null</code> values!
   */
  @Nullable
  Object convert (@Nonnull Object aSource);
}
