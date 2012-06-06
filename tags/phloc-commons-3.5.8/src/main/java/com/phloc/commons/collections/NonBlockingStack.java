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
package com.phloc.commons.collections;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ICloneable;
import com.phloc.commons.IHasSize;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A version of a stack that does not use {@link java.util.Vector} but an
 * {@link ArrayList} as the underlying data structure as opposed to
 * {@link java.util.Stack}. This spares us from unnecessary synchronization.
 * 
 * @author philip
 * @param <ELEMENTTYPE>
 *        The type of the elements contained in the stack
 */
@NotThreadSafe
public class NonBlockingStack <ELEMENTTYPE> extends AbstractCollection <ELEMENTTYPE> implements
                                                                                    IHasSize,
                                                                                    Serializable,
                                                                                    ICloneable <NonBlockingStack <ELEMENTTYPE>>
{
  private final List <ELEMENTTYPE> m_aList = new ArrayList <ELEMENTTYPE> ();

  public NonBlockingStack ()
  {}

  public NonBlockingStack (@Nullable final ELEMENTTYPE... aElements)
  {
    ContainerHelper.getConcatenatedInline (m_aList, aElements);
  }

  public NonBlockingStack (@Nullable final Collection <? extends ELEMENTTYPE> aCollection)
  {
    if (aCollection != null)
      m_aList.addAll (aCollection);
  }

  public NonBlockingStack (@Nullable final NonBlockingStack <? extends ELEMENTTYPE> aStack)
  {
    if (aStack != null)
      m_aList.addAll (aStack.m_aList);
  }

  /**
   * Pushes an item onto the top of this stack.
   * 
   * @param aItem
   *        the item to be pushed onto this stack.
   * @return the <code>aItem</code> argument.
   */
  @Nullable
  public ELEMENTTYPE push (@Nullable final ELEMENTTYPE aItem)
  {
    m_aList.add (aItem);
    return aItem;
  }

  /**
   * Removes the object at the top of this stack and returns that object as the
   * value of this function.
   * 
   * @return The object at the top of this stack (the last item of the list).
   * @exception EmptyStackException
   *            if this stack is empty.
   */
  @Nullable
  public ELEMENTTYPE pop ()
  {
    if (m_aList.isEmpty ())
      throw new EmptyStackException ();
    return m_aList.remove (m_aList.size () - 1);
  }

  /**
   * Looks at the object at the top of this stack without removing it from the
   * stack.
   * 
   * @return the object at the top of this stack (the last item of the list).
   * @exception EmptyStackException
   *            if this stack is empty.
   */
  @Nullable
  public ELEMENTTYPE peek ()
  {
    if (m_aList.isEmpty ())
      throw new EmptyStackException ();
    return m_aList.get (m_aList.size () - 1);
  }

  @Override
  public boolean addAll (@Nonnull final Collection <? extends ELEMENTTYPE> aCont)
  {
    return m_aList.addAll (aCont);
  }

  /**
   * Tests if this stack is empty.
   * 
   * @return <code>true</code> if and only if this stack contains no items;
   *         <code>false</code> otherwise.
   */
  @Override
  public boolean isEmpty ()
  {
    return m_aList.isEmpty ();
  }

  @Override
  @Nonnegative
  public int size ()
  {
    return m_aList.size ();
  }

  @Override
  @Nonnull
  public Iterator <ELEMENTTYPE> iterator ()
  {
    return m_aList.iterator ();
  }

  @Nullable
  public ELEMENTTYPE firstElement ()
  {
    if (m_aList.isEmpty ())
      throw new EmptyStackException ();
    return m_aList.get (0);
  }

  @Override
  public void clear ()
  {
    m_aList.clear ();
  }

  @Override
  public boolean contains (@Nullable final Object aElement)
  {
    return m_aList.contains (aElement);
  }

  @Nonnull
  public NonBlockingStack <ELEMENTTYPE> getClone ()
  {
    return new NonBlockingStack <ELEMENTTYPE> (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final NonBlockingStack <?> rhs = (NonBlockingStack <?>) o;
    return m_aList.equals (rhs.m_aList);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aList).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("list", m_aList).toString ();
  }

  @Nonnull
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> create ()
  {
    return new NonBlockingStack <ELEMENTTYPE> ();
  }

  @Nonnull
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> create (@Nullable final ELEMENTTYPE... aElements)
  {
    return new NonBlockingStack <ELEMENTTYPE> (aElements);
  }

  @Nonnull
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> create (@Nullable final Collection <? extends ELEMENTTYPE> aCont)
  {
    return new NonBlockingStack <ELEMENTTYPE> (aCont);
  }

  @Nonnull
  public static <ELEMENTTYPE> NonBlockingStack <ELEMENTTYPE> create (@Nullable final NonBlockingStack <? extends ELEMENTTYPE> aStack)
  {
    return new NonBlockingStack <ELEMENTTYPE> (aStack);
  }
}
