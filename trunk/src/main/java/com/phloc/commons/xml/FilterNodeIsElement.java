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
package com.phloc.commons.xml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.phloc.commons.filter.IFilter;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * An implementation of {@link IFilter} on {@link Node} objects that will only
 * return {@link Element} nodes.
 * 
 * @author philip
 */
@NotThreadSafe
public final class FilterNodeIsElement implements IFilter <Node>
{
  private final IFilter <Element> m_aNestedElementFilter;

  public FilterNodeIsElement ()
  {
    this (null);
  }

  public FilterNodeIsElement (@Nullable final IFilter <Element> aNestedElementFilter)
  {
    m_aNestedElementFilter = aNestedElementFilter;
  }

  public boolean matchesFilter (@Nonnull final Node aNode)
  {
    if (aNode.getNodeType () != Node.ELEMENT_NODE)
      return false;
    if (m_aNestedElementFilter == null)
      return true;
    return m_aNestedElementFilter.matchesFilter ((Element) aNode);
  }

  @Nullable
  public IFilter <Element> getNestedElementFilter ()
  {
    return m_aNestedElementFilter;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FilterNodeIsElement))
      return false;
    final FilterNodeIsElement rhs = (FilterNodeIsElement) o;
    return m_aNestedElementFilter.equals (rhs.m_aNestedElementFilter);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aNestedElementFilter).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("customFilter", m_aNestedElementFilter).toString ();
  }
}
