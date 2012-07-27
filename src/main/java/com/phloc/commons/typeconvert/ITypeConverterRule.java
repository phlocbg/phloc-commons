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

/**
 * Flexible type converter that can handle multiple source and/or destination
 * classes.
 * 
 * @author philip
 */
public interface ITypeConverterRule extends ITypeConverter
{
  /**
   * Check if this converter can handle the conversion from the passed source to
   * the passed destination class. Note: as this method is called for every type
   * conversion for which no exact converters are present, the implementation of
   * this method should be as efficient as possible.
   * 
   * @param aSrcClass
   *        Source class to convert from. Never <code>null</code>.
   * @param aDstClass
   *        Destination class to convert to. Never <code>null</code>.
   * @return <code>true</code> if conversion is possible, <code>false</code>
   *         otherwise.
   */
  boolean canConvert (@Nonnull Class <?> aSrcClass, @Nonnull Class <?> aDstClass);
}
