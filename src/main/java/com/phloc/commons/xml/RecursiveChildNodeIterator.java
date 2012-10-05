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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Iterate all children of the start node, but NOT the start node itself.
 * 
 * @author philip
 */
public class RecursiveChildNodeIterator implements IIterableIterator <Node>
{
  private final Iterator <Node> m_aIter;

  public RecursiveChildNodeIterator (@Nonnull final Node eParent)
  {
    if (eParent == null)
      throw new NullPointerException ("parent");
    final List <Node> aNodes = new ArrayList <Node> ();
    _fillListPrefix (eParent, aNodes);
    m_aIter = aNodes.iterator ();
  }

  private static void _fillListPrefix (@Nonnull final Node aParent, @Nonnull final List <Node> aNodes)
  {
    final NodeList aNodeList = aParent.getChildNodes ();
    if (aNodeList != null)
    {
      final int nlsize = aNodeList.getLength ();
      for (int i = 0; i < nlsize; ++i)
      {
        final Node aCurrent = aNodeList.item (i);
        aNodes.add (aCurrent);

        _fillListPrefix (aCurrent, aNodes);
      }
    }
  }

  public boolean hasNext ()
  {
    return m_aIter.hasNext ();
  }

  public Node next ()
  {
    return m_aIter.next ();
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public Iterator <Node> iterator ()
  {
    return this;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("iter", m_aIter).toString ();
  }
}
