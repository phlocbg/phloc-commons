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
package com.phloc.types.datatype;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.id.IHasID;

/**
 * Base interface for data types
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IDataType extends IHasID <String>
{
  /**
   * @return <code>true</code> if it is a simple data type and can safely be
   *         casted to {@link ISimpleDataType}.
   */
  boolean isSimple ();

  /**
   * @return <code>true</code> if it is not a simple data type but a complex
   *         data type.
   */
  boolean isComplex ();
}
