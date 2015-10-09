/**
 * Copyright (C) 2006-2015 phloc systems
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

import com.phloc.commons.state.EContinue;

/**
 * A callback interface that is used in several places of the type conversion
 * engine.
 * 
 * @author Philip Helger
 */
public interface ITypeConverterCallback
{
  @Nonnull
  EContinue call (@Nonnull Class <?> aSrcClass, @Nonnull Class <?> aDstClass, @Nonnull ITypeConverter aConverter);
}
