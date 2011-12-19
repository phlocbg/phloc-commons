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
package com.phloc.commons.microdom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.IMicroContainer;
import com.phloc.commons.microdom.IMicroNode;

/**
 * Default implementation of the {@link IMicroContainer} interface.
 * 
 * @author philip
 */
public final class MicroContainer extends AbstractMicroNodeWithChildren implements IMicroContainer
{
  public MicroContainer ()
  {}

  public MicroContainer (@Nullable final IMicroNode... aChildNodes)
  {
    if (aChildNodes != null)
      for (final IMicroNode aChildNode : aChildNodes)
        appendChild (aChildNode);
  }

  @Nonnull
  public String getNodeName ()
  {
    return "#container";
  }

  @Nonnull
  public IMicroContainer getClone ()
  {
    final IMicroContainer ret = new MicroContainer ();
    if (hasChildren ())
      for (final IMicroNode aChildNode : getChildren ())
        ret.appendChild (aChildNode.getClone ());
    return ret;
  }
}
