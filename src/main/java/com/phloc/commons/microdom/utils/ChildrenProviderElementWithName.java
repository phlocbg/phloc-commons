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
 * @author Philip Helger
 */
public final class ChildrenProviderElementWithName implements IChildrenProvider <IMicroElement>
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

  public boolean hasChildren (@Nullable final IMicroElement aCurrent)
  {
    // Not an element?
    if (aCurrent == null || !aCurrent.isElement ())
      return false;

    // Namespace URI defined?
    if (StringHelper.hasText (m_sNamespaceURI))
      return aCurrent.hasChildElements (m_sNamespaceURI, m_sTagName);
    return aCurrent.hasChildElements (m_sTagName);
  }

  @Nonnegative
  public int getChildCount (@Nullable final IMicroElement aCurrent)
  {
    return aCurrent == null ? 0 : getChildren (aCurrent).size ();
  }

  @Nonnull
  public Collection <? extends IMicroElement> getChildren (@Nullable final IMicroElement aCurrent)
  {
    // Not an element?
    if (aCurrent == null)
      return new ArrayList <IMicroElement> ();

    // Namespace URI defined?
    if (StringHelper.hasText (m_sNamespaceURI))
      return aCurrent.getAllChildElements (m_sNamespaceURI, m_sTagName);

    return aCurrent.getAllChildElements (m_sTagName);
  }
}
