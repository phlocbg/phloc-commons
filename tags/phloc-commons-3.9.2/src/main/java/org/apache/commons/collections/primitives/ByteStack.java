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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.primitives;

import java.util.EmptyStackException;

import javax.annotation.Nonnegative;

/**
 * A primitive byte based Stack. The underlying backing store is an
 * ArrayByteList where the front of the list is the bottom of the stack and
 * the tail of the list is the top of the stack.
 *
 * @author Apache Directory Project
 * @since Commons Primitives 1.1
 * @version $Revision: 480460 $ $Date: 2006-11-29 09:14:21 +0100 (Mi, 29 Nov
 *          2006) $
 */
public class ByteStack
{
  /** the underlying dynamic primitive backing store */
  private final ArrayByteList m_aList = new ArrayByteList ();

  /**
   * Creates an empty primitive stack.
   */
  public ByteStack ()
  {}

  /**
   * Creates a stack prepopulating it with values.
   *
   * @param aElements
   *        the array to add
   */
  public ByteStack (final byte [] aElements)
  {
    for (final byte aElement : aElements)
      m_aList.add (aElement);
  }

  /**
   * Tests if this stack is empty.
   *
   * @return true if and only if this stack is empty; false otherwise
   * @deprecated Use #isEmpty() instead
   */
  @Deprecated
  public boolean empty ()
  {
    return isEmpty ();
  }

  /**
   * Tests if this stack is empty.
   *
   * @return true if and only if this stack is empty; false otherwise
   */
  public boolean isEmpty ()
  {
    return m_aList.isEmpty ();
  }

  /**
   * Looks at the top of this stack without removing it.
   *
   * @return the value at the top of this stack
   * @throws java.util.EmptyStackException
   *         if this stack is empty
   */
  public byte peek ()
  {
    if (m_aList.isEmpty ())
      throw new EmptyStackException ();

    return m_aList.get (m_aList.size () - 1);
  }

  /**
   * Return the n'th byte down the stack, where 0 is the top element and
   * [size()-1] is the bottom element.
   *
   * @param n
   *        the element index
   * @return the element at the index
   * @throws EmptyStackException
   *         if the stack is empty
   * @throws IndexOutOfBoundsException
   *         if the index is out of bounds
   */
  public byte peek (final int n)
  {
    if (m_aList.isEmpty ())
      throw new EmptyStackException ();

    return m_aList.get (m_aList.size () - n - 1);
  }

  /**
   * Removes the value at the top of this stack and returns it.
   *
   * @return value at the top of this stack
   * @throws java.util.EmptyStackException
   *         if this stack is empty
   */
  public byte pop ()
  {
    if (m_aList.isEmpty ())
      throw new EmptyStackException ();

    return m_aList.removeElementAt (m_aList.size () - 1);
  }

  /**
   * Pushes a value onto the top of this stack.
   *
   * @param item
   *        the value to push onto this stack
   * @return the item argument for call chaining
   */
  public byte push (final byte item)
  {
    m_aList.add (item);
    return item;
  }

  /**
   * Returns the 1-based position where a value is on this stack. If the value
   * occurs as an item in this stack, this method returns the distance from the
   * top of the stack of the occurrence nearest the top of the stack; the
   * topmost item on the stack is considered to be at distance 1.
   *
   * @param item
   *        the value to search for from the top down
   * @return the 1-based position from the top of the stack where the int is
   *         located; the return value -1 indicates that the int is not on the
   *         stack
   */
  public int search (final byte item)
  {
    for (int i = m_aList.size () - 1; i >= 0; i--)
      if (m_aList.get (i) == item)
        return m_aList.size () - i;
    return -1;
  }

  /**
   * Gets items from the stack where the index is zero based and the top of the
   * stack is at an index of size()-1 with the bottom of the stack at an index
   * of 0.
   *
   * @param index
   *        the index into the stack treated as a list
   * @return the value at the index
   */
  public byte get (final int index)
  {
    return m_aList.get (index);
  }

  /**
   * Gets the size of this stack.
   *
   * @return the size of this stack
   */
  @Nonnegative
  public int size ()
  {
    return m_aList.size ();
  }

  /**
   * Empties the contents of the stack.
   */
  public void clear ()
  {
    m_aList.clear ();
  }
}
