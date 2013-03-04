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
package com.phloc.commons.idfactory;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This implementation of {@link IIntIDFactory} reads IDs from a device. It does
 * it by reserving a range of <em>n</em> IDs so that not each ID reservation
 * requires IO. If only 1 ID is effectively used, the other <em>n</em>-1 IDs are
 * lost and will never be assigned to any object again.
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractPersistingIntIDFactory implements IIntIDFactory
{
  private final int m_nReserveCount;
  private int m_nID = 0;
  private int m_nLastID = -1;

  public AbstractPersistingIntIDFactory (@Nonnegative final int nReserveCount)
  {
    if (nReserveCount < 1)
      throw new IllegalArgumentException ("reserve count illegal: " + nReserveCount);
    m_nReserveCount = nReserveCount;
  }

  @Nonnegative
  protected final int getReserveCount ()
  {
    return m_nReserveCount;
  }

  /**
   * Read the current ID from the device. In case the method is called for a
   * non-initialized device, 0 should be returned.<br>
   * The update should write the read value plus the passed reserve count back
   * to the device. This method should perform an atomic read and update to
   * avoid that ID can be reused.<br>
   * Pseudo code:
   * 
   * <pre>
   * protected int readAndUpdateIDCounter (int nReserveCount)
   * {
   *   final int nRead = FileIO.read (file);
   *   FileIO.write (file, nRead + nReserveCount);
   *   return nRead;
   * }
   * </pre>
   * 
   * @param nReserveCount
   *        the number that should be added to the read value. Always &gt; 0.
   * @return 0 if this method is called for a non-initialized device, the value
   *         read from the device otherwise or
   *         {@link com.phloc.commons.CGlobal#ILLEGAL_UINT} in case of an error.
   */
  protected abstract int readAndUpdateIDCounter (@Nonnegative int nReserveCount);

  private void _updateIDRange ()
  {
    final int nID = readAndUpdateIDCounter (m_nReserveCount);

    // the existing ID may not be < than the previously used ID!
    if (m_nLastID >= 0 && nID < m_nID)
      throw new IllegalStateException ("The read value " + nID + " is smaller than the last known ID " + m_nID + "!");

    m_nID = nID;
    m_nLastID = nID + m_nReserveCount;
  }

  /*
   * Note: this implementation is not synchronized because the method calling
   * this method must be synchronized.
   * @see com.phloc.commons.id.IIntIDProvider#getNewID()
   */
  public final int getNewID ()
  {
    if (m_nID >= m_nLastID)
      _updateIDRange ();
    return m_nID++;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractPersistingIntIDFactory rhs = (AbstractPersistingIntIDFactory) o;
    return m_nReserveCount == rhs.m_nReserveCount && m_nID == rhs.m_nID;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nReserveCount).append (m_nID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("reserveCount", m_nReserveCount)
                                       .append ("ID", m_nID)
                                       .append ("lastID", m_nLastID)
                                       .toString ();
  }
}