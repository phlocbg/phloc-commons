/**
 * Copyright (C) 2006-2011 phloc systems
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

import java.util.Collection;

import javax.annotation.Nullable;

import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.parent.IChildrenProviderWithID;
import com.phloc.commons.parent.IHasChildren;

/**
 * An implementation of the {@link IChildrenProviderWithID} interface that works
 * with all types that implement {@link IHasChildren} and {@link IHasID}.
 * 
 * @author philip
 * @param <CHILDTYPE>
 *        The data type of the child objects.
 */
public class ChildrenProviderHasChildrenWithID <KEYTYPE, CHILDTYPE extends IHasChildren <CHILDTYPE> & IHasID <KEYTYPE>> extends
                                                                                                                        ChildrenProviderHasChildren <CHILDTYPE> implements
                                                                                                                                                               IChildrenProviderWithID <KEYTYPE, CHILDTYPE>
{
  @Nullable
  public CHILDTYPE getChildWithID (@Nullable final CHILDTYPE aCurrent, @Nullable final KEYTYPE aID)
  {
    if (aCurrent != null)
    {
      // Get all children (if any)
      final Collection <? extends CHILDTYPE> aChildren = aCurrent.getChildren ();
      if (aChildren != null)
        for (final CHILDTYPE aChild : aChildren)
          if (aChild != null && CompareUtils.nullSafeEquals (aChild.getID (), aID))
            return aChild;
    }
    return null;
  }
}
