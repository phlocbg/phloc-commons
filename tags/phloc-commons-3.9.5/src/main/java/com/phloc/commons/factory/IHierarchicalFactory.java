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
package com.phloc.commons.factory;

import com.phloc.commons.annotations.DevelopersNote;

/**
 * This is a generic interface for creating objects of a certain type that have
 * a parent item. For creating root items, please use the
 * {@link IHierarchicalRootFactory} interface!
 * 
 * @author philip
 * @param <DATATYPE>
 *        The type of object to create.
 */
public interface IHierarchicalFactory <DATATYPE> extends IFactoryWithParameter <DATATYPE, DATATYPE>
{
  /**
   * Create an object of the desired type.
   * 
   * @param aParent
   *        The parent item to use. May never be <code>null</code>.
   * @return The created object.
   */
  @DevelopersNote ("No @Nullable annotation as we can make no assumptions on the state")
  DATATYPE create (DATATYPE aParent);
}
