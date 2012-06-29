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
package com.phloc.commons.xml;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.filter.collections.FilterIterator;

/**
 * Iterate child elements of a single node. Does not iterate recursively.
 * 
 * @author philip
 */
public class ChildElementIterator implements IIterableIterator <Element>
{
  public static final class FilterIsElement implements IFilter <Node>
  {
    private final IFilter <Element> m_aCustomFilter;

    public FilterIsElement (@Nullable final IFilter <Element> aCustomFilter)
    {
      m_aCustomFilter = aCustomFilter;
    }

    public boolean matchesFilter (@Nonnull final Node aNode)
    {
      if (aNode.getNodeType () != Node.ELEMENT_NODE)
        return false;
      return m_aCustomFilter == null ? true : m_aCustomFilter.matchesFilter ((Element) aNode);
    }
  }

  /** The nodes to iterate. */
  @Nonnull
  private final Iterator <Node> m_aIter;

  public ChildElementIterator (@Nonnull final Node aStartNode)
  {
    this (aStartNode, null);
  }

  public ChildElementIterator (@Nonnull final Node aStartNode, @Nullable final IFilter <Element> aCustomFilter)
  {
    m_aIter = new FilterIterator <Node> (new ChildNodeIterator (aStartNode), new FilterIsElement (aCustomFilter));
  }

  public final boolean hasNext ()
  {
    return m_aIter.hasNext ();
  }

  @Nonnull
  public final Element next ()
  {
    return (Element) m_aIter.next ();
  }

  @UnsupportedOperation
  public final void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public Iterator <Element> iterator ()
  {
    return this;
  }
}
