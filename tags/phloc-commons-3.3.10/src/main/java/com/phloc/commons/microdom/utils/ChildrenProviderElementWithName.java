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
package com.phloc.commons.microdom.utils;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.parent.IChildrenProvider;
import com.phloc.commons.string.StringHelper;

/**
 * Implementation of the {@link IChildrenProvider} for {@link IMicroNode}
 * objects considering only elements with a certain element name (and optionally
 * a namespace URI).
 * 
 * @author philip
 */
public final class ChildrenProviderElementWithName implements IChildrenProvider <IMicroNode>
{
  private final String m_sNamespaceURI;
  private final String m_sTagName;

  public ChildrenProviderElementWithName (@Nonnull @Nonempty final String sTagName)
  {
    this (null, sTagName);
  }

  public ChildrenProviderElementWithName (@Nullable final String sNamespaceURI, @Nonnull @Nonempty final String sTagName)
  {
    if (StringHelper.hasNoText (sTagName))
      throw new IllegalArgumentException ("tagName");
    m_sNamespaceURI = sNamespaceURI;
    m_sTagName = sTagName;
  }

  public boolean hasChildren (@Nonnull final IMicroNode aCurrent)
  {
    // Not an element?
    if (!aCurrent.isElement ())
      return false;

    // Namespace URI defined?
    if (StringHelper.hasText (m_sNamespaceURI))
      return ((IMicroElement) aCurrent).hasChildElements (m_sNamespaceURI, m_sTagName);
    return ((IMicroElement) aCurrent).hasChildElements (m_sTagName);
  }

  @Nonnegative
  public int getChildCount (@Nonnull final IMicroNode aCurrent)
  {
    return getChildren (aCurrent).size ();
  }

  @Nonnull
  public Collection <? extends IMicroNode> getChildren (@Nonnull final IMicroNode aCurrent)
  {
    // Not an element?
    if (!aCurrent.isElement ())
      return new ArrayList <IMicroNode> ();

    // Namespace URI defined?
    if (StringHelper.hasText (m_sNamespaceURI))
      return ((IMicroElement) aCurrent).getChildElements (m_sNamespaceURI, m_sTagName);

    return ((IMicroElement) aCurrent).getChildElements (m_sTagName);
  }
}
