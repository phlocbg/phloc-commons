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
package com.phloc.commons.parent.impl;

import javax.annotation.Nullable;

import com.phloc.commons.parent.IHasParent;
import com.phloc.commons.parent.IParentProvider;

/**
 * A standard implementation of the {@link IParentProvider} interface that works
 * with all types that implement {@link IHasParent}.
 * 
 * @author philip
 * @param <PARENTTYPE>
 *        The data type of the parent object.
 */
public class ParentProviderHasParent <PARENTTYPE extends IHasParent <PARENTTYPE>> implements
                                                                                  IParentProvider <PARENTTYPE>
{
  @Nullable
  public PARENTTYPE getParent (@Nullable final PARENTTYPE aCurrent)
  {
    return aCurrent == null ? null : aCurrent.getParent ();
  }
}
