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
package com.phloc.types.dyntypes;

import javax.annotation.Nonnull;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.types.datatype.IHasDataType;
import com.phloc.types.datatype.ISimpleDataType;

/**
 * Represents one single dynamic value.
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IDynamicValue extends IDynamicValueEditor, ICloneable <IDynamicValue>, IHasDataType
{
  /**
   * @return The non-<code>null</code> native class that backs this type.
   */
  @Nonnull
  Class <?> getNativeClass ();

  /**
   * @return The non-<code>null</code> data type descriptor.
   */
  @Nonnull
  ISimpleDataType <?> getDataType ();
}
