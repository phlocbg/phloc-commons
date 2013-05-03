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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.iterate.IIterableIterator;
import com.phloc.commons.microdom.IMicroNode;

/**
 * Class for recursively visiting all elements of a micro node
 * 
 * @author Philip Helger
 */
public class MicroRecursiveIterator implements IIterableIterator <IMicroNode>
{
  private final List <IMicroNode> m_aOpen = new ArrayList <IMicroNode> ();

  public MicroRecursiveIterator (@Nonnull final IMicroNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    m_aOpen.add (aNode);
  }

  public boolean hasNext ()
  {
    return !m_aOpen.isEmpty ();
  }

  public IMicroNode next ()
  {
    if (m_aOpen.isEmpty ())
      throw new NoSuchElementException ();

    final IMicroNode ret = m_aOpen.remove (0);
    if (ret.hasChildren ())
      m_aOpen.addAll (0, ret.getChildren ());
    return ret;
  }

  @UnsupportedOperation
  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  public Iterator <IMicroNode> iterator ()
  {
    return this;
  }
}
